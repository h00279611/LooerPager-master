package com.bus.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.util.Log;

public class SingleService extends Service {
	private static final int NEW_INTENT_COMMING = 0;
	private Handler handler;

	private final class WorkThreadHanlder extends Handler {
		// 使用指定Looper创建Handler
		public WorkThreadHanlder(Looper looper) {
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) {
			if (msg.what == NEW_INTENT_COMMING) {
				Log.d("SingleService", Thread.currentThread().getName());
				try {
					Thread.sleep(5 * 1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				// arg1中存储的是onStartCommand()方法的startId参数
				stopSelf(msg.arg1);
			}
		}
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Message msg = handler.obtainMessage();
		msg.arg1 = startId;
		msg.what = NEW_INTENT_COMMING;
		msg.sendToTarget();
		return START_STICKY;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		HandlerThread thread = new HandlerThread("work thread",
				Process.THREAD_PRIORITY_BACKGROUND);
		// thread启动之后才能调用getLooper()方法获取thread中的Looper对象
		thread.start();
		// 使用子线程的Looper创建handler, 该handler绑定在子线程的消息队列上
		handler = new WorkThreadHanlder(thread.getLooper());
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
}
