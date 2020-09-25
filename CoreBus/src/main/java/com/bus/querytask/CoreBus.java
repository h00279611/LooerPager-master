package com.bus.querytask;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map.Entry;
import java.util.UUID;

import com.bslee.log.SysLog;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * @descripton 单核单任务模型
 * @author lihongjiang
 * @version 1.0
 */
public class CoreBus {

	// 统一处理UI界面更新
	private Handler mainHandler = new Handler();
	// 内部消息处理总线,各自处理自己的消息分发
	private Handler handler;
	// 任务托管
	private LinkedHashMap<UUID, CallQueryTask> tasks = new LinkedHashMap<UUID, CallQueryTask>();
	// 总线创建
	public CoreBus(String name) {
		tasks.clear();
		CpuCore workhandler = new CpuCore(name) {
			@Override
			public boolean handleMessage(final Message msg) {
				if (0x8888 == msg.what) {
					final UUID  uuid=(UUID) msg.obj;
					final CallQueryTask task = tasks.get(uuid);
					if (task!=null) {
						SysLog.show("CoreBus", "任务实例存在");
						// 必须是主线程的消息Handler处理器
						mainHandler.post(new Runnable() {
							@Override
							public void run() {
								SysLog.show("CoreBus", "任务完成开始回调:"+uuid.toString());
								try {
									task.onComplete();
									//防止Activity退出,线程未移除,回调导致ANR
								} catch (Exception e) {
									SysLog.show("CoreBus", "异常抓住"+uuid.toString());
									e.printStackTrace();
								}
								SysLog.show("CoreBus", "任务完成结束回调:"+uuid.toString());
							}
						});
					}else{
						SysLog.show("CoreBus", "任务实例不存在");
					}		
				}
				return false;
			}
		};
		workhandler.setPriority(Thread.MIN_PRIORITY);
		workhandler.start();
		SysLog.show("CoreBus", "CPU名称:"+workhandler.getName());
		// 工作hander在非UI线程中自己给自己发消息处理,可以实现单核单任务模型
		handler = new Handler(workhandler.getLooper(), workhandler);
	}

	/**
	 * 启动任务,是异步方法
	 * 
	 * @param task
	 */
	public void postQueryTask(QueryTask task) {
		task.handler = handler;
		tasks.put(task.taskId,  task);
		handler.post(task);
		SysLog.show("CoreBus", "任务开始运行:"+task.taskId.toString());
		SysLog.show("CoreBus", "任务总大小:"+tasks.size());
	}
	
	/**
	 * 取消存在任务实例
	 * 
	 * @param task
	 */
	public synchronized void removeQueryTask(QueryTask task) {
		if (handler != null) {
			tasks.remove(task.taskId);
			handler.removeCallbacks(task);
			SysLog.show("CoreBus", "任务结束"+task.taskId.toString());
			SysLog.show("CoreBus", "任务结束tasks大小:"+tasks.size());
		}
	}

	/**
	 * 按任务Id取消任务,主要场合用于删除匿名任务.
	 * 
	 * @param taskId
	 */
	public synchronized void removeQueryTask(UUID taskId) {
		if (handler != null) {
			handler.removeCallbacks(tasks.get(taskId));
			tasks.remove(taskId);
			SysLog.show("CoreBus", "任务结束"+taskId.toString());
			SysLog.show("CoreBus", "任务结束tasks大小:"+tasks.size());
		}
	}

	public void removeQueryTaskAll() {

           for (Entry<UUID, CallQueryTask> keys:tasks.entrySet()) {
        	   handler.removeCallbacks(keys.getValue());
		  }
           tasks.clear();
	}

}
