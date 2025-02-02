package com.tibiadata.tibia_crawler.model.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CalendarUtilsTest {

    @Test
    public void testGreaterThan180Days() throws ParseException {
        String dateStr = "2024-01-31"; // Data no formato yyyy-MM-dd
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Date date = sdf.parse(dateStr); // Converte para Date
        Calendar testCalendar = Calendar.getInstance();
        testCalendar.setTime(date); // Define a data no Calendar

        assertTrue(CalendarUtils.greaterThan180Days(Calendar.getInstance(), testCalendar), "A diferença entre as data é maior que 180 dias (6 meses)");

    }

    @Test
    @Disabled
    public void testIsCurrentMinute(){
        int minute = 46;

        assertTrue(CalendarUtils.isCurrentMinute(minute), "O minuto fornecido não corresponde ao minuto atual");
    }

    @Test
    @Disabled
    public void testIsCurrentHourAndMinute(){
        int hour = 00;
        int minute = 44;
        assertTrue(CalendarUtils.isCurrentHourAndMinute(hour, minute), "A hora e minuto fornecido não correspondem a hora e minuto atual");
    }

    @Test
    @Disabled
    public void testIsSameDate() throws ParseException {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse("2025-01-30");
        calendar.setTime(date);

        assertTrue(CalendarUtils.isSameDate(calendar), "As datas comparadas não estão no mesmo dia ou mês ou ano");
    }

    @Test
    @Disabled
    public void testMinutesToServerSave() {
        // Configuração do fuso horário europeu em CET/CEST
        // O método deve calcular a diferença para 05:00 OU 06:00 do próximo dia (horário de Brasília)

        long remainingMinutes = CalendarUtils.minutesToServerSave(); // Chama o método

        assertEquals(467, remainingMinutes);
    }


}
