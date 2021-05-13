package fr.ul.miage.resto.model.jackson.deserializer;

import fr.ul.miage.resto.constants.MealType;
import fr.ul.miage.resto.model.jackson.AbstractJacksonTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MealTypeDeserializerTest extends AbstractJacksonTest {
    MealTypeDeserializer mealTypeDeserializer = new MealTypeDeserializer();

    @Test
    void testDeserialization() {
        MealType expected = easyRandom.nextObject(MealType.class);

        MealType actual = mealTypeDeserializer.convert(expected.toString());

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(actual, expected);
    }

    @Test
    void testDeserializationWithError() {
        MealType actual = mealTypeDeserializer.convert("unexisting");

        Assertions.assertNull(actual);
    }
}
