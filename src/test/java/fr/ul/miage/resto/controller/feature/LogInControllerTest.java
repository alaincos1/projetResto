package fr.ul.miage.resto.controller.feature;

import fr.ul.miage.resto.Launcher;
import fr.ul.miage.resto.dao.service.BaseService;
import fr.ul.miage.resto.model.entity.UserEntity;
import fr.ul.miage.resto.view.feature.LogInView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

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

        verify(logInView, times(0)).displayLogInError();
    }

    @Test
    @DisplayName("Demande de l'id du user incorrecte")
    void testAskUserIdIncorrect() {
        doReturn("dir").when(logInController).getUserIdInput();
        when(baseService.getUserById(anyString())).thenReturn(null);
        doCallRealMethod().doNothing().when(logInController).askUserId();
        logInController.askUserId();

        verify(logInView, times(1)).displayLogInError();
    }

    @Test
    @DisplayName("Déconnexion")
    void testDisconnect() {
        doNothing().when(logInController).launch();

        logInController.disconnect();

        verify(logInView, times(1)).displayDisconnect();
        verify(logInController, times(1)).launch();
        assertNull(Launcher.getLoggedUser());
    }
}
