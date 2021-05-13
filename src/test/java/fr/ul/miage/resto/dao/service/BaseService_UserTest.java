package fr.ul.miage.resto.dao.service;

import fr.ul.miage.resto.constants.Role;
import fr.ul.miage.resto.model.entity.TableEntity;
import fr.ul.miage.resto.model.entity.UserEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class BaseService_UserTest extends AbstractServiceTest {
    @Test
    void testSaveUser() {
        UserEntity userEntity = easyRandom.nextObject(UserEntity.class);

        when(userCollection.save(any())).thenReturn(true);

        boolean response = baseService.save(userEntity);

        Assertions.assertTrue(response);
    }

    @Test
    void testUpdateUser() {
        UserEntity userEntity = easyRandom.nextObject(UserEntity.class);

        doReturn(true).when(baseService).updateUser(any(UserEntity.class));

        boolean response = baseService.update(userEntity);

        Assertions.assertTrue(response);
    }

    @Test
    void testGetById() {
        UserEntity expected = easyRandom.nextObject(UserEntity.class);

        when(userCollection.getUserById(anyString())).thenReturn(expected);

        UserEntity actual = baseService.getUserById(expected.get_id());

        assertEqual(actual, expected);
    }

    @Test
    void testGetByIdWithError() {
        UserEntity expected = easyRandom.nextObject(UserEntity.class);

        when(userCollection.getUserById(anyString())).thenReturn(null);

        UserEntity actual = baseService.getUserById(expected.get_id());

        Assertions.assertNull(actual);
    }

    @Test
    void testSafeDeleteUser() {
        UserEntity user = easyRandom.nextObject(UserEntity.class);

        when(userCollection.delete(any(UserEntity.class), anyBoolean())).thenReturn(true);
        doNothing().when(baseService).removeUserFromAllTable(any(UserEntity.class));

        boolean actual = baseService.safeDeleteUser(user);

        Assertions.assertTrue(actual);
    }

    @Test
    void testSafeDeleteUserWithError() {
        UserEntity user = easyRandom.nextObject(UserEntity.class);

        when(userCollection.delete(any(UserEntity.class), anyBoolean())).thenReturn(false);

        boolean actual = baseService.safeDeleteUser(user);

        Assertions.assertFalse(actual);
    }

    @Test
    void testPromoteUser() {
        UserEntity userToPromote = easyRandom.nextObject(UserEntity.class);
        userToPromote.setRole(Role.SERVER);
        Role role = Role.DIRECTOR;

        doNothing().when(baseService).removeUserFromAllTable(any(UserEntity.class));
        doReturn(true).when(baseService).update(any(UserEntity.class));

        boolean actual = baseService.promoteUser(userToPromote, role);

        Assertions.assertTrue(actual);
    }

    @Test
    void testPromoteUserWithoutDirectionRole() {
        UserEntity userToPromote = easyRandom.nextObject(UserEntity.class);
        userToPromote.setRole(Role.HELPER);
        Role role = Role.SERVER;

        boolean actual = baseService.promoteUser(userToPromote, role);

        Assertions.assertFalse(actual);
    }

    @Test
    void testPromoteUserWithSameRole() {
        UserEntity userToPromote = easyRandom.nextObject(UserEntity.class);
        userToPromote.setRole(Role.BUTLER);
        Role role = Role.BUTLER;

        boolean actual = baseService.promoteUser(userToPromote, role);

        Assertions.assertFalse(actual);
    }

    @Test
    void testPromoteUserWithActualDirector() {
        UserEntity userToPromote = easyRandom.nextObject(UserEntity.class);
        userToPromote.setRole(Role.DIRECTOR);
        Role role = Role.BUTLER;

        boolean actual = baseService.promoteUser(userToPromote, role);

        Assertions.assertFalse(actual);
    }

    @Test
    void testRemoveUserServerFromAllTable() {
        UserEntity user = easyRandom.nextObject(UserEntity.class);
        user.setRole(Role.SERVER);

        List<TableEntity> tables = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            TableEntity table = easyRandom.nextObject(TableEntity.class);
            table.setIdServer(user.get_id());
            tables.add(table);
        }

        doReturn(tables).when(baseService).getAllTableByServerOrHelper(anyString());

        baseService.removeUserFromAllTable(user);

        verify(baseService, times(tables.size())).save(any(TableEntity.class));
    }

    @Test
    void testRemoveUserHelperFromAllTable() {
        UserEntity user = easyRandom.nextObject(UserEntity.class);
        user.setRole(Role.HELPER);

        List<TableEntity> tables = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            TableEntity table = easyRandom.nextObject(TableEntity.class);
            table.setIdHelper(user.get_id());
            tables.add(table);
        }

        doReturn(tables).when(baseService).getAllTableByServerOrHelper(anyString());

        baseService.removeUserFromAllTable(user);

        verify(baseService, times(tables.size())).save(any(TableEntity.class));
    }

    @Test
    void testRemoveUserFromAllTableWithError() {
        UserEntity user = easyRandom.nextObject(UserEntity.class);
        user.setRole(Role.DIRECTOR);

        baseService.removeUserFromAllTable(user);

        verify(baseService, times(0)).save(any(TableEntity.class));
    }

    private void assertEqual(UserEntity actual, UserEntity expected) {
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(actual.get_id(), expected.get_id());
        Assertions.assertEquals(actual.getRole(), expected.getRole());
    }
}
