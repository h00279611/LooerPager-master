package com.example.common.model;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.example.common.bean.DBModel;
import com.example.common.bean.Progress;
import com.example.common.bean.Status;
import com.example.common.constants.EnumDBType;
import com.example.common.db.BaseDataDone;
import com.example.common.db.DBFactory;
import com.example.common.listener.OnLoadListener;
import com.example.common.thread.ThreadPool;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.function.Consumer;

public class BaseModel<T extends DBModel> implements IBaseModel<T> {

    protected BaseDataDone<T> mBaseDao;

    protected MyHandler mHandler;


    public BaseModel(Context context, EnumDBType dbType) {
        Type genType = getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        Class mClass = (Class) params[0];

        mHandler = new MyHandler();
        mBaseDao = DBFactory.getInstance().getBaseDao(context, mClass, dbType);
    }


    protected void handleMsg(Message msg, OnLoadListener onLoadListener) {
        // 新增
        if (msg.what == 0) {
            Status statusObj = (Status) msg.obj;
            onLoadListener.addCompleted(statusObj.datas, statusObj.status);
        } else if (msg.what == 1) {
            List<T> list = (List<T>) msg.obj;
            onLoadListener.queryCompleted(list);
        } else if (msg.what == 2) {
            boolean status = (boolean) msg.obj;
            onLoadListener.deleteCompleted(status);
        } else if (msg.what == 3) {
            Progress progress = (Progress) msg.obj;
            onLoadListener.addProgress(progress.list, progress.totalCount, progress.completedCount);
        }
    }


    public void batchInsert(List<T> list, OnLoadListener listener) {
        mHandler.setOnLoadListener(listener);

        runTask(v -> {
            boolean status = mBaseDao.batchInsert(list);
            Message message = Message.obtain();
            message.what = 0;
            message.obj = new Status(list, status);
            mHandler.sendMessage(message);
        });
    }


    public void insert(List<T> list, OnLoadListener listener) {
        mHandler.setOnLoadListener(listener);

        runTask(v -> {
            int count = 1;
            for (T t : list) {
                mBaseDao.insert(t);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Message message = Message.obtain();
                message.what = 3;
                message.obj = new Progress(list, list.size(), count);
                mHandler.sendMessage(message);
                count++;
            }

        });
    }


    public void query(OnLoadListener listener) {
        mHandler.setOnLoadListener(listener);

        runTask(v -> {
            List<T> list = mBaseDao.query();
            Message message = Message.obtain();
            message.what = 1;
            message.obj = list;
            mHandler.sendMessage(message);
        });
    }

    public void deleteAll(OnLoadListener listener) {
        mHandler.setOnLoadListener(listener);

        runTask(v -> {
            boolean status = mBaseDao.deleteAll();
            Message message = Message.obtain();
            message.what = 2;
            message.obj = status;
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


    private class MyHandler extends Handler {
        OnLoadListener mOnLoadListener;


        public void setOnLoadListener(OnLoadListener onLoadListener) {
            this.mOnLoadListener = onLoadListener;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            handleMsg(msg, mOnLoadListener);
        }

    }
}
