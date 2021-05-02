package fr.ul.miage.projetResto.controller.role;

import fr.ul.miage.projetResto.Launcher;
import fr.ul.miage.projetResto.appinfo.Service;
import fr.ul.miage.projetResto.constants.DishType;
import fr.ul.miage.projetResto.constants.InfoRestaurant;
import fr.ul.miage.projetResto.constants.OrderState;
import fr.ul.miage.projetResto.constants.Role;
import fr.ul.miage.projetResto.controller.feature.LogInController;
import fr.ul.miage.projetResto.dao.service.BaseService;
import fr.ul.miage.projetResto.model.entity.CategoryEntity;
import fr.ul.miage.projetResto.model.entity.DishEntity;
import fr.ul.miage.projetResto.model.entity.OrderEntity;
import fr.ul.miage.projetResto.model.entity.ProductEntity;
import fr.ul.miage.projetResto.view.feature.LogInView;
import fr.ul.miage.projetResto.view.role.CookView;
import fr.ul.miage.projetResto.view.role.DirectorView;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class CookController extends RoleMenuController {
    private final BaseService baseService;
    private final Service service;
    private final CookView cookView;

    @Override
    public void callAction(Integer action) {
        Role role = Launcher.getLoggedUser().getRole();
        switch (action) {
            case 0:
                if (role.equals(Role.Director)) {
                    DirectorController directorController = new DirectorController(baseService, service, new DirectorView());
                    directorController.launch(Role.Director);
                } else {
                    LogInController logInController = new LogInController(baseService, service, new LogInView());
                    logInController.disconnect();
                }
                break;
            case 1:
                viewOrdersList();
                break;
            case 2:
                setOrderReady();
                break;
            case 3:
                createDish();
                break;
            case 4:
                endCooking();
                break;
            default:
                break;
        }
    }

    public void viewOrdersList() {
        List<OrderEntity> orders = baseService.getNotPreparedOrders();
        if (orders.isEmpty()) {
            cookView.displayNoOrderToPrepare();
            launch(Role.Cook);
        } else {
            cookView.displayOrdersList(orders);
            askMainMenu();
        }
    }

    public void setOrderReady() {
        List<OrderEntity> orders = baseService.getNotPreparedOrders();
        if (!orders.isEmpty()) {
            cookView.displayOrdersList(orders);
            cookView.displayWhichOrderPrepared();
            int input = getIntegerInput(0, orders.size()) - 1;
            if (input != -1) {
                orders.get(input).setOrderState(OrderState.Prepared);
                baseService.update(orders.get(input));
                cookView.displayOrderPrepared(orders.get(input).getIdTable());
                if (doAgain()) {
                    setOrderReady();
                }
            }
        } else {
            cookView.displayNoOrderToPrepare();
        }
        launch(Role.Cook);
    }

    protected void createDish() {
        DishEntity newDish = new DishEntity();
        boolean modify = false;
        cookView.displayAskInput("le nom du plat", "moins de " + InfoRestaurant.MAX_LENGTH_NAME.getValue() + " caractères");
        String name = getStringInput();
        if (baseService.getDishById(name) != null) {
            cookView.displayModifyOrCancel();
            Integer choice = getIntegerInput(0, 1);
            if (choice == 0) {
                launch(Role.Cook);
                return;
            }
            modify = true;
        }

        newDish.set_id(name);
        newDish.setPrice(getPriceDish());
        newDish.setDishType(getDishType());
        newDish.setIdCategory(getDishCategory(newDish.getDishType()));
        newDish.setIdsProduct(getDishProducts());
        newDish.setOnTheMenu(false);

        cookView.displayDish(newDish);
        if (getIntegerInput(0, 1) == 1) {
            saveDish(newDish, modify);
        } else {
            cookView.displaySaveOrNot(false);
        }
        launch(Role.Cook);
    }

    protected Integer getPriceDish() {
        cookView.displayAskInput("le prix", "<=" + InfoRestaurant.MAX_PRICE.getValue() + " €");
        return getIntegerInput(1, InfoRestaurant.MAX_PRICE.getValue());
    }

    protected DishType getDishType() {
        cookView.displayAskInput("le type de plat", StringUtils.EMPTY);
        cookView.displayDishType();
        Integer input = getIntegerInput(1, DishType.values().length);
        return DishType.values()[input - 1];
    }

    protected String getDishCategory(DishType dishType) {
        cookView.displayAskInput("la catégorie du plat", StringUtils.EMPTY);
        List<CategoryEntity> categories = baseService.getCategoriesWithDishTypeAsList(dishType);
        cookView.displayCategories(categories);
        Integer input = getIntegerInput(0, categories.size());
        if (input == 0) {
            cookView.displayAskInput("la nouvelle catégorie du plat", "moins de " + InfoRestaurant.MAX_LENGTH_NAME.getValue() + " caractères");
            String newCat = getStringInput();
            while (baseService.getCategoryById(newCat) != null) {
                cookView.displayAskInput("une catégorie de plat qui n'est pas déjà définie dans un autre type de plat que " + dishType.getDish(), "moins de " + InfoRestaurant.MAX_LENGTH_NAME.getValue() + " caractères");
                newCat = getStringInput();
            }
            return newCat;
        } else {
            return categories.get(input - 1).get_id();
        }
    }

    protected List<String> getDishProducts() {
        List<ProductEntity> productEntities = baseService.getAllProductsAsList();
        cookView.displayAskInput("les produits du plat", "ex : 1/6/8, maximum " + InfoRestaurant.MAX_CHOICES.getValue() + " produits");
        cookView.displayProducts(productEntities);
        List<ProductEntity> selection = parseToSelectedProducts(productEntities, getStringMultipleChoices(1, productEntities.size()));
        cookView.displayProducts(selection);
        cookView.displayProductsOkOrNot();
        if (getIntegerInput(0, 1) == 0) {
            return getDishProducts();
        } else {
            return parseToIdProducts(selection);
        }
    }

    protected List<String> parseToIdProducts(List<ProductEntity> selection) {
        List<String> listId = new ArrayList<>();
        for (ProductEntity product : selection) {
            listId.add(product.get_id());
        }
        return listId;
    }

    protected List<ProductEntity> parseToSelectedProducts(List<ProductEntity> productEntities, String stringMultipleChoices) {
        String[] selection = stringMultipleChoices.replace(" ", "").split("/");
        List<ProductEntity> selectedProducts = new ArrayList<>();
        for (String sel : selection) {
            if (selectedProducts.stream().noneMatch(x -> x.equals(productEntities.get(Integer.parseInt(sel) - 1)))) {
                selectedProducts.add(productEntities.get(Integer.parseInt(sel) - 1));
            }
        }
        return selectedProducts;
    }

    protected void saveDish(DishEntity newDish, boolean modify) {
        boolean done1 = true;
        boolean done2 = true;
        if (modify) {
            if (!baseService.update(newDish)) {
                done1 = false;
            }
        } else {
            if (!baseService.save(newDish)) {
                done1 = false;
            }
        }

        if (baseService.getCategoryById(newDish.getIdCategory()) == null) {
            CategoryEntity cat = new CategoryEntity();
            cat.set_id(newDish.getIdCategory());
            cat.setDishType(newDish.getDishType());
            if (!baseService.save(cat)) {
                done2 = false;
            }
        }
        cookView.displaySaveOrNot(done1 && done2);
    }

    protected void endCooking() {
        if (!service.isEndNewClients()) {
            cookView.displayAskEndCooking();
            Integer input = getIntegerInput(0, 1);
            if (input == 1) {
                service.setEndNewClients(true);
                cookView.displayEnded();
            }
        } else {
            cookView.displayAlreadyEnded();
        }
        launch(Role.Cook);
    }
}
