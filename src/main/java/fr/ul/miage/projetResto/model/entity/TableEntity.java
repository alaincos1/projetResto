package fr.ul.miage.projetResto.model.entity;

import fr.ul.miage.projetResto.constants.TableState;
import lombok.Data;

@Data
public class TableEntity {
    private String _id;
    private TableState tableState;
    private Integer nbSeats;
    private String idServer;
    private String idHelper;
}
