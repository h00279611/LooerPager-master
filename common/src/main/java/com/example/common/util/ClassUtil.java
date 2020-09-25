package com.example.common.util;


import android.content.Context;

import java.io.File;
import java.io.FileFilter;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import dalvik.system.DexFile;

public class ClassUtil {


    /**
     * 通过接口名取得某个接口下所有实现这个接口的类
     */
    public static List<Class<?>> getAllClassByInterface(Context context, Class<?> c) {
        List<Class<?>> returnClassList = null;

        if (c.isInterface()) {
            // 获取当前的包名
            String packageName = c.getPackage().getName();
            // 获取当前包下以及子包下所以的类
            List<Class<?>> allClass = getClasses(context, packageName);
            if (allClass != null) {
                returnClassList = new ArrayList<Class<?>>();
                for (Class<?> cls : allClass) {
                    // 判断是否是同一个接口
                    if (c.isAssignableFrom(cls)) {
                        // 本身不加入进去
                        if (!c.equals(cls)) {
                            returnClassList.add(cls);
                        }
                    }
                }
            }
        }

        return returnClassList;
    }


    public static List<Class<?>> getAllClassByAnnotation(Context context, String packageName, Class<? extends Annotation> c) {
        List<Class<?>> returnClassList = null;
        if (c.isAnnotation()) {
            returnClassList = new ArrayList<Class<?>>();

            // 获取当前包下以及子包下所以的类
            List<Class<?>> allClass = getClasses(context, packageName);
            for (Class<?> cls : allClass) {
                if (cls.isAnnotationPresent(c)) {
                    returnClassList.add(cls);
                }

            }

        }
        return returnClassList;
    }


    /**
     * 取得某一类所在包的所有类名 不含迭代
     */
    public static String[] getPackageAllClassName(String classLocation, String packageName) {
        // 将packageName分解
        String[] packagePathSplit = packageName.split("[.]");
        String realClassLocation = classLocation;
        int packageLength = packagePathSplit.length;
        for (int i = 0; i < packageLength; i++) {
            realClassLocation = realClassLocation + File.separator + packagePathSplit[i];
        }
        File packeageDir = new File(realClassLocation);
        if (packeageDir.isDirectory()) {
            String[] allClassName = packeageDir.list();
            return allClassName;
        }
        return null;
    }

    /**
     * 从包package中获取所有的Class
     *
     * @param packageName
     * @return
     */
    public static List<Class<?>> getClasses(Context context, String packageName) {
        ArrayList<Class<?>> classes = new ArrayList<Class<?>>();
        try {
            DexFile df = new DexFile(context.getPackageCodePath());
            Enumeration<String> entries = df.entries();
            while (entries.hasMoreElements()) {
                String className = (String) entries.nextElement();
                if (className.contains(packageName)) {
                    classes.add(Class.forName(className));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return classes;

    }


    /**
     * 以文件的形式来获取包下的所有Class
     *
     * @param packageName
     * @param packagePath
     * @param recursive
     * @param classes
     */
    private static void findAndAddClassesInPackageByFile(String packageName, String packagePath, final boolean recursive, List<Class<?>> classes) {
        // 获取此包的目录 建立一个File
        File dir = new File(packagePath);
        // 如果不存在或者 也不是目录就直接返回
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }
        // 如果存在 就获取包下的所有文件 包括目录
        File[] dirfiles = dir.listFiles(new FileFilter() {
            // 自定义过滤规则 如果可以循环(包含子目录) 或则是以.class结尾的文件(编译好的java类文件)
            public boolean accept(File file) {
                return (recursive && file.isDirectory()) || (file.getName().endsWith(".class"));
            }
        });
        // 循环所有文件
        for (File file : dirfiles) {
            // 如果是目录 则继续扫描
            if (file.isDirectory()) {
                findAndAddClassesInPackageByFile(packageName + "." + file.getName(), file.getAbsolutePath(), recursive, classes);
            } else {
                // 如果是java类文件 去掉后面的.class 只留下类名
                String className = file.getName().substring(0, file.getName().length() - 6);
                try {
                    // 添加到集合中去
                    classes.add(Class.forName(packageName + '.' + className));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
