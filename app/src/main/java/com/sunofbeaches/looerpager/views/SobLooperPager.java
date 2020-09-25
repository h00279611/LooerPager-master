package com.sunofbeaches.looerpager.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.sunofbeaches.looerpager.R;
import com.sunofbeaches.looerpager.adapter.PictureAdapter;
import com.sunofbeaches.looerpager.model.PagerItem;
import com.sunofbeaches.looerpager.utils.SizeUtils;

import java.util.List;

public class SobLooperPager extends LinearLayout {

    private static final String TAG = "SobLooperPager";
    private SobViewPager mViewPager;
    private LinearLayout mPointContainer;
    private TextView mTitleTv;
    private BindTitleListener mTitleSetListener = null;
    private PictureAdapter mPictureAdapter = null;
    private OnItemClickListener mOnItemClickListener = null;
    private boolean mIsTitleShow;
    private int mPagerShowCount;
    private int mSwitchTime;
    private List<PagerItem> pagerItemList;


    public SobLooperPager(Context context) {
        this(context, null);
    }

    public SobLooperPager(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SobLooperPager(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.looper_pager_layout, this, true);
        //等价于以下两行代码
        //View item = LayoutInflater.from(context).inflate(R.layout.looper_pager_layout,this,false);
        //addView(item);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.looper_style);
        //从TypedArray中读取属性值
        mIsTitleShow = ta.getBoolean(R.styleable.looper_style_is_title_show, true);
        mPagerShowCount = ta.getInteger(R.styleable.looper_style_show_pager_count, 1);
        mSwitchTime = ta.getInteger(R.styleable.looper_style_switch_time, -1);
//        Log.d(TAG,"mIsTitleShow -- > " + mIsTitleShow);
//        Log.d(TAG,"mPagerShowCount -- > " + mPagerShowCount);
//        Log.d(TAG,"mSwitchTime -- > " + mSwitchTime);
        ta.recycle();
        init();

        mViewPager.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return false;
            }
        });
    }

    private void init() {
        initView();
        initEvent();
    }

    private void initEvent() {
        mViewPager.setPagerItemClickListener(new SobViewPager.OnPagerItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (mOnItemClickListener != null && mPictureAdapter != null) {
                    int realPosition = position % mPictureAdapter.getDataSize();
                    mOnItemClickListener.onItemClick(realPosition);
                }
            }
        });
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //切换的一个回调方法
            }

            @Override
            public void onPageSelected(int position) {
                //切换停下来的回调
                if (mPictureAdapter != null) {
                    //停下来以后，设置标题
                    int realPosition = position % mPictureAdapter.getDataSize();
                    //
                    if (mTitleSetListener != null) {
                        mTitleTv.setText(mTitleSetListener.getTitle(realPosition));
                    }
                    // 切换指示器焦点
                    updateIndicator();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //切换状态改变的回调

            }
        });
    }


    public interface BindTitleListener {
        String getTitle(int position);
    }


    public void setData(List<PagerItem> pagerItemList, BindTitleListener listener) {
        this.pagerItemList = pagerItemList;
        this.mTitleSetListener = listener;
        this.mPictureAdapter = new PictureAdapter(pagerItemList);
        mViewPager.setAdapter(mPictureAdapter);
        mViewPager.setCurrentItem(Integer.MAX_VALUE / 2 + 1);
        if (listener != null) {
            mTitleTv.setText(listener.getTitle(mViewPager.getCurrentItem() % pagerItemList.size()));
        }
        //可以得到数据的个数，根据数据个数，来动态创建圆点，indicator
        updateIndicator();
    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }


    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    private void updateIndicator() {
        if (mPictureAdapter != null && mTitleSetListener != null) {
            int count = pagerItemList.size();
            mPointContainer.removeAllViews();
            for (int i = 0; i < count; i++) {
                View point = new View(getContext());
                if (mViewPager.getCurrentItem() % count == i) {
                    //   point.setBackgroundColor(Color.parseColor("#ff0000"));
                    point.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_circle_read));
                } else {
                    point.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_circle_white));
//                    point.setBackgroundColor(Color.parseColor("#ffffff"));
                }
                //设置大小
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(SizeUtils.dip2px(getContext(), 8), SizeUtils.dip2px(getContext(), 8));
                layoutParams.setMargins(SizeUtils.dip2px(getContext(), 5), 0, SizeUtils.dip2px(getContext(), 5), 0);
                point.setLayoutParams(layoutParams);
                //添加到容器里去
                mPointContainer.addView(point);
            }
        }
    }

    private void initView() {
        mViewPager = this.findViewById(R.id.looper_pager_vp);
        if (mSwitchTime != -1) {
            mViewPager.setDelayTime(mSwitchTime);
        }
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setPageMargin(SizeUtils.dip2px(getContext(), 10));
        mPointContainer = this.findViewById(R.id.looper_point_container_lv);
        mTitleTv = this.findViewById(R.id.looper_title_tv);
        if (!mIsTitleShow) {
            mTitleTv.setVisibility(GONE);
        }
    }
}
