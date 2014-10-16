package com.yshow.shike.fragments;

import java.util.ArrayList;

import android.widget.EditText;
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
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class Fragment_Student_GuanYu extends Activity {
	private TextView tv_wenti_send;
	private EditText fankui;
	private String url;
	private TextView ev_premiere;
	private TextView ev_faq_huida;
	private Soft_Info softInfo;
	Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_student_guanyu);
		context = this;
		Sofy_Info();
		fankui = (EditText) findViewById(R.id.tv_wenti_fankui_shuru);
		tv_wenti_send = (TextView) findViewById(R.id.tv_wenti_send);
		ev_premiere = (TextView) findViewById(R.id.ev_premiere);
		ev_faq_huida = (TextView) findViewById(R.id.ev_faq_huida);
		tv_wenti_send.setOnClickListener(listener);
		findViewById(R.id.tv_seek_all).setOnClickListener(listener);
		findViewById(R.id.tv_faq_seek_all).setOnClickListener(listener);
		findViewById(R.id.tv_onclick_seek_all).setOnClickListener(listener);
		findViewById(R.id.tv_sk_back).setOnClickListener(listener);
	}

	private OnClickListener listener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.tv_wenti_send:// 发送意见反馈
				String content = fankui.getText().toString().trim();
				if (TextUtils.isEmpty(content)) {
					Toast.makeText(context, "请输入内容", 0).show();
				} else {
					Feed_Back_Question(content);
				}
				break;
			case R.id.tv_seek_all:// 功能介绍
				Intent intent = new Intent(context, WebActivity.class);
				String url3 = softInfo.introduceurl;
				ev_premiere.setText(softInfo.introduce);
				url = "http://apitest.shikeke.com/" + url3;
				intent.putExtra("url", url);
				startActivity(intent);
				break;
			case R.id.tv_faq_seek_all:// 如何获得学分.
				ev_faq_huida.setText(softInfo.FAQ);
				Intent faqintent = new Intent(context, WebActivity.class);
				String stuurl = "http://apitest.shikeke.com/" + softInfo.FAQurl;
				faqintent.putExtra("url", stuurl);
				startActivity(faqintent);
				break;
			case R.id.tv_onclick_seek_all:// 底部的查看全部
				Intent teatentent = new Intent(context, WebActivity.class);
				String teaurl = "http://www.shikeke.com/";
				teatentent.putExtra("url", teaurl);
				startActivity(teatentent);
				break;
			case R.id.tv_sk_back:
				finish();
				break;
			default:
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
				Toast.makeText(context, "你的问题已接受", 0).show();
				fankui.setText("");
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
