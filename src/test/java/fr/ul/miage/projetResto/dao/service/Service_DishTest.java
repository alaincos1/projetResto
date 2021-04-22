package fr.ul.miage.projetResto.dao.service;

import fr.ul.miage.projetResto.model.entity.DishEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class Service_DishTest extends AbstractServiceTest {
    @Test
    public void testSaveDish() {
        DishEntity dishEntity = easyRandom.nextObject(DishEntity.class);

        when(dishCollection.save(any())).thenReturn(true);

        boolean response = service.save(dishEntity);

        Assertions.assertTrue(response);
    }

    @Test
    public void testGetById() {
        DishEntity expected = easyRandom.nextObject(DishEntity.class);

        when(dishCollection.getDishById(anyString())).thenReturn(expected);

        DishEntity actual = service.getDishById(expected.get_id());

        assertEqual(actual, expected);
    }

    @Test
    public void testGetByIdWithError() {
        DishEntity expected = easyRandom.nextObject(DishEntity.class);

        when(dishCollection.getDishById(anyString())).thenReturn(null);

        DishEntity actual = service.getDishById(expected.get_id());

        Assertions.assertNull(actual);
    }

    private void assertEqual(DishEntity actual, DishEntity expected) {
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(actual.get_id(), expected.get_id());
        Assertions.assertEquals(actual.getDishType(), expected.getDishType());
        Assertions.assertEquals(actual.getIdsProduct().size(), expected.getIdsProduct().size());
        Assertions.assertEquals(actual.getPrice(), expected.getPrice());
    }
}
