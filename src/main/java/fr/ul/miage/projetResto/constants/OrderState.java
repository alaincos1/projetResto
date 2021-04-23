package fr.ul.miage.projetResto.constants;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import fr.ul.miage.projetResto.model.jackson.deserializer.OrderStateDeserializer;
import fr.ul.miage.projetResto.model.jackson.serializer.EnumSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
@JsonSerialize(converter = EnumSerializer.class)
@JsonDeserialize(converter = OrderStateDeserializer.class)
public enum OrderState implements EnumInBase {
    Ordered("Commandée"),
    Prepared("Préparée"),
    Served("Servie"),
    Checked("Payée");

    private String state;

    public String getName() {
        return "orderState";
    }
}
