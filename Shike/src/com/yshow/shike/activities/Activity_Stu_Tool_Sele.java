package com.yshow.shike.activities;
import java.io.File;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;
import com.umeng.analytics.MobclickAgent;
import com.yshow.shike.R;
import com.yshow.shike.entity.LoginManage;
import com.yshow.shike.utils.Dialog;
import com.yshow.shike.utils.Dilog_Share;
import com.yshow.shike.utils.HelpUtil;
import com.yshow.shike.utils.Take_Phon_album;

/**
 * 学生点击提问以后进入的页面,显示照相机背景,点击弹框选择照相还是相册
 */
public class Activity_Stu_Tool_Sele extends Activity {
	private Context context;
	private android.app.Dialog stu_Take_Phon;
	// 相册回调
	private final int ALBUM_PIC = 10001;
	// 相机回调
	private final int PHOTO_PIC = 10002;
	private Take_Phon_album intence;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		setContentView(R.layout.take_phon_imag);
		stu_Take_Phon = Dilog_Share.Stu_Take_Phon(context, listener);
		initData();
	}
	private void initData() {
		intence = Take_Phon_album.getIntence();
		// YD.getInstence().setYD(listener,context,
		// STU_YD2,findViewById(R.id.stu_take_phone));
		findViewById(R.id.iv_take_phon).setOnClickListener(listener);
		findViewById(R.id.tv_tool_back).setOnClickListener(listener);

	}

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            HelpUtil.showHelp(this,HelpUtil.HELP_STU_2,findViewById(R.id.rootview));
        }
    }

	private OnClickListener listener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.iv_take_phon:
				stu_Take_Phon.show();
				break;
			case R.id.tv_pai_zhao:
				intence.startImageCapture(Activity_Stu_Tool_Sele.this,PHOTO_PIC);
				stu_Take_Phon.dismiss();
				break;
			case R.id.tv_xiagnc:
				intence.gotoSysPic(Activity_Stu_Tool_Sele.this,ALBUM_PIC);
				stu_Take_Phon.dismiss();
				break;
			case R.id.tv_tea_cancel:
				stu_Take_Phon.dismiss();
				break;
			case R.id.tv_tool_back:
				finish();
				break;
			}
		}
	};
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == -1) {
			if (Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
			if (requestCode == ALBUM_PIC) {
				if (data != null && resultCode == -1) {
					String bitmap_url = intence.uriToPath(Activity_Stu_Tool_Sele.this, data.getData());
					Bundle bundle = new Bundle();
					bundle.putString("bitmap", bitmap_url);
					Dialog.intent(context, Stu_Paint_Activity.class, bundle);
					finish();
				}
			} else if (requestCode == PHOTO_PIC && resultCode == -1) {
				File cameraFile = new File(Environment.getExternalStorageDirectory().getPath(), "camera.jpg");
				if (cameraFile.exists()) {
					String bitmap_url = cameraFile.getAbsolutePath();
					Bundle bundle = new Bundle();
					bundle.putString("bitmap", bitmap_url);
					Dialog.intent(context, Stu_Paint_Activity.class, bundle);
					finish();
				}
			}
	      }else {
			Toast.makeText(context,"请检查你的SD卡是否存在", Toast.LENGTH_SHORT).show();
		 }
	 }
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
