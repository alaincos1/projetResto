package fr.ul.miage.projetResto.controller.role;

import fr.ul.miage.projetResto.appinfo.Service;
import fr.ul.miage.projetResto.constants.InfoRestaurant;
import fr.ul.miage.projetResto.constants.Role;
import fr.ul.miage.projetResto.controller.feature.LogInController;
import fr.ul.miage.projetResto.dao.service.BaseService;
import fr.ul.miage.projetResto.model.entity.ProductEntity;
import fr.ul.miage.projetResto.model.entity.UserEntity;
import fr.ul.miage.projetResto.view.feature.LogInView;
import fr.ul.miage.projetResto.view.role.*;
import lombok.AllArgsConstructor;

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
                analysesIncomes();
                break;
            case 5:
                analysesPerformances();
                break;
            case 6:
                endService();
                break;
            case 7:
                butlerController.launch(Role.Butler);
                break;
            case 8:
                serverController.launch(Role.Server);
                break;
            case 9:
                helperController.launch(Role.Helper);
                break;
            case 10:
                cookController.launch(Role.Cook);
                break;
            default:
                break;
        }
    }

    protected void manageEmployees() {
        List<String> actions = new ArrayList<>(Arrays.asList("Ajouter", "Modifier", "Supprimer", "Promouvoir"));
        directorView.displayManageEmployeesMenu(actions);
        int choixMenu = getIntegerInput(0, 4);

        if (choixMenu == 1) {
            addEmployee();
        } else if (choixMenu > 0) {
            List<UserEntity> users = baseService.getAllUser();
            users.removeIf(user -> user.getRole() == Role.Director);
            directorView.displayEmployees(actions.get((choixMenu - 1)), users);
            int selected = getIntegerInput(0, users.size() + 1);

            if (selected > 0) {
                UserEntity selectedUser = users.get(selected - 1);

                if (choixMenu == 2) {
                    updateEmployee(selectedUser);
                } else if (choixMenu == 3) {
                    deleteEmployee(selectedUser);
                } else if (choixMenu == 4) {
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

    protected void analysesIncomes() {
    }

    protected void analysesPerformances() {
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
