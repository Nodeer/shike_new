package com.yshow.shike.activities;

import com.umeng.analytics.MobclickAgent;
import com.yshow.shike.R;
import com.yshow.shike.UIApplication;
import com.yshow.shike.activities.Age_Person_Info.Callback;
import com.yshow.shike.entity.LoginManage;
import com.yshow.shike.entity.SKStudent;
import com.yshow.shike.fragments.UserLoginFragment;
import com.yshow.shike.fragments.UserRegisterFragment;
import com.yshow.shike.utils.Dialog;
import com.yshow.shike.utils.FileService;
import com.yshow.shike.utils.FragmentExchangeController;
import com.yshow.shike.utils.MyAsyncHttpResponseHandler;
import com.yshow.shike.utils.SKAsyncApiController;
import com.yshow.shike.utils.SKResolveJsonUtil;
import com.yshow.shike.utils.ScreenSizeUtil;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.content.Context;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

/**
 * 注册,登陆页面
 */
public class Login_Reg_Activity extends BaseActivity implements OnClickListener {
    private TextView mRegButton;
    private ImageView mSlideTitle;
    private Button mLoginButton;

    private CheckBox isRemember;
    private AutoCompleteTextView username_edit;
    private EditText password_edit;
    private String username;
    private String password;
    private TextView tv_pasword;
    private FileService fileService; //实例化业务对象
    private ArrayAdapter<String> adapter;
    private boolean checked = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_reg_activity);
        mRegButton = (TextView) findViewById(R.id.button_reg);
        mRegButton.setOnClickListener(this);
        mSlideTitle = (ImageView) findViewById(R.id.title_slide);
        mSlideTitle.setOnClickListener(this);
        mLoginButton = (Button) findViewById(R.id.login_button);
        mLoginButton.setOnClickListener(this);
//        ft = manager.beginTransaction();
//        ft.replace(R.id.content, new UserRegisterFragment());
//        ft.commit();

        fileService = new FileService(this);
        username_edit = (AutoCompleteTextView) findViewById(R.id.login_name_auto_edit);
        String name = fileService.getSp_Date("name");
        if (!TextUtils.isEmpty(name)) {
            username_edit.setText(name);
        }
        password_edit = (EditText) findViewById(R.id.login_pwd_edit);
        String password = fileService.getSp_Date("pass");
        if (!TextUtils.isEmpty(password)) {
            password_edit.setText(password);
        }
        isRemember = (CheckBox) findViewById(R.id.isremember);
        tv_pasword = (TextView) findViewById(R.id.tv_pasword);
        // 用户名回显
        SharedPreferences sp = getSharedPreferences("shared_prefs", Context.MODE_WORLD_READABLE);
        String str_name = sp.getString("savename", "");
        String[] auto_array = str_name.split(",");
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, auto_array);
        username_edit.setAdapter(adapter);
        isRemember.setOnClickListener(this);
        tv_pasword.setOnClickListener(this);

        if(fileService.getBoolean("isfirstuse",true)){
            FragmentExchangeController.addFragment(getSupportFragmentManager(), new UserRegisterFragment(), Window.ID_ANDROID_CONTENT, "reg", R.anim.dialog_enter_from_top, R.anim.dialog_exit_to_top,R.anim.dialog_enter_from_top,R.anim.dialog_exit_to_top);
            fileService.putBoolean("isfirstuse",false);
        }
    }

    //点击屏幕 关闭输入弹出框
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (getCurrentFocus() != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(getCurrentFocus().getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
        return true;
    }

    // 用户名和密码判断  修改
    public boolean checkRegister() {
        username = username_edit.getText().toString().trim();
        password = password_edit.getText().toString().trim();
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            return false;
        }
        fileService.saveAddString("savename", username);
        return true;
    }

    /**
     * 登录成功后记住密码
     */
    private void skLogin() {
        SKAsyncApiController.skLogin(username, password,
                new MyAsyncHttpResponseHandler(Login_Reg_Activity.this, true) {
                    @Override
                    public void onSuccess(String json) {
                        super.onSuccess(json);
                        boolean success = SKResolveJsonUtil.getInstance().resolveIsSuccess(json, Login_Reg_Activity.this);
                        if (success) {
                            LoginManage instance = LoginManage.getInstance();
                            instance.setmLoginSuccess(true);
                            SKStudent student = SKResolveJsonUtil.getInstance().resolveLoginInfo(json);
                            instance.setStudent(student);
                            fileService.putBoolean("is_tea", student.getTypes().equals("1"));
                            Bundle bundle = new Bundle();
                            //如果不是老师
                            if (!LoginManage.getInstance().isTeacher(Login_Reg_Activity.this)) {
                                //是学生
                                UIApplication.getInstance().setAuid_flag(true);
                                Dialog.Intent(Login_Reg_Activity.this, Student_Main_Activity.class);
                            } else {
                                //是老师
                                Dialog.Intent(Login_Reg_Activity.this, Teather_Main_Activity.class);
                            }
                            if (checked) {
                                fileService.saveString("name", username);
                                fileService.saveString("pass", password);
                            }
                            fileService.saveString("autologin_name", username);
                            fileService.saveString("autologin_pass", password);
                            Login_Reg_Activity.this.finish();
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_reg:
            case R.id.title_slide:
                FragmentExchangeController.addFragment(getSupportFragmentManager(), new UserRegisterFragment(), Window.ID_ANDROID_CONTENT, "reg", R.anim.dialog_enter_from_top, R.anim.dialog_exit_to_top,R.anim.dialog_enter_from_top,R.anim.dialog_exit_to_top);
                break;
            case R.id.login_button:
                if (checkRegister()) {
                    skLogin();
                }
                break;
            case R.id.tv_pasword:
                Dialog.Intent(Login_Reg_Activity.this, Forget_Pass_Activity.class);
            case R.id.isremember:
                checked = isRemember.isChecked();
                break;
        }
    }
}
