package com.yshow.shike.fragments;

import java.util.ArrayList;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.widget.EditText;

import com.yshow.shike.R;
import com.yshow.shike.activities.FankuiActivity;
import com.yshow.shike.activities.WebViewActivity;
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

/**
 * 学生的关于师课
 */
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

        TextView titletext = (TextView) findViewById(R.id.title_text);
        titletext.setText("关于师课");

        findViewById(R.id.gongneng_jieshao_btn).setOnClickListener(listener);
        findViewById(R.id.faq_btn).setOnClickListener(listener);
        findViewById(R.id.wenti_fankui_btn).setOnClickListener(listener);
        findViewById(R.id.left_btn).setOnClickListener(listener);


        PackageManager packageManager = getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String localVersion = packInfo.versionName;
        TextView versiontext = (TextView) findViewById(R.id.ver_name);
        versiontext.setText("当前版本: V" + localVersion);

        Sofy_Info();
    }

    private OnClickListener listener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent;
            switch (v.getId()) {
                case R.id.wenti_fankui_btn:// 发送意见反馈
                    intent = new Intent(context, FankuiActivity.class);
                    startActivity(intent);
                    break;
                case R.id.gongneng_jieshao_btn:// 功能介绍
                    intent = new Intent(context, WebViewActivity.class);
                    String url3 = softInfo.introduceurl;
                    url = "http://apitest.shikeke.com/" + url3;
                    intent.putExtra("url", url);
                    intent.putExtra("title", "功能介绍");
                    startActivity(intent);
                    break;
                case R.id.faq_btn:// 如何获得学分.
                    intent = new Intent(context, WebViewActivity.class);
                    String stuurl = "http://apitest.shikeke.com/" + softInfo.FAQurl;
                    intent.putExtra("url", stuurl);
                    intent.putExtra("title", "FAQ问答");
                    startActivity(intent);
                    break;
//			case R.id.tv_onclick_seek_all:// 底部的查看全部
//				Intent teatentent = new Intent(context, WebActivity.class);
//				String teaurl = "http://www.shikeke.com/";
//				teatentent.putExtra("url", teaurl);
//				startActivity(teatentent);
//				break;
                case R.id.left_btn:
                    finish();
                    break;
                default:
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
