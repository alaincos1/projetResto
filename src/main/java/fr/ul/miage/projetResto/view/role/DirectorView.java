package fr.ul.miage.projetResto.view.role;

import fr.ul.miage.projetResto.constants.Role;
import fr.ul.miage.projetResto.model.entity.ProductEntity;
import fr.ul.miage.projetResto.model.entity.UserEntity;

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

    public void displayManageEmployeesMenu(List<String> actions) {
        System.out.println("Selectionnez une action.");
        System.out.println("0) Annuler");
        int i = 0;
        for (i = 0; i < actions.size(); i++) {
            System.out.println((i + 1) + ") " + actions.get(i) + " un employé");
        }

    }

    public void displayEmployees(String action, List<UserEntity> users) {
        System.out.println("Selectionnez l'employé à " + action.toLowerCase());
        System.out.println("0) Annuler");
        for (int i = 0; i < users.size(); i++) {
            System.out.println((i + 1) + ") " + users.get(i).get_id());
        }
    }

    public void displayIdChoice() {
        System.out.println("Renseignez l'identifiant de l'employé.");
    }

    public void displayRoleChoice() {
        System.out.println("Selectionnez le role de l'employé.");
        for (int i = 0; i < Role.values().length; i++) {
            System.out.println(i + ") " + Role.values()[i]);
        }
    }

    public void displayDirectionRole() {
        System.out.println("Selectionnez le role de l'employé.");
        System.out.println("0) " + Role.Director);
        System.out.println("1) " + Role.Butler);
    }
}
