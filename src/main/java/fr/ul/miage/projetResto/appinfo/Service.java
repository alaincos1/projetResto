package fr.ul.miage.projetResto.appinfo;

import fr.ul.miage.projetResto.constants.MealType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Service {
    private MealType mealType;
    private String date;
    private boolean endNewClients;

    public Service(MealType mealType, String date) {
        this.mealType = mealType;
        this.date = date;
        this.endNewClients = false;
    }
}
