package fr.ul.miage.resto;

import fr.ul.miage.resto.appinfo.Service;
import fr.ul.miage.resto.constants.MealType;
import fr.ul.miage.resto.constants.Role;
import fr.ul.miage.resto.model.entity.UserEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Launcher")
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
        Launcher.setLoggedUser(new UserEntity("admin", Role.DIRECTOR));
        assertEquals(Launcher.getLoggedUser(), new UserEntity("admin", Role.DIRECTOR));
    }

    @Test
    void testGetLoggedUser() {
        Launcher.setLoggedUser(new UserEntity("admin", Role.BUTLER));
        assertEquals(Launcher.getLoggedUser(), new UserEntity("admin", Role.BUTLER));
    }
}
