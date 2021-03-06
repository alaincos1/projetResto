package fr.ul.miage.resto.controller.feature;

import fr.ul.miage.resto.Launcher;
import fr.ul.miage.resto.appinfo.Service;
import fr.ul.miage.resto.constants.Role;
import fr.ul.miage.resto.controller.role.*;
import fr.ul.miage.resto.dao.service.BaseService;
import fr.ul.miage.resto.model.entity.UserEntity;
import fr.ul.miage.resto.utils.InputUtil;
import fr.ul.miage.resto.view.feature.LogInView;
import fr.ul.miage.resto.view.role.*;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class LogInController {
    private final BaseService baseService;
    private final Service service;
    private final LogInView logInView;

    public void launch() {
        askUserId();
        Role role = Launcher.getLoggedUser().getRole();
        connectUserAccordingRole(role);
    }

    public void askUserId() {
        logInView.displayMessage("Veuillez vous connecter avec votre identifiant : ");
        String input = getUserIdInput();
        UserEntity user = baseService.getUserById(input);
        if (user == null) {
            logInView.displayMessage("Utilisateur inconnu, veuillez recommencer.");
            askUserId();
        } else {
            Launcher.setLoggedUser(user);
        }
    }

    protected void connectUserAccordingRole(Role role) {
        RoleController controller;
        switch (role) {
            case DIRECTOR:
                controller = new DirectorController(baseService, service, new DirectorView());
                break;
            case BUTLER:
                controller = new ButlerController(baseService, service, new ButlerView());
                break;
            case COOK:
                controller = new CookController(baseService, service, new CookView());
                break;
            case SERVER:
                controller = new ServerController(baseService, service, new ServerView());
                break;
            case HELPER:
            default:
                controller = new HelperController(baseService, service, new HelperView());
                break;
        }
        launchController(controller, role);
    }

    protected void launchController(RoleController controller, Role role) {
        controller.launch(role);
    }

    public void disconnect() {
        Launcher.setLoggedUser(null);
        logInView.displayMessage("Vous ??tes d??connect??.e");
        launch();
    }

    public String getUserIdInput() {
        return InputUtil.getUserIdInput();
    }

}
