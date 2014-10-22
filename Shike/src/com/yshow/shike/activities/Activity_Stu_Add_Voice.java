package com.yshow.shike.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateFormat;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yshow.shike.R;
import com.yshow.shike.UIApplication;
import com.yshow.shike.entity.LoginManage;
import com.yshow.shike.utils.Dialog;
import com.yshow.shike.utils.MediaPlayerUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 学生涂鸦完成后,进入的添加语音页面
 */
public class Activity_Stu_Add_Voice extends BaseActivity implements OnClickListener {
    private Button mRecordButton;// 录音键
    private ImageView iv_board_picture; // 被画图片
    private Bitmap bitmap;
    private MediaRecorder mr;
    private int Count = 0;
    //    private ImageView iv_record1, dele_record1;
//    private ImageView iv_record2, dele_record2;
//    private ImageView iv_record3, dele_record3;
    private ArrayList<String> urllist;
    private View voiceRecorddingLayout;
    private TextView recordTimeRemainText;
    private int recLen = 0;
    private Timer timer = null;
    private Button delVoice;
    private File file;
    private String url;
    private boolean dele_mes_flag = true;
    private Boolean dele_list = false;
    private MediaPlayerUtil mediaPlayerUtil;
    final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    recordTimeRemainText.setText("录音剩余时间：" + recLen);
                    if (recLen < 0) {
                        mr.release();
                        timer.cancel();
                        task.cancel();
                        recordTimeRemainText.setText("录音剩余时间：" + 0);
                        recLen = 1;
                    }
            }
        }
    };
    private TimerTask task;
    private boolean isRecordCancel = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stu_add_voice_layout);
        mediaPlayerUtil = new MediaPlayerUtil();
        InitDtae();
    }

    @SuppressWarnings("unchecked")
    private void InitDtae() {
        mRecordButton = (Button) findViewById(R.id.record_btn);
        findViewById(R.id.tv_tool_back).setOnClickListener(this);
        findViewById(R.id.next_btn).setOnClickListener(this);
//        iv_record1 = (ImageView) findViewById(R.id.iv_record1);
//        iv_record2 = (ImageView) findViewById(R.id.iv_record2);
//        iv_record3 = (ImageView) findViewById(R.id.iv_record3);
        voiceRecorddingLayout = findViewById(R.id.voice_recordding_layout);
        recordTimeRemainText = (TextView) findViewById(R.id.record_remain_text);
        delVoice = (Button) findViewById(R.id.delete_voice);
//        dele_record1 = (ImageView) findViewById(R.id.iv_dele_record1);
//        dele_record2 = (ImageView) findViewById(R.id.iv_dele_record2);
//        dele_record3 = (ImageView) findViewById(R.id.iv_dele_record3);
//        dele_record1.setOnClickListener(this);
//        dele_record2.setOnClickListener(this);
//        dele_record3.setOnClickListener(this);
        delVoice.setOnClickListener(this);
        mRecordButton.setOnClickListener(this);
//        iv_record1.setOnClickListener(this);
//        iv_record2.setOnClickListener(this);
//        iv_record3.setOnClickListener(this);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        bitmap = (Bitmap) bundle.get("bitmap");
        int pbw = bitmap.getWidth();
        int pbh = bitmap.getHeight();
        iv_board_picture = (ImageView) findViewById(R.id.image);
        iv_board_picture.setImageBitmap(bitmap);
        // 把从照相和拍照的图片存放到一个 AppliCtion 作用应用不退出 他就一直存在
        ((UIApplication) UIApplication.getInstance()).getList().add(bitmap);
        urllist = new ArrayList<String>();
        iv_board_picture.setOnClickListener(this);
        mRecordButton.setOnTouchListener(new OnTouchListener() {
            private long xianzaitime;
            private Long i;

            @Override
            public boolean onTouch(View arg0, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (Count < 3) {
                            xianzaitime = System.currentTimeMillis();
                            isRecordCancel = false;
                            REC();
                        } else {
                            Toast.makeText(Activity_Stu_Add_Voice.this, "最多只能发3条语音", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if (!isRecordCancel) {
                            if (Count < 3) {
                                timer.cancel();
                                task.cancel();
                                long NewtTime = System.currentTimeMillis();
                                i = NewtTime - xianzaitime;
                                if (i < 1000) {
                                    Toast.makeText(Activity_Stu_Add_Voice.this, "时间太短", Toast.LENGTH_SHORT).show();
                                } else {
                                    VoideShow();
                                    urllist.add(url);
                                    Count++;
                                }
                                voiceRecorddingLayout.setVisibility(View.GONE);
                                mr.release();
                            }
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (!isRecordCancel) {
                            Rect r = new Rect();
                            arg0.getLocalVisibleRect(r);
                            boolean b = r.contains((int) (event.getX()), (int) (event.getY()));
                            if (!b) {
                                Toast.makeText(Activity_Stu_Add_Voice.this, "录音取消", Toast.LENGTH_SHORT).show();
                                isRecordCancel = true;
                                timer.cancel();
                                task.cancel();
                                voiceRecorddingLayout.setVisibility(View.GONE);
                                mr.release();
                            }
                        }
                        break;
                }
                return false;
            }

        });
    }


    @SuppressWarnings("static-access")
    private void REC() {
        voiceRecorddingLayout.setVisibility(View.VISIBLE);
        recLen = 30;
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                recLen--;
                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);
            }
        };
        timer.schedule(task, 0, 1000);

        url = Environment.getExternalStorageDirectory() + File.separator + "shike" + File.separator + "record" + File.separator + new DateFormat().format("yyyyMMdd_HHmmss", Calendar.getInstance(Locale.CHINA)) + ".amr";
        file = new File(url);
        mr = new MediaRecorder();
        mr.setAudioSource(MediaRecorder.AudioSource.DEFAULT);// 从麦克风源进行录音
        mr.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);// 设置输出格式
        mr.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);// 设置编码格式
        mr.setOutputFile(file.getAbsolutePath());// 设置输出文件
        try {
            file.createNewFile();// 创建文件
            mr.prepare();// 准备录制
            mr.start();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_down_step:
                Distinction_Stu_Tea();
                break;
            case R.id.tv_up_Step:
                finish();
                break;
            case R.id.iv_board_picture:
                Dialog.IntentParamet(this, ImageActivity.class, "bitmap", bitmap);
                break;
            case R.id.iv_record1:
                try {
                    String url = urllist.get(0);
                    mediaPlayerUtil.VoidePlay(url);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.iv_record2:
                try {
                    String url1 = urllist.get(1);
                    mediaPlayerUtil.VoidePlay(url1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.iv_record3:
                try {
                    String url2 = urllist.get(2);
                    mediaPlayerUtil.VoidePlay(url2);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            // 控制三个删除按钮的出现
            case R.id.rl_dele_mes:
//                if (iv_record1.getVisibility() == View.VISIBLE) {
//                    if (dele_mes_flag) {
//                        dele_record1.setVisibility(View.VISIBLE);
//                    } else {
//                        dele_record1.setVisibility(View.GONE);
//                    }
//                }
//                if (iv_record2.getVisibility() == View.VISIBLE) {
//                    if (dele_mes_flag) {
//                        dele_record2.setVisibility(View.VISIBLE);
//                    } else {
//                        dele_record2.setVisibility(View.GONE);
//                    }
//                }
//                if (iv_record3.getVisibility() == View.VISIBLE) {
//                    if (dele_mes_flag) {
//                        dele_record3.setVisibility(View.VISIBLE);
//                    } else {
//                        dele_record3.setVisibility(View.GONE);
//                    }
//                }
                dele_mes_flag = !dele_mes_flag;
                break;
            // 设置老师照相的点击事件
            case R.id.tv_paizhao:
                Dialog.Intent(this, Activity_Tea_Tool_Sele.class);
                break;
            // 删除消息1
            case R.id.iv_dele_record1:
                urllist.remove(0);
                dele_list = true;
                Count--;
//                View_Hide(iv_record1, dele_record1);
                break;
            case R.id.iv_dele_record2:
                if (dele_list) {
                    urllist.remove(0);
                } else {
                    urllist.remove(1);
                }
                Count--;
//                View_Hide(iv_record2, dele_record2);
                break;
            case R.id.iv_dele_record3:
                urllist.remove(urllist.size() - 1);
//                View_Hide(iv_record3, dele_record3);
                Count--;
                break;
        }
    }

    private void View_Hide(View view1, View view2) {
        view1.setVisibility(View.GONE);
        view2.setVisibility(View.GONE);
    }

    /**
     * 用来区分 老师和学生 跳转的页面
     */
    private void Distinction_Stu_Tea() {
        Bundle bundle = new Bundle();
//		bundle.putParcelable("bitmap", bitmap);
        if (!LoginManage.getInstance().isTeacher(this)) {//是学生
            bundle.putStringArrayList("urllist", urllist);
            Activity_My_Board2.saveBitmap = bitmap;
            Dialog.intent(this, Activity_My_Board2.class, bundle);
        } else {//如果是老师...显然就是拍照制作题目,下一个页面应该是给题目设置标题和选择文件夹
            Dialog.intent(this, Activity_Que_board2.class, bundle);
            finish();
        }
    }

    public void VoideShow() {
//        if (Count == 0) {
//            iv_record1.setVisibility(View.VISIBLE);
//        } else if (Count == 1) {
//            iv_record2.setVisibility(View.VISIBLE);
//        } else {
//            iv_record3.setVisibility(View.VISIBLE);
//        }
    }

    @Override
    protected void onStop() {
        mediaPlayerUtil.Stop_Play();
        super.onStop();
    }
}