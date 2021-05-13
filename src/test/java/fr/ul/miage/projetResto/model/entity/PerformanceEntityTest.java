package fr.ul.miage.projetResto.model.entity;

import fr.ul.miage.projetResto.constants.MealType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("PerformanceEntity")
@ExtendWith(MockitoExtension.class)
class PerformanceEntityTest {

    @Test
    @DisplayName("Teste la modification des performances de service")
    void testUpdateService() {
        PerformanceEntity expected = new PerformanceEntity();
        expected.set_id("2021/09/30DINNER");
        expected.setNbTableServed(6);
        expected.setNbOrder(16);
        expected.setPreparationTime(526);
        expected.setServiceTime(360);

        PerformanceEntity result = new PerformanceEntity();
        result.set_id("2021/09/30DINNER");
        result.setNbTableServed(5);
        result.setNbOrder(16);
        result.setPreparationTime(526);
        result.setServiceTime(350);

        result.update("serviceTime", 10);

        assertEquals(expected, result);
    }

    @Test
    @DisplayName("Teste la modification des performances de préparation")
    void testUpdatePreparation() {
        PerformanceEntity expected = new PerformanceEntity();
        expected.set_id("2021/09/30DINNER");
        expected.setNbTableServed(5);
        expected.setNbOrder(17);
        expected.setPreparationTime(536);
        expected.setServiceTime(350);

        PerformanceEntity result = new PerformanceEntity();
        result.set_id("2021/09/30DINNER");
        result.setNbTableServed(5);
        result.setNbOrder(16);
        result.setPreparationTime(526);
        result.setServiceTime(350);

        result.update("preparationTime", 10);

        assertEquals(expected, result);
    }

    @Test
    @DisplayName("Teste l'initialisation des performances de préparation")
    void testInitPreparation() {
        PerformanceEntity expected = new PerformanceEntity();
        expected.set_id("2021/09/30DINNER");
        expected.setMealType(MealType.DINNER);
        expected.setNbTableServed(0);
        expected.setNbOrder(1);
        expected.setPreparationTime(10);
        expected.setServiceTime(0);

        PerformanceEntity result = new PerformanceEntity();

        result.initPerf("2021/09/30DINNER", "preparationTime", 10);

        assertEquals(expected, result);
    }

    @Test
    @DisplayName("Teste l'initialisation des performances de service")
    void testInitService() {
        PerformanceEntity expected = new PerformanceEntity();
        expected.set_id("2021/09/30DINNER");
        expected.setMealType(MealType.DINNER);
        expected.setNbTableServed(1);
        expected.setNbOrder(0);
        expected.setPreparationTime(0);
        expected.setServiceTime(10);

        PerformanceEntity result = new PerformanceEntity();

        result.initPerf("2021/09/30DINNER", "serviceTime", 10);

        assertEquals(expected, result);
    }
}
