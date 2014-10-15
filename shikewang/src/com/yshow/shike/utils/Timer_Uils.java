package com.yshow.shike.utils;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Timer_Uils {
	public void getTimer(final Handler handler) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				int Timer_record = 120;
				while (Timer_record >= 0) {
					try {
						sendMessage(handler, 0, Timer_record);
						SystemClock.sleep(1000);
						Timer_record--;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				sendMessage(handler, 1, -1);
			}
		}).start();
	}

	private void sendMessage(Handler handler, int sign, int value) {
		Message message = handler.obtainMessage();
		message.what = sign;
		if (value > 0) {
			Bundle data = new Bundle();
			data.putInt("time_record", value);
			message.setData(data);
		}
		handler.sendMessage(message);
	}

	public static String getCurrentTime(String format) {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
		String currentTime = sdf.format(date);
		return currentTime;
	}

	public static String getCurrentTime() {
		return getCurrentTime("yyyy-MM-dd  HH:mm:ss");
	}
}
