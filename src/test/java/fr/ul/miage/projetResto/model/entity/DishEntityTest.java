package fr.ul.miage.projetResto.model.entity;

import fr.ul.miage.projetResto.constants.DishType;
import fr.ul.miage.projetResto.dao.service.BaseService;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@DisplayName("DishEntity")
@ExtendWith(MockitoExtension.class)
class DishEntityTest {
    private final EasyRandom easyRandom = new EasyRandom();
    @InjectMocks
    DishEntity dishEntity;

    @Test
    @DisplayName("Retourne vrai car stock du produit suffisant")
    void testCheckStock(@Mock BaseService baseService) {
        dishEntity = new DishEntity();

        List<String> products = new ArrayList<>();
        products.add("salade");
        products.add("tomate");
        dishEntity.setIdsProduct(products);

        ProductEntity product1 = new ProductEntity();
        product1.set_id("salade");
        product1.setStock(15);
        ProductEntity product2 = new ProductEntity();
        product2.set_id("tomate");
        product2.setStock(10);
        when(baseService.getProductById(anyString())).thenReturn(product1).thenReturn(product2);

        boolean enoughStock = dishEntity.checkStock(baseService);
        assertTrue(enoughStock);
    }

    @Test
    @DisplayName("Retourne faux car stock d'un produit insuffisant")
    void testCheckStockNotEnough(@Mock BaseService baseService) {
        dishEntity = new DishEntity();

        List<String> products = new ArrayList<>();
        products.add("salade");
        products.add("tomate");
        dishEntity.setIdsProduct(products);

        ProductEntity product1 = new ProductEntity();
        product1.set_id("salade");
        product1.setStock(15);
        ProductEntity product2 = new ProductEntity();
        product2.set_id("tomate");
        product2.setStock(0);
        when(baseService.getProductById(anyString())).thenReturn(product1).thenReturn(product2);

        boolean enoughStock = dishEntity.checkStock(baseService);
        assertFalse(enoughStock);
    }

    @Test
    @DisplayName("Ajoute du stock à tous les produits d'un plat")
    void testChangeStockAdd(@Mock BaseService baseService) {
        dishEntity = new DishEntity();

        List<String> products = new ArrayList<>();
        products.add("salade");
        products.add("tomate");
        dishEntity.setIdsProduct(products);

        ProductEntity product1 = new ProductEntity();
        product1.set_id("salade");
        product1.setStock(15);
        ProductEntity product2 = new ProductEntity();
        product2.set_id("tomate");
        product2.setStock(0);
        ProductEntity productExpected1 = new ProductEntity();
        productExpected1.set_id("salade");
        productExpected1.setStock(16);
        ProductEntity productExpected2 = new ProductEntity();
        productExpected2.set_id("tomate");
        productExpected2.setStock(1);

        when(baseService.getProductById(anyString())).thenReturn(product1).thenReturn(product2);

        dishEntity.changeStock(baseService, true);
        verify(baseService, times(1)).update(productExpected1);
        verify(baseService, times(1)).update(productExpected2);
    }

    @Test
    @DisplayName("Enlève du stock à tous les produits d'un plat")
    void testChangeStockRemove(@Mock BaseService baseService) {
        dishEntity = new DishEntity();

        List<String> products = new ArrayList<>();
        products.add("salade");
        products.add("tomate");
        dishEntity.setIdsProduct(products);

        ProductEntity product1 = new ProductEntity();
        product1.set_id("salade");
        product1.setStock(15);
        ProductEntity product2 = new ProductEntity();
        product2.set_id("tomate");
        product2.setStock(18);
        ProductEntity productExpected1 = new ProductEntity();
        productExpected1.set_id("salade");
        productExpected1.setStock(14);
        ProductEntity productExpected2 = new ProductEntity();
        productExpected2.set_id("tomate");
        productExpected2.setStock(17);

        when(baseService.getProductById(anyString())).thenReturn(product1).thenReturn(product2);

        dishEntity.changeStock(baseService, false);
        verify(baseService, times(1)).update(productExpected1);
        verify(baseService, times(1)).update(productExpected2);
    }

    @Test
    void testOrderDishByTypeWithDifferentLowerDishType() {
        DishEntity dish1 = easyRandom.nextObject(DishEntity.class);
        DishEntity dish2 = easyRandom.nextObject(DishEntity.class);
        dish1.setDishType(DishType.MainCourse);
        dish2.setDishType(DishType.Dessert);

        Integer actual = DishEntity.orderDishByType(dish1, dish2);

        Assertions.assertEquals(-1, actual);
    }

    @Test
    void testOrderDishByTypeWithDifferentHigherDishType() {
        DishEntity dish1 = easyRandom.nextObject(DishEntity.class);
        DishEntity dish2 = easyRandom.nextObject(DishEntity.class);
        dish1.setDishType(DishType.Dessert);
        dish2.setDishType(DishType.MainCourse);

        Integer actual = DishEntity.orderDishByType(dish1, dish2);

        Assertions.assertEquals(1, actual);
    }

    @Test
    void testOrderDishByTypeWithSameDishType() {
        DishEntity dish1 = easyRandom.nextObject(DishEntity.class);
        DishEntity dish2 = easyRandom.nextObject(DishEntity.class);
        dish1.setDishType(DishType.MainCourse);
        dish2.setDishType(DishType.MainCourse);

        Integer actual = DishEntity.orderDishByType(dish1, dish2);

        Assertions.assertEquals(0, actual);
    }
}


