package fr.ul.miage.resto.view.role;

import fr.ul.miage.resto.constants.MealType;
import fr.ul.miage.resto.constants.Role;
import fr.ul.miage.resto.constants.TableState;
import fr.ul.miage.resto.controller.role.ButlerController;
import fr.ul.miage.resto.dao.service.BaseService;
import fr.ul.miage.resto.model.entity.BookingEntity;
import fr.ul.miage.resto.model.entity.TableEntity;
import fr.ul.miage.resto.model.entity.UserEntity;
import org.codehaus.plexus.util.StringUtils;

import java.util.List;

public class ButlerView extends RoleView {
    private static final String ID = " - Id: ";
    private static final String NB_PLACES = " Nb places: ";
    private static final String SERVER = " Serveur : ";
    private static final String ASSISTANT = " Assistant : ";


    public int displayTablesList(List<TableEntity> tables, String userId, String tableState) {
        int nbTables = 0;
        for (TableEntity table : tables) {
            if ((StringUtils.isBlank(userId) && table.getTableState().getState().equals(tableState)) ||
                    (StringUtils.isNotBlank(userId) && !userId.equals(table.getIdServer()) && !userId.equals(table.getIdHelper()))) {
                displayMessage(ID + table.get_id() + NB_PLACES + table.getNbSeats() + SERVER
                        + table.getIdServer() + ASSISTANT + table.getIdHelper());
                nbTables++;
            }
        }
        return nbTables;
    }

    public void displayServersList(List<UserEntity> users) {
        for (UserEntity user : users) {
            if (user.getRole().equals(Role.SERVER) || user.getRole().equals(Role.HELPER)) {
                displayMessage(" - " + user.get_id() + " -> " + user.getRole().getValue());
            }
        }
    }

    public void displayAllTables(List<TableEntity> tables) {
        for (TableEntity table : tables) {
            displayMessage(ID + table.get_id() + NB_PLACES + table.getNbSeats() + SERVER
                    + table.getIdServer() + ASSISTANT + table.getIdHelper());
        }
    }


    public void displayAllTablesWithNoBooking(List<TableEntity> tables, String date, MealType mealType,
                                              BaseService baseService) {
        for (TableEntity table : tables) {
            if (tableIsReserved(table, date, mealType, baseService) == null) {
                displayMessage(ID + table.get_id() + NB_PLACES + table.getNbSeats());
            }
        }
    }

    public int displayAllTablesWithBooking(List<TableEntity> tables, String date, MealType mealType,
                                           BaseService baseService) {
        int nbBooking = 0;
        for (TableEntity table : tables) {
            String name = tableIsReserved(table, date, mealType, baseService);
            if (StringUtils.isNotBlank(name) && table.getTableState().equals(TableState.BOOKED)) {
                displayMessage(ID + table.get_id() + NB_PLACES + table.getNbSeats() + " Nom : " + name);
                nbBooking++;
            }
        }
        return nbBooking;
    }

    private String tableIsReserved(TableEntity table, String date, MealType mealType, BaseService baseService) {
        List<BookingEntity> bookings = baseService.getAllBooking();
        for (BookingEntity booking : bookings) {
            if (table.get_id().equals(booking.getIdTable()) && booking.getMealType().equals(mealType)
                    && booking.getDate().equals(date)) {
                return booking.getReservationName();
            }
        }
        return null;
    }

    public void displayChoiceTableClient() {
        displayMessage("Choisissez la table pour placer vos clients. Entrez l'Id de la table.");
    }

    public void displayBookingDiner() {
        displayMessage("La réservation est possible seulement pour le service du diner.");
        displayMessage("Est ce correct ? \n0) Non \n1) Oui");
    }

    public void displayInputIncorrect() {
        displayMessage("Entrée incorrecte, veuillez recommencer.");
    }

    public int displayAllTablesForBill(List<TableEntity> tables, ButlerController butlerController) {
        int t = 0;
        for (TableEntity table : tables) {
            if (Boolean.TRUE.equals(butlerController.stateForBill(table) && butlerController.ordersServed(table))) {
                displayMessage(ID + table.get_id() + NB_PLACES + table.getNbSeats());
                t++;
            }
        }
        return t;
    }
}
