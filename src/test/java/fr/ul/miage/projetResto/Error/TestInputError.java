package fr.ul.miage.projetResto.Error;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumingThat;
import static org.junit.jupiter.api.DynamicTest.stream;

@DisplayName("ErrorInput")
public class TestInputError {

	@Test
    @DisplayName("Integer : Vérifier que si l'input est un string alors renvoyer null")
    void checkInputIntegerReturnNull() {
		String test = "a";
		assertNull(InputError.checkInteger(test, 1, 5));
    }
	
	@Test
    @DisplayName("Integer : Vérifier que l'input est un integer et renvoie le bon nombre")
    void checkInputInteger() {
		String test = "3";
		assertEquals(3, InputError.checkInteger(test, 1, 5));
    }
	
	@Test
    @DisplayName("Integer : Vérifier que la methode renvoie null si le nombre est trop petit")
    void checkInputIntegerSmallReturnNull() {
		String test = "0";
		assertEquals(3, InputError.checkInteger(test, 1, 5));
    }
	
	@Test
    @DisplayName("Integer : Vérifier que la methode renvoie null si le nombre est trop grand")
    void checkInputIntegerBigReturnNull() {
		String test = "7";
		assertEquals(3, InputError.checkInteger(test, 1, 5));
    }
	
	@Test
    @DisplayName("Integer : Vérifier que la methode renvoie null si le nombre dépasse la taille maximum d'un int")
    void checkInputIntegerMaxSizeReturnNull() {
		String test = "9999999999999999999999999";
		assertEquals(3, InputError.checkInteger(test, 1, 5));
    }
	
	@Test
    @DisplayName("String : Vérifier que si l'input est un integer alors renvoyer null")
    void checkInputStringReturnNull() {
		String test = "3";
		assertNull(InputError.checkString(test));
    }
	
	@Test
    @DisplayName("String : Vérifier que l'input est un string et renvoie le bon string")
    void checkInputString() {
		String test = "test";
		assertEquals("test", InputError.checkString(test));
    }
	
	@Test
    @DisplayName("UserId : Vérifier que l'input est un string et renvoie le bon string")
    void checkInputUserId() {
		String test = "test";
		assertEquals("test", InputError.checkUserId(test));
    }
	
	@Test
    @DisplayName("UserId : Vérifier que la méthode retourne null si le string contient des caractères spéciaux")
    void checkInputUserIdReturnNull() {
		String test = "test-";
		assertEquals(null, InputError.checkUserId(test));
    }
	
	@Test
    @DisplayName("Date : Vérifier que la méthode retourne null si le string n'est pas de format")
    void checkInputDateFormatReturnNull() {
		String test = "22/03/2020";
		assertEquals(null, InputError.checkDate(test));
    }
	
	@Test
    @DisplayName("Date : Vérifier que la méthode retourne null si la date est impossible")
    void checkInputDateImpossibleReturnNull() {
		String test = "2021/02/31";
		assertEquals(null, InputError.checkDate(test));
    }
	
	@Test
    @DisplayName("Date : Vérifier que la méthode retourne la bonne date sous forme de string si elle est correcte")
    void checkInputDate() {
		String test = "test-";
		assertEquals(null, InputError.checkDate(test));
    }
}
