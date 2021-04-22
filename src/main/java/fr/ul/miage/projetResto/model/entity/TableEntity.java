package fr.ul.miage.projetResto.model.entity;

import lombok.Data;

import java.util.List;

@Data
public class TableEntity {
    private String _id;
    private String tableState;
    private String idUser1;
    private String idUser2;
    private List<String> idsBooking;
    private List<String> idsBill;
}
