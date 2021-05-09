package fr.ul.miage.projetResto.model.entity;

import fr.ul.miage.projetResto.constants.DishType;
import fr.ul.miage.projetResto.constants.TableState;
import fr.ul.miage.projetResto.dao.service.BaseService;
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
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@DisplayName("OrderEntity")
@ExtendWith(MockitoExtension.class)
class OrderEntityTest {
    @InjectMocks
    OrderEntity orderEntity;

    @Test
    @DisplayName("Retourne TableState.Starter")
    void checkGetDishTypeStarter(@Mock BaseService baseService) {
        orderEntity = new OrderEntity();

        List<String> dishes = new ArrayList<>();
        dishes.add("boisson");
        dishes.add("salade");
        orderEntity.setIdsDish(dishes);

        DishEntity dishEntity = new DishEntity();
        dishEntity.setDishType(DishType.Drink);
        DishEntity dishEntity1 = new DishEntity();
        dishEntity1.setDishType(DishType.Starter);
        when(baseService.getDishById(anyString())).thenReturn(dishEntity).thenReturn(dishEntity1);

        TableState tableState = orderEntity.getDishType(baseService);
        assertEquals(TableState.Starter,tableState);
    }

    @Test
    @DisplayName("Retourne TableState.MainCourse")
    void checkGetDishTypeMainCourse(@Mock BaseService baseService) {
        orderEntity = new OrderEntity();

        List<String> dishes = new ArrayList<>();
        dishes.add("boisson");
        dishes.add("légumes");
        orderEntity.setIdsDish(dishes);

        DishEntity dishEntity = new DishEntity();
        dishEntity.setDishType(DishType.Drink);
        DishEntity dishEntity1 = new DishEntity();
        dishEntity1.setDishType(DishType.MainCourse);
        when(baseService.getDishById(anyString())).thenReturn(dishEntity).thenReturn(dishEntity1);

        TableState tableState = orderEntity.getDishType(baseService);
        assertEquals(TableState.MainCourse,tableState);
    }

    @Test
    @DisplayName("Retourne TableState.Dessert")
    void checkGetDishType(@Mock BaseService baseService) {
        orderEntity = new OrderEntity();

        List<String> dishes = new ArrayList<>();
        dishes.add("boisson");
        dishes.add("beignet au chocolat");
        orderEntity.setIdsDish(dishes);

        DishEntity dishEntity = new DishEntity();
        dishEntity.setDishType(DishType.Drink);
        DishEntity dishEntity1 = new DishEntity();
        dishEntity1.setDishType(DishType.Dessert);
        when(baseService.getDishById(anyString())).thenReturn(dishEntity).thenReturn(dishEntity1);

        TableState tableState = orderEntity.getDishType(baseService);
        assertEquals(TableState.Dessert,tableState);
    }

    @Test
    @DisplayName("Retourne null : normalement n'arrive jamais")
    void checkGetDishTypeNone(@Mock BaseService baseService) {
        orderEntity = new OrderEntity();

        List<String> dishes = new ArrayList<>();
        dishes.add("boisson");
        orderEntity.setIdsDish(dishes);

        DishEntity dishEntity = new DishEntity();
        dishEntity.setDishType(DishType.Drink);
        when(baseService.getDishById(anyString())).thenReturn(dishEntity);

        TableState tableState = orderEntity.getDishType(baseService);
        assertNull(tableState);
    }

    @Test
    @DisplayName("Rend le stock d'un plat à la base")
    void checkGiveStockBack(@Mock BaseService baseService) {
        orderEntity = new OrderEntity();
        List<String> dishes = new ArrayList<>();
        for (int i = 0; i < 6; i++){
            dishes.add(" "+i);
        }
        orderEntity.setIdsDish(dishes);
        DishEntity dishEntity = mock(DishEntity.class);
        when(baseService.getDishById(anyString())).thenReturn(dishEntity);
        doNothing().when(dishEntity).changeStock(any(BaseService.class), anyBoolean());

        orderEntity.giveStockBack(baseService);

        verify(dishEntity, times(orderEntity.getIdsDish().size())).changeStock(any(BaseService.class), eq(true));
    }
}
