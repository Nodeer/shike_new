package com.yshow.shike.utils;
import android.text.TextUtils;
import com.yshow.shike.entity.SKStudent;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
public class FileService {
	private SharedPreferences sp; // 获取sp对象
	public FileService(Context context) {
		super();
		sp = context.getSharedPreferences("shared_prefs",Context.MODE_WORLD_READABLE);
	}
	/**
	 * 采用SharedPreferences存储数据的操作
	 * @param name pass fileName
	 */
		public boolean saveBytsp(String name, String pass) {
			Boolean save = true;
			String oldName = sp.getString("name","");
			String[] split = oldName.split(",");
			for (String string : split) {
				if(name.equals(string)){
					save = false;
					break;
				}
			}
			if(save){
				if(oldName.length() == 0){
					oldName = name;
				}else {
					oldName = oldName + ","+name;
				}
				Editor editor = sp.edit();
				editor.putString("name", oldName);
				editor.putString("pass", pass);
				return editor.commit();
			}
			return false;
	}

    public void saveString(String name, String pass){
        Editor editor = sp.edit();
        editor.putString(name, pass);
        editor.commit();
    }

    public void saveAddString(String name, String pass) {
        String s = sp.getString(name, "");
        if(s.contains(pass)){
            return;
        }
        if (!TextUtils.isEmpty(s)) {
            s += ",";
            s += pass;
        }else{
            s = pass;
        }
        Editor editor = sp.edit();
        editor.putString(name, s);
        editor.commit();
    }

    /**
	 *  获取sp里面的数据 FileName 存到本地的文件名 key 关键字
	 */
	public String getSp_Date(String key){
		String str_name = sp.getString(key,"");
		return str_name;
	}
	// 动态的往数组里面添加数据
	public String[] insert(String[] arr, String str){
			int size = arr.length;
			String[] tmp = new String[size + 1];
			System.arraycopy(arr, 0, tmp, 0, size);
			tmp[size] = str;
			return tmp;
	}
	/**
	 *用来判断是否第一次登陆  获取数据
	 */
	public boolean getBoolean(final String Key,final boolean Value){
		return sp.getBoolean(Key,Value);
	}
	/**
	 * 用来判断是否第一次登陆  存取数据
	 */
	public boolean putBoolean(final Context pContext, final String Key,final boolean Value) {
		try {
			final SharedPreferences.Editor editor = sp.edit();
			editor.putBoolean(Key, Value);
			return editor.commit();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	/**
	 * 存取自动登录的用户昵称
	 * @param nickname 自动登录完成个人信息后记录用户名
	 * @param auto_user作为是否是第一次自动登录的标记
	 * @return
	 */
	public boolean set_auto_info(String name,String auto_user){
		Editor editor = sp.edit();
		editor.putString("auto_user_name",name);
		editor.putString("auto_user",auto_user);
		return editor.commit();
	}
	/**
	 * 采用SharedPreferences存储用户信息
	 * @param name pass fileName
	 */
		public boolean setStudent(String userinfo) {
				Editor editor = sp.edit();
				editor.putString("user_info", userinfo);
				return editor.commit();
	}
}