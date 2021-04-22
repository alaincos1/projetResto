package fr.ul.miage.projetResto.dao.service;

import fr.ul.miage.projetResto.model.entity.OrderEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class Service_OrderTest extends AbstractServiceTest {
    @Test
    public void testSaveOrder() {
        OrderEntity orderEntity = easyRandom.nextObject(OrderEntity.class);

        when(orderCollection.save(any())).thenReturn(true);

        boolean response = service.save(orderEntity);

        Assertions.assertTrue(response);
    }

    @Test
    public void testGetById() {
        OrderEntity expected = easyRandom.nextObject(OrderEntity.class);

        when(orderCollection.getOrderById(anyString())).thenReturn(expected);

        OrderEntity actual = service.getOrderById(expected.get_id());

        assertEqual(actual, expected);
    }

    @Test
    public void testGetByIdWithError() {
        OrderEntity expected = easyRandom.nextObject(OrderEntity.class);

        when(orderCollection.getOrderById(anyString())).thenReturn(null);

        OrderEntity actual = service.getOrderById(expected.get_id());

        Assertions.assertNull(actual);
    }

    private void assertEqual(OrderEntity actual, OrderEntity expected) {
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(actual.get_id(), expected.get_id());
        Assertions.assertEquals(actual.getIdTable(), expected.getIdTable());
        Assertions.assertEquals(actual.getOrderState(), expected.getOrderState());
        Assertions.assertEquals(actual.getChildOrder(), expected.getChildOrder());
        Assertions.assertEquals(actual.getIdsDish().size(), expected.getIdsDish().size());
    }
}
