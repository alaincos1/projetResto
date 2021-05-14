package fr.ul.miage.resto.model.entity;

import fr.ul.miage.resto.constants.TableState;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("TableEntity")
@ExtendWith(MockitoExtension.class)
class TableEntityTest {

    @ParameterizedTest
    @CsvSource({
            "ser,hel",
            "ser,-",
            "-,hel",
            "-,-"
    })
    void testToString(String ser, String hel) {
        TableEntity tableEntity = new TableEntity();
        tableEntity.setId("1");
        tableEntity.setTableState(TableState.FREE);
        tableEntity.setNbSeats(4);
        tableEntity.setIdServer(ser.equals("-") ? null : ser);
        tableEntity.setIdHelper(hel.equals("-") ? null : hel);
        String expected = "Table n°1, nombre de places: 4, état: Libre\n    -> serveur: " + ser + ", assistant: " + hel;

        String actual = tableEntity.toString();

        Assertions.assertEquals(expected, actual);
    }
}


