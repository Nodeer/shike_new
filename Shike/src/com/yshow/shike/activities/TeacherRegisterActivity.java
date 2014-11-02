package com.yshow.shike.activities;

import android.content.Intent;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;

import com.yshow.shike.R;
import com.yshow.shike.entity.SKStudent;
import com.yshow.shike.utils.Dialog;
import com.yshow.shike.utils.MyAsyncHttpResponseHandler;
import com.yshow.shike.utils.SKAsyncApiController;
import com.yshow.shike.utils.SKResolveJsonUtil;
import com.yshow.shike.utils.Timer_Uils;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class TeacherRegisterActivity extends BaseActivity implements
        OnClickListener {
    private SKStudent skStudent;
    private EditText login_teacher_phone; // 手机号
    private EditText et_verification_code; // 验证码
    private Context context;
    private TextView tv_send_pas; // 发送验证码
    private int time_record;
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    Bundle data = msg.getData();
                    time_record = data.getInt("time_record");
                    tv_send_pas.setText("" + time_record);
                    break;
                case 1:
                    tv_send_pas.setText("发送验证码");
                    tv_send_pas.setOnClickListener(TeacherRegisterActivity.this);
                    break;
            }
        }
    };

    private EditText extra_code;

    private CheckBox agrementBox;
    private TextView agrementBtn;
    private boolean isAgrement = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.sk_teacher_register);
        initData();
    }

    private void initData() {
        skStudent = new SKStudent();
        login_teacher_phone = (EditText) findViewById(R.id.login_name_edit);
        et_verification_code = (EditText) findViewById(R.id.login_pwd_edit);


        TextView titleText = (TextView) findViewById(R.id.title_text);
        titleText.setText("老师注册");


        agrementBox = (CheckBox) findViewById(R.id.agrement_checkbox);
        agrementBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isAgrement = isChecked;
            }
        });
        agrementBtn = (TextView) findViewById(R.id.agrement_text);
        agrementBtn.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        agrementBtn.setOnClickListener(this);

        ImageView tv_back = (ImageView) findViewById(R.id.left_btn);
        tv_back.setOnClickListener(this);
        findViewById(R.id.next_btn).setOnClickListener(this);
        findViewById(R.id.left_btn).setOnClickListener(this);

        extra_code = (EditText) findViewById(R.id.extra_code);

        tv_send_pas = (TextView) findViewById(R.id.tv_pasword);
        tv_send_pas.setOnClickListener(this);
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
                    skStudent.setMob(login_teacher_phone.getText().toString().trim());
                    skStudent.setVcodeRes(et_verification_code.getText().toString().trim());
                    skStudent.setTypes("1");
                    skStudent.reference = extra_code.getText().toString();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("teather", skStudent);
                    Dialog.intent(this, TeacherRegisterUploadPapersActivity.class, bundle);
                }
                break;
            case R.id.tv_pasword:
                String phonename = login_teacher_phone.getText().toString().trim();
                if (TextUtils.isEmpty(phonename) || phonename.length() != 11) {
                    Toast.makeText(this, "请输入正确的手机号!", 0).show();
                } else {
                    sendVcode(phonename);
                    new Timer_Uils().getTimer(handler);
                    tv_send_pas.setOnClickListener(null);
                }
                break;
            case R.id.agrement_text:
                Intent it = new Intent(TeacherRegisterActivity.this, WebViewActivity.class);
                it.putExtra("url", SKAsyncApiController.SHIKE_VALUE_API_SERVER_URL + "/?m=content&a=agreement");
                startActivity(it);
                break;
        }
    }

    private void sendVcode(final String mob) {
        SKAsyncApiController.skGetVcode(mob, new MyAsyncHttpResponseHandler(
                this, true) {
            @Override
            public void onSuccess(String json) {
                super.onSuccess(json);
                boolean success = SKResolveJsonUtil.getInstance().resolveIsSuccess(json, TeacherRegisterActivity.this);
                if (success) {
                    Toast.makeText(TeacherRegisterActivity.this, "验证码已发送", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // 用户名和密码判断
    public boolean checkRegister() {
        String username = login_teacher_phone.getText().toString().trim();
        String pasword = et_verification_code.getText().toString().trim();
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
    public boolean onTouchEvent(MotionEvent event) {
        if (getCurrentFocus() != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(getCurrentFocus().getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
        return true;
    }
}
