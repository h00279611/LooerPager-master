package com.sunofbeaches.looerpager.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.sunofbeaches.looerpager.R;

/**
 * 一辆小车在圆上不停的旋转
 *
 */
public class CarView extends View {

    private Bitmap mCarBitmap;
    private Path mPath;
    private PathMeasure mPathMeasure;
    private Paint mCirclePaint;
    private Paint mCarPaint;
    private Matrix mCarMatrix;
    // 当前的进度
    private float distanceRatio;

    public CarView(Context context) {
        this(context, null);
    }

    public CarView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init(){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.outWidth = 20;
        options.outHeight =20;


        // 小车
        mCarBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.car, options);
        mPath = new Path();
        mPath.addCircle(0, 0, 200, Path.Direction.CW);

        mPathMeasure = new PathMeasure(mPath, false);

        mCirclePaint = new Paint();
        //设置画笔粗细
        mCirclePaint.setStrokeWidth(5);
        //画笔属性是空心圆
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setColor(Color.BLACK);

        mCarPaint = new Paint();
        mCarPaint.setColor(Color.DKGRAY);
        mCarPaint.setStrokeWidth(2);
        mCarPaint.setStyle(Paint.Style.STROKE);

        mCarMatrix = new Matrix();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);




        canvas.drawColor(Color.WHITE);
        int width = canvas.getWidth();
        int height = canvas.getHeight();

        // 移动canvas坐标到中心
        canvas.translate(width/2, height/2);
        mCarMatrix.reset();

        // 绘制圆
        canvas.drawPath(mPath, mCirclePaint);


        // 当前的进度
        distanceRatio += 0.006f;
        if(distanceRatio >=1){
            distanceRatio =0;
        }

        // 记录位置
        float[] pos = new float[2];
        // 记录切点值xy
        float[] tan = new float[2];
        float distance =  mPathMeasure.getLength() * distanceRatio;
        // 用于获取某个点的角度
        mPathMeasure.getPosTan(distance, pos, tan);

        //  tan0代表cos  tan[1]代表sin
        double atan = Math.atan2(tan[1], tan[0]);

        // 计算小车本身要旋转的角度
        float degree = (float) ((float) atan * 180 / Math.PI);

        // 设置旋转角度和旋转中心
        mCarMatrix.postRotate(degree, mCarBitmap.getWidth()/ 2, mCarBitmap.getHeight()/ 2);

        // 这里要将设置到小车的中心点
        mCarMatrix.postTranslate(pos[0] - mCarBitmap.getWidth()/2, pos[1] - mCarBitmap.getHeight()/2);

        // 画当前小车的位置
        canvas.drawBitmap(mCarBitmap, mCarMatrix, mCarPaint);

        // 不断的重绘
        invalidate();
    }
}
