package com.yshow.shike.activities;
import com.umeng.analytics.MobclickAgent;
import com.yshow.shike.R;
import com.yshow.shike.fragments.Stu_QueCount_Subject;
import com.yshow.shike.fragments.Stu_QueCount_Time;
import com.yshow.shike.utils.PartnerConfig;
import com.yshow.shike.utils.ScreenSizeUtil;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

/**
 * 学生点击复习以后进入的页面
 *
 */
public class My_Question_Count extends FragmentActivity implements OnClickListener{
	private TextView subject_fen ,time_fen;//按学科分类  按时间分类
	private int startX = 0; // 记录移动的位置
	private View undline,bac_huise;//红线和背景图片
	private int mea_view; //测量View的方法
	private FragmentManager manager; //Fragment 管理器
	private FragmentTransaction ft; //Fragment 转换器
     @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.my_ques_coun);
    	initData();
    }
	private void initData() {
		findViewById(R.id.tv_stu_back).setOnClickListener(this);//返回键
		TextView tiku_delete = (TextView) findViewById(R.id.tv_stu_deleter);
		tiku_delete.setOnClickListener(this);//删除键
		undline = findViewById(R.id.stu_coun_undline);
		bac_huise = findViewById(R.id.coun_coun_huise);
		subject_fen = (TextView) findViewById(R.id.tv_subject_fen);
		time_fen = (TextView) findViewById(R.id.tv_time_fen);
		subject_fen.setOnClickListener(this);
		time_fen.setOnClickListener(this);
		if(PartnerConfig.TEATHER_ID != null || PartnerConfig.SUBJECT_ID != null){
			tiku_delete.setVisibility(View.GONE);
		}
		 // 初始化红线的长度
		mea_view = ScreenSizeUtil.getScreenWidth(My_Question_Count.this, 2);
		LayoutParams params = new LinearLayout.LayoutParams(mea_view,2);
		int view_hig = ScreenSizeUtil.Dp2Px(this,55);
		android.widget.RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(mea_view,view_hig);
		int view_height = ScreenSizeUtil.View_Measure(findViewById(R.id.stu_quescoun));
		layoutParams.topMargin = view_height;
		bac_huise.setLayoutParams(layoutParams);
		undline.setLayoutParams(params);
		manager = getSupportFragmentManager();
		ft = manager.beginTransaction();
		ft.replace(R.id.content,new Stu_QueCount_Subject());
		ft.commit();
	}
	@Override
	public void onClick(View v) {
		ft = manager.beginTransaction();
		switch (v.getId()) {
		case R.id.tv_stu_back:
			finish();
			break;
		case R.id.tv_stu_deleter:
			if(startX == 0){
				Stu_QueCount_Subject.handler.sendEmptyMessage(1);
			}else {
				Stu_QueCount_Time.handler.sendEmptyMessage(2);
			}
			break;
		//按学科分类
		case R.id.tv_subject_fen:
			subject_fen.setTextColor(getResources().getColor(R.color.reg));
			time_fen.setTextColor(getResources().getColor(R.color.sk_student_main_color));
			ft.replace(R.id.content,new Stu_QueCount_Subject());
			StartTransAnim(startX, 0,undline);
			StartTransAnim(startX, 0,bac_huise);
			startX = 0;
			break;
		// 按时间分类
		case R.id.tv_time_fen:
			time_fen.setTextColor(getResources().getColor(R.color.reg));
			subject_fen.setTextColor(getResources().getColor(R.color.sk_student_main_color));
			ft.replace(R.id.content,new Stu_QueCount_Time());
			StartTransAnim(startX, 1,undline);
			StartTransAnim(startX, 1,bac_huise);
			startX = 1;
			break;
		}
		ft.commit();
	}
	//开始执行动画的位移
	private void StartTransAnim(float fromXDelta,float toXDelta,View view) {
		TranslateAnimation anim = new TranslateAnimation(fromXDelta*mea_view, toXDelta*mea_view, 0, 0);
		anim.setDuration(500);
		anim.setFillAfter(true);
		view.startAnimation(anim);
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		PartnerConfig.TEATHER_ID = null;
		PartnerConfig.SUBJECT_ID = null;
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
