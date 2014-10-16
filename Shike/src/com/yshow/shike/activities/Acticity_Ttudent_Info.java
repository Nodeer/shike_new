package com.yshow.shike.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.umeng.analytics.MobclickAgent;
import com.yshow.shike.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yshow.shike.entity.SKMessage;
import com.yshow.shike.entity.Star_Teacher_Parse;
import com.yshow.shike.entity.Student_Info;
import com.yshow.shike.utils.MyAsyncHttpResponseHandler;
import com.yshow.shike.utils.SKAsyncApiController;
import com.yshow.shike.utils.SKResolveJsonUtil;

/**
 * 引导页设置
 * 
 * @author
 */
public class Acticity_Ttudent_Info extends Activity {
	private TextView tv_stu_nick;
	private TextView tv_stu_name;
	private TextView tv_stu_day;
	private TextView tv_stu_guan_zhu;
	private TextView tv_stu_ti_wen;
	private ImageView tudent_picture;
	private Star_Teacher_Parse stu_Info;
	private SKMessage extra;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activty_student_info);
		initData();
	}

	private void initData() {
		Bundle bundleExtra = getIntent().getExtras();
		stu_Info = (Star_Teacher_Parse) bundleExtra.getSerializable("Star_Teacher_Parse");
		extra = (SKMessage) bundleExtra.getSerializable("teather_sKMessage");
		tv_stu_nick = (TextView) findViewById(R.id.tv_stu_nick);
		tv_stu_name = (TextView) findViewById(R.id.tv_stu_name);
		tv_stu_day = (TextView) findViewById(R.id.tv_stu_day);
		tv_stu_guan_zhu = (TextView) findViewById(R.id.tv_stu_guan_zhu);
		tv_stu_ti_wen = (TextView) findViewById(R.id.tv_stu_ti_wen);
		tudent_picture = (ImageView) findViewById(R.id.tudent_stu_picture1);
		findViewById(R.id.tx_stu_info_back).setOnClickListener(listener);
		tv_stu_ti_wen.setOnClickListener(listener);
		None_Null();
	}

	private OnClickListener listener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.tx_stu_info_back:
				finish();
				break;
			case R.id.tv_stu_ti_wen:
				Toast.makeText(Acticity_Ttudent_Info.this, "查看提问数据", 0).show();
				break;
			}
		}
	};

	// 对消息页面和我的学生页面的对象进行非空判断
	private void None_Null() {
		if (extra != null) {
			String stu_uid = extra.getUid();
			Student_InFo(stu_uid);
		}
		if (stu_Info != null) {
			stu_info();
		}
	}

	private void Student_InFo(String uid) {
		SKAsyncApiController.User_Info(uid, new MyAsyncHttpResponseHandler(this, true) {
			@Override
			public void onSuccess(String json) {
				super.onSuccess(json);
				boolean success = SKResolveJsonUtil.getInstance().resolveIsSuccess(json, Acticity_Ttudent_Info.this);
				if (success) {
					Student_Info student_Info = SKResolveJsonUtil.getInstance().Student_Info_Pase(json);
					tv_stu_nick.setText("用户名 : " + student_Info.getNickname());
					tv_stu_name.setText("姓    名 : " + student_Info.getName());
					tv_stu_day.setText("学龄段 : " + student_Info.getGrade() + "" + student_Info.getGradeName());
					tv_stu_guan_zhu.setText(student_Info.getInfo());
					tv_stu_ti_wen.setText("提问数据: " + student_Info.getQuestions());
					Init_Picture(student_Info.getIcon(), tudent_picture);
				}
			}
		});
	}

	// 从我的学生页面跳转过来 设置学生的基本信息
	private void stu_info() {
		tv_stu_nick.setText("用户名 :" + stu_Info.getNickname());
		tv_stu_name.setText("姓    名:" + stu_Info.getName());
		tv_stu_day.setText("学龄段 :" + stu_Info.getGrade() + "" + stu_Info.getGradeName());
		tv_stu_guan_zhu.setText(stu_Info.getInfo());
		tv_stu_ti_wen.setText("提问数据:" + stu_Info.getQuestions());
		Init_Picture(stu_Info.getIcon(), tudent_picture);
	}

	// 设置头像
	private void Init_Picture(String url, ImageView imageView) {
		DisplayImageOptions options = new DisplayImageOptions.Builder().cacheOnDisc(true)
				.showImageForEmptyUri(R.drawable.teather_stu_picture).showImageOnFail(R.drawable.teather_stu_picture).cacheInMemory(true)
				.build();
		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.displayImage(url, imageView, options);
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
}