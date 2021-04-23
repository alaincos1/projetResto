package fr.ul.miage.projetResto.model.jackson.deserializer;

import fr.ul.miage.projetResto.constants.Role;
import fr.ul.miage.projetResto.model.jackson.AbstractJacksonTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RoleDeserializerTest extends AbstractJacksonTest {
    RoleDeserializer roleDeserializer = new RoleDeserializer();

    @Test
    public void testDeserialization() {
        Role expected = easyRandom.nextObject(Role.class);

        Role actual = roleDeserializer.convert(expected.toString());

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(actual, expected);
    }

    @Test
    public void testDeserializationWithErrror() {
        Role actual = roleDeserializer.convert("unexisting");

        Assertions.assertNull(actual);
    }
}
