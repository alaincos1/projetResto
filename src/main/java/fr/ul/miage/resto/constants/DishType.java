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
    Starter("Entrée", 1),
    MainCourse("Plat", 2),
    Dessert("Dessert", 3),
    Drink("Boisson", 4);

    private final String dish;
    private final Integer priority;
}
