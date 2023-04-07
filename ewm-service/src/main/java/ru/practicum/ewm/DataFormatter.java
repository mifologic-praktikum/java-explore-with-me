package ru.practicum.ewm;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DataFormatter {

    public static DateTimeFormatter getFormatter() {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    }

    public static LocalDateTime fromStringToDate(String date) {
        DateTimeFormatter formatter = getFormatter();
        return LocalDateTime.parse(date, formatter);
    }

    public static String fromDateToString(LocalDateTime date) {
        DateTimeFormatter formatter = getFormatter();
        return date.format(formatter);
    }
}
