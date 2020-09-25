package com.bus.asynchandler;
import android.content.AsyncQueryHandler;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
/**
 * 异步任务类
 * 
 * @description 所有异步任务拥有各自的UI更新Handler,共享一个handler消息通信
 * @author lihongjiang
 * 
 */
public class ShareCoreAsyncWorkHandler extends Handler {

	private static final String TAG = "AsyncWorkHandler";
	private static Looper sLooper = null;
	private WorkerHandler mWorkerHanler;

	protected final class WorkerArgs {
		Handler handler;
	}

	public ShareCoreAsyncWorkHandler() {
		synchronized (AsyncQueryHandler.class) {
			if (sLooper == null) {
				HandlerThread thread = new HandlerThread("AsyncWorkHandler");
				thread.start();
				sLooper = thread.getLooper();
			}
		}
		mWorkerHanler = new WorkerHandler(sLooper);
	}

	protected class WorkerHandler extends Handler {
		public WorkerHandler(Looper looper) {
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) {
			WorkerArgs args = (WorkerArgs) msg.obj;
			int info = msg.arg1;
			Log.i(TAG, "worker handler=-------------------" + info);
			Message result = args.handler.obtainMessage();
			result.arg1 = info;
			result.sendToTarget();
		}

	}

	/**
	 * 需要重写的回调函数
	 */
	protected void onCompleteWork(int taskId) {

	}

	/**
	 * 执行任务
	 * 
	 * @param strInfo
	 * @param task
	 */
	public void doWork(final int strInfo, final SingleTask task) {

		mWorkerHanler.post(new Runnable() {

			@Override
			public void run() {
				task.doTask();
				Message msg = mWorkerHanler.obtainMessage();
				WorkerArgs workArgs = new WorkerArgs();
				workArgs.handler = ShareCoreAsyncWorkHandler.this;
				msg.obj = workArgs;
				msg.arg1 = strInfo;
				mWorkerHanler.sendMessage(msg);
			}
		});

	}

	/**
	 * 执行UI更新
	 * 
	 * @param strInfo
	 * @param task
	 */
	public void doWork(int strInfo) {

		Message msg = mWorkerHanler.obtainMessage();
		WorkerArgs workArgs = new WorkerArgs();
		workArgs.handler = ShareCoreAsyncWorkHandler.this;
		msg.obj = workArgs;
		msg.arg1 = strInfo;
		mWorkerHanler.sendMessage(msg);

	}

	@Override
	public void handleMessage(Message msg) {
		Log.i(TAG, "main handler ----------------" + msg.arg1);
		onCompleteWork(msg.arg1);

	}
}