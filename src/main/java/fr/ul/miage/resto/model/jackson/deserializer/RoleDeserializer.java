package fr.ul.miage.resto.model.jackson.deserializer;

import com.fasterxml.jackson.databind.util.StdConverter;
import fr.ul.miage.resto.constants.Role;
import org.apache.commons.lang3.EnumUtils;

public class RoleDeserializer extends StdConverter<String, Role> {
    @Override
    public Role convert(String s) {
        if (EnumUtils.isValidEnum(Role.class, s)) {
            return Role.valueOf(s);
        }
        return null;
    }
}
