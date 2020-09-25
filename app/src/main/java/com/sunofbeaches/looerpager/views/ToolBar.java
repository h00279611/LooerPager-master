package com.sunofbeaches.looerpager.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sunofbeaches.looerpager.R;

public class ToolBar extends RelativeLayout implements View.OnClickListener {


    private String mTitle;
    private int mBackColor;
    private View mBarLeft;
    private TextView mBarTitle;
    private View mBarRight;
    private BarCLickListener barClickListener;

    public ToolBar(Context context) {
        this(context,null);
    }

    public ToolBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ToolBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    @SuppressLint("ResourceAsColor")
    private void initView(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ToolBar);
        mTitle = a.getString(R.styleable.ToolBar_title);
        mBackColor = a.getColor(R.styleable.ToolBar_backgroundColor, R.color.colorPrimary);
        // 回收资源
        a.recycle();

        View view = LayoutInflater.from(context).inflate(R.layout.activit_tool_bar, this, true);
        view.setBackgroundColor(mBackColor);

        mBarLeft = view.findViewById(R.id.iv_titlebar_left);
        mBarTitle = view.findViewById(R.id.iv_title_title);
        mBarRight = view.findViewById(R.id.iv_titlebar_right);

        mBarTitle.setText(mTitle);
        mBarLeft.setOnClickListener(this);
        mBarRight.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (this.barClickListener == null) {
            return;
        }

        int id = v.getId();
        if (id == R.id.iv_titlebar_left) {
            this.barClickListener.leftClick(v);
        } else if (id == R.id.iv_titlebar_right) {
            this.barClickListener.rightClick(v);
        }

    }


    public void setBarClickListener(BarCLickListener barClickListener) {
        this.barClickListener = barClickListener;
    }


    public interface BarCLickListener {
        void leftClick(View view);

        void rightClick(View view);
    }

}
