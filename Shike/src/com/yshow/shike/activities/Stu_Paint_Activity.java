package com.yshow.shike.activities;
import android.util.DisplayMetrics;
import com.umeng.analytics.MobclickAgent;
import com.yshow.shike.entity.Question_Bank;
import com.yshow.shike.utils.*;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.yshow.shike.R;
import com.yshow.shike.UIApplication;
import com.yshow.shike.customview.PaletteView;
import com.yshow.shike.entity.LoginManage;
import com.yshow.shike.fragments.Fragment_Message;
import com.yshow.shike.service.MySKService;

import java.io.Serializable;

/**
 * 老师题库页面,点添加以后进入/学生提问时,拍照或者选择照片以后进入..
 * 反正就是在图片上绘制的页面
 */
public class Stu_Paint_Activity extends Activity implements OnClickListener {
	private Context context;
	private PaletteView paletteView = null; // 初始化画笔
	private LinearLayout ll_title, buttom_plain;
	private ProgressDialogUtil dialogUtil;// 连网精度条
	private TextView pain_huifu;
	private TextView next_tool;
	private Bitmap bitmap;
	private Bundle extras;
	private String questionId;
	private boolean booleanExtra;
	private String bigBitmapUrl;
	private Bitmap_Manger_utils bitmap_intence;
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				if (extras != null) {
					if (booleanExtra && !questionId.equals("")) {//如果是从制作题目进来的话,这两个是进不去的.直接发送图片地址到下一个页面
						skUploadeImage(questionId, bitmap);
						dialogUtil.show();
						return;
					}
				}
				dialogUtil.dismiss();
                Bundle bun = new Bundle();
                bun.putParcelable("bitmap",bitmap);
				Dialog.intent(context, Activity_My_Board.class, bun);
				finish();
				break;
			}
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stu_paint_activity);
		context = this;
		initData();
		Tea_Stu();
		dialogUtil = new ProgressDialogUtil(this);
	}
	private void initData() {
		bitmap_intence = Bitmap_Manger_utils.getIntence();
		paletteView = (PaletteView) findViewById(R.id.stu_palette);
		extras = getIntent().getExtras();
        if(extras!=null){//这个地方..有的页面没有传东西过来...做个空保护
            questionId = extras.getString("questionId");
            booleanExtra = extras.getBoolean("isContinue");
        }
        next_tool = (TextView) findViewById(R.id.tv_stu_nextplan);
        if (booleanExtra && !questionId.equals("")) {
			next_tool.setText("发送");
		}
		ll_title = (LinearLayout) findViewById(R.id.li_stu_plain);
		buttom_plain = (LinearLayout) findViewById(R.id.buttom_plain);
		findViewById(R.id.tv_stu_planback).setOnClickListener(this);
		next_tool.setOnClickListener(this);
		findViewById(R.id.plan_cexiao).setOnClickListener(this);
		pain_huifu = (TextView) findViewById(R.id.pain_huifu);
		pain_huifu.setOnClickListener(this);
		paletteView.setCurrentColor(Color.RED);
		// 是否是 照相还是 从相册里面拿图片 进行判断
		String path  = null;
        if(extras!=null){
            path = getIntent().getExtras().getString("bitmap");
        }
        if (path != null) {
			Bitmap press_bitmap = press_bitmap(path);
			int pbw=press_bitmap.getWidth();
			int pbh=press_bitmap.getHeight();
			ScreenSizeUtil.ScreenWidth=pbw;
			ScreenSizeUtil.ScreenHeight=pbh;
			paletteView.setBgBitmap1(press_bitmap,path);
		}
	}
	private Bitmap press_bitmap(String path) {
		// 手机屏幕的宽
		int screenWidth = ScreenSizeUtil.getScreenWidth(this, 1);
		Options op = new BitmapFactory.Options();
		op.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, op);
		int op_h = op.outHeight;
		int op_w = op.outWidth;
		int h = 0;
		int rotateDegree = bitmap_intence.readPictureDegree(path);
		if(rotateDegree == 90){
			h = screenWidth * op_w / op_h;
		}else {
			h = screenWidth * op_h / op_w;
		}
		LayoutParams p = new LayoutParams(LayoutParams.FILL_PARENT,h);
		paletteView.setLayoutParams(p);
		if(rotateDegree == 90){
			op.inSampleSize = op_h / screenWidth;
		}else {
			op.inSampleSize = op_w / screenWidth;
		}
		op.inSampleSize++;
		op.inJustDecodeBounds = false;
//		op.inSampleSize = op_w/screenWidth;
        op.inDensity = DisplayMetrics.DENSITY_DEFAULT;
        op.inTargetDensity = DisplayMetrics.DENSITY_DEFAULT;
		Bitmap getbitmap = BitmapFactory.decodeFile(path, op).copy(Config.RGB_565, true);
		while(getbitmap.getHeight() * getbitmap.getRowBytes() > Runtime.getRuntime().maxMemory()/8){
			op.inSampleSize++;
			getbitmap.recycle();
			getbitmap = BitmapFactory.decodeFile(path, op);
		}
		//对图片进行旋转
		Bitmap	new_rotae_bitmap = bitmap_intence.rotaingImageView(rotateDegree, getbitmap);
		return new_rotae_bitmap;
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_stu_planback:
			finish();
			break;
		case R.id.tv_stu_nextplan:
            dialogUtil.show();
			new Thread(){
				public void run() {
					//查看华过后的原图
					bigBitmapUrl = paletteView.getBigBitmapUrl();
					UIApplication.getInstance().addPicUrls(bigBitmapUrl);
					// 预览图片
					bitmap = paletteView.getNewBitmap();
					int pbw=bitmap.getWidth();
					int pbh=bitmap.getHeight();
					handler.sendEmptyMessage(0);
				};
			}.start();
			break;
		// 删除画板上的数据
		case R.id.plan_cexiao:
			paletteView.Delete();
			break;
		// 恢复画板上的数据
		case R.id.pain_huifu:
			paletteView.Recover();
			break;
		}
	}
	private void skUploadeImage(final String questionId, Bitmap bitmap) {
		// 改成 上传图片 改成原图的url了
		int screenWidth = ScreenSizeUtil.getScreenWidth(this, 1);
		int screenHeight = ScreenSizeUtil.getScreenHeight(this);
		screenWidth=ScreenSizeUtil.ScreenWidth;
		screenHeight=ScreenSizeUtil.ScreenHeight;
		Bitmap getbitmap = Dialog.getbitmap(bigBitmapUrl, screenWidth,screenHeight);
		SKAsyncApiController.skUploadImage(questionId, getbitmap,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(String json) {
						super.onSuccess(json);
						dialogUtil.dismiss();
						boolean resolveIsSuccess = SKResolveJsonUtil.getInstance().resolveIsSuccess(json, context);
						if (resolveIsSuccess) {
							skSend_messge(questionId, "0");
							dialogUtil.show();
						}
					}
				});
	}
	// 判断是老师登陆还是学生登陆
	private void Tea_Stu() {
		if (LoginManage.getInstance().isTeacher(this)) {
			ll_title.setBackgroundResource(R.color.bottom_widow_color);
            buttom_plain.setBackgroundResource(R.color.tea_add_remark_lucency);
        }
	}

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            if (LoginManage.getInstance().isTeacher(this)) {
                HelpUtil.showHelp(Stu_Paint_Activity.this, HelpUtil.HELP_TEA_6, findViewById(R.id.rl_stu_remark));
            } else {
                HelpUtil.showHelp(Stu_Paint_Activity.this, HelpUtil.HELP_STU_3, findViewById(R.id.rl_stu_remark));
            }
        }
    }

    private void skSend_messge(final String questionId, String isSend) {
		SKAsyncApiController.skSend_messge(questionId, "", isSend,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(String content) {
						super.onSuccess(content);
						dialogUtil.dismiss();
						String error = null;
						try {
							JSONObject jsonObject = new JSONObject(content);
							int optBoolean = jsonObject.optInt("success");
							if (optBoolean == 1) {
								String data = jsonObject.optString("data");
								String type = jsonObject.optString("type");
								if (type.equals("confirm")) {
									AlertDialog.Builder dia = new Builder(context);
									dia.setMessage(data);
									dia.setNegativeButton("取消", null);
									dia.setPositiveButton(
											"确定",
											new DialogInterface.OnClickListener() {
												@Override
												public void onClick(DialogInterface dialog,int which) {
													skSend_messge(questionId,"1");
													finish();
												}
											});
									dia.show();
								} else {
//									Fragment_Message.handler.sendEmptyMessage(MySKService.HAVE_NEW_MESSAGE);
                                    setResult(Activity.RESULT_OK);
									finish();
								}
							} else {
								error = jsonObject.optString("error");
								Toast.makeText(context, error, 0).show();
							}
						} catch (JSONException e) {
							Toast.makeText(context, "服务异常", 0).show();
						}
					}
				});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (bitmap != null && !bitmap.isRecycled()) {
			bitmap.recycle();
			bitmap = null;
		}
		System.gc();
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