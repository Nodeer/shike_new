package com.yshow.shike.activities;

import java.io.File;

import android.widget.Toast;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.umeng.analytics.MobclickAgent;
import com.yshow.shike.R;
import com.yshow.shike.UIApplication;
import com.yshow.shike.entity.LoginManage;
import com.yshow.shike.entity.SKStudent;
import com.yshow.shike.utils.*;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.app.Activity;

public class StartingUp extends Activity {
	private ImageView view;
	private String save_one_auto; // 判断用户是否是第一次自动登录
	private Auto_Login_User auto_Login; // 自动登录用户如不是第一次就联网登录
	private FileService fileService;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.welcome);
		fileService = new FileService(this);
		save_one_auto = fileService.getSp_Date("auto_user");
		view = (ImageView) findViewById(R.id.image_view);
		auto_Login = new Auto_Login_User(this);
		// User_Login();
		Start_Anim();
	}

	/**
	 * 检查登录用户是否已登录过
	 */
	private void User_Login() {
		if (save_one_auto.equals("")) {
			String name = fileService.getSp_Date("autologin_name");
			if (!name.equals("")) {
				String pass = fileService.getSp_Date("autologin_pass");
				autoLogin(name, pass);
			} else {
				Dialog.Intent(StartingUp.this, Login_Reg_Activity.class);
				finish();
			}
		} else {
			auto_Login.auto_login_info();
		}

	}

	// 开机动画
	private void Start_Anim() {
		AlphaAnimation animation = new AlphaAnimation(0.1f, 1.0f);
		animation.setDuration(1000);
		view.setAnimation(animation);
		animation.start();
		animation.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				User_Login();
			}
		});
	}

	public void autoLogin(String name, String pass) {
		// 登录过的用户
		SKAsyncApiController.skLogin(name, pass, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String json) {
				super.onSuccess(json);
				boolean Login_Success = SKResolveJsonUtil.getInstance().resolveIsSuccess(json, StartingUp.this);
				if (Login_Success) {
					LoginManage instance = LoginManage.getInstance();
					instance.setmLoginSuccess(true);
					SKStudent student = SKResolveJsonUtil.getInstance().resolveLoginInfo(json);
					instance.setStudent(student);
					fileService.putBoolean(StartingUp.this, "is_tea", student.getTypes().equals("1"));
					if (instance.isTeacher()) {
						// 是老师就登陆老师界面
						Dialog.Intent(StartingUp.this, Teather_Main_Activity.class);
					} else {
						// 学生登录
						UIApplication.getInstance().setAuid_flag(true);
						Dialog.Intent(StartingUp.this, Student_Main_Activity.class);
					}
					finish();
				} else {
					Toast.makeText(StartingUp.this, "自动登录失败", Toast.LENGTH_SHORT).show();
					Dialog.Intent(StartingUp.this, Login_Reg_Activity.class);
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
}
