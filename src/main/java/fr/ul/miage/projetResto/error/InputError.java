package fr.ul.miage.projetResto.error;

import fr.ul.miage.projetResto.constants.InfoRestaurant;
import org.apache.commons.lang3.StringUtils;

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
        Date d;
        try {
            d = format.parse(date);
        } catch (ParseException e) {
            return null;
        }

        Date auj = new Date();        
        if (d.before(auj) && !format.format(auj).equals(format.format(d)) ) {
        	System.out.println("La date ne peut pas être dans le passé.");
        	return null;
        }
        return format.format(d);
    }

    public static String checkStringCommand(String command, Integer borneMin, Integer borneMax) {
        if (command.charAt(0) != '-') {
            return null;
        }

        String selection = command.substring(2);
        String[] listSelection = selection.replace(" ", "").split("/");
        String action = String.valueOf(command.charAt(1));
        switch (action) {
            case "v":
                if (command.replace(" ", "").length() > 2) {
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
        return command;
    }

    public static String checkString(String string) {
        if (string.length() > InfoRestaurant.MAX_LENGTH_NAME.getValue() || StringUtils.isBlank(string)) {
            return null;
        }
        return string;
    }

    public static String checkStringMultipleChoices(String command, Integer borneMin, Integer borneMax) {
        if (StringUtils.isBlank(command)) {
            return null;
        }

        String selection = command.replace(" ", "");
        String[] listSelection = selection.split("/");
        if (listSelection.length > InfoRestaurant.MAX_CHOICES.getValue()) {
            return null;
        }
        for (String part : listSelection) {
            if (checkInteger(part, borneMin, borneMax) == null) {
                return null;
            }
        }
        return command;
    }

}
