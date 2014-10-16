package com.yshow.shike.activities;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;
import com.umeng.analytics.MobclickAgent;
import com.yshow.shike.R;
import com.yshow.shike.entity.LoginManage;
import com.yshow.shike.entity.SKStudent;
import com.yshow.shike.utils.Dialog;
import com.yshow.shike.utils.MyAsyncHttpResponseHandler;
import com.yshow.shike.utils.SKAsyncApiController;
import com.yshow.shike.utils.SKResolveJsonUtil;
public class Activity_Teather_Material_Three extends Activity implements OnClickListener {
	private TextView login_name_edit;
	private TextView login_pass_edit;
	private SKStudent sKStudent;
	private TextView tv_comfig;
	@Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_teather_material_three);
    	initData();
    	sKStudent = (SKStudent) getIntent().getSerializableExtra("student");
		if (sKStudent == null) {
			finish();
		}else {
			tv_comfig.setText("完成");
		}
    }
	private void initData() {
		login_name_edit = (TextView) findViewById(R.id.login_name_edit);
		login_pass_edit = (TextView) findViewById(R.id.login_pass_edit);
		findViewById(R.id.tv_back).setOnClickListener(this);
		tv_comfig = (TextView) findViewById(R.id.tv_comfig);
		tv_comfig.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_back:
			finish();
			break;
		case R.id.tv_comfig:
			boolean checkRegister2 = checkRegister();
			if(checkRegister2){
				skRegister();
			}
			break;
		}
	}
	private boolean checkRegister() {
		String paw = login_name_edit.getText().toString().trim();
		String aginpaw = login_pass_edit.getText().toString().trim();
		if (TextUtils.isEmpty(paw) || TextUtils.isEmpty(aginpaw)) {
			Toast.makeText(this, "密码不能为空", 0).show();
			return false;
		}
		if (!paw.equals(aginpaw)) {
			Toast.makeText(this, "两次密码不一样", 0).show();
			return false;
		}
		sKStudent.setPwd(paw);
		return true;
	}
// 老师注册
	private void skRegister() {
		SKAsyncApiController.skRegister_Teather(sKStudent,new MyAsyncHttpResponseHandler(this,true) {
					@Override
					public void onSuccess(String json) {
						super.onSuccess(json);
						boolean success = SKResolveJsonUtil.getInstance().resolveIsSuccess(json, Activity_Teather_Material_Three.this);
						if(success){
							LoginManage instance = LoginManage.getInstance();
							instance.setmLoginSuccess(true);
							SKStudent student = SKResolveJsonUtil.getInstance().resolveLoginInfo(json);
							instance.setStudent(student);
							Dialog.Intent(Activity_Teather_Material_Three.this,Teather_Main_Activity.class);
						}
					}
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (getCurrentFocus() != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(getCurrentFocus().getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
        return true;
    }
}
