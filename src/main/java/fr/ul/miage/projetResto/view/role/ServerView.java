package fr.ul.miage.projetResto.view.role;

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
}
