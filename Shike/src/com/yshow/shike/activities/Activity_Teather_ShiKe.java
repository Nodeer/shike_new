package com.yshow.shike.activities;
import java.lang.reflect.Type;

import com.umeng.analytics.MobclickAgent;
import com.yshow.shike.R;
import com.yshow.shike.UIApplication;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yshow.shike.entity.LoginManage;
import com.yshow.shike.entity.UpLoad_Image;
import com.yshow.shike.entity.User_Info;
import com.yshow.shike.entity.UpLoad_Image.Upload_Filed;
import com.yshow.shike.utils.Dialog;
import com.yshow.shike.utils.Dilog_Share;
import com.yshow.shike.utils.MyAsyncHttpResponseHandler;
import com.yshow.shike.utils.SKAsyncApiController;
import com.yshow.shike.utils.SKResolveJsonUtil;
import com.yshow.shike.utils.Take_Phon_album;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 老师端点击我的师课以后进入的页面
 */
public class Activity_Teather_ShiKe extends Activity implements OnClickListener{
	private ImageView teather_picture;
	private TextView tv_teather_nickname;
	private TextView tv_teather_name;
	private TextView tv_teather_jifen;
	private TextView tv_teather_zhu;
	private TextView tv_shike_fen,tv_jieti ; // 时刻分 接题次数文本
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	private String user_name;
	private android.app.Dialog stu_Take_Phon;
	private final int TAKE_PICTURE = 1;
	private final int TAKE_PHONE = 2;
	private final int SETTO_IMAGEVIEW = 4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_teather_shike);
    	initDta();
    }
	private void initDta() {
		stu_Take_Phon = Dilog_Share.Stu_Take_Phon(this, this);
		options = new DisplayImageOptions.Builder().cacheOnDisc(true).showImageForEmptyUri(R.drawable.teather_picture).showImageOnFail(R.drawable.teather_picture).cacheInMemory(true).build();
		imageLoader = ImageLoader.getInstance();
		teather_picture = (ImageView) findViewById(R.id.teather_stu_picture);
		tv_teather_nickname = (TextView) findViewById(R.id.tv_teather_nickname);
		tv_teather_name = (TextView) findViewById(R.id.tv_teather_name);
		tv_teather_jifen = (TextView) findViewById(R.id.tv_teather_jifen);
		tv_teather_zhu = (TextView) findViewById(R.id.tv_teather_zhu);
		tv_shike_fen = (TextView) findViewById(R.id.tv_shike_fen);
		tv_jieti = (TextView) findViewById(R.id.tv_jieti);
		teather_picture.setOnClickListener(this);
		findViewById(R.id.tv_tea_skback).setOnClickListener(this);
		findViewById(R.id.tv_tea_seting).setOnClickListener(this);
		findViewById(R.id.tv_tea_prize).setOnClickListener(this);
		findViewById(R.id.tv_tea_cash).setOnClickListener(this);
	}



	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		//兑换奖品
		case R.id.tv_tea_prize:
			Dialog.Intent(this,TeacherExchangeGodsActivity.class);
		break;
		//兑换现金
		case R.id.tv_tea_cash:
			Dialog.Intent(this,TeacherExchangeMoneyActivity.class);
		    break;
		// 设置按钮
		case R.id.tv_tea_seting:
			UIApplication.getInstance().setUser_name(user_name);
			Dialog.Intent(this, Activity_My_SkSeting.class);
			break;
		case R.id.tv_tea_skback:
			finish();
			break;
		case R.id.teather_stu_picture:
			stu_Take_Phon.show();
			break;
			 // 拍照
		case R.id.tv_pai_zhao:
            Take_Phon_album.getIntence().Take_Phone(TAKE_PHONE,Activity_Teather_ShiKe.this);
			 stu_Take_Phon.dismiss();
			break;
			// 相册
			case R.id.tv_xiagnc:
				Take_Phon_album.getIntence().Take_Pickture(TAKE_PICTURE, Activity_Teather_ShiKe.this);
				stu_Take_Phon.dismiss();
				break;
			// 取消
			case R.id.tv_tea_cancel:
				stu_Take_Phon.dismiss();
				break;
		}
	}
	private User_Info my_teather;
	private void MyShiKeInfo(String uid){
		SKAsyncApiController.User_Info(uid, new MyAsyncHttpResponseHandler(this,true){
			public void onSuccess(String json) {
                super.onSuccess(json);
				my_teather = SKResolveJsonUtil.getInstance().My_teather(json);
				user_name = my_teather.getNickname();
				tv_teather_nickname.setText("用户名："+user_name);
				tv_teather_name.setText("学科："+my_teather.getSubject());
				tv_teather_jifen.setText("学分："+my_teather.getPoints());
				tv_teather_zhu.setText(my_teather.getInfo());
				tv_shike_fen.setText(my_teather.getLike_num());
				tv_jieti.setText(my_teather.getClaim_question_num());
				imageLoader.displayImage(my_teather.getPicurl(), teather_picture, options);
			};
		}
	);
  }
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_OK){
			Bundle bundle = data.getExtras();
			String time_make = System.currentTimeMillis()+"";
		switch (requestCode) {
			case SETTO_IMAGEVIEW:
				Bitmap bitmap = (Bitmap) bundle.getParcelable("data");
				Bitmap newbm = Dialog.scaleImg(bitmap, 400, 400);
				teather_picture.setImageBitmap(newbm);
				Reg_Imag(Activity_Teather_ShiKe.this, newbm, time_make);
			break;
		   case TAKE_PICTURE:
			Take_Phon_album.getIntence().startPhotoZoom(Activity_Teather_ShiKe.this,data.getData(),SETTO_IMAGEVIEW);
			break;
		   case TAKE_PHONE:
			   Bitmap bit_map = (Bitmap) bundle.getParcelable("data");
			   Bitmap new_bit_map = Dialog.scaleImg(bit_map, 400, 400);
			   teather_picture.setImageBitmap(new_bit_map);
			   Reg_Imag(Activity_Teather_ShiKe.this, new_bit_map, time_make);
			break;
		}
	   }
	}
	/**
	 *上传图片
	 * @param bitmap
	 * @param path
	 * @return 
	 */
	public void Reg_Imag(final Context context,Bitmap bitmap,String path) {
		SKAsyncApiController.Up_Loading_Tea(bitmap,path,new MyAsyncHttpResponseHandler(context, true){
		    @Override
		    public void onSuccess(String json) {
		    	super.onSuccess(json);
		    	Type tp = new TypeToken<UpLoad_Image>() {}.getType();
				Gson gs = new Gson();
				UpLoad_Image up_image = gs.fromJson(json, tp);
				Upload_Filed data = up_image.getData();
				my_teather.setIcon(data.getFileId());
				save_icon(my_teather);
		    }
		});
	}
	private void save_icon(User_Info user_Info){
		SKAsyncApiController.save_teask_icon(user_Info, new AsyncHttpResponseHandler(){
			@Override
			public void onSuccess(String json) {
				super.onSuccess(json);
				SKResolveJsonUtil.getInstance().resolveIsSuccess(json, Activity_Teather_ShiKe.this);
			}
		});
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
}
