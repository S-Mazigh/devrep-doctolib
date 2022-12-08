package devrep.projet.devmed.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

@Service
public class AppointmentService {
    
    public String createHoraires(Map<String, String> params) {
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
}
