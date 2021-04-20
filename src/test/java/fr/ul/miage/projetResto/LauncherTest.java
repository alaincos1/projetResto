package fr.ul.miage.projetResto;

import fr.ul.miage.projetResto.appinfo.Service;
import fr.ul.miage.projetResto.constants.MealType;
import lombok.var;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LauncherTest {

    @Test
    void testInitializeService(){
        Launcher.initializeService(MealType.Dîner, "2021/04/20");
        assertEquals(Launcher.getService().getMealType(), MealType.Dîner);
        assertEquals(Launcher.getService().getDate(), "2021/04/20");
    }

    @Test
    void testGetService(){
        Launcher.initializeService(MealType.Déjeuner, "2020/04/20");
        assertEquals(Launcher.getService(), new Service(MealType.Déjeuner, "2020/04/20"));
    }
}
