package com.yshow.shike.activities;

import com.umeng.analytics.MobclickAgent;
import com.yshow.shike.R;
import com.yshow.shike.entity.LoginManage;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class WebActivity extends BaseActivity implements OnClickListener {
    Context context;
    private String url;
    private WebView wv_webview;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.web_view);
        url = getIntent().getExtras().getString("url");

        String title = getIntent().getStringExtra("title");
        TextView titletext = (TextView) findViewById(R.id.title_text);
        titletext.setText(title);

        createMainView();
    }

    private void createMainView() {
        wv_webview = (WebView) findViewById(R.id.wv_webview);
        wv_webview.loadUrl(url);
        findViewById(R.id.left_btn).setOnClickListener(this);
        wv_webview.getSettings().setJavaScriptEnabled(true);
        wv_webview.getSettings().setBuiltInZoomControls(true);
        wv_webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        wv_webview.getSettings().setPluginState(PluginState.ON);
        wv_webview.setBackgroundColor(0);
        wv_webview.getSettings().setUseWideViewPort(true);
        wv_webview.getSettings().setLoadWithOverviewMode(true);
        wv_webview.setWebViewClient(new Client());
    }

    class Client extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (wv_webview != null) {
            wv_webview.setVisibility(View.GONE);
            wv_webview.removeAllViews();
            wv_webview.destroy();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_btn:
                finish();
                break;
        }
    }

}
