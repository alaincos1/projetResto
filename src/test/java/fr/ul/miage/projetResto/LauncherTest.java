package fr.ul.miage.projetResto;

import fr.ul.miage.projetResto.appinfo.Service;
import fr.ul.miage.projetResto.constants.MealType;
import fr.ul.miage.projetResto.constants.Role;
import fr.ul.miage.projetResto.model.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LauncherTest {

    @Test
    void testSetService(){
        Launcher.setService(MealType.Dîner, "2021/04/20");
        assertEquals(Launcher.getService().getMealType(), MealType.Dîner);
        assertEquals(Launcher.getService().getDate(), "2021/04/20");
    }

    @Test
    void testGetService(){
        Launcher.setService(MealType.Déjeuner, "2020/04/20");
        assertEquals(Launcher.getService(), new Service(MealType.Déjeuner, "2020/04/20"));
    }

    @Test
    void testSetLoggedUser(){
        Launcher.setLoggedUser(new User("admin", Role.Director));
        assertEquals(Launcher.getLoggedUser(),new User("admin", Role.Director));
    }

    @Test
    void testGetLoggedUser(){
        Launcher.setLoggedUser(new User("admin", Role.Butler));
        assertEquals(Launcher.getLoggedUser(), new User("admin", Role.Butler));
    }
}
