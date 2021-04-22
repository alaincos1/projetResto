package fr.ul.miage.projetResto.dao.service;

import fr.ul.miage.projetResto.model.entity.PerformanceEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class Service_PerformanceTest extends AbstractServiceTest {
    @Test
    public void testSavePerformance() {
        PerformanceEntity performanceEntity = easyRandom.nextObject(PerformanceEntity.class);

        when(performanceCollection.save(any())).thenReturn(true);

        boolean response = service.save(performanceEntity);

        Assertions.assertTrue(response);
    }

    @Test
    public void testGetById() {
        PerformanceEntity expected = easyRandom.nextObject(PerformanceEntity.class);

        when(performanceCollection.getPerformanceById(anyString())).thenReturn(expected);

        PerformanceEntity actual = service.getPerformanceById(expected.get_id());

        assertEqual(actual, expected);
    }

    @Test
    public void testGetByIdWithError() {
        PerformanceEntity expected = easyRandom.nextObject(PerformanceEntity.class);

        when(performanceCollection.getPerformanceById(anyString())).thenReturn(null);

        PerformanceEntity actual = service.getPerformanceById(expected.get_id());

        Assertions.assertNull(actual);
    }

    private void assertEqual(PerformanceEntity actual, PerformanceEntity expected) {
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(actual.get_id(), expected.get_id());
        Assertions.assertEquals(actual.getMinPreparationTime(), expected.getMinPreparationTime());
        Assertions.assertEquals(actual.getMinServiceTime(), expected.getMinServiceTime());
        Assertions.assertEquals(actual.getNbCustomers(), expected.getNbCustomers());
        Assertions.assertEquals(actual.getNbDish(), expected.getNbDish());
    }
}
