package fr.ul.miage.projetResto.controller.role;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import fr.ul.miage.projetResto.Launcher;
import fr.ul.miage.projetResto.constants.Role;
import fr.ul.miage.projetResto.constants.TableState;
import fr.ul.miage.projetResto.controller.feature.LogInController;
import fr.ul.miage.projetResto.dao.repository.Mapper;
import fr.ul.miage.projetResto.model.entity.OrderEntity;
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
        butlerView.displayChoiceServer();
        butlerView.displayServersList(users);
        
        String choiceServer = InputUtil.getUserIdInput();
        String choiceTable = "";
        if (!StringUtils.isBlank(choiceServer) && !isUserIdCorrect(choiceServer)) {
            System.out.println("Utilisateur inconnu, veuillez recommencer.");
            affectTablesToServer();
        } else {
        	butlerView.displayChoiceTableServer(choiceServer);
        	butlerView.displayTablesList(tables, choiceServer, null);
        	choiceTable = choiceTable();
        }
        
        TableEntity tableToChange = Launcher.getBaseService().getTableById(choiceTable);
        tableToChange.setIdServer(choiceServer);
        Launcher.getBaseService().update(tableToChange);

        launch(Role.Butler);
    }

	protected void editBills() {
    }

    protected void affectTablesToClients() {
    	List<TableEntity> tables = Launcher.getBaseService().getAllTable();
    	butlerView.displayIsABill();
    	Integer reservation = InputUtil.getIntegerInput(0, 1);
    	butlerView.displayChoiceTableClient();
    	if (reservation == 1) {
    		butlerView.displayTablesList(tables, null, TableState.Booked.getState());
        }else {
        	butlerView.displayTablesList(tables, null, TableState.Free.getState());
        }
    	String choiceTable = choiceTable();
    	
        TableEntity tableToChange = Launcher.getBaseService().getTableById(choiceTable);
        tableToChange.setTableState(TableState.Occupied);
        Launcher.getBaseService().update(tableToChange);
        
        launch(Role.Butler);
    }

    protected void takeBookings() {
    	
    }
    
    private boolean isTableIdCorrect(String tableId) {
        TableEntity table = Launcher.getBaseService().getTableById(tableId);
        if (table != null) {
            return true;
        }
        return false;
    }
    
    private String choiceTable() {
    	String choiceTable = InputUtil.getUserIdInput();
    	
    	if (!StringUtils.isBlank(choiceTable.toString()) && !isTableIdCorrect(choiceTable)) {
            System.out.println("Table inconnue, veuillez recommencer.");
            return choiceTable();
        } 
    	return choiceTable;
    }
    
    private boolean isUserIdCorrect(String userId) {
        UserEntity user = Launcher.getBaseService().getUserById(userId);
        if (user != null) {
            Launcher.setLoggedUser(user);
            return true;
        }
        return false;
    }
}
