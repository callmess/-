package com.ss.utils;

import org.apache.log4j.Logger;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class HttpURLConnectionUtils {
    private static Logger logger = Logger.getLogger(HttpURLConnectionUtils.class);

    /**
     * @param url         WebService服务的请求地址
     * @param soapXml     请求报文
     * @param propertyMap 请求参数
     * @return 返回报文
     * @throws IOException 异常
     */
    private static String executeHttpServer(String url, String soapXml, Map<String, String> propertyMap) throws IOException {
        logger.debug(soapXml);
        URL serUrl = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) serUrl.openConnection(); //建立连接，并将连接强转为Http连接
        conn.setRequestMethod("POST"); // 设置请求方式
        conn.setConnectTimeout(50000);
        conn.setDoOutput(true);   //是否有出参
        for (Map.Entry<String, String> entry : propertyMap.entrySet()) {
            conn.setRequestProperty(entry.getKey(), entry.getValue());
        }
        if (null != soapXml) {
            byte[] entitydata = soapXml.getBytes();
            conn.setRequestProperty("Content-length", String.valueOf(entitydata.length));
            OutputStreamWriter outputStream = new OutputStreamWriter(conn.getOutputStream(), "ISO-8859-1");
            outputStream.write(soapXml);
            outputStream.flush();
            outputStream.close();
        }
        int code = conn.getResponseCode();
        String responString = "";
        InputStream inputStream;
        if (code == 200) {
            inputStream = conn.getInputStream();
        } else {
            inputStream = conn.getErrorStream();
        }
        String sCurrentLine;
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "ISO-8859-1"));

        while ((sCurrentLine = reader.readLine()) != null) {
            responString += sCurrentLine;
        }
        logger.debug(responString);
        conn.disconnect();
        return responString;
    }



    public static String executeHttpServer(String url) throws IOException {

        URL serUrl = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) serUrl.openConnection(); //建立连接，并将连接强转为Http连接
        conn.setRequestMethod("GET"); // 设置请求方式
        conn.setConnectTimeout(50000);
        conn.setDoOutput(true);   //是否有出参
        OutputStreamWriter outputStream = new OutputStreamWriter(conn.getOutputStream(), "utf-8");
        outputStream.flush();
        outputStream.close();
        int code = conn.getResponseCode();
        String responString = "";
        InputStream inputStream;
        if (code == 200) {
            inputStream = conn.getInputStream();
        } else {
            inputStream = conn.getErrorStream();
        }
        String sCurrentLine;
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));

        while ((sCurrentLine = reader.readLine()) != null) {
            responString += sCurrentLine;
        }
        System.out.println(responString);
        conn.disconnect();
        return responString;
    }
    /**
     * 调用WebService服务
     * @param soapXml 请求报文
     * @param url     服务地址
     * @return 返回报文
     * @throws IOException 异常
     */
    public static String executeHttpServer(String soapXml, String url) throws IOException {
        Map<String, String> propertyMap = new HashMap();
        propertyMap.put("Content-Type", "text/xml");
        propertyMap.put("chartset", "UTF-8");
        return executeHttpServer(url, soapXml, propertyMap);
    }


}
