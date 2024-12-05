package com.tibiadata.tibia_crawler.model.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Devmachine
 */
public class CalendarUtils {

    Calendar calendar;

    /**
     *
     * @return local date yyyy-MM-dd HH:mm:ss
     */
    public String getDate() {
        calendar = Calendar.getInstance();
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

}
