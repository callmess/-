package com.ss.usefortest;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * java日期格式|时间戳格式
 * Created by juneday on 2016/12/22.
 */
public class StringDateFormatTest {
    public static void main(String agrs[]){
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS");
        String strdate = sdf.format(date);
        String timestamp = sdf2.format(date);
        System.out.println(strdate);
        System.out.println(timestamp );

    }
}
