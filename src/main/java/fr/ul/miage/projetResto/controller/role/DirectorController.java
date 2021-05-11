package fr.ul.miage.projetResto.controller.role;

import fr.ul.miage.projetResto.appinfo.Service;
import fr.ul.miage.projetResto.constants.InfoRestaurant;
import fr.ul.miage.projetResto.constants.Role;
import fr.ul.miage.projetResto.constants.TableState;
import fr.ul.miage.projetResto.controller.feature.LogInController;
import fr.ul.miage.projetResto.dao.service.BaseService;
import fr.ul.miage.projetResto.model.entity.*;
import fr.ul.miage.projetResto.view.feature.LogInView;
import fr.ul.miage.projetResto.view.role.*;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
public class DirectorController extends RoleMenuController {
    private final BaseService baseService;
    private final Service service;
    private final DirectorView directorView;

    @Override
    public void callAction(Integer action) {
        ButlerController butlerController = new ButlerController(baseService, service, new ButlerView());
        ServerController serverController = new ServerController(baseService, service, new ServerView());
        HelperController helperController = new HelperController(baseService, service, new HelperView());
        CookController cookController = new CookController(baseService, service, new CookView());
        switch (action) {
            case 0:
                LogInController logInController = new LogInController(baseService, service, new LogInView());
                logInController.disconnect();
                break;
            case 1:
                manageEmployees();
                break;
            case 2:
                manageDayMenu();
                break;
            case 3:
                manageStocks();
                break;
            case 4:
                manageTables();
                break;
            case 5:
                analysesIncomes();
                break;
            case 6:
                analysesPerformances();
                break;
            case 7:
                endService();
                break;
            case 8:
                butlerController.launch(Role.Butler);
                break;
            case 9:
                serverController.launch(Role.Server);
                break;
            case 10:
                helperController.launch(Role.Helper);
                break;
            case 11:
                cookController.launch(Role.Cook);
                break;
            default:
                break;
        }
    }

    protected void manageEmployees() {
        List<String> actions = new ArrayList<>(Arrays.asList("Ajouter", "Modifier", "Supprimer", "Promouvoir"));
        directorView.displayManage(actions, "un employé");
        int menuChoice = getIntegerInput(0, 4);

        if (menuChoice == 1) {
            addEmployee();
        } else if (menuChoice > 0) {
            List<UserEntity> users = baseService.getAllUsers();
            users.removeIf(user -> user.getRole() == Role.Director);
            directorView.displayEmployees(actions.get((menuChoice - 1)), users);
            int selected = getIntegerInput(0, users.size() + 1);

            if (selected > 0) {
                UserEntity selectedUser = users.get(selected - 1);

                if (menuChoice == 2) {
                    updateEmployee(selectedUser);
                } else if (menuChoice == 3) {
                    deleteEmployee(selectedUser);
                } else if (menuChoice == 4) {
                    promoteEmployee(selectedUser);
                }
            }
        }
        launch(Role.Director);
    }

    protected void promoteEmployee(UserEntity user) {
        directorView.displayDirectionRole();
        int choice = getIntegerInput(0, 1);
        Role newRole = choice == 0 ? Role.Director : Role.Butler;

        if (baseService.promoteUser(user, newRole)) {
            directorView.displaySuccess();

            if (newRole == Role.Director) {
                callAction(0);
            }
        } else {
            directorView.displayError();
        }
    }

    protected void addEmployee() {
        directorView.displayIdChoice();
        String id = getUserIdInput();

        directorView.displayRoleChoice();
        Integer input = getIntegerInput(0, Role.values().length - 1);

        UserEntity userEntity = new UserEntity();
        userEntity.set_id(id);
        userEntity.setRole(Role.values()[input]);

        if (baseService.save(userEntity)) {
            directorView.displaySuccess();
        } else {
            directorView.displayError();
        }
    }

    protected void updateEmployee(UserEntity user) {
        directorView.displayRoleChoice();
        Integer input = getIntegerInput(0, Role.values().length - 1);

        Role newRole = Role.values()[input];

        user.setRole(newRole);

        if (baseService.update(user)) {
            directorView.displaySuccess();
        } else {
            directorView.displayError();
        }
    }

    protected void deleteEmployee(UserEntity user) {
        if (baseService.safeDeleteUser(user)) {
            directorView.displaySuccess();
        } else {
            directorView.displayError();
        }
    }

    protected void manageDayMenu() {
        List<String> actions = new ArrayList<>(Arrays.asList("Visualiser un plat de", "Ajouter un plat à", "Supprimer un plat de"));
        directorView.displayManage(actions, "la carte des menus");
        int choice = getIntegerInput(0, 3);

        if (choice > 0) {
            List<DishEntity> dishs = null;
            if (choice == 2) {
                dishs = baseService.getAllDishesNotOnTheMenuOrdered();
            } else {
                dishs = baseService.getAllDishesOntheMenuOrdered();
            }

            if (CollectionUtils.isNotEmpty(dishs)) {
                if (choice == 1) {
                    directorView.displayDishChoice(dishs, false);
                } else {
                    addOrDeleteDishOnTheMenu(dishs);
                }
            } else {
                if (choice == 2) {
                    directorView.displayNoDishsNotOntheMenu();
                } else {
                    directorView.displayNoDishsOnTheMenu();
                }
            }
        }

        launch(Role.Director);
    }

    protected void addOrDeleteDishOnTheMenu(List<DishEntity> dishs) {
        directorView.displayDishChoice(dishs, true);
        int dishChoice = getIntegerInput(0, dishs.size());

        if (dishChoice > 0) {
            DishEntity selectedDish = dishs.get(dishChoice - 1);
            selectedDish.setOnTheMenu(!selectedDish.isOnTheMenu());
            baseService.update(selectedDish);
        }
    }

    //permet de voir les stocks de tous les produits du restaurant
    protected void manageStocks() {
        List<ProductEntity> products = baseService.getAllProducts();
        directorView.displayStock(products);
        ProductEntity product;
        int input = getIntegerInput(0, products.size() + 2) - 2;
        if (input == -1) {
            product = createProduct();
            saveProduct(product);
        } else if (input >= 0) {
            product = addStockProduct(products, input);
            saveProduct(product);
        }
        launch(Role.Director);
    }

    //Créé un nouveau produit et son stock
    protected ProductEntity createProduct() {
        ProductEntity product = new ProductEntity();
        directorView.displayAskNameProduct();
        String name = getStringInput();
        while (baseService.getProductById(name) != null) {
            directorView.displayProductAlreadyExist();
            name = getStringInput();
        }
        product.set_id(name);
        directorView.displayAskAddStock(0, InfoRestaurant.MAX_STOCK.getValue());
        product.setStock(getIntegerInput(0, InfoRestaurant.MAX_STOCK.getValue()));
        return product;
    }

    //Ajoute du stock à un produit existant
    protected ProductEntity addStockProduct(List<ProductEntity> products, Integer input) {
        ProductEntity product = products.get(input);
        while (product.getStock() == InfoRestaurant.MAX_STOCK.getValue()) {
            directorView.displayStockMax();
            input = getIntegerInput(1, products.size()) - 1;
            product = products.get(input);
        }
        directorView.displayAskAddStock(1, InfoRestaurant.MAX_STOCK.getValue() - product.getStock());
        int addStock = getIntegerInput(1, InfoRestaurant.MAX_STOCK.getValue() - product.getStock());
        products.get(input).setStock(product.getStock() + addStock);

        return product;
    }

    //Sauvegarde le produit mis à jour ou créé
    protected void saveProduct(ProductEntity product) {
        if (baseService.update(product) || baseService.save(product)) {
            directorView.displayProductSave();
        } else {
            directorView.displayError();
        }
        directorView.displayManageStockAgain();
        if (doAgain()) {
            manageStocks();
        }
    }

    //gère les tables du restaurant
    protected void manageTables() {
        List<TableEntity> tables = baseService.getAllTables();
        Integer i;
        if (tables.isEmpty()) {
            directorView.displayNoTables();
            directorView.displayAskAddTable();
            i = getIntegerInput(0, 1);
        } else {
            directorView.displayTables(tables);
            directorView.displayAskAddRemoveTable();
            i = getIntegerInput(0, 2);
        }
        if (i == 1 && tables.size() == InfoRestaurant.MAX_TABLES.getValue()) {
            directorView.displayEnoughTables();
        } else if (i == 1) {
            addTable(tables);
        } else if (i == 2) {
            removeTable();
        }
        launch(Role.Director);
    }

    //ajoute une table
    protected void addTable(List<TableEntity> tables) {
        TableEntity tableToAdd = new TableEntity();
        tableToAdd.set_id(getFreeNumberTable(tables));
        tableToAdd.setTableState(TableState.Free);
        directorView.displayAskNumberSeats();
        tableToAdd.setNbSeats(getIntegerInput(1, InfoRestaurant.MAX_SEATS.getValue()));
        if (baseService.save(tableToAdd)) {
            directorView.displayTableAdded();
        } else {
            directorView.displayError();
        }
    }

    //supprime une table
    protected void removeTable() {
        List<TableEntity> tables = baseService.getAllRemovableTables();
        if (tables.isEmpty()) {
            directorView.displayNoTableCanBeRemoved();
        } else {
            directorView.displayAskTableToRemove();
            directorView.displayTables(tables);
            int choice = getIntegerInput(1, tables.size()) - 1;
            if (baseService.deleteTable(tables.get(choice).get_id())) {
                directorView.displayTableRemoved();
            } else {
                directorView.displayError();
            }
        }
    }

    //trouve le premier numéro de table libre de 1 à 100;
    protected String getFreeNumberTable(List<TableEntity> tables) {
        List<Integer> tableId = new ArrayList<>();
        tables.forEach(tableEntity -> tableId.add(Integer.parseInt(tableEntity.get_id())));
        int i = 1;
        while (tableId.contains(i) && i <= InfoRestaurant.MAX_TABLES.getValue()) {
            i++;
        }
        return "" + i;
    }

    protected void analysesIncomes() {
    }

    protected void analysesPerformances() {
        List<PerformanceEntity> perf = baseService.getWeekPerformance();
        if (CollectionUtils.isEmpty(perf)) {
            directorView.displayMessage("Aucune performance enregistrée");
        } else {
            directorView.displayPerf(perf);

            List<PerformanceEntity> allPerf = baseService.getAllPerformance();
            List<Integer> results = getAllPerfStats(allPerf);
            directorView.displayMessage("\n --- Performances globales ---" +
                    "\n  - Préparation des commandes : " + results.get(0) + " min/commande" +
                    "\n  - Rotation des clients : " + results.get(1) + " min/table");
        }
        askMainMenu();
    }

    protected List<Integer> getAllPerfStats(List<PerformanceEntity> allPerf) {
        List<Integer> results = new ArrayList<>();
        Integer totalServiceTime = 0;
        Integer totalPreparationTime = 0;
        Integer totalTableServed = 0;
        Integer totalOrder = 0;

        for (PerformanceEntity perf : allPerf) {
            totalServiceTime += perf.getServiceTime();
            totalPreparationTime += perf.getPreparationTime();
            totalTableServed += perf.getNbTableServed();
            totalOrder += perf.getNbOrder();
        }
        results.add((totalOrder == 0) ? 0 : totalPreparationTime / totalOrder);
        results.add((totalTableServed == 0) ? 0 : totalServiceTime / totalTableServed);
        return results;
    }

    //Met fin à la prise des commandes, fin de service
    protected void endService() {
        if (!service.isEndService()) {
            directorView.displayAskEndService();
            Integer input = getIntegerInput(0, 1);
            if (input == 1) {
                service.setEndNewClients(true);
                service.setEndService(true);
                directorView.displayEnded();
            }
        } else {
            directorView.displayAlreadyEnded();
        }
        launch(Role.Director);
    }
}
