package com.example.common.util;

import java.lang.reflect.Field;

public class ReflectUtils {

    public static Object getValue(Object obj, String filedName) {
        if (obj == null) {
            return null;
        }

        try {
            Field field = obj.getClass().getDeclaredField(filedName);
            field.setAccessible(true);
            Object result = field.get(obj);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
