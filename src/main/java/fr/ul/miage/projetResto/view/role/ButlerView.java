package fr.ul.miage.projetResto.view.role;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import fr.ul.miage.projetResto.constants.Role;
import fr.ul.miage.projetResto.constants.TableState;
import fr.ul.miage.projetResto.model.entity.OrderEntity;
import fr.ul.miage.projetResto.model.entity.TableEntity;
import fr.ul.miage.projetResto.model.entity.UserEntity;

public class ButlerView extends RoleView {

	public void displayTablesList(List<TableEntity> tables, String serverId, String tableState) {
		for (TableEntity table : tables) {
			if (serverId == null && table.getTableState().getState().equals(tableState)) {
				System.out.println(" - Id: " + table.get_id() + " Nb places: " + table.getNbSeats() + " Serveur : "
						+ table.getIdServer() + " Assistant : " + table.getIdHelper());
			} else if (serverId != null && !serverId.equals(table.getIdServer())) {
				System.out.println(" - Id: " + table.get_id() + " Nb places: " + table.getNbSeats() + " Serveur : "
						+ table.getIdServer() + " Assistant : " + table.getIdHelper());
			}
		}
	}

	public void displayServersList(List<UserEntity> users) {
		for (UserEntity user : users) {
			if (user.getRole().equals(Role.Server)) {
				System.out.println(" - " + user.get_id());
			}
		}
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
		System.out.println("Choisissez la table pour placer vos clients.");
	}

}
