package com.sunofbeaches.looerpager;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

public class BaseActivity extends Activity {

    protected ActionBar mActionBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActionBar = getActionBar();

    }


    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
