package fr.ul.miage.projetResto.model.jackson.deserializer;

import com.fasterxml.jackson.databind.util.StdConverter;
import fr.ul.miage.projetResto.constants.TableState;
import org.apache.commons.lang3.EnumUtils;

public class TableStateDeserializer extends StdConverter<String, TableState> {
    @Override
    public TableState convert(String s) {
        if (EnumUtils.isValidEnum(TableState.class, s)) {
            return TableState.valueOf(s);
        }
        return null;
    }
}
