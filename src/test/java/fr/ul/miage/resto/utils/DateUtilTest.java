package fr.ul.miage.resto.utils;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("DateUtil")
class DateUtilTest {

    @Test
    @DisplayName("Parse une date")
    void testGetLocalDate() {
        LocalDate expected = LocalDate.parse("2021/09/30", DateTimeFormatter.ofPattern("yyyy/MM/dd"));

        LocalDate localDate = DateUtil.getLocalDate("2021/09/30");

        assertEquals(expected, localDate);
    }

    @Test
    @DisplayName("Problème de parsing d'une date")
    void testGetLocalDateNull() {
        LocalDate localDate = DateUtil.getLocalDate("2021/09/32");

        assertNull(localDate);
    }

    @Test
    @DisplayName("Retourne les 6 derniers mois")
    void testGetDatesMonth() {
        LocalDate local1 = LocalDate.parse("2021/09/30", DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        List<DateDto> expected = new ArrayList<>();
        expected.add(new DateDto("2021/04/01", "2021/04/30", "avril 2021"));
        expected.add(new DateDto("2021/05/01", "2021/05/30", "mai 2021"));
        expected.add(new DateDto("2021/06/01", "2021/06/30", "juin 2021"));
        expected.add(new DateDto("2021/07/01", "2021/07/30", "juillet 2021"));
        expected.add(new DateDto("2021/08/01", "2021/08/30", "août 2021"));
        expected.add(new DateDto("2021/09/01", "2021/09/30", "septembre 2021"));

        List<DateDto> dates = DateUtil.getDatesMonth(6, local1);

        assertEquals(expected, dates);
    }

    @Test
    @DisplayName("Retourne les 4 dernieres semaines")
    void testGetDatesWeek() {
        LocalDate local1 = LocalDate.parse("2021/09/30", DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        List<DateDto> expected = new ArrayList<>();
        expected.add(new DateDto("2021/09/06", "2021/09/12", "2021/09/06-2021/09/12"));
        expected.add(new DateDto("2021/09/13", "2021/09/19", "2021/09/13-2021/09/19"));
        expected.add(new DateDto("2021/09/20", "2021/09/26", "2021/09/20-2021/09/26"));
        expected.add(new DateDto("2021/09/27", "2021/10/03", "2021/09/27-2021/10/03"));

        List<DateDto> dates = DateUtil.getDatesWeek(4, local1);

        assertEquals(expected, dates);
    }

    @Test
    @DisplayName("Retourne les 7 derniers jours")
    void testGetDatesDays() {
        LocalDate local1 = LocalDate.parse("2021/09/30", DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        List<DateDto> expected = new ArrayList<>();
        expected.add(new DateDto("2021/09/24", "2021/09/24", "2021/09/24 (vendredi)"));
        expected.add(new DateDto("2021/09/25", "2021/09/25", "2021/09/25 (samedi)"));
        expected.add(new DateDto("2021/09/26", "2021/09/26", "2021/09/26 (dimanche)"));
        expected.add(new DateDto("2021/09/27", "2021/09/27", "2021/09/27 (lundi)"));
        expected.add(new DateDto("2021/09/28", "2021/09/28", "2021/09/28 (mardi)"));
        expected.add(new DateDto("2021/09/29", "2021/09/29", "2021/09/29 (mercredi)"));
        expected.add(new DateDto("2021/09/30", "2021/09/30", "2021/09/30 (jeudi)"));

        List<DateDto> dates = DateUtil.getDatesDays(7, local1);

        assertEquals(expected, dates);
    }

    @ParameterizedTest
    @CsvSource({
            "day,2021/09/32",
            " ,2021/09/30"
    })
    @DisplayName("Retourne une liste vide")
    void testGetListDateNull(String type, String date) {
        List<DateDto> dates = DateUtil.getListDate(type, date, 7);

        assertTrue(CollectionUtils.isEmpty(dates));
    }

    @Test
    @DisplayName("Retourne une liste de jours")
    void testGetListDateDay() {
        List<DateDto> expected = new ArrayList<>();
        expected.add(new DateDto("2021/09/29", "2021/09/29", "2021/09/29 (mercredi)"));
        expected.add(new DateDto("2021/09/30", "2021/09/30", "2021/09/30 (jeudi)"));

        List<DateDto> dates = DateUtil.getListDate("day", "2021/09/30", 2);

        assertEquals(expected, dates);
    }

    @Test
    @DisplayName("Retourne une liste de semaines")
    void testGetListDateWeek() {
        List<DateDto> expected = new ArrayList<>();
        expected.add(new DateDto("2021/09/20", "2021/09/26", "2021/09/20-2021/09/26"));
        expected.add(new DateDto("2021/09/27", "2021/10/03", "2021/09/27-2021/10/03"));

        List<DateDto> dates = DateUtil.getListDate("week", "2021/09/30", 2);

        assertEquals(expected, dates);
    }

    @Test
    @DisplayName("Retourne une liste de mois")
    void testGetListDateMonth() {
        List<DateDto> expected = new ArrayList<>();
        expected.add(new DateDto("2021/08/01", "2021/08/30", "août 2021"));
        expected.add(new DateDto("2021/09/01", "2021/09/30", "septembre 2021"));

        List<DateDto> dates = DateUtil.getListDate("month", "2021/09/30", 2);

        assertEquals(expected, dates);
    }

    @Test
    @DisplayName("Retourne la liste des 2 derniers jours")
    void testGetFormattedDateDays() {
        LocalDate localDate = DateUtil.getLocalDate("2021/09/28");
        SimpleDateFormat defaultFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.FRENCH);

        List<String> expected = new ArrayList<>();
        expected.add("2021/09/29");
        expected.add("2021/09/30");

        List<String> dates = DateUtil.getFormattedDate(defaultFormat, localDate, 2, 0);

        assertEquals(expected, dates);
    }

    @Test
    @DisplayName("Retourne la liste des 2 dernieres semaines")
    void testGetFormattedDateWeek() {
        LocalDate localDate = DateUtil.getLocalDate("2021/09/13");
        SimpleDateFormat defaultFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.FRENCH);

        List<String> expected = new ArrayList<>();
        expected.add("2021/09/20");
        expected.add("2021/09/27");

        List<String> dates = DateUtil.getFormattedDate(defaultFormat, localDate, 2, 1);

        assertEquals(expected, dates);
    }

    @Test
    @DisplayName("Retourne la liste des 2 derniers mois")
    void testGetFormattedDateMonth() {
        LocalDate localDate = DateUtil.getLocalDate("2021/07/01");
        SimpleDateFormat defaultFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.FRENCH);

        List<String> expected = new ArrayList<>();
        expected.add("2021/08/01");
        expected.add("2021/09/01");

        List<String> dates = DateUtil.getFormattedDate(defaultFormat, localDate, 2, 2);

        assertEquals(expected, dates);
    }

}
