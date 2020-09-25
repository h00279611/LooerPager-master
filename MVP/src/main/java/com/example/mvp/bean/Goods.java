package com.example.mvp.bean;

import com.example.common.annotation.TableAnnotation;
import com.example.common.annotation.TableAttrAnnotation;
import com.example.common.bean.DBModel;


@TableAnnotation(tableName = "tal_company", uri = "/tal_company")
public class Goods extends DBModel{

    @TableAttrAnnotation(name = "_id", isInsert = false, isKey = true)
    private int id;

    @TableAttrAnnotation(name = "name")
    private String name;

    public Goods(){}

    public Goods(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
