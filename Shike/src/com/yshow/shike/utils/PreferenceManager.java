package com.yshow.shike.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @since 2011-11-13
 */
public class PreferenceManager {
	public static final String KEY_USERNAME = "KEY_USERNAME";
	public static final String KEY_MEMBER_ID = "KEY_MEMBER_ID";
	public static final String KEY_PASS_KEY = "KEY_PASS_KEY";
	public static final String KEY_IS_REMEMBER_PASSWORD = "com.shike.is.check.password";
	public static final String KEY_SK_PASSWORD = "com.shike.password";
	public static final String KEY_SK_ACCOUNT = "com.shike.account";
	public static final String KEY_CHECK_UPDATE_NEXT_SHOW_TIME = "update_nexte_show_time";// 妫?煡鏇存柊涓嬩竴娆℃彁绀虹殑鏃堕棿

	private static SharedPreferences getSharedPreferences(final Context pContext) {
		if (pContext == null) {
			return null;
		}
		return android.preference.PreferenceManager
				.getDefaultSharedPreferences(pContext);
	}

	public static float getFloat(final Context pContext, final String pKey,
			final float pDefaultValue) throws Exception {
		return PreferenceManager.getSharedPreferences(pContext).getFloat(pKey,
				pDefaultValue);
	}

	public static boolean putFloat(final Context pContext, final String pKey,
			final float pValue) {
		try {
			final SharedPreferences.Editor editor = PreferenceManager
					.getSharedPreferences(pContext).edit();
			editor.putFloat(pKey, pValue);
			return editor.commit();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static int getInt(final Context pContext, final String pKey,
			final int pDefaultValue) throws Exception {
		return PreferenceManager.getSharedPreferences(pContext).getInt(pKey,
				pDefaultValue);
	}

	public static boolean putInt(final Context pContext, final String pKey,
			final int pValue) {
		try {
			final SharedPreferences.Editor editor = PreferenceManager
					.getSharedPreferences(pContext).edit();
			editor.putInt(pKey, pValue);
			return editor.commit();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static long getLong(final Context pContext, final String pKey,
			final long pDefaultValue) throws Exception {
		return PreferenceManager.getSharedPreferences(pContext).getLong(pKey,
				pDefaultValue);
	}

	public static boolean putLong(final Context pContext, final String pKey,
			final long pValue) {
		try {
			final SharedPreferences.Editor editor = PreferenceManager
					.getSharedPreferences(pContext).edit();
			editor.putLong(pKey, pValue);
			return editor.commit();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean getBoolean(final Context pContext, final String pKey,
			final boolean pDefaultValue) throws Exception {
		return PreferenceManager.getSharedPreferences(pContext).getBoolean(
				pKey, pDefaultValue);
	}

	public static boolean putBoolean(final Context pContext, final String pKey,
			final boolean pValue) {
		try {
			final SharedPreferences.Editor editor = PreferenceManager.getSharedPreferences(pContext).edit();
			editor.putBoolean(pKey, pValue);
			return editor.commit();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static String getString(final Context pContext, final String pKey,
			final String pDefaultValue) throws Exception {
		return PreferenceManager.getSharedPreferences(pContext).getString(pKey,
				pDefaultValue);
	}

	public static boolean putString(final Context pContext, final String pKey,
			final String pValue) {
		try {
			final SharedPreferences.Editor editor = PreferenceManager
					.getSharedPreferences(pContext).edit();
			editor.putString(pKey, pValue);
			return editor.commit();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean remove(final Context context, final String key) {
		try {
			final SharedPreferences.Editor editor = PreferenceManager
					.getSharedPreferences(context).edit();
			editor.remove(key);
			return editor.commit();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static class Config {
		public static final boolean DEVELOPER_MODE = false;
	}

}