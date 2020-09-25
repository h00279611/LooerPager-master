package com.bus.querytask;

/**
 * @descripton 队列任务
 * @author lihongjiang
 * @version 1.0
 */
public interface CallQueryTask extends Runnable{
	public void doTask();

	public void onComplete();

}
