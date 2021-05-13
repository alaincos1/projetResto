package fr.ul.miage.resto.model.entity;

import fr.ul.miage.resto.constants.MealType;
import lombok.Data;

@Data
public class BookingEntity {
    private String _id;
    private MealType mealType;
    private String reservationName;
    private String date;
    private String idTable;
}
