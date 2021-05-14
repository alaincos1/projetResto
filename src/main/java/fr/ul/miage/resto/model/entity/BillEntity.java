package fr.ul.miage.resto.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.ul.miage.resto.constants.MealType;
import lombok.Data;

import java.util.List;

@Data
public class BillEntity {
    @JsonProperty("_id")
    private String id;
    private Integer totalPrice;
    private String date;
    private MealType mealType;
    private List<String> idsOrder;
}
