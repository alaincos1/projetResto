package fr.ul.miage.resto.controller.feature;

import fr.ul.miage.resto.Launcher;
import fr.ul.miage.resto.constants.Role;
import fr.ul.miage.resto.controller.role.RoleController;
import fr.ul.miage.resto.dao.service.BaseService;
import fr.ul.miage.resto.model.entity.UserEntity;
import fr.ul.miage.resto.utils.InputUtil;
import fr.ul.miage.resto.view.feature.LogInView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@DisplayName("LogInController")
@ExtendWith(MockitoExtension.class)
class LogInControllerTest {
    @Mock
    LogInView logInView;
    @Mock
    BaseService baseService;
    @Spy
    @InjectMocks
    LogInController logInController;

    @Test
    @DisplayName("Demande de l'id du user")
    void testAskUserId() {
        doReturn("dir").when(logInController).getUserIdInput();
        when(baseService.getUserById(anyString())).thenReturn(new UserEntity());

        logInController.askUserId();

        verify(logInView, times(0)).displayMessage("Utilisateur inconnu, veuillez recommencer.");
    }

    @Test
    @DisplayName("Demande de l'id du user incorrecte")
    void testAskUserIdIncorrect() {
        doReturn("dir").when(logInController).getUserIdInput();
        when(baseService.getUserById(anyString())).thenReturn(null);
        doCallRealMethod().doNothing().when(logInController).askUserId();
        logInController.askUserId();

        verify(logInView, times(1)).displayMessage("Utilisateur inconnu, veuillez recommencer.");
    }

    @Test
    @DisplayName("Déconnexion")
    void testDisconnect() {
        doNothing().when(logInController).launch();

        logInController.disconnect();

        verify(logInView, times(1)).displayMessage("Vous êtes déconnecté.e");
        verify(logInController, times(1)).launch();
        assertNull(Launcher.getLoggedUser());
    }

    @ParameterizedTest
    @ValueSource(strings = {"DIRECTOR", "COOK", "SERVER", "HELPER", "BUTLER"})
    @DisplayName("Appelle la méthode launch du controlleur 1")
    void testConnectUserAccordingRole(String arg) {
        Role role = Role.valueOf(arg);
        doNothing().when(logInController).launchController(any(), eq(role));

        logInController.connectUserAccordingRole(role);

        verify(logInController, times(1)).launchController(any(), eq(role));
    }

    @Test
    @DisplayName("Appelle la méthode launch du controlleur 2")
    void testLaunchController(@Mock RoleController roleController) {
        doNothing().when(roleController).launch(any(Role.class));

        logInController.launchController(roleController, Role.DIRECTOR);

        verify(roleController, times(1)).launch(Role.DIRECTOR);
    }

    @ParameterizedTest
    @ValueSource(strings = {"DIRECTOR", "COOK", "SERVER", "HELPER", "BUTLER"})
    @DisplayName("Lance le log in controlleur ")
    void testLaunch(String arg) {
        try (MockedStatic<Launcher> utilities = Mockito.mockStatic(Launcher.class)) {
            Role role = Role.valueOf(arg);
            UserEntity user = new UserEntity();
            user.setId("user");
            user.setRole(role);

            utilities.when(Launcher::getLoggedUser)
                    .thenReturn(user);

            doNothing().when(logInController).askUserId();
            doNothing().when(logInController).connectUserAccordingRole(role);

            logInController.launch();

            verify(logInController, times(1)).askUserId();
            verify(logInController, times(1)).connectUserAccordingRole(role);
        }
    }

    @Test
    @DisplayName("Retourne une entrée user id")
    void testGetUserIdInput() {
        try (MockedStatic<InputUtil> utilities = Mockito.mockStatic(InputUtil.class)) {
            utilities.when(InputUtil::getUserIdInput)
                    .thenReturn("user");

            assertEquals("user", logInController.getUserIdInput());
        }
    }
}
