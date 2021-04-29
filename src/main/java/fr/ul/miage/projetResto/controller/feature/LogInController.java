package fr.ul.miage.projetResto.controller.feature;

import fr.ul.miage.projetResto.Launcher;
import fr.ul.miage.projetResto.appinfo.Service;
import fr.ul.miage.projetResto.constants.Role;
import fr.ul.miage.projetResto.controller.role.*;
import fr.ul.miage.projetResto.dao.service.BaseService;
import fr.ul.miage.projetResto.model.entity.UserEntity;
import fr.ul.miage.projetResto.utils.InputUtil;
import fr.ul.miage.projetResto.view.feature.LogInView;
import org.apache.commons.lang3.StringUtils;

public class LogInController {
    private BaseService baseService;
    private Service service;
    private final LogInView logInView = new LogInView();

    public LogInController(BaseService baseService, Service service){
        this.baseService = baseService;
        this.service = service;
    }

    public void launch() {
        askUserId();
        connectUserAccordingRole();
    }

    public void askUserId() {
        logInView.displayLogIn();
        String input = InputUtil.getUserIdInput();
        UserEntity user = baseService.getUserById(input);
        if (!StringUtils.isBlank(input) && user == null) {
            logInView.displayLogInError();
            askUserId();
        } else if (user != null) {
            Launcher.setLoggedUser(user);
        }
    }

    private void connectUserAccordingRole() {
        RoleMenuController controller = null;
        Role role = Launcher.getLoggedUser().getRole();
        switch (role) {
            case Director:
                controller = new DirectorController(baseService, service);
                break;
            case Butler:
                controller = new ButlerController(baseService, service);
                break;
            case Cook:
                controller = new CookController(baseService, service);
                break;
            case Server:
                controller = new ServerController(baseService, service);
                break;
            case Helper:
                controller = new HelperController(baseService, service);
                break;
        }
        controller.launch(role);
    }

    public void disconnect() {
        Launcher.setLoggedUser(null);
        logInView.displayDisconnect();
        launch();
    }

}
