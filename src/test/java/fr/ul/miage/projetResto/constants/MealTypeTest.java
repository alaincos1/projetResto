package fr.ul.miage.projetResto.constants;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MealTypeTest {

    @Test
    void getFromIdTestReturns1(){
        MealType mealTypeTest = MealType.getFromId(1);
        assertEquals(mealTypeTest, MealType.Déjeuner);
    }

    @Test
    void getFromIdTestReturns2(){
        MealType mealTypeTest = MealType.getFromId(2);
        assertEquals(mealTypeTest, MealType.Dîner);
    }

    @Test
    void getFromIdTestReturnsNull(){
        MealType mealTypeTest = MealType.getFromId(3);
        assertEquals(mealTypeTest, null);
    }

}
