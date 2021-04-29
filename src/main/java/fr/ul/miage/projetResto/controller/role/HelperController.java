package fr.ul.miage.projetResto.controller.role;

import fr.ul.miage.projetResto.Launcher;
import fr.ul.miage.projetResto.appinfo.Service;
import fr.ul.miage.projetResto.constants.Role;
import fr.ul.miage.projetResto.controller.feature.LogInController;
import fr.ul.miage.projetResto.dao.service.BaseService;
import fr.ul.miage.projetResto.view.role.HelperView;

public class HelperController extends RoleMenuController {
    private BaseService baseService;
    private Service service;
    HelperView helperView = new HelperView();

    public HelperController(BaseService baseService, Service service){
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
                cleanTables();
                break;
        }
    }

    protected void viewTables() {
    }

    protected void cleanTables() {
    }
}
