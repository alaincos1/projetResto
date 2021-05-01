package fr.ul.miage.projetResto.controller.role;

import fr.ul.miage.projetResto.Launcher;
import fr.ul.miage.projetResto.appinfo.Service;
import fr.ul.miage.projetResto.constants.Role;
import fr.ul.miage.projetResto.controller.feature.LogInController;
import fr.ul.miage.projetResto.dao.service.BaseService;
import fr.ul.miage.projetResto.model.entity.TableEntity;
import fr.ul.miage.projetResto.model.entity.UserEntity;
import fr.ul.miage.projetResto.view.feature.LogInView;
import fr.ul.miage.projetResto.view.role.DirectorView;
import fr.ul.miage.projetResto.view.role.HelperView;
import fr.ul.miage.projetResto.view.role.ServerView;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class HelperController extends RoleMenuController {
    private final BaseService baseService;
    private final Service service;
    private final HelperView helperView;

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
                cleanTables();
                break;
        }
    }

    protected void viewTables(UserEntity user) {
        List<TableEntity> tables = baseService.getAllTableByServerOrHelper(user.get_id());
        if(tables.isEmpty()){
            helperView.displayNoTablesAffected();
        }else{
            helperView.displayTablesAffected(tables);
        }
        askMainMenu();
    }

    protected void cleanTables() {
    }
}
