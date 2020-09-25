package com.example.mylibrary;

import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mylibrary.skin.SkinEngine;

import java.io.File;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        // 界面无标题栏
        supportRequestWindowFeature(Window.FEATURE_OPTIONS_PANEL);

        super.onCreate(savedInstanceState);

        XmlResourceParser resourceParser = getResources().getXml(R.xml.user);

    }


    // 点击换肤按钮
    public void skinAction(View view) {
        String skinPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "my.skin";
        SkinEngine.getInstance().updateSkin(skinPath);
    }

}
