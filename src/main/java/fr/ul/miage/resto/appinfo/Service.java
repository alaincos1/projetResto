package fr.ul.miage.resto.appinfo;

import fr.ul.miage.resto.constants.MealType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Service {
    private MealType mealType;
    private String date;
    private boolean endNewClients;
    private boolean endService;

    public Service(MealType mealType, String date) {
        this.mealType = mealType;
        this.date = date;
        this.endNewClients = false;
        this.endService = false;
    }
}
