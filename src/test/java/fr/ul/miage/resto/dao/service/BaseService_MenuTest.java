package fr.ul.miage.resto.dao.service;

import fr.ul.miage.resto.model.entity.CategoryEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class BaseService_MenuTest extends AbstractServiceTest {
    @Test
    void testSaveMenu() {
        CategoryEntity categoryEntity = easyRandom.nextObject(CategoryEntity.class);

        when(categoryCollection.save(any())).thenReturn(true);

        boolean response = baseService.save(categoryEntity);

        Assertions.assertTrue(response);
    }

    @Test
    void testUpdateMenu() {
        CategoryEntity categoryEntity = easyRandom.nextObject(CategoryEntity.class);

        when(categoryCollection.update(any())).thenReturn(true);

        boolean response = baseService.update(categoryEntity);

        Assertions.assertTrue(response);
    }

    @Test
    void testGetById() {
        CategoryEntity expected = easyRandom.nextObject(CategoryEntity.class);

        when(categoryCollection.getCategoryById(anyString())).thenReturn(expected);

        CategoryEntity actual = baseService.getCategoryById(expected.getId());

        assertEqual(actual, expected);
    }

    @Test
    void testGetByIdWithError() {
        CategoryEntity expected = easyRandom.nextObject(CategoryEntity.class);

        when(categoryCollection.getCategoryById(anyString())).thenReturn(null);

        CategoryEntity actual = baseService.getCategoryById(expected.getId());

        Assertions.assertNull(actual);
    }

    private void assertEqual(CategoryEntity actual, CategoryEntity expected) {
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(actual.getId(), expected.getId());
    }
}
