package com.yshow.shike.activities;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.yshow.shike.R;
import com.yshow.shike.utils.Dialog;
import com.yshow.shike.utils.Take_Phon_album;

/**
 * 学生点击提问以后进入的页面,选择拍照还是从相册选择
 */
public class Activity_Stu_Ask_Step1 extends BaseActivity {
    private Context context;
    // 相册回调
    private final int ALBUM_PIC = 10001;
    // 相机回调
    private final int PHOTO_PIC = 10002;
    private Take_Phon_album intence;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.stu_ask_step1_layout);
        initData();
    }

    private void initData() {
        intence = Take_Phon_album.getIntence();
        findViewById(R.id.camera_btn).setOnClickListener(listener);
        findViewById(R.id.dcim_btn).setOnClickListener(listener);
        findViewById(R.id.tv_tool_back).setOnClickListener(listener);

    }

//    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//        super.onWindowFocusChanged(hasFocus);
//        if (hasFocus) {
//            HelpUtil.showHelp(this,HelpUtil.HELP_STU_2,findViewById(R.id.rootview));
//        }
//    }

    private OnClickListener listener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.camera_btn:
                    intence.startImageCapture(Activity_Stu_Ask_Step1.this, PHOTO_PIC);
                    break;
                case R.id.dcim_btn:
                    intence.gotoSysPic(Activity_Stu_Ask_Step1.this, ALBUM_PIC);
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
                        String bitmap_url = intence.uriToPath(Activity_Stu_Ask_Step1.this, data.getData());
                        Bundle bundle = new Bundle();
                        bundle.putString("bitmap", bitmap_url);
                        Dialog.intent(context, Activity_Stu_Ask_Step2.class, bundle);
                        finish();
                    }
                } else if (requestCode == PHOTO_PIC && resultCode == -1) {
                    File cameraFile = new File(Environment.getExternalStorageDirectory().getPath(), "camera.jpg");
                    if (cameraFile.exists()) {
                        String bitmap_url = cameraFile.getAbsolutePath();
                        Bundle bundle = new Bundle();
                        bundle.putString("bitmap", bitmap_url);
                        Dialog.intent(context, Activity_Stu_Ask_Step2.class, bundle);
                        finish();
                    }
                }
            } else {
                Toast.makeText(context, "请检查你的SD卡是否存在", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
