package fr.ul.miage.projetResto.view.role;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import fr.ul.miage.projetResto.Launcher;
import fr.ul.miage.projetResto.constants.MealType;
import fr.ul.miage.projetResto.constants.Role;
import fr.ul.miage.projetResto.constants.TableState;
import fr.ul.miage.projetResto.model.entity.BookingEntity;
import fr.ul.miage.projetResto.model.entity.OrderEntity;
import fr.ul.miage.projetResto.model.entity.TableEntity;
import fr.ul.miage.projetResto.model.entity.UserEntity;

public class ButlerView extends RoleView {

	public void displayTablesList(List<TableEntity> tables, String userId, String tableState) {
		for (TableEntity table : tables) {
			if (userId == null && table.getTableState().getState().equals(tableState)) {
				System.out.println(" - Id: " + table.get_id() + " Nb places: " + table.getNbSeats() + " Serveur : "
						+ table.getIdServer() + " Assistant : " + table.getIdHelper());
			} else if (userId != null && !userId.equals(table.getIdServer()) && !userId.equals(table.getIdHelper())) {
				System.out.println(" - Id: " + table.get_id() + " Nb places: " + table.getNbSeats() + " Serveur : "
						+ table.getIdServer() + " Assistant : " + table.getIdHelper());
			}
		}
	}

	public void displayServersList(List<UserEntity> users) {
		for (UserEntity user : users) {
			if (user.getRole().equals(Role.Server) || user.getRole().equals(Role.Helper)) {
				System.out.println(" - " + user.get_id() + " -> " + user.getRole().getValue());
			}
		}
	}

	public void displayAllTables(List<TableEntity> tables) {
		for (TableEntity table : tables) {
			System.out.println(" - Id: " + table.get_id() + " Nb places: " + table.getNbSeats() + " Serveur : "
					+ table.getIdServer() + " Assistant : " + table.getIdHelper());
		}
	}

	public void displayAllTablesWithNoBooking(List<TableEntity> tables, String date, MealType mealType) {
		for (TableEntity table : tables) {
			if (tableIsReserved(table, date, mealType) == null) {
				System.out.println(" - Id: " + table.get_id() + " Nb places: " + table.getNbSeats());
			}
		}
	}

	public int displayAllTablesWithBooking(List<TableEntity> tables, String date, MealType mealType) {
		int nbBooking = 0;
		for (TableEntity table : tables) {
			String name = tableIsReserved(table, date, mealType);
			if (name != null) {
				System.out.println(" - Id: " + table.get_id() + " Nb places: " + table.getNbSeats() + " Nom : " + name);
				nbBooking++;
			}
		}
		return nbBooking;
	}

	private String tableIsReserved(TableEntity table, String date, MealType mealType) {
		List<BookingEntity> bookings = Launcher.getBaseService().getAllBooking();
		for (BookingEntity booking : bookings) {
			if (table.get_id().equals(booking.getIdTable()) && booking.getMealType().equals(mealType)
					&& booking.getDate().equals(date)) {
				return booking.getReservationName();
			}
		}
		return null;
	}

	public void displayIsABill() {
		System.out.println("Est ce une réservation ? \n 0) Non \n 1) Oui ");
	}

	public void displayChoiceServer() {
		System.out.println("Choisissez le serveur que vous voulez affecter à une table.");
	}

	public void displayChoiceTableServer(String userId) {
		System.out.println("Choisissez la table à affecter à " + userId + ". Entrez l'Id de la table.");
	}

	public void displayChoiceTableClient() {
		System.out.println("Choisissez la table pour placer vos clients. Entrez l'Id de la table.");
	}

	public void displayActionSucceded() {
		System.out.println("L'action a été effectué avec succès.");
	}

	public void displayActionFailed() {
		System.out.println("L'action n'a pas pu aboutir.");
	}

	public void displayBookingDate() {
		System.out.println("Entrez la date de réservation au format AAAA/MM/JJ.");
	}

	public void displayBookingService() {
		System.out.println("Pour quel service est la réservation? \n0) Dejeuner \n1) Diner");
	}

	public void displayBookingName() {
		System.out.println("Entrez le nom de réservation.");
	}

	public void displayBookingDiner() {
		System.out.println("La réservation est possible seulement pour le service du diner.");
		System.out.println("Est ce correct ? \n0) Non \n1) Oui");
	}

	public void displayBookingImpossible() {
		System.out.println("Impossible de créer une réservation pour aujourd'hui.");
	}

}
