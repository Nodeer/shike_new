package com.yshow.shike.activities;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.yshow.shike.R;

/**
 * Created by Administrator on 2014-10-28.
 */
public class WebViewActivity extends BaseActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage_webview_layout);


        findViewById(R.id.left_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        WebView webView = (WebView) findViewById(R.id.webview);
        String url = getIntent().getStringExtra("url");
        String title = getIntent().getStringExtra("title");
        TextView titletext = (TextView) findViewById(R.id.title_text);
        if (title != null) {
            titletext.setText(title);
        }
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        webView.loadUrl(url);
    }
}
