package de.hawhh.ristorante.modelgenerator;

import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class DateTimeUtil {

    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    public static List<String> getDaysOfMonthAsString(int year, int month){
        return LocalDate.of(year,month,1).datesUntil(LocalDate.of(year,month, maxDay(year,month))).map(ld -> DateTimeUtil.DATE_FORMATTER.format(ld)).collect(Collectors.toList());
    }

    public static List<LocalDate> getDaysOfMonth(int year, int month){
        return LocalDate.of(year,month,1).datesUntil(LocalDate.of(year,month, maxDay(year,month))).collect(Collectors.toList());
    }

    private static int maxDay(int year, int month) {
        YearMonth ym =  YearMonth.of(year, month);
        return ym.lengthOfMonth();
    }

    public static List<Month> legalMonthsPerYear(int year){
        LocalDate date = LocalDate.now();
        if (date.getYear()== year) {
            return Arrays.asList(Arrays.copyOf(Month.values(),date.getMonthValue()));
        } else if (date.getYear() < year)
            return Arrays.asList();
        else return Arrays.asList(Month.values());
    }
}
