package com.cn.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    private static final String YYYY_MM = "yyyy-MM";

    public static String datePath() {
        SimpleDateFormat format = new SimpleDateFormat(YYYY_MM);
        return format.format(new Date());
    }
}
