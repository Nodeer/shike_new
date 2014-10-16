package com.yshow.shike.activities;
import com.umeng.analytics.MobclickAgent;
import com.yshow.shike.R;
import com.yshow.shike.fragments.Fragment_Teacher_Zhanghu_Kaihu;
import com.yshow.shike.fragments.Fragment_Teacher_Zhanghu_Shouru;
import com.yshow.shike.fragments.Fragment_Teacher_Zhanghu_duihuan;
import com.yshow.shike.utils.ScreenSizeUtil;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

/**
 * 老师设置账户信息
 */
public class Activity_Teacher_zhanghu extends FragmentActivity {
	private FragmentTransaction ft; // Fragment 转化活动
	private FragmentManager manager; //Fragment管理器
	private int mea_view; // 长度测量
	private View undline,bac_topic; //需要测量的红线 和字体的背景颜色
	private TextView bank_coun,acc_balance,exchange_info;//银行账户 收入明细 兑换信息
	private int startX = 0; // 当前一道第几个位置
    /**是否从兑换现金页面进来,如果是的话,更新成功以后需要返回*/
    public boolean isNeedBack = false;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_teacher_zhanghu);
        isNeedBack = getIntent().getBooleanExtra("isneedback",false);
		init();
	}
	private void init() {
		findViewById(R.id.tv_bank_back).setOnClickListener(listener);
		bank_coun = (TextView) findViewById(R.id.tv_bank_coun);
		acc_balance = (TextView) findViewById(R.id.tv_acc_balance);
		exchange_info = (TextView) findViewById(R.id.tv_exchange_info);
		int view_height = ScreenSizeUtil.View_Measure(findViewById(R.id.li_bank_Info));
		bac_topic = findViewById(R.id.bank_bac);
		undline = findViewById(R.id.bank_undline);
		bank_coun.setOnClickListener(listener);
		exchange_info.setOnClickListener(listener);
		acc_balance.setOnClickListener(listener);
		findViewById(R.id.li_bank_content).setOnClickListener(listener);
        // 初始化红线的长度
		mea_view = ScreenSizeUtil.getScreenWidth(this,3);
		LayoutParams params = new LinearLayout.LayoutParams(mea_view,3);
		// 动态设置背影图片的宽度
		int view_hig = ScreenSizeUtil.Dp2Px(this,45);
		android.widget.RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(mea_view,view_hig);
		layoutParams.topMargin = view_height;
		bac_topic.setLayoutParams(layoutParams);
		undline.setLayoutParams(params);
		manager = getSupportFragmentManager();
		ft = manager.beginTransaction();
		ft.replace(R.id.li_bank_content,new Fragment_Teacher_Zhanghu_Kaihu());
		ft.commitAllowingStateLoss();
	}
	private OnClickListener listener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			ft = manager.beginTransaction();
			switch (v.getId()) {
			// 返回键
			case R.id.tv_bank_back:
				finish();
				break;
			//银行账户
			case R.id.tv_bank_coun:
				bank_coun.setTextColor(getResources().getColor(R.color.reg));
				acc_balance.setTextColor(getResources().getColor(R.color.log));
				exchange_info.setTextColor(getResources().getColor(R.color.log));
				ft.replace(R.id.li_bank_content,new Fragment_Teacher_Zhanghu_Kaihu());
				StartTransAnim(startX, 0,undline);
				StartTransAnim(startX, 0,bac_topic);
				startX = 0;
				break;
			// 兑换信息
			case R.id.tv_acc_balance:
				exchange_info.setTextColor(getResources().getColor(R.color.log));
				bank_coun.setTextColor(getResources().getColor(R.color.log));
				acc_balance.setTextColor(getResources().getColor(R.color.reg));
				ft.replace(R.id.li_bank_content,new Fragment_Teacher_Zhanghu_duihuan());
				StartTransAnim(startX, 1,undline);
				StartTransAnim(startX, 1,bac_topic);
				startX = 1;
				break;
			//收入明细
			case R.id.tv_exchange_info:
				acc_balance.setTextColor(getResources().getColor(R.color.log));
				bank_coun.setTextColor(getResources().getColor(R.color.log));
				exchange_info.setTextColor(getResources().getColor(R.color.reg));
				ft.replace(R.id.li_bank_content,new Fragment_Teacher_Zhanghu_Shouru());
				StartTransAnim(startX, 2,undline);
				StartTransAnim(startX, 2,bac_topic);
				startX = 2;
				break;
			}
			ft.commit();
		}
	};
	//  测量手机屏幕的宽度和高度
//	private int View_Measure() {
//		@SuppressWarnings("deprecation")
//		int width = getWindowManager().getDefaultDisplay().getWidth();
//		int i = width / 3;
//		return i;
//	}
	//开始执行动画的位移
	private void StartTransAnim(float fromXDelta,float toXDelta,View view) {
		TranslateAnimation anim = new TranslateAnimation(fromXDelta*mea_view, toXDelta*mea_view, 0, 0);
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
