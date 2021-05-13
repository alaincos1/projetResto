package fr.ul.miage.resto.model.entity;

import fr.ul.miage.resto.constants.MealType;
import lombok.Data;

import java.util.List;

@Data
public class BillEntity {
    private String _id;
    private Integer totalPrice;
    private String date;
    private MealType mealType;
    private List<String> idsOrder;
}
