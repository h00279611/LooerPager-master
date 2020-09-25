package com.example.skin.packagea.skin;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import com.example.skin.packagea.R;


public class SkinnableButtonView extends AppCompatButton implements ViewsMatch {

    private AttrsBean mAttrsBean;

    public SkinnableButtonView(Context context) {
        this(context, null);
    }

    public SkinnableButtonView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SkinnableButtonView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mAttrsBean = new AttrsBean();

        TypedArray typedArray = context.obtainStyledAttributes(
                attrs, R.styleable.SkinnableButton, defStyleAttr, 0);


        // 存储到临时对象
        mAttrsBean.saveViewResource(typedArray, R.styleable.SkinnableButton);

        // 非常重要
        typedArray.recycle();

    }

    @Override
    public void skinnableView() {
        int key = R.styleable.SkinnableButton[R.styleable.SkinnableButton_android_background];
        int backgroudResourceId = mAttrsBean.getViewResource(key);
        if (backgroudResourceId > 0) {
            if (SkinManager.getInstance().isDefaultSkin()) {
                Drawable drawable = ContextCompat.getDrawable(getContext(), backgroudResourceId);
                setBackgroundDrawable(drawable);
            } else {
                Object skinResourceId = SkinManager.getInstance().getBackgroudOrSrc(backgroudResourceId);
                if (skinResourceId instanceof Integer) {
                    int color = (int) skinResourceId;
                    setBackgroundColor(color);
                } else {
                    Drawable drawable = (Drawable) skinResourceId;
                    setBackgroundDrawable(drawable);
                }
            }
        }

        key = R.styleable.SkinnableButton[R.styleable.SkinnableButton_android_textColor];
        int textColorResId = mAttrsBean.getViewResource(key);
        if (textColorResId > 0) {
            if (SkinManager.getInstance().isDefaultSkin()) {
                ColorStateList textColor = ContextCompat.getColorStateList(getContext(), textColorResId);
                setTextColor(textColor);
            } else {
                ColorStateList textColor = SkinManager.getInstance().getColorStateList(textColorResId);
                setTextColor(textColor);
            }
        }

    }
}
