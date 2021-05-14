package fr.ul.miage.resto.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.ul.miage.resto.constants.DishType;
import lombok.Data;

@Data
public class CategoryEntity {
    @JsonProperty("_id")
    private String id;
    private DishType dishType;
}
