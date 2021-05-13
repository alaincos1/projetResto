package fr.ul.miage.resto.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
public class DateUtil {
    private static final String DATE_FORMAT = "yyyy/MM/dd";

    private DateUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static List<DateDto> getListDate(String type, String date, Integer last) {
        LocalDate localDate = getLocalDate(date);

        if (localDate == null) {
            return Collections.emptyList();
        }

        if (StringUtils.equalsIgnoreCase("day", type)) {
            return getDatesDays(last, localDate);
        } else if (StringUtils.equalsIgnoreCase("week", type)) {
            return getDatesWeek(last, localDate);
        } else if (StringUtils.equalsIgnoreCase("month", type)) {
            return getDatesMonth(last, localDate);
        }

        return Collections.emptyList();
    }

    private static List<DateDto> getDatesDays(Integer last, LocalDate localDate) {
        localDate = localDate.minusDays(last);
        SimpleDateFormat defaultFormat = new SimpleDateFormat(DATE_FORMAT, Locale.FRENCH);
        SimpleDateFormat displayFormat = new SimpleDateFormat("yyyy/MM/dd (EEEE)", Locale.FRENCH);

        List<String> startDates = getFormattedDate(defaultFormat, localDate, last, 0);
        List<String> displayDates = getFormattedDate(displayFormat, localDate, last, 0);

        List<DateDto> dates = new ArrayList<>();
        for (int i = 0; i < startDates.size(); i++) {
            dates.add(new DateDto(startDates.get(i), startDates.get(i), displayDates.get(i)));
        }

        return dates;
    }

    private static List<DateDto> getDatesWeek(Integer last, LocalDate localDate) {
        LocalDate localDate1 = localDate.minusWeeks(last).with(DayOfWeek.MONDAY);
        LocalDate localDate2 = localDate.minusWeeks(last).with(DayOfWeek.SUNDAY);

        SimpleDateFormat defaultFormat = new SimpleDateFormat(DATE_FORMAT, Locale.FRENCH);

        List<String> startDates = getFormattedDate(defaultFormat, localDate1, last, 1);
        List<String> endDates = getFormattedDate(defaultFormat, localDate2, last, 1);

        List<DateDto> dates = new ArrayList<>();
        for (int i = 0; i < startDates.size(); i++) {
            dates.add(new DateDto(startDates.get(i), endDates.get(i), startDates.get(i) + "-" + endDates.get(i)));
        }

        return dates;
    }

    private static List<DateDto> getDatesMonth(Integer last, LocalDate localDate) {
        LocalDate localDate1 = localDate.minusMonths(last).withDayOfMonth(1);
        LocalDate localDate2 = localDate.minusMonths(last).withDayOfMonth(localDate.lengthOfMonth());
        SimpleDateFormat displayFormat = new SimpleDateFormat("MMMM yyyy", Locale.FRENCH);
        SimpleDateFormat defaultFormat = new SimpleDateFormat(DATE_FORMAT, Locale.FRENCH);

        List<String> displayDates = getFormattedDate(displayFormat, localDate1, last, 2);
        List<String> startDates = getFormattedDate(defaultFormat, localDate1, last, 2);
        List<String> endDates = getFormattedDate(defaultFormat, localDate2, last, 2);

        List<DateDto> dates = new ArrayList<>();

        for (int i = 0; i < startDates.size(); i++) {
            dates.add(new DateDto(startDates.get(i), endDates.get(i), displayDates.get(i)));
        }

        return dates;
    }

    private static List<String> getFormattedDate(SimpleDateFormat format, LocalDate localDate, Integer last, Integer type) {
        return IntStream.rangeClosed(1, last)
                .mapToObj(type == 0 ? localDate::plusDays : type == 1 ? localDate::plusWeeks : localDate::plusMonths)
                .map(Date::valueOf)
                .map(format::format)
                .collect(Collectors.toList());
    }

    private static LocalDate getLocalDate(String date) {
        try {
            return LocalDate.parse(date, DateTimeFormatter.ofPattern(DATE_FORMAT));
        } catch (DateTimeParseException e) {
            log.error(e.getMessage());
        }
        return null;
    }
}
