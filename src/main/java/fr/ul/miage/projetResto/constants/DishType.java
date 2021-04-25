package fr.ul.miage.projetResto.constants;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import fr.ul.miage.projetResto.model.jackson.deserializer.DishTypeDeserializer;
import fr.ul.miage.projetResto.model.jackson.serializer.EnumSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
@JsonSerialize(converter = EnumSerializer.class)
@JsonDeserialize(converter = DishTypeDeserializer.class)
public enum DishType implements EnumInBase {
    Starter("Entrée"),
    MainCourse("Plat"),
    Dessert("Dessert"),
    Drink("Boisson");

    private final String dish;
}
