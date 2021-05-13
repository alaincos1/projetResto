package fr.ul.miage.projetResto.controller.role;

import fr.ul.miage.projetResto.appinfo.Service;
import fr.ul.miage.projetResto.constants.MealType;
import fr.ul.miage.projetResto.constants.Role;
import fr.ul.miage.projetResto.dao.service.BaseService;
import fr.ul.miage.projetResto.model.entity.DishEntity;
import fr.ul.miage.projetResto.model.entity.ProductEntity;
import fr.ul.miage.projetResto.model.entity.TableEntity;
import fr.ul.miage.projetResto.model.entity.UserEntity;
import fr.ul.miage.projetResto.utils.DateDto;
import fr.ul.miage.projetResto.view.role.DirectorView;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

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
    public void testManageEmployeeAddEmployee() {
        doReturn(1).when(directorController).getIntegerInput(anyInt(), anyInt());
        doNothing().when(directorController).launch(Role.Director);
        doNothing().when(directorController).addEmployee();

        directorController.manageEmployees();

        verify(directorController, times(1)).addEmployee();
    }

    @Test
    public void testManageEmployeeWithReturn() {
        doReturn(0).when(directorController).getIntegerInput(anyInt(), anyInt());
        doNothing().when(directorController).launch(Role.Director);

        directorController.manageEmployees();

        verify(directorController, times(1)).launch(any(Role.class));
    }

    @Test
    public void testManageEmployeeUpdateEmployee() {
        UserEntity user = easyRandom.nextObject(UserEntity.class);
        List<UserEntity> users = new ArrayList<>();
        users.add(user);

        doReturn(2).doReturn(1).when(directorController).getIntegerInput(anyInt(), anyInt());
        doNothing().when(directorController).launch(Role.Director);
        doNothing().when(directorController).updateEmployee(any(UserEntity.class));
        when(baseService.getAllUsers()).thenReturn(users);

        directorController.manageEmployees();

        verify(directorController, times(1)).updateEmployee(any(UserEntity.class));
    }

    @Test
    public void testManageEmployeeDeleteEmployee() {
        UserEntity user = easyRandom.nextObject(UserEntity.class);
        List<UserEntity> users = new ArrayList<>();
        users.add(user);

        doReturn(3).doReturn(1).when(directorController).getIntegerInput(anyInt(), anyInt());
        doNothing().when(directorController).launch(Role.Director);
        doNothing().when(directorController).deleteEmployee(any(UserEntity.class));
        when(baseService.getAllUsers()).thenReturn(users);

        directorController.manageEmployees();

        verify(directorController, times(1)).deleteEmployee(any(UserEntity.class));
    }

    @Test
    public void testManageEmployeePromoteEmployee() {
        UserEntity user = easyRandom.nextObject(UserEntity.class);
        List<UserEntity> users = new ArrayList<>();
        users.add(user);

        doReturn(4).doReturn(1).when(directorController).getIntegerInput(anyInt(), anyInt());
        doNothing().when(directorController).launch(Role.Director);
        doNothing().when(directorController).promoteEmployee(any(UserEntity.class));
        when(baseService.getAllUsers()).thenReturn(users);

        directorController.manageEmployees();

        verify(directorController, times(1)).promoteEmployee(any(UserEntity.class));
    }

    @Test
    public void testPromoteEmployeeToButtler() {
        UserEntity user = easyRandom.nextObject(UserEntity.class);

        doReturn(1).when(directorController).getIntegerInput(anyInt(), anyInt());

        when(baseService.promoteUser(any(UserEntity.class), any(Role.class))).thenReturn(true);

        directorController.promoteEmployee(user);

        verify(directorView, times(1)).displaySuccess();
        verify(directorController, times(0)).callAction(anyInt());
    }

    @Test
    public void testPromoteEmployeeToDirector() {
        UserEntity user = easyRandom.nextObject(UserEntity.class);

        doReturn(0).when(directorController).getIntegerInput(anyInt(), anyInt());
        doNothing().when(directorController).callAction(anyInt());

        when(baseService.promoteUser(any(UserEntity.class), any(Role.class))).thenReturn(true);

        directorController.promoteEmployee(user);

        verify(directorView, times(1)).displaySuccess();
    }

    @Test
    public void testPromoteEmployeeToWithError() {
        UserEntity user = easyRandom.nextObject(UserEntity.class);

        doReturn(1).when(directorController).getIntegerInput(anyInt(), anyInt());

        when(baseService.promoteUser(any(UserEntity.class), any(Role.class))).thenReturn(false);

        directorController.promoteEmployee(user);

        verify(directorView, times(1)).displayError();
    }

    @Test
    public void testAddEmployee() {
        doReturn("id").when(directorController).getUserIdInput();
        doReturn(0).when(directorController).getIntegerInput(anyInt(), anyInt());
        when(baseService.save(any(UserEntity.class))).thenReturn(true);

        UserEntity user = easyRandom.nextObject(UserEntity.class);

        directorController.addEmployee();

        verify(directorView, times(1)).displaySuccess();
    }

    @Test
    public void testAddEmployeeWithError() {
        doReturn("id").when(directorController).getUserIdInput();
        doReturn(0).when(directorController).getIntegerInput(anyInt(), anyInt());
        when(baseService.save(any(UserEntity.class))).thenReturn(false);

        UserEntity user = easyRandom.nextObject(UserEntity.class);

        directorController.addEmployee();

        verify(directorView, times(1)).displayError();
    }

    @Test
    public void testUpdateEmployee() {
        UserEntity user = easyRandom.nextObject(UserEntity.class);
        doReturn(0).when(directorController).getIntegerInput(anyInt(), anyInt());

        when(baseService.update(any(UserEntity.class))).thenReturn(true);

        directorController.updateEmployee(user);

        verify(directorView, times(1)).displaySuccess();
    }

    @Test
    public void testUpdateEmployeeWithError() {
        UserEntity user = easyRandom.nextObject(UserEntity.class);
        doReturn(0).when(directorController).getIntegerInput(anyInt(), anyInt());

        when(baseService.update(any(UserEntity.class))).thenReturn(false);

        directorController.updateEmployee(user);

        verify(directorView, times(1)).displayError();
    }

    @Test
    public void testDeleteEmployee() {
        UserEntity user = easyRandom.nextObject(UserEntity.class);

        when(baseService.safeDeleteUser(any(UserEntity.class))).thenReturn(true);

        directorController.deleteEmployee(user);

        verify(directorView, times(1)).displaySuccess();
    }

    @Test
    public void testDeleteEmployeeWithError() {
        UserEntity user = easyRandom.nextObject(UserEntity.class);

        when(baseService.safeDeleteUser(any(UserEntity.class))).thenReturn(false);

        directorController.deleteEmployee(user);

        verify(directorView, times(1)).displayError();
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
        doReturn(1).when(directorController).getIntegerInput(anyInt(), anyInt());
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
        doReturn(1).when(directorController).getIntegerInput(anyInt(), anyInt());
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
        doReturn(2).when(directorController).getIntegerInput(anyInt(), anyInt());
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
        doReturn(0).when(directorController).getIntegerInput(anyInt(), anyInt());
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
        doReturn(4).when(directorController).getIntegerInput(anyInt(), anyInt());

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
        doReturn(1).when(directorController).getIntegerInput(anyInt(), anyInt());

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

    @Test
    public void testManageDayMenuWithReturn() {
        doReturn(0).when(directorController).getIntegerInput(anyInt(), anyInt());

        doNothing().when(directorController).launch(Role.Director);

        directorController.manageDayMenu();

        verify(directorController, times(1)).launch(any(Role.class));
    }

    @Test
    @DisplayName("Test le cas ou la carte des menus est vide et que l'on voudrait la visualiser ou supprimer un plat")
    public void testManageDayMenuWithNoDishOnTheMenu() {
        doReturn(1).when(directorController).getIntegerInput(anyInt(), anyInt());

        when(baseService.getAllDishesOntheMenuOrdered()).thenReturn(null);
        doNothing().when(directorController).launch(any(Role.class));

        directorController.manageDayMenu();

        verify(directorView, times(1)).displayNoDishsOnTheMenu();
    }

    @Test
    @DisplayName("Test le cas ou l'on voudrait ajouter un plat à la carte mais que tout les plats sont déjà dans le menu ou qu'il n'y a pas de plats en base")
    public void testManageDayMenuWithAllDishOnTheMenu() {
        doReturn(2).when(directorController).getIntegerInput(anyInt(), anyInt());

        when(baseService.getAllDishesNotOnTheMenuOrdered()).thenReturn(null);
        doNothing().when(directorController).launch(any(Role.class));

        directorController.manageDayMenu();

        verify(directorView, times(1)).displayNoDishsNotOntheMenu();
    }

    @Test
    public void testManageDayMenuVisualize() {
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
    public void testManageDayMenuAdd() {
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
    public void testManageDayMenuDelete() {
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
    public void testAddOrDeleteDishOnTheMenuWithReturn() {
        doReturn(0).when(directorController).getIntegerInput(anyInt(), anyInt());

        List<DishEntity> dishs = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            dishs.add(easyRandom.nextObject(DishEntity.class));
        }

        directorController.addOrDeleteDishOnTheMenu(dishs);

        verify(baseService, times(0)).update(any(DishEntity.class));
    }

    @Test
    public void testAddOrDeleteDishOnTheMenu() {
        doReturn(1).when(directorController).getIntegerInput(anyInt(), anyInt());

        List<DishEntity> dishs = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            dishs.add(easyRandom.nextObject(DishEntity.class));
        }

        directorController.addOrDeleteDishOnTheMenu(dishs);

        verify(baseService, times(1)).update(any(DishEntity.class));
    }

    @Test
    public void testAnalysesIncomesWithReturn() {
        doReturn(0).when(directorController).getIntegerInput(anyInt(), anyInt());

        doNothing().when(directorController).launch(Role.Director);

        directorController.analysesIncomes();

        verify(directorController, times(1)).launch(any(Role.class));
    }

    @Test
    public void testAnalysesIncomes() {
        doReturn(1).when(directorController).getIntegerInput(anyInt(), anyInt());

        doNothing().when(directorController).launch(Role.Director);
        doNothing().when(directorController).analyses(anyString(), anyInt());

        directorController.analysesIncomes();

        verify(directorController, times(1)).analyses(anyString(), anyInt());
    }

    @Test
    public void testAnalysesIncomesMostFamous() {
        doReturn(4).when(directorController).getIntegerInput(anyInt(), anyInt());

        doNothing().when(directorController).launch(Role.Director);
        doNothing().when(directorController).analysesMostFamous();

        directorController.analysesIncomes();

        verify(directorController, times(1)).analysesMostFamous();
    }

    @Test
    public void testAnalyses() {
        String type = "day";
        Integer last = 4;
        String date = "2021/05/13";
        List<DateDto> dates = new ArrayList<>(Arrays.asList(new DateDto("2021/05/13", "2021/05/13", "2021/05/13 (JEUDI)")));

        when(service.getDate()).thenReturn(date);
        doReturn(1).doReturn(1).when(directorController).getIntegerInput(anyInt(), anyInt());
        doReturn(dates).when(directorController).getListDate(anyString(), anyString(), anyInt());
        when(baseService.getTakingByPeriod(any(DateDto.class), any(MealType.class))).thenReturn("Recette");

        directorController.analyses(type, last);

        verify(directorView, times(1)).simpleDisplay(anyString());
    }

    @Test
    public void testAnalysesMostFamous() {
        Map.Entry<String, Integer> mostFamous = new AbstractMap.SimpleEntry<>("Tarte à la rubarbe", 42);

        when(baseService.getMostFamousDish()).thenReturn(mostFamous);

        directorController.analysesMostFamous();

        verify(directorView, times(1)).displayMostFamous(any(Map.Entry.class));
    }

    @Test
    public void testAnalysesMostFamousWithNull() {
        when(baseService.getMostFamousDish()).thenReturn(null);

        directorController.analysesMostFamous();

        verify(directorView, times(1)).displayError();
    }
}
