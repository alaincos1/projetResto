package fr.ul.miage.resto.view.role;

import fr.ul.miage.resto.constants.MealType;
import fr.ul.miage.resto.constants.Role;
import fr.ul.miage.resto.constants.TableState;
import fr.ul.miage.resto.dao.service.BaseService;
import fr.ul.miage.resto.model.entity.BookingEntity;
import fr.ul.miage.resto.model.entity.OrderEntity;
import fr.ul.miage.resto.model.entity.TableEntity;
import fr.ul.miage.resto.model.entity.UserEntity;

import java.util.List;

import org.codehaus.plexus.util.StringUtils;

public class ButlerView extends RoleView {

	public int displayTablesList(List<TableEntity> tables, String userId, String tableState) {
		int nbTables = 0;
		for (TableEntity table : tables) {
			if (StringUtils.isBlank(userId) && table.getTableState().getState().equals(tableState)) {
				System.out.println(" - Id: " + table.get_id() + " Nb places: " + table.getNbSeats() + " Serveur : "
						+ table.getIdServer() + " Assistant : " + table.getIdHelper());
				nbTables++;
			} else if (StringUtils.isNotBlank(userId) && !userId.equals(table.getIdServer()) && !userId.equals(table.getIdHelper())) {
				System.out.println(" - Id: " + table.get_id() + " Nb places: " + table.getNbSeats() + " Serveur : "
						+ table.getIdServer() + " Assistant : " + table.getIdHelper());
				nbTables++;
			}
		}
		return nbTables;
	}

	public void displayServersList(List<UserEntity> users) {
		for (UserEntity user : users) {
			if (user.getRole().equals(Role.SERVER) || user.getRole().equals(Role.HELPER)) {
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
	
	

	public void displayAllTablesWithNoBooking(List<TableEntity> tables, String date, MealType mealType,
			BaseService baseService) {
		for (TableEntity table : tables) {
			if (tableIsReserved(table, date, mealType, baseService) == null) {
				System.out.println(" - Id: " + table.get_id() + " Nb places: " + table.getNbSeats());
			}
		}
	}

	public int displayAllTablesWithBooking(List<TableEntity> tables, String date, MealType mealType,
			BaseService baseService) {
		int nbBooking = 0;
		for (TableEntity table : tables) {
			String name = tableIsReserved(table, date, mealType, baseService);
			if (StringUtils.isNotBlank(name) && table.getTableState().equals(TableState.BOOKED)) {
				System.out.println(" - Id: " + table.get_id() + " Nb places: " + table.getNbSeats() + " Nom : " + name);
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

	public void displayAnyBooking() {
		System.out.println("Il n'y a pas de réservation ce jour.");
	}

	public void displayInputIncorrect() {
		System.out.println("Entrée incorrecte, veuillez recommencer.");
	}

	public void displayEndNewClient() {
		System.out.println(
				"Malheureusement nous n'acceptons plus de nouveaux clients mais vous pouvez réserver ou alors revenir demain :)");
	}

	public int displayAllTablesForBill(List<TableEntity> tables, BaseService baseService) {
		int t = 0;
		for (TableEntity table : tables) {
			if (stateForBill(table)) {
				if (orderServed(table, baseService)) {
					System.out.println(" - Id: " + table.get_id() + " Nb places: " + table.getNbSeats());
					t++;
				}
			}
		}
		return t;
	}
	
	public void displayPriveBill(Integer price) {
		System.out.println("Le prix total de cette facture est " + price + " euros.");
	}
	
	public void displayBillImpossible() {
		System.out.println("Aucun facture ne peut-être éditée.");
	}
	
	public void displayChoiceTableForBill() {
		System.out.println("Choisissez la table pour éditer sa facture. Entrez son id.");
	}
	
	/**
	 * Savoir si l'état de la table est correct pour éditer la facture
	 * @param table
	 * @return bollean 
	 */
	public Boolean stateForBill(TableEntity table) {
		if (table.getTableState() == TableState.BOOKED || table.getTableState() == TableState.FREE
				|| table.getTableState() == TableState.OCCUPIED) {
			return false;
		}
		return true;
	}

	/**
	 * Savoir si des commandes ont été servies à cette table
	 * @param table
	 * @param baseService
	 * @return boolean
	 */
	public Boolean orderServed(TableEntity table, BaseService baseService) {
		List<OrderEntity> orders = baseService.getServedOrders();
		for (OrderEntity order : orders) {
			if (order.getIdTable().equals(table.get_id())) {
				return true;
			}
		}
		return false;
	}

	public void displayDishes(String id, Integer price) {
		System.out.println("- " + id + " : " + price + " euros");
	}

	public void displayAnyTableFree() {
		System.out.println("Malheureusement, notre restaurant est complet !");
	}

}