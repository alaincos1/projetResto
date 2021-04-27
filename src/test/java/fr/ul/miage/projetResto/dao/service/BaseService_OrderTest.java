package fr.ul.miage.projetResto.dao.service;

import fr.ul.miage.projetResto.constants.OrderState;
import fr.ul.miage.projetResto.model.entity.OrderEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class BaseService_OrderTest extends AbstractServiceTest {
    @Test
    public void testSaveOrder() {
        OrderEntity orderEntity = easyRandom.nextObject(OrderEntity.class);

        when(orderCollection.save(any())).thenReturn(true);

        boolean response = baseService.save(orderEntity);

        Assertions.assertTrue(response);
    }

    @Test
    public void testUpdateOrder() {
        OrderEntity orderEntity = easyRandom.nextObject(OrderEntity.class);

        when(orderCollection.update(any())).thenReturn(true);

        boolean response = baseService.update(orderEntity);

        Assertions.assertTrue(response);
    }

    @Test
    public void testGetById() {
        OrderEntity expected = easyRandom.nextObject(OrderEntity.class);

        when(orderCollection.getOrderById(anyString())).thenReturn(expected);

        OrderEntity actual = baseService.getOrderById(expected.get_id());

        assertEqual(actual, expected);
    }

    @Test
    public void testGetByIdWithError() {
        OrderEntity expected = easyRandom.nextObject(OrderEntity.class);

        when(orderCollection.getOrderById(anyString())).thenReturn(null);

        OrderEntity actual = baseService.getOrderById(expected.get_id());

        Assertions.assertNull(actual);
    }

    @Test
    public void testGetAllBooking() {
        List<OrderEntity> expected = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            expected.add(easyRandom.nextObject(OrderEntity.class));
            expected.get(i).setOrderState(OrderState.Ordered);
        }

        when(baseService.getAllNotCheckedOrder()).thenReturn(expected);

        List<OrderEntity> actual = baseService.getAllNotCheckedOrder();

        assertEqual(actual, expected);
    }

    private void assertEqual(OrderEntity actual, OrderEntity expected) {
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(actual.get_id(), expected.get_id());
        Assertions.assertEquals(actual.getIdTable(), expected.getIdTable());
        Assertions.assertEquals(actual.getOrderState(), expected.getOrderState());
        Assertions.assertEquals(actual.getChildOrder(), expected.getChildOrder());
        Assertions.assertEquals(actual.getIdsDish().size(), expected.getIdsDish().size());
        Assertions.assertEquals(actual.getRank(), expected.getRank());
    }

    private void assertEqual(List<OrderEntity> actual, List<OrderEntity> expected) {
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(actual.size(), expected.size());

        for (int i = 0; i < actual.size(); i++) {
            assertEqual(actual.get(i), expected.get(i));
        }
    }
}
