package com.yshow.shike.wxapi;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.tencent.mm.sdk.openapi.BaseReq;
import com.tencent.mm.sdk.openapi.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.yshow.shike.utils.WeixinManager;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		WeixinManager mgr = new WeixinManager(this);
		mgr.getApi().handleIntent(getIntent(), this);
	}

	@Override
	public void onReq(BaseReq arg0) {
		this.finish();
	}

	@Override
	public void onResp(BaseResp arg0) {
		String result = "";
		switch (arg0.errCode) {
		case BaseResp.ErrCode.ERR_OK:
			result = "发送成功";
			break;
		case BaseResp.ErrCode.ERR_USER_CANCEL:
			result = "发送取消";
			break;
		case BaseResp.ErrCode.ERR_AUTH_DENIED:
			result = "发送被拒绝";
			break;
		default:
			result = "发送失败";
			break;
		}
		Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
		this.finish();
	}
}
