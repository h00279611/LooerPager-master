package com.sunofbeaches.looerpager.model;

import com.sunofbeaches.looerpager.annotation.TableAnnotation;
import com.sunofbeaches.looerpager.annotation.TableAttrAnnotation;

@TableAnnotation(tableName = "tal_company")
public class Company extends DBModel{

    @TableAttrAnnotation(name = "_id", isInsert = false, isKey = true)
    private int id;

    @TableAttrAnnotation(name = "name")
    private String name;

    public Company(){}

    public Company(String name) {
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
