package fr.ul.miage.projetResto.view.role;

import fr.ul.miage.projetResto.constants.Features;
import fr.ul.miage.projetResto.constants.Role;

public class RoleView {
    public void displayMenu(Role roleTemp) {
        System.out.println("\n --- \n " +
                "En tant que "+ roleTemp.getValue()+" choisissez une action : \n" +
                " 0) Retour (déconnexion si aucun menu supérieur)");
        int i = 1;
        for (Features feature : Features.values()) {
            if (feature.getRole().equals(roleTemp)) {
                System.out.println(" "+i + ") " + feature.getLabel());
                i++;
            }
        }
    }
}
