package fr.ul.miage.resto.view;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class GeneralView {
    public void displayMessage(String message) {
        System.out.println(message);
    }

    public void displayChoice(List<String> choice, Integer startIndex, boolean withIndex) {
        for (String display : choice) {
            String toDisplay = StringUtils.EMPTY;
            if (withIndex) {
                toDisplay = (" " + startIndex++ + ") ");
            }
            displayMessage(toDisplay + display);
        }
    }
}
