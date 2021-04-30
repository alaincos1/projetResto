package fr.ul.miage.projetResto.controller.feature;

import fr.ul.miage.projetResto.appinfo.Service;
import fr.ul.miage.projetResto.constants.MealType;
import fr.ul.miage.projetResto.dao.InitRestaurant;
import fr.ul.miage.projetResto.dao.service.BaseService;
import fr.ul.miage.projetResto.utils.InputUtil;
import fr.ul.miage.projetResto.view.feature.LogInView;
import fr.ul.miage.projetResto.view.feature.StartView;

public class StartController {
    StartView startView;
    private final BaseService baseService;
    private Service service;
    private InitRestaurant initRestaurant;
    private MealType mealType;
    private String date;

    public StartController(BaseService baseService, Service service, StartView startView) {
        this.baseService = baseService;
        this.service = service;
        this.startView = startView;
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
        Integer input = getIntegerInput(1, 2);
        mealType = MealType.values()[input - 1];
    }

    public void askMealDate() {
        startView.displayDate();
        date = getDateInput();
    }

    public void createService() {
        service = new Service();
        service.setMealType(mealType);
        service.setDate(date);
        startView.displayService(service);
    }

    public void launchLoginView() {
        LogInController logInController = new LogInController(baseService, service, new LogInView());
        logInController.launch();
    }

    public Integer getIntegerInput(Integer min, Integer max) {
        return InputUtil.getIntegerInput(min, max);
    }

    public String getDateInput() {
        return InputUtil.getDateInput();
    }
}
