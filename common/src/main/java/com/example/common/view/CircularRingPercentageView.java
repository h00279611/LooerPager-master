package com.example.common.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.common.R;
import com.example.common.util.ViewUtils;

/**
 * 微信跑步圆形进度
 */
public class CircularRingPercentageView extends View {

    // 绘制圆形的画笔
    private Paint paint;

    // 绘制刻度的画笔
    private Paint mPaintText;

    // 圆形进度条的直径
    private int circleWith;

    // 渐变背景色
    private int roundBackgroundColor;

    //刻度字体的颜色
    private int textColor;

    // 刻度文字的大小
    private float textSize;

    // 进度条的宽度
    private float roundWith;

    // 进度
    private float progress = 0;

    // 颜色数组
    private int[] colors = {0xffff4639, 0xffCDD513, 0xff3CDF5F};

    // 圆环的半径
    private int radius;

    // 进度条与中心的位置
    private RectF oval;

    // 进度条分割数量
    private int maxColorNumber = 100;

    // 两个间隔快之间的距离
    private float singlPoint;

    // 间隔块的宽度
    private float lineWith;

    // 圆环的半径
    private int circleCenter;

    // 渐变色的位置
    private SweepGradient mSweepGradient;

    // 是否绘制间隔块标记
    private boolean isLine;

    // 设置默认目标步数
    private float tartGet = 6000;


    public CircularRingPercentageView(Context context) {
        this(context, null);
    }

    public CircularRingPercentageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircularRingPercentageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // 设置自定义属性样式
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircularRing);

        // 分割数量
        maxColorNumber = typedArray.getInt(R.styleable.CircularRing_circleNumber, 100);

        //圆环进度条的直径
        circleWith = typedArray.getDimensionPixelOffset(R.styleable.CircularRing_circleWidth, ViewUtils.getDpValue(context, 280));

        // 渐变色的背景
        roundBackgroundColor = typedArray.getColor(R.styleable.CircularRing_roundColor, 0xffddddd);

        // 刻度字体的颜色
        textColor = typedArray.getColor(R.styleable.CircularRing_circleTextColor, 0xff9999);

        // 进度条的宽度
        roundWith = typedArray.getDimension(R.styleable.CircularRing_circleRoundWidth, 40);

        // 刻度文字的大小
        textSize = typedArray.getDimension(R.styleable.CircularRing_circleTextSize, ViewUtils.getDpValue(context, 8));

        // 渐变色数组
        // 渐变色数组中的第一个颜色
        colors[0] = typedArray.getColor(R.styleable.CircularRing_circleColor1, 0xfffff34);

        // 渐变色数组中的第二个颜色
        colors[1] = typedArray.getColor(R.styleable.CircularRing_circleColor2, 0xffeee34);

        // 渐变色数组中的第三个颜色
        colors[2] = typedArray.getColor(R.styleable.CircularRing_circleColor3, 0xfaaa10);

        // 资源回收
        typedArray.recycle();
    }


    // 圆环进度条空白处的原色背景
    public void setRoundBackgroundColor(int roundBackgroundColor) {
        this.roundBackgroundColor = roundBackgroundColor;
        paint.setColor(roundBackgroundColor);
        // 刷新重新绘制
        invalidate();
    }

    // 设置刻度字体的颜色
    public void setTextColor(int textColor) {
        this.textColor = textColor;
        mPaintText.setColor(textColor);
        invalidate();
    }

    // 设置刻度字体的大小
    public void setTextSize(float textSize) {
        this.textSize = textSize;
        mPaintText.setTextSize(textSize);
        invalidate();
    }

    // 设置渐变色
    public void setColors(int[] colors) {
        if (colors.length < 2) {
            throw new IllegalArgumentException("colors lenth < 2");
        }
        this.colors = colors;

        sweepGradientInit(); // 调用渐变色初始化
        invalidate();
    }

    // 渐变色初如化
    public void sweepGradientInit() {
        // 渐变颜色位置
        mSweepGradient = new SweepGradient(this.circleWith / 2, this.circleWith / 2, colors, null);
        // 旋转渐变
        Matrix matrix = new Matrix();
        // 圆环的宽度和高度一样都是circleWith/2
        matrix.setRotate(-90, circleWith / 2, circleWith / 2);
        // 设置矩阵
        mSweepGradient.setLocalMatrix(matrix);
    }

    // 设置间隔块宽度
    public void setLineWith(float lineWith) {
        this.lineWith = lineWith;
        invalidate();
    }

    // 是否绘制间隔块
    public void setLine(boolean line) {
        isLine = line;
        invalidate();
    }

    // 获取圆形进度条直径
    public int getCircleWith() {
        return circleWith;
    }

    // 设置圆环宽度
    public void setRoundWith(float roundWith) {
        this.roundWith = roundWith;
        if (roundWith > circleCenter) {
            this.roundWith = circleCenter;
        }

        // 计算圆环半径值
        radius = (int) (circleCenter - this.roundWith / 2);
        // 计算圆环左侧、右侧、上侧、下侧的位置
        oval.left = circleCenter - radius;
        oval.right = circleCenter + radius;
        oval.top = circleCenter - radius;
        oval.bottom = circleCenter + radius;

        paint.setStrokeWidth(this.roundWith);
        invalidate();
    }


    // 设置圆环的直径
    public void setCircleWith(int circleWith) {
        this.circleWith = circleWith;
        circleCenter = circleWith / 2;
        if (roundWith > circleCenter) {
            roundWith = circleCenter;
        }
        setRoundWith(roundWith);

        // 渐变颜色位置
        mSweepGradient = new SweepGradient(this.circleWith / 2, this.circleWith / 2, colors, null);
        // 旋转渐变
        Matrix matrix = new Matrix();
        // 圆环的宽度和高度一样都是circleWith/2
        matrix.setRotate(-90, circleWith / 2, circleWith / 2);
        // 设置矩阵
        mSweepGradient.setLocalMatrix(matrix);
    }

    // 设置目标步数
    public void setTartGet(float tartGet) {
        this.tartGet = tartGet;
        invalidate();
    }

    // 返回默认与修改后的目标步数

    public float getTartGet() {
        return tartGet;
    }

    //初始化控件
    public void initView(){
        circleCenter = circleWith / 2;                      // 圆环中心点
        singlPoint = 360 / maxColorNumber;                  // 两个间隔块之间的距离
        radius = (int) (circleCenter - roundWith /2);       // 圆环的半径
        sweepGradientInit();                                // 渐变色初始化

        mPaintText = new Paint();
        mPaintText.setColor(textColor);                     // 设置画笔颜色
        mPaintText.setTextAlign(Paint.Align.CENTER);        // 排列位置
        mPaintText.setTextSize(textSize);                   // 文字大小
        mPaintText.setAntiAlias(true);                      // 开启抗拒齿


        paint = new Paint();
        paint.setColor(roundBackgroundColor);
        paint.setStyle(Paint.Style.STROKE);  // 画笔样式
        paint.setStrokeWidth(roundWith);     // 画笔宽度
        paint.setAntiAlias(true);            // 开启抗拒齿

    }
}
