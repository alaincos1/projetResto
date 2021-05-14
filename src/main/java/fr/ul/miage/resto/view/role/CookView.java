package fr.ul.miage.resto.view.role;

import fr.ul.miage.resto.constants.DishType;
import fr.ul.miage.resto.model.entity.CategoryEntity;
import fr.ul.miage.resto.model.entity.OrderEntity;
import fr.ul.miage.resto.model.entity.ProductEntity;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class CookView extends RoleView {

    public void displayOrdersList(List<OrderEntity> orders) {
        int i = 1;
        for (OrderEntity order : orders) {
            displayMessage(" " + i + ") Table: " + order.getIdTable() + (Boolean.TRUE.equals(order.getChildOrder()) ? " PRIORITAIRE ENFANT" : StringUtils.EMPTY));
            for (String dish : order.getIdsDish()) {
                displayMessage("  -> " + dish);
            }
            i++;
        }
    }

    public void displayNoOrderToPrepare() {
        displayMessage("Il n'y a aucune commande à préparer.");
    }

    public void displayAskInput(String input, String regex) {
        if (!regex.isEmpty()) {
            displayMessage("Entrez " + input + " (" + regex + "): ");
        } else {
            displayMessage("Entrez " + input + ": ");
        }
    }

    public void displayDishType() {
        List<String> choices = new ArrayList<>();
        for (DishType dishType : DishType.values()) {
            choices.add(dishType.getDish());
        }
        displayChoice(choices, 1, true);
    }

    public void displayCategories(List<CategoryEntity> categories) {
        List<String> choices = new ArrayList<>();
        displayMessage(" 0) Nouvelle catégorie");
        for (CategoryEntity category : categories) {
            choices.add(category.get_id());
        }
        displayChoice(choices, 1, true);
    }

    public void displayProducts(List<ProductEntity> productEntities) {
        List<String> choices = new ArrayList<>();
        for (ProductEntity product : productEntities) {
            choices.add(product.get_id());
        }
        displayChoice(choices, 1, true);
    }

    public void displaySaveOrNot(boolean b) {
        if (b) {
            displaySuccess();
        } else {
            displayError();
        }
    }
}
