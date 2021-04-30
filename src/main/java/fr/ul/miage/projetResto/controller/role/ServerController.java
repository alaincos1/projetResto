package fr.ul.miage.projetResto.controller.role;

import fr.ul.miage.projetResto.Launcher;
import fr.ul.miage.projetResto.appinfo.Service;
import fr.ul.miage.projetResto.constants.Role;
import fr.ul.miage.projetResto.controller.feature.LogInController;
import fr.ul.miage.projetResto.dao.service.BaseService;
import fr.ul.miage.projetResto.view.feature.LogInView;
import fr.ul.miage.projetResto.view.role.DirectorView;
import fr.ul.miage.projetResto.view.role.ServerView;
import lombok.AllArgsConstructor;

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
                viewTables();
                break;
            case 2:
                setTablesDirty();
                break;
            case 3:
                takeOrders();
                break;
            case 4:
                serveOrders();
                break;
        }
    }

    protected void viewTables() {
    }

    protected void setTablesDirty() {
    }

    protected void takeOrders() {
    }

    private void serveOrders() {
    }
}
