package fr.ul.miage.projetResto;

import ch.qos.logback.classic.LoggerContext;
import fr.ul.miage.projetResto.appinfo.Service;
import fr.ul.miage.projetResto.constants.MealType;
import fr.ul.miage.projetResto.controller.feature.StartController;
import fr.ul.miage.projetResto.dao.InsertData;
import fr.ul.miage.projetResto.dao.repository.*;
import fr.ul.miage.projetResto.dao.service.BaseService;
import fr.ul.miage.projetResto.model.entity.*;
import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.Level;

public class Launcher {
    private static Service service;
    private static UserEntity loggedUser;
    public static BaseService baseService;

    public static void main(String[] args){
        ((LoggerContext) LoggerFactory.getILoggerFactory()).getLogger("org.mongodb.driver").setLevel(Level.OFF);
        initBaseService();
        InsertData.feedData();
        StartController startController = new StartController();
        startController.launch();
    }

    private static void initBaseService() {
        baseService = new BaseService(new BillCollection(),
                new BookingCollection(),
                new DishCollection(),
                new MenuCollection(),
                new OrderCollection(),
                new PerformanceCollection(),
                new ProductCollection(),
                new TableCollection(),
                new UserCollection());
    }

    public static void setService(MealType mealtype, String date){
        service = new Service(mealtype, date);
    }

    public static Service getService() {
        return service;
    }

    public static void setLoggedUser(UserEntity user){
        loggedUser = user;
    }

    public static UserEntity getLoggedUser() {
        return loggedUser;
    }
}
