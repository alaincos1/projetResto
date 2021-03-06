package fr.ul.miage.resto.constants;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import fr.ul.miage.resto.model.jackson.deserializer.OrderStateDeserializer;
import fr.ul.miage.resto.model.jackson.serializer.EnumSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
@JsonSerialize(converter = EnumSerializer.class)
@JsonDeserialize(converter = OrderStateDeserializer.class)
public enum OrderState implements EnumInBase {
    ORDERED("Commandée"),
    PREPARED("Préparée"),
    SERVED("Servie"),
    CHECKED("Payée"),
    UNCHECKED("Impayée");

    private final String state;
}
