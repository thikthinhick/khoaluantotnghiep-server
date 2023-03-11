package com.vnu.server.utils;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
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
    public static String convertDateToString(Date date, String format) {
        SimpleDateFormat sampleDateFormat = new SimpleDateFormat(format);
        return sampleDateFormat.format(date);
    }
    public static String lastOneMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, 1);
        return convertDateToString(cal.getTime(), "yyyy-MM");
    }
    public static String convertNumberToCost(int number) {
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        return formatter.format(number)+" VNĐ";
    }
}
