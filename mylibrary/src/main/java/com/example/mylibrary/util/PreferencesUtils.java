package com.example.mylibrary.util;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesUtils {

    private static String PREFERENCE_NAME ="com.huawei.skin";

    public static void putBoolean(Context context, String key, boolean val) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putBoolean(key, val);
        edit.commit();
    }

    public static boolean getBoolean(Context context, String key) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return preferences.getBoolean(key, false);
    }
}
