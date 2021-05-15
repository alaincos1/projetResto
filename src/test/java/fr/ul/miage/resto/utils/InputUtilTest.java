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
        try (MockedStatic<InputErrorUtil> utilities = Mockito.mockStatic(InputErrorUtil.class)) {
            utilities.when(() -> InputErrorUtil.checkInteger("0", 0, 0))
                    .thenReturn(0);
            try (MockedStatic<InputUtil> utilities2 = Mockito.mockStatic(InputUtil.class)) {
                utilities2.when(InputUtil::getUserInput)
                        .thenReturn("0");

                assertEquals(0, InputUtil.getIntegerInput(0, 0));
            }
        }
    }

    @Test
    @DisplayName("Récupère l'input")
    void testGetIntegerInputTwice() {
        try (MockedStatic<InputErrorUtil> utilities = Mockito.mockStatic(InputErrorUtil.class)) {
            utilities.when(() -> InputErrorUtil.checkInteger("0", 0, 0))
                    .thenReturn(null).thenReturn(0);
            try (MockedStatic<InputUtil> utilities2 = Mockito.mockStatic(InputUtil.class)) {
                utilities2.when(InputUtil::getUserInput)
                        .thenReturn("1").thenReturn("0");

                assertEquals(0, InputUtil.getIntegerInput(0, 0));
            }
        }
    }
}
