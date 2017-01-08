package com.ss.usefortest;

import org.apache.commons.collections.map.HashedMap;

import java.util.Map;

/**
 * Created by ss on 2016/5/5.
 * Use for :
 */
public class Test {


    /** 通过Unicode编码区间的转换达到转换的效果
     * 全角转半角
     * @param inputStr 全角
     * @return 半角字符
     */
    public static String to_single_byte(String inputStr){
        char c[] = inputStr.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == '\u3000') {
                c[i] = ' ';
            } else if (c[i] > '\uFF00' && c[i] < '\uFF5F') {
                c[i] = (char) (c[i] - 65248);

            }
        }
        String returnString = new String(c);

        return returnString;
    }

    /**
     *
     * @param inputStr
     * @return 全角字符
     */
    public static String to_mu_type (String inputStr){
        char c []=inputStr.toCharArray();
        for (int i = 0; i <c.length ; i++) {
            if (c [i]==' '){
                c[i]='\u3000';
            }
            else if (c[i] < '\177') {
                c[i] = (char) (c[i] + 65248);
            }
        }
        String resultString= new String(c);
        return resultString;
    }


    /*public static void main(String args[]) {
        String str= to_single_byte("４５６７８９ｄ ！＠＄＾＆＊＊（）＿＋＠＃＄～？？＜＞：＂｛｝｜ｄｄ");
        System.out.println('\uFF01');
        System.out.println(str);*//*
        String str ="123456+123456677888+234567";
        String aa[] =str.split("");

        for (String a:aa
             ) {
            System.out.println(a);
        }

    }*/

    public static void printMuByte(){
        for (int i ='\0'; i < '\uFF6F' ; i++) {
            System.out.print((char)i);
        }
    }

    /**
     * 校验港澳台社会保障号合法性
     * @param aac002
     * @param aac161
     * @param aac147
     * @param aac058
     * @return
     */
     public static Boolean checkNationCode(String aac002, String aac161, String aac147,String aac058) {

        String nationCode = aac002.substring(0, 3);//前三位国家/地区代码
        String IDnumber = aac002.substring(4); //证件号码
        String asizeCode = aac002.substring(3, 4); //预留位
        if (!nationCode.equals(aac161) || !asizeCode.matches("[0-9]") || !IDnumber.matches("[0-9]{8}")) {
            return false;
        }
        if ("HKG".equals(aac161) && "04".equals(aac058)) {
            aac147 = aac147.substring(1, 9);
            return aac147.equals( IDnumber);
        }
        if ("TWN".equals(aac161) && "06".equals(aac058)) {
            aac147 = aac147.substring(0, 8);
            return aac147.equals( IDnumber);
        }

        return false;
    }


    public static void main(String args[]) {
        printMuByte();
//        System.out.println( "12345678".matches("[0-9]{8}"));
//        System.out.println( checkNationCode("HKG012345678","HKG","H1234567800","04"));
    }
}
