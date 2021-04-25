package fr.ul.miage.projetResto.error;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputError {

    public static Integer checkInteger(String nb, Integer borneMin, Integer borneMax) {
        Integer toReturn;
        try {
            toReturn = Integer.parseInt(nb);

        } catch (Exception e) {
            return null;
        }

        if (toReturn < borneMin || toReturn > borneMax) {
            return null;
        }
        return toReturn;
    }

    public static String checkUserId(String userId) {
        String regExpression = "[a-zA-Z_0-9]*";
        Pattern p = Pattern.compile(regExpression);
        Matcher matcher = p.matcher(userId);

        if (!matcher.matches())
            return null;

        return userId;
    }

    public static String checkDate(String date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        format.setLenient(false);
        try {
            Date d = format.parse(date);
        } catch (ParseException e) {
            return null;
        }
        return date;
    }

    public static String checkStringCommande(String commande, Integer borneMin, Integer borneMax) {
        if (commande.charAt(0) != '-') {
            return null;
        }


        String selection = commande.substring(2);
        String[] listSelection = selection.replace(" ", "").split("/");
        String action = String.valueOf(commande.charAt(1));
        switch (action) {
            case "v":
                if (commande.replace(" ", "").length() > 2) {
                    return null;
                }
                break;
            case "a":
            case "e":
            case "d":
                for (String part : listSelection) {
                    if (checkInteger(part, borneMin, borneMax) == null) {
                        return null;
                    }
                }
                break;
            default:
                return null;
        }
        return commande;
    }

}
