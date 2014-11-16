package com.yshow.shike.db;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 语音使用的数据库工具
 */
public class VoiceDatabaseDao {
    private MyVoiceDataBaseOpenHelper myOpenHelper;
    private String result;
    private List<String> voide_list;

    public VoiceDatabaseDao(Context context) {
        myOpenHelper = new MyVoiceDataBaseOpenHelper(context);
    }

    /**
     * 插入数据库
     *
     * @param voidce_url
     */
    public void insert(String voidce_url) {
        SQLiteDatabase db = myOpenHelper.getWritableDatabase();
        if (db.isOpen()) {
            db.execSQL("insert into voice (content) values (?)", new Object[]{voidce_url});
            db.close();
        }
    }

    /**
     * 查询数据库
     *
     * @return
     */
    public List<String> Query() {
        SQLiteDatabase db = myOpenHelper.getWritableDatabase();
        voide_list = new ArrayList<String>();
        if (db.isOpen()) {
            Cursor cursor = db.rawQuery("select * from voice", null);
            while (cursor.moveToNext()) {
                result = cursor.getString(cursor.getColumnIndex("content"));
                voide_list.add(result);
            }
            cursor.close();
        }
        return voide_list;
    }

    /**
     * 查询数据库 单条语音
     *
     * @return
     */
    public String Query_Condition(String voidce_url) {
        SQLiteDatabase db = myOpenHelper.getWritableDatabase();
        if (db.isOpen()) {
            Cursor cursor = db.rawQuery("select * from voice where content = ?", new String[]{voidce_url});
            while (cursor.moveToNext()) {
                result = cursor.getString(cursor.getColumnIndex("content"));
            }
            cursor.close();
        }
        return result;
    }

    public boolean hasItem(String url) {
        boolean has = false;
        SQLiteDatabase db = myOpenHelper.getWritableDatabase();
        if (db.isOpen()) {
            Cursor cursor = db.rawQuery("select * from voice where content = ?", new String[]{url});
            if (cursor.getCount() == 0) {
                has = false;
            } else {
                has = true;
            }
            cursor.close();
        }
        return has;
    }
}
