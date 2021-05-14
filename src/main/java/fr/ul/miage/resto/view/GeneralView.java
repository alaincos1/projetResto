package fr.ul.miage.resto.view;

import java.util.List;

public class GeneralView {
    public void displayMessage(String message) {
        System.out.println(message);
    }

    public void displayChoice(List<String> choice, Integer startIndex, boolean withIndex) {
        for (String display : choice) {
            if (withIndex) {
                displayMessage(" " + startIndex++ + ") ");
            }
            displayMessage(display);
        }
    }
}
