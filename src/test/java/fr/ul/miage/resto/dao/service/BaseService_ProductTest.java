package fr.ul.miage.resto.dao.service;

import fr.ul.miage.resto.model.entity.ProductEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class BaseService_ProductTest extends AbstractServiceTest {
    @Test
    void testSaveProduct() {
        ProductEntity productEntity = easyRandom.nextObject(ProductEntity.class);

        when(productCollection.save(any())).thenReturn(true);

        boolean response = baseService.save(productEntity);

        Assertions.assertTrue(response);
    }

    @Test
    void testUpdateProduct() {
        ProductEntity productEntity = easyRandom.nextObject(ProductEntity.class);

        when(productCollection.update(any())).thenReturn(true);

        boolean response = baseService.update(productEntity);

        Assertions.assertTrue(response);
    }

    @Test
    void testGetById() {
        ProductEntity expected = easyRandom.nextObject(ProductEntity.class);

        when(productCollection.getProductById(anyString())).thenReturn(expected);

        ProductEntity actual = baseService.getProductById(expected.getId());

        assertEqual(actual, expected);
    }

    @Test
    void testGetByIdWithError() {
        ProductEntity expected = easyRandom.nextObject(ProductEntity.class);

        when(productCollection.getProductById(anyString())).thenReturn(null);

        ProductEntity actual = baseService.getProductById(expected.getId());

        Assertions.assertNull(actual);
    }

    @Test
    void testGetAllProducts() {
        ProductEntity productEntity = easyRandom.nextObject(ProductEntity.class);
        List<ProductEntity> expected = new ArrayList<>(Arrays.asList(productEntity));

        when(productCollection.getAllProducts()).thenReturn(expected);

        List<ProductEntity> actual = baseService.getAllProducts();

        assertEqual(expected, actual);
    }

    private void assertEqual(ProductEntity actual, ProductEntity expected) {
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(actual.getId(), expected.getId());
        Assertions.assertEquals(actual.getStock(), expected.getStock());
    }

    private void assertEqual(List<ProductEntity> actual, List<ProductEntity> expected) {
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(actual.size(), expected.size());

        for (int i = 0; i < actual.size(); i++) {
            assertEqual(actual.get(i), expected.get(i));
        }
    }
}
