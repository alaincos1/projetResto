package fr.ul.miage.projetResto;

import fr.ul.miage.projetResto.appinfo.Service;
import fr.ul.miage.projetResto.constants.MealType;
import fr.ul.miage.projetResto.controller.feature.StartController;
import fr.ul.miage.projetResto.model.User;

public class Launcher {
    private static Service service;
    private static User loggedUser;

    public static void main(String[] args) {
        StartController startController = new StartController();
        startController.launch();
    }

    public static void setService(MealType mealtype, String date){
        service = new Service(mealtype, date);
    }

    public static Service getService() {
        return service;
    }

    public static void setLoggedUser(User user){
        loggedUser = user;
    }

    public static User getLoggedUser() {
        return loggedUser;
    }
}
