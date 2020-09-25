package com.example.quickfind;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * 绘制快速索引的字母
 * 1.  写26个字母放入数组中
 * 2.  在onMeasure计算每条的高itemHeight和宽itemWidth和
 * 3.  在onDraw中计算每个字母的wordWidth, wordHeight, wordX, wordY
 * <p>
 * <p>
 * 手指按下文字变色
 * 1. 重写onTouchEvent(),返回true,在down/move的过程中计算int touchIndex = Y/itemHeight，强制绘制
 * 2. 在onDraw()方法对于对应的下标设置画笔变色
 * 3. 在up的时候:touchIndex = -1,强制绘制
 */
public class IndexView extends View {

    private int itemWidth;
    private int itemHeight;
    private Paint mPaint;

    // 手指按下位置对应的Item的索引
    private int touchIndex = -1;

    // 当手指滑动或按下时传递给调用方
    private DataChangeListener mDataChangeListener;

    private String[] words = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};


    public IndexView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        mPaint.setColor(Color.WHITE);
        mPaint.setTextSize(40);
        mPaint.setAntiAlias(true);// 设置抗矩齿
        mPaint.setTypeface(Typeface.DEFAULT_BOLD);// 设置粗体字
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        itemWidth = getMeasuredWidth();
        itemHeight = getMeasuredHeight() / words.length;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < words.length; i++) {
            String word = words[i]; // A
            if (touchIndex == i) {
                // 设置灰色
                mPaint.setColor(Color.GRAY);
            } else {
                // 设置白色
                mPaint.setColor(Color.WHITE);
            }

            // 在字母外框上一个矩阵，矩阵的宽就是字母的宽，矩阵的高就是字母的高
            Rect rect = new Rect();
            // 画笔
            // 0，1的取一个字母
            mPaint.getTextBounds(word, 0, 1, rect);

            // 字母的高和宽
            int wordWidth = rect.width();
            int wordHeight = rect.height();

            // 计算每个字母在视图中的坐标
            int wordX = itemWidth / 2 - wordWidth / 2;
            int wordY = itemHeight / 2 + wordHeight / 2 + i * itemHeight;

            canvas.drawText(word, wordX, wordY, mPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                float y = event.getY();
                int index = (int) (y / itemHeight); // 字母索引
                if (index != touchIndex) {
                    touchIndex = index;
                    if (mDataChangeListener != null && touchIndex >= 0 && touchIndex < words.length) {
                        mDataChangeListener.onChange(words[touchIndex]);
                    }
                    invalidate(); // 强制绘制会导致onDraw重新执行
                }
                break;

            case MotionEvent.ACTION_UP:
                break;
        }


        return true;
    }


    public void setDataChangeListener(DataChangeListener dataChangeListener) {
        mDataChangeListener = dataChangeListener;
    }

    public interface DataChangeListener {
        void onChange(String character);
    }
}
