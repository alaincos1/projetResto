package fr.ul.miage.resto.dao.service;

import fr.ul.miage.resto.model.entity.DishEntity;
import fr.ul.miage.resto.model.entity.OrderEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class BaseService_DishTest extends AbstractServiceTest {
    @Test
    void testSaveDish() {
        DishEntity dishEntity = easyRandom.nextObject(DishEntity.class);

        when(dishCollection.save(any())).thenReturn(true);

        boolean response = baseService.save(dishEntity);

        Assertions.assertTrue(response);
    }

    @Test
    void testUpdateDish() {
        DishEntity dishEntity = easyRandom.nextObject(DishEntity.class);

        when(dishCollection.update(any())).thenReturn(true);

        boolean response = baseService.update(dishEntity);

        Assertions.assertTrue(response);
    }

    @Test
    void testGetById() {
        DishEntity expected = easyRandom.nextObject(DishEntity.class);

        when(dishCollection.getDishById(anyString())).thenReturn(expected);

        DishEntity actual = baseService.getDishById(expected.get_id());

        assertEqual(actual, expected);
    }

    @Test
    void testGetByIdWithError() {
        DishEntity expected = easyRandom.nextObject(DishEntity.class);

        when(dishCollection.getDishById(anyString())).thenReturn(null);

        DishEntity actual = baseService.getDishById(expected.get_id());

        Assertions.assertNull(actual);
    }

    @Test
    void testGetMostFamousDish() {
        List<String> idsDish1 = new ArrayList<>(Arrays.asList("plat1", "plat2"));
        List<String> idsDish2 = new ArrayList<>(Arrays.asList("plat1", "plat3"));

        OrderEntity orderEntity1 = easyRandom.nextObject(OrderEntity.class);
        OrderEntity orderEntity2 = easyRandom.nextObject(OrderEntity.class);

        orderEntity1.setIdsDish(idsDish1);
        orderEntity2.setIdsDish(idsDish2);
        List<OrderEntity> orders = new ArrayList<>(Arrays.asList(orderEntity1, orderEntity2));

        when(orderCollection.getAllChecked()).thenReturn(orders);

        Map.Entry<String, Integer> mostFamous = baseService.getMostFamousDish();

        Assertions.assertNotNull(mostFamous);
        Assertions.assertEquals(2, mostFamous.getValue());
        Assertions.assertEquals("plat1", mostFamous.getKey());
    }

    private void assertEqual(DishEntity actual, DishEntity expected) {
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(actual.get_id(), expected.get_id());
        Assertions.assertEquals(actual.getDishType(), expected.getDishType());
        Assertions.assertEquals(actual.getIdsProduct().size(), expected.getIdsProduct().size());
        Assertions.assertEquals(actual.getPrice(), expected.getPrice());
        Assertions.assertEquals(actual.getIdCategory(), expected.getIdCategory());
    }
}
