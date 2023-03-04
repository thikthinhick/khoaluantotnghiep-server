package com.vnu.server.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StringUtils {

    public static Timestamp convertStringToTimestamp(String time) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date parsedDate = dateFormat.parse(time);
            return new Timestamp(parsedDate.getTime());
        } catch(Exception e) {
            return null;
        }
    }
    public static String convertDateToString(Date date, String format) {
        SimpleDateFormat sampleDateFormat = new SimpleDateFormat(format);
        return sampleDateFormat.format(date);
    }
}
