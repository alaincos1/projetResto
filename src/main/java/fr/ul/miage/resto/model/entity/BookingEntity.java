package fr.ul.miage.resto.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.ul.miage.resto.constants.MealType;
import lombok.Data;

@Data
public class BookingEntity {
    @JsonProperty("_id")
    private String id;
    private MealType mealType;
    private String reservationName;
    private String date;
    private String idTable;
}
