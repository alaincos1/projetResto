package fr.ul.miage.projetResto.controller.role;

import fr.ul.miage.projetResto.appinfo.Service;
import fr.ul.miage.projetResto.constants.DishType;
import fr.ul.miage.projetResto.constants.MealType;
import fr.ul.miage.projetResto.constants.OrderState;
import fr.ul.miage.projetResto.constants.Role;
import fr.ul.miage.projetResto.constants.TableState;
import fr.ul.miage.projetResto.dao.service.BaseService;
import fr.ul.miage.projetResto.model.entity.BillEntity;
import fr.ul.miage.projetResto.model.entity.DishEntity;
import fr.ul.miage.projetResto.model.entity.OrderEntity;
import fr.ul.miage.projetResto.model.entity.TableEntity;
import fr.ul.miage.projetResto.model.entity.UserEntity;
import fr.ul.miage.projetResto.view.role.ButlerView;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@DisplayName("ButlerController")
@ExtendWith(MockitoExtension.class)
public class ButlerControllerTest {
    @Mock
    ButlerView butlerView;
    @Mock
    BaseService baseService;
    @Mock
    Service service;
    @Spy
    @InjectMocks
    ButlerController butlerController;

    @Test
    @DisplayName("Réservation automatique pour le diner du jour si on est au service du déjeuner")
    void checkReturnMealTypeForTodayDiner() {
        String dateBooking = "2021/04/29";
        String dateToday = "2021/04/29";

        when(service.getDate()).thenReturn(dateToday);
        when(service.getMealType()).thenReturn(MealType.LUNCH);
        doNothing().when(butlerView).displayBookingDiner();
        doReturn(1).when(butlerController).getIntegerInput(anyInt(), anyInt());

        MealType toTested = butlerController.choiceMealTypeWithDate(dateBooking);
        assertEquals(toTested, MealType.DINNER);
    }

    @Test
    @DisplayName("Réservation impossible si c'est pour le jour et qu'on est au diner")
    void checkReturnMealTypeWithDateImpossible() {
        String dateBooking = "2022/04/29";
        String dateToday = "2022/04/29";

        when(service.getDate()).thenReturn(dateToday);
        when(service.getMealType()).thenReturn(MealType.DINNER);
        doNothing().when(butlerController).launch(any(Role.class));

        butlerController.choiceMealTypeWithDate(dateBooking);
        verify(butlerView, times(1)).displayBookingImpossible();
    }

    @Test
    @DisplayName("Réservation pour le service choisi (Dejeuner) par le client")
    void checkReturnMealTypeWithDateDejeuner() {
        String dateBooking = "2021/04/29";
        String dateToday = "2021/04/30";

        when(service.getDate()).thenReturn(dateToday);
        doNothing().when(butlerView).displayBookingService();
        doReturn(0).when(butlerController).getIntegerInput(anyInt(), anyInt());

        MealType toTested = butlerController.choiceMealTypeWithDate(dateBooking);
        assertEquals(toTested, MealType.LUNCH);
    }

    @Test
    @DisplayName("Réservation pour le service choisi (Diner) par le client")
    void checkReturnMealTypeWithDateDiner() {
        String dateBooking = "2021/04/29";
        String dateToday = "2021/04/30";

        when(service.getDate()).thenReturn(dateToday);
        doNothing().when(butlerView).displayBookingService();
        doReturn(1).when(butlerController).getIntegerInput(anyInt(), anyInt());

        MealType toTested = butlerController.choiceMealTypeWithDate(dateBooking);
        assertEquals(toTested, MealType.DINNER);
    }

    @Test
    @DisplayName("Retourne le mon mealType en fonction de l'entrée de l'utilisateur : Dejeuner")
    void choiceMealTypeTestDejeuner() {
        MealType test = butlerController.choiceMealType(0);
        assertEquals(MealType.LUNCH, test);
    }

    @Test
    @DisplayName("Retourne le mon mealType en fonction de l'entrée de l'utilisateur : Diner")
    void choiceMealTypeTestDiner() {
        MealType test = butlerController.choiceMealType(1);
        assertEquals(MealType.DINNER, test);
    }

    @Test
    @DisplayName("Vérifier l'existence d'une table par son id et son état")
    void isTableIdCorrectTestReturnTrue() {
        TableEntity table = new TableEntity();
        table.set_id("1");
        table.setIdHelper("hel1");
        table.setIdServer("ser1");
        table.setNbSeats(2);
        table.setTableState(TableState.Free);

        when(baseService.getTableById(anyString())).thenReturn(table);

        assertTrue(butlerController.isTableIdCorrect("1", TableState.Free));
    }

    @Test
    @DisplayName("Vérifier l'existence d'une table par son id et son état")
    void isTableIdCorrectTestReturnFalse() {
        TableEntity table = new TableEntity();
        table.set_id("1");
        table.setIdHelper("hel1");
        table.setIdServer("ser1");
        table.setNbSeats(2);
        table.setTableState(TableState.Free);

        when(baseService.getTableById(anyString())).thenReturn(table);

        assertFalse(butlerController.isTableIdCorrect("1", TableState.Booked));
    }

    @Test
    @DisplayName("Vérifier l'existence d'une table par son id et son état")
    void isTableIdCorrectTestReturnFalse_FalseId() {
        TableEntity table = new TableEntity();
        table.set_id("1");
        table.setIdHelper("hel1");
        table.setIdServer("ser1");
        table.setNbSeats(2);
        table.setTableState(TableState.Free);

        when(baseService.getTableById(anyString())).thenReturn(null);

        assertFalse(butlerController.isTableIdCorrect("2", TableState.Free));
    }

    @Test
    @DisplayName("Retourne l'id correct de la table entrée par l'utilisateur, état correct")
    void choiceTableTestReturnIdTableCorrect() {
        doReturn("1").when(butlerController).getStringInput();
        doReturn(true).when(butlerController).isTableIdCorrect(anyString(), any(TableState.class));

        assertEquals("1", butlerController.choiceTable(TableState.Free));
    }

    @Test
    @DisplayName("choix de la table avec état incorrect")
    void choiceTableTestReturnFalse() {
        doReturn("1").when(butlerController).getStringInput();
        doNothing().when(butlerView).displayInputIncorrect();
        doReturn(false).when(butlerController).isTableIdCorrect(anyString(), any(TableState.class));
        doCallRealMethod().doReturn("1").when(butlerController).choiceTable(any(TableState.class));

        butlerController.choiceTable(TableState.Booked);

        verify(butlerView, times(1)).displayInputIncorrect();
        verify(butlerController, times(2)).choiceTable(any(TableState.class));
    }

    @Test
    @DisplayName("Return true pour le choix de la table")
    void choiceTableTestReturnTrue() {
        doReturn("1").when(butlerController).getStringInput();
        doReturn(true).when(butlerController).isTableIdCorrect(anyString(), any(TableState.class));

        assertEquals("1", butlerController.choiceTable(TableState.Booked));
    }

    @Test
    @DisplayName("choix de la table correct avec id serveur")
    void choiceTableServerCorrectServer() {
        UserEntity user = new UserEntity();
        user.set_id("hel1");
        user.setRole(Role.Server);

        doReturn("1").when(butlerController).getStringInput();
        doReturn(true).when(butlerController).isTableIdCorrectServer("1", user);

        assertEquals("1", butlerController.choiceTableServer(user));
    }

    @Test
    @DisplayName("choix de la table incorrect avec id serveur false")
    void choiceTableServerCorrectServerFalse() {
        UserEntity user = new UserEntity();
        user.set_id("hel1");
        user.setRole(Role.Server);

        doReturn("1").when(butlerController).getStringInput();
        doNothing().when(butlerView).displayInputIncorrect();
        doReturn(false).when(butlerController).isTableIdCorrectServer("1", user);
        doCallRealMethod().doReturn("1").when(butlerController).choiceTableServer(user);

        butlerController.choiceTableServer(user);

        verify(butlerView, times(1)).displayInputIncorrect();
        verify(butlerController, times(2)).choiceTableServer(user);
    }

    @Test
    @DisplayName("Vérifier que la méthode retourne false quand l'utilisateur choisit une table incorrecte car le assistant est deja assigné à la table ")
    void isTableIdCorrectServerTestReturnFalseHelper() {
        UserEntity user = new UserEntity();
        user.set_id("hel1");
        user.setRole(Role.Helper);

        TableEntity table = new TableEntity();
        table.set_id("1");
        table.setIdHelper("hel1");
        table.setIdServer("ser1");
        table.setNbSeats(2);
        table.setTableState(TableState.Free);

        doReturn(table).when(baseService).getTableById(anyString());

        assertFalse(butlerController.isTableIdCorrectServer("1", user));
    }

    @Test
    @DisplayName("Vérifier que la méthode retourne true quand l'utilisateur choisit une table correcte avec serveur")
    void isTableIdCorrectServerTestReturnTrueServer() {
        UserEntity user = new UserEntity();
        user.set_id("test");
        user.setRole(Role.Server);

        TableEntity table = new TableEntity();
        table.set_id("1");
        table.setIdHelper("hel1");
        table.setIdServer("ser1");
        table.setNbSeats(2);
        table.setTableState(TableState.Free);

        doReturn(table).when(baseService).getTableById(anyString());

        assertTrue(butlerController.isTableIdCorrectServer("1", user));
    }

    @Test
    @DisplayName("Vérifier que la méthode retourne true quand l'utilisateur choisit une table correcte avec assistant")
    void isTableIdCorrectServerTestReturnTrueHelper() {
        UserEntity user = new UserEntity();
        user.set_id("test");
        user.setRole(Role.Helper);

        TableEntity table = new TableEntity();
        table.set_id("1");
        table.setIdHelper("hel1");
        table.setIdServer("ser1");
        table.setNbSeats(2);
        table.setTableState(TableState.Free);

        doReturn(table).when(baseService).getTableById(anyString());

        assertTrue(butlerController.isTableIdCorrectServer("1", user));
    }

    @Test
    @DisplayName("Vérifier que la méthode retourne false quand l'utilisateur choisit une table incorrecte car le serveur est deja assigné à la table ")
    void isTableIdCorrectServerTestReturnFalseServer() {
        UserEntity user = new UserEntity();
        user.set_id("ser1");
        user.setRole(Role.Server);

        TableEntity table = new TableEntity();
        table.set_id("1");
        table.setIdHelper("hel1");
        table.setIdServer("ser1");
        table.setNbSeats(2);
        table.setTableState(TableState.Free);

        doReturn(table).when(baseService).getTableById(anyString());

        assertFalse(butlerController.isTableIdCorrectServer("1", user));
    }

    @Test
    @DisplayName("L'utilisateur ne veut pas de réservation")
    void choiceReservationTestReturnNull() {
        assertNull(butlerController.choiceReservation(null, 0));
    }

    @Test
    @DisplayName("Pas de réservation aujourd'hui")
    void choiceReservationTestReturn0() {
        service.setDate("2021/04/29");
        service.setMealType(MealType.DINNER);
        List<TableEntity> tables = baseService.getAllTables();
        when(butlerView.displayAllTablesWithBooking(tables, service.getDate(), service.getMealType(), baseService)).thenReturn(0);
        doNothing().when(butlerView).displayAnyBooking();

        butlerController.choiceReservation(baseService.getAllTables(), 1);

        verify(butlerView, times(1)).displayAnyBooking();
    }

    @Test
    @DisplayName("Retourner l'id de la table réservée")
    void choiceReservationTestReturnId() {
        service.setDate("2021/04/29");
        service.setMealType(MealType.DINNER);
        List<TableEntity> tables = baseService.getAllTables();
        when(butlerView.displayAllTablesWithBooking(tables, service.getDate(), service.getMealType(), baseService)).thenReturn(1);
        doNothing().when(butlerView).displayChoiceTableClient();
        doReturn("1").when(butlerController).choiceTable(any(TableState.class));

        String test = butlerController.choiceReservation(baseService.getAllTables(), 1);
        assertEquals("1", test);
    }

    @Test 
    @DisplayName("Verifier la taille de la liste retournée return 2")
    void listDishestTestOneOrder() {
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
    	order1.setIdTable("2");
    	order1.setOrderState(OrderState.Served);
    	order1.setRank(1);
    	order1.setIdsDish(list);
    	
    	TableEntity table = new TableEntity();
        table.set_id("1");
        table.setIdHelper("hel1");
        table.setIdServer("ser1");
        table.setNbSeats(2);
        table.setTableState(TableState.Dessert);
    	
        List<OrderEntity> listOrder = new ArrayList<>();
        listOrder.add(order);
        listOrder.add(order1);

        when(baseService.getServedOrders()).thenReturn(listOrder);
        
        assertEquals(2, butlerController.listDishes(table).size());
    }
    
    @Test 
    @DisplayName("Verifier la taille de la liste retournée return 4")
    void listDishestTestTwoOrders() {
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
    	
    	TableEntity table = new TableEntity();
        table.set_id("1");
        table.setIdHelper("hel1");
        table.setIdServer("ser1");
        table.setNbSeats(2);
        table.setTableState(TableState.Dessert);
    	
        List<OrderEntity> listOrder = new ArrayList<>();
        listOrder.add(order);
        listOrder.add(order1);

        when(baseService.getServedOrders()).thenReturn(listOrder);
        
        assertEquals(3, butlerController.listDishes(table).size());
    }
    
    @Test
    @DisplayName("Calculer le prix d'un facture : 20")
    void priceBillTest20() {
    	List<String> listProducts = new ArrayList<>();
    	listProducts.add("1");
    	listProducts.add("2");
    	DishEntity dish1 = new DishEntity();
    	dish1.set_id("1");
    	dish1.setDishType(DishType.Dessert);
    	dish1.setIdCategory("Viande");
    	dish1.setIdsProduct(listProducts);
    	dish1.setOnTheMenu(true);
    	dish1.setPrice(15);

    	DishEntity dish2 = new DishEntity();
    	dish2.set_id("2");
    	dish2.setDishType(DishType.Dessert);
    	dish2.setIdCategory("Boisson");
    	dish2.setIdsProduct(listProducts);
    	dish2.setOnTheMenu(true);
    	dish2.setPrice(5);
    	
    	List<String> listDish = new ArrayList<>();
    	listDish.add("1");
    	listDish.add("2");
    	
    	when(baseService.getDishById("1")).thenReturn(dish1);
    	when(baseService.getDishById("2")).thenReturn(dish2);
    	doNothing().when(butlerView).displayDishes(anyString(), anyInt());
    	
    	assertEquals(20, butlerController.priceBill(listDish));
    }
    
    @Test
    @DisplayName("Vérifier que l'édition d'une facture s'effectue")
    void editBillsCorrect() {
    	TableEntity table = new TableEntity();
        table.set_id("1");
        table.setIdHelper("hel1");
        table.setIdServer("ser1");
        table.setNbSeats(2);
        table.setTableState(TableState.Dessert);
        
        List<TableEntity> list = new ArrayList<>();
        list.add(table);
        
        doReturn(2).when(butlerView).displayAllTablesForBill(anyList(), any(BaseService.class));
    	when(butlerView.orderServed(table, baseService)).thenReturn(true);
    	when(butlerView.stateForBill(table)).thenReturn(true);
    	doNothing().when(butlerView).displayChoiceTableForBill();
    	doReturn("1").when(butlerController).getStringInput();
    	when(baseService.getTableById(anyString())).thenReturn(table);
    	doNothing().when(butlerController).saveObject(any(BillEntity.class));
    	doNothing().when(butlerController).launch(any(Role.class));
    	
    	butlerController.editBills();
    	
    	verify(butlerController, times(1)).saveObject(any(BillEntity.class));
    }
    
    @Test
    @DisplayName("Vérifier que l'édition d'une facture ne s'éffectue pas car pas de table au bon état")
    void editBillsFailedAnyTable() {
    	TableEntity table = new TableEntity();
        table.set_id("1");
        table.setIdHelper("hel1");
        table.setIdServer("ser1");
        table.setNbSeats(2);
        table.setTableState(TableState.Dessert);
        
        List<TableEntity> list = new ArrayList<>();
        list.add(table);
        
        doReturn(0).when(butlerView).displayAllTablesForBill(anyList(), any(BaseService.class));
        doNothing().when(butlerView).displayBillImpossible();
        doNothing().when(butlerController).launch(any(Role.class));
    	
    	butlerController.editBills();
    	
    	verify(butlerView, times(1)).displayBillImpossible();
    }
    
    @Test
    @DisplayName("Vérifier que l'édition d'une facture ne s'éffectue pas car la table entrée est incorrect")
    void editBillsFailedChoiceTable() {
       	TableEntity table = new TableEntity();
        table.set_id("1");
        table.setIdHelper("hel1");
        table.setIdServer("ser1");
        table.setNbSeats(2);
        table.setTableState(TableState.Dessert);
        
        List<TableEntity> list = new ArrayList<>();
        list.add(table);

    	doReturn("3").when(butlerController).getStringInput();
    	when(baseService.getTableById(anyString())).thenReturn(null);
        doReturn(2).when(butlerView).displayAllTablesForBill(anyList(), any(BaseService.class));
    	doNothing().when(butlerView).displayInputIncorrect();
    	doNothing().when(butlerController).launch(any(Role.class));
    	doCallRealMethod().doNothing().when(butlerController).editBills();
    	
    	butlerController.editBills();
    	
    	verify(butlerView, times(1)).displayInputIncorrect();
    }
    
    @Test
    @DisplayName("Vérifier que la table se met pas à jour quand le user choisi n'existe pas")
    void affectTablesToServerTestIncorrect() {
       	TableEntity table = new TableEntity();
        table.set_id("1");
        table.setIdHelper("hel1");
        table.setIdServer("ser1");
        table.setNbSeats(2);
        table.setTableState(TableState.Dessert);
        
        List<TableEntity> listTable = new ArrayList<>();
        listTable.add(table);
        
        UserEntity user = new UserEntity();
        user.set_id("ser1");
        user.setRole(Role.Server);

        List<UserEntity> listUser = new ArrayList<>();
        listUser.add(user);
        
        when(baseService.getAllUsers()).thenReturn(listUser);
        when(baseService.getAllTables()).thenReturn(listTable);
    	doNothing().when(butlerView).displayAllTables(listTable);
    	doNothing().when(butlerView).displayChoiceServer();
    	doNothing().when(butlerView).displayServersList(listUser);
    	doReturn("test").when(butlerController).getUserIdInput();
        when(baseService.getUserById(anyString())).thenReturn(null);
        doNothing().when(butlerView).displayInputIncorrect();
        doCallRealMethod().doNothing().when(butlerController).affectTablesToServer();
        when(baseService.getTableById(anyString())).thenReturn(table);
        doNothing().when(butlerController).updateObject(any(TableEntity.class));
        
    	butlerController.affectTablesToServer();
    	
    	verify(butlerView, times(1)).displayInputIncorrect();
    }
    
    @Test
    @DisplayName("Vérifier que la table se met à jour avec le nouveau serveur/assistant")
    void affectTablesToServerTestCorrect() {
       	TableEntity table = new TableEntity();
        table.set_id("1");
        table.setIdHelper("hel1");
        table.setIdServer("ser1");
        table.setNbSeats(2);
        table.setTableState(TableState.Dessert);
        
        List<TableEntity> listTable = new ArrayList<>();
        listTable.add(table);
        
        UserEntity user = new UserEntity();
        user.set_id("ser2");
        user.setRole(Role.Server);

        List<UserEntity> listUser = new ArrayList<>();
        listUser.add(user);
        
        when(baseService.getAllUsers()).thenReturn(listUser);
        when(baseService.getAllTables()).thenReturn(listTable);
    	doNothing().when(butlerView).displayAllTables(listTable);
    	doNothing().when(butlerView).displayChoiceServer();
    	doNothing().when(butlerView).displayServersList(listUser);
    	doReturn("ser2").when(butlerController).getUserIdInput();
        when(baseService.getUserById(anyString())).thenReturn(user);
    	doNothing().when(butlerView).displayChoiceTableServer(anyString());
    	doReturn(1).when(butlerView).displayTablesList(listTable, "ser2", null);
    	doReturn("1").when(butlerController).choiceTableServer(user);
        
        when(baseService.getTableById(anyString())).thenReturn(table);
        doNothing().when(butlerController).updateObject(any(TableEntity.class));
        
    	butlerController.affectTablesToServer();

    	verify(butlerView, times(0)).displayInputIncorrect();
    	verify(butlerController, times(1)).updateObject(any(TableEntity.class));
    }
    
    @Test
    @DisplayName("Vérifier que l'affectation des clients se fait")
    void affectTablesToCLientsTestCorrect() {
       	TableEntity table = new TableEntity();
        table.set_id("1");
        table.setIdHelper("hel1");
        table.setIdServer("ser1");
        table.setNbSeats(2);
        table.setTableState(TableState.Dessert);
        
        List<TableEntity> listTable = new ArrayList<>();
        listTable.add(table);
        
        when(baseService.getAllTables()).thenReturn(listTable);
    	doNothing().when(butlerView).displayIsABill();
    	
    	doReturn(0).when(butlerController).getIntegerInput(anyInt(), anyInt());
    	doReturn("1").when(butlerController).choiceReservation(listTable, 0);
    	doReturn(2).when(butlerView).displayTablesList(listTable, null, "Libre");
    	doNothing().when(butlerView).displayChoiceTableClient();
    	doReturn("1").when(butlerController).choiceTable(any(TableState.class));

        when(baseService.getTableById(anyString())).thenReturn(table);
        doNothing().when(butlerController).updateObject(any(TableEntity.class));
        
    	butlerController.affectTablesToClients();

    	verify(butlerView, times(1)).displayChoiceTableClient();
    	verify(butlerController, times(1)).updateObject(any(TableEntity.class));
    }
    
    @Test
    @DisplayName("Vérifier que l'affectation des clients ne se fait pas si il n'y a pas de tables")
    void affectTablesToCLientsTestIncorrect() {
       	TableEntity table = new TableEntity();
        table.set_id("1");
        table.setIdHelper("hel1");
        table.setIdServer("ser1");
        table.setNbSeats(2);
        table.setTableState(TableState.Dessert);
        
        List<TableEntity> listTable = new ArrayList<>();
        
        when(baseService.getAllTables()).thenReturn(listTable);
    	doNothing().when(butlerView).displayIsABill();
    	
    	doReturn(0).when(butlerController).getIntegerInput(anyInt(), anyInt());
    	doReturn("1").when(butlerController).choiceReservation(listTable, 0);
    	doReturn(0).when(butlerView).displayTablesList(listTable, null, "Libre");
    	doNothing().when(butlerView).displayAnyTableFree();
    	doNothing().when(butlerController).launch(any(Role.class));

        when(baseService.getTableById(anyString())).thenReturn(table);
        doNothing().when(butlerController).updateObject(any(TableEntity.class));
        
    	butlerController.affectTablesToClients();

    	verify(butlerView, times(0)).displayChoiceTableClient();
    	verify(butlerView, times(1)).displayAnyTableFree();
    	verify(butlerController, times(1)).launch(any(Role.class));
    	
    }
}
