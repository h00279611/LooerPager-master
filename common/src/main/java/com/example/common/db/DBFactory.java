package com.example.common.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.common.annotation.TableAnnotation;
import com.example.common.bean.Singleton;
import com.example.common.bean.Table;
import com.example.common.constants.EnumDBType;
import com.example.common.util.ClassUtil;

import java.util.List;

public class DBFactory {

    private BaseDataDone mBaseDao;

    private static Singleton<DBFactory> mSingleton = new Singleton<DBFactory>() {
        @Override
        protected DBFactory create() {
            return new DBFactory();
        }
    };


    /**
     * 创建数据库及表并升级表结构
     *
     * @param dbName
     * @param dbClassPackages
     */
    public void init(Context context, String dbName, List<String> dbClassPackages) {
        //  创建数据库
        DatabaseManager.getInstance().init(context, dbName);

        for (String dbClassPackage : dbClassPackages) {
            List<Class<?>> annotationList = ClassUtil.getAllClassByAnnotation(context, dbClassPackage, TableAnnotation.class);
            for (Class<?> aClass : annotationList) {
                Table table = TableParser.getInstance().convertTable(aClass);
                DatabaseManager.getInstance().createAndUpgradeTable(aClass, table);
            }

        }

    }


    public BaseDataDone getBaseDao(Context context, Class mClass, EnumDBType dbType) {
        if (dbType == EnumDBType.SQLLITE) {
            mBaseDao = new BaseDao<>(context, mClass);
        } else {
            mBaseDao = new BaseContentResolver<>(context, mClass);
        }

        return mBaseDao;
    }


    private DBFactory() {

    }

    public static DBFactory getInstance() {
        return mSingleton.get();
    }


}
