package fr.ul.miage.projetResto.error;

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

@DisplayName("InputError")
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
		assertNull(InputError.checkInteger(test, 1, 5));
    }
	 
	@Test
    @DisplayName("Integer : Vérifier que la methode renvoie null si le nombre est trop grand")
    void checkInputIntegerBigReturnNull() {
		String test = "7";
		assertNull(InputError.checkInteger(test, 1, 5));
    }
	
	@Test
    @DisplayName("Integer : Vérifier que la methode renvoie null si le nombre dépasse la taille maximum d'un int")
    void checkInputIntegerMaxSizeReturnNull() {
		String test = "9999999999999999999999999";
		assertNull(InputError.checkInteger(test, 1, 5));
    }
	
	@Test
    @DisplayName("Integer : Vérifier que la methode renvoie null si le nombre est vide")
    void checkInputIntegerEmptyReturnNull() {
		String test = "";
		assertNull(InputError.checkInteger(test, 1, 5));
    }
	
	@Test
    @DisplayName("String Commande : Vérifier que on peut ajouter commande enfant")
    void checkInputStringCommandeEnfant() {
		String test = "-e 1/5";
		assertEquals("-e 1/5",InputError.checkStringCommand(test, 1, 5));
    }
	
	@Test
    @DisplayName("String Commande : Vérifier que on peut ajouter commande adulte")
    void checkInputStringCommandeAdulte() {
		String test = "-a 1/5";
		assertEquals("-a 1/5", InputError.checkStringCommand(test, 1, 5));
    }
	
	@Test
    @DisplayName("String Commande : Vérifier que on peut valider")
    void checkInputStringCommandeValidee() {
		String test = "-v";
		assertEquals("-v", InputError.checkStringCommand(test, null, null));
    }
	
	@Test
    @DisplayName("String Commande : Vérifier que on peut supprimer")
    void checkInputStringCommandeSupprimeElement() {
		String test = "-d 1/5";
		assertEquals("-d 1/5", InputError.checkStringCommand(test, 1, 5));
    }
	
	@Test
    @DisplayName("String Commande : Vérification si input est faux")
    void checkInputStringCommandeIncorrect() {
		String test = "-a -e";
		assertNull(InputError.checkStringCommand(test, 1, 5));
    }
	
	@Test
    @DisplayName("String Commande : Vérification si input est faux")
    void checkInputStringCommandeIncorrect2() {
		String test = "124";
		assertNull(InputError.checkStringCommand(test, 1, 5));
    }
	
	@Test
    @DisplayName("String Commande : Vérification si input est faux")
    void checkInputStringCommandeIncorrect3() {
		String test = "-a 1 12";
		assertNull(InputError.checkStringCommand(test, 1, 5));
    }
	
	@Test
    @DisplayName("String Commande : Vérifier que l'input est un string et renvoie le bon string")
    void checkInputString() {
		String test = "-a";
		assertNull(InputError.checkStringCommand(test, null, null));
    }
	
	@Test
    @DisplayName("String Commande : Vérifier que valider ne prends pas de nombre derrière")
    void checkInputStringCommandeValideNull() {
		String test = "-v 1";
		assertNull(InputError.checkStringCommand(test, 1, 5));
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
    @DisplayName("Date : Vérifier que la méthode retourne null si la date est impossible (31 février) ")
    void checkInputDateImpossibleReturnNull() {
		String test = "2021/02/31";
		assertEquals(null, InputError.checkDate(test));
    }
	
	@Test
    @DisplayName("Date : Vérifier que la méthode retourne null si la date est impossible (mois n°56)")
    void checkInputDateImpossibleReturnNull2() {
		String test = "2021/56/12";
		assertEquals(null, InputError.checkDate(test));
    }
	
	@Test
    @DisplayName("Date : Vérifier que la méthode retourne null si la date est impossible (jour n°56)")
    void checkInputDateImpossibleReturnNull3() {
		String test = "2021/03/56";
		assertEquals(null, InputError.checkDate(test));
    }
	
	@Test
    @DisplayName("Date : Vérifier que la méthode retourne la bonne date sous forme de string si elle est correcte")
    void checkInputDate() {
		String test = "2021/03/30";
		assertEquals("2021/03/30", InputError.checkDate(test));
    }
}
