package com.yshow.shike.activities;

import android.widget.Toast;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.yshow.shike.R;
import com.yshow.shike.UIApplication;
import com.yshow.shike.entity.LoginManage;
import com.yshow.shike.entity.SKStudent;
import com.yshow.shike.push.PushUtil;
import com.yshow.shike.utils.*;

import android.os.Bundle;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;

public class StartingUp extends BaseActivity {
    private ImageView view;
    private FileService fileService;
    private boolean isAutoLogin = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.welcome);
        fileService = new FileService(this);
        isAutoLogin = SharePreferenceUtil.getInstance().getBoolean("autologin");
        view = (ImageView) findViewById(R.id.image_view);
        Start_Anim();
        PushManager.startWork(getApplicationContext(),
                PushConstants.LOGIN_TYPE_API_KEY,
                PushUtil.getMetaValue(StartingUp.this, "api_key"));
    }

    /**
     * 检查登录用户是否已登录过
     */
    private void User_Login() {
        if (isAutoLogin) {//正式用户,自动登陆
            String name = fileService.getSp_Date("autologin_name");
            if (!name.equals("")) {
                String pass = fileService.getSp_Date("autologin_pass");
                autoLogin(name, pass);
            } else {
                Dialog.Intent(StartingUp.this, Login_Reg_Activity.class);
                finish();
            }
        } else {//用户上次使用的立即提问
            boolean hasShowSlide = SharePreferenceUtil.getInstance().getBoolean("need_slide");
            if(hasShowSlide){
                Dialog.Intent(StartingUp.this, Login_Reg_Activity.class);
                finish();
            }else{
                Dialog.Intent(StartingUp.this, StartHelpActivity.class);
                finish();
            }


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
        SKAsyncApiController.skLogin(name, pass, new MyAsyncHttpResponseHandler(this,false) {
            @Override
            public void onSuccess(String json) {
                super.onSuccess(json);
                boolean Login_Success = SKResolveJsonUtil.getInstance().resolveIsSuccess(json, StartingUp.this);
                if (Login_Success) {
                    LoginManage instance = LoginManage.getInstance();
                    instance.setmLoginSuccess(true);
                    SKStudent student = SKResolveJsonUtil.getInstance().resolveLoginInfo(json);
                    instance.setStudent(student);
                    fileService.putBoolean("is_tea", student.getTypes().equals("1"));
                    if (instance.isTeacher()) {
                        // 是老师就登陆老师界面
                        Dialog.Intent(StartingUp.this, Teather_Main_Activity.class);
                    } else {
                        // 学生登录
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

}
