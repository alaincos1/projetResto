package fr.ul.miage.projetResto.controller.feature;

import fr.ul.miage.projetResto.Launcher;
import fr.ul.miage.projetResto.appinfo.Service;
import fr.ul.miage.projetResto.constants.Role;
import fr.ul.miage.projetResto.controller.role.*;
import fr.ul.miage.projetResto.dao.service.BaseService;
import fr.ul.miage.projetResto.model.entity.UserEntity;
import fr.ul.miage.projetResto.utils.InputUtil;
import fr.ul.miage.projetResto.view.feature.LogInView;
import fr.ul.miage.projetResto.view.role.*;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@AllArgsConstructor
public class LogInController {
    private final BaseService baseService;
    private final Service service;
    private final LogInView logInView;

    public void launch() {
        askUserId();
        Role role = Launcher.getLoggedUser().getRole();
        RoleMenuController controller = null;
        connectUserAccordingRole(controller, role);
    }

    public void askUserId() {
        logInView.displayLogIn();
        String input = getUserIdInput();
        UserEntity user = baseService.getUserById(input);
        if (user == null) {
            logInView.displayLogInError();
            askUserId();
        } else {
            Launcher.setLoggedUser(user);
        }
    }

    protected void connectUserAccordingRole(RoleMenuController controller, Role role) {
        switch (role) {
            case Director:
                controller = new DirectorController(baseService, service, new DirectorView());
                break;
            case Butler:
                controller = new ButlerController(baseService, service, new ButlerView());
                break;
            case Cook:
                controller = new CookController(baseService, service, new CookView());
                break;
            case Server:
                controller = new ServerController(baseService, service, new ServerView());
                break;
            case Helper:
                controller = new HelperController(baseService, service, new HelperView());
                break;
        }
        controller.launch(role);
    }

    public void disconnect() {
        Launcher.setLoggedUser(null);
        logInView.displayDisconnect();
        launch();
    }

    public String getUserIdInput() {
        return InputUtil.getUserIdInput();
    }

}
