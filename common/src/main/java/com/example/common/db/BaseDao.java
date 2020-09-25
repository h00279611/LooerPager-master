package com.example.common.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.core.util.Consumer;

import com.example.common.bean.DBModel;
import com.example.common.bean.Table;

import java.util.List;

public class BaseDao<T extends DBModel> extends BaseDataDone<T> {

    private SQLiteDatabase mDatabase;

    public BaseDao(Context context, Class<T> mClass) {
        super(context, mClass);
    }


    @Override
    protected void insert(T t, Table table, ContentValues values) {
        startTransactionTask(v -> {
            String insertSQL = getInsertSQL(values, table);
            mDatabase.execSQL(insertSQL);
        });
    }

    @Override
    protected void batchInsert(List<T> list, Table table, List<ContentValues> contentValues) {
        startTransactionTask(v -> {
            for (ContentValues contentValue : contentValues) {
                String insertSQL = getInsertSQL(contentValue, table);
                mDatabase.execSQL(insertSQL);
            }
        });
    }

    @Override
    protected void deleteAll(Table table) {
        startTransactionTask(v -> mDatabase.delete(table.getTableName(), null, null));
    }

    @Override
    protected Cursor getCursor(Table table) {
        initDataBase();
        return mDatabase.query(table.getTableName(), null, null, null, null, null, null);
    }


    private void startTransactionTask(Consumer consumer) {
        try {
            initDataBase();

            mDatabase.beginTransaction();
            consumer.accept(null);
            mDatabase.setTransactionSuccessful();
            mDatabase.endTransaction();
        } finally {
            DatabaseManager.getInstance().closeDatabase();
        }
    }

    private void initDataBase() {
        mDatabase = DatabaseManager.getInstance().openDatabase();
    }


    private String getInsertSQL(ContentValues values, Table table) {
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

            String fileName = tableAttr.getFiledName();
            Object value = values.get(fileName);
            sb.append("'").append(value).append("'");
            if (i != attrList.size() - 1) {
                sb.append(",");
            }
        }

        sb.append(")");
        return sb.toString();
    }


    private Table convertTable() {
        return TableParser.getInstance().convertTable(mClass);
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
