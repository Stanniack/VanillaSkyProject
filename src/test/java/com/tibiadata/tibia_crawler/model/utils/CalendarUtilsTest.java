package com.tibiadata.tibia_crawler.model.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CalendarUtilsTest {

    @Test
    public void testGreaterThan180Days() throws ParseException {
        String dateStr = "2024-09-31"; // Data no formato yyyy-MM-dd
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Date date = sdf.parse(dateStr); // Converte para Date
        Calendar testCalendar = Calendar.getInstance();
        testCalendar.setTime(date); // Define a data no Calendar

        assertTrue(CalendarUtils.greaterThan180Days(Calendar.getInstance(), testCalendar), "A diferença entre as data é maior que 180 dias (6 meses)");

    }

    @Test
    public void testIsCurrentMinute(){
        int minute = 46;
        assertTrue(CalendarUtils.isCurrentMinute(minute), "O minuto fornecido não corresponde ao minuto atual");
    }
}
