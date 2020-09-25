package com.example.quickfind.model;

import com.example.quickfind.util.ChineseCharToEn;

public class Person {
    private String name;
    private String pingYin;

    public Person(String name) {
        this.name = name;
        this.pingYin = ChineseCharToEn.getFirstLetter(name);
    }

    public String getName() {
        return name;
    }

    public String getPingYin() {
        return pingYin;
    }
}
