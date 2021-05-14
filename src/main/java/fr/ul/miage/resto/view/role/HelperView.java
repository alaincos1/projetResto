package fr.ul.miage.resto.view.role;

import fr.ul.miage.resto.model.entity.TableEntity;

import java.util.ArrayList;
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

    public void displayTablesToClean(List<TableEntity> tablestoClean) {
        displayMessage("Quelle table souhaitez vous nettoyer ?" +
                "\n 0) Annuler");
        List<String> choices = new ArrayList<>();
        for (TableEntity table : tablestoClean) {
            choices.add(table.toString());
        }
        displayChoice(choices, 1, true);
    }
}
