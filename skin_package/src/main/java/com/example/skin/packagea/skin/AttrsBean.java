package com.example.skin.packagea.skin;

import android.content.res.TypedArray;
import android.util.SparseIntArray;

public class AttrsBean {

    private SparseIntArray resourcesMap;
    private static final int DEAFAULT_VALUE = -1;

    public AttrsBean() {
        resourcesMap = new SparseIntArray();
    }


    /**
     * @param typedArray 控件属性的类型集合，如：backgroud/ textColor
     * @param styleable 自定义属性，参考value/attrs.xml
     */
    public void saveViewResource(TypedArray typedArray, int[] styleable) {
        for (int i = 0; i < typedArray.length(); i++) {
            int key = styleable[i];
            int resourceId = typedArray.getResourceId(i, DEAFAULT_VALUE);
            resourcesMap.put(key, resourceId);
        }
    }


    /**
     * 获取控件某属性的resourceId
     *
     * @param styleable
     * @return
     */
    public int getViewResource(int styleable){
        return resourcesMap.get(styleable);
    }


}
