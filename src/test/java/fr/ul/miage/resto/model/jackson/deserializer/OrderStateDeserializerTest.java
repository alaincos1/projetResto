package fr.ul.miage.resto.model.jackson.deserializer;

import fr.ul.miage.resto.constants.OrderState;
import fr.ul.miage.resto.model.jackson.AbstractJacksonTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class OrderStateDeserializerTest extends AbstractJacksonTest {
    OrderStateDeserializer orderStateDeserializer = new OrderStateDeserializer();

    @Test
    void testDeserialization() {
        OrderState expected = easyRandom.nextObject(OrderState.class);

        OrderState actual = orderStateDeserializer.convert(expected.toString());

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(actual, expected);
    }

    @Test
    void testDeserializationWithError() {
        OrderState actual = orderStateDeserializer.convert("unexisting");

        Assertions.assertNull(actual);
    }
}
