package com.example.mvp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mvp.R;
import com.example.mvp.bean.Goods;

import java.util.ArrayList;
import java.util.List;

public class GoodsAdapter extends RecyclerView.Adapter<GoodsAdapter.MyRecycicleHolder> {

    private List<Goods> mGoods;

    public GoodsAdapter() {
    }

    @NonNull
    @Override
    public MyRecycicleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_txt_item, parent, false);
        return new MyRecycicleHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyRecycicleHolder holder, int position) {
        Goods company = mGoods.get(position);
        holder.mTitleView.setText(company.getName());
    }

    @Override
    public int getItemCount() {
        return mGoods == null ? 0 : mGoods.size();
    }

    public void setDatas(List<Goods> list) {
        initGoods();
        this.mGoods.clear();
        this.mGoods.addAll(list);
        this.notifyDataSetChanged();
    }

    private void initGoods() {
        if (this.mGoods == null) {
            this.mGoods = new ArrayList<>();
        }
    }


    public void addDatas(List<Goods> list){
        initGoods();
        this.mGoods.addAll(list);
        this.notifyDataSetChanged();
    }

    public void clearDatas() {
        initGoods();
        this.mGoods.clear();
        this.notifyDataSetChanged();
    }


    class MyRecycicleHolder extends RecyclerView.ViewHolder {

        private final TextView mTitleView;
        private final Switch mSwitch;


        public MyRecycicleHolder(@NonNull View itemView) {
            super(itemView);

            mTitleView = itemView.findViewById(R.id.tv_txt);
            mSwitch = itemView.findViewById(R.id.tv_switch);
        }
    }
}
