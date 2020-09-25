package com.example.skin.packagea.skin;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.appcompat.app.AppCompatViewInflater;

public class CustomAppcompatViewInflater extends AppCompatViewInflater {

    Context mContext;
    String name;
    AttributeSet attrs;

    public CustomAppcompatViewInflater(Context context) {
        mContext = context;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAttrs(AttributeSet attrs) {
        this.attrs = attrs;
    }


    public View autoMatch(){
        switch (name){
            case "Button":
                return new SkinnableButtonView(mContext, attrs);

            case "TextView":
                return new SkinnableTextView(mContext, attrs);
        }
        return null;
    }

}
