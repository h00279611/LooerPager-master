package com.example.common.model;

import com.example.common.listener.OnLoadListener;

import java.util.List;

public interface IBaseModel<T> {

    void query(OnLoadListener onLoadListener);

    void batchInsert(List<T> goods, OnLoadListener onLoadListener);

    void deleteAll(OnLoadListener onLoadListener);

    void insert(List<T> list, OnLoadListener onLoadListener);

}
