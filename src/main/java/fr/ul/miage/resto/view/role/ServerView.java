package fr.ul.miage.resto.view.role;

import fr.ul.miage.resto.model.entity.DishEntity;
import fr.ul.miage.resto.model.entity.OrderEntity;
import fr.ul.miage.resto.model.entity.TableEntity;
import fr.ul.miage.resto.utils.MenuUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ServerView extends RoleView {
    private static final String CANCEL = "\n 0) Annuler";

    public void displayNoTablesAffected() {
        displayMessage("Aucune table ne vous est affectée.");
    }

    public void displayTablesAffected(List<TableEntity> tables) {
        List<String> choices = new ArrayList<>();
        for (TableEntity table : tables) {
            choices.add(table.toString());
        }
        displayChoice(choices, 1, true);
    }

    public void displayTablesToDirty(List<TableEntity> tablesToDirty) {
        displayMessage("Quelle table est à débarasser ?" + CANCEL);
        List<String> choices = new ArrayList<>();
        for (TableEntity table : tablesToDirty) {
            choices.add(table.toString());
        }
        displayChoice(choices, 1, true);
    }

    public void displayOrdersToServe(List<OrderEntity> orders) {
        displayMessage("Quelle commande souhaitez vous servir ?" + CANCEL);
        List<String> choices = new ArrayList<>();
        for (OrderEntity order : orders) {
            choices.add(" Table n°" + order.getIdTable() + ", état: " + order.getOrderState() + ", nombre de plats: " + order.getIdsDish().size());
        }
        displayChoice(choices, 1, true);
    }

    public void displayChoiceDishType(Map<Integer, MenuUtil> menus) {
        displayMessage("Quel type de plats compose cette commande ?" + CANCEL);
        List<String> choices = new ArrayList<>();
        for (int i = 1; i < menus.size() + 1; i++) {
            if (menus.get(i).isDishesAvailable()) {
                choices.add(menus.get(i).getDishType().getDish());
            } else {
                choices.add(menus.get(i).getDishType().getDish() + " : Désolé. Il n'y a aucun plat disponible. Annuler.");
            }
        }
        displayChoice(choices, 1, true);
    }

    public void displayNoDishOnTheMenu() {
        displayMessage("Il n'y a pas de plats sur le menu.");
    }

    public void displayMenuByCat(List<DishEntity> allDishes) {
        int i = 0;
        String cat = allDishes.get(0).getIdCategory();
        displayMessage("--- " + cat + " ---");
        for (DishEntity dish : allDishes) {
            if (!dish.getIdCategory().equals(cat)) {
                cat = dish.getIdCategory();
                displayMessage("--- " + cat + " ---");
            }
            displayMessage(" " + i + ") " + dish.get_id() + ", Prix: " + dish.getPrice());
            i++;
        }
    }

    public void displayNoDishInTheOrder() {
        displayMessage("Il est impossible de valider ou supprimer des plats d'une commande vide.");
    }

    public void displayOrderChoice() {
        displayMessage("Entrez votre choix. \"-a 1/2/6\" pour ajouter, \"-d 1/6\" pour supprimer, \"-v\" pour valider.");
    }

    public void displayOrderToSave(OrderEntity orderToSave) {
        if (Boolean.TRUE.equals(orderToSave.getChildOrder())) {
            displayMessage("- Commande Enfant");
        }
        for (String dish : orderToSave.getIdsDish()) {
            displayMessage(" - " + dish);
        }
        displayMessage("Validez vous cette commande ?" +
                "\n 0) Non" +
                "\n 1) Oui");
    }
}
