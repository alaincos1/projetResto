package fr.ul.miage.projetResto.dao.service;

import fr.ul.miage.projetResto.model.entity.CategoryEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class BaseService_MenuTest extends AbstractServiceTest {
    @Test
    public void testSaveMenu() {
        CategoryEntity categoryEntity = easyRandom.nextObject(CategoryEntity.class);

        when(categoryCollection.save(any())).thenReturn(true);

        boolean response = baseService.save(categoryEntity);

        Assertions.assertTrue(response);
    }

    @Test
    public void testUpdateMenu() {
        CategoryEntity categoryEntity = easyRandom.nextObject(CategoryEntity.class);

        when(categoryCollection.update(any())).thenReturn(true);

        boolean response = baseService.update(categoryEntity);

        Assertions.assertTrue(response);
    }

    @Test
    public void testGetById() {
        CategoryEntity expected = easyRandom.nextObject(CategoryEntity.class);

        when(categoryCollection.getCategoryById(anyString())).thenReturn(expected);

        CategoryEntity actual = baseService.getCategoryById(expected.get_id());

        assertEqual(actual, expected);
    }

    @Test
    public void testGetByIdWithError() {
        CategoryEntity expected = easyRandom.nextObject(CategoryEntity.class);

        when(categoryCollection.getCategoryById(anyString())).thenReturn(null);

        CategoryEntity actual = baseService.getCategoryById(expected.get_id());

        Assertions.assertNull(actual);
    }

    private void assertEqual(CategoryEntity actual, CategoryEntity expected) {
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(actual.get_id(), expected.get_id());
    }
}
