package com.bus.querytask;

import java.util.UUID;

import android.os.Handler;
import android.os.Message;

import com.bslee.log.SysLog;

/**
 * @descripton 任务执行类
 * @author lihongjiang
 * @version 1.0
 */
public abstract class QueryTask implements  CallQueryTask {
	public UUID taskId = UUID.randomUUID();
	public Handler handler;

	public QueryTask(UUID taskId) {
		this.taskId = taskId;
	}

	@Override
	public void run() {
		SysLog.show("CoreBus", "CoreBus运行任务:" + taskId.toString());
		doTask();
		Message msg = handler.obtainMessage();
		msg.what = 0x8888;
		msg.obj = taskId;
		handler.sendMessage(msg);
		SysLog.show("CoreBus", "CoreBus运行任务结束,转发消息" + taskId.toString());
	}

}