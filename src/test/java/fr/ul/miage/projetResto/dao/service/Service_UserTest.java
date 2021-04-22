package fr.ul.miage.projetResto.dao.service;

import fr.ul.miage.projetResto.model.entity.UserEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class Service_UserTest extends AbstractServiceTest {
    @Test
    public void testSaveUser() {
        UserEntity userEntity = easyRandom.nextObject(UserEntity.class);

        when(userCollection.save(any())).thenReturn(true);

        boolean response = service.save(userEntity);

        Assertions.assertTrue(response);
    }

    @Test
    public void testGetById() {
        UserEntity expected = easyRandom.nextObject(UserEntity.class);

        when(userCollection.getUserById(anyString())).thenReturn(expected);

        UserEntity actual = service.getUserById(expected.get_id());

        assertEqual(actual, expected);
    }

    @Test
    public void testGetByIdWithError() {
        UserEntity expected = easyRandom.nextObject(UserEntity.class);

        when(userCollection.getUserById(anyString())).thenReturn(null);

        UserEntity actual = service.getUserById(expected.get_id());

        Assertions.assertNull(actual);
    }

    private void assertEqual(UserEntity actual, UserEntity expected) {
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(actual.get_id(), expected.get_id());
        Assertions.assertEquals(actual.getRole(), expected.getRole());
    }
}
