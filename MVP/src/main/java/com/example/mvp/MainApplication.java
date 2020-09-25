package com.example.mvp;

import android.app.Application;

import com.example.common.db.DBFactory;

import java.util.Arrays;

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // DBInit
        DBFactory.getInstance().init(this, "mvp.db", Arrays.asList("com.example.mvp.bean"));
    }
}
