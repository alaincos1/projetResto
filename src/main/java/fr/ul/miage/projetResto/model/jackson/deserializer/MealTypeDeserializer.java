package fr.ul.miage.projetResto.model.jackson.deserializer;

import com.fasterxml.jackson.databind.util.StdConverter;
import fr.ul.miage.projetResto.constants.MealType;
import org.apache.commons.lang3.EnumUtils;

public class MealTypeDeserializer extends StdConverter<String, MealType> {
    @Override
    public MealType convert(String s) {
        if (EnumUtils.isValidEnum(MealType.class, s)) {
            return MealType.valueOf(s);
        }
        return null;
    }
}
