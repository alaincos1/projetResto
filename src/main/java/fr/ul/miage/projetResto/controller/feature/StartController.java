package fr.ul.miage.projetResto.controller.feature;

import fr.ul.miage.projetResto.appinfo.Service;
import fr.ul.miage.projetResto.constants.MealType;
import fr.ul.miage.projetResto.dao.InitRestaurant;
import fr.ul.miage.projetResto.dao.service.BaseService;
import fr.ul.miage.projetResto.utils.InputUtil;
import fr.ul.miage.projetResto.view.feature.StartView;

public class StartController {
    private BaseService baseService;
    private Service service;
    StartView startView = new StartView();
    private InitRestaurant initRestaurant;
    private MealType mealType;
    private String date;

    public StartController(BaseService baseService, Service service){
        this.baseService = baseService;
        this.service = service;
    }

    public void launch() {
        askMealType();
        askMealDate();
        createService();
        initRestaurant = new InitRestaurant(service, baseService);
        initRestaurant.initRestaurant();
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
        service.setMealType(mealType);
        service.setDate(date);
        startView.displayService(service);
    }

    public void launchLoginView() {
        LogInController logInController = new LogInController(baseService, service);
        logInController.launch();
    }
}
