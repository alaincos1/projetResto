package fr.ul.miage.projetResto.dao.service;

import fr.ul.miage.projetResto.model.entity.TableEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

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
    public void testUpdateTable() {
        TableEntity tableEntity = easyRandom.nextObject(TableEntity.class);

        when(tableCollection.update(any())).thenReturn(true);

        boolean response = baseService.update(tableEntity);

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

    @Test
    public void testGetAllTable() {
        List<TableEntity> expected = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            expected.add(easyRandom.nextObject(TableEntity.class));
        }

        when(baseService.getAllTable()).thenReturn(expected);

        List<TableEntity> actual = baseService.getAllTable();

        assertEqual(actual, expected);
    }

    private void assertEqual(TableEntity actual, TableEntity expected) {
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(actual.get_id(), expected.get_id());
        Assertions.assertEquals(actual.getTableState(), expected.getTableState());
        Assertions.assertEquals(actual.getNbSeats(), expected.getNbSeats());
        Assertions.assertEquals(actual.getIdServer(), expected.getIdServer());
        Assertions.assertEquals(actual.getIdHelper(), expected.getIdHelper());
    }

    private void assertEqual(List<TableEntity> actual, List<TableEntity> expected) {
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(actual.size(), expected.size());

        for (int i = 0; i < actual.size(); i++) {
            assertEqual(actual.get(i), expected.get(i));
        }
    }
}
