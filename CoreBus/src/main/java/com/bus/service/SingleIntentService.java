package com.bus.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class SingleIntentService extends IntentService {
	public SingleIntentService() {
		// 设置子线程名称
		super("work thread");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Log.d("SingleService", Thread.currentThread().getName());
		try {
			Thread.sleep(5 * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
