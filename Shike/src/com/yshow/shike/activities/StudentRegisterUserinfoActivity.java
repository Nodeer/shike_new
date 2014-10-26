package com.yshow.shike.activities;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.umeng.analytics.MobclickAgent;
import com.yshow.shike.R;
import com.yshow.shike.UIApplication;
import com.yshow.shike.entity.LoginManage;
import com.yshow.shike.entity.SKArea;
import com.yshow.shike.entity.SKGrade;
import com.yshow.shike.entity.SKStudent;
import com.yshow.shike.entity.SkClasses;
import com.yshow.shike.utils.AreaSeltorUtil;
import com.yshow.shike.utils.Dialog;
import com.yshow.shike.utils.GradeSeltorUtil;
import com.yshow.shike.utils.JieDuanSeltorUtil;
import com.yshow.shike.utils.MyAsyncHttpResponseHandler;
import com.yshow.shike.utils.SKAsyncApiController;
import com.yshow.shike.utils.SKResolveJsonUtil;
import com.yshow.shike.utils.AreaSeltorUtil.AreaSeltorUtilButtonOnclickListening;
import com.yshow.shike.utils.GradeSeltorUtil.SystemDialogButtonOnclickListening;
import com.yshow.shike.utils.XuelingDuanSeltorUtil;

/**
 * 学生注册输入手机号和验证码之后的页面.用户填写用户名,地区,年级
 */
public class StudentRegisterUserinfoActivity extends BaseActivity implements OnClickListener {
    private EditText login_name_edit2; // 手机号 文本框
    private SKStudent sKStudent;
    private TextView xuelingduan, jieduan, diqu;

    private EditText passwordEditText, passwordConfirmEdittext;

    private LinearLayout mXueLingDuanBtn, mJieDuanBtn, mDiQuBtn;

    private ArrayList<SKGrade> mGradeData;

    private int mGradeIndex = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stu_register_userinfo_layout);
        sKStudent = (SKStudent) getIntent().getExtras().getSerializable("student");
        initdata();
    }

    private void initdata() {
        TextView titleText = (TextView) findViewById(R.id.title_text);
        titleText.setText("填写资料");

        login_name_edit2 = (EditText) findViewById(R.id.login_name_edit);
        findViewById(R.id.left_btn).setOnClickListener(this);
        findViewById(R.id.next_btn).setOnClickListener(this);

        mXueLingDuanBtn = (LinearLayout) findViewById(R.id.xuelingduan_btn);
        mXueLingDuanBtn.setOnClickListener(this);
        mJieDuanBtn = (LinearLayout) findViewById(R.id.jieduan_btn);
        mJieDuanBtn.setOnClickListener(this);
        mDiQuBtn = (LinearLayout) findViewById(R.id.diqu_btn);
        mDiQuBtn.setOnClickListener(this);

        xuelingduan = (TextView) findViewById(R.id.xuelingduan_text);
        jieduan = (TextView) findViewById(R.id.jieduan_text);
        diqu = (TextView) findViewById(R.id.diqu_text);
        passwordEditText = (EditText) findViewById(R.id.login_password_edit);
        passwordConfirmEdittext = (EditText) findViewById(R.id.login_password_confirm_edit);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_btn:
                finish();
                break;
            case R.id.next_btn:
                if (checkRegister()) {
                    skRegister();
                }
                break;
            case R.id.xuelingduan_btn:
                getxuelingduan();
                break;
            case R.id.jieduan_btn:
                getJieduan();
                break;
            case R.id.diqu_btn:
                getArea();
                break;
        }
    }


    //学生正常注册走的流程
    private void skRegister() {
        SKAsyncApiController.skRegister(sKStudent,
                new MyAsyncHttpResponseHandler(this, true) {
                    @Override
                    public void onSuccess(int arg0, String arg1) {
                        boolean success = SKResolveJsonUtil.getInstance().resolveIsSuccess(arg1, StudentRegisterUserinfoActivity.this);
                        if (success) {
                            SKAsyncApiController.skLogin(sKStudent.getMob(), sKStudent.getPwd(), new AsyncHttpResponseHandler() {
                                @Override
                                public void onSuccess(String json) {
                                    super.onSuccess(json);
                                    boolean Login_Success = SKResolveJsonUtil.getInstance().resolveIsSuccess(json, StudentRegisterUserinfoActivity.this);
                                    if (Login_Success) {
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
        Dialog.Intent(StudentRegisterUserinfoActivity.this, Student_Main_Activity.class);
        UIApplication.getInstance().setAuid_flag(true);
    }


    public boolean checkRegister() {
        if (checkPassword()) {
            String username = login_name_edit2.getText().toString().trim();
            String xuelingString = xuelingduan.getText().toString().trim();
            String jieduanString = jieduan.getText().toString().trim();
            String diquString = diqu.getText().toString().trim();
            if (TextUtils.isEmpty(username)) {
                Toast.makeText(this, "用户名不能为空!", Toast.LENGTH_SHORT).show();
                return false;
            } else if (TextUtils.isEmpty(xuelingString)) {
                Toast.makeText(this, "您还没有选择学龄段呢！", Toast.LENGTH_SHORT).show();
                return false;
            } else if (TextUtils.isEmpty(jieduanString)) {
                Toast.makeText(this, "您还没有选择阶段呢！", Toast.LENGTH_SHORT).show();
                return false;
            } else if (TextUtils.isEmpty(diquString)) {
                Toast.makeText(this, "您还没有选择城市呢！", Toast.LENGTH_SHORT).show();
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

    private void getxuelingduan() {
        SKAsyncApiController.skGetGrade(new MyAsyncHttpResponseHandler(this,
                true) {
            @Override
            public void onSuccess(String arg0) {
                super.onSuccess(arg0);
                mGradeData = SKResolveJsonUtil.getInstance().resolveGrade(arg0);


                final XuelingDuanSeltorUtil systemDialog = new XuelingDuanSeltorUtil(
                        StudentRegisterUserinfoActivity.this, mGradeData);
                systemDialog.setLeftButtonText("完成");
                systemDialog.show();
                systemDialog
                        .setXuelingSeltorUtilButtonOnclickListening(new XuelingDuanSeltorUtil.XuelingSeltorUtilButtonOnclickListening() {
                            @Override
                            public void onClickRight() {
                            }

                            @Override
                            public void onClickLeft() {
                                mGradeIndex = systemDialog.getGradeIndex();
                                xuelingduan.setText(systemDialog.getGradeName());
                            }
                        });

            }
        });
    }

    private void getJieduan() {
        if (mGradeIndex == -1) {
            Toast.makeText(this, "您还没有选择学龄段呢！", Toast.LENGTH_SHORT).show();
            return;
        }
        ArrayList<SkClasses> classList = mGradeData.get(mGradeIndex).getClasses();

        final JieDuanSeltorUtil systemDialog = new JieDuanSeltorUtil(StudentRegisterUserinfoActivity.this, classList);
        systemDialog.setLeftButtonText("完成");
        systemDialog.show();
        systemDialog.setJieduanSeltorUtilButtonOnclickListening(new JieDuanSeltorUtil.JieduanSeltorUtilButtonOnclickListening() {
            @Override
            public void onClickRight() {
            }

            @Override
            public void onClickLeft() {
                sKStudent.setGradeId(systemDialog.getGradeId());
                jieduan.setText(systemDialog.getSeltotText());
            }
        });
    }

    private void getArea() {
        SKAsyncApiController.skGetArea(new MyAsyncHttpResponseHandler(this,
                true) {
            @Override
            public void onSuccess(String arg0) {
                super.onSuccess(arg0);
                ArrayList<SKArea> resolveArea = SKResolveJsonUtil.getInstance()
                        .resolveArea(arg0);
                final AreaSeltorUtil systemDialog = new AreaSeltorUtil(
                        StudentRegisterUserinfoActivity.this, resolveArea);
                systemDialog.setLeftButtonText("完成");
                systemDialog.show();
                systemDialog
                        .setAreaSeltorUtilButtonOnclickListening(new AreaSeltorUtilButtonOnclickListening() {
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

}
