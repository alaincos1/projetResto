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
    EMPLOYEES("Gérer les employés", Role.Director),
    DAY_MENU("Gérer la carte du jour", Role.Director),
    STOCKS("Gérer les stocks", Role.Director),
    INCOMES("Analyser les ventes", Role.Director),
    PERFORMANCES("Analyser les performances", Role.Director),
    END_SERVICE("Arrêter le service", Role.Director),
    BUTLER("Fonctionnalités du maître d'hôtel", Role.Director),
    SERVER("Fonctionnalités des serveurs", Role.Director),
    HELPER("Fonctionnalités des assistants de service", Role.Director),
    COOK("Fonctionnalités des cuisiniers", Role.Director),
    SERVER_TO_TABLE("Affecter les tables au personnel", Role.Butler),
    BILL("Editer des factures", Role.Butler),
    CLIENT_TO_TABLE("Affecter un client à une table", Role.Butler),
    BOOKING("Prendre des réservations", Role.Butler),
    TABLES_VIEW_SERVER("Visualiser mes tables", Role.Server),
    DIRTY_TABLE("Déclarer des tables à débarrasser", Role.Server),
    TAKE_ORDERS("Prendre des commandes à une table", Role.Server),
    SERVE_ORDERS("Servir des commandes à une table", Role.Server),
    TABLES_VIEW_HELPER("Visualiser mes tables", Role.Helper),
    CLEAN_TABLE("Nettoyer des tables", Role.Helper),
    ORDERS_VIEW("Voir la liste des commandes", Role.Cook),
    ORDERS_READY("Déclarer un commande prête", Role.Cook),
    CREATE_DISH("Créer un plat", Role.Cook),
    END_COOKING("Prévenir de la fin des prises de commandes", Role.Cook);

    private final String label;
    private final Role role;
}
