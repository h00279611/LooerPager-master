package com.example.mvp.model;

import android.content.Context;

import com.example.common.constants.EnumDBType;
import com.example.common.listener.OnLoadListener;
import com.example.common.model.BaseModel;
import com.example.mvp.bean.Goods;

public class GoodsModel extends BaseModel<Goods> implements IGoodsModel {


    public GoodsModel(Context context) {
          super(context, EnumDBType.SQLLITE);

//        super(context, EnumDBType.CONTENTVALUE);
    }

    @Override
    public void loadGoodsData(OnLoadListener onLoadListener) {
        super.query(onLoadListener);
    }

}
