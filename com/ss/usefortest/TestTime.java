package com.ss.usefortest;

import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ss on 2016/7/27.
 *
 */
public class TestTime {
    public static void main(String[] args) {
        Date date = new Date();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS");
        String mm = sdf.format(date);
        System.out.println(mm);
       System.out.println(System.currentTimeMillis());

    }
}
