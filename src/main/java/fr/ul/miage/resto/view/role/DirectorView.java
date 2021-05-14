package fr.ul.miage.resto.view.role;

import fr.ul.miage.resto.constants.DishType;
import fr.ul.miage.resto.constants.MealType;
import fr.ul.miage.resto.constants.Role;
import fr.ul.miage.resto.model.entity.*;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DirectorView extends RoleView {

    public void displayStock(List<ProductEntity> products) {
        displayMessage("Sélectionner le produit auquel ajouter du stock: " +
                "\n 0) Annuler" +
                "\n 1) Nouveau produit");
        List<String> choices = new ArrayList<>();
        for (ProductEntity product : products) {
            choices.add(product.getId() + ", Stock: " + product.getStock());
        }
        displayChoice(choices, 2, true);
    }

    public void displayAskAddStock(int min, int max) {
        displayMessage("Entrez le stock à ajouter au stock initial. Entre " + min + " et " + max + ": ");
    }

    public void displayTables(List<TableEntity> tables) {
        List<String> choices = new ArrayList<>();
        for (TableEntity table : tables) {
            choices.add(table.toString());
        }
        displayChoice(choices, 1, true);
    }

    public void displayManage(List<String> actions, String toManage) {
        displayMessage("Selectionnez une action. \n 0) Annuler");
        List<String> choices = new ArrayList<>();
        for (String action : actions) {
            choices.add(action + " " + toManage);
        }
        displayChoice(choices, 1, true);
    }

    public void displayManage(List<String> actions) {
        displayManage(actions, "");
    }

    public void displayEmployees(String action, List<UserEntity> users) {
        displayMessage("Selectionnez l'employé à " + action.toLowerCase() + "\n 0) Annuler");
        List<String> choices = new ArrayList<>();
        for (UserEntity user : users) {
            choices.add(user.getId());
        }
        displayChoice(choices, 1, true);
    }

    public void displayRoleChoice() {
        displayMessage("Selectionnez le role de l'employé.");
        List<String> choices = new ArrayList<>();
        for (Role role : Role.values()) {
            choices.add(role.getValue());
        }
        displayChoice(choices, 1, true);
    }

    // nécessite une liste triée
    public void displayDishChoice(List<DishEntity> dishs, boolean withIndex) {
        if (withIndex) {
            displayMessage("0) Annuler");
        }

        int startIndex = 1;
        for (DishType dishType : DishType.values()) {
            List<String> choice = dishs.stream()
                    .filter(dish -> dish.getDishType() == dishType)
                    .sorted(DishEntity::orderDishByType)
                    .map(DishEntity::getId)
                    .collect(Collectors.toList());

            if (CollectionUtils.isNotEmpty(choice)) {
                displayMessage(dishType.getDish());
                displayChoice(choice, startIndex, withIndex);
                startIndex += choice.size();
                displayMessage("\n");
            }
        }
    }

    public void displayPerf(List<PerformanceEntity> perfs) {
        for (PerformanceEntity perf : perfs) {
            String date = perf.getId().substring(0, 10);
            String type = perf.getId().substring(10);
            MealType meal = MealType.valueOf(type);
            int meanPrep = perf.getPreparationTime() / perf.getNbOrder();
            int meanServ = perf.getServiceTime() / perf.getNbTableServed();
            displayMessage(" -> " + date + " " + meal.getMealValue() +
                    "\n     - Préparation des commandes : " + meanPrep + " min/commande" +
                    "\n     - Rotation des clients : " + meanServ + " min/table");
        }
    }
}
