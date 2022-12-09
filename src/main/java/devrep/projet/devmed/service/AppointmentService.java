package devrep.projet.devmed.service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import devrep.projet.devmed.entities.RendezVous;
import devrep.projet.devmed.repository.AppointmentRepository;
import devrep.projet.devmed.repository.UtilisateurRepository;

@Service
public class AppointmentService {

    @Autowired
    private UtilisateurRepository UtilisateurBD;

    @Autowired
    private AppointmentRepository RdvBD;

    @Transactional
    public void addRdv(Long idPatient, Long idPro, String date) {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("E dd/MM/yyyy HH:mm");
        RendezVous toAdd = new RendezVous();
        if (idPatient == null || idPro == null || date == null) // ne rien faire si un est null (l'authentification spring bug des fois, la session n'est pas tout a fait cloturée)
            return;
        toAdd.setPatient(UtilisateurBD.getReferenceById(idPatient));
        toAdd.setPro(UtilisateurBD.getReferenceById(idPro));
        // format string before
        toAdd.setDaterdv(Date.from(LocalDateTime.parse(date, dateFormat).toInstant(getZoneOffset())));
        System.out.println("Appointement to add: " + toAdd);
        RdvBD.save(toAdd);
    }

    // Pratiquement que des statiques qui sert à la manipulation des dates pour la
    // vue et la sauvegarde.

    // Crée la string à stoqué
    public static String createHoraires(Map<String, String> params) {
        String[] days = { "Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi" };
        StringBuffer horaires = new StringBuffer();
        for (String d : days) {
            if (!d.equals("Vendredi"))
                horaires.append(d + "[" + params.get(d + "Open") + ">" + params.get(d + "Close") + "]&");
            else
                horaires.append(d + "[" + params.get(d + "Open") + ">" + params.get(d + "Close") + "]");
        }
        System.out.println(params + " => " + horaires);
        return horaires.toString(); // JOUR[Open>Close]&JOUR[Open>Close]&....
    }

    // Convertie la string stoquée en Liste utile à la vue
    public static List<String[]> formatHoraires(String horaires) {
        List<String[]> toReturn = new ArrayList<>();
        if (horaires != null) {
            String[] days = horaires.split("&");
            Pattern hoursPattern = Pattern.compile("\\w+?\\[(\\w+:\\w+)>(\\w+:\\w+)\\]");
            Matcher matcher;
            for (String str : days) {
                matcher = hoursPattern.matcher(str);
                // System.out.println(str+": "+matcher.matches());
                if (matcher.matches())
                    toReturn.add(new String[] { matcher.group(1), matcher.group(2) });
                // System.out.println(matcher.group(1) + " " + matcher.group(2));
            }
        }
        return toReturn;
    }

    // Pour récupérer les dates des prochains jours ouvrables
    public static Map<Integer, String> getThisWeek() {
        // Recuperer le temps actuelle avec la zone geographique du serveur !!
        Calendar now = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        // SimpleDateFormat day = new SimpleDateFormat("E");
        Map<Integer, String> thisweek = new HashMap<>();
        Date next;
        int jour, date;
        for (int i = 0; i < 7; i++) { // le but est d'avoir les 5 prochains jours ouvrables.
        jour = now.get(Calendar.DAY_OF_WEEK); // recupérer la journée 
        date = now.get(Calendar.DAY_OF_MONTH);
            if (jour != Calendar.SUNDAY && jour != Calendar.SATURDAY) { // que les jours ouverables
                next = now.getTime();
                thisweek.put(date, dateFormat.format(next)); // utiliser la date du jour dans le mois pour avoir un ordre croissant.
            }
            now.add(Calendar.DAY_OF_WEEK, 1); // passe  au jour suivant
        }
        return thisweek;
    }

    // L'entité rendez vous ne peut prendre qu'un Date ou Calendar comme attribut, du coup il faut convertir le LocalDateTime en Date
    // Sachez que Date à plus d'information que le LocalDateTime; Date ou calendar comprennent la zone geographique.
    // Pour calculer le zoneOffset du systeme
    public ZoneOffset getZoneOffset() {
        ZoneId myZone = ZoneId.of("Europe/Paris");
        return myZone.getRules().getOffset(LocalDateTime.now());
    }

    // Pour récupérer les disponibilités pour les cinq prochain jours ouvrables et
    // les afficher avec thymeleaf
    public List<Map<String, Boolean>> getDisponibilities(String horaires) {
        Map<Integer, String> nextFiveDays = getThisWeek();
        List<String[]> mesHoraires = formatHoraires(horaires);
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        DateTimeFormatter vueFormat = DateTimeFormatter.ofPattern("E dd/MM/yyyy HH:mm");

        Map<String, Boolean> disponibilitiesInADay;
        List<Map<String, Boolean>> disponibilitiesForFiveDays = new ArrayList<>();

        LocalDateTime openTime, closeTime;
        int i = 0; // pour la liste des horaires
        System.out.println("Day keys: " + nextFiveDays.keySet());
        for (int day : nextFiveDays.keySet()) {
            disponibilitiesInADay = new LinkedHashMap<>();
            // borner avec les horaires
            // Je parse la date "dd/MM/yyyy"+" "+"HeureOuverture"
            /*openTime = Date.from(LocalDateTime.parse(nextFiveDays.get(day) + " " + mesHoraires.get(i)[0], dateFormat)
                    .toInstant(getZoneOffset()));
            closeTime = Date.from(LocalDateTime.parse(nextFiveDays.get(day) + " " + mesHoraires.get(i)[1], dateFormat)
                    .toInstant(getZoneOffset()));*/
            openTime = LocalDateTime.parse(nextFiveDays.get(day) + " " + mesHoraires.get(i)[0], dateFormat);
            closeTime = LocalDateTime.parse(nextFiveDays.get(day) + " " + mesHoraires.get(i)[1], dateFormat);
            System.out.println("open=" + openTime + ", close=" + closeTime);
            for (LocalDateTime d = openTime; !d.equals(closeTime); d = d.plusHours(1)) { // une heure pour tous pour l'instant
                // Malheuresement Date doit être utilisée pour la recherche
                //System.out.println("open=" + openTime + ", d="+d+", close=" + closeTime);
                disponibilitiesInADay.put(vueFormat.format(d), !RdvBD.findByDaterdv(Date.from(d.toInstant(getZoneOffset()))).isEmpty());
                System.out.println("date:"+d+" loop dispo: "+disponibilitiesInADay);
            }
            disponibilitiesForFiveDays.add(disponibilitiesInADay);
            i++;
        }
        System.out.println("dispo : " + disponibilitiesForFiveDays);
        return disponibilitiesForFiveDays;
    }
}
