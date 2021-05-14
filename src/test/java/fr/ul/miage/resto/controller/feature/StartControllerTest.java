package fr.ul.miage.resto.controller.feature;

import fr.ul.miage.resto.appinfo.Service;
import fr.ul.miage.resto.constants.MealType;
import fr.ul.miage.resto.view.feature.StartView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@DisplayName("StartController")
@ExtendWith(MockitoExtension.class)
class StartControllerTest {
    @Mock
    StartView startView;
    @Spy
    @InjectMocks
    StartController startController;

    @Test
    @DisplayName("Demande le type de repas pour un déjeuner")
    void testAskMealType1() {
        doReturn(1).when(startController).getIntegerInput(anyInt(), anyInt());

        startController.askMealType();

        assertEquals(MealType.LUNCH, startController.getMealType());
    }

    @Test
    @DisplayName("Demande le type de repas pour un dîner")
    void testAskMealType2() {
        doReturn(2).when(startController).getIntegerInput(anyInt(), anyInt());

        startController.askMealType();

        assertEquals(MealType.DINNER, startController.getMealType());
    }

    @Test
    @DisplayName("Demande la date du service")
    void testAskMealDate() {
        doReturn("2022/09/30").when(startController).getDateInput();

        startController.askMealDate();

        assertEquals("2022/09/30", startController.getDate());
    }

    @Test
    @DisplayName("Créé le service")
    void testCreateService() {
        startController.setMealType(MealType.LUNCH);
        startController.setDate("2022/09/30");
        startController.createService();

        verify(startView, times(1)).displayMessage("Service du jour : "+MealType.LUNCH.getMealValue()+" 2022/09/30");
    }
}
