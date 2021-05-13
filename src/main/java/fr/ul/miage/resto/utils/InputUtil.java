package fr.ul.miage.resto.utils;

import fr.ul.miage.resto.error.InputError;
import fr.ul.miage.resto.view.feature.InputUtilView;
import org.apache.commons.lang3.StringUtils;

import java.util.Scanner;

public class InputUtil {
    private static final Scanner scanner = new Scanner(System.in);

    private InputUtil() {
        throw new IllegalStateException("Utility class");
    }

    private static String getUserInput() {
        return scanner.nextLine();
    }

    public static Integer getIntegerInput(Integer min, Integer max) {
        Integer input = InputError.checkInteger(getUserInput(), min, max);
        while (input == null) {
            displayInputIssue();
            input = InputError.checkInteger(getUserInput(), min, max);
        }
        return input;
    }

    public static String getDateInput() {
        String input = InputError.checkDate(getUserInput());
        while (StringUtils.isBlank(input)) {
            displayInputIssue();
            input = InputError.checkDate(getUserInput());
        }
        return input;
    }

    public static String getUserIdInput() {
        String input = InputError.checkUserId(getUserInput());
        while (StringUtils.isBlank(input)) {
            displayInputIssue();
            input = InputError.checkUserId(getUserInput());

        }
        return input;
    }

    public static String getStringCommandInput(Integer min, Integer max) {
        String input = InputError.checkStringCommand(getUserInput(), min, max);
        while (StringUtils.isBlank(input)) {
            displayInputIssue();
            input = InputError.checkStringCommand(getUserInput(), min, max);

        }
        return input;
    }

    public static String getStringInput() {
        String input = InputError.checkString(getUserInput());
        while (StringUtils.isBlank(input)) {
            displayInputIssue();
            input = InputError.checkString(getUserInput());
        }
        return input;
    }

    public static String getStringMultipleChoices(Integer min, Integer max) {
        String input = InputError.checkStringMultipleChoices(getUserInput(), min, max);
        while (StringUtils.isBlank(input)) {
            displayInputIssue();
            input = InputError.checkStringMultipleChoices(getUserInput(), min, max);

        }
        return input;
    }

    private static void displayInputIssue() {
        InputUtilView inputUtilView = new InputUtilView();
        inputUtilView.displayInputIssue();
    }
}
