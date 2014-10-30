package com.yshow.shike.activities;
import android.text.TextUtils;
import com.umeng.analytics.MobclickAgent;
import com.yshow.shike.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yshow.shike.entity.LoginManage;
import com.yshow.shike.entity.User_Info;
import com.yshow.shike.fragments.Fragment_Student_GuanYu;
import com.yshow.shike.service.MySKService;
import com.yshow.shike.utils.Dialog;
import com.yshow.shike.utils.Dilog_Share;
import com.yshow.shike.utils.Exit_Login;
import com.yshow.shike.utils.MyAsyncHttpResponseHandler;
import com.yshow.shike.utils.Net_Servse;
import com.yshow.shike.utils.PartnerConfig;
import com.yshow.shike.utils.SKAsyncApiController;
import com.yshow.shike.utils.SKResolveJsonUtil;
import com.yshow.shike.utils.ShareDialog;
import com.yshow.shike.utils.WeixinManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * 学生端点击我的师课
 * @author Administrator
 */
public class Activity_MyShiKe extends Activity implements OnClickListener{
	private TextView tv_button_back;
	private TextView shengyu_jifen;
	private TextView tv_user_name;
	private TextView user_nickname;
	private TextView grade_name;
	private TextView benyue_tiwen;
	private TextView tv_guan_zhu;
	private ImageView tudent_picture;
	private Context context;
	private android.app.Dialog dialog;
	private Intent service; //学生端的一个消息服务
	private String mob ;//区分注册用户和非注册用户的标准  为空是体验用户
	private WeixinManager weixinManager;

    private static final int Edit_Info_Code = 7890;
	@Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_my_shike);
    	context = this;
    	initDta();
		service = new Intent(this, MySKService.class);
		startService(service);
    }
	@SuppressWarnings("static-access")
	private void initDta() {
//		nickName = UIApplication.getInstance().getNickName();
		weixinManager = new WeixinManager(Activity_MyShiKe.this);
		dialog = new Dilog_Share().Dilog_Anim(context, dialogListener);
		tv_button_back = (TextView) findViewById(R.id.tv_button_back);
		shengyu_jifen = (TextView) findViewById(R.id.tv_shengyu_jifen);
		tudent_picture = (ImageView) findViewById(R.id.tudent_picture);
		tv_user_name = (TextView) findViewById(R.id.tv_user_name);
		user_nickname = (TextView) findViewById(R.id.tv_user_nickname);
		grade_name = (TextView) findViewById(R.id.tv_grade_name);
		benyue_tiwen = (TextView) findViewById(R.id.tv_benyue_tiwen);
		tv_guan_zhu = (TextView) findViewById(R.id.tv_guan_zhu);
		findViewById(R.id.tv_sk_recg).setOnClickListener(this);
		findViewById(R.id.tv_info).setOnClickListener(this);
		findViewById(R.id.tv_acc_info).setOnClickListener(this);
		findViewById(R.id.tv_shre_frd).setOnClickListener(this);
		TextView my_shike_exit = (TextView)findViewById(R.id.tv_finsh_reg);
		my_shike_exit.setOnClickListener(this);
		findViewById(R.id.tv_about_sk).setOnClickListener(this);
		tv_button_back.setOnClickListener(this);
		mob = LoginManage.getInstance().getStudent().getMob();
		if(mob != null){
			my_shike_exit.setText("退        出");
            my_shike_exit.setVisibility(View.GONE);
			my_shike_exit.setTextColor(context.getResources().getColor(R.color.button_typeface_color));
			my_shike_exit.setBackgroundColor(context.getResources().getColor(R.color.sk_student_main_color));
		}else {
			my_shike_exit.setBackgroundColor(context.getResources().getColor(R.color.reg_finsh));
		}
	}
	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		// 充值按钮
		case R.id.tv_sk_recg:
			// 体验用户不能充值
			if(mob == null){
				Dialog.finsh_Reg_Dialog(context);
			}else {
				Dialog.Intent(context, Activity_Recharge.class);
			}
			break;
		case R.id.tv_info://个人信息
			if(mob == null){
				Dialog.finsh_Reg_Dialog(context);
			}else {
                Intent intent = new Intent(context, TeaPersonInfoActivity.class);
                startActivityForResult(intent, Edit_Info_Code);
			}
			break;	
		case R.id.tv_acc_info:
			if(mob == null){
				Dialog.finsh_Reg_Dialog(context);
			}else {
				Dialog.Intent(context,Activity_Student_Account_Message.class);
			}
			break;
		case R.id.tv_shre_frd:
			dialog.show();
			break;
		case R.id.tv_about_sk:
			Dialog.Intent(context, Fragment_Student_GuanYu.class);
			break;	
		case R.id.tv_finsh_reg:
			if(mob == null){
				Dialog.Intent(context,StudentRegisterActivity.class);
			}else {
				Exit_Login.getInLogin().Back_Login(context);
			}
			break;	
		case R.id.tv_button_back:
			finish();
			break;
		}
	}
	private void MyShiKeInfo(String uid){
		SKAsyncApiController.User_Info(uid, new MyAsyncHttpResponseHandler(this,true){
			public void onSuccess(int arg0, String json) {
				DisplayImageOptions options =Net_Servse.getInstence().Picture_Shipei(R.drawable.student_picture);
				ImageLoader imageLoader = ImageLoader.getInstance();
				User_Info my_teather = SKResolveJsonUtil.getInstance().My_teather(json);
				shengyu_jifen.setText("剩余学分："+my_teather.getPoints());
				tv_user_name.setText("姓名："+my_teather.getName());
				user_nickname.setText("用户名："+my_teather.getNickname());
				grade_name.setText("学龄段："+""+my_teather.getGrade()+""+my_teather.getGradeName());
                if(TextUtils.isEmpty(my_teather.getQuestions())){
                    benyue_tiwen.setText("本月提问：0");
                }else{
                    benyue_tiwen.setText("本月提问：" + my_teather.getQuestions());
                }
				if(my_teather.getInfo().equals("")){
					tv_guan_zhu.setText("很抱歉，我还没有填写自我介绍，失礼了！");
				}else {
					tv_guan_zhu.setText(my_teather.getInfo());
				}
				imageLoader.displayImage(my_teather.getPicurl(), tudent_picture, options);
			};
		}
	  );
	}
	/*
	 * dialog监听
	 */
	private OnClickListener dialogListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			// 微信分享
			case R.id.share_dialog_weixin:
				dialog.dismiss();
				weixinManager.shareWeixin();
				break;
			// 微信朋友圈分享
			case R.id.share_weixin_friend:
				dialog.dismiss();
				weixinManager.shareWeixinCircle();
				break;
			// 短信分享
			case R.id.share_dialog_message:
				ShareDialog.sendSMS(context,PartnerConfig.CONTEBT);
				dialog.dismiss();
				break;
			// e_mail分享
			case R.id.share_dialog_email:
				ShareDialog.openCLD(PartnerConfig.CONTEBT,context);
				dialog.dismiss();
				break;
			case R.id.share_dialog_cancle:
				dialog.dismiss();
				break;
			}
		}
	}; 
//			  停止消息服務
	@Override
	protected void onDestroy() {
	 super.onDestroy();
		if (service != null) {
				stopService(service);
			}
		}

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        String uid = LoginManage.getInstance().getStudent().getUid();
        if(uid != null){
            MyShiKeInfo(uid);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Edit_Info_Code) {
            if(resultCode==Activity.RESULT_OK){
                User_Info info = (User_Info) data.getSerializableExtra("info");
                tv_user_name.setText("姓名：" + info.getName());
                grade_name.setText("学龄段：" + "" + info.getGrade() + "" + info.getGradeName());
                tv_guan_zhu.setText(info.getInfo());
            }
        }
    }
}