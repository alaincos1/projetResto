package fr.ul.miage.projetResto.model.entity;

import fr.ul.miage.projetResto.dao.service.BaseService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@DisplayName("DishEntity")
@ExtendWith(MockitoExtension.class)
class DishEntityTest {
    @InjectMocks
    DishEntity dishEntity;

    @Test
    @DisplayName("Retourne vrai car stock du produit suffisant")
    void checkCheckStock(@Mock BaseService baseService) {
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
    void checkCheckStockNotEnough(@Mock BaseService baseService) {
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
    void checkChangeStockAdd(@Mock BaseService baseService) {
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
        verify(baseService, times(1)).save(productExpected1);
        verify(baseService, times(1)).save(productExpected2);
    }

    @Test
    @DisplayName("Enlève du stock à tous les produits d'un plat")
    void checkChangeStockRemove(@Mock BaseService baseService) {
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
        verify(baseService, times(1)).save(productExpected1);
        verify(baseService, times(1)).save(productExpected2);
    }
}
