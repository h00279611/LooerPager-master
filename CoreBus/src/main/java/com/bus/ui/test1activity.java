package com.bus.ui;

import java.util.UUID;

import com.bus.querytask.CoreBus;
import com.bus.querytask.QueryTask;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

/**
 * @descripton 单核单任务框架测试
 * @author lihongjiang
 * @version 1.0
 */
public class test1activity extends Activity {

	CoreBus qb;
	CoreBus qb2;
	QueryTask task;
	QueryTask task2;
	int i, j = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		qb = new CoreBus("CPU1");
		qb2 = new CoreBus("CPU2");
		task = new QueryTask(UUID.randomUUID()) {

			@Override
			public void onComplete() {
			
			}

			@Override
			public void doTask() {

			}
		};
		task2 = new QueryTask(UUID.randomUUID()) {

			@Override
			public void onComplete() {
			
			}

			@Override
			public void doTask() {

			}
		};
		qb.postQueryTask(task);
		qb2.postQueryTask(task2);
		qb.postQueryTask(task);
		qb2.postQueryTask(task2);
		qb.postQueryTask(task);
		qb2.postQueryTask(task2);
		qb.postQueryTask(task);
		qb2.postQueryTask(task2);
		qb.postQueryTask(task);
		qb2.postQueryTask(task2);
		qb.postQueryTask(task);
		qb2.postQueryTask(task2);

	}

	@Override
	protected void onDestroy() {
		if (qb != null) {
			// 移除消息或者线程
			qb.removeQueryTask(task);
			qb2.removeQueryTask(task2);
		}
		super.onDestroy();
	}
}
