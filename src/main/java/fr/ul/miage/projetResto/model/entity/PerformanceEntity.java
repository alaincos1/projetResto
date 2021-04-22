package fr.ul.miage.projetResto.model.entity;

import lombok.Data;

@Data
public class PerformanceEntity {
    private String _id;
    private Integer minServiceTime;
    private Integer nbCustomers;
    private Integer minPreparationTime;
    private Integer nbDish;
}
