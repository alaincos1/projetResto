package fr.ul.miage.projetResto.dao.service;

import fr.ul.miage.projetResto.model.entity.MenuEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class Service_MenuTest extends AbstractServiceTest {
    @Test
    public void testSaveMenu() {
        MenuEntity menuEntity = easyRandom.nextObject(MenuEntity.class);

        when(menuCollection.save(any())).thenReturn(true);

        boolean response = service.save(menuEntity);

        Assertions.assertTrue(response);
    }

    @Test
    public void testGetById() {
        MenuEntity expected = easyRandom.nextObject(MenuEntity.class);

        when(menuCollection.getMenuById(anyString())).thenReturn(expected);

        MenuEntity actual = service.getMenuById(expected.get_id());

        assertEqual(actual, expected);
    }

    @Test
    public void testGetByIdWithError() {
        MenuEntity expected = easyRandom.nextObject(MenuEntity.class);

        when(menuCollection.getMenuById(anyString())).thenReturn(null);

        MenuEntity actual = service.getMenuById(expected.get_id());

        Assertions.assertNull(actual);
    }

    private void assertEqual(MenuEntity actual, MenuEntity expected) {
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(actual.get_id(), expected.get_id());
        Assertions.assertEquals(actual.getIdsDish().size(), expected.getIdsDish().size());
    }
}
