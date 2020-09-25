package com.bus.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class MultipleService extends Service {

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, final int startId) {
		new Thread() {
			public void run() {
				Log.d("SingleService", Thread.currentThread().getName());
				try {
					Thread.sleep(5 * 1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				stopSelf(startId);
			};
		}.start();
		return super.onStartCommand(intent, flags, startId);
	}
}
