package fr.ul.miage.resto.model.entity;

import fr.ul.miage.resto.constants.DishType;
import lombok.Data;

@Data
public class CategoryEntity {
    private String _id;
    private DishType dishType;
}
