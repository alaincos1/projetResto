package fr.ul.miage.projetResto.view.role;

import fr.ul.miage.projetResto.constants.DishType;
import fr.ul.miage.projetResto.model.entity.CategoryEntity;
import fr.ul.miage.projetResto.model.entity.DishEntity;
import fr.ul.miage.projetResto.model.entity.OrderEntity;
import fr.ul.miage.projetResto.model.entity.ProductEntity;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class CookView extends RoleView {

    public void displayAskEndCooking() {
        System.out.println("Souhaitez vous annoncer la fin de prise en charge des nouveaux clients ? Choix définitif." +
                "\n O) Non" +
                "\n 1) Oui");
    }

    public void displayAlreadyEnded() {
        System.out.println("La fin de prise en charge des nouveaux clients a déjà été annoncée.");
    }

    public void displayEnded() {
        System.out.println("La fin de prise en charge des nouveaux clients est annoncée.");
    }

    public void displayOrdersList(List<OrderEntity> orders) {
        int i = 1;
        for (OrderEntity order : orders) {
            System.out.println(" " + i + ") Id: " + order.get_id() + " Table: " + order.getIdTable() + (Boolean.TRUE.equals(order.getChildOrder()) ? " PRIORITAIRE ENFANT" : StringUtils.EMPTY));
            for (String dish : order.getIdsDish()) {
                System.out.println("  -> " + dish);
            }
            i++;
        }
    }

    public void displayWhichOrderPrepared() {
        System.out.println("\nEntrez le numéro de la commande prête à être servie ou 0 pour annuler.");
    }

    public void displayOrderPrepared(String id, String idTable) {
        System.out.println("La commande " + id + " pour la table " + idTable + " est prête !" +
                "\nVoulez vous déclarer une autre commande terminée ?" +
                "\n O) Non" +
                "\n 1) Oui");
    }

    public void displayNoOrderToPrepare() {
        System.out.println("Il n\'y a aucune commande à préparer.");
    }

    public void displayAskInput(String input, String regex) {
        if (!regex.isEmpty()) {
            System.out.println("Entrez " + input + " (" + regex + "): ");
        } else {
            System.out.println("Entrez " + input + ": ");
        }
    }

    public void displayModifyOrCancel() {
        System.out.println("Le plat existe déjà. Modifier ou annuler ?" +
                "\n O) Annuler" +
                "\n 1) Modifier");
    }

    public void displayDishType() {
        Integer i = 1;
        for (DishType dishType : DishType.values()) {
            System.out.println(" " + i + ") " + dishType.getDish());
            i++;
        }
    }

    public void displayCategories(List<CategoryEntity> categories) {
        Integer i = 1;
        System.out.println(" 0) Nouvelle catégorie");
        for (CategoryEntity categorie : categories) {
            System.out.println(" " + i + ") " + categorie.get_id());
            i++;
        }
    }

    public void displayProducts(List<ProductEntity> productEntities) {
        Integer i = 1;
        for (ProductEntity product : productEntities) {
            System.out.println(" " + i + ") " + product.get_id());
            i++;
        }
    }

    public void displayProductsOkOrNot() {
        System.out.println("Ces ingrédients vous conviennent-il ?" +
                "\n 0) Non" +
                "\n 1) Oui");
    }

    public void displayDish(DishEntity newDish) {
        System.out.println(newDish.toString());
        System.out.println("Voulez vous" +
                "\n 0) Abandonner ce plat" +
                "\n 1) Sauvegarder ce plat");
    }

    public void displaySaveOrNot(boolean b) {
        if (b) {
            System.out.println("Sauvegarde réussie");
        } else {
            System.out.println("Abandon/problème de sauvegarde, plat non créé.");
        }
    }
}
