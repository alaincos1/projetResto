package fr.ul.miage.projetResto.view.role;

import fr.ul.miage.projetResto.model.entity.TableEntity;

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
}
