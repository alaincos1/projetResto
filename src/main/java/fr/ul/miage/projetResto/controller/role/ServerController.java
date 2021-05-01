package fr.ul.miage.projetResto.controller.role;

import fr.ul.miage.projetResto.Launcher;
import fr.ul.miage.projetResto.appinfo.Service;
import fr.ul.miage.projetResto.constants.Role;
import fr.ul.miage.projetResto.constants.TableState;
import fr.ul.miage.projetResto.controller.feature.LogInController;
import fr.ul.miage.projetResto.dao.service.BaseService;
import fr.ul.miage.projetResto.model.entity.TableEntity;
import fr.ul.miage.projetResto.model.entity.UserEntity;
import fr.ul.miage.projetResto.view.feature.LogInView;
import fr.ul.miage.projetResto.view.role.DirectorView;
import fr.ul.miage.projetResto.view.role.ServerView;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class ServerController extends RoleMenuController {
    private final BaseService baseService;
    private final Service service;
    private final ServerView serverView;

    @Override
    public void callAction(Integer action) {
        Role role = Launcher.getLoggedUser().getRole();
        switch (action) {
            case 0:
                if (role.equals(Role.Director)) {
                    DirectorController directorController = new DirectorController(baseService, service, new DirectorView());
                    directorController.launch(Role.Director);
                } else {
                    LogInController logInController = new LogInController(baseService, service, new LogInView());
                    logInController.disconnect();
                }
                break;
            case 1:
                viewTables(Launcher.getLoggedUser());
                break;
            case 2:
                setTablesDirty(Launcher.getLoggedUser());
                break;
            case 3:
                takeOrders();
                break;
            case 4:
                serveOrders();
                break;
            default:
                break;
        }
    }

    protected void viewTables(UserEntity user) {
        List<TableEntity> tables;
        if (Role.Director.equals(user.getRole())) {
            tables = baseService.getAllTable();
        } else {
            tables = baseService.getAllTableByServerOrHelper(user.get_id());
        }

        if (tables.isEmpty()) {
            serverView.displayNoTablesAffected();
        } else {
            serverView.displayTablesAffected(tables);
        }
        askMainMenu();
    }

    protected void setTablesDirty(UserEntity user) {
        List<TableEntity> tablesToDirty;
        if (Role.Director.equals(user.getRole())) {
            tablesToDirty = baseService.getAllTableByState(TableState.Occupied);
        } else {
            tablesToDirty = baseService.getAllTableByServerOrHelperAndState(user.get_id(), TableState.Occupied);
        }

        if (tablesToDirty.isEmpty()) {
            serverView.displayNoTablesToDirty();
        } else {
            serverView.displayTablesToDirty(tablesToDirty);
            int choice = getIntegerInput(0, tablesToDirty.size()) - 1;
            if (choice != -1) {
                tablesToDirty.get(choice).setTableState(TableState.Dirty);
                if (baseService.update(tablesToDirty.get(choice))) {
                    serverView.displayTableDirtyDoAgain();
                    if (doAgain()) {
                        setTablesDirty(user);
                    }
                } else {
                    serverView.displayError();
                }
            }
        }
        launch(Role.Server);
    }

    protected void takeOrders() {
    }

    private void serveOrders() {
    }
}
