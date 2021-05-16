package fr.ul.miage.resto.dao.service;

import fr.ul.miage.resto.constants.DishType;
import fr.ul.miage.resto.model.entity.CategoryEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class BaseService_CategoryTest extends AbstractServiceTest {
    @Test
    void testSaveOrder() {
        CategoryEntity categoryEntity = easyRandom.nextObject(CategoryEntity.class);

        when(categoryCollection.save(any())).thenReturn(true);

        boolean response = baseService.save(categoryEntity);

        Assertions.assertTrue(response);
    }

    @Test
    void testUpdateOrder() {
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

    @Test
    void testGetCategoriesByDishType() {
        CategoryEntity categoryEntity = easyRandom.nextObject(CategoryEntity.class);
        List<CategoryEntity> expected = new ArrayList<>(Arrays.asList(categoryEntity));

        DishType dishType = easyRandom.nextObject(DishType.class);

        when(categoryCollection.getCategoriesByDishType(any(DishType.class))).thenReturn(expected);

        List<CategoryEntity> actual = baseService.getCategoriesByDishType(dishType);

        assertEqual(expected, actual);
    }

    private void assertEqual(CategoryEntity actual, CategoryEntity expected) {
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(actual.getId(), expected.getId());
        Assertions.assertEquals(actual.getDishType(), expected.getDishType());
    }

    private void assertEqual(List<CategoryEntity> actual, List<CategoryEntity> expected) {
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(actual.size(), expected.size());

        for (int i = 0; i < actual.size(); i++) {
            assertEqual(actual.get(i), expected.get(i));
        }
    }
}
