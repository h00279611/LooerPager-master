package com.example.skin.packagea;

import android.app.Application;

import com.example.skin.packagea.skin.SkinManager;

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        SkinManager.init(this);
    }
}
