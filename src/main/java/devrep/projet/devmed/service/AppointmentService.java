package devrep.projet.devmed.service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.datetime.joda.DateTimeFormatterFactory;
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
    public void addRdv(Long idPatient, Long idPro, String date)
    {
        RendezVous toAdd = new RendezVous();
        toAdd.setPatient(UtilisateurBD.getReferenceById(idPatient));
        toAdd.setPro(UtilisateurBD.getReferenceById(idPro));
        // format string before ?
        toAdd.setDaterdv(new Date(LocalDate.parse(date).toEpochDay())); // parse le string en date puis get les millisecondes
        System.out.println("Appointement to add: "+toAdd);
        RdvBD.save(toAdd);
    }

    // Pratiquement que des statiques qui sert à la manipulation des dates pour la vue et la sauvegarde.

    // Crée la string à stoqué
    public static String createHoraires(Map<String, String> params) {
        String[] days = {"Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi"};
        StringBuffer horaires = new StringBuffer();
        for(String d:days) {
            if(!d.equals("Vendredi"))
                horaires.append(d+"["+params.get(d+"Open")+">"+params.get(d+"Close")+"]&");
            else
            horaires.append(d+"["+params.get(d+"Open")+">"+params.get(d+"Close")+"]");
        }
        System.out.println(params+" => "+horaires);
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
                //System.out.println(str+": "+matcher.matches());
                if (matcher.matches())
                    toReturn.add(new String[]{matcher.group(1),matcher.group(2)});
                    //System.out.println(matcher.group(1) + " " + matcher.group(2));
            }
        }
        return toReturn;
    }

    // Pour récupérer les dates des prochains jours ouvrables
    public static Map<String,String> getThisWeek()
    {
        // get today date
        Calendar now = Calendar.getInstance();
        SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat day = new SimpleDateFormat("E");
        Map<String, String> thisweek = new HashMap<>();
        Date next;
        for(int i=0; i<7; i++) { // le but est d'avoir les 5 prochains jours ouvrables.
            if(now.get(Calendar.DAY_OF_WEEK)!=Calendar.SUNDAY && now.get(Calendar.DAY_OF_WEEK)!=Calendar.SATURDAY)
            {
                next = now.getTime();
                thisweek.put(day.format(next), date.format(next));
            }
            now.add(Calendar.DAY_OF_WEEK, 1);
        }
        return thisweek;
    }

    // Pour récupérer les disponibilités pour les cinq prochain jours ouvrables et les afficher avec thymeleaf
    public List<Map<Date, Boolean>> getDisponibilities(String horaires) {
        Map<String, String> nextFiveDays = getThisWeek();
        List<String[]> mesHoraires = formatHoraires(horaires);
        DateTimeFormatter myFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        
        Map<Date, Boolean> disponibilitiesInADay;
        List<Map<Date, Boolean>> disponibilitiesForFiveDays = new ArrayList<>();
        
        Date openTime, closeTime;
        long rdvDuration = (3600*1000); // une heure pour tous pour l'instant
        int i = 0; // pour la liste des horaires
        String[] weekDays = new String[]{"Mon","Tue","Wed","Thu","Fri"};
        System.out.println("Day keys: "+nextFiveDays.keySet());
        for(String day: weekDays) {
            // borner avec les horaires
            // Je parse la date "dd/MM/yyyy"+" "+"HeureOuverture"
            disponibilitiesInADay = new HashMap<>();
            openTime = new Date(LocalDate.parse(nextFiveDays.get(day)+" "+mesHoraires.get(i)[0], myFormat).toEpochDay());
            closeTime = new Date(LocalDate.parse(nextFiveDays.get(day)+" "+mesHoraires.get(i)[1], myFormat).toEpochDay());
            for(Date d=openTime; !d.equals(closeTime); d = new Date(d.getTime()+rdvDuration)) {
                disponibilitiesInADay.put(d, !RdvBD.findByDaterdv(d).isEmpty());
            }
            disponibilitiesForFiveDays.add(disponibilitiesInADay);
        }
        return disponibilitiesForFiveDays;
    }

    // Liste de rendezvous par heure avec un boolean si pris
}
