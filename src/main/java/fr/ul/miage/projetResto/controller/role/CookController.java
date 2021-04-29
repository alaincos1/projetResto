package fr.ul.miage.projetResto.controller.role;

import fr.ul.miage.projetResto.Launcher;
import fr.ul.miage.projetResto.constants.DishType;
import fr.ul.miage.projetResto.constants.InfoRestaurant;
import fr.ul.miage.projetResto.constants.OrderState;
import fr.ul.miage.projetResto.constants.Role;
import fr.ul.miage.projetResto.controller.feature.LogInController;
import fr.ul.miage.projetResto.model.entity.CategoryEntity;
import fr.ul.miage.projetResto.model.entity.DishEntity;
import fr.ul.miage.projetResto.model.entity.OrderEntity;
import fr.ul.miage.projetResto.model.entity.ProductEntity;
import fr.ul.miage.projetResto.utils.InputUtil;
import fr.ul.miage.projetResto.view.role.CookView;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class CookController extends RoleMenuController {
    CookView cookView = new CookView();

    @Override
    public void callAction(Integer action) {
        Role role = Launcher.getLoggedUser().getRole();
        switch (action) {
            case 0:
                if (role.equals(Role.Director)) {
                    DirectorController directorController = new DirectorController();
                    directorController.launch(Role.Director);
                } else {
                    LogInController logInController = new LogInController();
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

    protected void viewOrdersList() {
        List<OrderEntity> orders = Launcher.getBaseService().getNotPreparedOrders();
        if (orders.isEmpty()) {
            cookView.displayNoOrderToPrepare();
            launch(Role.Cook);
        } else {
            cookView.displayOrdersList(orders);
            askMainMenu();
        }
    }

    protected void setOrderReady() {
        List<OrderEntity> orders = Launcher.getBaseService().getNotPreparedOrders();
        if (!orders.isEmpty()) {
            cookView.displayOrdersList(orders);
            cookView.displayWhichOrderPrepared();
            Integer input = InputUtil.getIntegerInput(0, orders.size()) - 1;
            if (input != -1) {
                orders.get(input).setOrderState(OrderState.Prepared);
                Launcher.getBaseService().update(orders.get(input));
                cookView.displayOrderPrepared(orders.get(input).get_id(), orders.get(input).getIdTable());
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
        String name = InputUtil.getStringInput();
        if (Launcher.getBaseService().getDishById(name) != null) {
            cookView.displayModifyOrCancel();
            Integer choice = InputUtil.getIntegerInput(0, 1);
            if (choice == 0) {
                launch(Role.Cook);
                return;
            }
            modify = true;
        }
        newDish.set_id(name);
        newDish.setPrice(getPriceDish());
        newDish.setDishType(getDishType());
        newDish.setIdCategory(getDishCategory());
        newDish.setIdsProduct(getDishProducts());
        newDish.setOnTheMenu(false);

        cookView.displayDish(newDish);
        if (InputUtil.getIntegerInput(0, 1) == 1) {
            saveDish(newDish, modify);
        } else {
            cookView.displaySaveOrNot(false);
        }
        launch(Role.Cook);
    }

    private Integer getPriceDish() {
        cookView.displayAskInput("le prix", "<=" + InfoRestaurant.MAX_PRICE.getValue() + " €");
        return InputUtil.getIntegerInput(1, InfoRestaurant.MAX_PRICE.getValue());
    }

    private DishType getDishType() {
        cookView.displayAskInput("le type de plat", StringUtils.EMPTY);
        cookView.displayDishType();
        Integer input = InputUtil.getIntegerInput(1, DishType.values().length);
        return DishType.values()[input - 1];
    }

    private String getDishCategory() {
        cookView.displayAskInput("la catégorie du plat", StringUtils.EMPTY);
        List<CategoryEntity> categories = Launcher.getBaseService().getAllCategoriesAsList();
        cookView.displayCategories(categories);
        Integer input = InputUtil.getIntegerInput(0, categories.size());
        if (input == 0) {
            cookView.displayAskInput("la nouvelle catégorie du plat", "moins de " + InfoRestaurant.MAX_LENGTH_NAME.getValue() + " caractères");
            return InputUtil.getStringInput();
        } else {
            return categories.get(input - 1).get_id();
        }
    }

    private List<String> getDishProducts() {
        List<ProductEntity> productEntities = Launcher.getBaseService().getAllProductsAsList();
        cookView.displayAskInput("les produits du plat", "ex : 1/6/8, maximum " + InfoRestaurant.MAX_CHOICES.getValue() + " produits");
        cookView.displayProducts(productEntities);
        List<ProductEntity> selection = parseToSelectedProducts(productEntities, InputUtil.getStringMultipleChoices(1, productEntities.size()));
        cookView.displayProducts(selection);
        cookView.displayProductsOkOrNot();
        if (InputUtil.getIntegerInput(0, 1) == 0) {
            return getDishProducts();
        } else {
            return parseToIdProducts(selection);
        }
    }

    private List<String> parseToIdProducts(List<ProductEntity> selection) {
        List<String> listId = new ArrayList<>();
        for (ProductEntity product : selection) {
            listId.add(product.get_id());
        }
        return listId;
    }

    private List<ProductEntity> parseToSelectedProducts(List<ProductEntity> productEntities, String stringMultipleChoices) {
        String[] selection = stringMultipleChoices.replace(" ", "").split("/");
        List<ProductEntity> selectedProducts = new ArrayList<>();
        int i = 0;
        for (String sel : selection) {
            if (selectedProducts.stream().noneMatch(x -> x.equals(productEntities.get(Integer.parseInt(sel) - 1)))){
                selectedProducts.add(productEntities.get(Integer.parseInt(sel) - 1));
            }
            i++;
        }
        return selectedProducts;
    }

    private void saveDish(DishEntity newDish, boolean modify) {
        boolean done1 = true;
        boolean done2 = true;
        if (modify) {
            if (!Launcher.getBaseService().update(newDish)) {
                done1 = false;
            }
        } else {
            if (!Launcher.getBaseService().save(newDish)) {
                done1 = false;
            }
        }

        if (Launcher.getBaseService().getCategoryById(newDish.getIdCategory()) == null) {
            CategoryEntity cat = new CategoryEntity();
            cat.set_id(newDish.getIdCategory());
            if (!Launcher.getBaseService().save(cat)) {
                done2 = false;
            }
        }
        cookView.displaySaveOrNot(done1 && done2);
    }

    protected void endCooking() {
        if (!Launcher.getService().isEndNewClients()) {
            cookView.displayAskEndCooking();
            Integer input = InputUtil.getIntegerInput(0, 1);
            if (input == 1) {
                Launcher.getService().setEndNewClients(true);
                cookView.displayEnded();
            }
        } else {
            cookView.displayAlreadyEnded();
        }
        launch(Role.Cook);
    }
}
