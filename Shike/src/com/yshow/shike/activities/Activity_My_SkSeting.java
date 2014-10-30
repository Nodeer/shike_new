package com.yshow.shike.activities;

import com.umeng.analytics.MobclickAgent;
import com.yshow.shike.R;
import com.yshow.shike.fragments.Fragment_Teacher_About_Shike;
import com.yshow.shike.utils.Dialog;
import com.yshow.shike.utils.Dilog_Share;
import com.yshow.shike.utils.PartnerConfig;
import com.yshow.shike.utils.ShareDialog;
import com.yshow.shike.utils.WeixinManager;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class Activity_My_SkSeting extends Activity {
	private Context context;
	private android.app.Dialog dialog; // 分享的Dialog动画
	private WeixinManager weixinManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_skseting);
		context = this;
		initData();
	}

	@SuppressWarnings("static-access")
	private void initData() {
		weixinManager = new WeixinManager(Activity_My_SkSeting.this);
		dialog = new Dilog_Share().Dilog_Anim(context, listener);
		findViewById(R.id.tv_seting_back).setOnClickListener(listener);
		findViewById(R.id.tv_tea_setinfo).setOnClickListener(listener);
		findViewById(R.id.tv_tea_accinfo).setOnClickListener(listener);
		findViewById(R.id.tv_tea_shrefrd).setOnClickListener(listener);
		findViewById(R.id.tv_tea_aboutsk).setOnClickListener(listener);
	}

	private OnClickListener listener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.tv_seting_back:
				finish();
				break;
			case R.id.tv_tea_setinfo:
				Dialog.Intent(context, TeaPersonInfoActivity.class);
				break;
			case R.id.tv_tea_accinfo:
				Dialog.Intent(context, Activity_Teacher_zhanghu.class);
				break;
			case R.id.tv_tea_shrefrd:
				dialog.show();
				break;
			case R.id.tv_tea_aboutsk:
				Dialog.Intent(context, Fragment_Teacher_About_Shike.class);
				break;
			// 分享按钮的点击事件
			case R.id.share_dialog_weixin:
				dialog.dismiss();
				weixinManager.shareWeixin();
				break;
			case R.id.share_weixin_friend:
				dialog.dismiss();
				weixinManager.shareWeixinCircle();
				break;
			case R.id.share_dialog_message:
				dialog.dismiss();
				ShareDialog.sendSMS(context, PartnerConfig.CONTEBT);
				break;
			case R.id.share_dialog_email:
				dialog.dismiss();
				ShareDialog.openCLD(PartnerConfig.CONTEBT, Activity_My_SkSeting.this);
				break;
			case R.id.share_dialog_cancle:
				dialog.dismiss();
				break;
			}
		}
	};

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
	// 短信分享
	// private void sendSMS(){
	// Uri smsToUri = Uri.parse("smsto:");
	// Intent sendIntent = new Intent(Intent.ACTION_VIEW, smsToUri);
	// sendIntent.putExtra("sms_body", "分享");
	// sendIntent.setType("vnd.android-dir/mms-sms");
	// startActivityForResult(sendIntent,1002);
	// }
}
