package com.yshow.shike.activities;
import com.umeng.analytics.MobclickAgent;
import com.yshow.shike.R;
import com.yshow.shike.entity.Question_Bank;
import com.yshow.shike.utils.Dialog;
import com.yshow.shike.utils.MyAsyncHttpResponseHandler;
import com.yshow.shike.utils.SKAsyncApiController;
import com.yshow.shike.utils.SKResolveJsonUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 某个题库页面点击确定后的页面,可以重命名题库,选择文件夹
 */
public class Activity_File_four extends Activity implements OnClickListener {
	private Context context;
	private EditText tv_timu_name;
	private TextView et_file_name;
	private String questionId;
	private Question_Bank bank;
	private String cid;
	private String file_name;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_file_four);
		context = this;
		initata();
	}

	private void initata() {
		tv_timu_name = (EditText) findViewById(R.id.tv_timu_name);
		et_file_name = (TextView) findViewById(R.id.et_file_name);
		ImageView iv_file_picture = (ImageView) findViewById(R.id.iv_file_picture);
//		rl_four_anim = (RelativeLayout) findViewById(R.id.rl_four_anim);
		findViewById(R.id.tv_four_back).setOnClickListener(this);
		findViewById(R.id.tv_four_comfing).setOnClickListener(this);
//		findViewById(R.id.iv_four_anim).setOnClickListener(this);
		tv_timu_name.setOnClickListener(this);
		et_file_name.setOnClickListener(this);
		TiMu_File_Name(tv_timu_name, et_file_name, iv_file_picture);
	}

	// 获取标题名和文件名
	private void TiMu_File_Name(EditText tv_timu_name, TextView et_file_name,
			ImageView iv_file_picture) {
		Bundle bundle = getIntent().getExtras();
		bank = (Question_Bank) bundle.getSerializable("Question_Bank");
		Bitmap bitmap = (Bitmap) bundle.getParcelable("bitmap");
		String name = bank.getName();
		String FileName = bank.getCategory1();
		if (name != null && FileName != null) {
			questionId = (String) bundle.get("questionId");
			cid = bank.getCid();
			tv_timu_name.setText(name);
			et_file_name.setText(FileName);
			iv_file_picture.setImageBitmap(bitmap);
		} else {
			String timu = tv_timu_name.getText().toString().trim();
			tv_timu_name.setText(timu);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_four_back:
			finish();
			break;
		case R.id.tv_four_comfing:
			if (TextUtils.isEmpty(et_file_name.getText())) {
				Toast.makeText(context, "请选择文件夹", 0).show();
			} else if (TextUtils.isEmpty(tv_timu_name.getText())) {
				Toast.makeText(context, "请输入标题", 0).show();
			} else {
				Mess_Save(questionId, cid, file_name);
			}
			break;
//		case R.id.iv_four_anim:
//			Animation animation = new Animation(this, rl_four_anim, -20, 20);
//			animation.initData(isUnfold);
//			isUnfold = !isUnfold;
//			break;
		case R.id.et_file_name:
			Intent intent = new Intent(context, Activity_Board_File.class);
			startActivityForResult(intent, 0);
			break;
		}
	}

	// 获取文件夹名称和cid
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 1) {
			file_name = data.getStringExtra("file_name");
			et_file_name.setText(file_name);
		}
	}

	// 访问接口 保存题库
	private void Mess_Save(String questionId, String name, String cid) {
		SKAsyncApiController.Save_Ques_Count(cid, questionId, name,
				new MyAsyncHttpResponseHandler(this, true) {
					@Override
					public void onSuccess(String json) {
						super.onSuccess(json);
						boolean success = SKResolveJsonUtil.getInstance().resolveIsSuccess(
								json, context);
						if (success) {
							Toast.makeText(context, "上传成功", 0).show();
							Dialog.Intent(context, Teather_Main_Activity.class);
						}
					}
				});
	}

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
