package com.yshow.shike.utils;

import android.util.Log;


public class LogUtil {
	private static boolean isRealease = true;

	private static final String DEFAULT_TAG = "shike_log";

	public static void v(String msg) {
		v(DEFAULT_TAG, msg);
	}

	public static void v(String TAG, String msg) {
		if (!isRealease) {
			Log.v(TAG, msg);
		}
	}

	public static void i(String msg) {
		i(DEFAULT_TAG, msg);
	}

	public static void i(String TAG, String msg) {
		if (!isRealease) {
			Log.i(TAG, msg);
		}
	}

	public static void d(String msg) {
		d(DEFAULT_TAG, msg);
	}

	public static void d(String TAG, String msg) {
		if (!isRealease) {
			Log.d(TAG, msg);
		}
	}

	public static void e(String msg) {
		e(DEFAULT_TAG, msg);
	}

	public static void e(String TAG, String msg) {
		if (!isRealease) {
			Log.e(TAG, msg);
		}
	}
}
