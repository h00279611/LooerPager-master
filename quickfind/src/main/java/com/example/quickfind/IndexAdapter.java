package com.example.quickfind;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.quickfind.model.Person;
import com.example.quickfind.util.ChineseCharToEn;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IndexAdapter extends BaseAdapter {

    private final List<Person> peopleList;
    private final Context context;
    private String PrePingYin = "";


    public IndexAdapter(Context context, List<Person> peopleList) {
        this.context = context;
        this.peopleList = peopleList;
    }

    @Override
    public int getCount() {
        return peopleList == null ? 0 : peopleList.size();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        IndexViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.index_item, null);
            viewHolder = new IndexViewHolder();
            viewHolder.tv_word = convertView.findViewById(R.id.tv_word);
            viewHolder.tv_name = convertView.findViewById(R.id.tv_name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (IndexViewHolder) convertView.getTag();
        }

        Person person = peopleList.get(position);
        String name = person.getName();
        String pingYin = person.getPingYin();

        viewHolder.tv_word.setText(pingYin);
        viewHolder.tv_name.setText(name);

        Log.d("IndexAdapter", "position:" + position + ", name:" + name + ", pingYin:" + pingYin + ", PrePingYin:" + PrePingYin);

        // 根据拼接的首字母是否和上一个相同，相同则隐藏当前的
        viewHolder.tv_word.setVisibility(View.VISIBLE);
        if (position > 0) {
            if (pingYin.equals(peopleList.get(position - 1).getPingYin())) {
                viewHolder.tv_word.setVisibility(View.GONE);
            } else {
                viewHolder.tv_word.setVisibility(View.VISIBLE);
            }
        }

        PrePingYin = pingYin;
        return convertView;
    }


    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    class IndexViewHolder {
        TextView tv_word;
        TextView tv_name;

    }

}
