package fr.ul.miage.resto.view.feature;

import fr.ul.miage.resto.constants.MealType;
import fr.ul.miage.resto.view.GeneralView;

import java.util.ArrayList;
import java.util.List;

public class StartView extends GeneralView {
    public void displayMealType() {
        displayMessage("Veuillez choisir le type de service :");
        List<String> choices = new ArrayList<>();
        for (MealType mealType : MealType.values()) {
            choices.add(mealType.getMealValue());
        }
        displayChoice(choices, 1, true);
    }
}
