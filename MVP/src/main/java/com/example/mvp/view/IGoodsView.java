package com.example.mvp.view;

import com.example.common.view.IBaseView;
import com.example.mvp.bean.Goods;

import java.util.List;

/**
 * UI逻辑
 */
public interface IGoodsView extends IBaseView {

    // 显示图片和文字

    // 读取数据
    void showGoodsView(List<Goods> companyList);


    // 添加完数据后
    void addData(List<Goods> companyList, boolean status);

    //  删除完数据后
    void delete(boolean status);

    // 加载进度条
    void addProgress(List<Goods> list, int totalCount, int completeCount);


    // 加载动画
}
