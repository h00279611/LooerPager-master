package com.example.mvp.presenter;

import android.content.Context;

import com.example.common.listener.OnLoadListener;
import com.example.common.presenter.BasePresenter;
import com.example.mvp.bean.Goods;
import com.example.mvp.model.GoodsModel;
import com.example.mvp.model.IGoodsModel;
import com.example.mvp.view.IGoodsView;

import java.util.List;

public class GoodsPresenter extends BasePresenter<IGoodsView, GoodsModel> implements OnLoadListener<Goods> {

    // 需要使用弱引用：如果fetch查询时间较长，用户直接退出界面，不使用弱引用则Activity回收不了，导致内存泄露
    private IGoodsModel mIGoodsModel;

    private IGoodsView mIGoodsView;

    public GoodsPresenter(Context context, IGoodsView view) {
        super(view);
        this.mIGoodsModel = new GoodsModel(context);
        this.mIGoodsView = mTWeakReference.get();
    }


    public void fetch() {
        if (mIGoodsView != null && mIGoodsModel != null) {
            mIGoodsModel.loadGoodsData(this);
        }
    }


    public void batchAdd(List<Goods> list) {
        mIGoodsModel.batchInsert(list, this);
    }

    public void add(List<Goods> list) {
        mIGoodsModel.insert(list, this);
    }


    public void deleteAll(){
        mIGoodsModel.deleteAll(this);
    }


    @Override
    public void queryCompleted(List<Goods> list) {
        mIGoodsView.showGoodsView(list);
    }


    @Override
    public void queryError(int code, String message) {

    }

    @Override
    public void addCompleted(List<Goods> list, boolean status) {
        mIGoodsView.addData(list, status);
    }

    @Override
    public void deleteCompleted(boolean status) {
        mIGoodsView.delete(status);
    }

    @Override
    public void addProgress(List<Goods> list, int totalCount, int completeCount) {
        mIGoodsView.addProgress(list, totalCount, completeCount);
    }


}
