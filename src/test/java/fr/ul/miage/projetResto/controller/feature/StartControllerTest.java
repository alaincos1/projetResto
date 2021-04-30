package fr.ul.miage.projetResto.controller.feature;

import fr.ul.miage.projetResto.appinfo.Service;
import fr.ul.miage.projetResto.constants.MealType;
import fr.ul.miage.projetResto.dao.service.BaseService;
import fr.ul.miage.projetResto.model.entity.UserEntity;
import fr.ul.miage.projetResto.view.feature.StartView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@DisplayName("StartController")
@ExtendWith(MockitoExtension.class)
class StartControllerTest {@Mock
    StartView startView;
    @Mock
    BaseService baseService;
    @Mock
    Service service;
    @Spy
    @InjectMocks
    StartController startController;

    @Test
    @DisplayName("Demande le type de repas pour un déjeuner")
    void askMealType1() {
        doReturn(1).when(startController).getIntegerInput(anyInt(), anyInt());

        startController.askMealType();

        assertEquals(MealType.Déjeuner, startController.getMealType());
    }

    @Test
    @DisplayName("Demande le type de repas pour un dîner")
    void askMealType2() {
        doReturn(2).when(startController).getIntegerInput(anyInt(), anyInt());

        startController.askMealType();

        assertEquals(MealType.Dîner, startController.getMealType());
    }

    @Test
    @DisplayName("Demande la date du service")
    void askMealDate() {
        doReturn("2022/09/30").when(startController).getDateInput();

        startController.askMealDate();

        assertEquals("2022/09/30", startController.getDate());
    }

    @Test
    @DisplayName("Créé le service")
    void createService() {
        startController.setMealType(MealType.Déjeuner);
        startController.setDate("2022/09/30");
        startController.createService();

        verify(startView, times(1)).displayService(any(Service.class));
    }
}
