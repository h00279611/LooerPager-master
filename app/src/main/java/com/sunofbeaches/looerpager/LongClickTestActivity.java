package com.sunofbeaches.looerpager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LongClickTestActivity extends AppCompatActivity implements OnClickListener {

    private static final int NOSELECT_STATE = -1;// 表示未选中任何CheckBox

    private ListView listView;
    private Button bt_cancel, bt_delete;
    private TextView tv_sum;
    private LinearLayout linearLayout;

    private List<String> list = new ArrayList<String>();// 数据
    private List<String> chooseList = new ArrayList<String>();// 需要删除的数据
    private boolean isMultiSelect = false;// 是否处于多选状态

    private MyAdapter adapter;// ListView的Adapter
    private ActionBar mActionBar;
    private Menu mMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_long_click_test);
        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setTitle("测试");


        listView = (ListView) findViewById(R.id.listView1);
        bt_cancel = (Button) findViewById(R.id.bt_cancel);
        bt_delete = (Button) findViewById(R.id.bt_delete);
        tv_sum = (TextView) findViewById(R.id.tv_sum);
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        bt_cancel.setOnClickListener(this);
        bt_delete.setOnClickListener(this);

        // 设置初始数据
        list = new ArrayList<String>();
        for (int i = 0; i < 10; i++) {
            String str = "Item" + i;
            list.add(str);
        }

        // 未选中任何Item，position设置为-1
        adapter = new MyAdapter(LongClickTestActivity.this, list, NOSELECT_STATE);
        listView.setAdapter(adapter);


    }

    private void refershMenu() {
        mMenu.getItem(1).setEnabled(false);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.view_companay_menu, menu);
        for(int i=0; i < 4; i++){
            menu.getItem(i).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }

        mMenu = menu;

        refershMenu();
        return true;
    }

    private class MyAdapter extends BaseAdapter {

        private List<String> list;
        private LayoutInflater inflater;

        private HashMap<Integer, Integer> isCheckBoxVisible;// 用来记录是否显示checkBox
        private HashMap<Integer, Boolean> isChecked;// 用来记录是否被选中

        @SuppressLint("UseSparseArrays")
        public MyAdapter(Context context, List<String> list, int position) {
            inflater = LayoutInflater.from(context);
            this.list = list;
            isCheckBoxVisible = new HashMap<Integer, Integer>();
            isChecked = new HashMap<Integer, Boolean>();
            // 如果处于多选状态，则显示CheckBox，否则不显示
            if (isMultiSelect) {
                for (int i = 0; i < list.size(); i++) {
                    isCheckBoxVisible.put(i, CheckBox.VISIBLE);
                    isChecked.put(i, false);
                }
            } else {
                for (int i = 0; i < list.size(); i++) {
                    isCheckBoxVisible.put(i, CheckBox.INVISIBLE);
                    isChecked.put(i, false);
                }
            }

            // 如果长按Item，则设置长按的Item中的CheckBox为选中状态
            if (isMultiSelect && position >= 0) {
                isChecked.put(position, true);
            }
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = inflater.inflate(R.layout.item_layout_test, null);
                viewHolder.tv_Name = (TextView) convertView.findViewById(R.id.tv_name);
                viewHolder.cb = (CheckBox) convertView.findViewById(R.id.cb_select);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            final String str = list.get(position);
            viewHolder.tv_Name.setText(str);
            // 根据position设置CheckBox是否可见，是否选中
            viewHolder.cb.setChecked(isChecked.get(position));
            viewHolder.cb.setVisibility(isCheckBoxVisible.get(position));
            // ListView每一个Item的长按事件
            convertView.setOnLongClickListener(new onMyLongClick(position, list));
            /*
             * 在ListView中点击每一项的处理
             * 如果CheckBox未选中，则点击后选中CheckBox，并将数据添加到list_delete中
             * 如果CheckBox选中，则点击后取消选中CheckBox，并将数据从list_delete中移除
             */
            convertView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // 处于多选模式
                    if (isMultiSelect) {
                        if (viewHolder.cb.isChecked()) {
                            viewHolder.cb.setChecked(false);
                            chooseList.remove(str);
                        } else {
                            viewHolder.cb.setChecked(true);
                            chooseList.add(str);
                        }
                        tv_sum.setText("共选择了" + chooseList.size() + "项");
                    }
                }
            });
            return convertView;
        }

        class ViewHolder {
            public TextView tv_Name;
            public CheckBox cb;
        }

        // 自定义长按事件
        class onMyLongClick implements OnLongClickListener {

            private int position;
            private List<String> list;

            // 获取数据，与长按Item的position
            public onMyLongClick(int position, List<String> list) {
                this.position = position;
                this.list = list;
            }

            // 在长按监听时候，切记将监听事件返回ture
            @Override
            public boolean onLongClick(View v) {
                isMultiSelect = true;
                chooseList.clear();
                // 添加长按Item到删除数据list中
                chooseList.add(list.get(position));
                linearLayout.setVisibility(View.VISIBLE);
                tv_sum.setText("共选择了" + chooseList.size() + "项");
                for (int i = 0; i < list.size(); i++) {
                    adapter.isCheckBoxVisible.put(i, CheckBox.VISIBLE);
                }
                // 根据position，设置ListView中对应的CheckBox为选中状态
                adapter = new MyAdapter(LongClickTestActivity.this, list, position);
                listView.setAdapter(adapter);
                return true;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 取消选择
            case R.id.bt_cancel:
                isMultiSelect = false;// 退出多选模式
                chooseList.clear();// 清楚选中的数据
                // 重新加载Adapter
                adapter = new MyAdapter(LongClickTestActivity.this, list, NOSELECT_STATE);
                listView.setAdapter(adapter);
                linearLayout.setVisibility(View.GONE);
                break;
            // 删除
            case R.id.bt_delete:
                isMultiSelect = false;
                // 将数据从list中移除
                for (int i = 0; i < list.size(); i++) {
                    for (int j = 0; j < chooseList.size(); j++) {
                        if (list.get(i).equals(chooseList.get(j))) {
                            list.remove(i);
                        }
                    }
                }
                chooseList.clear();
                // 重新加载Adapter
                adapter = new MyAdapter(LongClickTestActivity.this, list, NOSELECT_STATE);
                listView.setAdapter(adapter);
                linearLayout.setVisibility(View.GONE);
                break;

            default:
                break;
        }
    }
}
