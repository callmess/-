package com.ss.utils;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

public class JSONUtils {
    private static final SerializerFeature[] CONFIG = new SerializerFeature[]{
            SerializerFeature.WriteNullBooleanAsFalse,// boolean为null时输出false
            SerializerFeature.WriteNullListAsEmpty,// list为null时输出[]
            SerializerFeature.WriteNullNumberAsZero,// number为null时输出0
            SerializerFeature.WriteNullStringAsEmpty // String为null时输出""
    };

    public static JSONObject toJson(Object object) {
        SerializeWriter out = new SerializeWriter();
        try {
            JSONSerializer serializer = new JSONSerializer(out);

            for (com.alibaba.fastjson.serializer.SerializerFeature feature : CONFIG) {
                serializer.config(feature, true);
            }
            serializer.write(object);
            return JSONObject.parseObject(out.toString());
        } finally {
            out.close();
        }
    }


    public static JSONObject toJsonNull(Object object) {
        SerializeWriter out = new SerializeWriter();
        try {
            JSONSerializer serializer = new JSONSerializer(out);

            for (com.alibaba.fastjson.serializer.SerializerFeature feature : CONFIG) {
                serializer.config(feature, true);
            }
            serializer.config(SerializerFeature.WriteEnumUsingToString, false);
            serializer.write(object);
            return JSONObject.parseObject(out.toString());
        } finally {
            out.close();
        }
    }


    public static Map<String, Object> formatdate(Map<String, Object> map) {
        for (Entry<String, Object> en : map.entrySet()) {
            if (en.getKey().contains("date") || en.getKey().contains("time") || en.getKey().contains("day")) {
                String createdate = en.getValue().toString();
                try {
                    en.setValue(createdate.substring(0, createdate.lastIndexOf(".")));
                } catch (Exception e) {
                    continue;
                }
            }
        }
        return map;
    }

    /**
     * 转换json到model
     * @param params
     * @param o
     * @return
     * @throws Exception
     */
    public static <T> Object parseJsonToObject(JSONObject params, Object o) throws Exception {
        Class<?> cl = o.getClass();
        try {
            Field[] fields = cl.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);// 设置可访问
                String name = field.getName(); // 获取属性的名字
                if (params.containsKey(name)) {
                    if (null != params.get(name) && params.get(name).toString().contains("'")) {
                        params.put(name, params.get(name).toString().replace("'", "''"));

                    }
                    String getname = name;
                    getname = getname.substring(0, 1).toUpperCase() + getname.substring(1); // 将属性的首字符大写，方便构造get，set方法
                    String type = "class java.lang.String";// 得到此属性的类型// 可以改这个:field.getType().toString();
                    Method m = null;
                    switch (type) {
                        case "int":
                            m = o.getClass().getMethod("set" + getname, int.class);
                            break;
                        case "class java.lang.String":
                            m = o.getClass().getMethod("set" + getname, String.class);
                            break;
                        case "class java.lang.Integer":
                            m = o.getClass().getMethod("set" + getname, Integer.class);
                            break;
                        case "class java.lang.Boolean":
                            m = o.getClass().getMethod("set" + getname, Boolean.class);
                            break;
                        case "class java.util.Date":
                            m = o.getClass().getMethod("set" + getname, Date.class);
                            break;
                        case "double":
                            m = o.getClass().getMethod("set" + getname, double.class);
                            break;
                        case "float":
                            m = o.getClass().getMethod("set" + getname, float.class);
                            break;

                        default:
                            break;
                    }
                    if (params.containsKey(name)) {
                        if (null == params.get(name)) {
                            params.put(name, "");
                        }
                        m.invoke(o, params.get(name).toString());
                    }
                }
            }
        } catch (Exception e) {
            o = null;
            throw e;
        }
        return o;
    }
}
