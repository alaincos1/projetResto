package fr.ul.miage.projetResto.appinfo;

import fr.ul.miage.projetResto.constants.MealType;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Service {
    private MealType mealType;
    private String date;
}
