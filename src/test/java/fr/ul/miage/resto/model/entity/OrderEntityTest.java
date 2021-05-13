package fr.ul.miage.resto.model.entity;

import fr.ul.miage.resto.constants.DishType;
import fr.ul.miage.resto.constants.TableState;
import fr.ul.miage.resto.dao.service.BaseService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@DisplayName("OrderEntity")
@ExtendWith(MockitoExtension.class)
class OrderEntityTest {
    @InjectMocks
    OrderEntity orderEntity;

    @Test
    @DisplayName("Retourne TableState.Starter")
    void testGetDishTypeStarter(@Mock BaseService baseService) {
        orderEntity = new OrderEntity();

        List<String> dishes = new ArrayList<>();
        dishes.add("boisson");
        dishes.add("salade");
        orderEntity.setIdsDish(dishes);

        DishEntity dishEntity = new DishEntity();
        dishEntity.setDishType(DishType.DRINK);
        DishEntity dishEntity1 = new DishEntity();
        dishEntity1.setDishType(DishType.STARTER);
        when(baseService.getDishById(anyString())).thenReturn(dishEntity).thenReturn(dishEntity1);

        TableState tableState = orderEntity.getDishType(baseService);
        assertEquals(TableState.STARTER, tableState);
    }

    @Test
    @DisplayName("Retourne TableState.MainCourse")
    void testGetDishTypeMainCourse(@Mock BaseService baseService) {
        orderEntity = new OrderEntity();

        List<String> dishes = new ArrayList<>();
        dishes.add("boisson");
        dishes.add("légumes");
        orderEntity.setIdsDish(dishes);

        DishEntity dishEntity = new DishEntity();
        dishEntity.setDishType(DishType.DRINK);
        DishEntity dishEntity1 = new DishEntity();
        dishEntity1.setDishType(DishType.MAIN_COURSE);
        when(baseService.getDishById(anyString())).thenReturn(dishEntity).thenReturn(dishEntity1);

        TableState tableState = orderEntity.getDishType(baseService);
        assertEquals(TableState.MAIN_COURSE, tableState);
    }

    @Test
    @DisplayName("Retourne TableState.Dessert")
    void testGetDishType(@Mock BaseService baseService) {
        orderEntity = new OrderEntity();

        List<String> dishes = new ArrayList<>();
        dishes.add("boisson");
        dishes.add("beignet au chocolat");
        orderEntity.setIdsDish(dishes);

        DishEntity dishEntity = new DishEntity();
        dishEntity.setDishType(DishType.DRINK);
        DishEntity dishEntity1 = new DishEntity();
        dishEntity1.setDishType(DishType.DESSERT);
        when(baseService.getDishById(anyString())).thenReturn(dishEntity).thenReturn(dishEntity1);

        TableState tableState = orderEntity.getDishType(baseService);
        assertEquals(TableState.DESSERT, tableState);
    }

    @Test
    @DisplayName("Retourne null : normalement n'arrive jamais")
    void testGetDishTypeNone(@Mock BaseService baseService) {
        orderEntity = new OrderEntity();

        List<String> dishes = new ArrayList<>();
        dishes.add("boisson");
        orderEntity.setIdsDish(dishes);

        DishEntity dishEntity = new DishEntity();
        dishEntity.setDishType(DishType.DRINK);
        when(baseService.getDishById(anyString())).thenReturn(dishEntity);

        TableState tableState = orderEntity.getDishType(baseService);
        assertNull(tableState);
    }

    @Test
    @DisplayName("Rend le stock d'un plat à la base")
    void testGiveStockBack(@Mock BaseService baseService) {
        orderEntity = new OrderEntity();
        List<String> dishes = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            dishes.add(" " + i);
        }
        orderEntity.setIdsDish(dishes);
        DishEntity dishEntity = mock(DishEntity.class);
        when(baseService.getDishById(anyString())).thenReturn(dishEntity);
        doNothing().when(dishEntity).changeStock(any(BaseService.class), anyBoolean());

        orderEntity.giveStockBack(baseService);

        verify(dishEntity, times(orderEntity.getIdsDish().size())).changeStock(any(BaseService.class), eq(true));
    }
}
