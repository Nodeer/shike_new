package com.yshow.shike.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import com.umeng.analytics.MobclickAgent;

public class Activity_Teacher_Account extends FragmentActivity{

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
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
