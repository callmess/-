package com.ss.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

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
     * 获取当天时间
     * @return dateStrng
     */
    public static String getNowDate() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    /**
     * 获取当前日期前后nt天的日期
     * @param format 日期格式, 默认为yyyy-MM-dd
     * @param differ 0为今天
     * @return 日期Str
     */
    public static String getDifferDay(String format, int differ) {
        if (format == null) {
            format = "yyyy-MM-dd";
        }
        Date date = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, differ);
        date = calendar.getTime();
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(date);
    }


    /**
     * 转换星期(星期日是第一天)
     * @param week 一周的第几天
     * @return 星期X
     */
    public static String getWeekDay(int week) {
        String[] weeks = {"日", "一", "二", "三", "四", "五", "六"};
        if (week < 0 || week > 6) {
            return "请输入0-6之间的整数";
        }
        return "星期" + weeks[week];
    }


    /**
     * 系统的换行
     */
    public static void checkSeparator() {
        String a = "1233" + System.getProperty("line.separator") + "00000";
        System.out.println(a);
    }
}
