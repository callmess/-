package com.ss.utils;


import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.util.HashMap;
import java.util.Map;


public class GetArea {
    public static void main(String[] args) {
        String host = "http://district.market.alicloudapi.com";
        String path = "/v3/config/district";
        String method = "GET";
        String appcode = "9692dd9cf0414704a80a47313094bc12";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<String, String>();
//        querys.put("callback", "callback");
        querys.put("extensions", "base");
        querys.put("filter", "filter");
        querys.put("offset", "20");
        querys.put("output", "JSON");
        querys.put("page", "1");
        querys.put("showbiz", "true");
        querys.put("subdistrict", "0");
        querys.put("keywords", "440100");


        try {
            /**
             * 重要提示如下:
             * HttpUtils请从
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
             * 下载
             *
             * 相应的依赖请参照
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
             */
            HttpResponse response = com.ss.utils.HttpUtils.doGet(host, path, method, headers, querys);
            System.out.println(response.toString());
            //获取response的body
            System.out.println(EntityUtils.toString(response.getEntity()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
