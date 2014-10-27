package com.yshow.shike.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by iceman.xu on 2014/10/27.
 */
public class SharePreferenceUtil {
    private SharePreferenceUtil() {

    }

    private static SharePreferenceUtil sInstance;

    private SharedPreferences mSp;


    public static void init(Context context) {
        sInstance = new SharePreferenceUtil();
        sInstance.mSp = context.getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
    }

    public static SharePreferenceUtil getInstance() {
        return sInstance;
    }


    public void putBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = mSp.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public boolean getBoolean(String key) {
        return mSp.getBoolean(key, false);
    }

    public void putInt(String key, int value) {
        SharedPreferences.Editor editor = mSp.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public int getInt(String key) {
        return mSp.getInt(key, 0);
    }

    public void putString(String key, String value) {
        SharedPreferences.Editor editor = mSp.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public String getString(String key) {
        return mSp.getString(key, "");
    }
}
