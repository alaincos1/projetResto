package fr.ul.miage.resto.constants;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import fr.ul.miage.resto.model.jackson.deserializer.TableStateDeserializer;
import fr.ul.miage.resto.model.jackson.serializer.EnumSerializer;
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
    Starter("Entrée"),
    MainCourse("Plat"),
    Dessert("Dessert"),
    Dirty("Sâle");

    private final String state;
}
