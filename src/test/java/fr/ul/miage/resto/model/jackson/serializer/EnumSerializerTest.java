package fr.ul.miage.resto.model.jackson.serializer;

import fr.ul.miage.resto.constants.*;
import fr.ul.miage.resto.model.jackson.AbstractJacksonTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class EnumSerializerTest extends AbstractJacksonTest {
    @Test
    void testDishTypeSerialization() {
        DishType expected = easyRandom.nextObject(DishType.class);

        String actual = enumSerializer.convert(expected);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(actual, expected.toString());
    }

    @Test
    void testMealTypeSerialization() {
        MealType expected = easyRandom.nextObject(MealType.class);

        String actual = enumSerializer.convert(expected);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(actual, expected.toString());
    }

    @Test
    void testOrderStateSerialization() {
        OrderState expected = easyRandom.nextObject(OrderState.class);

        String actual = enumSerializer.convert(expected);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(actual, expected.toString());
    }

    @Test
    void testRoleSerialization() {
        Role expected = easyRandom.nextObject(Role.class);

        String actual = enumSerializer.convert(expected);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(actual, expected.toString());
    }

    @Test
    void testTableStateSerialization() {
        TableState expected = easyRandom.nextObject(TableState.class);

        String actual = enumSerializer.convert(expected);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(actual, expected.toString());
    }
}
