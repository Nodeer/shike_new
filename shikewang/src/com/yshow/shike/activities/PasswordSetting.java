package com.yshow.shike.activities;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.umeng.analytics.MobclickAgent;
import com.yshow.shike.R;
import com.yshow.shike.UIApplication;
import com.yshow.shike.entity.LoginManage;
import com.yshow.shike.entity.SKStudent;
import com.yshow.shike.utils.Dialog;
import com.yshow.shike.utils.FileService;
import com.yshow.shike.utils.MyAsyncHttpResponseHandler;
import com.yshow.shike.utils.SKAsyncApiController;
import com.yshow.shike.utils.SKResolveJsonUtil;
public class PasswordSetting extends Activity implements OnClickListener {
	private EditText eT_PhoneName; // 第一次输入密码
	private EditText ET_PhoneName_agin;// 第二次输入密码
	private SKStudent sKStudent;
	private TextView tv_comfig;
	private ImageView yindao;
	private FileService fileService;
	private Context context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_pass_set);
		context = this;
		initData();
		fileService = new FileService(this);
		sKStudent = (SKStudent) getIntent().getExtras().getSerializable("student");
		if (sKStudent == null) {
			finish();
		}else {
			tv_comfig.setText("完成");
		}
	}
	private void initData() {
		eT_PhoneName = (EditText) findViewById(R.id.ET_PhoneName);
		ET_PhoneName_agin = (EditText) findViewById(R.id.ET_PhoneName_agin);
		findViewById(R.id.tv_back).setOnClickListener(this);
		tv_comfig = (TextView) findViewById(R.id.tv_comfig);
		yindao = (ImageView) findViewById(R.id.iv_yindao);
		tv_comfig.setOnClickListener(this);
		yindao.setOnClickListener(this);
	}
	private boolean checkRegister() {
		String paw = eT_PhoneName.getText().toString().trim();
		String aginpaw = ET_PhoneName_agin.getText().toString().trim();
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
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_back:
			finish();
			break;
		case R.id.tv_comfig:
			user_register();
			break;
		case R.id.iv_yindao:
			Dialog.Intent(PasswordSetting.this,Login_Reg_Activity.class);
			break;
		}
	}
	private void user_register() {
		if (checkRegister()) {
			if(fileService.getSp_Date("auto_user").equals("")){
				skRegister();
			}else {
				SKAsyncApiController.Auto_Save_Info(sKStudent,new AsyncHttpResponseHandler(){
					@Override
					public void onSuccess(String json) {
						super.onSuccess(json);
						Toast.makeText(PasswordSetting.this,"自动登录", 0).show();
						auto_Login(sKStudent.getMob(),sKStudent.getPwd());
					}
				});
			}
		}
	}
    //学生正常注册走的流程
	private void skRegister() {
		SKAsyncApiController.skRegister(sKStudent,
				new MyAsyncHttpResponseHandler(this, true) {
					@Override
					public void onSuccess(int arg0, String arg1) {
						boolean success = SKResolveJsonUtil.getInstance().resolveIsSuccess(arg1,PasswordSetting.this);
						if(success){
							SKAsyncApiController.skLogin(sKStudent.getMob(), sKStudent.getPwd(), new AsyncHttpResponseHandler(){
								@Override
								public void onSuccess(String json) {
									super.onSuccess(json);
									boolean Login_Success = SKResolveJsonUtil.getInstance().resolveIsSuccess(json, PasswordSetting.this);
									if(Login_Success){
										seting_student_info(json);
									}
								}

							});
						}
					}
				});
	}
	private void seting_student_info(String json) {
		LoginManage instance = LoginManage.getInstance();
		instance.setmLoginSuccess(true);
		SKStudent student = SKResolveJsonUtil.getInstance().resolveLoginInfo(json);
		instance.setStudent(student);
		Dialog.Intent(PasswordSetting.this,Student_Main_Activity.class);
		UIApplication.getInstance().setAuid_flag(true);
	}
	/**
	 * 登录成功后记住密码
	 */
	private void auto_Login(final String user_name,final String pass_word) {
		SKAsyncApiController.skLogin(user_name, pass_word,
				new MyAsyncHttpResponseHandler(context,true) {
					@Override
					public void onSuccess(String json) {
						super.onSuccess(json);
						boolean success = SKResolveJsonUtil.getInstance().resolveIsSuccess(json, context);
						if (success) {
							LoginManage instance = LoginManage.getInstance();
							instance.setmLoginSuccess(true);
							SKStudent student = SKResolveJsonUtil.getInstance().resolveLoginInfo(json);
							instance.setStudent(student);
							Bundle bundle = new Bundle();
							bundle.putString("!reg_user", student.getName());
						    Dialog.intent(context,Student_Main_Activity.class, bundle);
						    UIApplication.getInstance().setAuid_flag(true);
						    fileService.set_auto_info(null, "");
							finish();
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