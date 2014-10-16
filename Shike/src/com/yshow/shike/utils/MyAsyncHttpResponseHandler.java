package com.yshow.shike.utils;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.yshow.shike.UIApplication;

public class MyAsyncHttpResponseHandler extends AsyncHttpResponseHandler {
	private ProgressDialogUtil progress;
	private boolean isShow;
	public MyAsyncHttpResponseHandler(Context context, boolean isShow) {
		this.isShow = isShow;
		this.progress = new ProgressDialogUtil(context);
	}
	@Override
	public void onFinish() {
		super.onFinish();
		if (isShow) {
			progress.dismiss();
		}
	}
	@Override
	public void onStart() {
		super.onStart();
		if (isShow) {
			progress.show();
		}
	}

    @Override
    public void onSuccess(String s) {
        Log.i("result","返回:"+s);

    }

    @Override
    public void onFailure(Throwable error, String content) {
        super.onFailure(error, content);
        Toast.makeText(UIApplication.getInstance().getApplicationContext(),"亲，您的网络不给力！",Toast.LENGTH_SHORT).show();

    }
}
