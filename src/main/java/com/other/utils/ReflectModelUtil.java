package com.other.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.Map.Entry;


/**
 * 反射工具类
 * @author Administrator
 */
public class ReflectModelUtil {


    /**
     * 通过反射初始化实体，返回实体map
     * @param object
     * @return Map<String ,   Object>
     */
    public static Map<String, Object> getformatObject(Object object) {
        Class<?> cl = object.getClass();
        Map<String, Object> model = null;
        try {
            model = new LinkedHashMap<String, Object>();
            Field[] fields = cl.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);// 设置可访问
                String name = field.getName(); // 获取属性的名字
                String getname = name;
                getname = getname.substring(0, 1).toUpperCase() + getname.substring(1); // 将属性的首字符大写，方便构造get，set方法
                String type = field.getType().toString();// 得到此属性的类型
                Object value = ReflectModelUtil.setModelValue(object, type, getname);
                model.put(name, value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return model;
    }


    /**
     * 通过反射初始化实体，返回实体map
     * @param object
     * @return Map<String ,   Object>
     */
    public static Map<String, Object> initformatObject(Object object) {
        Class<?> cl = object.getClass();
        Map<String, Object> model = null;
        try {
            model = new HashMap<String, Object>();
            Field[] fields = cl.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);// 设置可访问
                String name = field.getName(); // 获取属性的名字
                String getname = name;
                getname = getname.substring(0, 1).toUpperCase() + getname.substring(1); // 将属性的首字符大写，方便构造get，set方法
                String type = field.getType().toString();// 得到此属性的类型
                Object value = ReflectModelUtil.initModelValue(object, type, getname);
                model.put(name, value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return model;
    }

    /**
     * 通过反射初始化实体，返回实体属性列表
     * @param object
     * @return List<String>
     */
    public static List<String> getObjectField(Object object) {
        Class<?> cl = object.getClass();
        List<String> model = null;
        try {
            model = new ArrayList<String>();
            Field[] fields = cl.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);// 设置可访问
                String name = field.getName(); // 获取属性的名字
                model.add(name);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return model;
    }

    /**
     * 初始化设置实体属性值
     * @param object
     * @param type    属性类型
     * @param getname get方法名
     * @return
     * @throws Exception
     */
    private static Object setModelValue(Object object, String type, String getname) throws Exception {
        Object val = null;
        if (type.equals("int")) { // 如果type是类类型，则前面包含"class "，后面跟类名
            Method m = object.getClass().getMethod("get" + getname);
            int value = (int) m.invoke(object); // 调用getter方法获取属性值
            if (value == 0) {
                m = object.getClass().getMethod("set" + getname, int.class);
                m.invoke(object, 0);
            }
            val = value;
        }
        if (type.equals("class java.lang.String")) { // 如果type是类类型，则前面包含"class "，后面跟类名
            Method m = object.getClass().getMethod("get" + getname);
            String value = (String) m.invoke(object); // 调用getter方法获取属性值
            if (value == null) {
                value = "";
                m = object.getClass().getMethod("set" + getname, String.class);
                m.invoke(object, "");
            }
            val = "'" + value + "'";
        }

        if (type.equals("class java.lang.Integer")) {
            Method m = object.getClass().getMethod("get" + getname);
            Integer value = (Integer) m.invoke(object);
            if (value == null) {
                m = object.getClass().getMethod("set" + getname, Integer.class);
                m.invoke(object, 0);
            }
            val = value;
        }
        if (type.equals("class java.lang.Boolean")) {
            Method m = object.getClass().getMethod("get" + getname);
            Boolean value = (Boolean) m.invoke(object);
            if (value == null) {
                m = object.getClass().getMethod("set" + getname, Boolean.class);
                m.invoke(object, false);
            }
            val = value;
        }
        if (type.equals("class java.util.Date")) {
            Method m = object.getClass().getMethod("get" + getname);
            Date value = (Date) m.invoke(object);
            if (value == null) {
                m = object.getClass().getMethod("set" + getname, Date.class);
                m.invoke(object, new Date());
            }
            val = value;
        }

        if (type.equals("double")) {
            Method m = object.getClass().getMethod("get" + getname);
            double value = (double) m.invoke(object);
            if (value == 0) {
                m = object.getClass().getMethod("set" + getname, double.class);
                m.invoke(object, 0);
            }
            val = value;
        }
        if (type.equals("float")) {
            Method m = object.getClass().getMethod("get" + getname);
            float value = (float) m.invoke(object);
            if (value == 0) {
                m = object.getClass().getMethod("set" + getname, float.class);
                m.invoke(object, 0);
            }
            val = value;
        }
        return val;
    }


    /**
     * 通过反射初始化实体，返回实体map
     * @param object
     * @return Map<String ,   Object>
     */
    public static Map<String, Object> getformatObject(Map<String, Object> o, Object object) {
        Class<?> cl = object.getClass();
        Map<String, Object> model = null;
        try {
            model = new LinkedHashMap<String, Object>();
            Field[] fields = cl.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);// 设置可访问
                String name = field.getName(); // 获取属性的名字
                for (Entry<String, Object> en : o.entrySet()) {
                    if (en.getKey().equals(name)) {
                        model.put(name, en.getValue());
                    }
                }
                if (!model.containsKey(name)) {
                    model.put(name, "");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return model;
    }


    /**
     * 初始化设置实体属性值
     * @param object
     * @param type    属性类型
     * @param getname get方法名
     * @return
     * @throws Exception
     */
    private static Object initModelValue(Object object, String type, String getname) throws Exception {
        Object val = null;
        if (type.equals("int")) { // 如果type是类类型，则前面包含"class "，后面跟类名
            Method m = object.getClass().getMethod("get" + getname);
            int value = (int) m.invoke(object); // 调用getter方法获取属性值
            if (value == 0) {
                m = object.getClass().getMethod("set" + getname, int.class);
                m.invoke(object, 0);
            }
            val = value;
        }
        if (type.equals("class java.lang.String")) { // 如果type是类类型，则前面包含"class "，后面跟类名
            Method m = object.getClass().getMethod("get" + getname);
            String value = (String) m.invoke(object); // 调用getter方法获取属性值
            if (value == null) {
                value = "";
                m = object.getClass().getMethod("set" + getname, String.class);
                m.invoke(object, "");
            }
            val = "" + value + "";
        }

        if (type.equals("class java.lang.Integer")) {
            Method m = object.getClass().getMethod("get" + getname);
            Integer value = (Integer) m.invoke(object);
            if (value == null) {
                m = object.getClass().getMethod("set" + getname, Integer.class);
                m.invoke(object, 0);
            }
            val = value;
        }
        if (type.equals("class java.lang.Boolean")) {
            Method m = object.getClass().getMethod("get" + getname);
            Boolean value = (Boolean) m.invoke(object);
            if (value == null) {
                m = object.getClass().getMethod("set" + getname, Boolean.class);
                m.invoke(object, false);
            }
            val = value;
        }
        if (type.equals("class java.util.Date")) {
            Method m = object.getClass().getMethod("get" + getname);
            Date value = (Date) m.invoke(object);
            if (value == null) {
                m = object.getClass().getMethod("set" + getname, Date.class);
                m.invoke(object, new Date());
            }
            val = value;
        }

        if (type.equals("double")) {
            Method m = object.getClass().getMethod("get" + getname);
            double value = (double) m.invoke(object);
            if (value == 0) {
                m = object.getClass().getMethod("set" + getname, double.class);
                m.invoke(object, 0);
            }
            val = value;
        }

        return val;
    }

    /**
     * map 转为实体
     * @param map
     * @param beanClass
     * @return
     */
    public static Object mapToObject(Map<String, Object> map, Class<?> beanClass) {
        Object obj = null;
        if (map == null)
            return null;

        try {
            obj = beanClass.newInstance();
            org.apache.commons.beanutils.BeanUtils.populate(obj, map);
        } catch (Exception e) {
            // TODO: handle exception
        }

        return obj;
    }


}
