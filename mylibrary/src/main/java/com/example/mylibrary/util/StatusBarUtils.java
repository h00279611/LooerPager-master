package com.example.mylibrary.util;

import android.app.Activity;
import android.content.res.TypedArray;

public class StatusBarUtils {

    public static void forNavigation(Activity activity) {
        TypedArray typedArray = activity.getTheme()
                .obtainStyledAttributes(0,
                        new int[]{android.R.attr.statusBarColor});


        int color = typedArray.getColor(0, 0);
        activity.getWindow().setStatusBarColor(color);
        typedArray.recycle();

    }
}
