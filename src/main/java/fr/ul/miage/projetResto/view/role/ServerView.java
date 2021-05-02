package fr.ul.miage.projetResto.view.role;

import fr.ul.miage.projetResto.model.entity.OrderEntity;
import fr.ul.miage.projetResto.model.entity.TableEntity;

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
}
