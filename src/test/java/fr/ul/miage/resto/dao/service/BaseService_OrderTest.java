package fr.ul.miage.resto.dao.service;

import fr.ul.miage.resto.constants.OrderState;
import fr.ul.miage.resto.model.entity.OrderEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class BaseService_OrderTest extends AbstractServiceTest {
    @Test
    void testSaveOrder() {
        OrderEntity orderEntity = easyRandom.nextObject(OrderEntity.class);

        when(orderCollection.save(any())).thenReturn(true);

        boolean response = baseService.save(orderEntity);

        Assertions.assertTrue(response);
    }

    @Test
    void testUpdateOrder() {
        OrderEntity orderEntity = easyRandom.nextObject(OrderEntity.class);

        when(orderCollection.update(any())).thenReturn(true);

        boolean response = baseService.update(orderEntity);

        Assertions.assertTrue(response);
    }

    @Test
    void testGetById() {
        OrderEntity expected = easyRandom.nextObject(OrderEntity.class);

        when(orderCollection.getOrderById(anyString())).thenReturn(expected);

        OrderEntity actual = baseService.getOrderById(expected.getId());

        assertEqual(actual, expected);
    }

    @Test
    void testGetByIdWithError() {
        OrderEntity expected = easyRandom.nextObject(OrderEntity.class);

        when(orderCollection.getOrderById(anyString())).thenReturn(null);

        OrderEntity actual = baseService.getOrderById(expected.getId());

        Assertions.assertNull(actual);
    }

    @Test
    void testGetAllNotCheckedOrder() {
        List<OrderEntity> expected = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            expected.add(easyRandom.nextObject(OrderEntity.class));
            expected.get(i).setOrderState(OrderState.ORDERED);
        }

        when(orderCollection.getAllNotChecked()).thenReturn(expected);

        List<OrderEntity> actual = baseService.getAllNotCheckedOrder();

        assertEqual(actual, expected);
    }

    @Test
    void testGetPreparedOrders() {
        List<OrderEntity> expected = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            expected.add(easyRandom.nextObject(OrderEntity.class));
        }

        when(orderCollection.getPreparedOrders()).thenReturn(expected);

        List<OrderEntity> actual = baseService.getPreparedOrders();

        assertEqual(actual, expected);
    }

    @Test
    void testGetServedOrders() {
        List<OrderEntity> expected = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            expected.add(easyRandom.nextObject(OrderEntity.class));
        }

        when(orderCollection.getServedOrders()).thenReturn(expected);

        List<OrderEntity> actual = baseService.getServedOrders();

        assertEqual(actual, expected);
    }

    private void assertEqual(OrderEntity actual, OrderEntity expected) {
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(actual.getId(), expected.getId());
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
