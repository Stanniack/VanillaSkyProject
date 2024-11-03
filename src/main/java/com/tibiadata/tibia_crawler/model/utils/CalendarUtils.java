package com.tibiadata.tibia_crawler.model.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 *
 * @author Devmachine
 */
public class CalendarUtils {

    Calendar calendar;

    public String getBrDate() {
        calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(calendar.getTime());
    }
}
