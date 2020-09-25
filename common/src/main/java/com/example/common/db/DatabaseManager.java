package com.example.common.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.common.bean.DBTableAttr;
import com.example.common.bean.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class DatabaseManager {

    private AtomicInteger mOpenCounter = new AtomicInteger();

    private static DatabaseManager instance;
    private static DBHelper mDatabaseHelper;
    private SQLiteDatabase mDatabase;

    public synchronized void init(Context context, String dbName) {
        mDatabaseHelper = new DBHelper(context, dbName);
    }


    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }

        return instance;
    }


    public synchronized SQLiteDatabase openDatabase() {
        if (mOpenCounter.incrementAndGet() == 1) {
            mDatabase = mDatabaseHelper.getWritableDatabase();
        }
        return mDatabase;
    }

    public synchronized void closeDatabase() {
        if (mOpenCounter.decrementAndGet() == 0) {
            Log.d("DatabaseManager", "closeDatabase");
            mDatabase.close();
        }
    }


    //CREATE TABLE IF NOT EXISTS tal_company(_id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR)
    private String packageCreateSQL(Table table) {
        StringBuffer sb = new StringBuffer("CREATE TABLE IF NOT EXISTS ");
        sb.append(table.getTableName()).append("(");

        List<Table.TableAttr> tableAttrList = table.getTableAttrList();
        for (Table.TableAttr tableAttr : tableAttrList) {
            String dbAttrName = tableAttr.getDbAttrName();
            Class<?> filedType = tableAttr.getFiledType();
            sb.append(dbAttrName).append(" ").append(getType(filedType)).append(",");
        }

        sb.deleteCharAt(sb.length() - 1);
        sb.append(")");

        return sb.toString();
    }


    private String getType(Class<?> filedType) {
        if (filedType == String.class) {
            return "VARCHAR";
        } else if (filedType == int.class || filedType == Integer.class) {
            return "INTEGER";
        } else if (filedType == boolean.class || filedType == Boolean.class) {
            return "BOOLEAN";
        }

        return "VARCHAR";

    }


    public void createAndUpgradeTable(Class<?> aClass, Table table) {

        try {
            SQLiteDatabase sqLiteDatabase = DatabaseManager.getInstance().openDatabase();
            createTable(sqLiteDatabase, table);

            // 当前数据库表中的字段名和类型
            List<DBTableAttr> currDBTableAttrs = getTableAttrs(table, sqLiteDatabase);

            // 比较当前表中的字段名和类型与注解类中的字段比较，实现自动升级表字段
            // TODO


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseManager.getInstance().closeDatabase();
        }

    }

    private List<DBTableAttr> getTableAttrs(Table table, SQLiteDatabase sqLiteDatabase) {
        String querySQL = String.format("pragma table_info(%s)", table.getTableName());
        Cursor cursor = sqLiteDatabase.rawQuery(querySQL, null);

        List<DBTableAttr> list = new ArrayList<>();

        while (cursor.moveToNext()) {
            String[] columnNames = cursor.getColumnNames();
            DBTableAttr dbTableAttr = new DBTableAttr();
            dbTableAttr.setName(cursor.getString(1));
            dbTableAttr.setType(cursor.getString(2));
            list.add(dbTableAttr);
            Log.w("tag", "attrName:" + cursor.getString(1) + ", attrType:" + cursor.getString(2));
        }
        cursor.close();

        return list;
    }


    private void createTable(SQLiteDatabase sqLiteDatabase, Table table) {
        String createSQL = packageCreateSQL(table);
        sqLiteDatabase.execSQL(createSQL);
    }
}