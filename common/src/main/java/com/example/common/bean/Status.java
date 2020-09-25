package com.example.common.bean;

import java.util.List;

public class Status<T> {
    public List<T> datas;
    public boolean status;

    public Status(List<T> datas, boolean status) {
        this.datas = datas;
        this.status = status;
    }
}