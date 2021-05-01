package fr.ul.miage.projetResto.controller.role;

import fr.ul.miage.projetResto.appinfo.Service;
import fr.ul.miage.projetResto.constants.Role;
import fr.ul.miage.projetResto.constants.TableState;
import fr.ul.miage.projetResto.dao.service.BaseService;
import fr.ul.miage.projetResto.model.entity.TableEntity;
import fr.ul.miage.projetResto.model.entity.UserEntity;
import fr.ul.miage.projetResto.view.role.HelperView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@DisplayName("HelperController")
@ExtendWith(MockitoExtension.class)
class HelperControllerTest {
    @Mock
    HelperView helperView;
    @Mock
    BaseService baseService;
    @Mock
    Service service;
    @Spy
    @InjectMocks
    HelperController helperController;

    @Test
    @DisplayName("Afficher les tables")
    void checkViewTables() {
        UserEntity user = new UserEntity();
        user.set_id("hel1");
        List<TableEntity> tables = new ArrayList<>();
        tables.add(new TableEntity());
        when(baseService.getAllTableByServerOrHelper(anyString())).thenReturn(tables);
        doNothing().when(helperController).askMainMenu();

        helperController.viewTables(user);

        verify(helperView, times(1)).displayTablesAffected(anyList());
    }

    @Test
    @DisplayName("Aucune table à afficher")
    void checkViewNoneTables() {
        UserEntity user = new UserEntity();
        user.set_id("hel1");
        List<TableEntity> tables = new ArrayList<>();
        when(baseService.getAllTableByServerOrHelper(anyString())).thenReturn(tables);
        doNothing().when(helperController).askMainMenu();

        helperController.viewTables(user);

        verify(helperView, times(1)).displayNoTablesAffected();
    }

    @Test
    @DisplayName("Nettoie une table")
    void checkCleanTables() {
        UserEntity user = new UserEntity();
        user.set_id("hel1");
        List<TableEntity> tables = new ArrayList<>();
        tables.add(new TableEntity());
        tables.get(0).setTableState(TableState.Dirty);
        when(baseService.getAllTableByServerOrHelperAndState(anyString(), any(TableState.class))).thenReturn(tables);
        doReturn(1).when(helperController).getIntegerInput(anyInt(), anyInt());
        doNothing().when(helperController).launch(Role.Helper);

        helperController.cleanTables(user);

        verify(helperView, times(1)).displayTablesToClean(anyList());
        verify(baseService, times(1)).update(any(TableEntity.class));
        assertEquals(TableState.Free, tables.get(0).getTableState());
    }

    @Test
    @DisplayName("Aucune table à nettoyer")
    void checkCleanNoneTables() {
        UserEntity user = new UserEntity();
        user.set_id("hel1");
        List<TableEntity> tables = new ArrayList<>();
        when(baseService.getAllTableByServerOrHelperAndState(anyString(), any(TableState.class))).thenReturn(tables);
        doNothing().when(helperController).launch(Role.Helper);

        helperController.cleanTables(user);

        verify(helperView, times(1)).displayNoTablesToClean();
        verify(baseService, times(0)).update(any(TableEntity.class));
    }

    @Test
    @DisplayName("Annule le nettoyage une table")
    void checkCleanTablesCancel() {
        UserEntity user = new UserEntity();
        user.set_id("hel1");
        List<TableEntity> tables = new ArrayList<>();
        tables.add(new TableEntity());
        tables.get(0).setTableState(TableState.Dirty);
        when(baseService.getAllTableByServerOrHelperAndState(anyString(), any(TableState.class))).thenReturn(tables);
        doReturn(0).when(helperController).getIntegerInput(anyInt(), anyInt());
        doNothing().when(helperController).launch(Role.Helper);

        helperController.cleanTables(user);

        verify(helperView, times(1)).displayTablesToClean(anyList());
        verify(baseService, times(0)).update(any(TableEntity.class));
        assertEquals(TableState.Dirty, tables.get(0).getTableState());
    }
}
