package fr.ul.miage.projetResto.model.entity;

import lombok.Data;

import java.util.List;

@Data
public class ProductEntity {
    private String _id;
    private Integer stock;
    List<String> idsDish;
}
