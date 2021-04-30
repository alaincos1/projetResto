package fr.ul.miage.projetResto.controller.role;

import fr.ul.miage.projetResto.appinfo.Service;
import fr.ul.miage.projetResto.constants.Role;
import fr.ul.miage.projetResto.controller.feature.LogInController;
import fr.ul.miage.projetResto.dao.service.BaseService;
import fr.ul.miage.projetResto.view.feature.LogInView;
import fr.ul.miage.projetResto.view.role.*;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DirectorController extends RoleMenuController {
    private final BaseService baseService;
    private final Service service;
    private final DirectorView directorView;

    @Override
    public void callAction(Integer action) {
        ButlerController butlerController = new ButlerController(baseService, service, new ButlerView());
        ServerController serverController = new ServerController(baseService, service, new ServerView());
        HelperController helperController = new HelperController(baseService, service, new HelperView());
        CookController cookController = new CookController(baseService, service, new CookView());
        switch (action) {
            case 0:
                LogInController logInController = new LogInController(baseService, service, new LogInView());
                logInController.disconnect();
                break;
            case 1:
                manageEmployees();
                break;
            case 2:
                manageDayMenu();
                break;
            case 3:
                manageStocks();
                break;
            case 4:
                analysesIncomes();
                break;
            case 5:
                analysesPerformances();
                break;
            case 6:
                endService();
                break;
            case 7:
                butlerController.launch(Role.Butler);
                break;
            case 8:
                serverController.launch(Role.Server);
                break;
            case 9:
                helperController.launch(Role.Helper);
                break;
            case 10:
                cookController.launch(Role.Cook);
                break;
        }
    }

    private void manageEmployees() {
    }

    private void manageDayMenu() {
    }

    private void manageStocks() {
    }

    private void analysesIncomes() {
    }

    private void analysesPerformances() {
    }

    private void endService() {
    }
}
