package com.clothes.websitequanao.common;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class DateUtil {
    private Timestamp timestamp;
    private DateFormat outputFormat;
    public static final String DMY_HMS = "dd/MM/yyyy hh:mm:ss";

    public static Timestamp returnByPatern(Timestamp input, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return Timestamp.valueOf(sdf.format(input));
    }

    public static Timestamp addDays(Timestamp t1, int days) {
        if (t1 == null) t1 = new Timestamp(System.currentTimeMillis());
        long values = (long) days * 24 * 60 * 60 * 1000;
        Long miliseconds = Long.valueOf(values);
        return new Timestamp(t1.getTime() + miliseconds);
    }

    public static Timestamp stringToTimestamp(String input) {
        if (input == null || input.isEmpty()) {
            return null;
        }
        return Timestamp.valueOf(input + " 00:00:00");
    }
}