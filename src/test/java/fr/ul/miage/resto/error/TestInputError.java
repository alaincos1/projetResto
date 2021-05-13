package fr.ul.miage.resto.error;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@DisplayName("InputError")
class TestInputError {

    @ParameterizedTest
    @DisplayName("Integer : Vérifier que si l'input est un string, est trop petit ou trop grand alors renvoyer null")
    @ValueSource(strings = {"a", "0", "7"})
    void testInputIntegerReturnNull(String arg) {
        assertNull(InputError.checkInteger(arg, 1, 5));
    }

    @Test
    @DisplayName("Integer : Vérifier que l'input est un integer et renvoie le bon nombre")
    void testInputInteger() {
        String test = "3";
        assertEquals(3, InputError.checkInteger(test, 1, 5));
    }

    @Test
    @DisplayName("Integer : Vérifier que la methode renvoie null si le nombre dépasse la taille maximum d'un int")
    void testInputIntegerMaxSizeReturnNull() {
        String test = "9999999999999999999999999";
        assertNull(InputError.checkInteger(test, 1, 5));
    }

    @Test
    @DisplayName("Integer : Vérifier que la methode renvoie null si le nombre est vide")
    void testInputIntegerEmptyReturnNull() {
        String test = "";
        assertNull(InputError.checkInteger(test, 1, 5));
    }

    @Test
    @DisplayName("String Commande : Vérifier que on peut ajouter commande adulte")
    void testInputStringCommandeAdulte() {
        String test = "-a 1/5";
        assertEquals("-a 1/5", InputError.checkStringCommand(test, 1, 5));
    }

    @Test
    @DisplayName("String Commande : Vérifier que on peut valider")
    void testInputStringCommandeValidee() {
        String test = "-v";
        assertEquals("-v", InputError.checkStringCommand(test, null, null));
    }

    @Test
    @DisplayName("String Commande : Vérifier que on peut supprimer")
    void testInputStringCommandeSupprimeElement() {
        String test = "-d 1/5";
        assertEquals("-d 1/5", InputError.checkStringCommand(test, 1, 5));
    }

    @ParameterizedTest
    @ValueSource(strings = {"-a -d", "124", "-a 1 12"})
    @DisplayName("String Commande : Vérification si input est faux")
    void testInputStringCommandeIncorrect(String arg) {
        assertNull(InputError.checkStringCommand(arg, 1, 5));
    }

    @Test
    @DisplayName("String Commande : Vérifier que l'input est un string et renvoie le bon string")
    void testInputString() {
        String test = "-a";
        assertNull(InputError.checkStringCommand(test, null, null));
    }

    @Test
    @DisplayName("String Commande : Vérifier que valider ne prends pas de nombre derrière")
    void testInputStringCommandeValideNull() {
        String test = "-v 1";
        assertNull(InputError.checkStringCommand(test, 1, 5));
    }

    @Test
    @DisplayName("UserId : Vérifier que l'input est un string et renvoie le bon string")
    void testInputUserId() {
        String test = "test";
        assertEquals("test", InputError.checkUserId(test));
    }

    @Test
    @DisplayName("UserId : Vérifier que la méthode retourne null si le string contient des caractères spéciaux")
    void testInputUserIdReturnNull() {
        String test = "test-";
        assertNull(InputError.checkUserId(test));
    }

    @ParameterizedTest
    @ValueSource(strings = {"22/03/2020", "2021/02/31"})
    @DisplayName("Date : Vérifier que la méthode retourne null si le string n'est pas de bon format, impossible,")
    void testInputDateFormatReturnNull(String arg) {
        assertNull(InputError.checkDate(arg));
    }

    @Test
    @DisplayName("Date : Vérifier que la méthode retourne null si la date est impossible (mois n°56)")
    void testInputDateImpossibleReturnNull2() {
        String test = "2021/56/12";
        assertNull(InputError.checkDate(test));
    }

    @Test
    @DisplayName("Date : Vérifier que la méthode retourne null si la date est impossible (jour n°56)")
    void testInputDateImpossibleReturnNull3() {
        String test = "2021/03/56";
        assertNull(InputError.checkDate(test));
    }

    @Test
    @DisplayName("Date : Vérifier que la méthode retourne la bonne date sous forme de string si elle est correcte")
    void testInputDate() {
        String test = "2022/03/30";
        assertEquals("2022/03/30", InputError.checkDate(test));
    }

    @Test
    @DisplayName("String : Vérifier que la méthode retourne null si la chaîne est vide")
    void testEmptyString() {
        String test = "    ";
        assertNull(InputError.checkString(test));
    }

    @Test
    @DisplayName("String : Vérifier que la méthode retourne null si la chaîne est plus grande que 30")
    void testStringLength() {
        String test = "Salades de champignons à la turque et aux olives";
        assertNull(InputError.checkString(test));
    }

    @Test
    @DisplayName("String : Vérifier que la méthode retourne la commande si aucune problème n'est détecté")
    void testStringCorrect() {
        String test = "Salades de champignons";
        assertEquals(test, InputError.checkString(test));
    }

    @Test
    @DisplayName("String : Vérifier que la méthode retourne null si la chaîne est vide")
    void testEmptyMultipleChoices() {
        String test = "   ";
        assertNull(InputError.checkStringMultipleChoices(test, 1, 2));
    }

    @Test
    @DisplayName("String : Vérifier que la méthode retourne null si plus de 10 choix")
    void testMultipleChoicesLength() {
        String test = "1/2/3/4/5/6/7/8/9/10/11";
        assertNull(InputError.checkStringMultipleChoices(test, 1, 11));
    }

    @Test
    @DisplayName("String : Vérifier que la méthode retourne null si un élément des choix est hors bornes")
    void testMultipleChoicesLimits() {
        String test = "1/6";
        String test2 = "6";
        assertNull(InputError.checkStringMultipleChoices(test, 1, 3));
        assertNull(InputError.checkStringMultipleChoices(test2, 1, 3));
    }

    @Test
    @DisplayName("String : Vérifier que la méthode retourne les choix si tout est correct")
    void testMultipleChoicesCorrect() {
        String test = "1/2/3";
        assertEquals(test, InputError.checkStringMultipleChoices(test, 1, 3));
    }
}
