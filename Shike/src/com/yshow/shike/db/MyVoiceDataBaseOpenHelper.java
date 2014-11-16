package com.yshow.shike.db;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 语音使用的数据库工具类
 */
public class MyVoiceDataBaseOpenHelper extends SQLiteOpenHelper{
	/**
	 * 创建数据库
	 * @param context
	 */
	public MyVoiceDataBaseOpenHelper(Context context) {
		super(context, "shike.db", null, 1);
	}
	/**
	 *创建表
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		String user_sql = "CREATE TABLE " + "voice" + "(" + "_id INTEGER  PRIMARY KEY ,"+ "mark TEXT ,"+ "content  TEXT)";
		db.execSQL(user_sql);
	}
	/**
	 *检查数据库是否更新 操作
	 */
	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
	}
}
