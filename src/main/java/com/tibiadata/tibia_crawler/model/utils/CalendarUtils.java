package com.tibiadata.tibia_crawler.model.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Devmachine
 */
public class CalendarUtils {

    /**
     *
     * @return local date yyyy-MM-dd HH:mm:ss
     */
    public String getDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(calendar.getTime());
    }

    /**
     * Convert a Date format to Calendar
     *
     * @param date Date to be converted to Calendar
     * @return Calendar
     */
    public Calendar convertToCalendar(Date date) {
        Calendar converted = Calendar.getInstance();
        converted.setTime(date);
        return converted;
    }

    /**
     * @param date1 compare to date 2
     * @param date2 is compared with date 1
     * @return true if the comparison between date1 and date2 is greater/equal
     * than 180 days
     */
    public boolean greaterThan180Days(Calendar date1, Calendar date2) {
        long diffInMillis = Math.abs(date1.getTimeInMillis() - date2.getTimeInMillis()); // Calcular a diferença em milissegundos
        long diffInDays = diffInMillis / (24 * 60 * 60 * 1000); // Converter milissegundos para dias (1 dia = 86400000 milissegundos)
        System.out.println(diffInDays);
        return diffInDays >= 180; // Verificar se a diferença é maior ou igual a 180 dias
    }

    /**
     * @param date a string format MM/dd/YYYY to be converted in calendar
     * @return A formated calendar
     */
    public Calendar parseDate(String date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            sdf.setLenient(false); // desativa datas improváveis
            Date parsedDate = sdf.parse(date);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(parsedDate);

            return calendar;

        } catch (ParseException ex) {
            Logger.getLogger(CalendarUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Calendar.getInstance();
    }

    /**
     * Converts a string representing a date and time in the format "MMM d yyyy,
     * HH:mm:ss" to a Calendar object, using the "Europe/Paris" time zone.
     *
     * @param textDate the date string in the format "MMM d yyyy, HH:mm:ss" to
     * be parsed
     * @return a Calendar instance representing the parsed date and time in the
     * "Europe/Paris" time zone
     */
    public Calendar parseToCalendar(String textDate) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d yyyy, HH:mm:ss", Locale.ENGLISH);// Define o padrão do formato "Mmm d yyyy"

        ZonedDateTime zonedDateTime = LocalDateTime.parse(textDate, formatter)
                .atZone(ZoneId.of("Europe/Paris"));

        Date date = Date.from(zonedDateTime.toInstant());// Converte ZonedDateTime para Date

        Calendar calendar = Calendar.getInstance();// Cria uma instância de Calendar e define a data
        calendar.setTime(date);

        return calendar;
    }

    /**
     * Compares two Calendar objects to check if they represent the same date
     * and time. The comparison includes year, month, day, hour, minute, and
     * second.
     *
     * @param c1 the first Calendar instance
     * @param c2 the second Calendar instance
     * @return true if both Calendar instances represent the same date and time,
     * false otherwise
     */
    public boolean isSameDateTime(Calendar c1, Calendar c2) {
        return c1 != null && c2 != null
                && c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
                && c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH)
                && c1.get(Calendar.DAY_OF_MONTH) == c2.get(Calendar.DAY_OF_MONTH)
                && c1.get(Calendar.HOUR_OF_DAY) == c2.get(Calendar.HOUR_OF_DAY)
                && c1.get(Calendar.MINUTE) == c2.get(Calendar.MINUTE)
                && c1.get(Calendar.SECOND) == c2.get(Calendar.SECOND);
    }

}
