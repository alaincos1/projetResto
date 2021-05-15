package fr.ul.miage.resto;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import fr.ul.miage.resto.appinfo.Service;
import fr.ul.miage.resto.constants.MealType;
import fr.ul.miage.resto.controller.feature.StartController;
import fr.ul.miage.resto.dao.repository.*;
import fr.ul.miage.resto.dao.service.BaseService;
import fr.ul.miage.resto.model.entity.UserEntity;
import fr.ul.miage.resto.view.feature.StartView;
import org.slf4j.LoggerFactory;

public class Launcher {
    private static BaseService baseService;
    private static Service service;
    private static UserEntity loggedUser;

    public static void main(String[] args) {
        ((LoggerContext) LoggerFactory.getILoggerFactory()).getLogger("org.mongodb.driver").setLevel(Level.OFF);
        initBaseService();
        StartController startController = new StartController(baseService, service, new StartView());
        startController.launch();
    }

    protected static void initBaseService() {
        baseService = new BaseService(new BillCollection(),
                new BookingCollection(),
                new DishCollection(),
                new CategoryCollection(),
                new OrderCollection(),
                new PerformanceCollection(),
                new ProductCollection(),
                new TableCollection(),
                new UserCollection());
    }

    public static void setService(MealType mealtype, String date) {
        service = new Service(mealtype, date);
    }

    public static Service getService() {
        return service;
    }

    public static UserEntity getLoggedUser() {
        return loggedUser;
    }

    public static void setLoggedUser(UserEntity user) {
        loggedUser = user;
    }

    public static BaseService getBaseService() {
        return baseService;
    }
}
