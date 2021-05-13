package fr.ul.miage.resto.model.jackson.deserializer;

import fr.ul.miage.resto.constants.DishType;
import fr.ul.miage.resto.model.jackson.AbstractJacksonTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class DishTypeDeserializerTest extends AbstractJacksonTest {
    DishTypeDeserializer dishTypeDeserializer = new DishTypeDeserializer();

    @Test
    void testDeserialization() {
        DishType expected = easyRandom.nextObject(DishType.class);

        DishType actual = dishTypeDeserializer.convert(expected.toString());

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(actual, expected);
    }

    @Test
    void testDeserializationWithError() {
        DishType actual = dishTypeDeserializer.convert("unexisting");

        Assertions.assertNull(actual);
    }
}
