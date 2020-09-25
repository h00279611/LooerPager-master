package com.bus.querytask;

import android.os.Handler.Callback;
import android.os.HandlerThread;
import android.os.Message;

/**
 * @descripton 界面更新管理
 * @author lihongjiang
 * @version 1.0
 */
public class CpuCore extends HandlerThread implements Callback {

	public CpuCore(String name) {
		super(name);
	}

	@Override
	public boolean handleMessage(Message msg) {
		return false;
	}

}
