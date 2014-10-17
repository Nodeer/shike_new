package com.yshow.shike.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;

import com.yshow.shike.fragments.Fragment_Find_Teacher;
import com.yshow.shike.fragments.Fragment_Message;

/**
 * Created by iceman.xu on 2014/10/17.
 */
public class FindTeacherActivity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Fragment_Find_Teacher fragment_message = new Fragment_Find_Teacher();
        transaction.add(Window.ID_ANDROID_CONTENT, fragment_message);
        transaction.commit();
    }
}
