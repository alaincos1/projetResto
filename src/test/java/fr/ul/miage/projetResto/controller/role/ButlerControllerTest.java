package fr.ul.miage.projetResto.controller.role;

import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import fr.ul.miage.projetResto.error.InputError;

@DisplayName("ButlerController")
public class ButlerControllerTest {

	ButlerController butlerController;
	
	@Test
    void checkInputIntegerReturnNull() {
		String test = "a";
		assertNull(InputError.checkInteger(test, 1, 5));
    } 
	
}
