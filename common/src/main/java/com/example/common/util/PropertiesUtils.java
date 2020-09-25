package com.example.common.util;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertiesUtils {

    private static Map<String, Properties> proMap = new HashMap<>();


    // path: /assets/sdk_param_config.properties
    public static void loadProperties(String path) {
        try {
            InputStream in = PropertiesUtils.class.getResourceAsStream(path);
            Properties properties = new Properties();
            properties.load(in);
            proMap.put(path, properties);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            Log.e("PropertiesUtils", "get nothing");
            e.printStackTrace();
        }
    }

    public static Properties getProperties(String path) {
        Properties properties = proMap.get(path);
        if (properties == null) {
            loadProperties(path);
            return proMap.get(path);
        }
        return properties;
    }

    public static Object getValue(String path, String key) {
        Properties properties = getProperties(path);
        if (properties != null) {
            return properties.get(key);
        }
        return null;
    }
}