package fr.ul.miage.projetResto.controller.role;

import fr.ul.miage.projetResto.appinfo.Service;
import fr.ul.miage.projetResto.constants.DishType;
import fr.ul.miage.projetResto.constants.Role;
import fr.ul.miage.projetResto.constants.TableState;
import fr.ul.miage.projetResto.dao.service.BaseService;
import fr.ul.miage.projetResto.model.entity.DishEntity;
import fr.ul.miage.projetResto.model.entity.OrderEntity;
import fr.ul.miage.projetResto.model.entity.TableEntity;
import fr.ul.miage.projetResto.model.entity.UserEntity;
import fr.ul.miage.projetResto.view.role.ServerView;
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

@DisplayName("ServerController")
@ExtendWith(MockitoExtension.class)
class ServerControllerTest {
    @Mock
    ServerView serverView;
    @Mock
    BaseService baseService;
    @Mock
    Service service;
    @Spy
    @InjectMocks
    ServerController serverController;

    @Test
    @DisplayName("Afficher les tables")
    void checkViewTables() {
        UserEntity user = new UserEntity();
        user.set_id("ser1");
        List<TableEntity> tables = new ArrayList<>();
        tables.add(new TableEntity());

        when(baseService.getAllTableByServerOrHelper(anyString())).thenReturn(tables);
        doNothing().when(serverController).askMainMenu();
        serverController.viewTables(user);
        verify(serverView, times(1)).displayTablesAffected(anyList());
    }

    @Test
    @DisplayName("Aucune table à afficher")
    void checkViewNoneTables() {
        UserEntity user = new UserEntity();
        user.set_id("ser1");
        List<TableEntity> tables = new ArrayList<>();

        when(baseService.getAllTableByServerOrHelper(anyString())).thenReturn(tables);
        doNothing().when(serverController).askMainMenu();
        serverController.viewTables(user);
        verify(serverView, times(1)).displayNoTablesAffected();
    }

    @Test
    @DisplayName("Déclare une table à débarasser")
    void checkSetTablesDirty() {
        UserEntity user = new UserEntity();
        user.set_id("ser1");
        List<TableEntity> tables = new ArrayList<>();
        tables.add(new TableEntity());
        tables.get(0).setTableState(TableState.Starter);
        when(baseService.getAllTableByServerOrHelperAndState(anyString(), any(TableState.class))).thenReturn(tables);
        doReturn(1).when(serverController).getIntegerInput(anyInt(), anyInt());
        doNothing().when(serverController).launch(Role.Server);

        serverController.setTablesDirty(user);

        verify(serverView, times(1)).displayTablesToDirty(anyList());
        verify(baseService, times(1)).update(any(TableEntity.class));
        assertEquals(TableState.Dirty, tables.get(0).getTableState());
    }

    @Test
    @DisplayName("Aucune table à déclarer à débarasser")
    void checkSetTablesDirtyNone() {
        UserEntity user = new UserEntity();
        user.set_id("ser1");
        List<TableEntity> tables = new ArrayList<>();
        when(baseService.getAllTableByServerOrHelperAndState(anyString(), any(TableState.class))).thenReturn(tables);
        doNothing().when(serverController).launch(Role.Server);

        serverController.setTablesDirty(user);

        verify(serverView, times(1)).displayNoTablesToDirty();
        verify(baseService, times(0)).update(any(TableEntity.class));
    }

    @Test
    @DisplayName("Annule la déclaration du débarassage d'une table")
    void checkSetTablesDirtyCancel() {
        UserEntity user = new UserEntity();
        user.set_id("ser1");
        List<TableEntity> tables = new ArrayList<>();
        tables.add(new TableEntity());
        tables.get(0).setTableState(TableState.Starter);
        when(baseService.getAllTableByServerOrHelperAndState(anyString(), any(TableState.class))).thenReturn(tables);
        doReturn(0).when(serverController).getIntegerInput(anyInt(), anyInt());
        doNothing().when(serverController).launch(Role.Server);

        serverController.setTablesDirty(user);

        verify(serverView, times(1)).displayTablesToDirty(anyList());
        verify(baseService, times(0)).update(any(TableEntity.class));
        assertEquals(TableState.Starter, tables.get(0).getTableState());
    }

    @Test
    @DisplayName("Servir une commande")
    void checkServeOrders() {
        UserEntity user = new UserEntity();
        user.set_id("ser1");
        List<TableEntity> tables = new ArrayList<>();
        List<OrderEntity> orders = new ArrayList<>();
        List<String> dishes = new ArrayList<>();
        dishes.add("plat");
        for(int i = 0; i < 3; i++){
            OrderEntity order = new OrderEntity();
            order.set_id(""+i);
            order.setIdTable(""+i);
            order.setIdsDish(dishes);
            orders.add(order);
        }
        DishEntity dishEntity = new DishEntity();
        dishEntity.setDishType(DishType.Starter);
        when(baseService.getAllTableByServerOrHelper(anyString())).thenReturn(tables);
        doReturn(orders).when(serverController).getOnlyServerOrders(anyList(), anyList());
        doReturn(1).when(serverController).getIntegerInput(anyInt(), anyInt());
        when(baseService.getTableById(anyString())).thenReturn(new TableEntity());
        when(baseService.getDishById(anyString())).thenReturn(dishEntity);
        doNothing().when(serverController).launch(Role.Server);

        serverController.serveOrders(user);

        verify(serverView, times(1)).displayOrdersToServe(anyList());
        verify(baseService, times(1)).update(any(OrderEntity.class));
    }

    @Test
    @DisplayName("Annuler le service d'une commande")
    void checkServeOrdersCancel() {
        UserEntity user = new UserEntity();
        user.set_id("ser1");
        List<TableEntity> tables = new ArrayList<>();
        List<OrderEntity> orders = new ArrayList<>();
        List<String> dishes = new ArrayList<>();
        dishes.add("plat");
        for(int i = 0; i < 3; i++){
            OrderEntity order = new OrderEntity();
            order.set_id(""+i);
            order.setIdTable(""+i);
            order.setIdsDish(dishes);
            orders.add(order);
        }
        DishEntity dishEntity = new DishEntity();
        dishEntity.setDishType(DishType.Starter);
        when(baseService.getAllTableByServerOrHelper(anyString())).thenReturn(tables);
        doReturn(orders).when(serverController).getOnlyServerOrders(anyList(), anyList());
        doReturn(0).when(serverController).getIntegerInput(anyInt(), anyInt());
        doNothing().when(serverController).launch(Role.Server);

        serverController.serveOrders(user);

        verify(serverView, times(1)).displayOrdersToServe(anyList());
        verify(baseService, times(0)).update(any(OrderEntity.class));
    }

    @Test
    @DisplayName("Aucune commande à servir")
    void checkServeOrdersNone() {
        UserEntity user = new UserEntity();
        user.set_id("ser1");
        List<TableEntity> tables = new ArrayList<>();
        List<OrderEntity> orders = new ArrayList<>();
        when(baseService.getAllTableByServerOrHelper(anyString())).thenReturn(tables);
        doReturn(orders).when(serverController).getOnlyServerOrders(anyList(), anyList());
        doNothing().when(serverController).launch(Role.Server);

        serverController.serveOrders(user);

        verify(serverView, times(1)).displayNoOrdersToServe();
        verify(baseService, times(0)).update(any(OrderEntity.class));
    }

    @Test
    @DisplayName("Récupère uniquement les commandes de cet utilisateur")
    void checkGetOnlyServerOrders() {
        List<OrderEntity> orders = new ArrayList<>();
        List<OrderEntity> orderExpected = new ArrayList<>();
        OrderEntity order = null;
        for(int i = 0; i < 3; i++){
            order = new OrderEntity();
            order.setIdTable(""+i);
            orders.add(order);
        }
        orderExpected.add(order);

        List<TableEntity> tables = new ArrayList<>();
        TableEntity table = new TableEntity();
        table.set_id("2");
        tables.add(table);

        List<OrderEntity> ordersActual = serverController.getOnlyServerOrders(orders, tables);
        assertEquals(orderExpected, ordersActual);
    }
}
