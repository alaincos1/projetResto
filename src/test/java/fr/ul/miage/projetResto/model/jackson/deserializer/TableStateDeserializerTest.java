package fr.ul.miage.projetResto.model.jackson.deserializer;

import fr.ul.miage.projetResto.constants.TableState;
import fr.ul.miage.projetResto.model.jackson.AbstractJacksonTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TableStateDeserializerTest extends AbstractJacksonTest {
    TableStateDeserializer tableStateDeserializer = new TableStateDeserializer();

    @Test
    public void testDeserialization() {
        TableState expected = easyRandom.nextObject(TableState.class);

        TableState actual = tableStateDeserializer.convert(expected.toString());

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(actual, expected);
    }

    @Test
    public void testDeserializationWithErrror() {
        TableState actual = tableStateDeserializer.convert("unexisting");

        Assertions.assertNull(actual);
    }
}
