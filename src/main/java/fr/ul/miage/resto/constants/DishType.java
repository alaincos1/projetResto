package fr.ul.miage.resto.constants;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import fr.ul.miage.resto.model.jackson.deserializer.DishTypeDeserializer;
import fr.ul.miage.resto.model.jackson.serializer.EnumSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
@JsonSerialize(converter = EnumSerializer.class)
@JsonDeserialize(converter = DishTypeDeserializer.class)
public enum DishType implements EnumInBase {
    STARTER("Entrée", 1),
    MAIN_COURSE("Plat", 2),
    DESSERT("Dessert", 3),
    DRINK("Boisson", 4);

    private final String dish;
    private final Integer priority;
}
