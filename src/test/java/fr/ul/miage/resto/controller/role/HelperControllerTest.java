package fr.ul.miage.resto.controller.role;

import fr.ul.miage.resto.constants.Role;
import fr.ul.miage.resto.constants.TableState;
import fr.ul.miage.resto.dao.service.BaseService;
import fr.ul.miage.resto.model.entity.TableEntity;
import fr.ul.miage.resto.model.entity.UserEntity;
import fr.ul.miage.resto.view.role.HelperView;
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
    @Spy
    @InjectMocks
    HelperController helperController;

    @Test
    @DisplayName("Afficher les tables")
    void testViewTables() {
        UserEntity user = new UserEntity();
        user.setId("hel1");
        List<TableEntity> tables = new ArrayList<>();
        tables.add(new TableEntity());
        when(baseService.getAllTableByServerOrHelper(anyString())).thenReturn(tables);
        doNothing().when(helperController).askMainMenu();

        helperController.viewTables(user);

        verify(helperView, times(1)).displayTablesAffected(anyList());
    }

    @Test
    @DisplayName("Aucune table à afficher")
    void testViewNoneTables() {
        UserEntity user = new UserEntity();
        user.setId("hel1");
        List<TableEntity> tables = new ArrayList<>();
        when(baseService.getAllTableByServerOrHelper(anyString())).thenReturn(tables);
        doNothing().when(helperController).askMainMenu();

        helperController.viewTables(user);

        verify(helperView, times(1)).displayNoTablesAffected();
    }

    @Test
    @DisplayName("Nettoie une table")
    void testCleanTables() {
        UserEntity user = new UserEntity();
        user.setId("hel1");
        List<TableEntity> tables = new ArrayList<>();
        tables.add(new TableEntity());
        tables.get(0).setTableState(TableState.DIRTY);
        when(baseService.getAllTableByServerOrHelperAndState(anyString(), any(TableState.class))).thenReturn(tables);
        doReturn(1).when(helperController).getIntegerInput(anyInt(), anyInt());
        doNothing().when(helperController).launch(Role.HELPER);

        helperController.cleanTables(user);

        verify(helperView, times(1)).displayTablesToClean(anyList());
        verify(baseService, times(1)).update(any(TableEntity.class));
        assertEquals(TableState.FREE, tables.get(0).getTableState());
    }

    @Test
    @DisplayName("Aucune table à nettoyer")
    void testCleanNoneTables() {
        UserEntity user = new UserEntity();
        user.setId("hel1");
        List<TableEntity> tables = new ArrayList<>();
        when(baseService.getAllTableByServerOrHelperAndState(anyString(), any(TableState.class))).thenReturn(tables);
        doNothing().when(helperController).launch(Role.HELPER);

        helperController.cleanTables(user);

        verify(helperView, times(1)).displayMessage("Aucune table à nettoyer.");
        verify(baseService, times(0)).update(any(TableEntity.class));
    }

    @Test
    @DisplayName("Annule le nettoyage une table")
    void testCleanTablesCancel() {
        UserEntity user = new UserEntity();
        user.setId("hel1");
        List<TableEntity> tables = new ArrayList<>();
        tables.add(new TableEntity());
        tables.get(0).setTableState(TableState.DIRTY);
        when(baseService.getAllTableByServerOrHelperAndState(anyString(), any(TableState.class))).thenReturn(tables);
        doReturn(0).when(helperController).getIntegerInput(anyInt(), anyInt());
        doNothing().when(helperController).launch(Role.HELPER);

        helperController.cleanTables(user);

        verify(helperView, times(1)).displayTablesToClean(anyList());
        verify(baseService, times(0)).update(any(TableEntity.class));
        assertEquals(TableState.DIRTY, tables.get(0).getTableState());
    }
}
