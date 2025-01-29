package com.tibiadata.tibia_crawler.model.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe utilitária para manipulação de datas e horários, fornecendo métodos
 * para conversão, comparação e formatação de datas.
 *
 * @author Devmachine
 */
public class CalendarUtils {

    /**
     * Compara duas datas para verificar se a diferença entre elas é maior ou
     * igual a 180 dias.
     *
     * @param date1 Primeira data a ser comparada.
     * @param date2 Segunda data a ser comparada.
     * @return {@code true} se a diferença entre as duas datas for maior ou
     * igual a 180 dias, {@code false} caso contrário.
     */
    public static boolean greaterThan180Days(Calendar date1, Calendar date2) {
        long diffInMillis = Math.abs(date1.getTimeInMillis() - date2.getTimeInMillis()); // Calcular a diferença em milissegundos
        long diffInDays = diffInMillis / (24 * 60 * 60 * 1000); // Converter milissegundos para dias (1 dia = 86400000 milissegundos)
        return diffInDays >= 180; // Verificar se a diferença é maior ou igual a 180 dias
    }

    /**
     * Converte uma string representando uma data e hora no formato "MMM d yyyy,
     * HH:mm:ss" para um objeto Calendar, usando o fuso horário "Europe/Paris".
     *
     * @param textDate A string representando a data e hora no formato "MMM d
     *                 yyyy, HH:mm:ss".
     * @return Um objeto Calendar representando a data e hora convertidas para o
     * fuso horário "Europe/Paris".
     */
    public static Calendar parseToCalendar(String textDate) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d yyyy, HH:mm:ss", Locale.ENGLISH); // Define o padrão do formato "Mmm d yyyy"

        ZonedDateTime zonedDateTime = LocalDateTime.parse(textDate, formatter).atZone(ZoneId.of("Europe/Paris"));

        Date date = Date.from(zonedDateTime.toInstant()); // Converte ZonedDateTime para Date

        Calendar calendar = Calendar.getInstance(); // Cria uma instância de Calendar e define a data
        calendar.setTime(date);

        return calendar;
    }

    /**
     * Verifica se o minuto atual é igual ao minuto fornecido.
     *
     * @param minute O minuto a ser comparado com o minuto atual.
     * @return {@code true} se o minuto atual for igual ao fornecido,
     * {@code false} caso contrário.
     */
    public static boolean isCurrentMinute(int minute) {
        return LocalTime.now().getMinute() == minute;
    }

    /**
     * Verifica se a hora e o minuto atuais são iguais aos valores fornecidos.
     *
     * @param hour   A hora a ser comparada com a hora atual.
     * @param minute O minuto a ser comparado com o minuto atual.
     * @return {@code true} se a hora e o minuto atuais forem iguais aos fornecidos,
     * {@code false} caso contrário.
     */
    public static boolean isCurrentHourAndMinute(int hour, int minute){
        return LocalTime.now().getHour() == hour && LocalTime.now().getMinute() == minute;
    }

    /**
     * Verifica se a data fornecida corresponde ao dia de hoje, ignorando horas,
     * minutos, segundos e milissegundos.
     *
     * @param calendar A data a ser comparada com a data atual.
     * @return {@code true} se a data fornecida for o mesmo dia que hoje,
     * {@code false} caso contrário.
     */
    public static boolean isSameDate(Calendar calendar) {
        if (calendar == null) {
            return false;
        }

        Calendar today = Calendar.getInstance();

        return calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) && calendar.get(Calendar.MONTH) == today.get(Calendar.MONTH) && calendar.get(Calendar.DAY_OF_MONTH) == today.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * Converte um objeto Date para um objeto Calendar.
     *
     * @param date Data a ser convertida para Calendar.
     * @return Calendar correspondente à data fornecida.
     */
    public static Calendar convertToCalendar(Date date) {
        Calendar converted = Calendar.getInstance();
        converted.setTime(date);
        return converted;
    }

    /**
     * Retorna a data e hora atual no formato "yyyy-MM-dd HH:mm:ss".
     *
     * @return String contendo a data e hora atual formatada.
     */
    public static String getDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(calendar.getTime());
    }

}
