package com.example.common.presenter;


import com.example.common.model.BaseModel;
import com.example.common.view.IBaseView;

import java.lang.ref.WeakReference;

public class BasePresenter<T extends IBaseView, V extends BaseModel> {

    protected WeakReference<T> mTWeakReference;


    public BasePresenter(T view){
        mTWeakReference = new WeakReference<>(view);
    }


}
