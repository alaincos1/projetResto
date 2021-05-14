package fr.ul.miage.resto.controller.role;

import fr.ul.miage.resto.constants.DishType;
import fr.ul.miage.resto.constants.OrderState;
import fr.ul.miage.resto.constants.Role;
import fr.ul.miage.resto.constants.TableState;
import fr.ul.miage.resto.dao.service.BaseService;
import fr.ul.miage.resto.model.entity.DishEntity;
import fr.ul.miage.resto.model.entity.OrderEntity;
import fr.ul.miage.resto.model.entity.TableEntity;
import fr.ul.miage.resto.model.entity.UserEntity;
import fr.ul.miage.resto.utils.MenuUtil;
import fr.ul.miage.resto.view.role.ServerView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("ServerController")
@ExtendWith(MockitoExtension.class)
class ServerControllerTest {
    @Mock
    ServerView serverView;
    @Mock
    BaseService baseService;
    @Spy
    @InjectMocks
    ServerController serverController;

    @Test
    @DisplayName("Afficher les tables")
    void testViewTables() {
        UserEntity user = new UserEntity();
        user.setId("ser1");
        List<TableEntity> tables = new ArrayList<>();
        tables.add(new TableEntity());

        when(baseService.getAllTableByServerOrHelper(anyString())).thenReturn(tables);
        doNothing().when(serverController).askMainMenu();
        serverController.viewTables(user);
        verify(serverView, times(1)).displayTablesAffected(anyList());
    }

    @Test
    @DisplayName("Aucune table à afficher")
    void testViewNoneTables() {
        UserEntity user = new UserEntity();
        user.setId("ser1");
        List<TableEntity> tables = new ArrayList<>();

        when(baseService.getAllTableByServerOrHelper(anyString())).thenReturn(tables);
        doNothing().when(serverController).askMainMenu();
        serverController.viewTables(user);
        verify(serverView, times(1)).displayNoTablesAffected();
    }

    @Test
    @DisplayName("Déclare une table à débarasser")
    void testSetTablesDirty() {
        UserEntity user = new UserEntity();
        user.setId("ser1");
        List<TableEntity> tables = new ArrayList<>();
        tables.add(new TableEntity());
        tables.get(0).setTableState(TableState.STARTER);
        when(baseService.getAllTableByServerOrHelperAndState(anyString(), any(TableState.class))).thenReturn(tables);
        doReturn(1).when(serverController).getIntegerInput(anyInt(), anyInt());
        doNothing().when(serverController).launch(Role.SERVER);

        serverController.setTablesDirty(user);

        verify(serverView, times(1)).displayTablesToDirty(anyList());
        verify(baseService, times(1)).update(any(TableEntity.class));
        assertEquals(TableState.DIRTY, tables.get(0).getTableState());
    }

    @Test
    @DisplayName("Aucune table à déclarer à débarasser")
    void testSetTablesDirtyNone() {
        UserEntity user = new UserEntity();
        user.setId("ser1");
        List<TableEntity> tables = new ArrayList<>();
        when(baseService.getAllTableByServerOrHelperAndState(anyString(), any(TableState.class))).thenReturn(tables);
        doNothing().when(serverController).launch(Role.SERVER);

        serverController.setTablesDirty(user);

        verify(serverView, times(1)).displayMessage("Aucune table à déclarer pour être débarassée.");
        verify(baseService, times(0)).update(any(TableEntity.class));
    }

    @Test
    @DisplayName("Annule la déclaration du débarassage d'une table")
    void testSetTablesDirtyCancel() {
        UserEntity user = new UserEntity();
        user.setId("ser1");
        List<TableEntity> tables = new ArrayList<>();
        tables.add(new TableEntity());
        tables.get(0).setTableState(TableState.STARTER);
        when(baseService.getAllTableByServerOrHelperAndState(anyString(), any(TableState.class))).thenReturn(tables);
        doReturn(0).when(serverController).getIntegerInput(anyInt(), anyInt());
        doNothing().when(serverController).launch(Role.SERVER);

        serverController.setTablesDirty(user);

        verify(serverView, times(1)).displayTablesToDirty(anyList());
        verify(baseService, times(0)).update(any(TableEntity.class));
        assertEquals(TableState.STARTER, tables.get(0).getTableState());
    }

    @Test
    @DisplayName("Servir une commande")
    void testServeOrders() {
        UserEntity user = new UserEntity();
        user.setId("ser1");
        List<TableEntity> tables = new ArrayList<>();
        List<OrderEntity> orders = new ArrayList<>();
        List<String> dishes = new ArrayList<>();
        dishes.add("plat");
        for (int i = 0; i < 3; i++) {
            OrderEntity order = new OrderEntity();
            order.setId("" + i);
            order.setIdTable("" + i);
            order.setIdsDish(dishes);
            orders.add(order);
        }
        DishEntity dishEntity = new DishEntity();
        dishEntity.setDishType(DishType.STARTER);
        when(baseService.getAllTableByServerOrHelper(anyString())).thenReturn(tables);
        doReturn(orders).when(serverController).getOnlyServerOrders(anyList(), anyList());
        doReturn(1).when(serverController).getIntegerInput(anyInt(), anyInt());
        when(baseService.getTableById(anyString())).thenReturn(new TableEntity());
        when(baseService.getDishById(anyString())).thenReturn(dishEntity);
        doNothing().when(serverController).launch(Role.SERVER);

        serverController.serveOrders(user);

        verify(serverView, times(1)).displayOrdersToServe(anyList());
        verify(baseService, times(1)).update(any(OrderEntity.class));
    }

    @Test
    @DisplayName("Annuler le service d'une commande")
    void testServeOrdersCancel() {
        UserEntity user = new UserEntity();
        user.setId("ser1");
        List<TableEntity> tables = new ArrayList<>();
        List<OrderEntity> orders = new ArrayList<>();
        List<String> dishes = new ArrayList<>();
        dishes.add("plat");
        for (int i = 0; i < 3; i++) {
            OrderEntity order = new OrderEntity();
            order.setId("" + i);
            order.setIdTable("" + i);
            order.setIdsDish(dishes);
            orders.add(order);
        }
        DishEntity dishEntity = new DishEntity();
        dishEntity.setDishType(DishType.STARTER);
        when(baseService.getAllTableByServerOrHelper(anyString())).thenReturn(tables);
        doReturn(orders).when(serverController).getOnlyServerOrders(anyList(), anyList());
        doReturn(0).when(serverController).getIntegerInput(anyInt(), anyInt());
        doNothing().when(serverController).launch(Role.SERVER);

        serverController.serveOrders(user);

        verify(serverView, times(1)).displayOrdersToServe(anyList());
        verify(baseService, times(0)).update(any(OrderEntity.class));
    }

    @Test
    @DisplayName("Aucune commande à servir")
    void testServeOrdersNone() {
        UserEntity user = new UserEntity();
        user.setId("ser1");
        List<TableEntity> tables = new ArrayList<>();
        List<OrderEntity> orders = new ArrayList<>();
        when(baseService.getAllTableByServerOrHelper(anyString())).thenReturn(tables);
        doReturn(orders).when(serverController).getOnlyServerOrders(anyList(), anyList());
        doNothing().when(serverController).launch(Role.SERVER);

        serverController.serveOrders(user);

        verify(serverView, times(1)).displayMessage("Il n'y a aucune commande à servir");
        verify(baseService, times(0)).update(any(OrderEntity.class));
    }

    @Test
    @DisplayName("Récupère uniquement les commandes de cet utilisateur")
    void testGetOnlyServerOrders() {
        List<OrderEntity> orders = new ArrayList<>();
        List<OrderEntity> orderExpected = new ArrayList<>();
        OrderEntity order = null;
        for (int i = 0; i < 3; i++) {
            order = new OrderEntity();
            order.setIdTable("" + i);
            orders.add(order);
        }
        orderExpected.add(order);

        List<TableEntity> tables = new ArrayList<>();
        TableEntity table = new TableEntity();
        table.setId("2");
        tables.add(table);

        List<OrderEntity> ordersActual = serverController.getOnlyServerOrders(orders, tables);
        assertEquals(orderExpected, ordersActual);
    }

    @Test
    @DisplayName("Faire une commande enfant")
    void testAskChildOrder() {
        doReturn(1).when(serverController).getIntegerInput(anyInt(), anyInt());

        boolean result = serverController.askChildOrder();

        assertTrue(result);
    }

    @Test
    @DisplayName("Faire une commande adulte")
    void testAskChildOrderAdult() {
        doReturn(0).when(serverController).getIntegerInput(anyInt(), anyInt());

        boolean result = serverController.askChildOrder();

        assertFalse(result);
    }

    @Test
    @DisplayName("Récupère le type de plat ")
    void testGetOrderDishType() {
        HashMap<Integer, MenuUtil> menus = new HashMap<>();
        menus.put(1, new MenuUtil(DishType.STARTER, true, new ArrayList<>()));

        doReturn(1).when(serverController).getIntegerInput(anyInt(), anyInt());

        DishType result = serverController.getOrderDishType(menus);

        assertEquals(DishType.STARTER, result);
    }

    @Test
    @DisplayName("Annulation de la récupèration du type de plat ")
    void testGetOrderDishTypeCancel() {
        HashMap<Integer, MenuUtil> menus = new HashMap<>();
        menus.put(1, new MenuUtil(DishType.STARTER, true, new ArrayList<>()));

        doReturn(0).when(serverController).getIntegerInput(anyInt(), anyInt());

        DishType result = serverController.getOrderDishType(menus);

        assertNull(result);
    }

    @Test
    @DisplayName("Récupèration du type de plat non disponible")
    void testGetOrderDishTypeNotAvailable() {
        HashMap<Integer, MenuUtil> menus = new HashMap<>();
        menus.put(1, new MenuUtil(DishType.STARTER, false, new ArrayList<>()));

        doReturn(1).when(serverController).getIntegerInput(anyInt(), anyInt());

        DishType result = serverController.getOrderDishType(menus);

        assertNull(result);
    }

    @Test
    @DisplayName("Calcule le rang de la nouvelle commande : Xème adulte")
    void testGetNextRankAdultNotFirst() {
        List<OrderEntity> orders = getListOfOrders(5, 9);

        when(baseService.getNotPreparedOrders()).thenReturn(orders);

        Integer result = serverController.getNextRank(false);

        assertEquals(6, result);
    }

    @Test
    @DisplayName("Calcule le rang de la nouvelle commande : 1ère adulte")
    void testGetNextRankAdultFirst() {
        List<OrderEntity> orders = getListOfOrders(0, 5);

        when(baseService.getNotPreparedOrders()).thenReturn(orders);

        Integer result = serverController.getNextRank(false);

        assertEquals(1, result);
    }

    @Test
    @DisplayName("Calcule le rang de la nouvelle commande : Xème enfant")
    void testGetNextRankChildrenNotFirst() {
        List<OrderEntity> orders = getListOfOrders(5, 9);

        when(baseService.getNotPreparedOrders()).thenReturn(orders);

        Integer result = serverController.getNextRank(true);

        assertEquals(10, result);
    }

    @Test
    @DisplayName("Calcule le rang de la nouvelle commande : 1ère enfant")
    void testGetNextRankChildrenFirst() {
        List<OrderEntity> orders = getListOfOrders(5, 0);

        when(baseService.getNotPreparedOrders()).thenReturn(orders);

        Integer result = serverController.getNextRank(true);

        assertEquals(1, result);
    }

    private List<OrderEntity> getListOfOrders(int nbAdult, int nbChildren) {
        List<OrderEntity> orders = new ArrayList<>();
        for (int i = 1; i <= nbAdult; i++) {
            OrderEntity order = new OrderEntity();
            order.setChildOrder(false);
            order.setRank(i);
            orders.add(order);
        }

        for (int i = 1; i <= nbChildren; i++) {
            OrderEntity order = new OrderEntity();
            order.setChildOrder(true);
            order.setRank(i);
            orders.add(order);
        }

        return orders;
    }

    @Test
    @DisplayName("Construit un objet OrderEntity")
    void testCreateOrderToSave() {
        List<String> dishes = new ArrayList<>();
        List<String> drinks = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            dishes.add("" + i);
            drinks.add("" + i + 6);
        }
        List<String> total = new ArrayList<>();
        total.addAll(dishes);
        total.addAll(drinks);

        OrderEntity order = new OrderEntity();
        order.setIdsDish(total);
        order.setIdTable("1");
        order.setChildOrder(false);
        order.setOrderState(OrderState.ORDERED);
        order.setRank(1);

        doReturn(1).when(serverController).getNextRank(anyBoolean());

        OrderEntity result = serverController.createOrderToSave("1", false, dishes, drinks);

        order.setId(result.getId()); //Id généré aléatoirement
        assertEquals(order, result);
    }

    @Test
    @DisplayName("Retire un plat de la selection du client")
    void testRemoveDish() {
        List<DishEntity> allDishes = new ArrayList<>();
        DishEntity dishEntity = new DishEntity();
        dishEntity.setId("5");
        dishEntity.setIdsProduct(new ArrayList<>());
        allDishes.add(dishEntity);
        DishEntity dishEntity1 = new DishEntity();
        dishEntity1.setId("12");
        dishEntity1.setIdsProduct(new ArrayList<>());
        allDishes.add(dishEntity1);
        List<String> selection = new ArrayList<>();
        selection.add("5");
        selection.add("8");
        selection.add("12");
        List<String> selectionExpected = new ArrayList<>();
        selectionExpected.add("8");
        String[] listToRemove = {"0", "1"};

        List<String> result = serverController.removeDish(allDishes, selection, listToRemove);

        assertEquals(selectionExpected, result);
    }

    @Test
    @DisplayName("Retire rien de la selection du client")
    void testRemoveDishNotInTheList() {
        List<DishEntity> allDishes = new ArrayList<>();
        DishEntity dishEntity = new DishEntity();
        dishEntity.setId("6");
        dishEntity.setIdsProduct(new ArrayList<>());
        allDishes.add(dishEntity);
        DishEntity dishEntity1 = new DishEntity();
        dishEntity1.setId("13");
        dishEntity1.setIdsProduct(new ArrayList<>());
        allDishes.add(dishEntity1);
        List<String> selection = new ArrayList<>();
        selection.add("5");
        selection.add("8");
        selection.add("12");
        List<String> selectionExpected = new ArrayList<>();
        selectionExpected.add("5");
        selectionExpected.add("8");
        selectionExpected.add("12");
        String[] listToRemove = {"0", "1"};

        List<String> result = serverController.removeDish(allDishes, selection, listToRemove);

        assertEquals(selectionExpected, result);
    }

    @Test
    @DisplayName("Ajoute des plats à la selection du client")
    void testAddDish() {
        List<DishEntity> allDishes = new ArrayList<>();
        DishEntity dishEntity = new DishEntity();
        dishEntity.setId("5");
        dishEntity.setIdsProduct(new ArrayList<>());
        allDishes.add(dishEntity);
        DishEntity dishEntity1 = new DishEntity();
        dishEntity1.setId("8");
        dishEntity1.setIdsProduct(new ArrayList<>());
        allDishes.add(dishEntity1);
        List<String> selection = new ArrayList<>();
        selection.add("12");
        List<String> selectionExpected = new ArrayList<>();
        selectionExpected.add("12");
        selectionExpected.add("5");
        selectionExpected.add("8");
        String[] listToAdd = {"0", "1"};

        List<String> result = serverController.addDish(allDishes, selection, listToAdd);

        assertEquals(selectionExpected, result);
    }

    @Test
    @DisplayName("Appelle l'ajout des plats")
    void testGetChosenDishesAdd() {
        doReturn(new ArrayList<>()).when(serverController).addDish(anyList(), anyList(), any(String[].class));

        serverController.getChosenDishes("-a 1/2/3", new ArrayList<>(), new ArrayList<>());

        verify(serverController, times(1)).addDish(anyList(), anyList(), any(String[].class));
    }

    @Test
    @DisplayName("Appelle la suppression des plats")
    void testGetChosenDishesRemove() {
        doReturn(new ArrayList<>()).when(serverController).removeDish(anyList(), anyList(), any(String[].class));

        serverController.getChosenDishes("-d 1/2/3", new ArrayList<>(), new ArrayList<>());

        verify(serverController, times(1)).removeDish(anyList(), anyList(), any(String[].class));
    }

    @Test
    @DisplayName("Erreur, n'appelle rien et renvoie les mêmes plats")
    void testGetChosenDishesNothing() {
        List<String> result = serverController.getChosenDishes("-v", new ArrayList<>(), new ArrayList<>());

        assertEquals(new ArrayList<>(), result);
    }

    @Test
    @DisplayName("Ajouter des boissons")
    void testAskDrink() {
        doReturn(1).when(serverController).getIntegerInput(anyInt(), anyInt());

        boolean result = serverController.askDrinksOrder();

        assertTrue(result);
    }

    @Test
    @DisplayName("Ne pas ajouter des boissons")
    void testAskDrinkNo() {
        doReturn(0).when(serverController).getIntegerInput(anyInt(), anyInt());

        boolean result = serverController.askDrinksOrder();

        assertFalse(result);
    }

    @Test
    @DisplayName("Aucun ajout de boisson")
    void testGetDrinksOrderedNone() {
        doReturn(false).when(serverController).askDrinksOrder();

        List<String> result = serverController.getDrinksOrdered();

        assertEquals(new ArrayList<>(), result);
    }

    @Test
    @DisplayName("Ajout de boisson")
    void testGetDrinksOrdered() {
        List<DishEntity> drinks = new ArrayList<>();
        drinks.add(new DishEntity());
        doReturn(true).when(serverController).askDrinksOrder();
        when(baseService.getDestockableDishesByDishType(DishType.DRINK)).thenReturn(drinks);
        doReturn(new ArrayList<>()).when(serverController).getDishesOrdered(any(DishType.class), any());

        serverController.getDrinksOrdered();

        verify(serverController, times(1)).getDishesOrdered(eq(DishType.DRINK), any());
    }

    @Test
    @DisplayName("Ajout de boisson mais aucune boisson")
    void testGetDrinksOrderedNoDrinks() {
        List<DishEntity> drinks = new ArrayList<>();
        doReturn(true).when(serverController).askDrinksOrder();
        when(baseService.getDestockableDishesByDishType(DishType.DRINK)).thenReturn(drinks);

        serverController.getDrinksOrdered();

        verify(serverView, times(1)).displayMessage("Aucune boisson disponible.");
    }

    @Test
    @DisplayName("Vérifie la disponibilité des types de plat : tout est disponible")
    void testCheckAvailabilityMenus() {
        HashMap<Integer, MenuUtil> menus = new HashMap<>();
        menus.put(1, new MenuUtil(DishType.STARTER, true, new ArrayList<>()));
        menus.put(2, new MenuUtil(DishType.MAIN_COURSE, true, new ArrayList<>()));
        menus.put(3, new MenuUtil(DishType.DESSERT, true, new ArrayList<>()));

        boolean result = serverController.checkAvailabilityMenus(menus);

        assertTrue(result);
    }

    @Test
    @DisplayName("Vérifie la disponibilité des types de plat : rien  n'est disponible")
    void testCheckAvailabilityMenusNone() {
        HashMap<Integer, MenuUtil> menus = new HashMap<>();
        menus.put(1, new MenuUtil(DishType.STARTER, false, new ArrayList<>()));
        menus.put(2, new MenuUtil(DishType.MAIN_COURSE, false, new ArrayList<>()));
        menus.put(3, new MenuUtil(DishType.DESSERT, false, new ArrayList<>()));

        boolean result = serverController.checkAvailabilityMenus(menus);

        assertFalse(result);
    }

    @Test
    @DisplayName("Vérifie la disponibilité des types de plat : un seul est disponible")
    void testCheckAvailabilityMenusOneTrue() {
        HashMap<Integer, MenuUtil> menus = new HashMap<>();
        menus.put(1, new MenuUtil(DishType.STARTER, false, new ArrayList<>()));
        menus.put(2, new MenuUtil(DishType.MAIN_COURSE, false, new ArrayList<>()));
        menus.put(3, new MenuUtil(DishType.DESSERT, true, new ArrayList<>()));

        boolean result = serverController.checkAvailabilityMenus(menus);

        assertTrue(result);
    }

    @Test
    @DisplayName("Récupère le menu selon l'état table: Occupée")
    void testGetMenusAvailableOccupied() {
        HashMap<Integer, MenuUtil> menus = new HashMap<>();
        menus.put(1, new MenuUtil(DishType.STARTER, false, new ArrayList<>()));
        menus.put(2, new MenuUtil(DishType.MAIN_COURSE, false, new ArrayList<>()));
        menus.put(3, new MenuUtil(DishType.DESSERT, false, new ArrayList<>()));

        when(baseService.getDestockableDishesByDishType(any(DishType.class))).thenReturn(new ArrayList<>());
        HashMap<Integer, MenuUtil> result = serverController.getMenusAvailable(TableState.OCCUPIED);

        assertEquals(menus, result);
    }

    @Test
    @DisplayName("Récupère le menu selon l'état table: Entrée")
    void testGetMenusAvailableStarter() {
        HashMap<Integer, MenuUtil> menus = new HashMap<>();
        menus.put(1, new MenuUtil(DishType.STARTER, false, new ArrayList<>()));
        menus.put(2, new MenuUtil(DishType.MAIN_COURSE, false, new ArrayList<>()));
        menus.put(3, new MenuUtil(DishType.DESSERT, false, new ArrayList<>()));

        when(baseService.getDestockableDishesByDishType(any(DishType.class))).thenReturn(new ArrayList<>());
        HashMap<Integer, MenuUtil> result = serverController.getMenusAvailable(TableState.STARTER);

        assertEquals(menus, result);
    }

    @Test
    @DisplayName("Récupère le menu selon l'état table: Plat")
    void testGetMenusAvailableMainCourse() {
        HashMap<Integer, MenuUtil> menus = new HashMap<>();
        menus.put(1, new MenuUtil(DishType.MAIN_COURSE, false, new ArrayList<>()));
        menus.put(2, new MenuUtil(DishType.DESSERT, false, new ArrayList<>()));

        when(baseService.getDestockableDishesByDishType(any(DishType.class))).thenReturn(new ArrayList<>());
        HashMap<Integer, MenuUtil> result = serverController.getMenusAvailable(TableState.MAIN_COURSE);

        assertEquals(menus, result);
    }

    @Test
    @DisplayName("Récupère le menu selon l'état table: Dessert")
    void testGetMenusAvailableDessert() {
        HashMap<Integer, MenuUtil> menus = new HashMap<>();
        menus.put(1, new MenuUtil(DishType.DESSERT, false, new ArrayList<>()));

        when(baseService.getDestockableDishesByDishType(any(DishType.class))).thenReturn(new ArrayList<>());
        HashMap<Integer, MenuUtil> result = serverController.getMenusAvailable(TableState.DESSERT);

        assertEquals(menus, result);
    }

    @Test
    @DisplayName("Récupère les choix de plats pour un type de plat : après une selection plus de plats disponibles")
    void testGetDishesOrderedNoDishOnTheMenuLeft() {
        HashMap<Integer, MenuUtil> menus = new HashMap<>();
        List<DishEntity> dishes = new ArrayList<>();
        DishEntity dish1 = new DishEntity();
        DishEntity dish2 = new DishEntity();
        DishEntity dish3 = new DishEntity();
        dish1.setId("Salade de pommes de terre");
        dish1.setIdCategory("Salades");
        dish2.setId("Beignet de bananes");
        dish2.setIdCategory("Apéro");
        dish3.setId("Salades de carottes");
        dish3.setIdCategory("Salades");
        dishes.add(dish1);
        dishes.add(dish2);
        dishes.add(dish3);
        menus.put(1, new MenuUtil(DishType.STARTER, true, dishes));
        List<String> idDishes = new ArrayList<>();
        idDishes.add("Salade de pommes de terre");
        idDishes.add("Salades de carottes");

        doReturn("-a 0/2").when(serverController).getStringCommandInput(anyInt(), anyInt());
        doReturn(idDishes).when(serverController).getChosenDishes(anyString(), anyList(), anyList());
        when(baseService.getDestockableDishesByDishType(any(DishType.class))).thenReturn(new ArrayList<>());

        serverController.getDishesOrdered(DishType.STARTER, menus);

        verify(serverView, times(1)).displayNoDishOnTheMenu();
    }

    @Test
    @DisplayName("Récupère les choix de plats pour un type de plat")
    void testGetDishesOrdered() {
        HashMap<Integer, MenuUtil> menus = new HashMap<>();
        List<DishEntity> dishes = new ArrayList<>();
        DishEntity dish1 = new DishEntity();
        DishEntity dish2 = new DishEntity();
        DishEntity dish3 = new DishEntity();
        dish1.setId("Salade de pommes de terre");
        dish1.setIdCategory("Salades");
        dish2.setId("Beignet de bananes");
        dish2.setIdCategory("Apéro");
        dish3.setId("Salades de carottes");
        dish3.setIdCategory("Salades");
        dishes.add(dish1);
        dishes.add(dish2);
        dishes.add(dish3);
        menus.put(1, new MenuUtil(DishType.STARTER, true, dishes));
        List<String> idDishes = new ArrayList<>();
        idDishes.add("Salade de pommes de terre");
        idDishes.add("Salades de carottes");

        doReturn("-a 0").doReturn("-a 1").doReturn("-v").when(serverController).getStringCommandInput(anyInt(), anyInt());
        doReturn(idDishes).when(serverController).getChosenDishes(anyString(), anyList(), anyList());
        when(baseService.getDestockableDishesByDishType(any(DishType.class))).thenReturn(dishes);

        serverController.getDishesOrdered(DishType.STARTER, menus);

        verify(serverView, times(0)).displayNoDishOnTheMenu();
    }

    @Test
    @DisplayName("Récupère les choix de plats pour un type de plat : validation sans plat")
    void testGetDishesOrderedValidationButNoDish() {
        HashMap<Integer, MenuUtil> menus = new HashMap<>();
        List<DishEntity> dishes = new ArrayList<>();
        DishEntity dish1 = new DishEntity();
        DishEntity dish2 = new DishEntity();
        DishEntity dish3 = new DishEntity();
        dish1.setId("Salade de pommes de terre");
        dish1.setIdCategory("Salades");
        dish2.setId("Beignet de bananes");
        dish2.setIdCategory("Apéro");
        dish3.setId("Salades de carottes");
        dish3.setIdCategory("Salades");
        dishes.add(dish1);
        dishes.add(dish2);
        dishes.add(dish3);
        menus.put(1, new MenuUtil(DishType.STARTER, true, dishes));
        List<String> idDishes = new ArrayList<>();
        idDishes.add("Salade de pommes de terre");
        idDishes.add("Salades de carottes");

        doReturn("-v").when(serverController).getStringCommandInput(anyInt(), anyInt());
        doReturn(idDishes).when(serverController).getChosenDishes(anyString(), anyList(), anyList());
        when(baseService.getDestockableDishesByDishType(any(DishType.class))).thenReturn(new ArrayList<>());

        serverController.getDishesOrdered(DishType.STARTER, menus);

        verify(serverView, times(1)).displayNoDishInTheOrder();
    }

    @Test
    @DisplayName("Prend une commande mais aucune table servable")
    void testTakeOrdersButNoTableToServe() {
        UserEntity user = new UserEntity();
        user.setId("serveur");
        user.setRole(Role.SERVER);
        List<TableEntity> tables = new ArrayList<>();

        when(baseService.getTablesReadyToOrderByServer(anyString())).thenReturn(tables);
        doNothing().when(serverController).launch(Role.SERVER);

        serverController.takeOrders(user);

        verify(serverView, times(1)).displayMessage("Il n'y a pas de tables où prendre de commandes. (Pas de clients, commande en cours de préparation, à servir...)");
    }

    @Test
    @DisplayName("Prend une commande mais annulation")
    void testTakeOrdersCancel() {
        UserEntity user = new UserEntity();
        user.setId("serveur");
        user.setRole(Role.SERVER);
        List<TableEntity> tables = new ArrayList<>();
        TableEntity table1 = new TableEntity();
        table1.setId("1");
        tables.add(table1);

        when(baseService.getTablesReadyToOrderByServer(anyString())).thenReturn(tables);
        doReturn(0).when(serverController).getIntegerInput(anyInt(), anyInt());
        doNothing().when(serverController).launch(Role.SERVER);

        serverController.takeOrders(user);

        verify(serverView, times(1)).displayMessage("À quelle table souhaitez vous prendre une commande ?" +
                "\n 0) Annuler");
        verify(serverController, times(0)).getMenusAvailable(any(TableState.class));
    }

    @Test
    @DisplayName("Prend une commande mais aucun plat sur le menus")
    void testTakeOrdersNoDishTypeAvailable() {
        UserEntity user = new UserEntity();
        user.setId("serveur");
        user.setRole(Role.SERVER);
        List<TableEntity> tables = new ArrayList<>();
        TableEntity table1 = new TableEntity();
        table1.setId("1");
        table1.setTableState(TableState.OCCUPIED);
        tables.add(table1);
        HashMap<Integer, MenuUtil> menus = new HashMap<>();
        menus.put(1, new MenuUtil(DishType.STARTER, false, new ArrayList<>()));
        menus.put(2, new MenuUtil(DishType.MAIN_COURSE, false, new ArrayList<>()));
        menus.put(3, new MenuUtil(DishType.DESSERT, false, new ArrayList<>()));

        when(baseService.getTablesReadyToOrderByServer(anyString())).thenReturn(tables);
        doReturn(1).when(serverController).getIntegerInput(anyInt(), anyInt());
        doReturn(menus).when(serverController).getMenusAvailable(any(TableState.class));
        doNothing().when(serverController).launch(Role.SERVER);

        serverController.takeOrders(user);

        verify(serverView, times(1)).displayNoDishOnTheMenu();
    }

    @Test
    @DisplayName("Prend une commande mais abandon au moment de chosir le type de plat")
    void testTakeOrdersCancelChoiceDishType() {
        UserEntity user = new UserEntity();
        user.setId("serveur");
        user.setRole(Role.SERVER);
        List<TableEntity> tables = new ArrayList<>();
        TableEntity table1 = new TableEntity();
        table1.setId("1");
        table1.setTableState(TableState.OCCUPIED);
        tables.add(table1);
        HashMap<Integer, MenuUtil> menus = new HashMap<>();
        menus.put(1, new MenuUtil(DishType.STARTER, true, new ArrayList<>()));
        menus.put(2, new MenuUtil(DishType.MAIN_COURSE, true, new ArrayList<>()));
        menus.put(3, new MenuUtil(DishType.DESSERT, true, new ArrayList<>()));

        when(baseService.getTablesReadyToOrderByServer(anyString())).thenReturn(tables);
        doReturn(1).when(serverController).getIntegerInput(anyInt(), anyInt());
        doReturn(menus).when(serverController).getMenusAvailable(any(TableState.class));
        doReturn(true).when(serverController).askChildOrder();
        doReturn(null).when(serverController).getOrderDishType(any());
        doNothing().when(serverController).launch(Role.SERVER);

        serverController.takeOrders(user);

        verify(serverController, times(0)).getDishesOrdered(any(DishType.class), any());
    }

    @Test
    @DisplayName("Prend une commande mais annule à la fin de la procédure ")
    void testTakeOrdersCancleAllOrder() {
        UserEntity user = new UserEntity();
        user.setId("serveur");
        user.setRole(Role.SERVER);
        List<TableEntity> tables = new ArrayList<>();
        TableEntity table1 = new TableEntity();
        table1.setId("1");
        table1.setTableState(TableState.OCCUPIED);
        tables.add(table1);
        HashMap<Integer, MenuUtil> menus = new HashMap<>();
        menus.put(1, new MenuUtil(DishType.STARTER, true, new ArrayList<>()));
        menus.put(2, new MenuUtil(DishType.MAIN_COURSE, true, new ArrayList<>()));
        menus.put(3, new MenuUtil(DishType.DESSERT, true, new ArrayList<>()));
        List<String> dishes = new ArrayList<>();
        dishes.add("Salades de riz");
        dishes.add("Beignet de crevettes");
        OrderEntity dish = new OrderEntity();
        OrderEntity spy = spy(dish);

        when(baseService.getTablesReadyToOrderByServer(anyString())).thenReturn(tables);
        doReturn(1).doReturn(0).when(serverController).getIntegerInput(anyInt(), anyInt());
        doReturn(menus).when(serverController).getMenusAvailable(any(TableState.class));
        doReturn(true).when(serverController).askChildOrder();
        doReturn(DishType.STARTER).when(serverController).getOrderDishType(any());
        doReturn(dishes).when(serverController).getDishesOrdered(any(DishType.class), any());
        doReturn(new ArrayList<>()).when(serverController).getDrinksOrdered();
        doReturn(spy).when(serverController).createOrderToSave(anyString(), anyBoolean(), anyList(), anyList());
        doNothing().when(spy).giveStockBack(any(BaseService.class));
        doNothing().when(serverController).launch(Role.SERVER);

        serverController.takeOrders(user);

        verify(serverView, times(1)).displayOrderToSave(any(OrderEntity.class));
        verify(baseService, times(0)).save(any(OrderEntity.class));
        verify(spy, times(1)).giveStockBack(any(BaseService.class));
    }

    @Test
    @DisplayName("Prend une commande")
    void testTakeOrders() {
        UserEntity user = new UserEntity();
        user.setId("serveur");
        user.setRole(Role.SERVER);
        List<TableEntity> tables = new ArrayList<>();
        TableEntity table1 = new TableEntity();
        table1.setId("1");
        table1.setTableState(TableState.OCCUPIED);
        tables.add(table1);
        HashMap<Integer, MenuUtil> menus = new HashMap<>();
        menus.put(1, new MenuUtil(DishType.STARTER, true, new ArrayList<>()));
        menus.put(2, new MenuUtil(DishType.MAIN_COURSE, true, new ArrayList<>()));
        menus.put(3, new MenuUtil(DishType.DESSERT, true, new ArrayList<>()));
        List<String> dishes = new ArrayList<>();
        dishes.add("Salades de riz");
        dishes.add("Beignet de crevettes");

        when(baseService.getTablesReadyToOrderByServer(anyString())).thenReturn(tables);
        doReturn(1).when(serverController).getIntegerInput(anyInt(), anyInt());
        doReturn(menus).when(serverController).getMenusAvailable(any(TableState.class));
        doReturn(true).when(serverController).askChildOrder();
        doReturn(DishType.STARTER).when(serverController).getOrderDishType(any());
        doReturn(dishes).when(serverController).getDishesOrdered(any(DishType.class), any());
        doReturn(new ArrayList<>()).when(serverController).getDrinksOrdered();
        doNothing().when(serverController).launch(Role.SERVER);

        serverController.takeOrders(user);

        verify(serverView, times(1)).displaySuccess();
        verify(baseService, times(1)).save(any(OrderEntity.class));
    }
}
