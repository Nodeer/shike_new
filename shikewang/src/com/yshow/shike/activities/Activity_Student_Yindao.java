package com.yshow.shike.activities;

import com.umeng.analytics.MobclickAgent;
import com.yshow.shike.R;
import com.yshow.shike.utils.Dialog;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class Activity_Student_Yindao extends Activity implements
OnClickListener {
	private ImageView yindao_1;// 引导图片1
	private ImageView yindao_2;// 引导图片2
	private ImageView yindao_3;// 引导图片3
	private ImageView yindao_4;// 引导图片4
	private ImageView yindao_5;// 引导图片5
	
	protected void onCreate(Bundle saveInsanceState) {
		super.onCreate(saveInsanceState);
		setContentView(R.layout.student_yindao);
		initData();
	}
	private void initData() {
		yindao_1 = (ImageView) findViewById(R.id.yindao_1);
		yindao_2 = (ImageView) findViewById(R.id.yindao_2);
		yindao_3 = (ImageView) findViewById(R.id.yindao_3);
		yindao_4 = (ImageView) findViewById(R.id.yindao_4);
		yindao_5 = (ImageView) findViewById(R.id.yindao_5);
		yindao_1.setOnClickListener(this);
		yindao_2.setOnClickListener(this);
		yindao_3.setOnClickListener(this);
		yindao_4.setOnClickListener(this);
		yindao_5.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.yindao_1:
			Yingdao();
			yindao_2.setVisibility(View.VISIBLE);
			break;
		case R.id.yindao_2:
			Yingdao();
			yindao_3.setVisibility(View.VISIBLE);
			break;
		case R.id.yindao_3:
			Yingdao();
			yindao_4.setVisibility(View.VISIBLE);
			break;
		case R.id.yindao_4:
			Yingdao();
			yindao_5.setVisibility(View.VISIBLE);
			break;
		case R.id.yindao_5:
			Yingdao();
			yindao_1.setVisibility(View.VISIBLE);
			break;
		}
	}
	private void Yingdao() {
		yindao_1.setVisibility(View.GONE);
		yindao_2.setVisibility(View.GONE);
		yindao_3.setVisibility(View.GONE);
		yindao_4.setVisibility(View.GONE);
		yindao_5.setVisibility(View.GONE);
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
