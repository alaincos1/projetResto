package fr.ul.miage.projetResto.model.entity;

import lombok.Data;

@Data
public class BillEntity {
    private String _id;
    private Integer totalPrice;
    private String date;
    private String mealType;
    private String idTable;
}
