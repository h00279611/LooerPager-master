package com.sunofbeaches.looerpager;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.mylibrary.BaseApplication;
import com.sunofbeaches.looerpager.aspect.trackpoint.TrackPoint;
import com.sunofbeaches.looerpager.aspect.trackpoint.TrackPointCallBack;
import com.sunofbeaches.looerpager.db.DBHelper;
import com.sunofbeaches.looerpager.manager.CompanyManager;
import com.sunofbeaches.looerpager.model.Company;

import java.util.ArrayList;
import java.util.List;


public class MainApp extends Application {
    private static final String TAG = "LOGCAT";

    @Override
    public void onCreate() {
        super.onCreate();
        addTackPoint();
        addDB();
    }

    private void addDB() {
      // 插入数据字典表数据
        CompanyManager companyManager = new CompanyManager(this);
        List<Company> list = new ArrayList<>();
        list.add(new Company("QQ"));
        list.add(new Company("微信"));
        list.add(new Company("华为"));
        list.add(new Company("中兴"));
        list.add(new Company("阿里"));
        list.add(new Company("腾讯"));
        companyManager.setCompanyListener(new CompanyManager.CompanyListener() {
            @Override
            public void addCompany(boolean status) {
                Log.i(TAG, "insert company status:"+ status);
            }
        });
        companyManager.add(list);
    }

    private void addTackPoint() {
        TrackPoint.init(new TrackPointCallBack() {
            @Override
            public void onClick(String pageClassName, String viewIdName) {
                Log.d(TAG, "onClick: " + pageClassName + "-" + viewIdName);
                //添加你的操作
            }

            @Override
            public void onPageOpen(String pageClassName) {
                Log.d(TAG, "onPageOpen: " + pageClassName);
                //添加你的操作
        }

            @Override
            public void onPageClose(String pageClassName) {
                Log.d(TAG, "onPageClose: " + pageClassName);
                //添加你的操作
            }
        });
    }


}
