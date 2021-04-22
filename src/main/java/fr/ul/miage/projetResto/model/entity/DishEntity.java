package fr.ul.miage.projetResto.model.entity;

import lombok.Data;

import java.util.List;

@Data
public class DishEntity {
    private String _id;
    private Integer price;
    private String dishType;
    private List<String> idsProduct;
}
