package com.yshow.shike.activities;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import com.umeng.analytics.MobclickAgent;
import com.yshow.shike.R;
import com.yshow.shike.entity.SKStudent;
import com.yshow.shike.utils.Dialog;
import com.yshow.shike.utils.MyAsyncHttpResponseHandler;
import com.yshow.shike.utils.SKAsyncApiController;
import com.yshow.shike.utils.SKResolveJsonUtil;
import com.yshow.shike.utils.Timer_Uils;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
public class TeacherRegisterActivity extends Activity implements
		OnClickListener {
//	private TeacherRegisterActivity mInsetance;
	private SKStudent skStudent;
	private EditText login_teacher_phone; // 手机号
	private EditText et_verification_code; // 验证码
	private Context context;
	private TextView tv_send_pas; // 发送验证码
	private int time_record;
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				Bundle data = msg.getData();
				time_record = data.getInt("time_record");
				tv_send_pas.setText(""+time_record);
				break;
			case 1:
				tv_send_pas.setText("发送验证码");
				tv_send_pas.setOnClickListener(TeacherRegisterActivity.this);
				break;
			}
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		setContentView(R.layout.sk_teacher_register);
		initData();
	}
	private void initData() {
//		mInsetance = this;
		skStudent = new SKStudent();
		login_teacher_phone = (EditText) findViewById(R.id.login_teacher_phone);
		et_verification_code = (EditText) findViewById(R.id.et_verification_code);
		TextView tv_back = (TextView) findViewById(R.id.tv_back);
		tv_back.setOnClickListener(this);
		tv_back.setText("返回");
		findViewById(R.id.tv_comfig).setOnClickListener(this);
		tv_send_pas = (TextView) findViewById(R.id.tv_send_pas);
		tv_send_pas.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		String phonename = login_teacher_phone.getText().toString().trim();
		switch (v.getId()) {
		case R.id.tv_back:
			tv_send_pas.setText("请输入验证码");
			finish();
			break;
		case R.id.tv_comfig:
			if (checkRegister() && (phonename != null && phonename.length() == 11)) {
				skStudent = new SKStudent();
				skStudent.setMob(login_teacher_phone.getText().toString().trim());
				skStudent.setVcodeRes(et_verification_code.getText().toString().trim());
				skStudent.setTypes("1");
				Bundle bundle = new Bundle();
				bundle.putSerializable("teather", skStudent);
				Dialog.intent(this, Activity_Teather_Material.class, bundle);
			}else {
				Toast.makeText(this, "请输入正确的手机号!", 0).show();
			}
			break;
		case R.id.tv_send_pas:
			if (TextUtils.isEmpty(phonename) || phonename.length() != 11) {
				Toast.makeText(this, "请输入正确的手机号!", 0).show();
			} else {
				sendVcode(phonename);
				new Timer_Uils().getTimer(handler); 
				tv_send_pas.setOnClickListener(null);
			}
			break;
		}
	}
	private void sendVcode(final String mob) {
		SKAsyncApiController.skGetVcode(mob, new MyAsyncHttpResponseHandler(
				this, true) {
			@Override
			public void onSuccess(String json) {
				super.onSuccess(json);
				SKResolveJsonUtil.getInstance().resolveIsSuccess(json,context);
			}
		});
	}
	// 用户名和密码判断
	public boolean checkRegister() {
		String username = login_teacher_phone.getText().toString().trim();
		String pasword = et_verification_code.getText().toString().trim();
		if (TextUtils.isEmpty(username)) {
			Toast.makeText(this, "用户名不能为空!", 0).show();
			return false;
		}
		if (TextUtils.isEmpty(pasword)) {
			Toast.makeText(this, "验证码不能为空!", 0).show();
			return false;
		}
		return true;
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (getCurrentFocus() != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(getCurrentFocus().getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
        return true;
    }
}
