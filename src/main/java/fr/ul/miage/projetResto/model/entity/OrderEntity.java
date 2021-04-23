package fr.ul.miage.projetResto.model.entity;

import fr.ul.miage.projetResto.constants.OrderState;
import lombok.Data;

import java.util.List;

@Data
public class OrderEntity {
    private String _id;
    private OrderState orderState;
    private Boolean childOrder;
    private Integer rank;
    private List<String> idsDish;
    private String idTable;
}
