package com.yshow.shike.activities;
import com.umeng.analytics.MobclickAgent;
import com.yshow.shike.R;
import com.yshow.shike.utils.Animation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
public class Activity_Teather_Ti_Ku_seleck extends Activity {
	private RelativeLayout rl_tiku;
	private static boolean isUnfold = true;
	Context context;
	private TextView tv_time;
	private TextView tv_file;
	Intent intent;
     @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.ti_ku_seleck);
    	context = this;
    	initData();
    }
	private void initData() {
		rl_tiku = (RelativeLayout) findViewById(R.id.rl_tiku);
		tv_file = (TextView) findViewById(R.id.iv_tiku_file);
		tv_time = (TextView) findViewById(R.id.iv_tiku_time);
		findViewById(R.id.iv_tiku_back).setOnClickListener(listener);
		findViewById(R.id.iv_tiku_confirm).setOnClickListener(listener);
		findViewById(R.id.iv_tiku_anim).setOnClickListener(listener);
		tv_time.setOnClickListener(listener);
		tv_file.setOnClickListener(listener);
	}
	private OnClickListener listener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.iv_tiku_back:
				finish();
				break;
			case R.id.iv_tiku_confirm:
				
				break;
			case R.id.iv_tiku_anim:
				Animation animation = new Animation(context, rl_tiku, -18,20);
				animation.initData(isUnfold);
				isUnfold = !isUnfold;
				break;
			case R.id.iv_tiku_time:
				intent = getIntent();
				intent.putExtra("time", tv_time.getText().toString().trim());
				setResult(1, intent);
				finish();
				break;
			case R.id.iv_tiku_file:
				intent = getIntent();
				intent.putExtra("file", tv_file.getText().toString().trim());
				setResult(0, intent);
				finish();
				break;
			}
		}
	};

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
