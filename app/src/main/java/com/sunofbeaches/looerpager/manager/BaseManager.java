package com.sunofbeaches.looerpager.manager;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.sunofbeaches.looerpager.db.BaseDao;
import com.sunofbeaches.looerpager.model.DBModel;
import com.sunofbeaches.looerpager.thread.ThreadPool;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.function.Consumer;

public class BaseManager<T extends DBModel> {

    private BaseDao<T> mBaseDao;

    public BaseManager(Context context) {
        Type genType = getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        Class mClass = (Class) params[0];

        mBaseDao = new BaseDao<>(context, mClass);
    }

    protected Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            handleMsg(msg);
        }
    };


    private void handleMsg(Message msg) {
        // 新增
        if (msg.what == 0) {
            handlerAddMsg(msg);
        } else if (msg.what == 1) {
            handlerQueryMsg(msg);
        }
    }

    protected void handlerQueryMsg(Message msg) {
    }

    protected void handlerAddMsg(Message msg) {
    }


    public void add(final List<T> list) {
        runTask(v -> {
            boolean status = mBaseDao.add(list);

            Message message = Message.obtain();
            message.what = 0;
            message.obj = status;
            mHandler.sendMessage(message);
        });
    }




    public void query() {
        runTask(v -> {
            List<T> list = mBaseDao.query();

            Message message = Message.obtain();
            message.what = 1;
            message.obj = list;
            mHandler.sendMessage(message);
        });
    }


    public void runTask(Consumer consumer) {
        ThreadPool.getInstance().runTask(new Runnable() {
            @Override
            public void run() {
                consumer.accept(null);
            }
        });
    }


}
