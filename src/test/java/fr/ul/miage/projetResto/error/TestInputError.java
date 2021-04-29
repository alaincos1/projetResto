package fr.ul.miage.projetResto.error;

import fr.ul.miage.projetResto.constants.InfoRestaurant;
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
        assertNull(InputError.checkUserId(test));
    }
	
	@Test
    @DisplayName("Date : Vérifier que la méthode retourne null si le string n'est pas de format")
    void checkInputDateFormatReturnNull() {
		String test = "22/03/2020";
        assertNull(InputError.checkDate(test));
    }
	
	@Test
    @DisplayName("Date : Vérifier que la méthode retourne null si la date est impossible (31 février) ")
    void checkInputDateImpossibleReturnNull() {
		String test = "2021/02/31";
        assertNull(InputError.checkDate(test));
    }
	
	@Test
    @DisplayName("Date : Vérifier que la méthode retourne null si la date est impossible (mois n°56)")
    void checkInputDateImpossibleReturnNull2() {
		String test = "2021/56/12";
        assertNull(InputError.checkDate(test));
    }
	
	@Test
    @DisplayName("Date : Vérifier que la méthode retourne null si la date est impossible (jour n°56)")
    void checkInputDateImpossibleReturnNull3() {
		String test = "2021/03/56";
        assertNull(InputError.checkDate(test));
    }
	
	@Test
    @DisplayName("Date : Vérifier que la méthode retourne la bonne date sous forme de string si elle est correcte")
    void checkInputDate() {
		String test = "2021/03/30";
		assertEquals("2021/03/30", InputError.checkDate(test));
    }

    @Test
    @DisplayName("String : Vérifier que la méthode retourne null si la chaîne est vide")
    void checkEmptyString() {
        String test = "    ";
        assertNull(InputError.checkString(test));
    }

    @Test
    @DisplayName("String : Vérifier que la méthode retourne null si la chaîne est plus grande que 30")
    void checkStringLength() {
        String test = "Salades de champignons à la turque et aux olives";
        assertNull(InputError.checkString(test));
    }

    @Test
    @DisplayName("String : Vérifier que la méthode retourne la commande si aucune problème n'est détecté")
    void checkStringCorrect() {
        String test = "Salades de champignons";
        assertEquals(test, InputError.checkString(test));
    }

    @Test
    @DisplayName("String : Vérifier que la méthode retourne null si la chaîne est vide")
    void checkEmptyMultipleChoices() {
        String test = "   ";
        assertNull(InputError.checkStringMultipleChoices(test, 1, 2));
    }

    @Test
    @DisplayName("String : Vérifier que la méthode retourne null si plus de 10 choix")
    void checkMultipleChoicesLength() {
        String test = "1/2/3/4/5/6/7/8/9/10/11";
        assertNull(InputError.checkStringMultipleChoices(test, 1, 11));
    }

    @Test
    @DisplayName("String : Vérifier que la méthode retourne null si un élément des choix est hors bornes")
    void checkMultipleChoicesLimits() {
        String test = "1/6";
        String test2 = "6";
        assertNull(InputError.checkStringMultipleChoices(test, 1, 3));
        assertNull(InputError.checkStringMultipleChoices(test2, 1, 3));
    }

    @Test
    @DisplayName("String : Vérifier que la méthode retourne les choix si tout est correct")
    void checkMultipleChoicesCorrect() {
        String test = "1/2/3";
        assertEquals(test, InputError.checkStringMultipleChoices(test,1,3));
    }
}
