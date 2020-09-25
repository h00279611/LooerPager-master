package com.example.skin.packagea.skin;

import android.content.res.Resources;

public class SkinCache {
    public String getSkinPackageName;
    Resources skinResources;
    String skinPackageName;

    private SkinCache() {
    }

    public SkinCache(Resources skinResources, String skinPackageName) {
        this.skinResources = skinResources;
        this.skinPackageName = skinPackageName;
    }

    public Resources getSkinResource() {
        return skinResources;
    }

    public String getSkinPackageName() {
        return skinPackageName;
    }
}
