package com.sunofbeaches.looerpager.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.sunofbeaches.looerpager.annotation.TableAnnotation;
import com.sunofbeaches.looerpager.annotation.TableAttrAnnotation;
import com.sunofbeaches.looerpager.model.DBModel;
import com.sunofbeaches.looerpager.model.Table;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class BaseDao<T extends DBModel> {

    private final DBHelper mDbHelper;
    private final SQLiteDatabase mDatabase;
    private String TAG = "BaseDao";
    private Class<T> mClass;

    public BaseDao(Context context, Class<T> mClass) {
        mDbHelper = new DBHelper(context);
        mDatabase = mDbHelper.getWritableDatabase();
        this.mClass = (Class<T>) mClass;
    }


    public boolean add(List<T> list) {
        if (list == null || list.size() == 0) {
            return true;
        }

        try {
            Table table = convertTable();
            if (table == null) {
                Log.w(TAG, "object is not set table annotation");
                return false;
            }

            mDatabase.beginTransaction();
            for (T data : list) {
                String insertSQL = getInsertSQL(data, table);
                mDatabase.execSQL(insertSQL);
            }
            mDatabase.setTransactionSuccessful();
            mDatabase.endTransaction();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return false;
        }finally {
        }

        return true;
    }


    public List<T> query() {
        List<T> list = new ArrayList<>();
        Table table = convertTable();
        Cursor cursor = mDatabase.query(table.getTableName(), null, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Object obj = getObjFromCursor(table, cursor);
                list.add((T) obj);

                cursor.moveToNext();
            }
        }

        return list;
    }

    private Object getObjFromCursor(Table table, Cursor cursor) {
        Object obj = null;
        try {
            obj = mClass.newInstance();
            List<Table.TableAttr> attrList = table.getTableAttrList();
            for (Table.TableAttr tableAttr : attrList) {
                int columnIndex = cursor.getColumnIndex(tableAttr.getDbAttrName());
                String value = cursor.getString(columnIndex);

                Field field = mClass.getDeclaredField(tableAttr.getFileName());
                field.setAccessible(true);
                Class<?> fieldType = field.getType();
                field.set(obj, getValue(value, fieldType));
            }

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        return obj;

    }

    private Object getValue(String value, Class<?> fieldType) {
        if (fieldType == Boolean.class || fieldType == boolean.class) {
            return Boolean.valueOf(value);
        } else if (fieldType == Integer.class || fieldType == int.class) {
            return Integer.valueOf(value);
        }

        return value;
    }


    private String getInsertSQL(T t, Table table) {
        StringBuffer sb = new StringBuffer();
        sb.append("insert into ").append(table.getTableName()).append("(");
        List<Table.TableAttr> attrList = table.getTableAttrList();
        for (int i = 0; i < attrList.size(); i++) {
            Table.TableAttr tableAttr = attrList.get(i);
            if (!tableAttr.isInsert()) {
                continue;
            }
            String dbAttrName = tableAttr.getDbAttrName();
            sb.append(dbAttrName);
            if (i != attrList.size() - 1) {
                sb.append(",");
            }
        }
        sb.append(") values ");
        sb.append("(");

        for (int i = 0; i < attrList.size(); i++) {
            Table.TableAttr tableAttr = attrList.get(i);
            if (!tableAttr.isInsert()) {
                continue;
            }

            try {
                String fileName = tableAttr.getFileName();
                Field field = t.getClass().getDeclaredField(fileName);
                field.setAccessible(true);
                Object value = field.get(t);
                sb.append("'").append(value).append("'");
                if (i != attrList.size() - 1) {
                    sb.append(",");
                }
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        sb.append(")");
        return sb.toString();
    }

    private Table convertTable() {
        TableAnnotation tableAnnotation = mClass.getAnnotation(TableAnnotation.class);
        if (tableAnnotation != null) {
            Table table = new Table();
            List<Table.TableAttr> mTableAttrList = new ArrayList<>();
            table.setTableAttrList(mTableAttrList);

            String tableName = tableAnnotation.tableName();
            table.setTableName(tableName);
            Field[] fields = mClass.getDeclaredFields();
            for (Field field : fields) {
                TableAttrAnnotation filedAnnotation = field.getAnnotation(TableAttrAnnotation.class);
                if (filedAnnotation == null) {
                    continue;
                }

                Table.TableAttr tableAttr = new Table.TableAttr();
                String fileName = field.getName();
                String dbAttrName = filedAnnotation.name();
                boolean insert = filedAnnotation.isInsert();
                boolean iskey = filedAnnotation.isKey();
                tableAttr.setFileName(fileName);
                tableAttr.setDbAttrName(dbAttrName);
                tableAttr.setInsert(insert);
                tableAttr.setKey(iskey);
                mTableAttrList.add(tableAttr);
            }

            return table;
        }

        return null;
    }


    /**
     * 获取表结构属性
     */
//    public DataTable iniStrutDv(String tableName, SQLiteConnection conn) {
//        DataSet ds=new DataSet();
//        conn.Open();
//        string sql="PRAGMA table_info("+tableName+")";
//        SQLiteDataAdapter sda=new SQLiteDataAdapter();
//        sda.SelectCommand=new System.Data.SQLite.SQLiteCommand();
//        sda.SelectCommand.CommandText=sql;
//        sda.SelectCommand.Connection=conn;
//        SQLiteCommandBuilder scb=newS QLiteCommandBuilder(sda);
//        sda.Fill(ds);
//        conn.Close();
//        return ds.Tables[0];
//    }


}
