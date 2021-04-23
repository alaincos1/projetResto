package fr.ul.miage.projetResto.model.jackson.deserializer;

import fr.ul.miage.projetResto.constants.DishType;
import fr.ul.miage.projetResto.model.jackson.AbstractJacksonTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class DishTypeDeserializerTest extends AbstractJacksonTest {
    DishTypeDeserializer dishTypeDeserializer = new DishTypeDeserializer();

    @Test
    public void testDeserialization() {
        DishType expected = easyRandom.nextObject(DishType.class);

        DishType actual = dishTypeDeserializer.convert(expected.toString());

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(actual, expected);
    }

    @Test
    public void testDeserializationWithErrror() {
        DishType actual = dishTypeDeserializer.convert("unexisting");

        Assertions.assertNull(actual);
    }
}
