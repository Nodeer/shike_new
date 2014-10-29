package com.yshow.shike.activities;

import java.util.ArrayList;

import com.yshow.shike.R;
import com.yshow.shike.entity.SKArea;
import com.yshow.shike.entity.SKTeacherOrSubject;
import com.yshow.shike.entity.Star_Teacher_Parse;
import com.yshow.shike.utils.AreaSeltorUtil;
import com.yshow.shike.utils.MyAsyncHttpResponseHandler;
import com.yshow.shike.utils.SKAsyncApiController;
import com.yshow.shike.utils.SKResolveJsonUtil;
import com.yshow.shike.utils.SexSeltorUtil;
import com.yshow.shike.utils.XuekeSelectUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SearchTeacherActivity extends BaseActivity implements OnClickListener {
    private EditText user_phon;
    private String nickname, school;  // 获取文本框电话号码
    private TextView tvSubject, diqu_text, tvSex;
    private String subject_id, diqu_id; //学科id 地区id
    private EditText et_user_name, et_school;//用户名 学校名
    private Context context;

    private LinearLayout mXuekeBtn, mDiquBtn, mXingbieBtn;

    private TextView mPhoneSearchBtn;

    private Button mSearchBtn;

    private String sexId = "-1";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_search_teacher);

        context = this;
        TextView titletext = (TextView) findViewById(R.id.title_text);
        titletext.setText("搜索老师");

        findViewById(R.id.left_btn).setOnClickListener(this);

        user_phon = (EditText) findViewById(R.id.et_user_phon);
        mPhoneSearchBtn = (TextView) findViewById(R.id.phone_search_btn);
        mPhoneSearchBtn.setOnClickListener(this);
        mSearchBtn = (Button) findViewById(R.id.search_btn);
        mSearchBtn.setOnClickListener(this);

        et_user_name = (EditText) findViewById(R.id.et_user_name);
        et_school = (EditText) findViewById(R.id.et_school);

        tvSubject = (TextView) findViewById(R.id.xueke_text);
        diqu_text = (TextView) findViewById(R.id.diqu_text);
        tvSex = (TextView) findViewById(R.id.xingbie_text);
        mXuekeBtn = (LinearLayout) findViewById(R.id.xueke_btn);
        mXuekeBtn.setOnClickListener(this);
        mDiquBtn = (LinearLayout) findViewById(R.id.diqu_btn);
        mDiquBtn.setOnClickListener(this);
        mXingbieBtn = (LinearLayout) findViewById(R.id.xingbie_btn);
        mXingbieBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 学科按钮
            case R.id.xueke_btn:
                skGetSubject();
                break;
            // 地区按钮
            case R.id.diqu_btn:
                skGetArea();
                break;
            // 性别按钮
            case R.id.xingbie_btn:
                getSex();
                break;
            case R.id.phone_search_btn:
                String phone = user_phon.getText().toString();
                Searth_Teather_Mob(phone);
                break;
            case R.id.search_btn:
                nickname = et_user_name.getText().toString().trim();
                school = et_school.getText().toString().trim();
                Searth_Teather_TiaoJian(nickname, school, sexId);
                break;
            case R.id.left_btn:
                finish();
                break;
        }
    }

    // 联网那一科目为条件的科目
    private void skGetSubject() {
        SKAsyncApiController.skGetSubject(new MyAsyncHttpResponseHandler(SearchTeacherActivity.this, true) {
            @Override
            public void onSuccess(String arg0) {
                super.onSuccess(arg0);
                ArrayList<SKTeacherOrSubject> subjects = SKResolveJsonUtil.getInstance().resolveSubject(arg0);
                SKTeacherOrSubject skTeacherOrSubject = new SKTeacherOrSubject();
                skTeacherOrSubject.setName("不限");
                skTeacherOrSubject.setSubjectId("0");
                subjects.add(0, skTeacherOrSubject);


                final XuekeSelectUtil subjectId = new XuekeSelectUtil(SearchTeacherActivity.this, subjects);
                subjectId.setLeftButtonText("完成");
                subjectId.show();
                subjectId.setAreaSeltorUtilButtonOnclickListening(new XuekeSelectUtil.AreaSeltorUtilButtonOnclickListening() {
                    @Override
                    public void onClickRight() {
                    }

                    @Override
                    public void onClickLeft() {
                        subject_id = subjectId.getGradeId();
                        tvSubject.setText(subjectId.getSeltotText());
                    }
                });

            }
        });
    }

    private void skGetArea() {
        SKAsyncApiController.skGetArea(new MyAsyncHttpResponseHandler(context, true) {
            @Override
            public void onSuccess(String arg0) {
                super.onSuccess(arg0);
                ArrayList<SKArea> resolveArea = SKResolveJsonUtil.getInstance().resolveArea(arg0);
                SKArea skArea = new SKArea();
                skArea.setId("0");
                skArea.setName("不限");
                resolveArea.add(0, skArea);


                final AreaSeltorUtil systemDialog = new AreaSeltorUtil(SearchTeacherActivity.this, resolveArea);
                systemDialog.setLeftButtonText("完成");
                systemDialog.show();
                systemDialog.setAreaSeltorUtilButtonOnclickListening(new AreaSeltorUtil.AreaSeltorUtilButtonOnclickListening() {
                    @Override
                    public void onClickRight() {
                    }

                    @Override
                    public void onClickLeft() {
                        diqu_id = systemDialog.getGradeId();
                        diqu_text.setText(systemDialog.getSeltotText());
                    }
                });
            }
        });
    }

    private void getSex() {
        final SexSeltorUtil systemDialog = new SexSeltorUtil(SearchTeacherActivity.this);
        systemDialog.setLeftButtonText("完成");
        systemDialog.show();
        systemDialog.setAreaSeltorUtilButtonOnclickListening(new SexSeltorUtil.AreaSeltorUtilButtonOnclickListening() {
            @Override
            public void onClickRight() {
            }

            @Override
            public void onClickLeft() {
                sexId = systemDialog.getGradeId();
                tvSex.setText(systemDialog.getSeltotText());
            }
        });
    }


    /**
     * 电话搜索老师
     */
    private void Searth_Teather_Mob(String user_name) {
        SKAsyncApiController.Searth_Teather_Mob(user_name,
                new MyAsyncHttpResponseHandler(SearchTeacherActivity.this, true) {
                    @Override
                    public void onSuccess(String json) {
                        super.onSuccess(json);
                        boolean success = SKResolveJsonUtil.getInstance().resolveIsSuccess(json, SearchTeacherActivity.this);
                        if (success) {
                            ArrayList<Star_Teacher_Parse> list = SKResolveJsonUtil.getInstance().Search_Terms(json);
                            Intent it = new Intent(SearchTeacherActivity.this, SearchResultTeacherListActivity.class);
                            it.putExtra("data", list);
                            startActivity(it);
                        }
                    }
                });
    }

    // 条件搜索
    private void Searth_Teather_TiaoJian(String nickname, String school, String sex) {
        SKAsyncApiController.Searth_Teather_TiaoJian(nickname, school, subject_id,
                diqu_id, sex, new MyAsyncHttpResponseHandler(context, true) {
                    @Override
                    public void onSuccess(String json) {
                        super.onSuccess(json);
                        SKResolveJsonUtil.getInstance().resolveIsSuccess(json, SearchTeacherActivity.this);
                        ArrayList<Star_Teacher_Parse> list = SKResolveJsonUtil.getInstance().Search_Terms(json);
                        Intent it = new Intent(SearchTeacherActivity.this, SearchResultTeacherListActivity.class);
                        it.putExtra("data", list);
                        startActivity(it);
                    }
                });
    }
}
