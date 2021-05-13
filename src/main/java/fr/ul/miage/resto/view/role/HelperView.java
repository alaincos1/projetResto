package fr.ul.miage.resto.view.role;

import fr.ul.miage.resto.model.entity.TableEntity;

import java.util.List;

public class HelperView extends RoleView {

    public void displayNoTablesAffected() {
        ServerView serverView = new ServerView();
        serverView.displayNoTablesAffected();
    }

    public void displayTablesAffected(List<TableEntity> tables) {
        ServerView serverView = new ServerView();
        serverView.displayTablesAffected(tables);
    }

    public void displayNoTablesToClean() {
        System.out.println("Aucune table à nettoyer.");
    }

    public void displayTablesToClean(List<TableEntity> tablestoClean) {
        System.out.println("Quelle table souhaitez vous nettoyer ?" +
                "\n 0) Annuler");
        int i = 1;
        for (TableEntity table : tablestoClean) {
            System.out.println(" " + i + ") " + table.toString());
            i++;
        }
    }

    public void displayTableCleanedDoAgain() {
        System.out.println("Table nettoyée ! Voulez vous en nettoyer une autre ?" +
                "\n 0) Non" +
                "\n 1) Oui");
    }
}
