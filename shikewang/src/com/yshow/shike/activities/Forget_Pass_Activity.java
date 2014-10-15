package com.yshow.shike.activities;

import com.umeng.analytics.MobclickAgent;
import com.yshow.shike.R;
import com.yshow.shike.utils.MyAsyncHttpResponseHandler;
import com.yshow.shike.utils.SKAsyncApiController;
import com.yshow.shike.utils.SKResolveJsonUtil;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Forget_Pass_Activity extends Activity implements OnClickListener {
	/** 手机号输入框 */
	private EditText ed_phone_name;// 获取手机号,验证码编辑框 控件
	/** 验证码输入框 */
	private EditText ver_name;// 获取手机号,验证码编辑框 控件
	private Context context;
	/** 发送验证码按钮 */
	private TextView send_pass;// 发送控件
	private int count;// 点击发送的数量统计
	private String phone;// 获取手机号

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.forget_pass_activity);
		context = this;
		initData();
	}

	private void initData() {
		findViewById(R.id.tv_back).setOnClickListener(this);
		findViewById(R.id.tv_pasword).setOnClickListener(this);
		ed_phone_name = (EditText) findViewById(R.id.phn_name);
		send_pass = (TextView) findViewById(R.id.send_pass);
		ver_name = (EditText) findViewById(R.id.ver_name);
		send_pass.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.tv_back:
			finish();
			break;
		case R.id.tv_pasword:
			phone = ed_phone_name.getText().toString().trim();
			if (phone.length() != 11) {
				Toast.makeText(context, "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
			} else {
				Get_Code(phone);
			}
			break;
		case R.id.send_pass:
			if (count >= 3) {
				Toast.makeText(context, "超过三次了,请明天再试", Toast.LENGTH_SHORT).show();
				return;
			}
			count++;
			if (checkPhoneAndCode()) {
				Send_Ver(phone);
			} else {
				Toast.makeText(context, "请输入手机号和密码", Toast.LENGTH_SHORT).show();
			}
			break;
		}
	}

	private boolean checkPhoneAndCode() {
		if (phone == null) {
			return false;
		}
		if (phone.length() != 11) {
			return false;
		}
		String name = ver_name.getText().toString().trim();
		if (name.length() == 0) {
			return false;
		}
		return true;
	}

	private void Send_Ver(String phone) {
		String name = ver_name.getText().toString().trim();
		Pw_Re(phone, name);
	}

	// 获取验证码
	private void Get_Code(final String mob) {
		SKAsyncApiController.getPassword(mob, new MyAsyncHttpResponseHandler(this, true) {
			@Override
			public void onSuccess(String json) {
				super.onSuccess(json);
				boolean success = SKResolveJsonUtil.getInstance().resolveIsSuccess(json);
				if (success) {
					Toast.makeText(context, "请稍候", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	/**
	 * 密码重做
	 * 
	 * @param mob
	 */
	private void Pw_Re(final String mob, String vcodeRes) {
		SKAsyncApiController.Reset_Password(mob, vcodeRes, new MyAsyncHttpResponseHandler(this, true) {
			public void onSuccess(String json) {
				boolean success = SKResolveJsonUtil.getInstance().resolveIsSuccess(json, context);
				if (success) {
					Toast.makeText(context, "我们将用短信的方式通知你", Toast.LENGTH_SHORT).show();
				}
			};
		});
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
