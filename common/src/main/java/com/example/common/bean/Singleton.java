package com.example.common.bean;

public abstract class Singleton<T> {

    private T mSingleton;

    public final T get() {
        synchronized (this) {
            if (mSingleton == null) {
                mSingleton = create();
            }
        }
        return mSingleton;
    }

    protected abstract T create();


}
