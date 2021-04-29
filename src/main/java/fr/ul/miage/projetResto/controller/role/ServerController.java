package fr.ul.miage.projetResto.controller.role;

import fr.ul.miage.projetResto.Launcher;
import fr.ul.miage.projetResto.appinfo.Service;
import fr.ul.miage.projetResto.constants.Role;
import fr.ul.miage.projetResto.controller.feature.LogInController;
import fr.ul.miage.projetResto.dao.service.BaseService;
import fr.ul.miage.projetResto.view.role.ServerView;

public class ServerController extends RoleMenuController {
    private BaseService baseService;
    private Service service;
    ServerView serverView = new ServerView();

    public ServerController(BaseService baseService, Service service){
        this.baseService = baseService;
        this.service = service;
    }

    @Override
    public void callAction(Integer action) {
        Role role = Launcher.getLoggedUser().getRole();
        switch (action) {
            case 0:
                if (role.equals(Role.Director)) {
                    DirectorController directorController = new DirectorController(baseService, service);
                    directorController.launch(Role.Director);
                } else {
                    LogInController logInController = new LogInController(baseService, service);
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
