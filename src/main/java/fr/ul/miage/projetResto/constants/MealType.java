package fr.ul.miage.projetResto.constants;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import fr.ul.miage.projetResto.model.jackson.deserializer.MealTypeDeserializer;
import fr.ul.miage.projetResto.model.jackson.serializer.EnumSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
@JsonSerialize(converter = EnumSerializer.class)
@JsonDeserialize(converter = MealTypeDeserializer.class)
public enum MealType implements EnumInBase {
    Déjeuner(1),
    Dîner(2);

    private final Integer id;

    public static MealType getFromId(Integer id) {
        switch (id) {
            case 1:
                return Déjeuner;
            case 2:
                return Dîner;
            default:
                return null;
        }
    }
}
