package com.tibiadata.tibia_crawler.model.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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

    public boolean isDifferenceGreaterThan180Days(Calendar date1, Calendar date2) {
        long diffInMillis = Math.abs(date1.getTimeInMillis() - date2.getTimeInMillis()); // Calcular a diferença em milissegundos
        long diffInDays = diffInMillis / (24 * 60 * 60 * 1000); // Converter milissegundos para dias (1 dia = 86400000 milissegundos)
        return diffInDays >= 180; // Verificar se a diferença é maior ou igual a 180 dias
    }
    
    // TODO método com input string MM/DD/YYYY e retorna um Calendar/Date formatado com essa data
}
