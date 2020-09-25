package com.example.common.db;

import com.example.common.bean.DBModel;

import java.util.List;

public interface IBase<T extends DBModel> {

    List<T> query();


    boolean batchInsert(List<T> list);


    boolean insert(T t);


    boolean deleteAll();

}
