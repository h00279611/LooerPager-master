package com.sunofbeaches.looerpager.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sunofbeaches.looerpager.R;
import com.sunofbeaches.looerpager.model.PagerItem;

import java.util.ArrayList;
import java.util.List;

public class MyRecycicleViewAdapter extends RecyclerView.Adapter<MyRecycicleViewAdapter.MyRecycicleHolder> {

    private boolean selectAll;
    private boolean clickCheckAll;
    private List<PagerItem> mArrayList = new ArrayList<>();
    private List<MyRecycicleHolder> mHolderList = new ArrayList<>();
    private AddDataListener mAddDataListener;


    public MyRecycicleViewAdapter(List<PagerItem> datas) {
        this(datas, false);
    }

    public MyRecycicleViewAdapter(List<PagerItem> datas, boolean selectAll) {
        this.selectAll = selectAll;
        if(selectAll){
            mArrayList.clear();
            mArrayList.add(new PagerItem("添加",""));
            mArrayList.addAll(datas);
        }else{
            this.mArrayList = datas;
        }
    }


    public void addDataAtLast(PagerItem pagerItem){
        addDataAt(pagerItem, mArrayList.size());
    }

    public void addDataAt(PagerItem pagerItem, int position){
        mArrayList.add(position, pagerItem);
        notifyItemInserted(position);
    }



    public void setAddDataListener(AddDataListener addDataListener) {
        mAddDataListener = addDataListener;
    }

    @NonNull
    @Override
    public MyRecycicleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_txt_item, parent, false);
        return new MyRecycicleHolder(view);
    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull MyRecycicleHolder holder, int position) {
        PagerItem item = mArrayList.get(position);
        holder.mTitleView.setText(item.getTitle());
        holder.mSwitch.setOnCheckedChangeListener(new MyRecycicleCheckedChangeListener(position));
        if(selectAll && position ==0){
            holder.mTitleView.setTextColor(Color.BLUE);
            holder.mTitleView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 弹出所有未被关注的公司的选择列表对应的Dialog
                    mAddDataListener.addData(v);
                }
            });
        }

        if (mHolderList.size() < getItemCount()) {
            mHolderList.add(position, holder);
        } else {
            mHolderList.set(position, holder);
        }

    }




    public interface AddDataListener {
        void addData(View v);
    }



    class MyRecycicleCheckedChangeListener implements CompoundButton.OnCheckedChangeListener{
        private int position;


        public MyRecycicleCheckedChangeListener(int position){
            this.position = position;
        }


        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(selectAll){
                if(position == 0){
                    clickCheckAll = true;
                    updateAllSwitch(isChecked);
                }else{
                    if(clickCheckAll){
                        return;
                    }
                    checkAndSetSwitch(isChecked);
                }
            }
        }

        private void checkAndSetSwitch(boolean isChecked) {
            MyRecycicleHolder allMyRecycicleHolder = mHolderList.get(0);
            if (!isChecked) {
                allMyRecycicleHolder.mSwitch.setChecked(false);
                return;
            }


            for (MyRecycicleHolder myRecycicleHolder : mHolderList) {
                boolean currIsChecked = myRecycicleHolder.mSwitch.isChecked();
                if (!currIsChecked) {
                    allMyRecycicleHolder.mSwitch.setChecked(false);
                    return;
                }
            }

            allMyRecycicleHolder.mSwitch.setChecked(true);
        }



        private void updateAllSwitch(boolean isChecked) {
            for (MyRecycicleHolder myRecycicleHolder : mHolderList) {
                myRecycicleHolder.mSwitch.setChecked(isChecked);
            }
        }
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
