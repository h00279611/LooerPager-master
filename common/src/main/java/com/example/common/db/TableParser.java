package com.example.common.db;

import com.example.common.annotation.TableAnnotation;
import com.example.common.annotation.TableAttrAnnotation;
import com.example.common.bean.Singleton;
import com.example.common.bean.Table;
import com.example.common.cache.DBCache;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class TableParser {

    private TableParser() {
    }


    private static Singleton<TableParser> mSingleton = new Singleton<TableParser>() {
        @Override
        protected TableParser create() {
            return new TableParser();
        }
    };


    public static TableParser getInstance() {
        return mSingleton.get();
    }


    public Table convertTable(Class mClass) {
        TableAnnotation tableAnnotation = (TableAnnotation) mClass.getAnnotation(TableAnnotation.class);
        if (tableAnnotation != null) {
            Table cacheTable = DBCache.getTable(mClass);
            if (cacheTable != null) {
                return cacheTable;
            }

            Table table = parseTable(mClass, tableAnnotation);
            DBCache.addTable(mClass, table);
            return table;
        }

        return null;
    }

    private Table parseTable(Class mClass, TableAnnotation tableAnnotation) {
        Table table = new Table();
        List<Table.TableAttr> mTableAttrList = new ArrayList<>();
        table.setTableAttrList(mTableAttrList);

        String tableName = tableAnnotation.tableName();
        String uri = tableAnnotation.uri();
        table.setTableName(tableName);
        table.setUrl(uri);

        Field[] fields = mClass.getDeclaredFields();
        for (Field field : fields) {
            TableAttrAnnotation filedAnnotation = field.getAnnotation(TableAttrAnnotation.class);
            if (filedAnnotation == null) {
                continue;
            }

            Table.TableAttr tableAttr = new Table.TableAttr();
            String fileName = field.getName();
            Class<?> type = field.getType();
            String dbAttrName = filedAnnotation.name();
            boolean insert = filedAnnotation.isInsert();
            boolean iskey = filedAnnotation.isKey();
            tableAttr.setFiledName(fileName);
            tableAttr.setDbAttrName(dbAttrName);
            tableAttr.setFiledType(type);
            tableAttr.setInsert(insert);
            tableAttr.setKey(iskey);
            mTableAttrList.add(tableAttr);
        }
        return table;
    }


}
