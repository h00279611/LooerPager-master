package com.example.common.bean;

import java.util.List;

public class Progress<T> {

    public List<T> list;

    public int totalCount;

    public int completedCount;

    public Progress( List<T> list, int totalCount, int completedCount) {
        this.list = list;
        this.totalCount = totalCount;
        this.completedCount = completedCount;
    }
}
