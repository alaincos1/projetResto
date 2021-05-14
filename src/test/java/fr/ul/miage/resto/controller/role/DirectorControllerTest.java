package fr.ul.miage.resto.controller.role;

import fr.ul.miage.resto.appinfo.Service;
import fr.ul.miage.resto.constants.InfoRestaurant;
import fr.ul.miage.resto.constants.MealType;
import fr.ul.miage.resto.constants.Role;
import fr.ul.miage.resto.dao.service.BaseService;
import fr.ul.miage.resto.model.entity.*;
import fr.ul.miage.resto.utils.DateDto;
import fr.ul.miage.resto.view.role.DirectorView;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@DisplayName("DirectorController")
@ExtendWith(MockitoExtension.class)
class DirectorControllerTest {
    EasyRandom easyRandom = new EasyRandom();

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
    void testEndService() {
        when(service.isEndService()).thenReturn(false);
        doReturn(1).when(directorController).getIntegerInput(anyInt(), anyInt());
        doNothing().when(directorController).launch(any(Role.class));

        directorController.endService();

        verify(service, times(1)).setEndService(true);
        verify(directorView, times(1)).displayMessage("La fin du service (des prises des commandes) est annoncée.");
    }

    @Test
    @DisplayName("Annuler l'arrêt du service")
    void testEndServiceCancel() {
        when(service.isEndService()).thenReturn(false);
        doReturn(0).when(directorController).getIntegerInput(anyInt(), anyInt());
        doNothing().when(directorController).launch(any(Role.class));

        directorController.endService();

        verify(service, times(0)).setEndService(true);
        verify(directorView, times(0)).displayMessage("La fin du service (des prises des commandes) est annoncée.");
    }

    @Test
    @DisplayName("La fin de service est déjà annoncée")
    void testEndServiceAlreadyEnded() {
        when(service.isEndService()).thenReturn(true);
        doNothing().when(directorController).launch(any(Role.class));

        directorController.endService();

        verify(directorView, times(1)).displayMessage("La fin du service (des prises des commandes) a déjà été annoncée.");
    }

    @Test
    @DisplayName("Retourne un nouveau produit et son stock")
    void testCreateProduct() {
        ProductEntity productExpected = new ProductEntity();
        productExpected.setStock(50);
        productExpected.setId("Sucre");

        doReturn("Sucre").when(directorController).getStringInput();
        when(baseService.getProductById(anyString())).thenReturn(null);
        doReturn(50).when(directorController).getIntegerInput(anyInt(), anyInt());

        ProductEntity productActual = directorController.createProduct();

        assertEquals(productExpected, productActual);
    }

    @Test
    @DisplayName("Retourne un nouveau produit et son stock avec un échec de saisie sur un produit déjà existant")
    void testCreateProductalreadyExist() {
        ProductEntity productExpected = new ProductEntity();
        productExpected.setStock(50);
        productExpected.setId("Sucre");

        doReturn("Sel").doReturn("Sucre").when(directorController).getStringInput();
        when(baseService.getProductById(anyString())).thenReturn(new ProductEntity()).thenReturn(null);
        doReturn(50).when(directorController).getIntegerInput(anyInt(), anyInt());

        ProductEntity productActual = directorController.createProduct();

        assertEquals(productExpected, productActual);
    }

    @Test
    @DisplayName("Sauvegarde le produit et son stock en base")
    void testSaveProduct() {
        ProductEntity productExpected = new ProductEntity();
        productExpected.setStock(50);
        productExpected.setId("Sucre");
        doReturn(false).when(directorController).doAgain();

        directorController.saveProduct(productExpected);

        verify(baseService, times(1)).update(any(ProductEntity.class));
        verify(baseService, times(1)).save(any(ProductEntity.class));
    }

    @Test
    @DisplayName("Ajout du stock à un produit existant")
    void testAddStockProduct() {
        List<ProductEntity> products = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            ProductEntity product = new ProductEntity();
            product.setStock(i + 5);
            product.setId("" + i);
            products.add(product);
        }
        ProductEntity productExpected = new ProductEntity();
        productExpected.setId("1");
        productExpected.setStock(56);
        doReturn(50).when(directorController).getIntegerInput(anyInt(), anyInt());

        ProductEntity productActual = directorController.addStockProduct(products, 1);

        assertEquals(productExpected, productActual);
    }

    @Test
    @DisplayName("Ajout du stock à un produit déjà au max du stock")
    void testAddStockProductMaxStock() {
        List<ProductEntity> products = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            ProductEntity product = new ProductEntity();
            product.setStock(i + 99);
            product.setId("" + i);
            products.add(product);
        }
        ProductEntity productExpected = new ProductEntity();
        productExpected.setId("0");
        productExpected.setStock(100);
        doReturn(1).when(directorController).getIntegerInput(anyInt(), anyInt());

        ProductEntity productActual = directorController.addStockProduct(products, 1);

        assertEquals(productExpected, productActual);
        verify(directorView, times(1)).displayMessage("Stock maximal pour ce produit. Entrez un autre produit.");
    }

    @Test
    @DisplayName("Manage le stock d'un nouveau produit")
    void testManageStockNewProduct() {
        List<ProductEntity> products = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            ProductEntity product = new ProductEntity();
            product.setStock(i + 5);
            product.setId("" + i);
            products.add(product);
        }
        when(baseService.getAllProducts()).thenReturn(products);
        doReturn(1).when(directorController).getIntegerInput(anyInt(), anyInt());
        doReturn(new ProductEntity()).when(directorController).createProduct();
        doNothing().when(directorController).saveProduct(any(ProductEntity.class));
        doNothing().when(directorController).launch(Role.DIRECTOR);

        directorController.manageStocks();

        verify(directorController, times(1)).saveProduct(any(ProductEntity.class));
    }

    @Test
    @DisplayName("Manage le stock d'un produit existant")
    void testManageStockCommonProduct() {
        List<ProductEntity> products = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            ProductEntity product = new ProductEntity();
            product.setStock(i + 5);
            product.setId("" + i);
            products.add(product);
        }
        when(baseService.getAllProducts()).thenReturn(products);
        doReturn(3).when(directorController).getIntegerInput(anyInt(), anyInt());
        doReturn(new ProductEntity()).when(directorController).addStockProduct(anyList(), anyInt());
        doNothing().when(directorController).saveProduct(any(ProductEntity.class));
        doNothing().when(directorController).launch(Role.DIRECTOR);

        directorController.manageStocks();

        verify(directorController, times(1)).saveProduct(any(ProductEntity.class));
    }

    @Test
    @DisplayName("Annule le management des stocks")
    void testManageStockCancel() {
        List<ProductEntity> products = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            ProductEntity product = new ProductEntity();
            product.setStock(i + 5);
            product.setId("" + i);
            products.add(product);
        }
        when(baseService.getAllProducts()).thenReturn(products);
        doReturn(0).when(directorController).getIntegerInput(anyInt(), anyInt());
        doNothing().when(directorController).launch(Role.DIRECTOR);

        directorController.manageStocks();

        verify(directorController, times(0)).saveProduct(any(ProductEntity.class));
    }

    @Test
    void testManageEmployeeAddEmployee() {
        doReturn(1).when(directorController).getIntegerInput(anyInt(), anyInt());
        doNothing().when(directorController).launch(Role.DIRECTOR);
        doNothing().when(directorController).addEmployee();

        directorController.manageEmployees();

        verify(directorController, times(1)).addEmployee();
    }

    @Test
    void testManageEmployeeWithReturn() {
        doReturn(0).when(directorController).getIntegerInput(anyInt(), anyInt());
        doNothing().when(directorController).launch(Role.DIRECTOR);

        directorController.manageEmployees();

        verify(directorController, times(1)).launch(any(Role.class));
    }

    @Test
    void testManageEmployeeUpdateEmployee() {
        UserEntity user = easyRandom.nextObject(UserEntity.class);
        List<UserEntity> users = new ArrayList<>();
        users.add(user);

        doReturn(2).doReturn(1).when(directorController).getIntegerInput(anyInt(), anyInt());
        doNothing().when(directorController).launch(Role.DIRECTOR);
        doNothing().when(directorController).updateEmployee(any(UserEntity.class));
        when(baseService.getAllUsers()).thenReturn(users);

        directorController.manageEmployees();

        verify(directorController, times(1)).updateEmployee(any(UserEntity.class));
    }

    @Test
    void testManageEmployeeDeleteEmployee() {
        UserEntity user = easyRandom.nextObject(UserEntity.class);
        List<UserEntity> users = new ArrayList<>();
        users.add(user);

        doReturn(3).doReturn(1).when(directorController).getIntegerInput(anyInt(), anyInt());
        doNothing().when(directorController).launch(Role.DIRECTOR);
        doNothing().when(directorController).deleteEmployee(any(UserEntity.class));
        when(baseService.getAllUsers()).thenReturn(users);

        directorController.manageEmployees();

        verify(directorController, times(1)).deleteEmployee(any(UserEntity.class));
    }

    @Test
    void testManageEmployeePromoteEmployee() {
        UserEntity user = easyRandom.nextObject(UserEntity.class);
        List<UserEntity> users = new ArrayList<>();
        users.add(user);

        doReturn(4).doReturn(1).when(directorController).getIntegerInput(anyInt(), anyInt());
        doNothing().when(directorController).launch(Role.DIRECTOR);
        doNothing().when(directorController).promoteEmployee(any(UserEntity.class));
        when(baseService.getAllUsers()).thenReturn(users);

        directorController.manageEmployees();

        verify(directorController, times(1)).promoteEmployee(any(UserEntity.class));
    }

    @Test
    void testPromoteEmployeeToButtler() {
        UserEntity user = easyRandom.nextObject(UserEntity.class);

        doReturn(1).when(directorController).getIntegerInput(anyInt(), anyInt());

        when(baseService.promoteUser(any(UserEntity.class), any(Role.class))).thenReturn(true);

        directorController.promoteEmployee(user);

        verify(directorView, times(1)).displaySuccess();
        verify(directorController, times(0)).callAction(anyInt());
    }

    @Test
    void testPromoteEmployeeToDirector() {
        UserEntity user = easyRandom.nextObject(UserEntity.class);

        doReturn(0).when(directorController).getIntegerInput(anyInt(), anyInt());
        doNothing().when(directorController).callAction(anyInt());

        when(baseService.promoteUser(any(UserEntity.class), any(Role.class))).thenReturn(true);

        directorController.promoteEmployee(user);

        verify(directorView, times(1)).displaySuccess();
    }

    @Test
    void testPromoteEmployeeToWithError() {
        UserEntity user = easyRandom.nextObject(UserEntity.class);

        doReturn(1).when(directorController).getIntegerInput(anyInt(), anyInt());

        when(baseService.promoteUser(any(UserEntity.class), any(Role.class))).thenReturn(false);

        directorController.promoteEmployee(user);

        verify(directorView, times(1)).displayError();
    }

    @Test
    void testAddEmployee() {
        doReturn("id").when(directorController).getUserIdInput();
        doReturn(0).when(directorController).getIntegerInput(anyInt(), anyInt());
        when(baseService.save(any(UserEntity.class))).thenReturn(true);

        easyRandom.nextObject(UserEntity.class);

        directorController.addEmployee();

        verify(directorView, times(1)).displaySuccess();
    }

    @Test
    void testAddEmployeeWithError() {
        doReturn("id").when(directorController).getUserIdInput();
        doReturn(0).when(directorController).getIntegerInput(anyInt(), anyInt());
        when(baseService.save(any(UserEntity.class))).thenReturn(false);

        easyRandom.nextObject(UserEntity.class);

        directorController.addEmployee();

        verify(directorView, times(1)).displayError();
    }

    @Test
    void testUpdateEmployee() {
        UserEntity user = easyRandom.nextObject(UserEntity.class);
        doReturn(0).when(directorController).getIntegerInput(anyInt(), anyInt());

        when(baseService.update(any(UserEntity.class))).thenReturn(true);

        directorController.updateEmployee(user);

        verify(directorView, times(1)).displaySuccess();
    }

    @Test
    void testUpdateEmployeeWithError() {
        UserEntity user = easyRandom.nextObject(UserEntity.class);
        doReturn(0).when(directorController).getIntegerInput(anyInt(), anyInt());

        when(baseService.update(any(UserEntity.class))).thenReturn(false);

        directorController.updateEmployee(user);

        verify(directorView, times(1)).displayError();
    }

    @Test
    void testDeleteEmployee() {
        UserEntity user = easyRandom.nextObject(UserEntity.class);

        when(baseService.safeDeleteUser(any(UserEntity.class))).thenReturn(true);

        directorController.deleteEmployee(user);

        verify(directorView, times(1)).displaySuccess();
    }

    @Test
    void testDeleteEmployeeWithError() {
        UserEntity user = easyRandom.nextObject(UserEntity.class);

        when(baseService.safeDeleteUser(any(UserEntity.class))).thenReturn(false);

        directorController.deleteEmployee(user);

        verify(directorView, times(1)).displayError();
    }

    @Test
    @DisplayName("Manage les tables uniquement ajout et Trop de tables")
    void testManageTablesTooManyTables() {
        List<TableEntity> tables = new ArrayList<>();
        for (int i = 1; i < 101; i++) {
            TableEntity table = new TableEntity();
            table.setId("" + i);
            tables.add(table);
        }

        when(baseService.getAllTables()).thenReturn(tables);
        doReturn(1).when(directorController).getIntegerInput(anyInt(), anyInt());
        doNothing().when(directorController).launch(Role.DIRECTOR);

        directorController.manageTables();

        verify(directorView, times(1)).displayMessage("Il y a déjà le maximum de tables possible: " + InfoRestaurant.MAX_TABLES.getValue());
    }

    @Test
    @DisplayName("Manage les tables uniquement ajout")
    void testManageTablesAddTable() {
        List<TableEntity> tables = new ArrayList<>();
        for (int i = 1; i < 5; i++) {
            TableEntity table = new TableEntity();
            table.setId("" + i);
            tables.add(table);
        }

        when(baseService.getAllTables()).thenReturn(tables);
        doReturn(1).when(directorController).getIntegerInput(anyInt(), anyInt());
        doNothing().when(directorController).launch(Role.DIRECTOR);

        directorController.manageTables();

        verify(directorController, times(1)).addTable(anyList());
    }

    @Test
    @DisplayName("Manage les tables uniquement suppression")
    void testManageTablesRemoveTable() {
        List<TableEntity> tables = new ArrayList<>();
        for (int i = 1; i < 5; i++) {
            TableEntity table = new TableEntity();
            table.setId("" + i);
            tables.add(table);
        }

        when(baseService.getAllTables()).thenReturn(tables);
        doReturn(2).when(directorController).getIntegerInput(anyInt(), anyInt());
        doNothing().when(directorController).launch(Role.DIRECTOR);

        directorController.manageTables();

        verify(directorController, times(1)).removeTable();
    }

    @Test
    @DisplayName("Manage les tables annuler")
    void testManageTablesCancel() {
        List<TableEntity> tables = new ArrayList<>();
        for (int i = 1; i < 5; i++) {
            TableEntity table = new TableEntity();
            table.setId("" + i);
            tables.add(table);
        }

        when(baseService.getAllTables()).thenReturn(tables);
        doReturn(0).when(directorController).getIntegerInput(anyInt(), anyInt());
        doNothing().when(directorController).launch(Role.DIRECTOR);

        directorController.manageTables();

        verify(directorController, times(0)).addTable(anyList());
        verify(directorController, times(0)).removeTable();
        verify(directorView, times(0)).displayMessage("Il y a déjà le maximum de tables possible: " + InfoRestaurant.MAX_TABLES.getValue());
    }

    @Test
    @DisplayName("Ajoute une table")
    void testAddTable() {
        doReturn("1").when(directorController).getFreeNumberTable(anyList());
        doReturn(4).when(directorController).getIntegerInput(anyInt(), anyInt());

        directorController.addTable(new ArrayList<>());

        verify(baseService, times(1)).save(any(TableEntity.class));
    }

    @Test
    @DisplayName("Retire une table")
    void testRemoveTable() {
        List<TableEntity> tables = new ArrayList<>();
        for (int i = 1; i < 5; i++) {
            TableEntity table = new TableEntity();
            table.setId("" + i);
            tables.add(table);
        }

        when(baseService.getAllRemovableTables()).thenReturn(tables);
        doReturn(1).when(directorController).getIntegerInput(anyInt(), anyInt());

        directorController.removeTable();

        verify(baseService, times(1)).deleteTable(anyString());
    }

    @Test
    @DisplayName("Retire une table mais liste des tables vide")
    void testRemoveTableEmptyList() {
        List<TableEntity> tables = new ArrayList<>();

        when(baseService.getAllRemovableTables()).thenReturn(tables);

        directorController.removeTable();

        verify(directorView, times(1)).displayMessage("Aucune table est supprimable (Clients attablés, réservations, etc...)");
    }

    @Test
    @DisplayName("Récupère le numéro libre d'une table")
    void testGetFreeNumberTable() {
        List<TableEntity> tables = new ArrayList<>();
        for (int i = 1; i < 5; i++) {
            TableEntity table = new TableEntity();
            table.setId("" + i);
            tables.add(table);
        }

        String result = directorController.getFreeNumberTable(tables);

        assertEquals("5", result);
    }

    @Test
    @DisplayName("Récupère le numéro libre d'une table sur une suite de nombre non linéaire")
    void testGetFreeNumberTableNoLinear() {
        List<TableEntity> tables = new ArrayList<>();
        for (int i = 1; i < 3; i++) {
            TableEntity table = new TableEntity();
            table.setId("" + i);
            tables.add(table);
        }

        TableEntity table = new TableEntity();
        table.setId("5");
        tables.add(table);

        String result = directorController.getFreeNumberTable(tables);

        assertEquals("3", result);
    }

    @Test
    @DisplayName("Récupère le numéro 1 car aucune table n'est présente en base")
    void testGetFreeNumberTableNoTables() {
        List<TableEntity> tables = new ArrayList<>();

        String result = directorController.getFreeNumberTable(tables);

        assertEquals("1", result);
    }

    @Test
    void testManageDayMenuWithReturn() {
        doReturn(0).when(directorController).getIntegerInput(anyInt(), anyInt());

        doNothing().when(directorController).launch(Role.DIRECTOR);

        directorController.manageDayMenu();

        verify(directorController, times(1)).launch(any(Role.class));
    }

    @Test
    @DisplayName("Test le cas ou la carte des menus est vide et que l'on voudrait la visualiser ou supprimer un plat")
    void testManageDayMenuWithNoDishOnTheMenu() {
        doReturn(1).when(directorController).getIntegerInput(anyInt(), anyInt());

        when(baseService.getAllDishesOntheMenuOrdered()).thenReturn(null);
        doNothing().when(directorController).launch(any(Role.class));

        directorController.manageDayMenu();

        verify(directorView, times(1)).displayMessage("Il n'y a pas de plats dans le menu");
    }

    @Test
    @DisplayName("Test le cas ou l'on voudrait ajouter un plat à la carte mais que tout les plats sont déjà dans le menu ou qu'il n'y a pas de plats en base")
    void testManageDayMenuWithAllDishOnTheMenu() {
        doReturn(2).when(directorController).getIntegerInput(anyInt(), anyInt());

        when(baseService.getAllDishesNotOnTheMenuOrdered()).thenReturn(null);
        doNothing().when(directorController).launch(any(Role.class));

        directorController.manageDayMenu();

        verify(directorView, times(1)).displayMessage("Il n'y a aucuns plats d'enregistrés ou ils sont déjà tous dans le menu");
    }

    @Test
    void testManageDayMenuVisualize() {
        doReturn(1).when(directorController).getIntegerInput(anyInt(), anyInt());

        List<DishEntity> dishs = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            dishs.add(easyRandom.nextObject(DishEntity.class));
        }

        when(baseService.getAllDishesOntheMenuOrdered()).thenReturn(dishs);
        doNothing().when(directorController).launch(any(Role.class));

        directorController.manageDayMenu();

        verify(directorView, times(1)).displayDishChoice(anyList(), anyBoolean());
    }

    @Test
    void testManageDayMenuAdd() {
        doReturn(2).when(directorController).getIntegerInput(anyInt(), anyInt());

        List<DishEntity> dishs = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            dishs.add(easyRandom.nextObject(DishEntity.class));
        }

        when(baseService.getAllDishesNotOnTheMenuOrdered()).thenReturn(dishs);
        doNothing().when(directorController).launch(any(Role.class));
        doNothing().when(directorController).addOrDeleteDishOnTheMenu(anyList());

        directorController.manageDayMenu();

        verify(directorController, times(1)).addOrDeleteDishOnTheMenu(anyList());
    }

    @Test
    void testManageDayMenuDelete() {
        doReturn(3).when(directorController).getIntegerInput(anyInt(), anyInt());

        List<DishEntity> dishs = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            dishs.add(easyRandom.nextObject(DishEntity.class));
        }

        when(baseService.getAllDishesOntheMenuOrdered()).thenReturn(dishs);
        doNothing().when(directorController).launch(any(Role.class));
        doNothing().when(directorController).addOrDeleteDishOnTheMenu(anyList());

        directorController.manageDayMenu();

        verify(directorController, times(1)).addOrDeleteDishOnTheMenu(anyList());
    }

    @Test
    void testAddOrDeleteDishOnTheMenuWithReturn() {
        doReturn(0).when(directorController).getIntegerInput(anyInt(), anyInt());

        List<DishEntity> dishs = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            dishs.add(easyRandom.nextObject(DishEntity.class));
        }

        directorController.addOrDeleteDishOnTheMenu(dishs);

        verify(baseService, times(0)).update(any(DishEntity.class));
    }

    @Test
    void testAddOrDeleteDishOnTheMenu() {
        doReturn(1).when(directorController).getIntegerInput(anyInt(), anyInt());

        List<DishEntity> dishs = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            dishs.add(easyRandom.nextObject(DishEntity.class));
        }

        directorController.addOrDeleteDishOnTheMenu(dishs);

        verify(baseService, times(1)).update(any(DishEntity.class));
    }

    @Test
    void testAnalysesIncomesWithReturn() {
        doReturn(0).when(directorController).getIntegerInput(anyInt(), anyInt());

        doNothing().when(directorController).launch(Role.DIRECTOR);

        directorController.analysesIncomes();

        verify(directorController, times(1)).launch(any(Role.class));
    }

    @Test
    void testAnalysesIncomes() {
        doReturn(1).when(directorController).getIntegerInput(anyInt(), anyInt());

        doNothing().when(directorController).launch(Role.DIRECTOR);
        doNothing().when(directorController).analyses(anyString(), anyInt());

        directorController.analysesIncomes();

        verify(directorController, times(1)).analyses(anyString(), anyInt());
    }

    @Test
    void testAnalysesIncomesMostFamous() {
        doReturn(4).when(directorController).getIntegerInput(anyInt(), anyInt());

        doNothing().when(directorController).launch(Role.DIRECTOR);
        doNothing().when(directorController).analysesMostFamous();

        directorController.analysesIncomes();

        verify(directorController, times(1)).analysesMostFamous();
    }

    @Test
    void testAnalyses() {
        String type = "day";
        Integer last = 4;
        String date = "2021/05/13";
        List<DateDto> dates = new ArrayList<>();
        dates.add(new DateDto("2021/05/13", "2021/05/13", "2021/05/13 (JEUDI)"));

        when(service.getDate()).thenReturn(date);
        doReturn(1).doReturn(1).when(directorController).getIntegerInput(anyInt(), anyInt());
        doReturn(dates).when(directorController).getListDate(anyString(), anyString(), anyInt());
        when(baseService.getTakingByPeriod(any(DateDto.class), any(MealType.class))).thenReturn("Recette");

        directorController.analyses(type, last);

        verify(directorView, times(1)).displayMessage(anyString());
    }

    @Test
    void testAnalysesMostFamous() {
        Map.Entry<String, Integer> mostFamous = new AbstractMap.SimpleEntry<>("Tarte à la rubarbe", 42);

        when(baseService.getMostFamousDish()).thenReturn(mostFamous);

        directorController.analysesMostFamous();

        verify(directorView, times(1)).displayMessage(any());
    }

    @Test
    void testAnalysesMostFamousWithNull() {
        when(baseService.getMostFamousDish()).thenReturn(null);

        directorController.analysesMostFamous();

        verify(directorView, times(1)).displayError();
    }

    @Test
    @DisplayName("Aucune performance dans la base de données")
    void testAnalysesPerformancesEmpty() {
        when(baseService.getWeekPerformance()).thenReturn(null);
        doNothing().when(directorView).displayMessage(anyString());
        doNothing().when(directorController).askMainMenu();

        directorController.analysesPerformances();

        verify(directorView, times(1)).displayMessage(anyString());
    }

    @Test
    @DisplayName("Affichage des performances dans la base de données")
    void testAnalysesPerformances() {
        List<PerformanceEntity> perfs = new ArrayList<>();
        perfs.add(new PerformanceEntity());
        perfs.add(new PerformanceEntity());
        perfs.add(new PerformanceEntity());

        List<Integer> ints = new ArrayList<>();
        ints.add(0);
        ints.add(1);

        when(baseService.getWeekPerformance()).thenReturn(perfs);
        when(baseService.getAllPerformance()).thenReturn(perfs);
        doReturn(ints).when(directorController).getAllPerfStats(anyList());
        doNothing().when(directorView).displayMessage(anyString());
        doNothing().when(directorController).askMainMenu();

        directorController.analysesPerformances();

        verify(directorView, times(1)).displayPerf(anyList());
        verify(directorView, times(1)).displayMessage(anyString());
    }

    @Test
    @DisplayName("Calcule les temps moyens globaux de rotation des clients et de préparation des commande")
    void testGetAllPerfStats() {
        List<PerformanceEntity> perfs = new ArrayList<>();
        PerformanceEntity perf1 = new PerformanceEntity("2021/09/30DINNER", MealType.DINNER, 20, 2, 50, 3);
        PerformanceEntity perf2 = new PerformanceEntity("2021/09/30LUNCH", MealType.LUNCH, 29, 1, 150, 5);
        perfs.add(perf1);
        perfs.add(perf2);
        List<Integer> expected = new ArrayList<>();
        expected.add(25);
        expected.add(16);

        List<Integer> results = directorController.getAllPerfStats(perfs);

        assertEquals(expected, results);
    }

    @Test
    @DisplayName("Calcule les temps moyens globaux de rotation des clients et de préparation des commande : zéro service")
    void testGetAllPerfStatsZeroService() {
        List<PerformanceEntity> perfs = new ArrayList<>();
        PerformanceEntity perf1 = new PerformanceEntity("2021/09/30DINNER", MealType.DINNER, 0, 0, 50, 3);
        PerformanceEntity perf2 = new PerformanceEntity("2021/09/30LUNCH", MealType.LUNCH, 0, 0, 150, 5);
        perfs.add(perf1);
        perfs.add(perf2);
        List<Integer> expected = new ArrayList<>();
        expected.add(25);
        expected.add(0);

        List<Integer> results = directorController.getAllPerfStats(perfs);

        assertEquals(expected, results);
    }

    @Test
    @DisplayName("Calcule les temps moyens globaux de rotation des clients et de préparation des commande : zéro préparation")
    void testGetAllPerfStatsZeroPreparation() {
        List<PerformanceEntity> perfs = new ArrayList<>();
        PerformanceEntity perf1 = new PerformanceEntity("2021/09/30DINNER", MealType.DINNER, 20, 2, 0, 0);
        PerformanceEntity perf2 = new PerformanceEntity("2021/09/30LUNCH", MealType.LUNCH, 29, 1, 0, 0);
        perfs.add(perf1);
        perfs.add(perf2);
        List<Integer> expected = new ArrayList<>();
        expected.add(0);
        expected.add(16);

        List<Integer> results = directorController.getAllPerfStats(perfs);

        assertEquals(expected, results);
    }
}
