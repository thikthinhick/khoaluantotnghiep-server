package com.vnu.server.utils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

public class StringUtils {

    public static Date convertStringDate(String time) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            return dateFormat.parse(time);
        } catch(Exception e) {
            return null;
        }
    }
    public static Date convertStringDate(String time, String format) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(format);
            return dateFormat.parse(time);
        } catch(Exception e) {
            return null;
        }
    }
    public static String convertDateToString(Date date, String format) {
        SimpleDateFormat sampleDateFormat = new SimpleDateFormat(format);
        return sampleDateFormat.format(date);
    }
    public static Date increaseDay(Date date, int value) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, value);
        return c.getTime();
    }
    public static String lastOneMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, -1);
        return convertDateToString(cal.getTime(), "yyyy-MM");
    }
    public static long getDifferenceDays(Date d1, Date d2) {
        return ChronoUnit.DAYS.between(d1.toInstant(), d2.toInstant());
    }
    public static String nextOneMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, 1);
        return convertDateToString(cal.getTime(), "yyyy-MM");
    }
    public static String convertJunToNumber(Long value) {
        return String.valueOf((double)Math.round((double)value / 36000) / 100);
    }
    public static String convertNumberToCost(int number) {
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        return formatter.format(number)+" VNĐ";
    }
    public static String ceilJun(Integer value) {
        return String.valueOf((double) Math.round((double) value / 36) / 100);
    }
}
