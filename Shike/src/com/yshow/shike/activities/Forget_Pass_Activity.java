package com.yshow.shike.activities;

import com.umeng.analytics.MobclickAgent;
import com.yshow.shike.R;
import com.yshow.shike.utils.MyAsyncHttpResponseHandler;
import com.yshow.shike.utils.SKAsyncApiController;
import com.yshow.shike.utils.SKResolveJsonUtil;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 忘记密码的页面
 */
public class Forget_Pass_Activity extends BaseActivity implements OnClickListener {
    /**
     * 手机号输入框
     */
    private EditText ed_phone_name;// 获取手机号,验证码编辑框 控件
    /**
     * 验证码输入框
     */
    private EditText ver_name;// 获取手机号,验证码编辑框 控件
    private Context context;
    /**
     * 发送验证码按钮
     */
    private TextView send_pass;// 发送控件
    private int count;// 点击发送的数量统计
    private String phone;// 获取手机号
    MyCountDownTimer timer;

    private TextView sendCodeBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget_pass_activity);
        context = this;
        initData();
        timer = new MyCountDownTimer(120000, 1000);
    }


    private class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            sendCodeBtn.setText(String.valueOf(millisUntilFinished / 1000));
        }

        @Override
        public void onFinish() {
            sendCodeBtn.setText("发送验证码");
            sendCodeBtn.setEnabled(true);
        }
    }

    private void initData() {

        TextView titletext = (TextView) findViewById(R.id.title_text);
        titletext.setText("忘记密码");

        findViewById(R.id.left_btn).setOnClickListener(this);


        sendCodeBtn = (TextView) findViewById(R.id.send_code_btn);
        sendCodeBtn.setOnClickListener(this);
        ed_phone_name = (EditText) findViewById(R.id.phn_name);
        send_pass = (TextView) findViewById(R.id.confirm_btn);
        ver_name = (EditText) findViewById(R.id.ver_name);
        send_pass.setOnClickListener(this);
    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.left_btn:
                finish();
                break;
            case R.id.send_code_btn://发送验证码
                phone = ed_phone_name.getText().toString().trim();
                if (phone.length() != 11) {
                    Toast.makeText(context, "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
                } else {
                    Get_Code(phone);
                }
                break;
            case R.id.confirm_btn:
                if (count >= 3) {
                    Toast.makeText(context, "超过三次了,请明天再试", Toast.LENGTH_SHORT).show();
                    return;
                }
                count++;
                if (checkPhoneAndCode()) {
                    Send_Ver(phone);
                } else {
                    Toast.makeText(context, "请输入手机号和验证码", Toast.LENGTH_SHORT).show();
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
        timer.start();
        sendCodeBtn.setEnabled(false);
        SKAsyncApiController.getPassword(mob, new MyAsyncHttpResponseHandler(this, true) {
            @Override
            public void onSuccess(String json) {
                super.onSuccess(json);
                boolean success = SKResolveJsonUtil.getInstance().resolveIsSuccess(json);
                if (success) {
                    Toast.makeText(context, "验证码已发送", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(context, "您的新密码于短信形式发给您.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
