package com.example.common.util;

import android.content.Context;
import android.util.TypedValue;

public class ViewUtils {


    /**
     * 将像素转换成dp
     *
     * @param context
     * @param w
     * @return
     */
    public static int getDpValue(Context context, int w) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, w, context.getResources().getDisplayMetrics());
    }

}
