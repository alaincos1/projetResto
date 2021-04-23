package fr.ul.miage.projetResto.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Liste de toutes les fonctionnalités par role.
 * En cas d'ajout d'une fonctionnalité, ajouter celle-ci dans la fonction callAction() du role correspondant dans le *Role*Controller
 */
@AllArgsConstructor
@Getter
public enum Features {
    Employees("Gérer les employés", Role.Director),
    DayMenu("Gérer la carte du jour", Role.Director),
    Stocks("Gérer les stocks", Role.Director),
    Incomes("Analyser les ventes", Role.Director),
    Performances("Analyser les performances", Role.Director),
    EndService("Arrêter le service", Role.Director),
    Butler("Fonctionnalités du maître d'hôtel", Role.Director),
    Server("Fonctionnalités des serveurs", Role.Director),
    Helper("Fonctionnalités des assistants de service", Role.Director),
    Cook("Fonctionnalités des cuisiniers", Role.Director),
    ServerToTable("Affecter les tables au personnel", Role.Butler),
    Bill("Editer des factures", Role.Butler),
    ClientToTable("Affecter un client à une table", Role.Butler),
    Booking("Prendre des réservations", Role.Butler),
    TablesViewServer("Visualiser mes tables", Role.Server),
    DirtyTable("Déclarer des tables à débarrasser", Role.Server),
    TakeOrders("Prendre des commandes pour une table", Role.Server),
    TablesViewHelper("Visualiser mes tables", Role.Helper),
    CleanTable("Nettoyer des tables", Role.Helper),
    OrdersView("Voir la liste des commandes", Role.Cook),
    OrdersReady("Déclarer un commande prête", Role.Cook),
    CreateDishes("Créer des plats", Role.Cook),
    EndCooking("Prévenir de la fin des prises de commandes", Role.Cook);

    private String label;
    private Role role;
}
