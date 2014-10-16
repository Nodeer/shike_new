package com.yshow.shike.activities;
import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;
import com.yshow.shike.R;
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
public class Activity_Teather_Material extends Activity implements
		OnClickListener {
	private EditText nickname_edit; // 昵称
	private TextView teather_subject;// 获取学科
	private TextView teather_diqu; // 获取地区
	private TextView teather_jieduan; // 获取阶段
	private SKStudent sKStudent;
	private String subject,diqu,jie_duan; // 学科  地区 阶段
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_teather_material);
		initData();
	}
	private void initData() {
		sKStudent = (SKStudent) getIntent().getExtras().getSerializable("teather");
		nickname_edit = (EditText) findViewById(R.id.login_nickname_edit);
		teather_subject = (TextView) findViewById(R.id.tv_teather_subject);
		teather_diqu = (TextView) findViewById(R.id.tv_teather_diqu);
		teather_jieduan = (TextView) findViewById(R.id.tv_teather_jiduan);
		teather_subject.setOnClickListener(this);
		teather_diqu.setOnClickListener(this);
		teather_jieduan.setOnClickListener(this);
		findViewById(R.id.tv_back).setOnClickListener(this);
		findViewById(R.id.tv_comfig).setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		//学科按钮
		case R.id.tv_teather_subject:
			getSubject();
			break;
		//地区按钮
		case R.id.tv_teather_diqu:
			getArea();
			break;
		//阶段按钮
		case R.id.tv_teather_jiduan:
			getJieDuan();
			break;
		//下一步按钮
		case R.id.tv_back:
			finish();
			break;
		case R.id.tv_comfig:
			subject = teather_subject.getText().toString().trim();
			diqu = teather_diqu.getText().toString().trim();
			jie_duan = teather_jieduan.getText().toString().trim();
			if (checkRegister()) {
				Bundle bundle = new Bundle();
				bundle.putSerializable("student", sKStudent);
				Dialog.intent(this, Activity_Teather_Material_Two.class, bundle);
			}
			break;
		}
	}
	// 获取地区
	private void getArea() {
		SKAsyncApiController.skGetArea(new MyAsyncHttpResponseHandler(this,true) {
			@Override
			public void onSuccess(String arg0) {
				super.onSuccess(arg0);
				Log.e("getArea", arg0);
				ArrayList<SKArea> resolveArea = SKResolveJsonUtil.getInstance().resolveArea(arg0);
				final AreaSeltorUtil systemDialog = new AreaSeltorUtil(Activity_Teather_Material.this, resolveArea);
				systemDialog.setLeftButtonText("完成");
				systemDialog.show();
				systemDialog.setAreaSeltorUtilButtonOnclickListening(new AreaSeltorUtilButtonOnclickListening() {
							@Override
							public void onClickRight() {}
							@Override
							public void onClickLeft() {
								sKStudent.setaId(systemDialog.getGradeId());
								teather_diqu.setText(systemDialog.getSeltotText());
							}
						});
			}
		});
	}
	// 获取学科
	private void getSubject() {
		SKAsyncApiController.skGetSubjeck(new MyAsyncHttpResponseHandler(this,true) {
			@Override
			public void onSuccess(final int arg0, final String arg1) {
				super.onSuccess(arg0, arg1);
				ArrayList<SKArea> paseSubject = SKResolveJsonUtil.getInstance().PaseSubject(arg1);
				final AreaSeltorUtil subjectId = new AreaSeltorUtil(Activity_Teather_Material.this, paseSubject);
				subjectId.setLeftButtonText("完成");
				subjectId.show();
				subjectId.setAreaSeltorUtilButtonOnclickListening(new AreaSeltorUtilButtonOnclickListening() {
							@Override
							public void onClickRight() {}
							@Override
							public void onClickLeft() {
								String gradeId = subjectId.getGradeId();
								sKStudent.setSubject(gradeId);
								teather_subject.setText(subjectId.getSeltotText());
							}
						});
			}
		});
	}
	// 获取阶段
	private void getJieDuan() {
		SKAsyncApiController.skGetGrade(new MyAsyncHttpResponseHandler(this,true) {
			@Override
			public void onSuccess(final int arg0, final String arg1) {
				super.onSuccess(arg0, arg1);
			ArrayList<SKGrade> SKGrades = SKResolveJsonUtil.getInstance().resolveGrade(arg1);
		    final Grade_Level_Utils grade_utils = new Grade_Level_Utils(Activity_Teather_Material.this, SKGrades);
			grade_utils.setLeftButtonText("完成");
			grade_utils.setGradeSeltorUtilButtonOnclickListening(new
					GradeSeltorUtilButtonOnclickListening() {
						@Override
						public void onClickRight() { }
						@Override
						public void onClickLeft() {
							String seltotText = grade_utils.getSeltotText();
							teather_jieduan.setText(seltotText);
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
		String username = nickname_edit.getText().toString().trim();
		if (TextUtils.isEmpty(username)) {
			Toast.makeText(this, "您还没有填写信姓名!", 0).show();
			return false;
		}else if (TextUtils.isEmpty(subject)) {
			Toast.makeText(this, "您还没有填写学科!", 0).show();
			return false;
		}else if (TextUtils.isEmpty(diqu)) {
			Toast.makeText(this, "您还没有填写地区呢!", 0).show();
			return false;
		}else if (TextUtils.isEmpty(jie_duan)) {
			Toast.makeText(this, "您还没有填写阶段呢!", 0).show();
			return false;
		}else {
			sKStudent.setNickname(username);
			return true;
		}
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