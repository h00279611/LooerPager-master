package com.example.mylibrary.skin;

import android.app.Activity;
import android.app.AppComponentFactory;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.reflect.Field;

public class SkinAcitivityLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {

    private static final String TAG = "SkinAcitivityLifecycleCallbacks";
    private SkinFactory mSkinFactory;

    /**
     * 会在Activity的super.onCreate()方法之后，在setContentView()之前调用此方法
     * @param activity
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onActivityCreated");


        // 因为在super.onCreate()方法中已经对LayoutInflaterCompat.setFactory2(layoutInflater, this);
        // 要想要系统使用自己的Factory，需要调用LayoutInflater.setFacoty2()调用，但是此方法有一个判断：  if (mFactorySet)直接异常，
        // 所以在调用setFactory2之前需要通过反射将mFactorySet设置为false
//        public void installViewFactory() {
//            LayoutInflater layoutInflater = LayoutInflater.from(mContext);
//            if (layoutInflater.getFactory() == null) {
//                LayoutInflaterCompat.setFactory2(layoutInflater, this);
//            } else {
//                if (!(layoutInflater.getFactory2() instanceof AppCompatDelegateImpl)) {
//                    Log.i(TAG, "The Activity's LayoutInflater already has a Factory installed"
//                            + " so we can not install AppCompat's");
//                }
//            }
//        }


        // 反射先将mFactorySet设置为false,这样才能设置自己的factory
        LayoutInflater inflater = LayoutInflater.from(activity);
        try{
            Field factorySet = LayoutInflater.class.getDeclaredField("mFactorySet");
            factorySet.setAccessible(true);
            factorySet.set(inflater, false);
        }catch (Exception e){
            e.printStackTrace();
        }

        mSkinFactory = new SkinFactory(activity);

        // 这样在调用setContentView()的时候，就会使用自定义的factory2
        inflater.setFactory2(mSkinFactory);


        // 注册观察者（监听用户操作，当点击换肤，通知所有观察者更新）
        SkinEngine.getInstance().addObserver(mSkinFactory);
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        Log.d(TAG, "onActivityStarted");
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        Log.d(TAG, "onActivityResumed");
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {
        Log.d(TAG, "onActivityPaused");
    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
        Log.d(TAG, "onActivityStopped");
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
        Log.d(TAG, "onActivitySaveInstanceState");
    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        Log.d(TAG, "onActivityDestroyed");
        SkinEngine.getInstance().deleteObserver(mSkinFactory);
    }
}
