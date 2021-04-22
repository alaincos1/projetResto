package fr.ul.miage.projetResto.model.entity;

import lombok.Data;

import java.util.List;

@Data
public class OrderEntity {
    private String _id;
    private String orderState;
    private Boolean childOrder;
    private List<String> idsDish;
    private String idTable;
}
