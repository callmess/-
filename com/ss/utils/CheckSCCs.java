package com.ss.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ss on 2016/5/23.
 * Use for : 社会信用代码和组织机构代码校验
 */
public class CheckSCCs {

    /**
     * 社会信用代码
     * @param str
     * @return
     */
    private static boolean checkSCC(String str) {
        //代码字符集-代码字符
        final String[] codeNo = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "J", "K", "L", "M", "N", "P", "Q", "R", "T", "U", "W", "X", "Y"};
        //代码字符集-代码字符数值
        final String[] staVal = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30"};
        //各位置序号上的加权因子
        int[] wi = {1, 3, 9, 27, 19, 26, 16, 17, 20, 29, 25, 13, 8, 24, 10, 30, 28};

        //统一代码由十八位的数字或大写英文字母（不适用I、O、Z、S、V）组成，第18位为校验位。
        //第1位为数字或大写英文字母，登记管理部门代码
        //第2位为数字或大写英文字母，机构类别代码
        //第3到8位共6位全为数字登记管理机关行政区划码
        //第9-17位共9位为数字或大写英文字母组织机构代码
        //第18为为数字或者大写的Y
        String regex = "^([0-9ABCDEFGHJKLMNPQRTUWXY]{2})([0-9]{6})([0-9ABCDEFGHJKLMNPQRTUWXY]{9})([0-9Y])$";

        Pattern pat = Pattern.compile(regex);

        Matcher matcher = pat.matcher(str);
        if (!matcher.matches()) {
//            System.out.println("表达式非法！");
            return false;
        }
        Map map = new HashMap();

        for (int i = 0; i < codeNo.length; i++) {
            map.put(codeNo[i], staVal[i]);
        }

        String[] all = new String[str.length()];

        all[0] = str.substring(0, str.length() - 1);
        all[1] = str.substring(str.length() - 1, str.length());

        final char[] values = all[0].toCharArray();

        int parity = 0;

        for (int i = 0; i < values.length; i++) {
            final String val = Character.toString(values[i]);
            parity += wi[i] * Integer.parseInt(map.get(val).toString());
        }

//        String cheak = (31 - parity % 31) == 30 ? "Y" : Integer.toString(31 - parity % 31);
//
//        return cheak.equals(all[1]);


        int cheak = (31 - parity % 31);
        if(cheak==31){
            return "0".equals(all[1]);
        }
        String _18Str = codeNo[cheak];


        return _18Str.equals(all[1]);
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        final String[] codeNo = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "J", "K", "L", "M", "N", "P", "Q", "R", "T", "U", "W", "X", "Y"};
        for (int j = 0; j < 31; j++) {
            for (int i = 1000; i < 10000; i++) {
                String str = "91350100M0001" + i+codeNo[j];

                boolean res = checkSCC(str);
                if (res) {
                    System.out.println(str);
                }
            }
        }
//        System.out.println( getAae053("23466345"));

    }


    public static String getAae053(String aae053) {
        int[] ws = { 3, 7, 9, 10, 5, 8, 4, 2 };
        String str = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        int sum = 0;
        for (int i = 0; i < 8; i++) {
            sum += str.indexOf(String.valueOf(aae053.charAt(i))) * ws[i];
        }
        // 当C9的值为10时，校验码应用大写的拉丁字母X表示；当C9的值为11时校验码用0表示。
        int c9 = 11 - (sum % 11);
        String c9str = String.valueOf(c9);
        if (11 == c9) {
            c9str = "0";
        } else if (10 == c9) {
            c9str = "X";
        }
        return aae053 + c9str;
    }

    public static boolean checkAab003(String aae053) {
        int[] ws = { 3, 7, 9, 10, 5, 8, 4, 2 };
        String str = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String reg = "^([0-9A-Z]){8}[0-9|X]$";// /^[A-Za-z0-9]{8}-[A-Za-z0-9]{1}$/
        if (!aae053.matches(reg)) {
            return false;
        }
        int sum = 0;
        for (int i = 0; i < 8; i++) {
            sum += str.indexOf(String.valueOf(aae053.charAt(i))) * ws[i];
        }
        // 当C9的值为10时，校验码应用大写的拉丁字母X表示；当C9的值为11时校验码用0表示。
        int c9 = 11 - (sum % 11);
        String c9str = String.valueOf(c9);
        if (11 == c9) {
            c9str = "0";
        } else if (10 == c9) {
            c9str = "X";
        }
        return c9str.equals(String.valueOf(aae053.charAt(8)));
    }

}

