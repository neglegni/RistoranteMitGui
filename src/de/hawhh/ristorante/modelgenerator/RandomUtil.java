package de.hawhh.ristorante.modelgenerator;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoField;
import java.util.Calendar;
import java.util.Random;

public class RandomUtil {

    private static final Random rand = new Random(78172971);
    public static Random getRandom(){return rand;}

    public static LocalDateTime randomInYears(int yearDiff, LocalDateTime dateTime){
        int monthDiff = 12;
        int dayOfMonthDiff = 31;
        int hourDiff = 12;
        int minuteDiff = 60;
        LocalDateTime current =  dateTime.minusYears(rand.nextInt(yearDiff));
        current = current.minusMonths(rand.nextInt(monthDiff));
        current = current.minusDays(rand.nextInt(dayOfMonthDiff));
        current = current.minusHours(rand.nextInt(hourDiff));
        current = current.minusMinutes(rand.nextInt(minuteDiff));
        return current;
    }
}
