package fr.ul.miage.resto.model.jackson.deserializer;

import fr.ul.miage.resto.constants.Role;
import fr.ul.miage.resto.model.jackson.AbstractJacksonTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class RoleDeserializerTest extends AbstractJacksonTest {
    RoleDeserializer roleDeserializer = new RoleDeserializer();

    @Test
    void testDeserialization() {
        Role expected = easyRandom.nextObject(Role.class);

        Role actual = roleDeserializer.convert(expected.toString());

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(actual, expected);
    }

    @Test
    void testDeserializationWithError() {
        Role actual = roleDeserializer.convert("unexisting");

        Assertions.assertNull(actual);
    }
}
