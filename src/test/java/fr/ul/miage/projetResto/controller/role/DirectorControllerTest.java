package fr.ul.miage.projetResto.controller.role;

import fr.ul.miage.projetResto.appinfo.Service;
import fr.ul.miage.projetResto.constants.Role;
import fr.ul.miage.projetResto.dao.service.BaseService;
import fr.ul.miage.projetResto.model.entity.ProductEntity;
import fr.ul.miage.projetResto.model.entity.TableEntity;
import fr.ul.miage.projetResto.view.role.DirectorView;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@DisplayName("DirectorController")
@ExtendWith(MockitoExtension.class)
class DirectorControllerTest {
    @Mock
    DirectorView directorView;
    @Mock
    BaseService baseService;
    @Mock
    Service service;
    @Spy
    @InjectMocks
    DirectorController directorController;

    @Test
    @DisplayName("Arrêter le service")
    void checkEndService() {
        when(service.isEndService()).thenReturn(false);
        doReturn(1).when(directorController).getIntegerInput(anyInt(), anyInt());
        doNothing().when(directorController).launch(any(Role.class));

        directorController.endService();

        verify(service, times(1)).setEndService(true);
        verify(directorView, times(1)).displayEnded();
    }

    @Test
    @DisplayName("Annuler l'arrêt du service")
    void checkEndServiceCancel() {
        when(service.isEndService()).thenReturn(false);
        doReturn(0).when(directorController).getIntegerInput(anyInt(), anyInt());
        doNothing().when(directorController).launch(any(Role.class));

        directorController.endService();

        verify(service, times(0)).setEndService(true);
        verify(directorView, times(0)).displayEnded();
    }

    @Test
    @DisplayName("La fin de service est déjà annoncée")
    void endServiceAlreadyEnded() {
        when(service.isEndService()).thenReturn(true);
        doNothing().when(directorController).launch(any(Role.class));

        directorController.endService();

        verify(directorView, times(1)).displayAlreadyEnded();
    }

    @Test
    @DisplayName("Retourne un nouveau produit et son stock")
    void checkCreateProduct() {
        ProductEntity productExpected = new ProductEntity();
        productExpected.setStock(50);
        productExpected.set_id("Sucre");

        doReturn("Sucre").when(directorController).getStringInput();
        when(baseService.getProductById(anyString())).thenReturn(null);
        doReturn(50).when(directorController).getIntegerInput(anyInt(), anyInt());

        ProductEntity productActual = directorController.createProduct();

        assertEquals(productExpected, productActual);
    }

    @Test
    @DisplayName("Retourne un nouveau produit et son stock avec un échec de saisie sur un produit déjà existant")
    void checkCreateProductalreadyExist() {
        ProductEntity productExpected = new ProductEntity();
        productExpected.setStock(50);
        productExpected.set_id("Sucre");

        doReturn("Sel").doReturn("Sucre").when(directorController).getStringInput();
        when(baseService.getProductById(anyString())).thenReturn(new ProductEntity()).thenReturn(null);
        doReturn(50).when(directorController).getIntegerInput(anyInt(), anyInt());

        ProductEntity productActual = directorController.createProduct();

        assertEquals(productExpected, productActual);
    }

    @Test
    @DisplayName("Sauvegarde le produit et son stock en base")
    void checkSaveProduct() {
        ProductEntity productExpected = new ProductEntity();
        productExpected.setStock(50);
        productExpected.set_id("Sucre");
        doReturn(false).when(directorController).doAgain();

        directorController.saveProduct(productExpected);

        verify(baseService, times(1)).update(any(ProductEntity.class));
        verify(baseService, times(1)).save(any(ProductEntity.class));
    }

    @Test
    @DisplayName("Ajout du stock à un produit existant")
    void checkAddStockProduct() {
        List<ProductEntity> products = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            ProductEntity product = new ProductEntity();
            product.setStock(i + 5);
            product.set_id("" + i);
            products.add(product);
        }
        ProductEntity productExpected = new ProductEntity();
        productExpected.set_id("1");
        productExpected.setStock(56);
        doReturn(50).when(directorController).getIntegerInput(anyInt(), anyInt());

        ProductEntity productActual = directorController.addStockProduct(products, 1);

        assertEquals(productExpected, productActual);
    }

    @Test
    @DisplayName("Ajout du stock à un produit déjà au max du stock")
    void checkAddStockProductMaxStock() {
        List<ProductEntity> products = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            ProductEntity product = new ProductEntity();
            product.setStock(i + 99);
            product.set_id("" + i);
            products.add(product);
        }
        ProductEntity productExpected = new ProductEntity();
        productExpected.set_id("0");
        productExpected.setStock(100);
        doReturn(1).when(directorController).getIntegerInput(anyInt(), anyInt());

        ProductEntity productActual = directorController.addStockProduct(products, 1);

        assertEquals(productExpected, productActual);
        verify(directorView, times(1)).displayStockMax();
    }

    @Test
    @DisplayName("Manage le stock d'un nouveau produit")
    void checkManageStockNewProduct() {
        List<ProductEntity> products = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            ProductEntity product = new ProductEntity();
            product.setStock(i + 5);
            product.set_id("" + i);
            products.add(product);
        }
        when(baseService.getAllProducts()).thenReturn(products);
        doReturn(1).when(directorController).getIntegerInput(anyInt(), anyInt());
        doReturn(new ProductEntity()).when(directorController).createProduct();
        doNothing().when(directorController).saveProduct(any(ProductEntity.class));
        doNothing().when(directorController).launch(Role.Director);

        directorController.manageStocks();

        verify(directorController, times(1)).saveProduct(any(ProductEntity.class));
    }

    @Test
    @DisplayName("Manage le stock d'un produit existant")
    void checkManageStockCommonProduct() {
        List<ProductEntity> products = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            ProductEntity product = new ProductEntity();
            product.setStock(i + 5);
            product.set_id("" + i);
            products.add(product);
        }
        when(baseService.getAllProducts()).thenReturn(products);
        doReturn(3).when(directorController).getIntegerInput(anyInt(), anyInt());
        doReturn(new ProductEntity()).when(directorController).addStockProduct(anyList(), anyInt());
        doNothing().when(directorController).saveProduct(any(ProductEntity.class));
        doNothing().when(directorController).launch(Role.Director);

        directorController.manageStocks();

        verify(directorController, times(1)).saveProduct(any(ProductEntity.class));
    }

    @Test
    @DisplayName("Annule le management des stocks")
    void checkManageStockCancel() {
        List<ProductEntity> products = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            ProductEntity product = new ProductEntity();
            product.setStock(i + 5);
            product.set_id("" + i);
            products.add(product);
        }
        when(baseService.getAllProducts()).thenReturn(products);
        doReturn(0).when(directorController).getIntegerInput(anyInt(), anyInt());
        doNothing().when(directorController).launch(Role.Director);

        directorController.manageStocks();

        verify(directorController, times(0)).saveProduct(any(ProductEntity.class));
    }

    @Test
    @DisplayName("Manage les tables uniquement ajout et Trop de tables")
    void checkManageTablesTooManyTables() {
        List<TableEntity> tables = new ArrayList<>();
        for (int i = 1; i < 101; i++) {
            TableEntity table = new TableEntity();
            table.set_id("" + i);
            tables.add(table);
        }

        when(baseService.getAllTables()).thenReturn(tables);
        doReturn(1).when(directorController).getIntegerInput(anyInt(),anyInt());
        doNothing().when(directorController).launch(Role.Director);

        directorController.manageTables();

        verify(directorView, times(1)).displayEnoughTables();
    }

    @Test
    @DisplayName("Manage les tables uniquement ajout")
    void checkManageTablesAddTable() {
        List<TableEntity> tables = new ArrayList<>();
        for (int i = 1; i < 5; i++) {
            TableEntity table = new TableEntity();
            table.set_id("" + i);
            tables.add(table);
        }

        when(baseService.getAllTables()).thenReturn(tables);
        doReturn(1).when(directorController).getIntegerInput(anyInt(),anyInt());
        doNothing().when(directorController).launch(Role.Director);

        directorController.manageTables();

        verify(directorController, times(1)).addTable(anyList());
    }

    @Test
    @DisplayName("Manage les tables uniquement suppression")
    void checkManageTablesRemoveTable() {
        List<TableEntity> tables = new ArrayList<>();
        for (int i = 1; i < 5; i++) {
            TableEntity table = new TableEntity();
            table.set_id("" + i);
            tables.add(table);
        }

        when(baseService.getAllTables()).thenReturn(tables);
        doReturn(2).when(directorController).getIntegerInput(anyInt(),anyInt());
        doNothing().when(directorController).launch(Role.Director);

        directorController.manageTables();

        verify(directorController, times(1)).removeTable();
    }

    @Test
    @DisplayName("Manage les tables annuler")
    void checkManageTablesCancel() {
        List<TableEntity> tables = new ArrayList<>();
        for (int i = 1; i < 5; i++) {
            TableEntity table = new TableEntity();
            table.set_id("" + i);
            tables.add(table);
        }

        when(baseService.getAllTables()).thenReturn(tables);
        doReturn(0).when(directorController).getIntegerInput(anyInt(),anyInt());
        doNothing().when(directorController).launch(Role.Director);

        directorController.manageTables();

        verify(directorController, times(0)).addTable(anyList());
        verify(directorController, times(0)).removeTable();
        verify(directorView, times(0)).displayEnoughTables();
    }

    @Test
    @DisplayName("Ajoute une table")
    void checkAddTable() {
        doReturn("1").when(directorController).getFreeNumberTable(anyList());
        doReturn(4).when(directorController).getIntegerInput(anyInt(),anyInt());

        directorController.addTable(new ArrayList<>());

        verify(baseService, times(1)).save(any(TableEntity.class));
    }

    @Test
    @DisplayName("Retire une table")
    void checkRemoveTable() {
        List<TableEntity> tables = new ArrayList<>();
        for (int i = 1; i < 5; i++) {
            TableEntity table = new TableEntity();
            table.set_id("" + i);
            tables.add(table);
        }

        when(baseService.getAllRemovableTables()).thenReturn(tables);
        doReturn(1).when(directorController).getIntegerInput(anyInt(),anyInt());

        directorController.removeTable();

        verify(baseService, times(1)).deleteTable(anyString());
    }

    @Test
    @DisplayName("Retire une table mais liste des tables vide")
    void checkRemoveTableEmptyList() {
        List<TableEntity> tables = new ArrayList<>();

        when(baseService.getAllRemovableTables()).thenReturn(tables);

        directorController.removeTable();

        verify(directorView, times(1)).displayNoTableCanBeRemoved();
    }

    @Test
    @DisplayName("Récupère le numéro libre d'une table")
    void checkGetFreeNumberTable() {
        List<TableEntity> tables = new ArrayList<>();
        for (int i = 1; i < 5; i++) {
            TableEntity table = new TableEntity();
            table.set_id("" + i);
            tables.add(table);
        }

        String result = directorController.getFreeNumberTable(tables);

        assertEquals("5", result);
    }

    @Test
    @DisplayName("Récupère le numéro libre d'une table sur une suite de nombre non linéaire")
    void checkGetFreeNumberTableNoLinear() {
        List<TableEntity> tables = new ArrayList<>();
        for (int i = 1; i < 3; i++) {
            TableEntity table = new TableEntity();
            table.set_id("" + i);
            tables.add(table);
        }

        TableEntity table = new TableEntity();
        table.set_id("5");
        tables.add(table);

        String result = directorController.getFreeNumberTable(tables);

        assertEquals("3", result);
    }

    @Test
    @DisplayName("Récupère le numéro 1 car aucune table n'est présente en base")
    void checkGetFreeNumberTableNoTables() {
        List<TableEntity> tables = new ArrayList<>();

        String result = directorController.getFreeNumberTable(tables);

        assertEquals("1", result);
    }
}
