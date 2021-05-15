package fr.ul.miage.resto.controller.role;

import fr.ul.miage.resto.appinfo.Service;
import fr.ul.miage.resto.constants.MealType;
import fr.ul.miage.resto.constants.Role;
import fr.ul.miage.resto.dao.service.BaseService;
import fr.ul.miage.resto.model.entity.PerformanceEntity;
import fr.ul.miage.resto.utils.DateDto;
import fr.ul.miage.resto.utils.DateUtil;
import fr.ul.miage.resto.utils.InputUtil;
import fr.ul.miage.resto.view.role.RoleView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("RoleController")
@ExtendWith(MockitoExtension.class)
class RoleControllerTest {
    @Mock
    RoleView roleView;
    @Spy
    @InjectMocks
    RoleController roleController;

    @Test
    @DisplayName("Sauvegarde les performances pour une commande prête : nouveau fichier de performances et service")
    void testSavePerfNewService(@Mock Service service, @Mock BaseService baseService) {
        when(service.getDate()).thenReturn("2021/09/30");
        when(service.getMealType()).thenReturn(MealType.DINNER);
        when(baseService.getPerformanceById(anyString())).thenReturn(null);

        roleController.savePerformance(service, baseService, "serviceTime", 30, 120);

        verify(baseService, times(1)).save(any(PerformanceEntity.class));
    }

    @Test
    @DisplayName("Sauvegarde les performances pour une commande prête : nouveau fichier de performances et preparation")
    void testSavePerfNewPreparation(@Mock Service service, @Mock BaseService baseService) {
        when(service.getDate()).thenReturn("2021/09/30");
        when(service.getMealType()).thenReturn(MealType.DINNER);
        when(baseService.getPerformanceById(anyString())).thenReturn(null);

        roleController.savePerformance(service, baseService, "preparationTime", 15, 30);

        verify(baseService, times(1)).save(any(PerformanceEntity.class));
    }

    @Test
    @DisplayName("Sauvegarde les performances pour une commande prête : modification du fichier de performances et service")
    void testSavePerfUpdateService(@Mock Service service, @Mock BaseService baseService) {
        when(service.getDate()).thenReturn("2021/09/30");
        when(service.getMealType()).thenReturn(MealType.DINNER);
        PerformanceEntity perf = new PerformanceEntity();
        perf.setId("2021/09/30DINNER");
        perf.setNbOrder(5);
        perf.setNbTableServed(9);
        perf.setPreparationTime(120);
        perf.setServiceTime(140);
        when(baseService.getPerformanceById(anyString())).thenReturn(perf);

        roleController.savePerformance(service, baseService, "serviceTime", 30, 120);

        verify(baseService, times(1)).update(any(PerformanceEntity.class));
    }

    @Test
    @DisplayName("Sauvegarde les performances pour une commande prête : modification du fichier de performances et preparation")
    void testSavePerfUpdatePreparation(@Mock Service service, @Mock BaseService baseService) {
        when(service.getDate()).thenReturn("2021/09/30");
        when(service.getMealType()).thenReturn(MealType.DINNER);
        PerformanceEntity perf = new PerformanceEntity();
        perf.setId("2021/09/30DINNER");
        perf.setNbOrder(5);
        perf.setNbTableServed(9);
        perf.setPreparationTime(120);
        perf.setServiceTime(140);
        when(baseService.getPerformanceById(anyString())).thenReturn(perf);

        roleController.savePerformance(service, baseService, "preparationTime", 15, 30);

        verify(baseService, times(1)).update(any(PerformanceEntity.class));
    }

    @ParameterizedTest
    @CsvSource({
            "DIRECTOR,11",
            "BUTLER,4",
            "COOK,4",
            "SERVER,4",
            "HELPER,2",
    })
    @DisplayName("Donne le nombre d'actions d'un role, test à modifier en cas d'ajout de fonctionnalité")
    void testSetNbActions(String role, int nbAction) {
        roleController.roleTemp = Role.valueOf(role);

        assertEquals(nbAction, roleController.setNbActions());
    }

    @Test
    @DisplayName("Lance le controller d'un role")
    void testLaunch() {
        doReturn(1).when(roleController).askAction();

        roleController.launch(Role.DIRECTOR);

        verify(roleController, times(1)).setNbActions();
        verify(roleController, times(1)).callAction(anyInt());
    }

    @Test
    @DisplayName("Demande la prochaine action")
    void testAskAction() {
        roleController.roleTemp = Role.DIRECTOR;
        roleController.nbActions = 1;

        doNothing().when(roleView).displayMenu(Role.DIRECTOR);
        try (MockedStatic<InputUtil> utilities = Mockito.mockStatic(InputUtil.class)) {
            utilities.when(() -> InputUtil.getIntegerInput(0, 1))
                    .thenReturn(1);

            assertEquals(1, roleController.askAction());
        }
    }

    @Test
    @DisplayName("Demande la prochaine action")
    void testAskMainMenu() {
        roleController.roleTemp = Role.DIRECTOR;
        doNothing().when(roleView).displayAskReturnMainMenu();
        doNothing().when(roleController).launch(any(Role.class));
        try (MockedStatic<InputUtil> utilities = Mockito.mockStatic(InputUtil.class)) {
            utilities.when(() -> InputUtil.getIntegerInput(0, 0))
                    .thenReturn(0);
            roleController.askMainMenu();

            verify(roleController, times(1)).launch(any(Role.class));
        }
    }

    @Test
    @DisplayName("Demande la prochaine action - Cas impossible")
    void testAskMainMenuImpossible() {
        roleController.roleTemp = Role.DIRECTOR;
        doNothing().when(roleView).displayAskReturnMainMenu();
        try (MockedStatic<InputUtil> utilities = Mockito.mockStatic(InputUtil.class)) {
            utilities.when(() -> InputUtil.getIntegerInput(0, 0))
                    .thenReturn(1);
            roleController.askMainMenu();

            verify(roleController, times(0)).launch(any(Role.class));
        }
    }

    @Test
    @DisplayName("Recommence l'action")
    void testDoAgain() {
        try (MockedStatic<InputUtil> utilities = Mockito.mockStatic(InputUtil.class)) {
            utilities.when(() -> InputUtil.getIntegerInput(0, 1))
                    .thenReturn(1);

            assertTrue(roleController.doAgain());
        }
    }

    @Test
    @DisplayName("Ne recommence pas l'action")
    void testDoNotAgain() {
        try (MockedStatic<InputUtil> utilities = Mockito.mockStatic(InputUtil.class)) {
            utilities.when(() -> InputUtil.getIntegerInput(0, 1))
                    .thenReturn(0);

            assertFalse(roleController.doAgain());
        }
    }

    @Test
    @DisplayName("Retourne une entrée int")
    void testGetIntegerInput() {
        try (MockedStatic<InputUtil> utilities = Mockito.mockStatic(InputUtil.class)) {
            utilities.when(() -> InputUtil.getIntegerInput(0, 10))
                    .thenReturn(5);

            assertEquals(5, roleController.getIntegerInput(0, 10));
        }
    }

    @Test
    @DisplayName("Retourne une entrée date")
    void testGetDateInput() {
        try (MockedStatic<InputUtil> utilities = Mockito.mockStatic(InputUtil.class)) {
            utilities.when(InputUtil::getDateInput)
                    .thenReturn("2021/09/30");

            assertEquals("2021/09/30", roleController.getDateInput());
        }
    }

    @Test
    @DisplayName("Retourne une entrée user id")
    void testGetUserIdInput() {
        try (MockedStatic<InputUtil> utilities = Mockito.mockStatic(InputUtil.class)) {
            utilities.when(InputUtil::getUserIdInput)
                    .thenReturn("user");

            assertEquals("user", roleController.getUserIdInput());
        }
    }

    @Test
    @DisplayName("Retourne une entrée commande serveur")
    void testGetStringCommandInput() {
        try (MockedStatic<InputUtil> utilities = Mockito.mockStatic(InputUtil.class)) {
            utilities.when(() -> InputUtil.getStringCommandInput(0, 10))
                    .thenReturn("-a 1/6");

            assertEquals("-a 1/6", roleController.getStringCommandInput(0, 10));
        }
    }

    @Test
    @DisplayName("Retourne une entrée chaîne")
    void testGetStringInput() {
        try (MockedStatic<InputUtil> utilities = Mockito.mockStatic(InputUtil.class)) {
            utilities.when(InputUtil::getStringInput)
                    .thenReturn("string");

            assertEquals("string", roleController.getStringInput());
        }
    }

    @Test
    @DisplayName("Retourne une entrée commande de choix multiple")
    void testGetStringMultipleChoicesInput() {
        try (MockedStatic<InputUtil> utilities = Mockito.mockStatic(InputUtil.class)) {
            utilities.when(() -> InputUtil.getStringMultipleChoices(0, 10))
                    .thenReturn("1/6");

            assertEquals("1/6", roleController.getStringMultipleChoices(0, 10));
        }
    }

    @Test
    @DisplayName("Retourne une liste de date")
    void testGetListDate() {
        List<DateDto> dates = new ArrayList<>();
        try (MockedStatic<DateUtil> utilities = Mockito.mockStatic(DateUtil.class)) {
            utilities.when(() -> DateUtil.getListDate("day", "2021/09/30", 1))
                    .thenReturn(dates);

            assertEquals(dates, roleController.getListDate("day", "2021/09/30", 1));
        }
    }
}
