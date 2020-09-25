package com.example.mylibrary.skin;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

public class SkinFactory implements LayoutInflater.Factory2, Observer {


    private final Activity mActivity;

    private static final String[] sClassPrefixList = {
            "android.widget.",
            "android.webkit",
            "android.app."
    };


    /**
     * 例如：new TextView(context, atrr) 或 new Button(context, attr)
     * 所以需要建立 获取控件的构造方法 参数类型，好去创建构造对象
     */
    private static Class<?>[] mConstrucotrSignature = new Class[]{Context.class, AttributeSet.class};


    /**
     * 进行缓存，因为ClassLoad getConstrucotr是耗费性能的
     */
    private static final Map<String, Constructor<? extends View>> sConsturcotrMap = new HashMap<>();

    private WidgetViewList mWidgetViewList;


    public SkinFactory(Activity activity) {
        this.mActivity = activity;
        this.mWidgetViewList = new WidgetViewList();
    }

    @Nullable
    @Override
    public View onCreateView(@Nullable View parent, @NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        // 创建系统自带的控件 如：TextView Button
        View resultView = createViewFromTag(parent, name, context, attrs);
        switch (name) {
            case "ImageView":
                resultView = new AppCompatImageView(context, attrs);
                break;
        }


        // 如果为null，可以认为是自定义view，
        if (resultView == null) {
            resultView = createView(name, "", context, attrs);
        }

        mWidgetViewList.saveWidgetView(attrs, resultView);
        return resultView;
    }

    private View createViewFromTag(View parent, String name, Context context, AttributeSet attrs) {
        View view = null;
        for (String s : sClassPrefixList) {
            view = createView(name, s, context, attrs);
            if (view != null) {
                break;
            }
        }
        return view;
    }

    private View createView(String name, String prefix, Context context, AttributeSet attrs) {
        String className = prefix + name;
        Constructor<? extends View> constructor = sConsturcotrMap.get(className);
        if (constructor == null) {
            try {
                Class<? extends View> aClass = context.getClassLoader().loadClass(className).asSubclass(View.class);
                constructor = aClass.getConstructor(mConstrucotrSignature);
                sConsturcotrMap.put(className, constructor);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        try {
            constructor.setAccessible(true);
            return constructor.newInstance(context, attrs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        return null;
    }


    @Override
    public void update(Observable o, Object arg) {

        // 收到通知开始换肤
        mWidgetViewList.skinChange();
    }


}
