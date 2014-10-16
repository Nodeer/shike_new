package com.yshow.shike.activities;
import com.umeng.analytics.MobclickAgent;
import com.yshow.shike.R;
import com.yshow.shike.fragments.Fragment_Account_Prepraid;
import com.yshow.shike.fragments.Fragment_Account_Use;
import com.yshow.shike.utils.ScreenSizeUtil;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View.OnClickListener;
import android.view.animation.TranslateAnimation;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
public class Activity_Student_Account_Message extends FragmentActivity {
	private int mea_view; //测量View的方法
	private FragmentManager manager; //Fragment 管理器
	private FragmentTransaction ft; //Fragment 转换器
	private int startX = 0; // 记录移动的位置
	private View undline,bac_huise;//红线和背景图片
	private Context context;
	private TextView tv_cz_reg,tv_sy_reg;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_student_account);
		context = this;
		initData();
	}
	private void initData() {
		findViewById(R.id.tv_coun_back).setOnClickListener(listener);
		tv_cz_reg = (TextView) findViewById(R.id.tv_cz_reg);
		tv_sy_reg = (TextView) findViewById(R.id.tv_sy_reg);
		int view_heith = ScreenSizeUtil.View_Measure(findViewById(R.id.li_stu_quescoun));
		tv_cz_reg.setOnClickListener(listener);
		tv_sy_reg.setOnClickListener(listener);
		undline = findViewById(R.id.coun_reg_undline);
		bac_huise = findViewById(R.id.coun_bac);
		 // 初始化红线的长度
		mea_view = View_Measure();
		int view_hig = ScreenSizeUtil.Dp2Px(context, 48);
		android.widget.RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(mea_view,view_hig);
		layoutParams.topMargin = view_heith;
		bac_huise.setLayoutParams(layoutParams);
		LayoutParams params = new LinearLayout.LayoutParams(mea_view,2);
		undline.setLayoutParams(params);
		manager = getSupportFragmentManager();
		ft = manager.beginTransaction();
		ft.replace(R.id.li_th_content,new Fragment_Account_Prepraid());
		ft.commit();
	}
	private OnClickListener listener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			ft = manager.beginTransaction();
			switch (v.getId()) {
			case R.id.tv_coun_back:
				finish();
				break;              
			case R.id.tv_cz_reg:
				tv_cz_reg.setTextColor(getResources().getColor(R.color.reg));
				tv_sy_reg.setTextColor(getResources().getColor(R.color.sk_student_main_color));
				ft.replace(R.id.li_th_content,new Fragment_Account_Prepraid());
				StartTransAnim(startX, 0,undline);
				StartTransAnim(startX, 0,bac_huise);
				startX = 0;
				break;
			case R.id.tv_sy_reg:
				tv_sy_reg.setTextColor(getResources().getColor(R.color.reg));
				tv_cz_reg.setTextColor(getResources().getColor(R.color.sk_student_main_color));
				ft.replace(R.id.li_th_content,new Fragment_Account_Use());
				StartTransAnim(startX, 1,undline);
				StartTransAnim(startX, 1,bac_huise);
				startX = 1;
				break;
			}
			ft.commit();
		}
	};
	//开始执行动画的位移
	private void StartTransAnim(float fromXDelta,float toXDelta,View view) {
		TranslateAnimation anim = new TranslateAnimation(fromXDelta*mea_view, toXDelta*mea_view, 0, 0);
		anim.setDuration(500);
		anim.setFillAfter(true);
		view.startAnimation(anim);
	}
	//  测量手机屏幕的宽度和高度
	private int View_Measure() {
		@SuppressWarnings("deprecation")
		int width = getWindowManager().getDefaultDisplay().getWidth();
		int i = width / 2;
		return i;
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
