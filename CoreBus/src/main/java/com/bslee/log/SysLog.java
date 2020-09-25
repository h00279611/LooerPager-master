package com.bslee.log;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class SysLog {

	private static boolean debug = true;

	private static boolean isDebug() {
		return debug;
	}

	public static void setDebug(boolean debug) {
		SysLog.debug = debug;
	}

	public static void show(String tag, String data) {
		if (isDebug()) {
			Log.d(tag, data);
			System.out.println(tag + "----" + data);
		}
	}

	public static void show(String data) {
		if (isDebug()) {
			Log.d("调试", data);
			System.out.println("调试" + "----" + data);
		}
	}

	public static void showToast(Context context, String tag) {
		if (isDebug()) {
			Toast.makeText(context, tag, Toast.LENGTH_LONG).show();
		}
	}
}
