package com.tibiadata.tibia_crawler.model.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

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
}
