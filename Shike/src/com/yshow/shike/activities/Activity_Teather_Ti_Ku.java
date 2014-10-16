package com.yshow.shike.activities;
import com.umeng.analytics.MobclickAgent;
import com.yshow.shike.R;
import com.yshow.shike.fragments.Tea_Tiku_Files;
import com.yshow.shike.fragments.Tea_Tiku_Time;
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
 * 老师端题库,可以按文件夹和日期查看自己的题库列表
 * @author Administrator
 */
public class Activity_Teather_Ti_Ku extends FragmentActivity implements OnClickListener {
	private TextView tea_files, tea_data; // 文件夹按钮 日期按钮
	private int startX = 0; // 记录移动的位置
	private View undline, bac_huise;// 红线和背景图片
	private int mea_view; // 测量View的方法
	private FragmentManager manager; // Fragment 管理器
	private FragmentTransaction ft; // Fragment 转换器
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.tea_tiku);
		initData();
	}
	private void initData() {
		tea_files = (TextView) findViewById(R.id.tv_tea_files);
		tea_data = (TextView) findViewById(R.id.tv_tea_data);
		undline = findViewById(R.id.tea_coun_undline);
		bac_huise = findViewById(R.id.tea_coun_huise);
		findViewById(R.id.tv_teah_back).setOnClickListener(this);
		findViewById(R.id.tv_tea_edit).setOnClickListener(this);
		tea_data.setOnClickListener(this);
		tea_files.setOnClickListener(this);
		int Height = ScreenSizeUtil.View_Measure(findViewById(R.id.li_stu_quescoun));
		// 初始化红线的长度
		mea_view = ScreenSizeUtil.getScreenWidth(this, 2);
		LayoutParams params = new LinearLayout.LayoutParams(mea_view, 2);
		undline.setLayoutParams(params);
		int view_hig = ScreenSizeUtil.Dp2Px(this, 45);
		android.widget.RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(mea_view, view_hig);
		layoutParams.topMargin = Height;
		bac_huise.setLayoutParams(layoutParams);
		manager = getSupportFragmentManager();
		ft = manager.beginTransaction();
		ft.replace(R.id.tea_content, new Tea_Tiku_Files());
		ft.commit();
	}

	@Override
	public void onClick(View v) {
		ft = manager.beginTransaction();
		switch (v.getId()) {
		case R.id.tv_tea_files:
			tea_files.setTextColor(getResources().getColor(R.color.reg));
			tea_data.setTextColor(getResources().getColor(R.color.bottom_widow_color));
			ft.replace(R.id.tea_content, new Tea_Tiku_Files());
			StartTransAnim(startX, 0, undline);
			StartTransAnim(startX, 0, bac_huise);
			startX = 0;
			break;
		case R.id.tv_tea_data:
			tea_data.setTextColor(getResources().getColor(R.color.reg));
			tea_files.setTextColor(getResources().getColor(R.color.bottom_widow_color));
			ft.replace(R.id.tea_content, new Tea_Tiku_Time());
			StartTransAnim(startX, 1, undline);
			StartTransAnim(startX, 1, bac_huise);
			startX = 1;
			break;
		case R.id.tv_teah_back:
			finish();
			break;
		// 题库编辑问题
		case R.id.tv_tea_edit:
			if (startX == 0) {
				Tea_Tiku_Files.handler.sendEmptyMessage(0);
			} else {
				Tea_Tiku_Time.handler.sendEmptyMessage(1);
			}
			break;
		}
		ft.commit();
	}

	// 开始执行动画的位移
	private void StartTransAnim(float fromXDelta, float toXDelta, View view) {
		TranslateAnimation anim = new TranslateAnimation(fromXDelta * mea_view,toXDelta * mea_view, 0, 0);
		anim.setDuration(500);
		anim.setFillAfter(true);
		view.startAnimation(anim);
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
