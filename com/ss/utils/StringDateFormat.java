package com.ss.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * java日期格式|时间戳格式
 * Created by juneday on 2016/12/22.
 */
public class StringDateFormat {

    /**
     * 精度带时间戳的日期格式
     * @param date 日期
     */
    public static String getStringDateTimeStamp(Date date) {
        if (date == null) {
            date = new Date();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS");
        String strdate = sdf.format(date);
        String timestamp = sdf2.format(date);
        System.out.println(strdate);
        System.out.println(timestamp);
        return timestamp;
    }

    /**
     * 系统的换行
     */
    public static void checkSeparator() {
        String a = "1233" + System.getProperty("line.separator") + "00000";
        System.out.println(a);
    }
}
