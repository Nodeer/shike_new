package com.yshow.shike.activities;
import java.util.ArrayList;
import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.yshow.shike.utils.Dialog;
import com.yshow.shike.utils.GradeSeltorUtil;
import com.yshow.shike.utils.MyAsyncHttpResponseHandler;
import com.yshow.shike.utils.SKAsyncApiController;
import com.yshow.shike.utils.SKResolveJsonUtil;
import com.yshow.shike.utils.AreaSeltorUtil.AreaSeltorUtilButtonOnclickListening;
import com.yshow.shike.utils.GradeSeltorUtil.SystemDialogButtonOnclickListening;

/**
 * 输入手机号和验证码之后的页面.用户填写用户名,地区,年级
 */
public class PhoneProving extends Activity implements OnClickListener {
	private EditText login_name_edit2; // 手机号 文本框
	private SKStudent sKStudent;
	private TextView grade;
	private TextView area;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_phone);
		sKStudent = (SKStudent) getIntent().getExtras().getSerializable("student");
		initdata();
	}
	private void initdata() {
		login_name_edit2 = (EditText) findViewById(R.id.login_name_edit);
		findViewById(R.id.tv_back).setOnClickListener(this);
		findViewById(R.id.tv_comfig).setOnClickListener(this);
		grade = (TextView) findViewById(R.id.sping1);
		grade.setOnClickListener(this);
		area = (TextView) findViewById(R.id.sping2);
		area.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_back:
			finish();
			break;
		case R.id.tv_comfig:
			if (checkRegister()) {
				Bundle bundle = new Bundle();
				bundle.putSerializable("student", sKStudent);
				Dialog.intent(this, PasswordSetting.class, bundle);
			}
			break;
		case R.id.sping1:
			getGrade();
			break;
		case R.id.sping2:
			getArea();
			break;
		}
	}
	public boolean checkRegister() {
		String username = login_name_edit2.getText().toString().trim();
		String gread = grade.getText().toString().trim();
		String area_name = area.getText().toString().trim();
		if (TextUtils.isEmpty(username)) {
			Toast.makeText(this, "用户名不能为空!", 0).show();
			return false;
		}else if(TextUtils.isEmpty(gread)){
			Toast.makeText(this, "您还没有选择年级呢！", 0).show();
			return false;
		}else if (TextUtils.isEmpty(area_name)) {
			Toast.makeText(this, "您还没有选择城市呢！", 0).show();
			return false;
		}else {
			sKStudent.setNickname(username);
			return true;
		}
	}
	private void getGrade() {
		SKAsyncApiController.skGetGrade(new MyAsyncHttpResponseHandler(this,
				true) {
			@Override
			public void onSuccess(String arg0) {
				super.onSuccess(arg0);
				ArrayList<SKGrade> resolveGrade = SKResolveJsonUtil.getInstance().resolveGrade(arg0);
				final GradeSeltorUtil systemDialog = new GradeSeltorUtil(PhoneProving.this, resolveGrade);
				systemDialog.setLeftButtonText("完成");
				systemDialog.show();
				systemDialog.setSystemDialogButtonOnclickListening(new SystemDialogButtonOnclickListening() {
							@Override
							public void onClickRight() {
							}
							@Override
							public void onClickLeft() {
								sKStudent.setGradeId(systemDialog.getGradeId());
								grade.setText(systemDialog.getSeltotText());
							}
						});
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
						PhoneProving.this, resolveArea);
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
								area.setText(systemDialog.getSeltotText());
							}
						});
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
