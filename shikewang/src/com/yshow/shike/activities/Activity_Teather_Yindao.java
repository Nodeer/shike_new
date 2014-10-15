package com.yshow.shike.activities;

import com.umeng.analytics.MobclickAgent;
import com.yshow.shike.R;

import android.app.Activity;
import android.os.Bundle;

public class Activity_Teather_Yindao extends Activity{
	
	
	protected void onCreate(Bundle saveInsanceState) {
		super.onCreate(saveInsanceState);
		setContentView(R.layout.teacher_yindao);
		
	}

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

}
