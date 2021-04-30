package fr.ul.miage.projetResto.controller.role;

import fr.ul.miage.projetResto.appinfo.Service;
import fr.ul.miage.projetResto.constants.DishType;
import fr.ul.miage.projetResto.constants.OrderState;
import fr.ul.miage.projetResto.constants.Role;
import fr.ul.miage.projetResto.dao.service.BaseService;
import fr.ul.miage.projetResto.model.entity.CategoryEntity;
import fr.ul.miage.projetResto.model.entity.DishEntity;
import fr.ul.miage.projetResto.model.entity.OrderEntity;
import fr.ul.miage.projetResto.model.entity.ProductEntity;
import fr.ul.miage.projetResto.view.role.CookView;
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
    void checkViewOrdersListsNone() {
        List<OrderEntity> orders = new ArrayList<>();
        when(baseService.getNotPreparedOrders()).thenReturn(orders);
        doNothing().when(cookController).launch(any(Role.class));
        cookController.viewOrdersList();
        verify(cookView, times(1)).displayNoOrderToPrepare();
    }

    @Test
    @DisplayName("Il y a des commandes à afficher")
    void checkViewOrdersLists() {
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
    void checkSetOrderReady() {
        List<OrderEntity> orders = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            OrderEntity order = new OrderEntity();
            order.setOrderState(OrderState.Ordered);
            orders.add(order);
        }

        when(baseService.getNotPreparedOrders()).thenReturn(orders);
        doReturn(1).when(cookController).getIntegerInput(anyInt(),anyInt());
        doReturn(false).when(cookController).doAgain();
        doNothing().when(cookController).launch(any(Role.class));

        cookController.setOrderReady();

        verify(baseService, times(1)).update(orders.get(0));
        assertEquals(OrderState.Prepared, orders.get(0).getOrderState());
        assertEquals(OrderState.Ordered, orders.get(1).getOrderState());
        assertEquals(OrderState.Ordered, orders.get(2).getOrderState());
    }

    @Test
    @DisplayName("Aucune commande à préparer")
    void checkSetOrderReadyNoneOrders() {
        List<OrderEntity> orders = new ArrayList<>();

        when(baseService.getNotPreparedOrders()).thenReturn(orders);
        doNothing().when(cookController).launch(any(Role.class));

        cookController.setOrderReady();

        verify(cookView, times(1)).displayNoOrderToPrepare();
    }

    @Test
    @DisplayName("Annuler la préparation")
    void checkSetOrderReadyCancel() {
        List<OrderEntity> orders = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            OrderEntity order = new OrderEntity();
            order.setOrderState(OrderState.Ordered);
            orders.add(order);
        }

        when(baseService.getNotPreparedOrders()).thenReturn(orders);
        doReturn(0).when(cookController).getIntegerInput(anyInt(),anyInt());
        doNothing().when(cookController).launch(any(Role.class));

        cookController.setOrderReady();

        verify(baseService, times(0)).update(orders.get(0));
        assertEquals(OrderState.Ordered, orders.get(0).getOrderState());
        assertEquals(OrderState.Ordered, orders.get(1).getOrderState());
        assertEquals(OrderState.Ordered, orders.get(2).getOrderState());
    }

    @Test
    @DisplayName("Une nouvelle commande est préparée en plus")
    void checkSetOrderReadyAgain() {
        List<OrderEntity> orders = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            OrderEntity order = new OrderEntity();
            order.setOrderState(OrderState.Ordered);
            orders.add(order);
        }

        when(baseService.getNotPreparedOrders()).thenReturn(orders);
        doReturn(1).when(cookController).getIntegerInput(anyInt(),anyInt());
        doReturn(true).doReturn(false).when(cookController).doAgain();
        doNothing().when(cookController).launch(any(Role.class));

        cookController.setOrderReady();

        verify(cookController, times(2)).setOrderReady();
        assertEquals(OrderState.Prepared, orders.get(0).getOrderState());
        assertEquals(OrderState.Ordered, orders.get(1).getOrderState());
        assertEquals(OrderState.Ordered, orders.get(2).getOrderState());
    }

    @Test
    @DisplayName("Créer un plat")
    void checkCreateDish() {
        doReturn("randomName").when(cookController).getStringInput();
        doReturn(2).when(cookController).getPriceDish();
        doReturn(DishType.Dessert).when(cookController).getDishType();
        doReturn("Glace").when(cookController).getDishCategory(any(DishType.class));
        List<String> idsProducts = new ArrayList<>();
        idsProducts.add("1");
        doReturn(idsProducts).when(cookController).getDishProducts();
        doReturn(1).when(cookController).getIntegerInput(anyInt(),anyInt());
        doNothing().when(cookController).saveDish(any(DishEntity.class),anyBoolean());
        doNothing().when(cookController).launch(any(Role.class));

        cookController.createDish();

        verify(cookController, times(1)).saveDish(any(DishEntity.class),anyBoolean());
    }

    @Test
    @DisplayName("Modifier un plat")
    void checkCreateDishModify() {
        doReturn("randomName").when(cookController).getStringInput();
        when(baseService.getDishById(anyString())).thenReturn(new DishEntity());
        doReturn(2).when(cookController).getPriceDish();
        doReturn(DishType.Dessert).when(cookController).getDishType();
        doReturn("Glace").when(cookController).getDishCategory(any(DishType.class));
        List<String> idsProducts = new ArrayList<>();
        idsProducts.add("1");
        doReturn(idsProducts).when(cookController).getDishProducts();
        doReturn(1).when(cookController).getIntegerInput(anyInt(),anyInt());
        doNothing().when(cookController).saveDish(any(DishEntity.class),anyBoolean());
        doNothing().when(cookController).launch(any(Role.class));

        cookController.createDish();

        verify(cookController, times(1)).saveDish(any(DishEntity.class),eq(true));
    }

    @Test
    @DisplayName("Abandon d'un plat")
    void checkCreateDishCancel() {
        doReturn("randomName").when(cookController).getStringInput();
        doReturn(2).when(cookController).getPriceDish();
        doReturn(DishType.Dessert).when(cookController).getDishType();
        doReturn("Glace").when(cookController).getDishCategory(any(DishType.class));
        List<String> idsProducts = new ArrayList<>();
        idsProducts.add("1");
        doReturn(idsProducts).when(cookController).getDishProducts();
        doReturn(0).when(cookController).getIntegerInput(anyInt(),anyInt());
        doNothing().when(cookController).launch(any(Role.class));

        cookController.createDish();

        verify(cookController, times(0)).saveDish(any(DishEntity.class),anyBoolean());
        verify(cookView, times(1)).displaySaveOrNot(false);
    }

    @Test
    @DisplayName("Récupère le prix du plat")
    void getPriceDish() {
        doReturn(1).when(cookController).getIntegerInput(anyInt(), anyInt());

        DishType dishtype = cookController.getDishType();

        assertEquals(DishType.Starter,dishtype);
    }

    @Test
    @DisplayName("Récupère la catégorie d'un plat")
    void getDishCategory() {
        List<CategoryEntity> catList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            CategoryEntity cat = new CategoryEntity();
            cat.set_id(""+i);
            cat.setDishType(DishType.Dessert);
            catList.add(cat);
        }
        when(baseService.getCategoriesWithDishTypeAsList(any(DishType.class))).thenReturn(catList);
        doReturn(1).when(cookController).getIntegerInput(anyInt(), anyInt());

        String dishCat = cookController.getDishCategory(DishType.Dessert);

        assertEquals("0", dishCat);
    }

    @Test
    @DisplayName("Ajoute une nouvelle catégorie d'un plat")
    void getDishCategoryCreate() {
        List<CategoryEntity> catList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            CategoryEntity cat = new CategoryEntity();
            cat.set_id(""+i);
            cat.setDishType(DishType.Dessert);
            catList.add(cat);
        }
        when(baseService.getCategoriesWithDishTypeAsList(any(DishType.class))).thenReturn(catList);
        doReturn(0).when(cookController).getIntegerInput(anyInt(), anyInt());
        doReturn("newCat").when(cookController).getStringInput();

        String dishCat = cookController.getDishCategory(DishType.Dessert);

        assertEquals("newCat", dishCat);
    }

    @Test
    @DisplayName("Ajoute des ingrédients")
    void getDishProducts() {
        List<ProductEntity> prodList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            ProductEntity prod = new ProductEntity();
            prod.set_id(""+i);
            prodList.add(prod);
        }
        doReturn(prodList).when(cookController).parseToSelectedProducts(anyList(),anyString());
        doReturn("1").when(cookController).getStringMultipleChoices(anyInt(), anyInt());
        doReturn(1).when(cookController).getIntegerInput(anyInt(), anyInt());

        cookController.getDishProducts();

        verify(cookController, times(1)).parseToIdProducts(anyList());
    }

    @Test
    @DisplayName("Annule l'ajout des ingrédients")
    void getDishProductsCancel() {
        List<ProductEntity> prodList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            ProductEntity prod = new ProductEntity();
            prod.set_id(""+i);
            prodList.add(prod);
        }
        doReturn(prodList).when(cookController).parseToSelectedProducts(anyList(),anyString());
        doReturn("1").when(cookController).getStringMultipleChoices(anyInt(), anyInt());
        doReturn(0).doReturn(1).when(cookController).getIntegerInput(anyInt(), anyInt());

        cookController.getDishProducts();

        verify(cookController, times(2)).getDishProducts();
    }

    @Test
    @DisplayName("Parse une liste de ProductEntity en List de String")
    void parseToIdProducts() {
        List<ProductEntity> prodList = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            ProductEntity prod = new ProductEntity();
            prod.set_id(""+i);
            prodList.add(prod);
        }
        List <String> prodString1 = new ArrayList<>();
        prodString1.add("0");
        prodString1.add("1");
        List <String> prodString2 = cookController.parseToIdProducts(prodList);

        assertEquals(prodString1,prodString2);
    }

    @Test
    @DisplayName("Retourne la liste de ProductEntities sélectionnés")
    void parseToSelectedProducts() {
        List<ProductEntity> prodList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            ProductEntity prod = new ProductEntity();
            prod.set_id(""+i);
            prodList.add(prod);
        }
        String multipleChoices = "1/2/4";

        List <ProductEntity> prodList2 = new ArrayList<>();
        prodList2.add(prodList.get(0));
        prodList2.add(prodList.get(1));
        prodList2.add(prodList.get(3));

        List <ProductEntity> result = cookController.parseToSelectedProducts(prodList, multipleChoices);

        assertEquals(prodList2,result);
    }

    @Test
    @DisplayName("Sauvegarde du plat")
    void saveDish() {
        DishEntity dish = new DishEntity();
        dish.set_id("dish");
        dish.setDishType(DishType.Dessert);
        dish.setIdCategory("dish");
        List<String> idsProduct = new ArrayList<>();
        idsProduct.add("1");
        dish.setIdsProduct(idsProduct);
        dish.setOnTheMenu(false);
        dish.setPrice(3);

        CategoryEntity cat = new CategoryEntity();
        cat.set_id("dish");
        when(baseService.getCategoryById(anyString())).thenReturn(cat);

        cookController.saveDish(dish,false);

        verify(baseService, times(1)).save(dish);
        verify(cookView, times(1)).displaySaveOrNot(anyBoolean());
    }

    @Test
    @DisplayName("Sauvegarde du plat modifié")
    void saveDishModify() {
        DishEntity dish = new DishEntity();
        dish.set_id("dish");
        dish.setDishType(DishType.Dessert);
        dish.setIdCategory("dish");
        List<String> idsProduct = new ArrayList<>();
        idsProduct.add("1");
        dish.setIdsProduct(idsProduct);
        dish.setOnTheMenu(false);
        dish.setPrice(3);

        CategoryEntity cat = new CategoryEntity();
        cat.set_id("dish");
        when(baseService.getCategoryById(anyString())).thenReturn(cat);

        cookController.saveDish(dish,true);

        verify(baseService, times(1)).update(dish);
        verify(cookView, times(1)).displaySaveOrNot(anyBoolean());
    }

    @Test
    @DisplayName("Sauvegarde du plat et d'une nouvelle catégorie")
    void saveDishNewCat() {
        DishEntity dish = new DishEntity();
        dish.set_id("dish");
        dish.setDishType(DishType.Dessert);
        dish.setIdCategory("dish");
        List<String> idsProduct = new ArrayList<>();
        idsProduct.add("1");
        dish.setIdsProduct(idsProduct);
        dish.setOnTheMenu(false);
        dish.setPrice(3);

        CategoryEntity cat = new CategoryEntity();
        cat.set_id("dish");
        when(baseService.getCategoryById(anyString())).thenReturn(null);

        cookController.saveDish(dish,false);

        verify(baseService, times(1)).save(any(DishEntity.class));
        verify(baseService, times(1)).save(any(CategoryEntity.class));
        verify(cookView, times(1)).displaySaveOrNot(anyBoolean());
    }

    @Test
    @DisplayName("Arrêter la prise en charge des nouveaux clients")
    void endCooking() {
        when(service.isEndNewClients()).thenReturn(false);
        doReturn(1).when(cookController).getIntegerInput(anyInt(), anyInt());
        doNothing().when(cookController).launch(any(Role.class));

        cookController.endCooking();

        verify(service, times(1)).setEndNewClients(true);
        verify(cookView, times(1)).displayEnded();
    }

    @Test
    @DisplayName("Annuler la prise en charge des nouveaux clients")
    void endCookingCancel() {
        when(service.isEndNewClients()).thenReturn(false);
        doReturn(0).when(cookController).getIntegerInput(anyInt(), anyInt());
        doNothing().when(cookController).launch(any(Role.class));

        cookController.endCooking();

        verify(service, times(0)).setEndNewClients(true);
        verify(cookView, times(0)).displayEnded();
    }

    @Test
    @DisplayName("La prise en charge des nouveaux clients est déjà stoppée")
    void endCookingAlreadyEnded() {
        when(service.isEndNewClients()).thenReturn(true);

        doNothing().when(cookController).launch(any(Role.class));

        cookController.endCooking();

        verify(cookView, times(1)).displayAlreadyEnded();
    }

}
