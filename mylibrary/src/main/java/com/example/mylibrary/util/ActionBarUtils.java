package com.example.mylibrary.util;

import android.app.ActionBar;
import android.app.Activity;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;

public class ActionBarUtils {

    public static void forNavigation(Activity activity) {
        TypedArray typedArray = activity.getTheme().obtainStyledAttributes(
                new int[]{android.R.attr.colorPrimary});
        int color = typedArray.getInt(0, 0);
        typedArray.recycle();

        ActionBar actionBar = activity.getActionBar();
        if(actionBar!=null){
            actionBar.setBackgroundDrawable(new ColorDrawable(color));
        }

    }
}
