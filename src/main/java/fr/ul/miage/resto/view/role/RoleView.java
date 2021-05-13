package fr.ul.miage.resto.view.role;

import fr.ul.miage.resto.constants.Features;
import fr.ul.miage.resto.constants.Role;
import fr.ul.miage.resto.view.GeneralView;

public class RoleView extends GeneralView {
    public void displayMenu(Role roleTemp) {
        System.out.println("\n --- \n " +
                "En tant que " + roleTemp.getValue() + " choisissez une action : \n" +
                " 0) Retour (déconnexion si aucun menu supérieur)");
        int i = 1;
        for (Features feature : Features.values()) {
            if (feature.getRole().equals(roleTemp)) {
                System.out.println(" " + i + ") " + feature.getLabel());
                i++;
            }
        }
    }

    public void displayAskReturnMainMenu() {
        System.out.println("\n Entrez 0 pour revenir au menu principal.");
    }

    public void displayError() {
        System.out.println("Abandon/erreur. L'action n'a pas été effectuée");
    }

    public void displaySuccess() {
        System.out.println("Opération effectuée");
    }
}
