package com.sunofbeaches.looerpager.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.sunofbeaches.looerpager.R;

public class GridItem extends RelativeLayout implements Checkable {

    private Context mContext;
    private boolean mChecked;
    private ImageView mImgView = null;
    private ImageView mSelcetView = null;

    public GridItem(Context context) {
        this(context, null, 0);
    }

    public GridItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GridItem(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        LayoutInflater.from(mContext).inflate(R.layout.grid_item, this);
        mImgView = (ImageView) findViewById(R.id.img_view);
        mSelcetView = (ImageView) findViewById(R.id.select);
    }

    @Override
    public void setChecked(boolean checked) {
        mChecked = checked;
        setBackgroundDrawable(checked ? getResources().getDrawable(
                R.drawable.ic_launcher_background) : null);
        mSelcetView.setVisibility(checked ? View.VISIBLE : View.GONE);
        if(!checked){
            mSelcetView.setImageResource(R.drawable.icon_choice);
        }else{
            mSelcetView.setImageResource(R.drawable.icon_choice_ok);
        }
    }

    @Override
    public boolean isChecked() {
        return mChecked;
    }

    @Override
    public void toggle() {
        setChecked(!mChecked);
    }

    public void setImgResId(int resId) {
        if (mImgView != null) {
            mImgView.setBackgroundResource(resId);
        }
    }
}