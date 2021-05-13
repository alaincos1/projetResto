package fr.ul.miage.resto.model.jackson.deserializer;

import com.fasterxml.jackson.databind.util.StdConverter;
import fr.ul.miage.resto.constants.OrderState;
import org.apache.commons.lang3.EnumUtils;

public class OrderStateDeserializer extends StdConverter<String, OrderState> {
    @Override
    public OrderState convert(String s) {
        if (EnumUtils.isValidEnum(OrderState.class, s)) {
            return OrderState.valueOf(s);
        }
        return null;
    }
}
