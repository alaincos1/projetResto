package fr.ul.miage.projetResto.view.role;

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
}
