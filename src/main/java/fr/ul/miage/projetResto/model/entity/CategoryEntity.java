package fr.ul.miage.projetResto.model.entity;

import fr.ul.miage.projetResto.constants.DishType;
import lombok.Data;

@Data
public class CategoryEntity {
    private String _id;
    private DishType dishType;
}
