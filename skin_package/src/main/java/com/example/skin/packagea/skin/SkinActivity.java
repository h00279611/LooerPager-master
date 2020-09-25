package com.example.skin.packagea.skin;

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

import com.example.skin.packagea.utils.ActionBarUtils;
import com.example.skin.packagea.utils.NavigationUtils;
import com.example.skin.packagea.utils.PreferencesUtils;
import com.example.skin.packagea.utils.StatusBarUtils;


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

    /**
     * App侧调用
     * @param skinPath
     * @param themColor
     */
    public void skinDynamic(String skinPath, int themColor){
        // 加载皮肤包
        SkinManager.getInstance().loaderSkinResources(skinPath);

        if(themColor!=0){
            // 获取皮肤包对应的颜色
            int color = SkinManager.getInstance().getColor(themColor);

            StatusBarUtils.forNavigation(this, color);

            // 换标题栏
            ActionBarUtils.forNavigation(this, color);

            // 换底部导航栏
            NavigationUtils.forNavigation(this, color);
        }

        applyViews(getWindow().getDecorView());
    }




    //  点击换肤(APP调用，只能白天黑夜2种更换)
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

        applyViews(getWindow().getDecorView());

    }

    private void applyViews(View view) {
        // 只对实现了ViewsMatch的接口的View进行换肤
        if(view instanceof ViewsMatch){
            ViewsMatch view1 = (ViewsMatch) view;
            view1.skinnableView();
        }

        if(view instanceof ViewGroup){
            ViewGroup viewGroup = (ViewGroup) view;
            int childCount = viewGroup.getChildCount();
            for (int i = 0; i < childCount; i++) {
                applyViews(viewGroup.getChildAt(i));
            }
        }
    }

}
