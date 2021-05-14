package fr.ul.miage.resto.controller.role;

import fr.ul.miage.resto.Launcher;
import fr.ul.miage.resto.appinfo.Service;
import fr.ul.miage.resto.constants.DishType;
import fr.ul.miage.resto.constants.InfoRestaurant;
import fr.ul.miage.resto.constants.OrderState;
import fr.ul.miage.resto.constants.Role;
import fr.ul.miage.resto.controller.feature.LogInController;
import fr.ul.miage.resto.dao.service.BaseService;
import fr.ul.miage.resto.model.entity.CategoryEntity;
import fr.ul.miage.resto.model.entity.DishEntity;
import fr.ul.miage.resto.model.entity.OrderEntity;
import fr.ul.miage.resto.model.entity.ProductEntity;
import fr.ul.miage.resto.view.feature.LogInView;
import fr.ul.miage.resto.view.role.CookView;
import fr.ul.miage.resto.view.role.DirectorView;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class CookController extends RoleController {
    private static final String MINUS_THAN = "moins de";
    private static final String CHAR = " caractères";
    private static final String YES_NO = "\n O) Non \n 1) Oui";
    private final BaseService baseService;
    private final Service service;
    private final CookView cookView;

    @Override
    public void callAction(Integer action) {
        Role role = Launcher.getLoggedUser().getRole();
        switch (action) {
            case 0:
                if (role.equals(Role.DIRECTOR)) {
                    DirectorController directorController = new DirectorController(baseService, service, new DirectorView());
                    directorController.launch(Role.DIRECTOR);
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
            launch(Role.COOK);
        } else {
            cookView.displayOrdersList(orders);
            askMainMenu();
        }
    }

    public void setOrderReady() {
        List<OrderEntity> orders = baseService.getNotPreparedOrders();
        if (!orders.isEmpty()) {
            cookView.displayOrdersList(orders);
            cookView.displayMessage("\nEntrez le numéro de la commande prête à être servie ou 0 pour annuler.");
            int input = getIntegerInput(0, orders.size()) - 1;
            if (input != -1) {
                orders.get(input).setOrderState(OrderState.PREPARED);
                baseService.update(orders.get(input));
                savePerformance(service, baseService, "preparationTime", 15, 30);
                cookView.displayMessage("La commande pour la table " + orders.get(input).getIdTable() + " est prête !" +
                        "\nVoulez vous déclarer une autre commande terminée ?" + YES_NO);
                if (doAgain()) {
                    setOrderReady();
                }
            }
        } else {
            cookView.displayNoOrderToPrepare();
        }
        launch(Role.COOK);
    }

    protected void createDish() {
        DishEntity newDish = new DishEntity();
        boolean modify = false;
        cookView.displayAskInput("le nom du plat", MINUS_THAN + InfoRestaurant.MAX_LENGTH_NAME.getValue() + CHAR);
        String name = getStringInput();
        if (baseService.getDishById(name) != null) {
            cookView.displayMessage("Le plat existe déjà. Modifier ou annuler ?" +
                    "\n O) Annuler" +
                    "\n 1) Modifier");
            Integer choice = getIntegerInput(0, 1);
            if (choice == 0) {
                launch(Role.COOK);
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

        cookView.displayMessage(newDish.toString());
        cookView.displayMessage("Voulez vous" +
                "\n 0) Abandonner ce plat" +
                "\n 1) Sauvegarder ce plat");
        if (getIntegerInput(0, 1) == 1) {
            saveDish(newDish, modify);
        } else {
            cookView.displaySaveOrNot(false);
        }
        launch(Role.COOK);
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
        List<CategoryEntity> categories = baseService.getCategoriesByDishType(dishType);
        cookView.displayCategories(categories);
        Integer input = getIntegerInput(0, categories.size());
        if (input == 0) {
            cookView.displayAskInput("la nouvelle catégorie du plat", MINUS_THAN + InfoRestaurant.MAX_LENGTH_NAME.getValue() + CHAR);
            String newCat = getStringInput();
            while (baseService.getCategoryById(newCat) != null) {
                cookView.displayAskInput("une catégorie de plat qui n'est pas déjà définie dans un autre type de plat que " + dishType.getDish(), MINUS_THAN + InfoRestaurant.MAX_LENGTH_NAME.getValue() + CHAR);
                newCat = getStringInput();
            }
            return newCat;
        } else {
            return categories.get(input - 1).get_id();
        }
    }

    protected List<String> getDishProducts() {
        List<ProductEntity> productEntities = baseService.getAllProducts();
        cookView.displayAskInput("les produits du plat", "ex : 1/6/8, maximum " + InfoRestaurant.MAX_CHOICES.getValue() + " produits");
        cookView.displayProducts(productEntities);
        List<ProductEntity> selection = parseToSelectedProducts(productEntities, getStringMultipleChoices(1, productEntities.size()));
        cookView.displayProducts(selection);
        cookView.displayMessage("Ces ingrédients vous conviennent-il ?" + YES_NO);
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
            cookView.displayMessage("Souhaitez vous annoncer la fin de prise en charge des nouveaux clients ? Choix définitif." +
                    YES_NO);
            Integer input = getIntegerInput(0, 1);
            if (input == 1) {
                service.setEndNewClients(true);
                cookView.displayMessage("La fin de prise en charge des nouveaux clients est annoncée.");
            }
        } else {
            cookView.displayMessage("La fin de prise en charge des nouveaux clients a déjà été annoncée.");
        }
        launch(Role.COOK);
    }
}
