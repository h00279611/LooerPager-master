package com.example.quickfind;

import androidx.appcompat.app.AppCompatActivity;

import android.nfc.tech.NfcB;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.quickfind.model.Person;
import com.example.quickfind.util.ChineseCharToEn;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity implements IndexView.DataChangeListener {

    private IndexView mIv_words;
    private TextView mTextView;
    private Handler mHandler = new Handler();
    private List<Person> mPersonList;
    private ListView mListView;
    private IndexAdapter mIndexAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initListener();
    }

    private void initListener() {
        mIv_words.setDataChangeListener(this);
    }

    private void initView() {
        mIv_words = findViewById(R.id.iv_words);
        mTextView =findViewById(R.id.tv_word);
        mListView = findViewById(R.id.lv_main);

        mPersonList = getPersonList();
        mIndexAdapter = new IndexAdapter(this, mPersonList);
        mListView.setAdapter(mIndexAdapter);
    }

    @Override
    public void onChange(String character) {
        mTextView.setVisibility(View.VISIBLE);
        mTextView.setText(character);

        // listView选中某行
        int selectPosition = getListViewSelectPosition(character);
        if (selectPosition != -1) {
            mListView.setSelection(selectPosition);
        }


        // 2秒后隐藏
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // 因为mHandler是主线程的，所有Runnable也是在主线程中执行的
                System.out.println("RunnableThread:" + Thread.currentThread().getName());
                mTextView.setVisibility(View.INVISIBLE);
            }
        }, 2000);
    }

    private int getListViewSelectPosition(String character) {
        for (int i = 0; i < mPersonList.size(); i++) {
            Person person = mPersonList.get(i);
            if(character.equals(person.getPingYin())){
                return i;
            }
        }
        return -1;
    }


    private List<Person> getPersonList() {
        List<Person> list = new ArrayList<>();
        list.add(new Person("爸爸"));
        list.add(new Person("妈"));
        list.add(new Person("黄印"));
        list.add(new Person("黄科"));
        list.add(new Person("李风秋"));
        list.add(new Person("大姐姐"));
        list.add(new Person("刘畅"));
        list.add(new Person("胡一天"));
        list.add(new Person("二姐姐"));
        list.add(new Person("黄子桐"));
        list.add(new Person("许晓莹"));
        list.add(new Person("张大千1"));
        list.add(new Person("张大千2"));
        list.add(new Person("张大千3"));
        list.add(new Person("张大千4"));
        list.add(new Person("张大千5"));
        list.add(new Person("张大千6"));
        list.add(new Person("张大千7"));
        list.add(new Person("菠菜"));
        list.add(new Person("黄坤1"));
        list.add(new Person("黄坤2"));
        list.add(new Person("黄坤3"));
        list.add(new Person("黄坤4"));
        list.add(new Person("黄坤5"));
        list.add(new Person("黄坤6"));
        list.add(new Person("黄坤7"));
        list.add(new Person("黄坤8"));
        list.add(new Person("黄坤9"));

        list.sort(new Comparator<Person>() {
            @Override
            public int compare(Person o1, Person o2) {
                return o1.getPingYin().compareTo(o2.getPingYin());
            }
        });
        return list;
    }
}
