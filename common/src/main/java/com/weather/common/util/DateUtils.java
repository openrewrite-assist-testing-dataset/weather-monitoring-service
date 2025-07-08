package com.weather.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    private static final SimpleDateFormat ISO_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    
    public static String formatDate(Date date) {
        return ISO_FORMAT.format(date);
    }
    
    public static Date parseDate(String dateString) {
        try {
            return ISO_FORMAT.parse(dateString);
        } catch (Exception e) {
            return new Date();
        }
    }
    
    public static Date getCurrentDate() {
        return new Date();
    }
}