package com.yshow.shike.fragments;

import java.util.ArrayList;

import android.text.TextUtils;
import com.yshow.shike.R;
import com.yshow.shike.activities.WebActivity;
import com.yshow.shike.entity.LoginManage;
import com.yshow.shike.entity.Soft_Info;
import com.yshow.shike.utils.MyAsyncHttpResponseHandler;
import com.yshow.shike.utils.SKAsyncApiController;
import com.yshow.shike.utils.SKResolveJsonUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Fragment_Teacher_About_Shike extends Activity {
	private Soft_Info softInfo;
	private Context context;
	private EditText tv_wenti_fankui_shuru;
	private TextView ev_premiere;
	private TextView ev_faq_huida;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_feacher_about_shike);
		context = this;
		initData();
	}

	private void initData() {
		Sofy_Info();
		findViewById(R.id.tv_wenti_send).setOnClickListener(listener);
		findViewById(R.id.tv_seek_all).setOnClickListener(listener);
		findViewById(R.id.tv_faq_seek_all).setOnClickListener(listener);
		findViewById(R.id.tv_onclick_seek_all).setOnClickListener(listener);
		findViewById(R.id.tv_tea_back).setOnClickListener(listener);
		tv_wenti_fankui_shuru = (EditText) findViewById(R.id.tv_wenti_fankui_shuru);
		ev_premiere = (TextView) findViewById(R.id.ev_premiere);
		ev_faq_huida = (TextView) findViewById(R.id.ev_faq_huida);
	}

	private OnClickListener listener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.tv_wenti_send:
				String content = tv_wenti_fankui_shuru.getText().toString().trim();
				if (TextUtils.isEmpty(content)) {
					Toast.makeText(Fragment_Teacher_About_Shike.this, "内容不能为空", Toast.LENGTH_SHORT).show();
				} else {
					Feed_Back_Question(content);
				}
				break;
			case R.id.tv_seek_all:// 功能介绍
				Intent intent = new Intent(context, WebActivity.class);
				String url3 = softInfo.introduceurl;
				ev_premiere.setText(softInfo.introduce);
				String url = "http://apitest.shikeke.com/" + url3;
				intent.putExtra("url", url);
				startActivity(intent);
				break;
			case R.id.tv_faq_seek_all:
				ev_faq_huida.setText(softInfo.FAQ_T);
				String url4 = softInfo.FAQ_Turl;
				Intent faqintent = new Intent(context, WebActivity.class);
				String faqurl = "http://apitest.shikeke.com/" + url4;
				faqintent.putExtra("url", faqurl);
				startActivity(faqintent);
				break;
			case R.id.tv_onclick_seek_all:
				Intent teatentent = new Intent(context, WebActivity.class);
				String teaurl = "http://www.shikeke.com/";
				teatentent.putExtra("url", teaurl);
				startActivity(teatentent);
				break;
			case R.id.tv_tea_back:
				finish();
				break;
			}
		}
	};

	// 意见反馈
	private void Feed_Back_Question(String contents) {
		SKAsyncApiController.Feed_Back(contents, new MyAsyncHttpResponseHandler(context, true) {
			@Override
			public void onSuccess(String json) {
				super.onSuccess(json);
				Toast.makeText(context, "你问题已接受", 0).show();
				tv_wenti_fankui_shuru.setText("");
			}
		});
	}

	// 關於綜合信息
	private void Sofy_Info() {
		SKAsyncApiController.Sof_Info(new MyAsyncHttpResponseHandler(context, true) {
			@Override
			public void onSuccess(String json) {
				super.onSuccess(json);
				boolean success = SKResolveJsonUtil.getInstance().resolveIsSuccess(json, context);
				if (success) {
					softInfo = SKResolveJsonUtil.getInstance().Soft_Info(json);
				}
			}
		});
	}
}
