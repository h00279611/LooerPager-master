package com.example.common.cache;

import com.example.common.bean.Table;

import java.util.HashMap;
import java.util.Map;

public class DBCache {

    private static Map<String, Table> tableMap = new HashMap<>();

    public static void addTable(Class cls, Table table) {
        String filePath = getKey(cls);
        tableMap.put(filePath, table);
    }

    public static Table getTable(Class cls) {
        String filePath = getKey(cls);
        return tableMap.get(filePath);
    }

    private static String getKey(Class cls) {
        return cls.getName();
    }
}
