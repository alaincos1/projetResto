package fr.ul.miage.projetResto.controller.feature;

import fr.ul.miage.projetResto.Launcher;
import fr.ul.miage.projetResto.constants.MealType;
import fr.ul.miage.projetResto.utils.InputUtil;
import fr.ul.miage.projetResto.view.feature.StartView;

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
        Integer input = InputUtil.getIntegerInput(1, 2);
        mealType = MealType.values()[input - 1];
    }

    public void askMealDate() {
        startView.displayDate();
        date = InputUtil.getDateInput();
    }

    public void createService() {
        Launcher.setService(mealType, date);
        startView.displayService();
    }

    public void launchLoginView() {
        LogInController logInController = new LogInController();
        logInController.launch();
    }
}
