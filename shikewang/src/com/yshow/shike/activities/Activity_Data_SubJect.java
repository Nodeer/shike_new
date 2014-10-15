package com.yshow.shike.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.yshow.shike.R;
import com.yshow.shike.utils.Animation;

/**
 * 引导页设置
 * 
 * @author
 */
public class Activity_Data_SubJect extends Activity implements OnClickListener {
	Context context;
	private TextView tv_data;
	private TextView tv_subject;
	private Intent intent;
	private boolean isUnfold = true;
	private RelativeLayout data_relativeLayout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.data_subject);
		context = this;
		initData();
	}
	private void initData() {
		tv_data = (TextView) findViewById(R.id.tv_data);
		tv_data.setOnClickListener(this);
		tv_subject = (TextView) findViewById(R.id.tv_subject);
		data_relativeLayout = (RelativeLayout) findViewById(R.id.rl_data_relativeLayout);
		tv_subject.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_data:
			intent = getIntent();
			intent.putExtra("data", true);
			setResult(111111, intent);
			finish();
			break;
		case R.id.tv_subject:
			intent = getIntent();
			intent.putExtra("data", false);
			setResult(111111, intent);
			finish();
			break;
		case R.id.tv_data_back:
			finish();
			break;
		case R.id.tv_data_comfige:
			break;
		case R.id.iv_data_animation:
			Animation animation = new Animation(this, data_relativeLayout, -21,20);
			animation.initData(isUnfold);
			isUnfold = !isUnfold;
			break;
		}	
	}

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}