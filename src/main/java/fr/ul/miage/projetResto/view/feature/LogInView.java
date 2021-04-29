package fr.ul.miage.projetResto.view.feature;

public class LogInView {

    public void displayLogIn() {
        System.out.println("Veuillez vous connecter avec votre identifiant : ");
    }

    public void displayDisconnect() {
        System.out.println("Vous êtes déconnecté.e");
    }

    public void displayLogInError() {
        System.out.println("Utilisateur inconnu, veuillez recommencer.");
    }
}
