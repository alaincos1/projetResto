package fr.ul.miage.projetResto.model.jackson.deserializer;

import fr.ul.miage.projetResto.constants.MealType;
import fr.ul.miage.projetResto.model.jackson.AbstractJacksonTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MealTypeDeserializerTest extends AbstractJacksonTest {
    MealTypeDeserializer mealTypeDeserializer = new MealTypeDeserializer();

    @Test
    public void testDeserialization() {
        MealType expected = easyRandom.nextObject(MealType.class);

        MealType actual = mealTypeDeserializer.convert(expected.toString());

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(actual, expected);
    }

    @Test
    public void testDeserializationWithErrror() {
        MealType actual = mealTypeDeserializer.convert("unexisting");

        Assertions.assertNull(actual);
    }
}
