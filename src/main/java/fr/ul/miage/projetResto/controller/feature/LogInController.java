package fr.ul.miage.projetResto.controller.feature;

import fr.ul.miage.projetResto.Launcher;
import fr.ul.miage.projetResto.constants.Role;
import fr.ul.miage.projetResto.controller.role.*;
import fr.ul.miage.projetResto.error.InputError;
import fr.ul.miage.projetResto.model.entity.UserEntity;
import fr.ul.miage.projetResto.utils.InputUtil;
import fr.ul.miage.projetResto.view.feature.LogInView;
import org.apache.commons.lang3.StringUtils;

public class LogInController {
    private final LogInView logInView = new LogInView();

    public void launch() {
        askUserId();
        connectUserAccordingRole();
    }

    public void askUserId() {
        logInView.displayLogIn();
        String input = null;
        while (StringUtils.isBlank(input)) {
            input = InputError.checkUserId(InputUtil.getUserInput());
            if (StringUtils.isBlank(input)) {
                System.out.println("Problème de saisie, veuillez recommencer.");
            } else if (!StringUtils.isBlank(input) && !isUserIdCorrect(input)) {
                input = null;
                System.out.println("Utilisateur inconnu, veuillez recommencer.");
            }
        }
    }

    private boolean isUserIdCorrect(String userId) {
        UserEntity user = Launcher.getBaseService().getUserById(userId);
        if (user != null) {
            Launcher.setLoggedUser(user);
            return true;
        }
        return false;
    }

    private void connectUserAccordingRole() {
        RoleMenuController controller = null;
        Role role = Launcher.getLoggedUser().getRole();
        switch (role) {
            case Director:
                controller = new DirectorController();
                break;
            case Butler:
                controller = new ButlerController();
                break;
            case Cook:
                controller = new CookController();
                break;
            case Server:
                controller = new ServerController();
                break;
            case Helper:
                controller = new HelperController();
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
