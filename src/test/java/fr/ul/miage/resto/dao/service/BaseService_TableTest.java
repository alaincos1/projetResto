package fr.ul.miage.resto.dao.service;

import fr.ul.miage.resto.constants.TableState;
import fr.ul.miage.resto.model.entity.TableEntity;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class BaseService_TableTest extends AbstractServiceTest {
    @Test
    void testSaveTable() {
        TableEntity tableEntity = easyRandom.nextObject(TableEntity.class);

        when(tableCollection.save(any())).thenReturn(true);

        boolean response = baseService.save(tableEntity);

        Assertions.assertTrue(response);
    }

    @Test
    void testUpdateTable() {
        TableEntity tableEntity = easyRandom.nextObject(TableEntity.class);

        when(tableCollection.update(any())).thenReturn(true);

        boolean response = baseService.update(tableEntity);

        Assertions.assertTrue(response);
    }

    @Test
    void testGetById() {
        TableEntity expected = easyRandom.nextObject(TableEntity.class);

        when(tableCollection.getTableById(anyString())).thenReturn(expected);

        TableEntity actual = baseService.getTableById(expected.getId());

        assertEqual(actual, expected);
    }

    @Test
    void testGetByIdWithError() {
        TableEntity expected = easyRandom.nextObject(TableEntity.class);

        when(tableCollection.getTableById(anyString())).thenReturn(null);

        TableEntity actual = baseService.getTableById(expected.getId());

        Assertions.assertNull(actual);
    }

    @Test
    void testGetAllTable() {
        List<TableEntity> expected = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            expected.add(easyRandom.nextObject(TableEntity.class));
        }

        when(tableCollection.getAll()).thenReturn(expected);

        List<TableEntity> actual = baseService.getAllTables();

        assertEqual(actual, expected);
    }

    @Test
    void testGetAllTableByServerOrHelper() {
        String user = RandomStringUtils.randomAlphabetic(10);

        List<TableEntity> expected = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            expected.add(easyRandom.nextObject(TableEntity.class));
        }

        when(tableCollection.getAllTableByServerOrHelper(anyString())).thenReturn(expected);

        List<TableEntity> actual = baseService.getAllTableByServerOrHelper(user);

        assertEqual(actual, expected);
    }

    @Test
    void testGetAllTableByServerOrHelperAndState() {
        String user = RandomStringUtils.randomAlphabetic(10);
        TableState tableState = easyRandom.nextObject(TableState.class);

        List<TableEntity> expected = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            expected.add(easyRandom.nextObject(TableEntity.class));
        }

        when(tableCollection.getAllTableByServerOrHelperAndState(anyString(), any(TableState.class))).thenReturn(expected);

        List<TableEntity> actual = baseService.getAllTableByServerOrHelperAndState(user, tableState);

        assertEqual(actual, expected);
    }

    @Test
    void testGetAllTableByState() {
        TableState tableState = easyRandom.nextObject(TableState.class);

        List<TableEntity> expected = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            expected.add(easyRandom.nextObject(TableEntity.class));
        }

        when(tableCollection.getAllTableByState(any(TableState.class))).thenReturn(expected);

        List<TableEntity> actual = baseService.getAllTableByState(tableState);

        assertEqual(actual, expected);
    }

    @Test
    void testDeleteTable() {
        String idTable = RandomStringUtils.randomAlphabetic(10);

        when(tableCollection.delete(anyString())).thenReturn(true);

        boolean actual = baseService.deleteTable(idTable);

        Assertions.assertTrue(actual);
    }

    @Test
    void testGetAllRemovableTables() {
        List<TableEntity> expected = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            expected.add(easyRandom.nextObject(TableEntity.class));
        }

        when(tableCollection.getAllRemovableTables()).thenReturn(expected);

        List<TableEntity> actual = baseService.getAllRemovableTables();

        assertEqual(actual, expected);
    }

    private void assertEqual(TableEntity actual, TableEntity expected) {
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(actual.getId(), expected.getId());
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
