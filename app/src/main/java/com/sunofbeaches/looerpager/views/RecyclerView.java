package com.sunofbeaches.looerpager.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;

import com.sunofbeaches.looerpager.R;

import java.util.ArrayList;
import java.util.List;

public class RecyclerView extends ViewGroup {
    private Adapter adapter;

    // 当前显示的View
    private List<View> viewList = new ArrayList<>();

    // 当前滑动的Y值
    private int currentY;

    // 行数
    private int rowCount;

    // viw的第一行 是占内容的第几行
    private int firstRow;

    // Y偏移量
    private int scrollY;

    // 初如化 第一屏最慢
    private boolean needRelayout;

    private int width;

    private int height;

    private int[] heights; // item高度

    private Recycler mRecycler;

    // 最小滑动距离
    private int touchSlop;


    public RecyclerView(Context context) {
        this(context, null);
    }

    public RecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        // 获取系统配置，每个手机不一样
        ViewConfiguration configuration = ViewConfiguration.get(context);
        this.touchSlop = configuration.getScaledTouchSlop();
        this.needRelayout = true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        final int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        // 需要计算出数据的总高度和heightSize相比较，比较小值作为

        if (adapter != null) {
            this.rowCount = adapter.getCount();
            this.heights = new int[rowCount];
            for (int i = 0; i < heights.length; i++) {
                heights[i] = adapter.getHeight(i);
            }

            //数据的高度
            int h = sumArray(heights, 0, heights.length);
            h = Math.min(heightSize, h);

            setMeasuredDimension(widthSize, h);
            super.onMeasure(widthMeasureSpec, h);
        }

    }


    // 从firstIndex ~  firstIndex + count 之间的数据和
    private int sumArray(int array[], int firstIndex, int count) {
        int sum = 0;
        count += firstIndex;

        for (int i = firstIndex; i < count; i++) {
            sum += array[i];
        }

        return sum;
    }


    // 重新layout要重新进行初始化
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (needRelayout || changed) {
            needRelayout = false;
            viewList.clear();
            removeAllViews();
            if (adapter != null) {

                width = r -l;
                height = b - t;
                int left = 0, top =0, right, bottom;
                // top < height判断当前摆放的Viw是否在屏内，如果超出则先不进行摆放
                for(int i=0; i < rowCount && top < height; i++){
                    right = width;
                    bottom = top + heights[i];
                    // 生成一个View
                    View view = makeAndStep(i, left, top, right, bottom);

                    // 循环摆放
                    top = bottom;
                }


            }

        }
    }

    private View makeAndStep(int row, int left, int top, int right, int bottom) {
        View view = obtailView(row, right - left, bottom - top);
        view.layout(left, top, right, bottom);
        return view;
    }

    private View obtailView(int row, int width, int height) {
        // 先从回收池中找，池中没有则从adapter中查找
        int itemViewType = adapter.getItemViewType(row);
        View reclyView = mRecycler.get(itemViewType);
        View view;
        if(reclyView == null){
            view = adapter.onCreateViewHodler(row, reclyView, this);
            if(view == null){
                throw new RuntimeException("onCreateViewHodler必须填充布局");
            }
        }else{
            // reclyView不为空代表需要重绘
            view = adapter.onBinderViewHodler(row, reclyView, this);
        }

        view.setTag(R.id.tag_type_view, itemViewType);
        view.measure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
        addView(view, 0);
        return view;
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        boolean intercept = false;
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                currentY = (int) event.getRawY();
                break;
           case MotionEvent.ACTION_MOVE:
               int y2 = Math.abs(currentY - (int) event.getRawY());
               // 当滑动距离大于最小滑动距离才认为是一个滑动事件
               if(y2 > touchSlop){
                   intercept = true;
               }
        }

        return intercept;
    }

    /**
     * 只会消费滑动事件
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_MOVE:{
                // Y方向当前所在的坐标值
                int y2= (int) event.getRawY();

                // <0:表示从上往下滑动； >0:表示从下往上滑动
                int diffY = currentY - y2;

                // 只对当前画布影响，并不影响子控件
                scrollBy(diffY, 0);
            }
        }

        return true;
    }

    @Override
    public void scrollBy(int x, int y) {
        // scrollY: 第一个可见Item的左上顶点到屏幕的左上顶点的距离
        // 上滑正 下滑负
        scrollY += y;
        if (scrollY > 0) {
            // 大于第1个可见元素，则移除
            while (scrollY > heights[firstRow]) {
                removeView(viewList.remove(0));
                scrollY -= heights[firstRow];
                firstRow++;
            }

            // 上滑下部分添加子view进来
            while (getFillHeight() < height){
                int addLast = firstRow + viewList.size();
                View view = obtailView(addLast, width, heights[addLast]);
                viewList.add(viewList.size(), view);
            }


        }else if(scrollY <0){


        }else{


        }
    }

    private int getFillHeight() {
        // 数据的高度 - scrollY
        return sumArray(heights, firstRow, viewList.size()) - scrollY;
    }

    @Override
    public void removeView(View view) {
        super.removeView(view);
        int itemViewType = (int) view.getTag(R.id.tag_type_view);
        // 添加到回收池中
        mRecycler.put(view, itemViewType);
    }

    public RecyclerView.Adapter getAdapter() {
        return adapter;
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        this.adapter = adapter;
        if(adapter!=null){
            mRecycler = new Recycler(adapter.getViewTypeCount());
            scrollY =0;
            firstRow =0;
            needRelayout = true;
            requestLayout();// 调用此方法会独发OnMeasure和onLayout方法
        }
    }

    public interface Adapter {
        View onCreateViewHodler(int postion, View convertView, ViewGroup parent);

        View onBinderViewHodler(int postion, View convertView, ViewGroup parent);

        // Item的类型
        int getItemViewType(int row);

        // Item的类型数量
        int getViewTypeCount();

        int getCount();

        int getHeight(int index);
    }


}
