package fr.ul.miage.projetResto.view.role;

import fr.ul.miage.projetResto.constants.InfoRestaurant;
import fr.ul.miage.projetResto.model.entity.ProductEntity;
import fr.ul.miage.projetResto.model.entity.TableEntity;

import java.util.List;

public class DirectorView extends RoleView {
    public void displayAskEndService() {
        System.out.println("Souhaitez vous annoncer la fin du service (des prises des commandes) ? Choix définitif." +
                "\n O) Non" +
                "\n 1) Oui");
    }

    public void displayEnded() {
        System.out.println("La fin du service (des prises des commandes) est annoncée.");
    }

    public void displayAlreadyEnded() {
        System.out.println("La fin du service (des prises des commandes) a déjà été annoncée.");
    }

    public void displayStock(List<ProductEntity> products) {
        System.out.println("Sélectionner le produit auquel ajouter du stock: " +
                "\n 0) Annuler" +
                "\n 1) Nouveau produit");
        int i = 2;
        for (ProductEntity product : products) {
            System.out.println(" " + i + ") " + product.get_id() + ", Stock: " + product.getStock());
            i++;
        }
    }

    public void displayManageStockAgain() {
        System.out.println("Souhaitez vous ajouter du stock à un autre produit ?" +
                "\n O) Non" +
                "\n 1) Oui");
    }

    public void displayProductSave() {
        System.out.println("Le produit et son stock sont sauvegardés.");
    }

    public void displayAskAddStock(int min, int max) {
        System.out.println("Entrez le stock à ajouter au stock initial. Entre " + min + " et " + max + ": ");
    }

    public void displayStockMax() {
        System.out.println("Stock maximal pour ce produit. Entrez un autre produit.");
    }

    public void displayProductAlreadyExist() {
        System.out.println("Ce produit existe déjà, entrez un autre intitulé.");
    }

    public void displayAskNameProduct() {
        System.out.println("Entrez le nom du produit.");
    }

    public void displayNoTables() {
        System.out.println("Il n'y a aucune table.");
    }

    public void displayTables(List<TableEntity> tables) {
        int i = 1;
        for (TableEntity table : tables) {
            System.out.println(" " + i + ") " + table.toString());
            i++;
        }
    }

    public void displayAskAddRemoveTable() {
        System.out.println("Souhaitez vous ajouter ou supprimer une table ?" +
                "\n O) Annuler" +
                "\n 1) Ajouter" +
                "\n 2) Supprimer");
    }

    public void displayAskAddTable() {
        System.out.println("Souhaitez vous ajouter une table ?" +
                "\n O) Annuler" +
                "\n 1) Ajouter");
    }

    public void displayEnoughTables() {
        System.out.println("Il y a déjà le maximum de tables possible: " + InfoRestaurant.MAX_TABLES.getValue());
    }

    public void displayTableAdded() {
        System.out.println("Table ajoutée !");
    }

    public void displayTableRemoved() {
        System.out.println("Table supprimée !");
    }

    public void displayAskNumberSeats() {
        System.out.println("Entrez un nombre de place pour cette table entre 1 et " + InfoRestaurant.MAX_SEATS.getValue() + " ");
    }

    public void displayAskTableToRemove() {
        System.out.println("Quelle table souhaitez vous supprimer ?");
    }

    public void displayNoTableCanBeRemoved() {
        System.out.println("Aucune table est supprimable (Clients attablés, réservations, etc...)");
    }
}
