package com.example.mvp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.annotation.BindView;
import com.example.common.view.PercentCircle;
import com.example.mvp.adapter.GoodsAdapter;
import com.example.mvp.bean.Goods;
import com.example.mvp.presenter.GoodsPresenter;
import com.example.mvp.view.IGoodsView;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements IGoodsView {

    private final String TAG = "MainActivity";

    @BindView(R.id.rv_main)
    RecyclerView mMRecyclerView;

    @BindView(R.id.progress_tv)
    TextView mProgressTv;

    @BindView(R.id.pCircleBar)
    PercentCircle mPercentCircle;

    private GoodsPresenter mGoodsPresenter;

    private GoodsAdapter mGoodsAdapter;


    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suer_main);

        // 从注解动态生成类中获取对象
        com.annotation.gener.BindViewInjector.inject(this);

        initView();

        mGoodsPresenter = new GoodsPresenter(this, this);
        mGoodsPresenter.fetch();
    }

    private void initView() {
//        mMRecyclerView = findViewById(R.id.rv_main);
//        mProgressTv = findViewById(R.id.progress_tv);
//        mPercentCircle = findViewById(R.id.pCircleBar);


        mGoodsAdapter = new GoodsAdapter();
        mMRecyclerView.setAdapter(mGoodsAdapter);
        mPercentCircle.setVisibility(View.GONE);

        // 设置布局
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mMRecyclerView.setLayoutManager(linearLayoutManager);
        mMRecyclerView.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(this).build());
    }

    @Override
    public void showGoodsView(List<Goods> companyList) {
        mGoodsAdapter.setDatas(companyList);
    }

    @Override
    public void addData(List<Goods> companyList, boolean status) {
        if (status) {
            mGoodsAdapter.addDatas(companyList);
        }
        showToast(status ? "添加成功" : "添加失败");
    }

    @Override
    public void delete(boolean status) {
        mGoodsAdapter.clearDatas();
        showToast(status ? "删除成功" : "删除失败");
    }

    @Override
    public void addProgress(List<Goods> list, int totalCount, int completeCount) {
        mProgressTv.setText("总共:" + totalCount + ", 已完成 :" + completeCount);
        if (completeCount < totalCount) {
            mPercentCircle.setVisibility(View.VISIBLE);
        } else {
            mPercentCircle.setVisibility(View.GONE);
            // 重新查询
            mGoodsPresenter.fetch();
        }

        int mCurrPercent = completeCount * 100 / totalCount;
        mPercentCircle.setProgressRate(120);
        mPercentCircle.setTargetPercent(100);
        mPercentCircle.setCurrentPercent(mCurrPercent);

    }

    @Override
    public void showToast(String info) {
        Toast.makeText(this, info, Toast.LENGTH_SHORT).show();
    }

    public void test(View view) {
        mGoodsPresenter.fetch();
    }


    public void batchAdd(View view) {
        int count = mGoodsAdapter.getItemCount();
        List<Goods> list = new ArrayList<>();
        for (int i = count; i < count + 10; i++) {
            list.add(new Goods("name:" + i));
        }
        mGoodsPresenter.batchAdd(list);
    }


    public void delete(View view) {
        mGoodsPresenter.deleteAll();
    }


    public void add(View view) {
        int count = mGoodsAdapter.getItemCount();
        List<Goods> list = new ArrayList<>();
        for (int i = count; i < count + 10; i++) {
            list.add(new Goods("name:" + i));
        }
        mGoodsPresenter.add(list);
    }


}
