package fr.ul.miage.projetResto.model.entity;

import fr.ul.miage.projetResto.constants.DishType;
import lombok.Data;

import java.util.List;

@Data
public class DishEntity {
    private String _id;
    private Integer price;
    private DishType dishType;
    private List<String> idsProduct;
}
