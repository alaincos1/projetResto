package fr.ul.miage.projetResto.view.role;

import fr.ul.miage.projetResto.constants.DishType;
import fr.ul.miage.projetResto.constants.InfoRestaurant;
import fr.ul.miage.projetResto.constants.Role;
import fr.ul.miage.projetResto.model.entity.DishEntity;
import fr.ul.miage.projetResto.model.entity.ProductEntity;
import fr.ul.miage.projetResto.model.entity.TableEntity;
import fr.ul.miage.projetResto.model.entity.UserEntity;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    public void displayManage(List<String> actions, String toManage) {
        System.out.println("Selectionnez une action.");
        System.out.println("0) Annuler");
        int i = 0;
        for (i = 0; i < actions.size(); i++) {
            System.out.println((i + 1) + ") " + actions.get(i) + " " + toManage);
        }
    }

    public void displayManage(List<String> actions) {
        displayManage(actions, "");
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

    public void displayNoDishsOnTheMenu() {
        System.out.println("Il n'y a pas de plats dans le menu");
    }

    public void displayNoDishsNotOntheMenu() {
        System.out.println("Il n'y a aucuns plats d'enregistrés ou ils sont déjà tous dans le menu");
    }

    public void displayChoice(List<String> choice, Integer startIndex, boolean withIndex) {
        for (String display : choice) {
            if (withIndex) {
                System.out.print(startIndex++ + ") ");
            }
            System.out.println(display);
        }
    }

    // nécessite une liste triée
    public void displayDishChoice(List<DishEntity> dishs, boolean withIndex) {
        if (withIndex) {
            System.out.println("0) Annuler");
        }

        Integer startIndex = 1;
        for (DishType dishType : DishType.values()) {
            List<String> choice = dishs.stream()
                    .filter(dish -> dish.getDishType() == dishType)
                    .sorted(DishEntity::orderDishByType)
                    .map(DishEntity::get_id)
                    .collect(Collectors.toList());

            if (CollectionUtils.isNotEmpty(choice)) {
                System.out.println(dishType.getDish());
                displayChoice(choice, startIndex, withIndex);
                startIndex += choice.size();
                System.out.println();
            }
        }
    }

    public void displayMostFamous(Map.Entry<String, Integer> mostFamous) {
        System.out.println(mostFamous.getKey() + ": " + mostFamous.getValue());
    }

    public void simpleDisplay(String toDisplay) {
        System.out.println(toDisplay);
    }
}
