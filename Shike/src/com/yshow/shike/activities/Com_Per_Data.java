package com.yshow.shike.activities;
import java.util.ArrayList;
import java.util.List;

import com.umeng.analytics.MobclickAgent;
import com.yshow.shike.R;
import com.yshow.shike.UIApplication;
import com.yshow.shike.entity.SKGrade;
import com.yshow.shike.entity.SkUpLoadQuestion;
import com.yshow.shike.utils.Dialog;
import com.yshow.shike.utils.FileService;
import com.yshow.shike.utils.GradeSeltorUtil;
import com.yshow.shike.utils.MyAsyncHttpResponseHandler;
import com.yshow.shike.utils.SKAsyncApiController;
import com.yshow.shike.utils.SKResolveJsonUtil;
import com.yshow.shike.utils.ScreenSizeUtil;
import com.yshow.shike.utils.GradeSeltorUtil.SystemDialogButtonOnclickListening;
import com.yshow.shike.utils.Send_Message;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
public class Com_Per_Data extends Activity implements OnClickListener{
	private EditText noreg_name;  //  用户名
	private TextView no_reg; // 学龄段
	private String gradeId ; // 年级的id
	private Context context ;// 上下文
	private String subjectid ;// 获科目id
	private Bitmap bitmap ; // 获取图片
	private ArrayList<String> urllist ;// 获取语音的路径
	private SkUpLoadQuestion skUpLoadQuestion ; //用来封装语音路径和图片
	private boolean success; // 提交个人信息成功的一个标记
	private String user_name ;
	private Send_Message send_Message ; //创建发送消息对象
      @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.com_per_data);
    	context = this;
    	InitData();
    }
	@SuppressWarnings("unchecked")
	private void InitData() {
		Bundle bundle = getIntent().getExtras();
		subjectid = (String) bundle.get("subjectid");
		urllist = (ArrayList<String>) bundle.get("urllist");
		List<String> picUrls = UIApplication.getInstance().getPicUrls();
        int screenWidth=ScreenSizeUtil.ScreenWidth;
        int screenHeight=ScreenSizeUtil.ScreenHeight;
		bitmap = Dialog.getbitmap(picUrls.get(0), screenWidth, screenHeight);
	    skUpLoadQuestion = new SkUpLoadQuestion(bitmap, urllist);
		findViewById(R.id.tv_per_skback).setOnClickListener(this);
		findViewById(R.id.tv_per_comg).setOnClickListener(this);
		no_reg = (TextView) findViewById(R.id.tv_no_reg);
		noreg_name = (EditText) findViewById(R.id.tv_noreg_name);
		no_reg.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
	 // 返回键
		case R.id.tv_per_skback:
			finish();
			break;
		// 确定键
		case R.id.tv_per_comg:
			if(success){
				send_Message = new Send_Message(context, bitmap, skUpLoadQuestion, "0");
				send_Message.skCreateQuestion(subjectid);
			}else {
				Toast.makeText(Com_Per_Data.this,"请补充个人信息", 0).show();
			}
			break;
		case R.id.tv_no_reg:
			getGrade();
			break;
		}
	}
	/**
	 * 获取年纪地区    获取个人的用户名和年龄段
	 */
	private void Commit_Per_Info() {
		user_name = noreg_name.getText().toString().trim();
		if(!TextUtils.isEmpty(user_name) || !(TextUtils.isEmpty(gradeId))){
			  Auod_Log(user_name,gradeId);
			}
	}
	/**
	 * 获取年纪地区
	 */
	private void getGrade() {
		SKAsyncApiController.skGetGrade(new MyAsyncHttpResponseHandler(this,true) {
			@Override
			public void onSuccess(String json) {
				super.onSuccess(json);
				boolean success = SKResolveJsonUtil.getInstance().resolveIsSuccess(json,context);
				if(success){
					// 访问网路 成功
					ArrayList<SKGrade> resolveGrade = SKResolveJsonUtil.getInstance().resolveGrade(json);
					final GradeSeltorUtil systemDialog = new GradeSeltorUtil(context, resolveGrade);
					systemDialog.setLeftButtonText("完成");
					systemDialog.show();
					systemDialog.setSystemDialogButtonOnclickListening(new SystemDialogButtonOnclickListening() {
						@Override
						public void onClickRight() {}
						@Override
						public void onClickLeft() {
							gradeId = systemDialog.getGradeId();
							no_reg.setText(systemDialog.getSeltotText());
							Commit_Per_Info();
						}
					});
				}
			}
		});
	}
	/**
	 * 用户自动登录  访问接口拿到用户名和年龄段
	 */
	private void Auod_Log(final String nickname,final String gradeId){
		SKAsyncApiController.auto_Info(nickname,gradeId,new MyAsyncHttpResponseHandler(this,true){
			@Override
			public void onSuccess(String json) {
				super.onSuccess(json);
				success = SKResolveJsonUtil.getInstance().resolveIsSuccess(json, Com_Per_Data.this);
				if(success){
					FileService auto_sp = new FileService(context);
					auto_sp.set_auto_info(user_name,null);
				}
			}
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
