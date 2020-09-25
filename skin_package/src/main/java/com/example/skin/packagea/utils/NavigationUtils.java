package com.example.skin.packagea.utils;

import android.app.Activity;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;

public class NavigationUtils {

    public static void forNavigation(Activity activity) {
        TypedArray typedArray = activity.getTheme()
                .obtainStyledAttributes(0,
                        new int[]{android.R.attr.statusBarColor});


        int color = typedArray.getColor(0, 0);
        activity.getWindow().setNavigationBarColor(color);
        typedArray.recycle();

    }


    public static void  forNavigation(Activity activity, int color){
        activity.getWindow().setNavigationBarColor(color);
    }

}
