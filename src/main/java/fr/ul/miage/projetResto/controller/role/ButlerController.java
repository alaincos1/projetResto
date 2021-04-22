package fr.ul.miage.projetResto.controller.role;

import fr.ul.miage.projetResto.Launcher;
import fr.ul.miage.projetResto.constants.Role;
import fr.ul.miage.projetResto.controller.feature.LogInController;
import fr.ul.miage.projetResto.view.role.ButlerView;

public class ButlerController extends RoleMenuController {
    private ButlerView butlerView = new ButlerView();

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
    }

    protected void editBills() {
    }

    protected void affectTablesToClients() {
    }

    protected void takeBookings() {
    }
}
