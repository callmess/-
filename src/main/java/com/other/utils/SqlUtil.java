package com.other.utils;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


/**
 * 数据查询工具
 * @author Administrator
 */
public class SqlUtil {

    /**
     * 构造插入sql语句
     * @param map   所需插入实体map
     * @param table 表名
     * @return
     */
    public static String getInsert(Map<String, Object> map, String table) {
        String sql = " INSERT INTO " + table + " ( ";
        String field = "";//查询字段
        String value = " VALUES ( ";//插入值
        int i = 0;
        for (Entry<String, Object> entry : map.entrySet()) {
            String point = ",";
            if (i == (map.size() - 1)) point = " ) ";//到达最后一个
            field += entry.getKey() + point;
            value += entry.getValue() + point;
            i++;
        }
        sql += (field + value);
        return sql;
    }


    public static String getFields(Map<String, Object> map) {
        String field = "";
        int i = 0;
        for (Entry<String, Object> entry : map.entrySet()) {
            String point = ",";
            if (i == (map.size() - 1)) {
                point = "";
            }
            field += entry.getKey() + point;
            i++;
        }
        return field;
    }


    /**
     * 构造查询sql语句
     * @param list  所需查询属性列表
     * @param table 表名
     * @return
     */
    public static String getSearch(List<String> list, String table, String where) {
        String sql = " SELECT  ";
        int i = 0;
        for (String string : list) {
            String point = ",";
            if (i == (list.size() - 1)) point = "";
            sql += (string + point);
            i++;
        }
        sql += " FROM " + table + " ";
        if (where != null) {
            sql += " " + where + " ";
        }
        return sql;

    }

    /**
     * 构造更新sql语句
     * @param map   所需更新实体map
     * @param table 表名
     * @return
     */
    public static String getUpdate(Map<String, Object> map, String table, String where) {
        String sql = " UPDATE " + table + " SET ";
        int i = 0;
        for (Entry<String, Object> entry : map.entrySet()) {
            String point = ",";
            if (i == (map.size() - 1)) point = "";
            sql += (entry.getKey() + "=" + entry.getValue() + point);
            i++;
        }
        if (where != null) {
            sql += where;
        }
        return sql;

    }


    /**
     * 构造删除sql语句
     * @param map   所需更新实体map
     * @param table 表名
     * @return
     */
    public static String getDelete(Map<String, Object> map, String table) {
        String sql = " DELETE FROM " + table + " ";
        int i = 0;
        String where = " WHERE ";
        for (Entry<String, Object> entry : map.entrySet()) {
            if (i == 0) where += (entry.getKey() + "=" + entry.getValue());
            break;
        }
        sql += where;
        return sql;
    }

    /**
     * 构造单条查询sql语句
     * @param map   所需查询属性列表
     * @param table 表名
     * @return
     */
    public static String getSearchOne(Map<String, Object> map, String table) {
        String sql = " SELECT  ";
        int i = 0;
        String where = " WHERE ";
        for (Entry<String, Object> entry : map.entrySet()) {
            if (i == 0) where += (entry.getKey() + "=" + entry.getValue());
            String point = ",";
            if (i == (map.size() - 1)) point = "";
            sql += (entry.getKey() + point);
            i++;
        }
        sql += (" FROM " + table + " " + where);
        return sql;
    }


    /**
     * 构造单条查询sql语句
     * @param field 所需查询属性列表
     * @param table 表名
     * @return
     */
    public static String getSearchOne(Map<String, Object> map, String field, String table) {
        String sql = " SELECT  ";
        if (field != null) {
            sql += field + " ,";
        }
        int i = 0;
        String where = " WHERE ";
        for (Entry<String, Object> entry : map.entrySet()) {
            if (i == 0) where += (entry.getKey() + "=" + entry.getValue());
            String point = ",";
            if (i == (map.size() - 1)) point = "";
            sql += (entry.getKey() + point);
            i++;
        }
        sql += (" FROM " + table + " " + where);
        return sql;
    }


    /**
     * 调用存储过程
     * @param map   传入参数
     * @param table 表名
     * @return
     */
    public static String getProcedure(Map<String, Object> map, String table) {
        String sql = " EXEC " + table + " ";
        if (null != map) {
            int i = 0;
            for (Entry<String, Object> entry : map.entrySet()) {
                String point = ",";
                if (i == (map.size() - 1)) point = "";
                if (entry.getKey().contains("date") || entry.getKey().contains("day") || entry.getKey().contains("time")) {
                    if ("''".equals(entry.getValue().toString().trim())) {
                        System.out.println(entry.getKey());
                        sql += ("null" + point);
                        i++;
                        continue;
                    }
                }
                sql += ("" + entry.getValue() + "" + point);
                i++;
            }
        }
        return sql;

    }


    /**
     * 调用存储过程 （操作）
     * @param map     传入参数
     * @param table   表名
     * @param inttype 操作类型
     * @return
     */
    public static String alterProcedure(Map<String, Object> map, String table, int inttype) {
        String sql = " EXEC " + table + " ";
        int i = 0;
        switch (inttype) {
            case 1://添加
                if (map.containsKey("id")) {
                    map.remove("id");
                }
                if (map.containsKey("createdate")) {
                    map.remove("createdate");
                }
                if (map.containsKey("editdate")) {
                    map.remove("editdate");
                }
                if (map.containsKey("editdate")) {
                    map.remove("editdate");
                }
                for (Entry<String, Object> entry : map.entrySet()) {
                    String point = ",";
                    if (i == (map.size() - 1)) point = "";
                    if (entry.getKey().contains("date") || entry.getKey().contains("day") || entry.getKey().contains("time")) {
                        if ("''".equals(entry.getValue().toString().trim())) {
                            System.out.println(entry.getKey() + "=" + entry.getValue());
                            sql += ("null" + point);
                            i++;
                            continue;
                        }
                    }

                    sql += ("" + entry.getValue() + "" + point);
                    i++;
                }
                break;
            case 2://修改
                if (map.containsKey("createdate")) {
                    map.remove("createdate");
                }
                if (map.containsKey("editdate")) {
                    map.remove("editdate");
                }
                if (map.containsKey("creator")) {
                    map.remove("creator");
                }
                for (Entry<String, Object> entry : map.entrySet()) {
                    String point = ",";
                    if (i == (map.size() - 1)) point = "";
                    if (entry.getKey().contains("date") || entry.getKey().contains("day") || entry.getKey().contains("time")) {
                        if ("''".equals(entry.getValue().toString().trim())) {
                            System.out.println(entry.getKey());
                            sql += ("null" + point);
                            i++;
                            continue;
                        }
                    }
                    sql += ("" + entry.getValue() + "" + point);
                    i++;
                }
                break;
            case 3://删除
                if (map.containsKey("id")) {
                    sql += map.get("id");
                }
                break;

        }
        return sql;
    }


}
