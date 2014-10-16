package com.yshow.shike.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author fanshuo
 * @since 2013-4-3
 */
@SuppressWarnings("deprecation")
public class DateUtils {
	static SimpleDateFormat sSimpleDateFormat;

	public static String formatDate(final Date pDate) {
		final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return dateFormat.format(pDate);
	}

	public static String formatDateH(final Date pDate) {
		final DateFormat dateFormat = new SimpleDateFormat("HH:mm");
		return dateFormat.format(pDate);
	}

	public static String formatDate(final Date date, DateFormat dateFormat) {
		return dateFormat.format(date);
	}

	public static String formatDateL(final Date pDate) {
		final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return dateFormat.format(pDate);
	}

	public static String formatDateM(final Date pDate) {
		final DateFormat dateFormat = new SimpleDateFormat("MM/dd");
		return dateFormat.format(pDate);
	}

	public static String formatDateY(final Date pDate) {
		final DateFormat dateFormat = new SimpleDateFormat("yyyy");
		return dateFormat.format(pDate);
	}

	public static String formatDateTime(Date date) {
		String retStr = "";
		retStr = date.getHours() + ":" + date.getMinutes();
		return retStr;
	}

	public static String formatDateCN(Date pDate) {
		final DateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日");
		return dateFormat.format(pDate);
	}

	public static String formatDateDifference(final Date pDate) {
		// X绉掑墠
		final long differenceTime = (System.currentTimeMillis() - pDate
				.getTime()) / 1000;
		// X鍒嗛挓鍓?
		final long minute = differenceTime / 60;
		// X灏忔椂鍓?
		final long hour = minute / 60;
		// X澶╁墠
		final long day = hour / 24;

		if (differenceTime < 60) {
			return "1分钟前";
		} else if (minute < 60) {
			return minute + "分钟前";
		} else if (hour < 24) {
			return hour + "小时前";
		} else {
			return day + "天前";
		}

	}

	public static Date parseDate(final String pDateString) {
		if (sSimpleDateFormat == null) {
			sSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		}
		try {
			return sSimpleDateFormat.parse(pDateString);
		} catch (final ParseException e) {
			return new Date();
		}
	}

	public static Long getCurData() {
		return System.currentTimeMillis();
	}

	public static Long get12Befor() {
		long time = System.currentTimeMillis();
		time = -(24 * 60 * 60 * 12);
		return time;
	}

	public static Long get48Befor() {
		long time = System.currentTimeMillis();
		time = -(24 * 60 * 60 * 48);
		return time;
	}

	public static Long getMonthBefor() {
		long time = System.currentTimeMillis();
		time = -(24 * 60 * 60) * 30;
		return time;
	}

	public static Long getWeekBefor() {
		long time = System.currentTimeMillis();
		time = -(24 * 60 * 60);
		return time;
	}
}