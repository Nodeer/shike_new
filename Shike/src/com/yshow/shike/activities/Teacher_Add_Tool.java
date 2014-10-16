package com.yshow.shike.activities;
import com.umeng.analytics.MobclickAgent;
import com.yshow.shike.R;
import com.yshow.shike.entity.LoginManage;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

/**
 * 从我的老师详情页面点击提问跳转
 */
public class Teacher_Add_Tool extends Activity implements OnClickListener{
    private boolean booleanExtra;
	private String questionId;
	private RelativeLayout add_tool_bottom;
	Intent intent;
	@Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.thank_add_tool);
    	initData();
    }
	private void initData() {
		findViewById(R.id.tea_gongju_text).setOnClickListener(this);
		findViewById(R.id.tea_tiku_tiku).setOnClickListener(this);
		findViewById(R.id.tv_add_tool_back).setOnClickListener(this);
		add_tool_bottom = (RelativeLayout) findViewById(R.id.ll_add_tool_bottom);
		Intent intent2 = getIntent();		
		booleanExtra = intent2.getBooleanExtra("isContinue", false);
		questionId = intent2.getStringExtra("questionId");
		if(LoginManage.getInstance().isTeacher()){
			add_tool_bottom.setBackgroundResource(R.color.bottom_widow_color);
		}
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tea_gongju_text:
			Intent intent = new Intent(this, Activity_Tea_Tool_Sele.class);
			intent.putExtra("isContinue", booleanExtra);
			intent.putExtra("questionId", questionId);
			intent.putExtra("tea_add_tool","1");
			startActivityForResult(intent, 1);
			finish();
			break;
		case R.id.tea_tiku_tiku:
			if(!LoginManage.getInstance().isTeacher()){
				intent = new Intent(this, My_Question_Count.class);
				intent.putExtra("isContinue", booleanExtra);
				intent.putExtra("questionId", questionId);
				startActivityForResult(intent, 1);
			}else {
				intent = new Intent(this, Activity_Teather_Ti_Ku.class);
				intent.putExtra("teather", "teather");
				startActivity(intent);
			}
			finish();
			break;
		case R.id.tv_add_tool_back:
			finish();
			break;
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