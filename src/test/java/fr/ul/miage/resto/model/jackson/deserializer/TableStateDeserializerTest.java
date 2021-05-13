package fr.ul.miage.resto.model.jackson.deserializer;

import fr.ul.miage.resto.constants.TableState;
import fr.ul.miage.resto.model.jackson.AbstractJacksonTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TableStateDeserializerTest extends AbstractJacksonTest {
    TableStateDeserializer tableStateDeserializer = new TableStateDeserializer();

    @Test
    void testDeserialization() {
        TableState expected = easyRandom.nextObject(TableState.class);

        TableState actual = tableStateDeserializer.convert(expected.toString());

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(actual, expected);
    }

    @Test
    void testDeserializationWithError() {
        TableState actual = tableStateDeserializer.convert("unexisting");

        Assertions.assertNull(actual);
    }
}
