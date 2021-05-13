package fr.ul.miage.projetResto.dao.service;

import fr.ul.miage.projetResto.constants.MealType;
import fr.ul.miage.projetResto.model.entity.BillEntity;
import fr.ul.miage.projetResto.utils.DateDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class BaseService_BillTest extends AbstractServiceTest {
    @Test
    public void testSaveBill() {
        BillEntity billEntity = easyRandom.nextObject(BillEntity.class);

        when(billCollection.save(any())).thenReturn(true);

        boolean response = baseService.save(billEntity);

        Assertions.assertTrue(response);
    }

    @Test
    public void testUpdateBill() {
        BillEntity billEntity = easyRandom.nextObject(BillEntity.class);

        when(billCollection.update(any())).thenReturn(true);

        boolean response = baseService.update(billEntity);

        Assertions.assertTrue(response);
    }

    @Test
    public void testGetById() {
        BillEntity expected = easyRandom.nextObject(BillEntity.class);

        when(billCollection.getBillById(anyString())).thenReturn(expected);

        BillEntity actual = baseService.getBillById(expected.get_id());

        assertEqual(actual, expected);
    }

    @Test
    public void testGetByIdWithError() {
        BillEntity expected = easyRandom.nextObject(BillEntity.class);

        when(billCollection.getBillById(anyString())).thenReturn(null);

        BillEntity actual = baseService.getBillById(expected.get_id());

        Assertions.assertNull(actual);
    }

    @Test
    public void testGetTakingByPeriod() {
        DateDto dateDto = easyRandom.nextObject(DateDto.class);
        MealType mealType = easyRandom.nextObject(MealType.class);

        BillEntity entity1 = easyRandom.nextObject(BillEntity.class);
        BillEntity entity2 = easyRandom.nextObject(BillEntity.class);
        entity1.setTotalPrice(20);
        entity2.setTotalPrice(25);
        List<BillEntity> bills = new ArrayList<>(Arrays.asList(entity1, entity2));

        when(billCollection.getBillsByPeriodAndMealType(any(DateDto.class), any(MealType.class))).thenReturn(bills);

        String taking = baseService.getTakingByPeriod(dateDto, mealType);

        Assertions.assertNotNull(taking);
        Assertions.assertEquals("recette: 45€", taking);
    }

    private void assertEqual(BillEntity actual, BillEntity expected) {
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(actual.get_id(), expected.get_id());
        Assertions.assertEquals(actual.getDate(), expected.getDate());
        Assertions.assertEquals(actual.getMealType(), expected.getMealType());
        Assertions.assertEquals(actual.getTotalPrice(), expected.getTotalPrice());
        Assertions.assertEquals(actual.getIdsOrder().size(), expected.getIdsOrder().size());
    }
}
