package com.yshow.shike.activities;

import java.io.File;
import java.io.Serializable;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.umeng.analytics.MobclickAgent;
import com.yshow.shike.R;
import com.yshow.shike.entity.Question_Bank;
import com.yshow.shike.utils.*;
import com.yshow.shike.utils.ImageUtil.ScalingLogic;

/**
 * 老师端进入老师端工具的控制/学生端从老师页面进入提问/老师端题库页面点击添加以后进入的制作题目页面
 * 
 * @author Administrator
 */
public class Activity_Tea_Tool_Sele extends Activity {
	private Context context;
	private boolean booleanExtra;
	private String questionId;
	private ImageView take_img;// 拍照的按钮
	private android.app.Dialog stu_Take_Phon;
	// 相机回调
	private final int PHOTO_PIC = 10001;
	// 相册回调
	private final int ALBUM_PIC = 10002;
	private Take_Phon_album phone_iamge;

    boolean is_tea;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		setContentView(R.layout.tea_take_phon);
        Intent intent2 = getIntent();
        booleanExtra = intent2.getBooleanExtra("isContinue", false);
        questionId = intent2.getStringExtra("questionId");
        initData();
        phone_iamge = Take_Phon_album.getIntence();
    }

	private void initData() {
		stu_Take_Phon = Dilog_Share.Stu_Take_Phon(context, listener);
		findViewById(R.id.tea_button_back).setOnClickListener(listener);
		take_img = (ImageView) findViewById(R.id.tea_take_img);
		take_img.setOnClickListener(listener);
		TextView paizao_seting = (TextView) findViewById(R.id.action_settings);
        is_tea = new FileService(context).getBoolean("is_tea", true);
        if (!is_tea) {
			take_img.setImageResource(R.drawable.pai_zhao);
			paizao_seting.setText("提问");
			findViewById(R.id.li_take_phon_back).setBackgroundResource(
					R.color.sk_student_main_color);
		}else{
            if(booleanExtra){
                paizao_seting.setText("拍照回答");
            }
        }
	}

	private OnClickListener listener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.tea_button_back:
				finish();
				break;
			case R.id.tea_take_img:
				stu_Take_Phon.show();
				break;
			case R.id.tv_pai_zhao:
				phone_iamge.startImageCapture(Activity_Tea_Tool_Sele.this,
						PHOTO_PIC);
				stu_Take_Phon.dismiss();
				break;
			case R.id.tv_xiagnc:
				phone_iamge.gotoSysPic(Activity_Tea_Tool_Sele.this, ALBUM_PIC);
				stu_Take_Phon.dismiss();
				break;
			case R.id.tv_tea_cancel:
				stu_Take_Phon.dismiss();
				break;
			}
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == -1) {
			if (requestCode == ALBUM_PIC) {
				if (data != null) {
					String bitmap_url = phone_iamge.uriToPath(Activity_Tea_Tool_Sele.this, data.getData());
					//图片压缩
                    //这里压缩了会导致图片横过来,无法全屏.--徐斌.2014.8.4
//					Bitmap decodeFile = ImageUtil.decodeFile(bitmap_url, 640,640, ScalingLogic.FIT);
//					ImageUtil.writeBitmap(decodeFile, bitmap_url);
					Bundle bundle = new Bundle();
					bundle.putString("bitmap", bitmap_url);
					bundle.putBoolean("isContinue", booleanExtra);
					bundle.putString("questionId", questionId);
                    if(booleanExtra){//如果是接着提问
                        Intent it = new Intent(context, Stu_Paint_Activity.class);
                        it.putExtras(bundle);
                        startActivityForResult(it,1);
                    }else{
                        Dialog.intent(context, Stu_Paint_Activity.class, bundle);
                        finish();
                    }
                }
			}
			if (requestCode == PHOTO_PIC) {
				File cameraFile = new File(Environment.getExternalStorageDirectory().getPath(), "camera.jpg");
				if (cameraFile.exists()) {
					String bitmap = cameraFile.getAbsolutePath();
					//图片压缩
                    //这里压缩了会导致图片横过来,无法全屏.--徐斌.2014.8.4
//					Bitmap decodeFile = ImageUtil.decodeFile(bitmap, 640, 640,ScalingLogic.FIT);
//					ImageUtil.writeBitmap(decodeFile, bitmap);
					Bundle bundle = new Bundle();
					bundle.putBoolean("isContinue", booleanExtra);
					bundle.putString("questionId", questionId);
					bundle.putString("bitmap", bitmap);
                    if(booleanExtra){//如果是接着提问
                        Intent it = new Intent(context, Stu_Paint_Activity.class);
                        it.putExtras(bundle);
                        startActivityForResult(it,1);
                    }else{
                        Dialog.intent(context, Stu_Paint_Activity.class, bundle);
                        finish();
                    }
				}
			}
            if(requestCode==1&&resultCode==Activity.RESULT_OK){
                setResult(Activity.RESULT_OK);
                finish();
            }
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

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
