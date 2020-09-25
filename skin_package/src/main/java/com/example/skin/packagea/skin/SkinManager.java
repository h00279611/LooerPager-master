package com.example.skin.packagea.skin;

import android.app.Application;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class SkinManager {

    private static SkinManager instance;
    private Application mApplication;
    private Resources appResources; // APP内置资源
    private Resources skinResources; // 皮肤包资源
    private String skinPackageName; // 皮肤包所在包名
    private boolean isDefaultSkin = true;
    private static final String ADD_ASSET_PATH = "addAssetPath";
    private Map<String, SkinCache> mSkinCacheMap;


    private SkinManager(Application application) {
        this.mApplication = application;
        this.appResources = application.getResources();
        this.mSkinCacheMap = new HashMap<>();
    }

    public static void init(Application application) {
        if (instance == null) {
            synchronized (SkinManager.class) {
                if (instance == null) {
                    instance = new SkinManager(application);
                }
            }
        }
    }


    public static SkinManager getInstance() {
        return instance;
    }


    /**
     * 加载皮肤包资源
     *
     * @param skinPath 皮肤包路径 ，为空则加载app内置资源
     */
    public void loaderSkinResources(String skinPath) {
        if (TextUtils.isEmpty(skinPath)) {
            isDefaultSkin = true;
            return;
        }

        // 性能优化
        SkinCache skinCache = mSkinCacheMap.get(skinPath);
        if (skinCache != null) {
            skinResources = skinCache.getSkinResource();
            skinPackageName = skinCache.getSkinPackageName();
            return;
        }


        try {
            // 创建资源管理器
            AssetManager assetManager = AssetManager.class.newInstance();
            // 被@hide，目前只能通过反射去调用
            Method method = assetManager.getClass().getDeclaredMethod(ADD_ASSET_PATH, String.class);
            method.setAccessible(true);
            method.invoke(assetManager, skinPath);

            // 创建加载外部皮肤包资源文件的Resource
            skinResources = new Resources(assetManager, appResources.getDisplayMetrics(), appResources.getConfiguration());

            // 根据皮肤包文件，获取皮肤包应用的包名
            skinPackageName = mApplication.getPackageManager().getPackageArchiveInfo(skinPath, PackageManager.GET_ACTIVITIES)
                    .packageName;

            // 若无法获取皮肤包应用的包名，加载App内置资源
            isDefaultSkin = TextUtils.isEmpty(skinPackageName);

            // 缓存
            if (!isDefaultSkin) {
                mSkinCacheMap.put(skinPath, new SkinCache(skinResources, skinPackageName));
            }

        } catch (Exception e) {
            e.printStackTrace();
            // 预羊：通过API可能发生异常
            isDefaultSkin = true;
        }
    }


    /**
     * 参考：resource.arsc资源映射表
     * 通过ID值获取资源的Name和Type
     *
     * @param resourceId 资源的ID(app内置资源)
     * @return 如果没有皮肤包则加载App内置资源ID，反之则加载皮肤包的资源ID
     */
    public int getSkinResourceIds(int resourceId) {
        // 优化
        if (isDefaultSkin) {
            return resourceId;
        }

        String resourceEntryName = appResources.getResourceEntryName(resourceId); // music_bg
        String resourceType = appResources.getResourceTypeName(resourceId); // drawable

        // 动态获取皮肤包内的指定资源ID
        int skinResourceId = skinResources.getIdentifier(resourceEntryName, resourceType, skinPackageName);
        isDefaultSkin = skinResourceId == 0;
        return skinResourceId == 0 ? resourceId : skinResourceId;
    }


    public int getColor(int resourceId) {
        int id = getSkinResourceIds(resourceId);
        return isDefaultSkin ? appResources.getColor(id) : skinResources.getColor(id);
    }


    public ColorStateList getColorStateList(int resourceId) {
        int id = getSkinResourceIds(resourceId);
        return isDefaultSkin ? appResources.getColorStateList(id) : skinResources.getColorStateList(id);
    }


    public Drawable getDrawableOrMipMap(int resourceId) {
        int id = getSkinResourceIds(resourceId);
        return isDefaultSkin ? appResources.getDrawable(id) : skinResources.getDrawable(id);
    }


    public String getString(int resourceId) {
        int id = getSkinResourceIds(resourceId);
        return isDefaultSkin ? appResources.getString(id) : skinResources.getString(id);
    }

    public Object getBackgroudOrSrc(int resourceId) {
        String resourceType = appResources.getResourceTypeName(resourceId);

        switch (resourceType) {
            case "color":
                return getColor(resourceId);

            case "mipmap": // drawable, mipmap
            case "drawable":
                return getDrawableOrMipMap(resourceId);
        }

        return null;
    }


    public boolean isDefaultSkin() {
        return isDefaultSkin;
    }

    public Typeface getTypeface(int resourceId) {
        //  通过资源ID获取资源path
        return null;
    }

}
