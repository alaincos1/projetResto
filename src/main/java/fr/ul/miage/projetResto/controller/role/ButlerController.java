package fr.ul.miage.projetResto.controller.role;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;

import fr.ul.miage.projetResto.Launcher;
import fr.ul.miage.projetResto.constants.MealType;
import fr.ul.miage.projetResto.constants.Role;
import fr.ul.miage.projetResto.constants.TableState;
import fr.ul.miage.projetResto.controller.feature.LogInController;
import fr.ul.miage.projetResto.model.entity.BookingEntity;
import fr.ul.miage.projetResto.model.entity.TableEntity;
import fr.ul.miage.projetResto.model.entity.UserEntity;
import fr.ul.miage.projetResto.utils.InputUtil;
import fr.ul.miage.projetResto.view.role.ButlerView;

public class ButlerController extends RoleMenuController {
	private final ButlerView butlerView = new ButlerView();

	@Override
	public void callAction(Integer action) {
		Role role = Launcher.getLoggedUser().getRole();
		switch (action) {
		case 0:
			if (role.equals(Role.Director)) {
				DirectorController directorController = new DirectorController();
				directorController.launch(Role.Director);
			} else {
				LogInController logInController = new LogInController();
				logInController.disconnect();
			}
			break;
		case 1:
			affectTablesToServer();
			break;
		case 2:
			editBills();
			break;
		case 3:
			affectTablesToClients();
			break;
		case 4:
			takeBookings();
			break;
		}
	}

	protected void affectTablesToServer() {
		List<UserEntity> users = Launcher.getBaseService().getAllUser();
		List<TableEntity> tables = Launcher.getBaseService().getAllTable();
		butlerView.displayAllTables(tables);
		butlerView.displayChoiceServer();
		butlerView.displayServersList(users);

		String choiceUser = InputUtil.getUserIdInput();
		UserEntity user = Launcher.getBaseService().getUserById(choiceUser);
		String choiceTable = "";
		if (!StringUtils.isBlank(choiceUser) && user == null) {
			System.out.println("Utilisateur inconnu, veuillez recommencer.");
			affectTablesToServer();
		} else {
			butlerView.displayChoiceTableServer(choiceUser);
			butlerView.displayTablesList(tables, choiceUser, null);
			choiceTable = choiceTableServer(user);
		}

		TableEntity tableToChange = Launcher.getBaseService().getTableById(choiceTable);

		if (user.getRole().equals(Role.Server)) {
			tableToChange.setIdServer(choiceUser);
		} else {
			tableToChange.setIdHelper(choiceUser);
		}

		updateObject(tableToChange);
	}

	protected void editBills() {
	}

	protected void affectTablesToClients() {
		List<TableEntity> tables = Launcher.getBaseService().getAllTable();

		butlerView.displayIsABill();

		Integer reservation = InputUtil.getIntegerInput(0, 1);
		String choiceTable = choiceReservation(tables, reservation);

		// si il ne veut pas de réservation ou qu'il n'y en a pas pour ce jour
		if ((reservation == 1 && choiceTable == null) || reservation == 0) {
			butlerView.displayTablesList(tables, null, TableState.Free.getState());
			butlerView.displayChoiceTableClient();
			choiceTable = choiceTable(TableState.Free);
		}

		TableEntity tableToChange = Launcher.getBaseService().getTableById(choiceTable);
		tableToChange.setTableState(TableState.Occupied);

		updateObject(tableToChange);
	}

	protected void takeBookings() {
		butlerView.displayBookingDate();
		String dateBooking = InputUtil.getDateInput();


		MealType mealTypeBooking = choiceMealTypeWithDate(dateBooking);		

		butlerView.displayBookingName();
		String nameBooking = InputUtil.getStringInput();

		List<TableEntity> tables = Launcher.getBaseService().getAllTable();
		butlerView.displayAllTablesWithNoBooking(tables, dateBooking, mealTypeBooking);
		butlerView.displayChoiceTableClient();
		String choiceTable = choiceTable(null);

		BookingEntity booking = new BookingEntity();
//		booking.set_id(new ObjectId().toString());
		booking.setDate(dateBooking);
		booking.setIdTable(choiceTable);
		booking.setMealType(mealTypeBooking);
		booking.setReservationName(nameBooking);

		saveObject(booking);
	}

	//Fonction qui demande le service en fonction de la date choisie
	private MealType choiceMealTypeWithDate(String dateBooking) {
		MealType mealTypeBooking = null;
		
		//Si la reservation est pour la date du jour et qu'on est au service du diner alors la reservation est impossible
		if (dateBooking.equals(Launcher.getService().getDate())
				&& Launcher.getService().getMealType() == MealType.Dîner) {
			butlerView.displayBookingImpossible();
			launch(Role.Butler);
		} 
		//Si la reservation est pour le jour et qu'on est au service du dejeuner alors la reservation est automatique pour le soir
		else if (dateBooking.equals(Launcher.getService().getDate())) {
			butlerView.displayBookingDiner();
			Integer correct = InputUtil.getIntegerInput(0, 1);
			if (correct == 0) {
				launch(Role.Butler);
			}else {
				mealTypeBooking = MealType.Dîner;
			}
		} 
		//Si c'est pour un date ultérieur on deande le service
		else {
			butlerView.displayBookingService();
			Integer choiceMealType = InputUtil.getIntegerInput(0, 1);
			mealTypeBooking = choiceMealType(choiceMealType);
		}
		return mealTypeBooking;
	}
	
	//FOnction qui permet de définir le service en fonction d l'entrée de l'utilisateur 
	private MealType choiceMealType(Integer choiceMealType) {
		MealType mealTypeBooking;
		if (choiceMealType == 0) {
			mealTypeBooking = MealType.Déjeuner;
		} else {
			mealTypeBooking = MealType.Dîner;

		}
		return mealTypeBooking;
	}

	//Fonction qui permet de savoir si la table existe en focntion de son id et si sont état est correct
	//Par exemple savoir si le table avec l'id 1 existe et est libre 
	private boolean isTableIdCorrect(String tableId, TableState state) {
		TableEntity table = Launcher.getBaseService().getTableById(tableId);
		if (table != null && (state == null || table.getTableState() == state)) {
			return true;
		}
		return false;
	}

	//Retourne l'id de la table si elle existe et est correcte d'apres l'entrée de l'utilisateur en fonction de l'etat de la table
	private String choiceTable(TableState state) {
		String choiceTable = InputUtil.getStringInput();

		if (!StringUtils.isBlank(choiceTable.toString()) && !isTableIdCorrect(choiceTable, state)) {
			butlerView.displayInputIncorrect();
			return choiceTable(state);
		}
		return choiceTable;
	}

	//Retourne l'id de la table si elle existe et est correct vis à vis du serveur/assistant en charge de celle ci 
	private String choiceTableServer(UserEntity user) {
		String choiceTable = InputUtil.getUserIdInput();

		if (!StringUtils.isBlank(choiceTable.toString()) && !isTableIdCorrectServer(choiceTable, user)) {
			butlerView.displayInputIncorrect();
			return choiceTableServer(user);
		}
		return choiceTable;
	}

	
	private boolean isTableIdCorrectServer(String tableId, UserEntity user) {
		TableEntity table = Launcher.getBaseService().getTableById(tableId);
		if (table != null && !table.getIdServer().equals(user.get_id()) && !table.getIdHelper().equals(user.get_id())) {
			return true;
		}
		return false;
	}

	//retourne l'id de la table qui correspond à l'entrée de l'utilisateur
	//la table est une table réservée
	private String choiceReservation(List<TableEntity> tables, Integer reservation) {
		String choiceTable = null;
		if (reservation == 1) {
			if (butlerView.displayAllTablesWithBooking(tables, Launcher.getService().getDate(),
					Launcher.getService().getMealType()) != 0) {
				butlerView.displayChoiceTableClient();
				choiceTable = choiceTable(TableState.Booked);
			} else {
				butlerView.displayAnyBooking();
			}
		}
		return choiceTable;
	}

	//Mettre à jour un objet (table...)
	private void updateObject(Object o) {
		if (Launcher.getBaseService().update(o)) {
			butlerView.displayActionSucceded();
		} else {
			butlerView.displayActionFailed();
		}
		launch(Role.Butler);
	}

	//Insere un objet (facture, réservation..)
	private void saveObject(Object o) {
		if (Launcher.getBaseService().save(o)) {
			butlerView.displayActionSucceded();
		} else {
			butlerView.displayActionFailed();
		}
		launch(Role.Butler);
	}
}
