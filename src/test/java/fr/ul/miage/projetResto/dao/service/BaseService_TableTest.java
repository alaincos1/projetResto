package fr.ul.miage.projetResto.dao.service;

import fr.ul.miage.projetResto.model.entity.TableEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class BaseService_TableTest extends AbstractServiceTest {
    @Test
    public void testSaveTable() {
        TableEntity tableEntity = easyRandom.nextObject(TableEntity.class);

        when(tableCollection.save(any())).thenReturn(true);

        boolean response = baseService.save(tableEntity);

        Assertions.assertTrue(response);
    }

    @Test
    public void testGetById() {
        TableEntity expected = easyRandom.nextObject(TableEntity.class);

        when(tableCollection.getTableById(anyString())).thenReturn(expected);

        TableEntity actual = baseService.getTableById(expected.get_id());

        assertEqual(actual, expected);
    }

    @Test
    public void testGetByIdWithError() {
        TableEntity expected = easyRandom.nextObject(TableEntity.class);

        when(tableCollection.getTableById(anyString())).thenReturn(null);

        TableEntity actual = baseService.getTableById(expected.get_id());

        Assertions.assertNull(actual);
    }

    private void assertEqual(TableEntity actual, TableEntity expected) {
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(actual.get_id(), expected.get_id());
        Assertions.assertEquals(actual.getIdsBill().size(), expected.getIdsBill().size());
        Assertions.assertEquals(actual.getIdsBooking().size(), expected.getIdsBooking().size());
        Assertions.assertEquals(actual.getIdUser1(), expected.getIdUser1());
        Assertions.assertEquals(actual.getIdUser2(), expected.getIdUser2());
        Assertions.assertEquals(actual.getTableState(), expected.getTableState());
    }
}
