package com.yshow.shike.activities;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.yshow.shike.R;
import com.yshow.shike.entity.LoginManage;
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

    /**
     * 是否从交互中过来的
     */
    private boolean isContine = false;
    /**
     * 从交互中过来的话,当前问题id
     */
    private String quesId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.stu_ask_step1_layout);
        isContine = getIntent().getBooleanExtra("isContinue", false);
        quesId = getIntent().getStringExtra("questionId");
        initData();
    }

    private void initData() {
        intence = Take_Phon_album.getIntence();
        findViewById(R.id.camera_btn).setOnClickListener(listener);
        findViewById(R.id.dcim_btn).setOnClickListener(listener);
        findViewById(R.id.paizhao_zhuyi).setOnClickListener(listener);
        findViewById(R.id.tv_tool_back).setOnClickListener(listener);
        if (isContine) {
            findViewById(R.id.action_step).setVisibility(View.GONE);
            TextView textview = (TextView) findViewById(R.id.action_text);
            if (LoginManage.getInstance().isTeacher()) {
                textview.setText("拍照回答");
            } else {
                textview.setText("拍照提问");
            }
        }

    }

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
                case R.id.paizhao_zhuyi:
                    Dialog.Intent(Activity_Stu_Ask_Step1.this, PaiZhaoHelpActivity.class);
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
                        bundle.putBoolean("isContinue", isContine);
                        bundle.putString("questionId", quesId);
                        if (isContine) {
                            Dialog.intent(context, Activity_Add_Remark.class, bundle);
                            finish();
                        } else {
                            Dialog.intent(context, Activity_Stu_Ask_Step2.class, bundle);
                            finish();
                        }
                    }
                } else if (requestCode == PHOTO_PIC && resultCode == -1) {
                    File cameraFile = new File(Environment.getExternalStorageDirectory().getPath(), "camera.jpg");
                    if (cameraFile.exists()) {
                        String bitmap_url = cameraFile.getAbsolutePath();
                        Bundle bundle = new Bundle();
                        bundle.putString("bitmap", bitmap_url);
                        bundle.putBoolean("isContinue", isContine);
                        bundle.putString("questionId", quesId);
                        if (isContine) {
                            Dialog.intent(context, Activity_Add_Remark.class, bundle);
                            finish();
                        } else {
                            Dialog.intent(context, Activity_Stu_Ask_Step2.class, bundle);
                            finish();
                        }
                    }
                }
            } else {
                Toast.makeText(context, "请检查你的SD卡是否存在", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
