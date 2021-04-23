package fr.ul.miage.projetResto.model.jackson.serializer;

import fr.ul.miage.projetResto.constants.*;
import fr.ul.miage.projetResto.model.jackson.AbstractJacksonTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class EnumSerializerTest extends AbstractJacksonTest {
    @Test
    public void testDishTypeSerialization() {
        DishType expected = easyRandom.nextObject(DishType.class);

        String actual = enumSerializer.convert(expected);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(actual, expected.toString());
    }

    @Test
    public void testMealTypeSerialization() {
        MealType expected = easyRandom.nextObject(MealType.class);

        String actual = enumSerializer.convert(expected);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(actual, expected.toString());
    }

    @Test
    public void testOrderStateSerialization() {
        OrderState expected = easyRandom.nextObject(OrderState.class);

        String actual = enumSerializer.convert(expected);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(actual, expected.toString());
    }

    @Test
    public void testRoleSerialization() {
        Role expected = easyRandom.nextObject(Role.class);

        String actual = enumSerializer.convert(expected);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(actual, expected.toString());
    }

    @Test
    public void testTableStateSerialization() {
        TableState expected = easyRandom.nextObject(TableState.class);

        String actual = enumSerializer.convert(expected);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(actual, expected.toString());
    }
}
