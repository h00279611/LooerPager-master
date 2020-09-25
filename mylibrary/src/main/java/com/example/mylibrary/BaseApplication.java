package com.example.mylibrary;

import android.app.Application;

import com.example.mylibrary.skin.SkinEngine;

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        try {
            SkinEngine.getInstance().init(this);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

}
