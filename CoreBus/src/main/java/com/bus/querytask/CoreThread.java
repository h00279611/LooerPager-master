package com.bus.querytask;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
/**
 * @descripton 任务执行类
 * @author lihongjiang
 * @version 1.0
 */
public  class CoreThread implements Runnable {
		public QueryTask task;
		public int taskId = 0;
		public Handler handler;
		public CoreThread(QueryTask task, int taskId) {
			this.task = task;
			this.taskId = taskId;

		}

		@Override
		public void run() {
			Log.v("test","task start");
			task.doTask();
			Message msg = handler.obtainMessage();
			msg.what = taskId;
			handler.sendMessage(msg);
		}
	}