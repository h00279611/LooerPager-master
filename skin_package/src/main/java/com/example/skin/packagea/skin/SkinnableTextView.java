package com.example.skin.packagea.skin;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.example.skin.packagea.R;


public class SkinnableTextView extends AppCompatTextView implements ViewsMatch {

    private AttrsBean mAttrsBean;

    public SkinnableTextView(Context context) {
        this(context, null);
    }

    public SkinnableTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SkinnableTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mAttrsBean = new AttrsBean();

        TypedArray typedArray = context.obtainStyledAttributes(
                attrs, R.styleable.SkinnableTextView, defStyleAttr, 0);


        // 存储到临时对象
        mAttrsBean.saveViewResource(typedArray, R.styleable.SkinnableTextView);

        // 非常重要
        typedArray.recycle();

    }

    @Override
    public void skinnableView() {
        int key = R.styleable.SkinnableTextView[R.styleable.SkinnableTextView_android_background];
        int backgroudResourceId = mAttrsBean.getViewResource(key);
        if(backgroudResourceId > 0){
            Drawable drawable = ContextCompat.getDrawable(getContext(), backgroudResourceId);
            setBackgroundDrawable(drawable);
        }


        key = R.styleable.SkinnableTextView[R.styleable.SkinnableTextView_android_textColor];
        int textColorResId = mAttrsBean.getViewResource(key);
        if(textColorResId > 0){
            ColorStateList textColor = ContextCompat.getColorStateList(getContext(), textColorResId);
            setTextColor(textColor);
        }

    }
}
