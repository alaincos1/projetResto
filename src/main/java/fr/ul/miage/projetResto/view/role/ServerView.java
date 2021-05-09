package fr.ul.miage.projetResto.view.role;

import fr.ul.miage.projetResto.model.entity.DishEntity;
import fr.ul.miage.projetResto.model.entity.OrderEntity;
import fr.ul.miage.projetResto.model.entity.TableEntity;
import fr.ul.miage.projetResto.utils.MenuUtil;

import java.util.HashMap;
import java.util.List;

public class ServerView extends RoleView {

    public void displayNoTablesAffected() {
        System.out.println("Aucune table ne vous est affectée.");
    }

    public void displayTablesAffected(List<TableEntity> tables) {
        int i = 1;
        for (TableEntity table : tables) {
            System.out.println(" " + i + ") " + table.toString());
            i++;
        }
    }

    public void displayNoTablesToDirty() {
        System.out.println("Aucune table à déclarer pour être débarassée.");
    }

    public void displayTablesToDirty(List<TableEntity> tablesToDirty) {
        System.out.println("Quelle table est à débarasser ?" +
                "\n 0) Annuler");
        int i = 1;
        for (TableEntity table : tablesToDirty) {
            System.out.println(" " + i + ") " + table.toString());
            i++;
        }
    }

    public void displayTableDirtyDoAgain() {
        System.out.println("Table déclarée à débarasser ! Voulez vous en déclarer une autre ?" +
                "\n 0) Non" +
                "\n 1) Oui");
    }

    public void displayNoOrdersToServe() {
        System.out.println("Il n'y a aucune commande à servir");
    }

    public void displayOrdersToServe(List<OrderEntity> orders) {
        System.out.println("Quelle commande souhaitez vous servir ?" +
                "\n 0) Annuler");
        int i = 1;
        for (OrderEntity order : orders) {
            System.out.println(" " + i + ") Table n°" + order.getIdTable() + ", état: " + order.getOrderState() + ", nombre de plats: " + order.getIdsDish().size());
            i++;
        }
    }

    public void displayOrderServeAgain() {
        System.out.println("Commande servie ! Voulez vous en servir une autre ?" +
                "\n 0) Non" +
                "\n 1) Oui");
    }

    public void displayNoTableToTakeOrders() {
        System.out.println("Il n'y a pas de tables où prendre de commandes. (Pas de clients, commande en cours de préparation, à servir...)");
    }

    public void displayAskTableToServe() {
        System.out.println("À quelle table souhaitez vous prendre une commande ?" +
                "\n 0) Annuler");
    }

    public void displayAskChildOrder() {
        System.out.println("Est-ce une commande enfant ?" +
                "\n 0) Non" +
                "\n 1) Oui");
    }

    public void displayChoiceDishType(HashMap<Integer, MenuUtil> menus) {
        System.out.println("Quel type de plats compose cette commande ?" +
                "\n 0) Annuler");
        for (int i = 1; i < menus.size() + 1; i++) {
            if (menus.get(i).isDishesAvailable()) {
                System.out.println(" " + i + ") " + menus.get(i).getDishType().getDish());
            } else {
                System.out.println(" " + i + ") " + menus.get(i).getDishType().getDish() + " : Désolé. Il n'y a aucun plat disponible. Annuler.");
            }
        }
    }

    public void displayAskDrinksOrder() {
        System.out.println("Voulez vous des boissons ?" +
                "\n 0) Non" +
                "\n 1) Oui");
    }

    public void displayNoDishOnTheMenu() {
        System.out.println("Il n'y a pas de plats sur le menu.");
    }

    public void displayMenuByCat(List<DishEntity> allDishes) {
        int i = 0;
        String cat = allDishes.get(0).getIdCategory();
        System.out.println("--- " + cat + " ---");
        for (DishEntity dish : allDishes) {
            if (!dish.getIdCategory().equals(cat)) {
                cat = dish.getIdCategory();
                System.out.println("--- " + cat + " ---");
            }
            System.out.println(" " + i + ") " + dish.get_id() + ", Prix: " + dish.getPrice());
            i++;
        }
    }

    public void displayNoDishInTheOrder() {
        System.out.println("Il est impossible de valider ou supprimer des plats d'une commande vide.");
    }

    public void displayOrderChoice() {
        System.out.println("Entrez votre choix. \"-a 1/2/6\" pour ajouter, \"-d 1/6\" pour supprimer, \"-v\" pour valider.");
    }

    public void displayNotEnoughStockForDish(DishEntity dishEntity) {
        System.out.println("Stock insuffisant pour le plat: " + dishEntity.get_id());
    }

    public void displayOrderToSave(OrderEntity orderToSave) {
        if(orderToSave.getChildOrder()){
            System.out.println("- Commande Enfant");
        }
        for (String dish : orderToSave.getIdsDish()){
            System.out.println(" - "+dish);
        }
        System.out.println("Validez vous cette commande ?" +
                "\n 0) Non" +
                "\n 1) Oui");
    }

    public void displayEndService() {
        System.out.println("Fin du service, aucune prise de commande possible.");
    }
}
