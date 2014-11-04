package com.yshow.shike.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;

import com.yshow.shike.entity.LoginManage;
import com.yshow.shike.fragments.Fragment_Message;

/**
 * Created by iceman.xu on 2014/10/17.
 */
public class MessageActivity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (LoginManage.getInstance().getStudent() == null) {
            startActivity(new Intent(this, StartingUp.class));
            finish();
            return;
        }
        
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Fragment_Message fragment_message = Fragment_Message.getInstance();
        transaction.add(Window.ID_ANDROID_CONTENT, fragment_message);
        transaction.commit();
    }
}
