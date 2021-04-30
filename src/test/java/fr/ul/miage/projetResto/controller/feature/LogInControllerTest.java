package fr.ul.miage.projetResto.controller.feature;

import fr.ul.miage.projetResto.Launcher;
import fr.ul.miage.projetResto.appinfo.Service;
import fr.ul.miage.projetResto.dao.service.BaseService;
import fr.ul.miage.projetResto.model.entity.UserEntity;
import fr.ul.miage.projetResto.view.feature.LogInView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@DisplayName("LogInController")
@ExtendWith(MockitoExtension.class)
class LogInControllerTest {
    @Mock
    LogInView logInView;
    @Mock
    BaseService baseService;
    @Mock
    Service service;
    @Spy
    @InjectMocks
    LogInController logInController;

    @Test
    @DisplayName("Demande de l'id du user")
    void askUserId() {
        doReturn("dir").when(logInController).getUserIdInput();
        when(baseService.getUserById(anyString())).thenReturn(new UserEntity());

        logInController.askUserId();

        verify(logInView, times(0)).displayLogInError();
    }

    @Test
    @DisplayName("Demande de l'id du user incorrecte")
    void askUserIdIncorrect() {
        doReturn("dir").when(logInController).getUserIdInput();
        when(baseService.getUserById(anyString())).thenReturn(null);
        doCallRealMethod().doNothing().when(logInController).askUserId();
        logInController.askUserId();

        verify(logInView, times(1)).displayLogInError();
    }

    @Test
    @DisplayName("Déconnexion")
    void disconnect() {
        doNothing().when(logInController).launch();

        logInController.disconnect();

        verify(logInView, times(1)).displayDisconnect();
        verify(logInController, times(1)).launch();
        assertNull(Launcher.getLoggedUser());
    }
}
