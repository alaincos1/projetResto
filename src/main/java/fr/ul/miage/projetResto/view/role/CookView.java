package fr.ul.miage.projetResto.view.role;

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
}
