package fr.ul.miage.resto.controller.role;

import fr.ul.miage.resto.appinfo.Service;
import fr.ul.miage.resto.constants.InfoRestaurant;
import fr.ul.miage.resto.constants.MealType;
import fr.ul.miage.resto.constants.Role;
import fr.ul.miage.resto.constants.TableState;
import fr.ul.miage.resto.dao.service.BaseService;
import fr.ul.miage.resto.model.entity.*;
import fr.ul.miage.resto.utils.DateDto;
import fr.ul.miage.resto.view.role.*;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
public class DirectorController extends RoleController {
    private final BaseService baseService;
    private final Service service;
    private final DirectorView directorView;

    @Override
    public void callAction(Integer action) {
        RoleController controller;
        switch (action) {
            case 0:
                goBackOrDisconnect(Role.DIRECTOR, baseService, service, true);
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
                controller = new ButlerController(baseService, service, new ButlerView());
                launchController(controller, Role.BUTLER);
                break;
            case 9:
                controller = new ServerController(baseService, service, new ServerView());
                launchController(controller, Role.SERVER);
                break;
            case 10:
                controller = new HelperController(baseService, service, new HelperView());
                launchController(controller, Role.HELPER);
                break;
            case 11:
                controller = new CookController(baseService, service, new CookView());
                launchController(controller, Role.COOK);
                break;
            default:
                break;
        }
    }

    protected void launchController(RoleController controller, Role role) {
        controller.launch(role);
    }

    protected void manageEmployees() {
        List<String> actions = new ArrayList<>(Arrays.asList("Ajouter", "Modifier", "Supprimer", "Promouvoir"));
        directorView.displayManage(actions, "un employé");
        int menuChoice = getIntegerInput(0, 4);

        if (menuChoice == 1) {
            addEmployee();
        } else if (menuChoice > 0) {
            List<UserEntity> users = baseService.getAllUsers();
            users.removeIf(user -> user.getRole() == Role.DIRECTOR);
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
        launch(Role.DIRECTOR);
    }

    protected void promoteEmployee(UserEntity user) {
        directorView.displayMessage("Selectionnez le role de l'employé." +
                "\n 0) " + Role.DIRECTOR +
                "\n 1) " + Role.BUTLER);
        int choice = getIntegerInput(0, 1);
        Role newRole = choice == 0 ? Role.DIRECTOR : Role.BUTLER;

        if (baseService.promoteUser(user, newRole)) {
            directorView.displaySuccess();

            if (newRole == Role.DIRECTOR) {
                callAction(0);
            }
        } else {
            directorView.displayError();
        }
    }

    protected void addEmployee() {
        directorView.displayMessage("Renseignez l'identifiant de l'employé.");
        String id = getUserIdInput();

        directorView.displayRoleChoice();
        Integer input = getIntegerInput(0, Role.values().length - 1);

        UserEntity userEntity = new UserEntity();
        userEntity.setId(id);
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
            List<DishEntity> dishs;
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
                    directorView.displayMessage("Il n'y a aucuns plats d'enregistrés ou ils sont déjà tous dans le menu");
                } else {
                    directorView.displayMessage("Il n'y a pas de plats dans le menu");
                }
            }
        }

        launch(Role.DIRECTOR);
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
        launch(Role.DIRECTOR);
    }

    //Créé un nouveau produit et son stock
    protected ProductEntity createProduct() {
        ProductEntity product = new ProductEntity();
        directorView.displayMessage("Entrez le nom du produit.");
        String name = getStringInput();
        while (baseService.getProductById(name) != null) {
            directorView.displayMessage("Ce produit existe déjà, entrez un autre intitulé.");
            name = getStringInput();
        }
        product.setId(name);
        directorView.displayAskAddStock(0, InfoRestaurant.MAX_STOCK.getValue());
        product.setStock(getIntegerInput(0, InfoRestaurant.MAX_STOCK.getValue()));
        return product;
    }

    //Ajoute du stock à un produit existant
    protected ProductEntity addStockProduct(List<ProductEntity> products, Integer input) {
        ProductEntity product = products.get(input);
        while (product.getStock() == InfoRestaurant.MAX_STOCK.getValue()) {
            directorView.displayMessage("Stock maximal pour ce produit. Entrez un autre produit.");
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
            directorView.displayMessage("Le produit et son stock sont sauvegardés.");
        } else {
            directorView.displayError();
        }
        directorView.displayMessage("Souhaitez vous ajouter du stock à un autre produit ?" +
                "\n O) Non" +
                "\n 1) Oui");
        if (doAgain()) {
            manageStocks();
        }
    }

    //gère les tables du restaurant
    protected void manageTables() {
        List<TableEntity> tables = baseService.getAllTables();
        Integer i;
        if (tables.isEmpty()) {
            directorView.displayMessage("Il n'y a aucune table.");
            directorView.displayMessage("Souhaitez vous ajouter une table ?" +
                    "\n O) Annuler" +
                    "\n 1) Ajouter");
            i = getIntegerInput(0, 1);
        } else {
            directorView.displayTables(tables);
            directorView.displayMessage("Souhaitez vous ajouter ou supprimer une table ?" +
                    "\n O) Annuler" +
                    "\n 1) Ajouter" +
                    "\n 2) Supprimer");
            i = getIntegerInput(0, 2);
        }
        if (i == 1 && tables.size() == InfoRestaurant.MAX_TABLES.getValue()) {
            directorView.displayMessage("Il y a déjà le maximum de tables possible: " + InfoRestaurant.MAX_TABLES.getValue());
        } else if (i == 1) {
            addTable(tables);
        } else if (i == 2) {
            removeTable();
        }
        launch(Role.DIRECTOR);
    }

    //ajoute une table
    protected void addTable(List<TableEntity> tables) {
        TableEntity tableToAdd = new TableEntity();
        tableToAdd.setId(getFreeNumberTable(tables));
        tableToAdd.setTableState(TableState.FREE);
        directorView.displayMessage("Entrez un nombre de place pour cette table entre 1 et " + InfoRestaurant.MAX_SEATS.getValue() + " ");
        tableToAdd.setNbSeats(getIntegerInput(1, InfoRestaurant.MAX_SEATS.getValue()));
        if (baseService.save(tableToAdd)) {
            directorView.displayMessage("Table ajoutée !");
        } else {
            directorView.displayError();
        }
    }

    //supprime une table
    protected void removeTable() {
        List<TableEntity> tables = baseService.getAllRemovableTables();
        if (tables.isEmpty()) {
            directorView.displayMessage("Aucune table est supprimable (Clients attablés, réservations, etc...)");
        } else {
            directorView.displayMessage("Quelle table souhaitez vous supprimer ?");
            directorView.displayTables(tables);
            int choice = getIntegerInput(1, tables.size()) - 1;
            if (baseService.deleteTable(tables.get(choice).getId())) {
                directorView.displayMessage("Table supprimée !");
            } else {
                directorView.displayError();
            }
        }
    }

    protected String getFreeNumberTable(List<TableEntity> tables) {
        List<Integer> tableId = new ArrayList<>();
        tables.forEach(tableEntity -> tableId.add(Integer.parseInt(tableEntity.getId())));
        int i = 1;
        while (tableId.contains(i) && i <= InfoRestaurant.MAX_TABLES.getValue()) {
            i++;
        }
        return "" + i;
    }

    protected void analysesIncomes() {
        List<String> actions = new ArrayList<>(Arrays.asList("Recettes quotidiennes", "Recettes hebdomadaires", "Recettes mensuelles", "Plat le plus populaire"));
        directorView.displayManage(actions);
        int choice = getIntegerInput(0, actions.size());

        if (choice > 0) {
            if (choice == 1)
                analyses("day", 7);
            else if (choice == 2)
                analyses("week", 4);
            else if (choice == 3)
                analyses("month", 6);
            else
                analysesMostFamous();
        }

        launch(Role.DIRECTOR);
    }

    protected void analyses(String type, Integer last) {
        int choice = 1;
        if (StringUtils.equalsIgnoreCase(type, "day")) {
            List<String> actions = Arrays.stream(MealType.values())
                    .map(MealType::getMealValue)
                    .collect(Collectors.toList());
            directorView.displayManage(actions);
            choice = getIntegerInput(0, actions.size());
        }

        if (choice > 0) {
            MealType mealType = null;
            if (StringUtils.equalsIgnoreCase(type, "day")) {
                mealType = MealType.values()[choice - 1];
            }

            List<DateDto> dates = getListDate(type, service.getDate(), last);

            if (CollectionUtils.isNotEmpty(dates)) {
                List<String> toDisplay = dates.stream()
                        .map(DateDto::getDisplayDate)
                        .collect(Collectors.toList());

                directorView.displayManage(toDisplay);
                int choiceDate = getIntegerInput(0, toDisplay.size());

                if (choiceDate > 0) {
                    DateDto dateDto = dates.get(choiceDate - 1);
                    directorView.displayMessage(baseService.getTakingByPeriod(dateDto, mealType));
                }
            }
        }
    }

    protected void analysesMostFamous() {
        Map.Entry<String, Integer> mostFamous = baseService.getMostFamousDish();
        if (mostFamous != null) {
            directorView.displayMessage(mostFamous.getKey() + ": " + mostFamous.getValue());
        } else {
            directorView.displayError();
        }
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
            directorView.displayMessage("Souhaitez vous annoncer la fin du service (des prises des commandes) ? Choix définitif." +
                    "\n O) Non" +
                    "\n 1) Oui");
            Integer input = getIntegerInput(0, 1);
            if (input == 1) {
                service.setEndNewClients(true);
                service.setEndService(true);
                directorView.displayMessage("La fin du service (des prises des commandes) est annoncée.");
            }
        } else {
            directorView.displayMessage("La fin du service (des prises des commandes) a déjà été annoncée.");
        }
        launch(Role.DIRECTOR);
    }
}
