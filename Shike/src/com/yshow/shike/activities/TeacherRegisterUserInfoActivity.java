package com.yshow.shike.activities;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;
import com.yshow.shike.R;
import com.yshow.shike.entity.LoginManage;
import com.yshow.shike.entity.SKArea;
import com.yshow.shike.entity.SKGrade;
import com.yshow.shike.entity.SKStudent;
import com.yshow.shike.utils.AreaSeltorUtil;
import com.yshow.shike.utils.AreaSeltorUtil.AreaSeltorUtilButtonOnclickListening;
import com.yshow.shike.utils.Dialog;
import com.yshow.shike.utils.Grade_Level_Utils;
import com.yshow.shike.utils.Grade_Level_Utils.GradeSeltorUtilButtonOnclickListening;
import com.yshow.shike.utils.MyAsyncHttpResponseHandler;
import com.yshow.shike.utils.SKAsyncApiController;
import com.yshow.shike.utils.SKResolveJsonUtil;

public class TeacherRegisterUserInfoActivity extends BaseActivity implements
        OnClickListener {
    private EditText nickname_edit; // 昵称
    private SKStudent sKStudent;
    private String xuekeString, diquString, jieduanString; // 学科  地区 阶段

    private LinearLayout mXueLingDuanBtn, mXukeBtn, mDiQuBtn;
    private TextView xuelingduan, xueke, diqu;
    private EditText passwordEditText, passwordConfirmEdittext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_register_userinfo_layout);
        initData();
    }

    private void initData() {
        TextView titleText = (TextView) findViewById(R.id.title_text);
        titleText.setText("填写资料");

        nickname_edit = (EditText) findViewById(R.id.login_name_edit);
        findViewById(R.id.left_btn).setOnClickListener(this);
        findViewById(R.id.next_btn).setOnClickListener(this);

        mXueLingDuanBtn = (LinearLayout) findViewById(R.id.xuelingduan_btn);
        mXueLingDuanBtn.setOnClickListener(this);
        mXukeBtn = (LinearLayout) findViewById(R.id.xueke_btn);
        mXukeBtn.setOnClickListener(this);
        mDiQuBtn = (LinearLayout) findViewById(R.id.diqu_btn);
        mDiQuBtn.setOnClickListener(this);

        xuelingduan = (TextView) findViewById(R.id.xuelingduan_text);
        xueke = (TextView) findViewById(R.id.xueke_text);
        diqu = (TextView) findViewById(R.id.diqu_text);
        passwordEditText = (EditText) findViewById(R.id.login_password_edit);
        passwordConfirmEdittext = (EditText) findViewById(R.id.login_password_confirm_edit);


        sKStudent = (SKStudent) getIntent().getExtras().getSerializable("teather");
        findViewById(R.id.left_btn).setOnClickListener(this);
        findViewById(R.id.next_btn).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //学科按钮
            case R.id.xueke_btn:
                getSubject();
                break;
            //地区按钮
            case R.id.diqu_btn:
                getArea();
                break;
            //阶段按钮
            case R.id.xuelingduan_btn:
                getJieDuan();
                break;
            //下一步按钮
            case R.id.left_btn:
                finish();
                break;
            case R.id.next_btn:
                xuekeString = xueke.getText().toString().trim();
                diquString = diqu.getText().toString().trim();
                jieduanString = xuelingduan.getText().toString().trim();
                if (checkRegister()) {
                    skRegister();
                }
                break;
        }
    }

    private void skRegister() {
        SKAsyncApiController.skRegister_Teather(sKStudent, new MyAsyncHttpResponseHandler(this, true) {
            @Override
            public void onSuccess(String json) {
                super.onSuccess(json);
                boolean success = SKResolveJsonUtil.getInstance().resolveIsSuccess(json, TeacherRegisterUserInfoActivity.this);
                if (success) {
                    LoginManage instance = LoginManage.getInstance();
                    instance.setmLoginSuccess(true);
                    SKStudent student = SKResolveJsonUtil.getInstance().resolveLoginInfo(json);
                    instance.setStudent(student);
                    Dialog.Intent(TeacherRegisterUserInfoActivity.this, Teather_Main_Activity.class);
                }
            }
        });
    }

    // 获取地区
    private void getArea() {
        SKAsyncApiController.skGetArea(new MyAsyncHttpResponseHandler(this, true) {
            @Override
            public void onSuccess(String arg0) {
                super.onSuccess(arg0);
                Log.e("getArea", arg0);
                ArrayList<SKArea> resolveArea = SKResolveJsonUtil.getInstance().resolveArea(arg0);
                final AreaSeltorUtil systemDialog = new AreaSeltorUtil(TeacherRegisterUserInfoActivity.this, resolveArea);
                systemDialog.setLeftButtonText("完成");
                systemDialog.show();
                systemDialog.setAreaSeltorUtilButtonOnclickListening(new AreaSeltorUtilButtonOnclickListening() {
                    @Override
                    public void onClickRight() {
                    }

                    @Override
                    public void onClickLeft() {
                        sKStudent.setaId(systemDialog.getGradeId());
                        diqu.setText(systemDialog.getSeltotText());
                    }
                });
            }
        });
    }

    // 获取学科
    private void getSubject() {
        SKAsyncApiController.skGetSubjeck(new MyAsyncHttpResponseHandler(this, true) {
            @Override
            public void onSuccess(final int arg0, final String arg1) {
                super.onSuccess(arg0, arg1);
                ArrayList<SKArea> paseSubject = SKResolveJsonUtil.getInstance().PaseSubject(arg1);
                final AreaSeltorUtil subjectId = new AreaSeltorUtil(TeacherRegisterUserInfoActivity.this, paseSubject);
                subjectId.setLeftButtonText("完成");
                subjectId.show();
                subjectId.setAreaSeltorUtilButtonOnclickListening(new AreaSeltorUtilButtonOnclickListening() {
                    @Override
                    public void onClickRight() {
                    }

                    @Override
                    public void onClickLeft() {
                        String gradeId = subjectId.getGradeId();
                        sKStudent.setSubject(gradeId);
                        xueke.setText(subjectId.getSeltotText());
                    }
                });
            }
        });
    }

    // 获取阶段
    private void getJieDuan() {
        SKAsyncApiController.skGetGrade(new MyAsyncHttpResponseHandler(this, true) {
            @Override
            public void onSuccess(final int arg0, final String arg1) {
                super.onSuccess(arg0, arg1);
                ArrayList<SKGrade> SKGrades = SKResolveJsonUtil.getInstance().resolveGrade(arg1);
                final Grade_Level_Utils grade_utils = new Grade_Level_Utils(TeacherRegisterUserInfoActivity.this, SKGrades);
                grade_utils.setLeftButtonText("完成");
                grade_utils.setGradeSeltorUtilButtonOnclickListening(new GradeSeltorUtilButtonOnclickListening() {
                    @Override
                    public void onClickRight() {
                    }

                    @Override
                    public void onClickLeft() {
                        String seltotText = grade_utils.getSeltotText();
                        xuelingduan.setText(seltotText);
                        sKStudent.setGradeId(grade_utils.getGradeId());
                        sKStudent.setFromGradeId(grade_utils.getFormGradeId());
                        sKStudent.setToGradeId(grade_utils.getToGradeId());
                    }
                });
                grade_utils.show();
            }
        });
    }

    public boolean checkRegister() {
        if (checkPassword()) {
            String username = nickname_edit.getText().toString().trim();
            if (TextUtils.isEmpty(username)) {
                Toast.makeText(this, "您还没有填写信姓名!", Toast.LENGTH_SHORT).show();
                return false;
            } else if (TextUtils.isEmpty(xuekeString)) {
                Toast.makeText(this, "您还没有填写学科!", Toast.LENGTH_SHORT).show();
                return false;
            } else if (TextUtils.isEmpty(diquString)) {
                Toast.makeText(this, "您还没有填写地区呢!", Toast.LENGTH_SHORT).show();
                return false;
            } else if (TextUtils.isEmpty(jieduanString)) {
                Toast.makeText(this, "您还没有填写阶段呢!", Toast.LENGTH_SHORT).show();
                return false;
            } else {
                sKStudent.setNickname(username);
                return true;
            }
        } else {
            return false;
        }

    }

    private boolean checkPassword() {
        String paw = passwordEditText.getText().toString().trim();
        String aginpaw = passwordConfirmEdittext.getText().toString().trim();
        if (TextUtils.isEmpty(paw) || TextUtils.isEmpty(aginpaw)) {
            Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!paw.equals(aginpaw)) {
            Toast.makeText(this, "两次密码不一样", Toast.LENGTH_SHORT).show();
            return false;
        }
        sKStudent.setPwd(paw);
        return true;
    }

}