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
            System.out.println(" " + i + ") Table n°" + table.get_id() + ", état: " + table.getTableState().getState());
            i++;
        }
    }
}
