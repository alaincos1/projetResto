package fr.ul.miage.resto.view.role;

import fr.ul.miage.resto.constants.Features;
import fr.ul.miage.resto.constants.Role;
import fr.ul.miage.resto.view.GeneralView;

import java.util.ArrayList;
import java.util.List;

public class RoleView extends GeneralView {
    public void displayMenu(Role roleTemp) {
        displayMessage("\n --- \n " +
                "En tant que " + roleTemp.getValue() + " choisissez une action : ");
        List<String> choices = new ArrayList<>();
        choices.add("Retour (déconnexion si aucun menu supérieur)");
        for (Features feature : Features.values()) {
            if (feature.getRole().equals(roleTemp)) {
                choices.add(feature.getLabel());
            }
        }
        displayChoice(choices, 0, true);
    }

    public void displayAskReturnMainMenu() {
        displayMessage("\n Entrez 0 pour revenir au menu principal.");
    }

    public void displayError() {
        displayMessage("Abandon/erreur. L'action n'a pas été effectuée");
    }

    public void displaySuccess() {
        displayMessage("Opération effectuée");
    }
}
