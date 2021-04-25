package fr.ul.miage.projetResto.view.feature;

import fr.ul.miage.projetResto.Launcher;
import fr.ul.miage.projetResto.constants.MealType;

public class StartView {

    public void displayMealType() {
        System.out.println("Veuillez choisir le type de service :");
        for (MealType mealType : MealType.values()) {
            System.out.println(mealType.getId() + ") " + mealType);
        }
    }

    public void displayDate() {
        System.out.println("Veuillez choisir la date du service au format  \"AAAA/MM/JJ\" :");
    }

    public void displayService() {
        System.out.println("Service du jour : " + Launcher.getService().getMealType() + " " + Launcher.getService().getDate());
    }
}
