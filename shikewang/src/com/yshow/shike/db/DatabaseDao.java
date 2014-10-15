package com.yshow.shike.db;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseDao {
    private MyOpenHelper myOpenHelper;
    private String result;
    private List<String> voide_list;

    public DatabaseDao(Context context) {
        myOpenHelper = new MyOpenHelper(context);
    }

    /**
     * 插入数据库
     *
     * @param poinstion
     * @param voidce_url
     */
    public void insert(String voidce_url) {
        SQLiteDatabase db = myOpenHelper.getWritableDatabase();
        if (db.isOpen()) {
            db.execSQL("insert into voidce (content) values (?)", new Object[]{voidce_url});
            db.close();
        }
    }

    /**
     * 查询数据库
     *
     * @param a
     * @return
     */
    public List<String> Query() {
        SQLiteDatabase db = myOpenHelper.getWritableDatabase();
        voide_list = new ArrayList<String>();
        if (db.isOpen()) {
            Cursor cursor = db.rawQuery("select * from voidce", null);
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
     * @param a
     * @return
     */
    public String Query_Condition(String voidce_url) {
        SQLiteDatabase db = myOpenHelper.getWritableDatabase();
        if (db.isOpen()) {
            Cursor cursor = db.rawQuery("select * from voidce where content = ?", new String[]{voidce_url});
            while (cursor.moveToNext()) {
                result = cursor.getString(cursor.getColumnIndex("content"));
            }
            cursor.close();
        }
        return result;
    }
}
