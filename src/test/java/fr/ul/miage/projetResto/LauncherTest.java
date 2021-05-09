package fr.ul.miage.projetResto;

import fr.ul.miage.projetResto.appinfo.Service;
import fr.ul.miage.projetResto.constants.MealType;
import fr.ul.miage.projetResto.constants.Role;
import fr.ul.miage.projetResto.model.entity.UserEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LauncherTest {

    @Test
    void testSetService() {
        Launcher.setService(MealType.DINNER, "2021/04/20");
        assertEquals(MealType.DINNER, Launcher.getService().getMealType());
        assertEquals("2021/04/20", Launcher.getService().getDate());
    }

    @Test
    void testGetService() {
        Launcher.setService(MealType.LUNCH, "2020/04/20");
        assertEquals(Launcher.getService(), new Service(MealType.LUNCH, "2020/04/20"));
    }

    @Test
    void testSetLoggedUser() {
        Launcher.setLoggedUser(new UserEntity("admin", Role.Director));
        assertEquals(Launcher.getLoggedUser(), new UserEntity("admin", Role.Director));
    }

    @Test
    void testGetLoggedUser() {
        Launcher.setLoggedUser(new UserEntity("admin", Role.Butler));
        assertEquals(Launcher.getLoggedUser(), new UserEntity("admin", Role.Butler));
    }
}
