package fr.ul.miage.resto.controller.feature;

import fr.ul.miage.resto.appinfo.Service;
import fr.ul.miage.resto.constants.MealType;
import fr.ul.miage.resto.dao.InitRestaurant;
import fr.ul.miage.resto.dao.service.BaseService;
import fr.ul.miage.resto.utils.InputUtil;
import fr.ul.miage.resto.view.feature.LogInView;
import fr.ul.miage.resto.view.feature.StartView;
import lombok.Data;

@Data
public class StartController {
    private final BaseService baseService;
    StartView startView;
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
        launchLogin();
    }

    public void askMealType() {
        startView.displayMealType();
        Integer input = getIntegerInput(1, 2);
        mealType = MealType.values()[input - 1];
    }

    public void askMealDate() {
        startView.displayAskDate();
        date = getDateInput();
    }

    public void createService() {
        service = new Service();
        service.setMealType(mealType);
        service.setDate(date);
        startView.displayService(service);
    }

    public void launchLogin() {
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
