package com.example.common.thread;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPool {

    private static ThreadPoolExecutor mPoolExecutor;
    private static ThreadPool mThreadPool;

    public static ThreadPool getInstance() {
        if (mThreadPool == null) {
            mThreadPool = new ThreadPool();
            initPoolExecutor();
        }

        return mThreadPool;
    }

    private static void initPoolExecutor() {
        mPoolExecutor = new ThreadPoolExecutor(2, 5,
                5, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>(10));
    }

    private ThreadPool() {
    }


    public void runTask(Runnable runnable){
        if(mPoolExecutor == null){
            initPoolExecutor();
        }

        mPoolExecutor.execute(runnable);
    }


}
