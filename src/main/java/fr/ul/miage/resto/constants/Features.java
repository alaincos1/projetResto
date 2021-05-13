package fr.ul.miage.resto.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Liste de toutes les fonctionnalités par role.
 * En cas d'ajout d'une fonctionnalité, ajouter celle-ci dans la fonction callAction() du role correspondant dans le *Role*Controller
 */
@AllArgsConstructor
@Getter
public enum Features {
    EMPLOYEES("Gérer les employés", Role.DIRECTOR),
    DAY_MENU("Gérer la carte du jour", Role.DIRECTOR),
    STOCKS("Gérer les stocks", Role.DIRECTOR),
    TABLES("Gérer les tables", Role.DIRECTOR),
    INCOMES("Analyser les ventes", Role.DIRECTOR),
    PERFORMANCES("Analyser les performances", Role.DIRECTOR),
    END_SERVICE("Arrêter le service", Role.DIRECTOR),
    BUTLER("Fonctionnalités du maître d'hôtel", Role.DIRECTOR),
    SERVER("Fonctionnalités des serveurs", Role.DIRECTOR),
    HELPER("Fonctionnalités des assistants de service", Role.DIRECTOR),
    COOK("Fonctionnalités des cuisiniers", Role.DIRECTOR),
    SERVER_TO_TABLE("Affecter les tables au personnel", Role.BUTLER),
    BILL("Editer des factures", Role.BUTLER),
    CLIENT_TO_TABLE("Affecter un client à une table", Role.BUTLER),
    BOOKING("Prendre des réservations", Role.BUTLER),
    TABLES_VIEW_SERVER("Visualiser mes tables", Role.SERVER),
    DIRTY_TABLE("Déclarer des tables à débarrasser", Role.SERVER),
    TAKE_ORDERS("Prendre des commandes à une table", Role.SERVER),
    SERVE_ORDERS("Servir des commandes à une table", Role.SERVER),
    TABLES_VIEW_HELPER("Visualiser mes tables", Role.HELPER),
    CLEAN_TABLE("Nettoyer des tables", Role.HELPER),
    ORDERS_VIEW("Voir la liste des commandes", Role.COOK),
    ORDERS_READY("Déclarer un commande prête", Role.COOK),
    CREATE_DISH("Créer un plat", Role.COOK),
    END_COOKING("Prévenir de la fin des prises de commandes", Role.COOK);

    private final String label;
    private final Role role;
}
