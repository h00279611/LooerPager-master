package com.example.mvp.model;

import com.example.common.listener.OnLoadListener;
import com.example.common.model.IBaseModel;
import com.example.mvp.bean.Goods;

public interface IGoodsModel extends IBaseModel<Goods> {

    void loadGoodsData(OnLoadListener onLoadListener);

}
