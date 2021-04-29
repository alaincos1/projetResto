package fr.ul.miage.projetResto.utils;


import fr.ul.miage.projetResto.error.InputError;
import org.apache.commons.lang3.StringUtils;

import java.util.Scanner;

public class InputUtil {
    private static final Scanner scanner = new Scanner(System.in);

    private static String getUserInput() {
        return scanner.nextLine();
    }

    public static void closeScanner() {
        scanner.close();
    }

    public static Integer getIntegerInput(Integer min, Integer max) {
        Integer input = InputError.checkInteger(getUserInput(), min, max);
        while (input == null) {
            System.out.println("Problème de saisie, veuillez recommencer.");
            input = InputError.checkInteger(getUserInput(), min, max);
        }
        return input;
    }

    public static String getDateInput() {
        String input = InputError.checkDate(getUserInput());
        while (StringUtils.isBlank(input)) {
            System.out.println("Problème de saisie, veuillez recommencer.");
            input = InputError.checkDate(getUserInput());
        }
        return input;
    }

    public static String getUserIdInput() {
        String input = InputError.checkUserId(getUserInput());
        while (StringUtils.isBlank(input)) {
            System.out.println("Problème de saisie, veuillez recommencer.");
            input = InputError.checkUserId(getUserInput());

        }
        return input;
    }

    public static String getStringCommandInput(Integer min, Integer max) {
        String input = InputError.checkStringCommand(getUserInput(), min, max);
        while (StringUtils.isBlank(input)) {
            System.out.println("Problème de saisie, veuillez recommencer.");
            input = InputError.checkStringCommand(getUserInput(), min, max);

        }
        return input;
    }

    public static String getStringInput() {
        String input = InputError.checkString(getUserInput());
        while (StringUtils.isBlank(input)) {
            System.out.println("Problème de saisie (texte trop long), veuillez recommencer.");
            input = InputError.checkString(getUserInput());
        }
        return input;
    }

    public static String getStringMultipleChoices(Integer min, Integer max) {
        String input = InputError.checkStringMultipleChoices(getUserInput(), min, max);
        while (StringUtils.isBlank(input)) {
            System.out.println("Problème de saisie, veuillez recommencer.");
            input = InputError.checkStringMultipleChoices(getUserInput(), min, max);

        }
        return input;
    }
}
