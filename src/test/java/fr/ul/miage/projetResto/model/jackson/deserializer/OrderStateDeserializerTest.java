package fr.ul.miage.projetResto.model.jackson.deserializer;

import fr.ul.miage.projetResto.constants.OrderState;
import fr.ul.miage.projetResto.model.jackson.AbstractJacksonTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class OrderStateDeserializerTest extends AbstractJacksonTest {
    OrderStateDeserializer orderStateDeserializer = new OrderStateDeserializer();

    @Test
    public void testDeserialization() {
        OrderState expected = easyRandom.nextObject(OrderState.class);

        OrderState actual = orderStateDeserializer.convert(expected.toString());

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(actual, expected);
    }

    @Test
    public void testDeserializationWithErrror() {
        OrderState actual = orderStateDeserializer.convert("unexisting");

        Assertions.assertNull(actual);
    }
}
