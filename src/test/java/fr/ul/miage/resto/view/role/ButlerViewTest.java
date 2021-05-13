package fr.ul.miage.resto.view.role;

import fr.ul.miage.resto.constants.OrderState;
import fr.ul.miage.resto.constants.TableState;
import fr.ul.miage.resto.dao.service.BaseService;
import fr.ul.miage.resto.model.entity.OrderEntity;
import fr.ul.miage.resto.model.entity.TableEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@DisplayName("ButlerView")
@ExtendWith(MockitoExtension.class)
class ButlerViewTest {
    @Mock
    BaseService baseService;

    ButlerView butlerView = new ButlerView();

    @Test
    @DisplayName("Retourne true car état de la table est correcte")
    void testStateForBillTestReturnTrue() {
        TableEntity table = new TableEntity();
        table.set_id("1");
        table.setIdHelper("hel1");
        table.setIdServer("ser1");
        table.setNbSeats(2);
        table.setTableState(TableState.DESSERT);

        assertTrue(butlerView.stateForBill(table));
    }

    @Test
    @DisplayName("Retourne false car état de la table est incorrecte")
    void testSstateForBillTestReturnFalse() {
        TableEntity table = new TableEntity();
        table.set_id("1");
        table.setIdHelper("hel1");
        table.setIdServer("ser1");
        table.setNbSeats(2);
        table.setTableState(TableState.FREE);

        assertFalse(butlerView.stateForBill(table));
    }

    @Test
    @DisplayName("Retourne true car état de la table est correct, des commandes ont été servi")
    void testOrderServedTestReturnTrue() {
        TableEntity table = new TableEntity();
        table.set_id("1");
        table.setIdHelper("hel1");
        table.setIdServer("ser1");
        table.setNbSeats(2);
        table.setTableState(TableState.FREE);

        OrderEntity order = new OrderEntity();
        order.set_id("1");
        order.setChildOrder(false);
        order.setIdTable("1");
        order.setOrderState(OrderState.SERVED);
        order.setRank(1);
        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        order.setIdsDish(list);

        OrderEntity order1 = new OrderEntity();
        order1.set_id("2");
        order1.setChildOrder(false);
        order1.setIdTable("1");
        order1.setOrderState(OrderState.SERVED);
        order1.setRank(1);
        List<String> list1 = new ArrayList<>();
        list1.add("1");
        order1.setIdsDish(list1);

        List<OrderEntity> listOrder = new ArrayList<>();
        listOrder.add(order);
        listOrder.add(order1);

        when(baseService.getServedOrders()).thenReturn(listOrder);

        assertTrue(butlerView.orderServed(table, baseService));
    }

    @Test
    @DisplayName("Retourne false car état de la table est incorrecte, aucun commande n'a été servi")
    void testOrderServedTestReturnFalse() {
        TableEntity table = new TableEntity();
        table.set_id("1");
        table.setIdHelper("hel1");
        table.setIdServer("ser1");
        table.setNbSeats(2);
        table.setTableState(TableState.FREE);

        List<OrderEntity> listOrder = new ArrayList<>();

        when(baseService.getServedOrders()).thenReturn(listOrder);

        assertFalse(butlerView.orderServed(table, baseService));
    }

}
