package com.sunofbeaches.looerpager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.sunofbeaches.looerpager.views.RecyclerView;

public class RecyclerViewTestActivity extends AppCompatActivity implements RecyclerView.Adapter {

    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recyclerview_test);
        mRecyclerView = findViewById(R.id.table);
        mRecyclerView.setAdapter(this);
    }

    @Override
    public View onCreateViewHodler(int postion, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_txt_item, parent, false);
        TextView textView = view.findViewById(R.id.tv_txt);
        textView.setText("第"+ postion +"行");
        return view;
    }

    @Override
    public View onBinderViewHodler(int postion, View convertView, ViewGroup parent) {
        TextView textView = convertView.findViewById(R.id.tv_txt);
        textView.setText("网易课程"+ postion);
        return convertView;
    }

    @Override
    public int getItemViewType(int row) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public int getCount() {
        return 30;
    }

    public int getHeight(int index){
        return 100;
    }
}
