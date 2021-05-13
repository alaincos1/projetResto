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
    FREE("Libre"),
    BOOKED("Réservée"),
    OCCUPIED("Occupée"),
    STARTER("Entrée"),
    MAIN_COURSE("Plat"),
    DESSERT("Dessert"),
    DIRTY("Sâle");

    private final String state;
}
