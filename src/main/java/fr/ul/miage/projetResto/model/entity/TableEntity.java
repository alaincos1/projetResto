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

    @Override
    public String toString() {
        StringBuilder toString = new StringBuilder();
        toString.append("Table n°").append(_id).append(", nombre de places: ").append(nbSeats).append(", état: ").append(tableState.getState());
        toString.append("\n    -> serveur: ").append((idServer==null)?"-":idServer).append(", assistant: ").append((idHelper==null)?"-":idHelper);
        return toString.toString();
    }
}
