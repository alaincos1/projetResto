package fr.ul.miage.resto.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("InputUtil")
@ExtendWith(MockitoExtension.class)
class InputUtilTest {

    @Test
    @DisplayName("Récupère l'input")
    void testGetIntegerInput() {
        try (MockedStatic<InputUtil> utilities = Mockito.mockStatic(InputUtil.class)) {
            utilities.when(InputUtil::getUserInput)
                    .thenReturn("0");

            assertEquals(0, InputUtil.getIntegerInput(0, 0));
        }
    }

    @Test
    @DisplayName("Récupère l'input")
    void testGetIntegerInputTwice() {
        try (MockedStatic<InputUtil> utilities = Mockito.mockStatic(InputUtil.class)) {
            utilities.when(InputUtil::getUserInput)
                    .thenReturn("1").thenReturn("0");

            assertEquals(0, InputUtil.getIntegerInput(0, 0));
        }
    }
}
