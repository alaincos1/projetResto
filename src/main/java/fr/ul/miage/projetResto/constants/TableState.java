package fr.ul.miage.projetResto.constants;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import fr.ul.miage.projetResto.model.jackson.deserializer.TableStateDeserializer;
import fr.ul.miage.projetResto.model.jackson.serializer.EnumSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
@JsonSerialize(converter = EnumSerializer.class)
@JsonDeserialize(converter = TableStateDeserializer.class)
public enum TableState implements EnumInBase {
    Free("Libre"),
    Booked("Réservée"),
    Occupied("Occupée"),
    Dirty("Sâle");

    private String state;
}
