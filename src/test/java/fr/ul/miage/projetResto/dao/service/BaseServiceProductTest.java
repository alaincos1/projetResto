package fr.ul.miage.projetResto.dao.service;

import fr.ul.miage.projetResto.model.entity.ProductEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class BaseServiceProductTest extends AbstractServiceTest {
    @Test
    public void testSaveProduct() {
        ProductEntity productEntity = easyRandom.nextObject(ProductEntity.class);

        when(productCollection.save(any())).thenReturn(true);

        boolean response = baseService.save(productEntity);

        Assertions.assertTrue(response);
    }

    @Test
    public void testGetById() {
        ProductEntity expected = easyRandom.nextObject(ProductEntity.class);

        when(productCollection.getProductById(anyString())).thenReturn(expected);

        ProductEntity actual = baseService.getProductById(expected.get_id());

        assertEqual(actual, expected);
    }

    @Test
    public void testGetByIdWithError() {
        ProductEntity expected = easyRandom.nextObject(ProductEntity.class);

        when(productCollection.getProductById(anyString())).thenReturn(null);

        ProductEntity actual = baseService.getProductById(expected.get_id());

        Assertions.assertNull(actual);
    }

    private void assertEqual(ProductEntity actual, ProductEntity expected) {
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(actual.get_id(), expected.get_id());
        Assertions.assertEquals(actual.getIdsDish().size(), expected.getIdsDish().size());
        Assertions.assertEquals(actual.getStock(), expected.getStock());
    }
}
