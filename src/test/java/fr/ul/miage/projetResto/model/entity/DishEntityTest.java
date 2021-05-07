package fr.ul.miage.projetResto.model.entity;

import fr.ul.miage.projetResto.constants.DishType;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DishEntityTest {
    private final EasyRandom easyRandom = new EasyRandom();

    @Test
    public void testOrderDishByTypeWithDifferentLowerDishType() {
        DishEntity dish1 = easyRandom.nextObject(DishEntity.class);
        DishEntity dish2 = easyRandom.nextObject(DishEntity.class);
        dish1.setDishType(DishType.MainCourse);
        dish2.setDishType(DishType.Dessert);

        Integer actual = DishEntity.orderDishByType(dish1, dish2);

        Assertions.assertEquals(-1, actual);
    }

    @Test
    public void testOrderDishByTypeWithDifferentHigherDishType() {
        DishEntity dish1 = easyRandom.nextObject(DishEntity.class);
        DishEntity dish2 = easyRandom.nextObject(DishEntity.class);
        dish1.setDishType(DishType.Dessert);
        dish2.setDishType(DishType.MainCourse);

        Integer actual = DishEntity.orderDishByType(dish1, dish2);

        Assertions.assertEquals(1, actual);
    }

    @Test
    public void testOrderDishByTypeWithSameDishType() {
        DishEntity dish1 = easyRandom.nextObject(DishEntity.class);
        DishEntity dish2 = easyRandom.nextObject(DishEntity.class);
        dish1.setDishType(DishType.MainCourse);
        dish2.setDishType(DishType.MainCourse);

        Integer actual = DishEntity.orderDishByType(dish1, dish2);

        Assertions.assertEquals(0, actual);
    }
}
