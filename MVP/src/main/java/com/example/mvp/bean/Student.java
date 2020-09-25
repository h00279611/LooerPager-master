package com.example.mvp.bean;

import com.example.common.annotation.TableAnnotation;
import com.example.common.annotation.TableAttrAnnotation;
import com.example.common.bean.DBModel;

@TableAnnotation(tableName = "tal_student", uri = "/tal_company")
public class Student extends DBModel {

    @TableAttrAnnotation(name = "_id", isInsert = false, isKey = true)
    private int id;

    @TableAttrAnnotation(name = "name")
    private String name;

    @TableAttrAnnotation(name = "age")
    private int age;

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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
