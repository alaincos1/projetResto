package fr.ul.miage.resto.model.jackson.deserializer;

import com.fasterxml.jackson.databind.util.StdConverter;
import fr.ul.miage.resto.constants.DishType;
import org.apache.commons.lang3.EnumUtils;

public class DishTypeDeserializer extends StdConverter<String, DishType> {
    @Override
    public DishType convert(String s) {
        if (EnumUtils.isValidEnum(DishType.class, s)) {
            return DishType.valueOf(s);
        }
        return null;
    }
}
