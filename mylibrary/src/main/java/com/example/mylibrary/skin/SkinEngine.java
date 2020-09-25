package com.example.mylibrary.skin;

import android.app.Application;

import java.util.Observable;

public class SkinEngine extends Observable {

    private static SkinEngine mSkinEngine;
    private SkinAcitivityLifecycleCallbacks mLifecycleCallbacks;
    private Application mApplication;


    private SkinEngine() {
    }

    public synchronized static SkinEngine getInstance() {
        if (mSkinEngine == null) {
            mSkinEngine = new SkinEngine();
        }
        return mSkinEngine;
    }


    public void init(Application application) throws IllegalAccessException {
        if(application == null){
            throw new IllegalAccessException("application is null");
        }

        mApplication = application;
        mLifecycleCallbacks = new SkinAcitivityLifecycleCallbacks();
        // 注册Activity的生命周期监听
        application.registerActivityLifecycleCallbacks(mLifecycleCallbacks);

    }


    /**
     * 用户点击换肤按钮
     *
     * @param skinPath
     */
    public void updateSkin(String skinPath){
        SkinResource.getInstance().setSkinResources(mApplication, skinPath);

        // 通知所有的观察者，有变更:
        setChanged();
        notifyObservers();
    }

}
