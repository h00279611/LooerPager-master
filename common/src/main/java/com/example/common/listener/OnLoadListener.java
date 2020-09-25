package com.example.common.listener;

import com.example.common.bean.DBModel;

import java.util.List;

public interface OnLoadListener<T extends DBModel> {
    default void queryCompleted(List<T> list) {

    }

    default void queryError(int code, String message) {

    }

    default void addCompleted(List<T> list, boolean status) {

    }

    default void deleteCompleted(boolean status) {

    }

    default void updateCompleted(boolean status) {

    }

    default void addProgress(List<T> list, int totalCount, int completeCount) {
    }

}