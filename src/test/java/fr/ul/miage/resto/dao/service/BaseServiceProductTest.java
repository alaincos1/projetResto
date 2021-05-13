package fr.ul.miage.resto.dao.service;

import fr.ul.miage.resto.model.entity.ProductEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class BaseServiceProductTest extends AbstractServiceTest {
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

        ProductEntity actual = baseService.getProductById(expected.get_id());

        assertEqual(actual, expected);
    }

    @Test
    void testGetByIdWithError() {
        ProductEntity expected = easyRandom.nextObject(ProductEntity.class);

        when(productCollection.getProductById(anyString())).thenReturn(null);

        ProductEntity actual = baseService.getProductById(expected.get_id());

        Assertions.assertNull(actual);
    }

    private void assertEqual(ProductEntity actual, ProductEntity expected) {
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(actual.get_id(), expected.get_id());
        Assertions.assertEquals(actual.getStock(), expected.getStock());
    }
}
