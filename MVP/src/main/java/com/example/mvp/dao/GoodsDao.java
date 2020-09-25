package com.example.mvp.dao;

import android.content.Context;

import com.example.common.db.BaseDao;
import com.example.mvp.bean.Goods;

public class GoodsDao extends BaseDao<Goods> {

    public GoodsDao(Context context, Class<Goods> mClass) {
        super(context, mClass);
    }


}
