package com.example.mylibrary.skin2;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.LayoutInflaterCompat;

import com.example.mylibrary.util.ActionBarUtils;
import com.example.mylibrary.util.NavigationUtils;
import com.example.mylibrary.util.PreferencesUtils;
import com.example.mylibrary.util.StatusBarUtils;

public class SkinActivity extends AppCompatActivity {

    private CustomAppcompatViewInflater mViewInflater;
    private String key = "isNight";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (openChangeSkin()) {
            LayoutInflater layoutInflater = LayoutInflater.from(this);
            LayoutInflaterCompat.setFactory2(layoutInflater, this);
        }
        super.onCreate(savedInstanceState);

//        User user = (User) XMLUtil.convertXmlFileToObject(this, User.class, R.xml.user);
//        System.out.println(user.getUserName());
        

//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        boolean isNight = PreferencesUtils.getBoolean(this, key);
        if(isNight){
            setDayNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else{
            setDayNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

    }


    @Nullable
    @Override
    public View onCreateView(@Nullable View parent, @NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        View view = null;
        if (openChangeSkin()) {
            if (mViewInflater == null) {
                mViewInflater = new CustomAppcompatViewInflater(context);
            }
            mViewInflater.setName(name);
            mViewInflater.setAttrs(attrs);
            view = mViewInflater.autoMatch();
        }

        if (view == null) {
            view = super.onCreateView(parent, name, context, attrs);
        }
        return view;
    }

    // 子类重写此方法
    public boolean openChangeSkin() {
        return false;
    }

    //  点击换肤
    public void dayOrNight(View view){
        int uiMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;

        switch (uiMode){
            case Configuration.UI_MODE_NIGHT_NO:
                setDayNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                PreferencesUtils.putBoolean(this, key,true);
                break;

            case Configuration.UI_MODE_NIGHT_YES:
                setDayNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                PreferencesUtils.putBoolean(this, key,false);
                break;
        }

    }


    private void setDayNightMode(int modeNightYes) {
        // 会修改getResources().getConfiguration().uiMode值，再次运行时获取的值就变了
        getDelegate().setLocalNightMode(modeNightYes);

        boolean isPost21 = Build.VERSION.SDK_INT >=21;
        if(isPost21){
            // 换状态栏
            StatusBarUtils.forNavigation(this);

            // 换标题栏
            ActionBarUtils.forNavigation(this);

            // 换底部导航栏
            NavigationUtils.forNavigation(this);
        }

        View decorView = getWindow().getDecorView();
        applyDayNightForView(decorView);

    }

    private void applyDayNightForView(View view) {
        // 只对实现了ViewsMatch的接口的View进行换肤
        if(view instanceof ViewsMatch){
            ViewsMatch view1 = (ViewsMatch) view;
            view1.skinnableView();
        }

        if(view instanceof ViewGroup){
            ViewGroup viewGroup = (ViewGroup) view;
            int childCount = viewGroup.getChildCount();
            for (int i = 0; i < childCount; i++) {
                applyDayNightForView(viewGroup.getChildAt(i));
            }
        }
    }

}
