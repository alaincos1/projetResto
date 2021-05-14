package fr.ul.miage.resto.dao.service;

import fr.ul.miage.resto.model.entity.PerformanceEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class BaseService_PerformanceTest extends AbstractServiceTest {
    @Test
    void testSavePerformance() {
        PerformanceEntity performanceEntity = easyRandom.nextObject(PerformanceEntity.class);

        when(performanceCollection.save(any())).thenReturn(true);

        boolean response = baseService.save(performanceEntity);

        Assertions.assertTrue(response);
    }

    @Test
    void testUpdatePerformance() {
        PerformanceEntity performanceEntity = easyRandom.nextObject(PerformanceEntity.class);

        when(performanceCollection.update(any())).thenReturn(true);

        boolean response = baseService.update(performanceEntity);

        Assertions.assertTrue(response);
    }

    @Test
    void testGetById() {
        PerformanceEntity expected = easyRandom.nextObject(PerformanceEntity.class);

        when(performanceCollection.getPerformanceById(anyString())).thenReturn(expected);

        PerformanceEntity actual = baseService.getPerformanceById(expected.getId());

        assertEqual(actual, expected);
    }

    @Test
    void testGetByIdWithError() {
        PerformanceEntity expected = easyRandom.nextObject(PerformanceEntity.class);

        when(performanceCollection.getPerformanceById(anyString())).thenReturn(null);

        PerformanceEntity actual = baseService.getPerformanceById(expected.getId());

        Assertions.assertNull(actual);
    }

    private void assertEqual(PerformanceEntity actual, PerformanceEntity expected) {
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(actual.getId(), expected.getId());
        Assertions.assertEquals(actual.getPreparationTime(), expected.getPreparationTime());
        Assertions.assertEquals(actual.getServiceTime(), expected.getServiceTime());
        Assertions.assertEquals(actual.getNbTableServed(), expected.getNbTableServed());
        Assertions.assertEquals(actual.getNbOrder(), expected.getNbOrder());
    }
}
