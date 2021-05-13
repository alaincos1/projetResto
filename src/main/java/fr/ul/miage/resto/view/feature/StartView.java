package fr.ul.miage.resto.view.feature;

import fr.ul.miage.resto.appinfo.Service;
import fr.ul.miage.resto.constants.MealType;

public class StartView {

    public void displayMealType() {
        System.out.println("Veuillez choisir le type de service :");
        int i = 1;
        for (MealType mealType : MealType.values()) {
            System.out.println(i++ + ") " + mealType.getMealValue());
        }
    }

    public void displayAskDate() {
        System.out.println("Veuillez choisir la date du service au format  \"AAAA/MM/JJ\" :");
    }

    public void displayService(Service service) {
        System.out.println("Service du jour : " + service.getMealType().getMealValue() + " " + service.getDate());
    }
}
