package com.example.mylibrary.skin;

import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class WidgetViewList {

    List<WidgetView> mViewList = new ArrayList<>();


    public void skinChange(){
        for(WidgetView view: mViewList){
            view.changeSkinView();
        }
    }

    public void saveWidgetView(AttributeSet attrs, View resultView) {
        mViewList.add(new WidgetView(resultView, attrs));
    }
}
