package fr.ul.miage.resto.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.ul.miage.resto.constants.TableState;
import lombok.Data;

@Data
public class TableEntity {
    @JsonProperty("_id")
    private String id;
    private TableState tableState;
    private Integer nbSeats;
    private String idServer;
    private String idHelper;

    @Override
    public String toString() {
        return "Table n°" + id + ", nombre de places: " + nbSeats + ", état: " + tableState.getState() +
                "\n    -> serveur: " + ((idServer == null) ? "-" : idServer) + ", assistant: " + ((idHelper == null) ? "-" : idHelper);
    }
}
