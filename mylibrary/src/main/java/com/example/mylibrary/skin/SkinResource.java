package com.example.mylibrary.skin;

import android.app.Application;

public class SkinResource {

    private static SkinResource mSkinResource = null;

    private SkinResource() {

    }

    public static synchronized SkinResource getInstance() {
        if (mSkinResource == null) {
            mSkinResource = new SkinResource();
        }

        return mSkinResource;
    }

    public void setSkinResources(Application application, String skinPath) {


    }
}
