package fr.ul.miage.projetResto.controller.role;

import fr.ul.miage.projetResto.Launcher;
import fr.ul.miage.projetResto.constants.Role;
import fr.ul.miage.projetResto.controller.feature.LogInController;
import fr.ul.miage.projetResto.view.role.HelperView;

public class HelperController extends RoleMenuController {
    HelperView helperView = new HelperView();

    @Override
    public void callAction(Integer action) {
        Role role = Launcher.getLoggedUser().getRole();
        switch(action){
            case 0:
                if(role.equals(Role.Director)){
                    DirectorController directorController = new DirectorController();
                    directorController.launch(Role.Director);
                }else {
                    LogInController logInController = new LogInController();
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
