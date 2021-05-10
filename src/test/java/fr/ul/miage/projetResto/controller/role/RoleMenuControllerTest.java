package fr.ul.miage.projetResto.controller.role;

import fr.ul.miage.projetResto.appinfo.Service;
import fr.ul.miage.projetResto.constants.MealType;
import fr.ul.miage.projetResto.dao.service.BaseService;
import fr.ul.miage.projetResto.model.entity.PerformanceEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@DisplayName("RoleMenuController")
@ExtendWith(MockitoExtension.class)
class RoleMenuControllerTest {
    @Spy
    @InjectMocks
    RoleMenuController roleMenuController;

    @Test
    @DisplayName("Sauvegarde les performances pour une commande prête : nouveau fichier de performances et service")
    void savePerfNewService(@Mock Service service, @Mock BaseService baseService) {
        when(service.getDate()).thenReturn("2021/09/30");
        when(service.getMealType()).thenReturn(MealType.DINNER);
        when(baseService.getPerformanceById(anyString())).thenReturn(null);

        roleMenuController.savePerformance(service, baseService, "serviceTime", 30, 120);

        verify(baseService, times(1)).save(any(PerformanceEntity.class));
    }

    @Test
    @DisplayName("Sauvegarde les performances pour une commande prête : nouveau fichier de performances et preparation")
    void savePerfNewPreparation(@Mock Service service, @Mock BaseService baseService) {
        when(service.getDate()).thenReturn("2021/09/30");
        when(service.getMealType()).thenReturn(MealType.DINNER);
        when(baseService.getPerformanceById(anyString())).thenReturn(null);

        roleMenuController.savePerformance(service, baseService, "preparationTime", 15, 30);

        verify(baseService, times(1)).save(any(PerformanceEntity.class));
    }

    @Test
    @DisplayName("Sauvegarde les performances pour une commande prête : modification du fichier de performances et service")
    void savePerfUpdateService(@Mock Service service, @Mock BaseService baseService) {
        when(service.getDate()).thenReturn("2021/09/30");
        when(service.getMealType()).thenReturn(MealType.DINNER);
        PerformanceEntity perf = new PerformanceEntity();
        perf.set_id("2021/09/30DINNER");
        perf.setNbOrder(5);
        perf.setNbTableServed(9);
        perf.setPreparationTime(120);
        perf.setServiceTime(140);
        when(baseService.getPerformanceById(anyString())).thenReturn(perf);

        roleMenuController.savePerformance(service, baseService, "serviceTime", 30, 120);

        verify(baseService, times(1)).update(any(PerformanceEntity.class));
    }

    @Test
    @DisplayName("Sauvegarde les performances pour une commande prête : modification du fichier de performances et preparation")
    void savePerfUpdatePreparation(@Mock Service service, @Mock BaseService baseService) {
        when(service.getDate()).thenReturn("2021/09/30");
        when(service.getMealType()).thenReturn(MealType.DINNER);
        PerformanceEntity perf = new PerformanceEntity();
        perf.set_id("2021/09/30DINNER");
        perf.setNbOrder(5);
        perf.setNbTableServed(9);
        perf.setPreparationTime(120);
        perf.setServiceTime(140);
        when(baseService.getPerformanceById(anyString())).thenReturn(perf);

        roleMenuController.savePerformance(service, baseService, "preparationTime", 15, 30);

        verify(baseService, times(1)).update(any(PerformanceEntity.class));
    }

}
