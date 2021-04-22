package fr.ul.miage.projetResto.controller.feature;

import fr.ul.miage.projetResto.Launcher;
import fr.ul.miage.projetResto.constants.MealType;
import fr.ul.miage.projetResto.error.InputError;
import fr.ul.miage.projetResto.utils.InputUtil;
import fr.ul.miage.projetResto.view.feature.StartView;
import org.apache.commons.lang3.StringUtils;

public class StartController {
    StartView startView = new StartView();
    private MealType mealType;
    private String date;

    public void launch() {
        askMealType();
        askMealDate();
        createService();
        launchLoginView();
    }

    public void askMealType() {
        startView.displayMealType();
        Integer input = InputError.checkInteger(InputUtil.getUserInput(), 1, 2);
        while(input == null) {
            System.out.println("Problème de saisie, veuillez recommencer.");
            input = InputError.checkInteger(InputUtil.getUserInput(), 1, 2);
        }
        mealType = MealType.getFromId(input);
    }

    public void askMealDate() {
        startView.displayDate();
        String input = InputError.checkDate(InputUtil.getUserInput());
        while(StringUtils.isBlank(input)) {
            System.out.println("Problème de saisie, veuillez recommencer.");
            input = InputError.checkDate(InputUtil.getUserInput());
        }
        date = input;
    }

    public void createService(){
        Launcher.setService(mealType, date);
        startView.displayService();
    }

    public void launchLoginView() {
        LogInController logInController = new LogInController();
        logInController.launch();
    }
}
