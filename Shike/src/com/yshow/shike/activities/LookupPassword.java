package com.yshow.shike.activities;
import com.umeng.analytics.MobclickAgent;
import com.yshow.shike.R;
import com.yshow.shike.utils.Dialog;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
 /**
  * 忘记密码
  * @author Administrator
  */
public class LookupPassword extends Activity implements OnClickListener{
    private EditText number;
	private EditText et_verify;
	private TextView tv_verify;
	private String phonName;
	private String verifyName;
	@Override
	public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
    	setContentView(R.layout.activity_pasword);  
    	number = (EditText) findViewById(R.id.ET_PhoneName);
    	et_verify = (EditText) findViewById(R.id.et_verify_edit);
    	tv_verify = (TextView) findViewById(R.id.tv_verify);  
    	tv_verify.setOnClickListener(this);
    }
	//用户名和密码判断
	public boolean checkRegister(){
		phonName = number.getText().toString().trim();
		verifyName = et_verify.getText().toString().trim();
		if(TextUtils.isEmpty(phonName)){
			Dialog.DeleteCache(this);
		}
		if(TextUtils.isEmpty(verifyName)){
			Toast.makeText(this, "请获取验证码", 0).show();
		}
		return true;
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_verify:
			if(checkRegister()){
				
				Toast.makeText(this, "获取手机号(phonName)发送联网发送给服务端", 0).show();
			}
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
