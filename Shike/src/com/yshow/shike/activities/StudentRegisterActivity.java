package com.yshow.shike.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yshow.shike.R;
import com.yshow.shike.entity.SKStudent;
import com.yshow.shike.utils.Dialog;
import com.yshow.shike.utils.MyAsyncHttpResponseHandler;
import com.yshow.shike.utils.SKAsyncApiController;
import com.yshow.shike.utils.SKResolveJsonUtil;
import com.yshow.shike.utils.Timer_Uils;

/**
 * 学生注册——手机验证
 *
 * @author
 */
public class StudentRegisterActivity extends BaseActivity implements
        OnClickListener {
    private StudentRegisterActivity mInsetance;
    private EditText login_name_edit;
    private EditText login_pwd_edit;//验证码输入框.
    private TextView sendVcode;
    private ImageView tv_back;
    private SKStudent skStudent;
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    Bundle data = msg.getData();
                    int time_record = data.getInt("time_record");
                    sendVcode.setText("" + time_record);
                    break;
                case 1:
                    sendVcode.setText("发送验证码");
                    sendVcode.setOnClickListener(StudentRegisterActivity.this);
                    break;
            }
        }
    };

    private CheckBox agrementBox;
    private TextView agrementBtn;
    private boolean isAgrement = true;

    private EditText extra_code;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_register_layout);
        mInsetance = this;
        initdata();
    }

    private void initdata() {
        tv_back = (ImageView) findViewById(R.id.left_btn);
        tv_back.setOnClickListener(this);

        TextView titleText = (TextView) findViewById(R.id.title_text);
        titleText.setText("学生注册");

        agrementBox = (CheckBox) findViewById(R.id.agrement_checkbox);
        agrementBox.setChecked(true);
        agrementBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isAgrement = isChecked;
            }
        });
        agrementBtn = (TextView) findViewById(R.id.agrement_text);
        agrementBtn.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        agrementBtn.setOnClickListener(this);

        findViewById(R.id.next_btn).setOnClickListener(this);
        login_name_edit = (EditText) findViewById(R.id.login_name_edit);
        login_pwd_edit = (EditText) findViewById(R.id.login_pwd_edit);
        extra_code = (EditText) findViewById(R.id.extra_code);
        sendVcode = (TextView) findViewById(R.id.tv_pasword);
        sendVcode.setOnClickListener(this);
    }

    // 用户名和密码判断
    public boolean checkRegister() {
        String username = login_name_edit.getText().toString().trim();
        String pasword = login_pwd_edit.getText().toString().trim();
        if (TextUtils.isEmpty(username)) {
            Toast.makeText(this, "手机号不能为空!", 0).show();
            return false;
        }
        if (!(username != null && username.length() == 11)) {
            Toast.makeText(this, "请输入正确的手机号!", 0).show();
            return false;
        }
        if (TextUtils.isEmpty(pasword)) {
            Toast.makeText(this, "验证码不能为空!", 0).show();
            return false;
        }
        if (!isAgrement) {
            Toast.makeText(this, "请确认用户协议!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.left_btn:
                finish();
                break;
            case R.id.next_btn:
                if (checkRegister()) {
                    skStudent = new SKStudent();
                    skStudent.setMob(login_name_edit.getText().toString().trim());
                    skStudent.setVcodeRes(login_pwd_edit.getText().toString().trim());
                    skStudent.setTypes("0");
                    skStudent.reference = extra_code.getText().toString();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("student", skStudent);
                    Dialog.intent(this, StudentRegisterUserinfoActivity.class, bundle);
                }
                break;
            //发送验证码
            case R.id.tv_pasword:
                String username = login_name_edit.getText().toString().trim();
                if (username != null && username.length() == 11) {
                    sendVcode(username);
                    new Timer_Uils().getTimer(handler);
                    sendVcode.setOnClickListener(null);
                } else {
                    Toast.makeText(this, "请正确输入手机号!", 0).show();
                }
                break;
            case R.id.agrement_text:
                Intent it = new Intent(StudentRegisterActivity.this, WebViewActivity.class);
                it.putExtra("url", SKAsyncApiController.SHIKE_VALUE_API_SERVER_URL + "/?m=content&a=agreement");
                startActivity(it);
                break;
        }
    }

    /**
     * 发送验证码
     *
     * @param mob
     */
    private void sendVcode(final String mob) {
        SKAsyncApiController.skGetVcode(mob, new MyAsyncHttpResponseHandler(
                this, false) {

            @Override
            public void onSuccess(String json) {
                super.onSuccess(json);
                boolean success = SKResolveJsonUtil.getInstance().resolveIsSuccess(json, StudentRegisterActivity.this);
                if (success) {
                    Toast.makeText(StudentRegisterActivity.this, "验证码已发送", Toast.LENGTH_SHORT).show();
                    skStudent = new SKStudent();
                    skStudent.setMob(mob);
                    skStudent.setVcodeRes(login_pwd_edit.getText().toString().trim());
                }
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (imm.isActive()) {
                View currentFocus = getCurrentFocus();
                if (currentFocus != null) {
                    imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }

}
