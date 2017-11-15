package com.other.utils.md5;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DESUtils {
    public final static String desKey = "87fce399d54e92a1d93b09d49a9da273";

    /**
     * 解密
     * @param message
     * @return
     * @throws Exception
     */
    public static String decrypt(String message) throws Exception {
        SecureRandom random = new SecureRandom();
        byte[] bytesrc = convertHexString(message);
        Cipher cipher = Cipher.getInstance("DES");
        DESKeySpec desKeySpec = new DESKeySpec(getDifferday("yyyyMMdd", 0).getBytes("UTF-8"));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, random);
        byte[] retByte = cipher.doFinal(bytesrc);
        return new String(retByte);
    }

    /**
     * 加密
     * @return str
     * @throws Exception ex
     */
    public static String encrypt() throws Exception {
        SecureRandom random = new SecureRandom();
        Cipher cipher = Cipher.getInstance("DES");
        DESKeySpec desKeySpec = new DESKeySpec(getDifferday("yyyyMMdd", 0).getBytes("UTF-8"));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, random);
        return toHexString(cipher.doFinal(desKey.getBytes("UTF-8")));
    }

    /**
     * 哈希解码
     * @param ss
     * @return
     */
    public static byte[] convertHexString(String ss) {
        byte digest[] = new byte[ss.length() / 2];
        for (int i = 0; i < digest.length; i++) {
            String byteString = ss.substring(2 * i, 2 * i + 2);
            int byteValue = Integer.parseInt(byteString, 16);
            digest[i] = (byte) byteValue;
        }
        return digest;
    }

    /**
     * 哈希编码
     * @param b
     * @return
     */
    public static String toHexString(byte b[]) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            String plainText = Integer.toHexString(0xff & b[i]);
            if (plainText.length() < 2)
                plainText = "0" + plainText;
            hexString.append(plainText);
        }
        return hexString.toString();
    }


    /**
     * 获取相差天数
     * @param format 日期格式
     * @param differ 0为今天
     * @return
     */
    private static String getDifferday(String format, int differ) {
        Date date = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendar.DATE, differ);
        date = calendar.getTime();
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(date);
    }

}
