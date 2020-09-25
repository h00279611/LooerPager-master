package com.sunofbeaches.looerpager.model;

public class PagerItem {
    private String title;

    private Integer picResId;

    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getPicResId() {
        return picResId;
    }

    public void setPicResId(Integer picResId) {
        this.picResId = picResId;
    }

    public PagerItem(String title,Integer picResId) {
        this.title = title;
        this.picResId = picResId;
    }


    public PagerItem(String title,String url) {
        this.title = title;
        this.url = url;
    }
}
