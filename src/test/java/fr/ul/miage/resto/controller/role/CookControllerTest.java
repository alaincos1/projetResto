package fr.ul.miage.resto.controller.role;

import fr.ul.miage.resto.Launcher;
import fr.ul.miage.resto.appinfo.Service;
import fr.ul.miage.resto.constants.DishType;
import fr.ul.miage.resto.constants.OrderState;
import fr.ul.miage.resto.constants.Role;
import fr.ul.miage.resto.dao.service.BaseService;
import fr.ul.miage.resto.model.entity.*;
import fr.ul.miage.resto.view.role.CookView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@DisplayName("CookController")
@ExtendWith(MockitoExtension.class)
class CookControllerTest {
    @Mock
    CookView cookView;
    @Mock
    BaseService baseService;
    @Mock
    Service service;
    @Spy
    @InjectMocks
    CookController cookController;

    @Test
    @DisplayName("Aucune commande à afficher")
    void testViewOrdersListsNone() {
        List<OrderEntity> orders = new ArrayList<>();
        when(baseService.getNotPreparedOrders()).thenReturn(orders);
        doNothing().when(cookController).launch(any(Role.class));
        cookController.viewOrdersList();
        verify(cookView, times(1)).displayNoOrderToPrepare();
    }

    @Test
    @DisplayName("Il y a des commandes à afficher")
    void testViewOrdersLists() {
        List<OrderEntity> orders = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            orders.add(new OrderEntity());
        }

        when(baseService.getNotPreparedOrders()).thenReturn(orders);
        doNothing().when(cookController).askMainMenu();
        cookController.viewOrdersList();
        verify(cookView, times(1)).displayOrdersList(orders);
    }

    @Test
    @DisplayName("La commande choisie est prête")
    void testSetOrderReady() {
        List<OrderEntity> orders = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            OrderEntity order = new OrderEntity();
            order.setOrderState(OrderState.ORDERED);
            orders.add(order);
        }

        when(baseService.getNotPreparedOrders()).thenReturn(orders);
        doReturn(1).when(cookController).getIntegerInput(anyInt(), anyInt());
        doReturn(false).when(cookController).doAgain();
        doNothing().when(cookController).savePerformance(any(Service.class), any(BaseService.class), anyString(), anyInt(), anyInt());
        doNothing().when(cookController).launch(any(Role.class));

        cookController.setOrderReady();

        verify(baseService, times(1)).update(orders.get(0));
        assertEquals(OrderState.PREPARED, orders.get(0).getOrderState());
        assertEquals(OrderState.ORDERED, orders.get(1).getOrderState());
        assertEquals(OrderState.ORDERED, orders.get(2).getOrderState());
    }

    @Test
    @DisplayName("Aucune commande à préparer")
    void testSetOrderReadyNoneOrders() {
        List<OrderEntity> orders = new ArrayList<>();

        when(baseService.getNotPreparedOrders()).thenReturn(orders);
        doNothing().when(cookController).launch(any(Role.class));

        cookController.setOrderReady();

        verify(cookView, times(1)).displayNoOrderToPrepare();
    }

    @Test
    @DisplayName("Annuler la préparation")
    void testSetOrderReadyCancel() {
        List<OrderEntity> orders = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            OrderEntity order = new OrderEntity();
            order.setOrderState(OrderState.ORDERED);
            orders.add(order);
        }

        when(baseService.getNotPreparedOrders()).thenReturn(orders);
        doReturn(0).when(cookController).getIntegerInput(anyInt(), anyInt());
        doNothing().when(cookController).launch(any(Role.class));

        cookController.setOrderReady();

        verify(baseService, times(0)).update(orders.get(0));
        assertEquals(OrderState.ORDERED, orders.get(0).getOrderState());
        assertEquals(OrderState.ORDERED, orders.get(1).getOrderState());
        assertEquals(OrderState.ORDERED, orders.get(2).getOrderState());
    }

    @Test
    @DisplayName("Une nouvelle commande est préparée en plus")
    void testSetOrderReadyAgain() {
        List<OrderEntity> orders = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            OrderEntity order = new OrderEntity();
            order.setOrderState(OrderState.ORDERED);
            orders.add(order);
        }

        when(baseService.getNotPreparedOrders()).thenReturn(orders);
        doReturn(1).when(cookController).getIntegerInput(anyInt(), anyInt());
        doReturn(true).doReturn(false).when(cookController).doAgain();
        doNothing().when(cookController).savePerformance(any(Service.class), any(BaseService.class), anyString(), anyInt(), anyInt());
        doNothing().when(cookController).launch(any(Role.class));

        cookController.setOrderReady();

        verify(cookController, times(2)).setOrderReady();
        assertEquals(OrderState.PREPARED, orders.get(0).getOrderState());
        assertEquals(OrderState.ORDERED, orders.get(1).getOrderState());
        assertEquals(OrderState.ORDERED, orders.get(2).getOrderState());
    }

    @Test
    @DisplayName("Créer un plat")
    void testCreateDish() {
        doReturn("randomName").when(cookController).getStringInput();
        doReturn(2).when(cookController).getPriceDish();
        doReturn(DishType.DESSERT).when(cookController).getDishType();
        doReturn("Glace").when(cookController).getDishCategory(any(DishType.class));
        List<String> idsProducts = new ArrayList<>();
        idsProducts.add("1");
        doReturn(idsProducts).when(cookController).getDishProducts();
        doReturn(1).when(cookController).getIntegerInput(anyInt(), anyInt());
        doNothing().when(cookController).saveDish(any(DishEntity.class), anyBoolean());
        doNothing().when(cookController).launch(any(Role.class));

        cookController.createDish();

        verify(cookController, times(1)).saveDish(any(DishEntity.class), anyBoolean());
    }

    @Test
    @DisplayName("Modifier un plat")
    void testCreateDishModify() {
        doReturn("randomName").when(cookController).getStringInput();
        when(baseService.getDishById(anyString())).thenReturn(new DishEntity());
        doReturn(2).when(cookController).getPriceDish();
        doReturn(DishType.DESSERT).when(cookController).getDishType();
        doReturn("Glace").when(cookController).getDishCategory(any(DishType.class));
        List<String> idsProducts = new ArrayList<>();
        idsProducts.add("1");
        doReturn(idsProducts).when(cookController).getDishProducts();
        doReturn(1).when(cookController).getIntegerInput(anyInt(), anyInt());
        doNothing().when(cookController).saveDish(any(DishEntity.class), anyBoolean());
        doNothing().when(cookController).launch(any(Role.class));

        cookController.createDish();

        verify(cookController, times(1)).saveDish(any(DishEntity.class), eq(true));
    }

    @Test
    @DisplayName("Abandon d'un plat")
    void testCreateDishCancel() {
        doReturn("randomName").when(cookController).getStringInput();
        doReturn(2).when(cookController).getPriceDish();
        doReturn(DishType.DESSERT).when(cookController).getDishType();
        doReturn("Glace").when(cookController).getDishCategory(any(DishType.class));
        List<String> idsProducts = new ArrayList<>();
        idsProducts.add("1");
        doReturn(idsProducts).when(cookController).getDishProducts();
        doReturn(0).when(cookController).getIntegerInput(anyInt(), anyInt());
        doNothing().when(cookController).launch(any(Role.class));

        cookController.createDish();

        verify(cookController, times(0)).saveDish(any(DishEntity.class), anyBoolean());
        verify(cookView, times(1)).displaySaveOrNot(false);
    }

    @Test
    @DisplayName("Récupère le prix du plat")
    void testGetPriceDish() {
        doReturn(1).when(cookController).getIntegerInput(anyInt(), anyInt());

        DishType dishtype = cookController.getDishType();

        assertEquals(DishType.STARTER, dishtype);
    }

    @Test
    @DisplayName("Récupère la catégorie d'un plat")
    void testGetDishCategory() {
        List<CategoryEntity> catList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            CategoryEntity cat = new CategoryEntity();
            cat.setId("" + i);
            cat.setDishType(DishType.DESSERT);
            catList.add(cat);
        }
        when(baseService.getCategoriesByDishType(any(DishType.class))).thenReturn(catList);
        doReturn(1).when(cookController).getIntegerInput(anyInt(), anyInt());

        String dishCat = cookController.getDishCategory(DishType.DESSERT);

        assertEquals("0", dishCat);
    }

    @Test
    @DisplayName("Ajoute une nouvelle catégorie d'un plat")
    void testGetDishCategoryCreate() {
        List<CategoryEntity> catList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            CategoryEntity cat = new CategoryEntity();
            cat.setId("" + i);
            cat.setDishType(DishType.DESSERT);
            catList.add(cat);
        }
        when(baseService.getCategoriesByDishType(any(DishType.class))).thenReturn(catList);
        doReturn(0).when(cookController).getIntegerInput(anyInt(), anyInt());
        doReturn("newCat").when(cookController).getStringInput();

        String dishCat = cookController.getDishCategory(DishType.DESSERT);

        assertEquals("newCat", dishCat);
    }

    @Test
    @DisplayName("Ajoute des ingrédients")
    void testGetDishProducts() {
        List<ProductEntity> prodList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            ProductEntity prod = new ProductEntity();
            prod.setId("" + i);
            prodList.add(prod);
        }
        doReturn(prodList).when(cookController).parseToSelectedProducts(anyList(), anyString());
        doReturn("1").when(cookController).getStringMultipleChoices(anyInt(), anyInt());
        doReturn(1).when(cookController).getIntegerInput(anyInt(), anyInt());

        cookController.getDishProducts();

        verify(cookController, times(1)).parseToIdProducts(anyList());
    }

    @Test
    @DisplayName("Annule l'ajout des ingrédients")
    void testGetDishProductsCancel() {
        List<ProductEntity> prodList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            ProductEntity prod = new ProductEntity();
            prod.setId("" + i);
            prodList.add(prod);
        }
        doReturn(prodList).when(cookController).parseToSelectedProducts(anyList(), anyString());
        doReturn("1").when(cookController).getStringMultipleChoices(anyInt(), anyInt());
        doReturn(0).doReturn(1).when(cookController).getIntegerInput(anyInt(), anyInt());

        cookController.getDishProducts();

        verify(cookController, times(2)).getDishProducts();
    }

    @Test
    @DisplayName("Parse une liste de ProductEntity en List de String")
    void testParseToIdProducts() {
        List<ProductEntity> prodList = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            ProductEntity prod = new ProductEntity();
            prod.setId("" + i);
            prodList.add(prod);
        }
        List<String> prodString1 = new ArrayList<>();
        prodString1.add("0");
        prodString1.add("1");
        List<String> prodString2 = cookController.parseToIdProducts(prodList);

        assertEquals(prodString1, prodString2);
    }

    @Test
    @DisplayName("Retourne la liste de ProductEntities sélectionnés")
    void testParseToSelectedProducts() {
        List<ProductEntity> prodList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            ProductEntity prod = new ProductEntity();
            prod.setId("" + i);
            prodList.add(prod);
        }
        String multipleChoices = "1/2/4";

        List<ProductEntity> prodList2 = new ArrayList<>();
        prodList2.add(prodList.get(0));
        prodList2.add(prodList.get(1));
        prodList2.add(prodList.get(3));

        List<ProductEntity> result = cookController.parseToSelectedProducts(prodList, multipleChoices);

        assertEquals(prodList2, result);
    }

    @Test
    @DisplayName("Sauvegarde du plat")
    void testSaveDish() {
        DishEntity dish = new DishEntity();
        dish.setId("dish");
        dish.setDishType(DishType.DESSERT);
        dish.setIdCategory("dish");
        List<String> idsProduct = new ArrayList<>();
        idsProduct.add("1");
        dish.setIdsProduct(idsProduct);
        dish.setOnTheMenu(false);
        dish.setPrice(3);

        CategoryEntity cat = new CategoryEntity();
        cat.setId("dish");
        when(baseService.getCategoryById(anyString())).thenReturn(cat);

        cookController.saveDish(dish, false);

        verify(baseService, times(1)).save(dish);
        verify(cookView, times(1)).displaySaveOrNot(anyBoolean());
    }

    @Test
    @DisplayName("Sauvegarde du plat modifié")
    void testSaveDishModify() {
        DishEntity dish = new DishEntity();
        dish.setId("dish");
        dish.setDishType(DishType.DESSERT);
        dish.setIdCategory("dish");
        List<String> idsProduct = new ArrayList<>();
        idsProduct.add("1");
        dish.setIdsProduct(idsProduct);
        dish.setOnTheMenu(false);
        dish.setPrice(3);

        CategoryEntity cat = new CategoryEntity();
        cat.setId("dish");
        when(baseService.getCategoryById(anyString())).thenReturn(cat);

        cookController.saveDish(dish, true);

        verify(baseService, times(1)).update(dish);
        verify(cookView, times(1)).displaySaveOrNot(anyBoolean());
    }

    @Test
    @DisplayName("Sauvegarde du plat et d'une nouvelle catégorie")
    void testSaveDishNewCat() {
        DishEntity dish = new DishEntity();
        dish.setId("dish");
        dish.setDishType(DishType.DESSERT);
        dish.setIdCategory("dish");
        List<String> idsProduct = new ArrayList<>();
        idsProduct.add("1");
        dish.setIdsProduct(idsProduct);
        dish.setOnTheMenu(false);
        dish.setPrice(3);

        CategoryEntity cat = new CategoryEntity();
        cat.setId("dish");
        when(baseService.getCategoryById(anyString())).thenReturn(null);

        cookController.saveDish(dish, false);

        verify(baseService, times(1)).save(any(DishEntity.class));
        verify(baseService, times(1)).save(any(CategoryEntity.class));
        verify(cookView, times(1)).displaySaveOrNot(anyBoolean());
    }

    @Test
    @DisplayName("Arrêter la prise en charge des nouveaux clients")
    void testEndCooking() {
        when(service.isEndNewClients()).thenReturn(false);
        doReturn(1).when(cookController).getIntegerInput(anyInt(), anyInt());
        doNothing().when(cookController).launch(any(Role.class));

        cookController.endCooking();

        verify(service, times(1)).setEndNewClients(true);
        verify(cookView, times(1)).displayMessage("La fin de prise en charge des nouveaux clients est annoncée.");
    }

    @Test
    @DisplayName("Annuler la prise en charge des nouveaux clients")
    void testEndCookingCancel() {
        when(service.isEndNewClients()).thenReturn(false);
        doReturn(0).when(cookController).getIntegerInput(anyInt(), anyInt());
        doNothing().when(cookController).launch(any(Role.class));

        cookController.endCooking();

        verify(service, times(0)).setEndNewClients(true);
        verify(cookView, times(0)).displayMessage("La fin de prise en charge des nouveaux clients est annoncée.");
    }

    @Test
    @DisplayName("La prise en charge des nouveaux clients est déjà stoppée")
    void testEndCookingAlreadyEnded() {
        when(service.isEndNewClients()).thenReturn(true);

        doNothing().when(cookController).launch(any(Role.class));

        cookController.endCooking();

        verify(cookView, times(1)).displayMessage("La fin de prise en charge des nouveaux clients a déjà été annoncée.");
    }

    @Test
    @DisplayName("Vérifie le callAction goBackOrDisconnect")
    void testCallAction0() {
        try (MockedStatic<Launcher> utilities = Mockito.mockStatic(Launcher.class)) {
            UserEntity user = new UserEntity();
            user.setId("user");
            user.setRole(Role.COOK);
            utilities.when(Launcher::getLoggedUser)
                    .thenReturn(user);
            doNothing().when(cookController).goBackOrDisconnect(any(Role.class), any(), any());

            cookController.callAction(0);

            verify(cookController, times(1)).goBackOrDisconnect(any(Role.class), any(), any());
        }
    }

    @Test
    @DisplayName("Vérifie le callAction viewOrdersList")
    void testCallAction1() {
        try (MockedStatic<Launcher> utilities = Mockito.mockStatic(Launcher.class)) {
            UserEntity user = new UserEntity();
            user.setId("user");
            user.setRole(Role.COOK);
            utilities.when(Launcher::getLoggedUser)
                    .thenReturn(user);
            doNothing().when(cookController).viewOrdersList();

            cookController.callAction(1);

            verify(cookController, times(1)).viewOrdersList();
        }
    }

    @Test
    @DisplayName("Vérifie le callAction setOrderReady")
    void testCallAction2() {
        try (MockedStatic<Launcher> utilities = Mockito.mockStatic(Launcher.class)) {
            UserEntity user = new UserEntity();
            user.setId("user");
            user.setRole(Role.COOK);
            utilities.when(Launcher::getLoggedUser)
                    .thenReturn(user);
            doNothing().when(cookController).setOrderReady();

            cookController.callAction(2);

            verify(cookController, times(1)).setOrderReady();
        }
    }

    @Test
    @DisplayName("Vérifie le callAction createDish")
    void testCallAction3() {
        try (MockedStatic<Launcher> utilities = Mockito.mockStatic(Launcher.class)) {
            UserEntity user = new UserEntity();
            user.setId("user");
            user.setRole(Role.COOK);
            utilities.when(Launcher::getLoggedUser)
                    .thenReturn(user);
            doNothing().when(cookController).createDish();

            cookController.callAction(3);

            verify(cookController, times(1)).createDish();
        }
    }

    @Test
    @DisplayName("Vérifie le callAction endCooking")
    void testCallAction4() {
        try (MockedStatic<Launcher> utilities = Mockito.mockStatic(Launcher.class)) {
            UserEntity user = new UserEntity();
            user.setId("user");
            user.setRole(Role.COOK);
            utilities.when(Launcher::getLoggedUser)
                    .thenReturn(user);
            doNothing().when(cookController).endCooking();

            cookController.callAction(4);

            verify(cookController, times(1)).endCooking();
        }
    }

    @Test
    @DisplayName("Vérifie le callAction Default")
    void testCallActionDefault() {
        try (MockedStatic<Launcher> utilities = Mockito.mockStatic(Launcher.class)) {
            UserEntity user = new UserEntity();
            user.setId("user");
            user.setRole(Role.HELPER);
            utilities.when(Launcher::getLoggedUser)
                    .thenReturn(user);

            cookController.callAction(-1);

            verify(cookController, times(0)).goBackOrDisconnect(any(Role.class), any(), any());
            verify(cookController, times(0)).viewOrdersList();
            verify(cookController, times(0)).setOrderReady();
            verify(cookController, times(0)).createDish();
            verify(cookController, times(0)).endCooking();
        }
    }
}
