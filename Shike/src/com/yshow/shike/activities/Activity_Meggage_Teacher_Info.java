package com.yshow.shike.activities;
import com.umeng.analytics.MobclickAgent;
import com.yshow.shike.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yshow.shike.entity.SKMessage;
import com.yshow.shike.entity.User_Info;
import com.yshow.shike.fragments.Fragment_Message;
import com.yshow.shike.service.MySKService;
import com.yshow.shike.utils.Dialog;
import com.yshow.shike.utils.MyAsyncHttpResponseHandler;
import com.yshow.shike.utils.Net_Servse;
import com.yshow.shike.utils.PartnerConfig;
import com.yshow.shike.utils.SKAsyncApiController;
import com.yshow.shike.utils.SKResolveJsonUtil;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
public class Activity_Meggage_Teacher_Info extends Activity implements
		OnClickListener {
	@SuppressWarnings("unused")
	private TextView tx_info_back;
	private TextView tv_attention;
	private Context context;
	private TextView jieti_count;
	private ImageView iv_xuan_zhong;
	private TextView tv_subject;
	private TextView tv_guan_zhu;
	private ImageView tudent_picture;
	private SKMessage extra;
	private TextView tx_info_back2;
	private TextView teacher_nick2;
	private TextView strdent_count2;
	private User_Info my_teather;
	private String uid;
	private TextView zan_coun;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_teacher_info);
		initDate();
	}
	private void initDate() {
		context = this;
		Bundle bundleExtra = getIntent().getExtras();
		extra = (SKMessage) bundleExtra.getSerializable("teather_sKMessage");
		if(!extra.getTeachUid().equals("0")){
			uid = extra.getTeachUid();
		}else {
			uid = extra.getClaim_uid();
		}
		MyShiKeInfo();
		tx_info_back2 = (TextView) findViewById(R.id.tx_info_back);
		teacher_nick2 = (TextView) findViewById(R.id.tv_teacher_nick);
		tv_subject = (TextView) findViewById(R.id.tv_subject);
		strdent_count2 = (TextView) findViewById(R.id.tv_strdent_count);
		tv_guan_zhu = (TextView) findViewById(R.id.tv_guan_zhu);
		tudent_picture = (ImageView) findViewById(R.id.tudent_picture);
		tv_attention = (TextView) findViewById(R.id.tv_attention);
		iv_xuan_zhong = (ImageView) findViewById(R.id.iv_xuan_zhong);
		jieti_count = (TextView) findViewById(R.id.tv_jieti_count);
		zan_coun = (TextView) findViewById(R.id.tv_zan_coun);
		findViewById(R.id.tx_info_tiwen).setOnClickListener(this);
		tx_info_back2.setOnClickListener(this);
		tv_attention.setOnClickListener(this);
		if(extra.getiSmyteach().equals("1")){
			iv_xuan_zhong.setVisibility(View.VISIBLE);
			tv_attention.setText("取消关注");
		}else {
			iv_xuan_zhong.setVisibility(View.GONE);
			tv_attention.setText("关注");
		}
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tx_info_back:
			finish();
			break;
		case R.id.tv_attention:
			if(iv_xuan_zhong.getVisibility() == View.VISIBLE){
				Teather_Info_QuXiao();
			}else {  
				Teather_Info_Attention();
			}
			break;
		case R.id.tx_info_tiwen:
			if(iv_xuan_zhong.getVisibility()!= View.VISIBLE){
				Dialog.Common_Use(context);
			}else {
				PartnerConfig.TEATHER_ID = my_teather.getUid();
				PartnerConfig.SUBJECT_ID = my_teather.getSubjectid();
				PartnerConfig.TEATHER_NAME = my_teather.getNickname();
				PartnerConfig.SUBJECT_NAME = my_teather.getSubject();
				Dialog.Intent(context, Teacher_Add_Tool.class);
			}
			break;
		}
	}
	private void Teather_Info_Attention() {
		SKAsyncApiController.Attention_Taeather_Parse(uid,
				new MyAsyncHttpResponseHandler(this, true) {
					public void onSuccess(int arg0, String jion) {
						boolean success = SKResolveJsonUtil.getInstance().resolveIsSuccess(jion, context);
						if (success) {
							tv_attention.setText("取消关注");
							iv_xuan_zhong.setVisibility(View.VISIBLE);
							Fragment_Message.handler.sendEmptyMessage(MySKService.HAVE_NEW_MESSAGE);
						}
					};
				});
	}
	// 关注老师 联网操作
	private void Teather_Info_QuXiao() {
		SKAsyncApiController.Qu_Xiao_GuanZhu(uid,new MyAsyncHttpResponseHandler(this, true) {
					public void onSuccess(String jion) {
						boolean success = SKResolveJsonUtil.getInstance().resolveIsSuccess(jion, context);
						if (success) {
							tv_attention.setText("关注");
							iv_xuan_zhong.setVisibility(View.GONE);
							Fragment_Message.handler.sendEmptyMessage(MySKService.HAVE_NEW_MESSAGE);
						}
					};
				});
	}
	private void MyShiKeInfo() {
		SKAsyncApiController.User_Info(uid,new MyAsyncHttpResponseHandler(this, true) {
					public void onSuccess(int arg0, String json) {
						boolean atent_Success = SKResolveJsonUtil.getInstance().resolveIsSuccess(json, context);
						if(atent_Success){
							DisplayImageOptions options =  Net_Servse.getInstence().Picture_Shipei(R.drawable.my_tea_phon);
							ImageLoader imageLoader = ImageLoader.getInstance();
							my_teather = SKResolveJsonUtil.getInstance().My_teather1(json);
							teacher_nick2.setText("用户名  ："+ my_teather.getNickname());
							strdent_count2.setText("学生数量  ：" + my_teather.getFansNum());
							tv_subject.setText("学科  ：" + my_teather.getSubject());
							tv_guan_zhu.setText(my_teather.getInfo());
							jieti_count.setText("接题次数 :"+my_teather.getClaim_question_num());
							zan_coun.setText("学生攒美 :"+my_teather.getLike_num());
							imageLoader.displayImage(my_teather.getPicurl(),tudent_picture, options);
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
