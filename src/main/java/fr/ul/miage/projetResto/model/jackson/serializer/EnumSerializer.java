package fr.ul.miage.projetResto.model.jackson.serializer;

import com.fasterxml.jackson.databind.util.StdConverter;
import fr.ul.miage.projetResto.constants.EnumInBase;

public class EnumSerializer extends StdConverter<EnumInBase, String> {
    @Override
    public String convert(EnumInBase enumInBase) {
        return enumInBase.toString();
    }
}
