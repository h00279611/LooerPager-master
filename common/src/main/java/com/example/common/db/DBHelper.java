package com.example.common.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.common.bean.Table;
import com.example.common.constants.DBConstants;

import java.util.List;


public class DBHelper extends SQLiteOpenHelper {

    private static final String TAG = "DBHelper";

    private static final String DATABASE_NAME = "test.db";  //数据库名字
    private static final int DATABASE_VERSION = 2;         //数据库版本号,此值只能越来越大，不能变小

    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public DBHelper(Context context, String dbName) {
        super(context, dbName, null, DATABASE_VERSION);
    }

    /**
     * 第一次创建的时候调用
     *
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "onCreate");
        // 创建的时候回调
//        db.execSQL(DBConstants.CREATE_COMPANY_TABLE);
//        db.execSQL(DBConstants.CREATE_USER_PRIVATE_COMPANY_TABLE);
    }

    /**
     * 只有新的版本比老的版本大，才会调用此方法；当需要调用此方法时，需要更改DATABASE_VERSION
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(TAG, "onUpgrade");

        // 升级数据库回调
        switch (oldVersion) {
            case 1:
                db.execSQL(DBConstants.UPDATE_COMPANY_TABLE);
                break;
            case 2:
                break;
        }
    }


}
