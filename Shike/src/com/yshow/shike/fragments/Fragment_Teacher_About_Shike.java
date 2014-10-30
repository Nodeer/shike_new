package com.yshow.shike.fragments;

import java.util.ArrayList;

import android.text.TextUtils;

import com.yshow.shike.R;
import com.yshow.shike.activities.FankuiActivity;
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

/**
 * 老师的关于师课页面
 */
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

        TextView titletext = (TextView) findViewById(R.id.title_text);
        titletext.setText("关于师课");

        findViewById(R.id.gongneng_jieshao_btn).setOnClickListener(listener);
        findViewById(R.id.faq_btn).setOnClickListener(listener);
        findViewById(R.id.wenti_fankui_btn).setOnClickListener(listener);
        findViewById(R.id.left_btn).setOnClickListener(listener);

//        findViewById(R.id.tv_wenti_send).setOnClickListener(listener);
//        findViewById(R.id.tv_seek_all).setOnClickListener(listener);
//        findViewById(R.id.tv_faq_seek_all).setOnClickListener(listener);
//        findViewById(R.id.tv_onclick_seek_all).setOnClickListener(listener);
//        findViewById(R.id.tv_tea_back).setOnClickListener(listener);
//        tv_wenti_fankui_shuru = (EditText) findViewById(R.id.tv_wenti_fankui_shuru);
//        ev_premiere = (TextView) findViewById(R.id.ev_premiere);
//        ev_faq_huida = (TextView) findViewById(R.id.ev_faq_huida);
    }

    private OnClickListener listener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent;
            switch (v.getId()) {
                case R.id.wenti_fankui_btn:
                    intent = new Intent(context, FankuiActivity.class);
                    startActivity(intent);
                    break;
                case R.id.gongneng_jieshao_btn:// 功能介绍
                    intent = new Intent(context, WebActivity.class);
                    String url3 = softInfo.introduceurl;
                    String url = "http://apitest.shikeke.com/" + url3;
                    intent.putExtra("url", url);
                    intent.putExtra("title", "功能介绍");
                    startActivity(intent);
                    break;
                case R.id.faq_btn:
                    String url4 = softInfo.FAQ_Turl;
                    intent = new Intent(context, WebActivity.class);
                    String faqurl = "http://apitest.shikeke.com/" + url4;
                    intent.putExtra("url", faqurl);
                    intent.putExtra("title", "FAQ问答");
                    startActivity(intent);
                    break;
//                case R.id.tv_onclick_seek_all:
//                    Intent teatentent = new Intent(context, WebActivity.class);
//                    String teaurl = "http://www.shikeke.com/";
//                    teatentent.putExtra("url", teaurl);
//                    startActivity(teatentent);
//                    break;
                case R.id.left_btn:
                    finish();
                    break;
            }
        }
    };

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
