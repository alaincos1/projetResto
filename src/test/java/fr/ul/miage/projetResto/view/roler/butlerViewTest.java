package fr.ul.miage.projetResto.view.roler;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import fr.ul.miage.projetResto.constants.OrderState;
import fr.ul.miage.projetResto.constants.TableState;
import fr.ul.miage.projetResto.dao.service.BaseService;
import fr.ul.miage.projetResto.model.entity.OrderEntity;
import fr.ul.miage.projetResto.model.entity.TableEntity;
import fr.ul.miage.projetResto.view.role.ButlerView;
import static org.mockito.Mockito.*;

@DisplayName("ButlerView")
@ExtendWith(MockitoExtension.class)
public class butlerViewTest {
	@Mock
	BaseService baseService;
	
	ButlerView butlerView;

	@Test
	@DisplayName("Retourne true car état de la table est correcte")
	void stateForBillTestReturnTrue() {
		TableEntity table = new TableEntity();
		table.set_id("1");
		table.setIdHelper("hel1");
		table.setIdServer("ser1");
		table.setNbSeats(2);
		table.setTableState(TableState.Dessert);

		assertTrue(butlerView.stateForBill(table));
	}

	@Test
	@DisplayName("Retourne false car état de la table est incorrecte")
	void stateForBillTestReturnFalse() {
		TableEntity table = new TableEntity();
		table.set_id("1");
		table.setIdHelper("hel1");
		table.setIdServer("ser1");
		table.setNbSeats(2);
		table.setTableState(TableState.Free);

		assertFalse(butlerView.stateForBill(table));
	}

	@Test
	@DisplayName("Retourne true car état de la table est incorrecte")
	void orderServedTestReturnTrue() {
		TableEntity table = new TableEntity();
		table.set_id("1");
		table.setIdHelper("hel1");
		table.setIdServer("ser1");
		table.setNbSeats(2);
		table.setTableState(TableState.Free);
		
    	OrderEntity order = new OrderEntity();
    	order.set_id("1");
    	order.setChildOrder(false);
    	order.setIdTable("1");
    	order.setOrderState(OrderState.Served);
    	order.setRank(1);
    	List<String> list = new ArrayList<>();
    	list.add("1");
    	list.add("2");
    	order.setIdsDish(list);
    	
    	OrderEntity order1 = new OrderEntity();
    	order1.set_id("2");
    	order1.setChildOrder(false);
    	order1.setIdTable("1");
    	order1.setOrderState(OrderState.Served);
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
	@DisplayName("Retourne false car état de la table est incorrecte")
	void orderServedTestReturnFalse() {
		TableEntity table = new TableEntity();
		table.set_id("1");
		table.setIdHelper("hel1");
		table.setIdServer("ser1");
		table.setNbSeats(2);
		table.setTableState(TableState.Free);
		
    	OrderEntity order = new OrderEntity();
    	order.set_id("1");
    	order.setChildOrder(false);
    	order.setIdTable("1");
    	order.setOrderState(OrderState.Prepared);
    	order.setRank(1);
    	List<String> list = new ArrayList<>();
    	list.add("1");
    	list.add("2");
    	order.setIdsDish(list);
    	
    	OrderEntity order1 = new OrderEntity();
    	order1.set_id("2");
    	order1.setChildOrder(false);
    	order1.setIdTable("1");
    	order1.setOrderState(OrderState.Ordered);
    	order1.setRank(1);
    	List<String> list1 = new ArrayList<>();
    	list1.add("1");
    	order1.setIdsDish(list1);
    	
		List<OrderEntity> listOrder = new ArrayList<>();
		listOrder.add(order);
		listOrder.add(order1);

		when(baseService.getServedOrders()).thenReturn(listOrder);

		assertFalse(butlerView.orderServed(table, baseService));
	}

}