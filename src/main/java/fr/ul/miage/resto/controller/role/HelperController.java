package fr.ul.miage.resto.controller.role;

import fr.ul.miage.resto.Launcher;
import fr.ul.miage.resto.appinfo.Service;
import fr.ul.miage.resto.constants.Role;
import fr.ul.miage.resto.constants.TableState;
import fr.ul.miage.resto.dao.service.BaseService;
import fr.ul.miage.resto.model.entity.TableEntity;
import fr.ul.miage.resto.model.entity.UserEntity;
import fr.ul.miage.resto.view.role.HelperView;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class HelperController extends RoleController {
    private final BaseService baseService;
    private final Service service;
    private final HelperView helperView;

    @Override
    public void callAction(Integer action) {
        Role role = Launcher.getLoggedUser().getRole();
        switch (action) {
            case 0:
                goBackOrDisconnect(role, baseService, service);
                break;
            case 1:
                viewTables(Launcher.getLoggedUser());
                break;
            case 2:
                cleanTables(Launcher.getLoggedUser());
                break;
            default:
                break;
        }
    }

    protected void viewTables(UserEntity user) {
        List<TableEntity> tables;
        if (Role.DIRECTOR.equals(user.getRole())) {
            tables = baseService.getAllTables();
        } else {
            tables = baseService.getAllTableByServerOrHelper(user.getId());
        }

        if (tables.isEmpty()) {
            helperView.displayNoTablesAffected();
        } else {
            helperView.displayTablesAffected(tables);
        }
        askMainMenu();
    }

    protected void cleanTables(UserEntity user) {
        List<TableEntity> tablesToClean;
        if (Role.DIRECTOR.equals(user.getRole())) {
            tablesToClean = baseService.getAllTableByState(TableState.DIRTY);
        } else {
            tablesToClean = baseService.getAllTableByServerOrHelperAndState(user.getId(), TableState.DIRTY);
        }

        if (tablesToClean.isEmpty()) {
            helperView.displayMessage("Aucune table à nettoyer.");
        } else {
            helperView.displayTablesToClean(tablesToClean);
            int choice = getIntegerInput(0, tablesToClean.size()) - 1;
            if (choice != -1) {
                tablesToClean.get(choice).setTableState(TableState.FREE);
                if (baseService.update(tablesToClean.get(choice))) {
                    helperView.displayMessage("Table nettoyée ! Voulez vous en nettoyer une autre ?" +
                            "\n 0) Non" +
                            "\n 1) Oui");
                    if (doAgain()) {
                        cleanTables(user);
                    }
                } else {
                    helperView.displayError();
                }
            }
        }
        launch(Role.HELPER);
    }
}
