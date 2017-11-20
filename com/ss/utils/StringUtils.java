package com.ss.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import sun.misc.BASE64Decoder;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ss on 2016/5/5.
 * Use for :
 */
public class StringUtils {


    /**
     * 全角转半角
     * 通过Unicode编码区间的转换达到转换的效果
     * @param inputStr 全角
     * @return 半角字符
     */
    public static String to_single_byte(String inputStr) {
        char c[] = inputStr.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == '\u3000') {
                c[i] = ' ';
            } else if (c[i] > '\uFF00' && c[i] < '\uFF5F') {
                c[i] = (char) (c[i] - 65248);

            }
        }

        return new String(c);
    }

    /**
     * 半角转全角
     * @param inputStr the String should be input
     * @return 全角字符
     */
    public static String to_mu_type(String inputStr) {
        char c[] = inputStr.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == ' ') {
                c[i] = '\u3000';
            } else if (c[i] < '\177') {
                c[i] = (char) (c[i] + 65248);
            }
        }
        return new String(c);
    }

    /**
     * 反转字符串
     * @param str 字符串
     */
    public static String reverString(String str) {

        char[] chars = str.toCharArray();
        System.out.println(chars);
        for (int i = 0, len = chars.length; i < len / 2; i++) {
            char temp = chars[i];
            chars[i] = chars[len - 1 - i];
            chars[len - 1 - i] = temp;
        }

        return new String(chars);
    }

    /**
     * 字符区间
     */
    public static void printMuByte() {
        for (int i = '\0'; i < '\uFF6F'; i++) {
            System.out.print((char) i);
        }
    }

    /**
     * 提取字符串中的数字 : 通过正则将非数字替换成空
     * @param str 参数字符串
     * @return 返回数字字符串
     */
    public static String getNumberFromString(String str) {
        if (str == null) {
            return "";
        }
        String regEx = "[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }


    /**
     * 转换UTF-8编码
     * @param str ISO String
     * @return UTF8 String
     * @throws UnsupportedEncodingException 不支持的编码异常
     */
    public static String charset2UTF8(String str) throws UnsupportedEncodingException {
        if (str == null) {
            return null;
        }
        return changeCharset("ISO-8859-1", "UTF-8"); // 用新的字符编码生成字符串
    }

    /**
     * 转换字符串编码
     * @param str     待转编码字符串
     * @param charset 编码类型
     * @return 转后字符串
     * @throws UnsupportedEncodingException 不支持的编码异常
     */
    private static String changeCharset(String str, String charset) throws UnsupportedEncodingException {
        if (str == null) {
            return null;
        }
        return new String(str.getBytes("ISO-8859-1"), charset); // 用新的字符编码生成字符串

    }

    /**
     * 获取长度len的随机字符
     * @param len 长度
     * @return str
     */
    public static String getRandomString(int len) {
        String str = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random localRandom = new Random();
        StringBuilder localStringBuffer = new StringBuilder();
        for (int i = 0; i < len; i++) {
            int j = localRandom.nextInt(str.length());
            localStringBuffer.append(str.charAt(j));
        }
        return localStringBuffer.toString();
    }

    /**
     * 字符A包含字符B?
     * @param str      大字符
     * @param childStr 小字符
     * @return result
     */
    public static boolean contains(String str, String childStr) {
        return str.indexOf(childStr) > 0;

    }


    /**
     * Base64 格式字符串转到文件
     * @param filePath   文件存储路径
     * @param fileName   文件名
     * @param fileString 文件内容 Base64
     * @throws IOException io
     */
    public static void string2FileFromBase64Code(String filePath, String fileName, String fileString) throws IOException {
        byte[] buffer = new BASE64Decoder().decodeBuffer(fileString);
        FileOutputStream out = new FileOutputStream(filePath + fileName);
        out.write(buffer);
        out.close();
    }

    /**
     * @param filePath 文件路径
     * @param fileName 文件名
     * @param buffer   字节数组
     * @return result
     */
    public static boolean byte2File(String filePath, String fileName, byte[] buffer) {

        try {
            FileOutputStream out = new FileOutputStream(filePath + fileName);
            out.write(buffer);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * JSon对象 转成JSon字符串
     * @param src JSon对象
     * @return JSon字符串
     */
    public static String toJsonStr(Object src) {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(src);
    }

    /**
     * JSon字符串转成集合
     * @param json JSon字符串
     * @return 集合
     */
    public static Map json2Collection(String json) {
        Gson gson = new Gson();
        Map map = gson.fromJson(json, new TypeToken<Object>() {
        }.getType());
        return map;
    }


    /**
     * 正则匹配出想要的字符串
     * @param strData 数据
     * @param regex   正则
     * @return result str
     */
    private static String getRegexStr(String strData, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(strData);
        StringBuffer data = new StringBuffer();
        while (matcher.find()) {
            int i = 0;
            data.append(matcher.group(i));
            i++;

        }
        return data.toString();
    }


}
