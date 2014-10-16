package com.yshow.shike.activities;
import java.io.Serializable;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;
import com.yshow.shike.R;
import com.yshow.shike.entity.Star_Teacher_Parse;
import com.yshow.shike.utils.Dialog;
import com.yshow.shike.utils.MyAsyncHttpResponseHandler;
import com.yshow.shike.utils.Net_Servse;
import com.yshow.shike.utils.PartnerConfig;
import com.yshow.shike.utils.SKAsyncApiController;
import com.yshow.shike.utils.SKResolveJsonUtil;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 学生登录状态下,显示我的老师详情页面
 */
public class Activity_My_Teacher_Info extends Activity implements OnClickListener{
	private TextView tx_info_back;
	private Star_Teacher_Parse item;
	private boolean flag = true;
	private TextView tv_attention;
	private Context context;
	private TextView tv_shike_fen;
	private ImageView iv_xuan_zhong;
	private DisplayImageOptions options;
	private ImageLoader imageLoader;
    private String myteather;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_teacher_info);
		initDate();
	}
	private void initDate() {
		context = this;
		options = Net_Servse.getInstence().Picture_Shipei(R.drawable.my_tea_phon);
		imageLoader = ImageLoader.getInstance();
		tx_info_back = (TextView) findViewById(R.id.tx_info_back);
		TextView teacher_nick = (TextView) findViewById(R.id.tv_teacher_nick);
		TextView tv_subject = (TextView) findViewById(R.id.tv_subject);
		TextView strdent_count = (TextView) findViewById(R.id.tv_strdent_count);
		TextView tv_guan_zhu = (TextView) findViewById(R.id.tv_guan_zhu);
		ImageView tudent_picture = (ImageView) findViewById(R.id.tudent_picture);
		tv_attention = (TextView) findViewById(R.id.tv_attention);
		TextView tv_zan_shu = (TextView) findViewById(R.id.tv_zan_coun);
		iv_xuan_zhong = (ImageView) findViewById(R.id.iv_xuan_zhong);
		tv_shike_fen = (TextView) findViewById(R.id.tv_jieti_count);
		Serializable extra = getIntent().getSerializableExtra("mark");
		item = (Star_Teacher_Parse) getIntent().getSerializableExtra("headpicture");
		if (extra != null && extra.equals("Fragment_My_Teacher")) {
			TextView action_settings = (TextView) findViewById(R.id.action_settings);
			action_settings.setText("我的老师");
		}
		if(item != null){
			myteather = item.getiSmyteath();
			teacher_nick.setText("用户名：" + item.getNickname());
			strdent_count.setText("学生数量:" + item.getFansNum());
			tv_subject.setText("学科：" + item.getSubjectid());
			tv_guan_zhu.setText(item.getInfo());
			imageLoader.displayImage(item.getIcon(), tudent_picture, options);
			tv_shike_fen.setText("接题次数：" + item.getClaim_question_num());
			tv_zan_shu.setText("学生赞数："+item.getLike_num());
		}
		tx_info_back.setOnClickListener(this);
		tv_attention.setOnClickListener(this);
		findViewById(R.id.tx_info_tiwen).setOnClickListener(this);
		if (myteather != null && myteather.equals("1")) {
			tv_attention.setText("取消关注");
			iv_xuan_zhong.setVisibility(View.VISIBLE);
			flag = true;
		} else {
			tv_attention.setText("关注");
			iv_xuan_zhong.setVisibility(View.GONE);
			flag = false;
		}
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tx_info_back:
			finish();
			break;
		case R.id.tv_attention:
			if (flag) {
				Teather_Info_QuXiao();
			} else {
				Teather_Info_Attention();
			}
			break;
		case R.id.tx_info_tiwen:
			if(iv_xuan_zhong.getVisibility()!= View.VISIBLE){
				Dialog.Common_Use(context);
			}else {
				PartnerConfig.TEATHER_ID = item.getUid();
				PartnerConfig.SUBJECT_ID = item.getSubjectid();
				PartnerConfig.TEATHER_NAME = item.getNickname();
				PartnerConfig.SUBJECT_NAME = item.getSubiect();
				Dialog.Intent(context, Activity_Tea_Tool_Sele.class);
			}
			break;
		}
	}
	// 取消老师 联网操作
	private void Teather_Info_Attention() {
		SKAsyncApiController.Attention_Taeather_Parse(item.getUid(),new MyAsyncHttpResponseHandler(this, true) {
					public void onSuccess(int arg0, String jion) {
						boolean success = SKResolveJsonUtil.getInstance().resolveIsSuccess(jion, context);
						if (success) {
							tv_attention.setText("取消关注");
							iv_xuan_zhong.setVisibility(View.VISIBLE);
							flag = true;
						}
					};
				});
	}
	// 关注老师 联网操作
	private void Teather_Info_QuXiao() {
		SKAsyncApiController.Qu_Xiao_GuanZhu(item.getUid(),new MyAsyncHttpResponseHandler(this, true) {
					public void onSuccess(String jion) {
						boolean success = SKResolveJsonUtil.getInstance().resolveIsSuccess(jion, context);
						if (success) {
							tv_attention.setText("关注");
							iv_xuan_zhong.setVisibility(View.GONE);
							flag = false;
						}
					};
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
