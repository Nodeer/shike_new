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
public class WebActivity extends Activity implements OnClickListener{
	Context context;
	private RelativeLayout rl_web_relt;
	private String url;
	private WebView wv_webview;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		setContentView(R.layout.web_view);
		url = getIntent().getExtras().getString("url");
		createMainView();
	}
	private void createMainView() {
	   wv_webview = (WebView) findViewById(R.id.wv_webview);
	   wv_webview.loadUrl(url);
	   rl_web_relt = (RelativeLayout) findViewById(R.id.rl_web_relt);
	   findViewById(R.id.tv_web_back).setOnClickListener(this);
	   wv_webview.getSettings().setJavaScriptEnabled(true);
	   wv_webview.getSettings().setBuiltInZoomControls(true);
	   wv_webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
	   wv_webview.getSettings().setPluginState(PluginState.ON);
	   wv_webview.setBackgroundColor(0);
	   wv_webview.getSettings().setUseWideViewPort(true);
	   wv_webview.getSettings().setLoadWithOverviewMode(true);
	   wv_webview.setWebViewClient(new Client());
	   if(LoginManage.getInstance().isTeacher()){
		   rl_web_relt.setBackgroundResource(R.color.button_teather_typeface_color);
	   }
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
		case R.id.tv_web_back:
			finish();
			break;
		}		
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
