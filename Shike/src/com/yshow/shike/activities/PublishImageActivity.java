package com.yshow.shike.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.yshow.shike.R;
import com.yshow.shike.entity.LoginManage;
import com.yshow.shike.entity.SkMessage_Res;
import com.yshow.shike.fragments.Fragment_Message;
import com.yshow.shike.service.MySKService;
import com.yshow.shike.utils.LogUtil;
import com.yshow.shike.utils.MediaPlayerUtil;
import com.yshow.shike.utils.MediaRecorderUtil;
import com.yshow.shike.utils.Net_Servse;
import com.yshow.shike.utils.ProgressDialogUtil;
import com.yshow.shike.utils.SKAsyncApiController;
import com.yshow.shike.utils.SKResolveJsonUtil;
import com.yshow.shike.utils.ScreenSizeUtil;
import com.yshow.shike.widget.MatrixImageview;
import com.yshow.shike.widget.StuTapeImage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 学生提问时的大图页面,与交互中的大图页面区分
 */
public class PublishImageActivity extends BaseActivity implements OnClickListener, StuTapeImage.VoiceImageClickListener {
    private Bitmap bitmap;
    private MatrixImageview large_img;
    private Net_Servse net_Servse;
    private ProgressDialogUtil dialogUtil;
    private TextView back_time, tea_end;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                back_time.setText("录音剩余时间：" + recLen);
                if (recLen < 0) {
                    mediaRecorderUtil.Give_Up_voide();
                    timer.cancel();
                    back_time.setText("录音剩余时间：" + 0);
                    recLen = 1;
                }
            } else {
                large_img.setBitmap(bitmap);
                dialogUtil.dismiss();
            }
        }

        ;
    };
    private TextView mVoiceBtn;
    private LinearLayout voiceLayout;

    private int recLen = 0;
    private Timer timer = null;
    private MediaRecorderUtil mediaRecorderUtil;
    private MediaPlayerUtil mediaPlayer;
    private boolean isRecordCancel = false;
    private View ll_volume_control;

    private int voideSize = 0;

    private ArrayList<String> voiceList = new ArrayList<String>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.publish_big_picture_layout);
        net_Servse = Net_Servse.getInstence();
        dialogUtil = new ProgressDialogUtil(this);
        mediaPlayer = new MediaPlayerUtil();
        mediaRecorderUtil = new MediaRecorderUtil(this);
        InitData();
    }

    private void InitData() {
        mVoiceBtn = (TextView) findViewById(R.id.voice);
        mVoiceBtn.setOnTouchListener(touchlistener);
        ll_volume_control = findViewById(R.id.voice_recordding_layout);
        ll_volume_control.setVisibility(View.GONE);
        voiceLayout = (LinearLayout) findViewById(R.id.voice_layout);
        back_time = (TextView) findViewById(R.id.record_remain_text);
        large_img = (MatrixImageview) findViewById(R.id.large_img);

        TextView titleText = (TextView) findViewById(R.id.title_text);
        findViewById(R.id.left_btn).setOnClickListener(this);
        findViewById(R.id.confirm_btn).setOnClickListener(this);

        large_img.setOnClickListener(this);
        bitmap = Activity_Stu_Ask_Step2.sUploadBitmap;
        bitmap.setDensity(ScreenSizeUtil.getScreenDensityDpi(this));
        large_img.setBitmap(bitmap);
        voiceList = (ArrayList<String>) Activity_Stu_Add_Voice.urllist.clone();
        voideSize = voiceList.size();
        titleText.setText("添加录音");

        addVoiceLayout();
    }

    private void addVoiceLayout() {
        for (String s : voiceList) {
            StuTapeImage img = new StuTapeImage(PublishImageActivity.this);
            LinearLayout.LayoutParams pa = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            pa.leftMargin = 20;
            img.setLayoutParams(pa);
            img.setPlayer(mediaPlayer);
            img.setVoicePath(s);
            img.setmVoiceImageClickListener(this);
            img.setDelVisiable(true);
            voiceLayout.addView(img);
        }
    }

    private View.OnTouchListener touchlistener = new View.OnTouchListener() {
        private Long uptime;
        private Long downtime;
        private boolean start_boolear; // 对SD卡进行判断

        @Override
        public boolean onTouch(View view, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    downtime = System.currentTimeMillis();
                    mediaRecorderUtil = new MediaRecorderUtil(PublishImageActivity.this);
                    start_boolear = mediaRecorderUtil.startRecorder();
                    isRecordCancel = false;
                    if (start_boolear) {
                        Timer_Time();
                        ll_volume_control.setVisibility(View.VISIBLE);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if (!isRecordCancel) {
                        if (start_boolear) {
                            if (timer != null) {
                                timer.cancel();
                            }
                            uptime = System.currentTimeMillis();
                            ll_volume_control.setVisibility(View.GONE);
                            mediaRecorderUtil.stopRecorder();
                            if (uptime - downtime < 1000) {
                                Toast.makeText(PublishImageActivity.this, "说话时间太短", Toast.LENGTH_SHORT).show();
                            } else {
                                final String file = mediaRecorderUtil.getFilePath();
                                if (voideSize >= 3) {
                                    Toast.makeText(PublishImageActivity.this, "最多只能发3条语音", Toast.LENGTH_SHORT).show();
                                } else {
                                    voiceList.add(file);
                                    voideSize++;
                                    StuTapeImage img = new StuTapeImage(PublishImageActivity.this);
                                    LinearLayout.LayoutParams pa = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                    pa.leftMargin = 20;
                                    img.setLayoutParams(pa);
                                    img.setPlayer(mediaPlayer);
                                    img.setVoicePath(file);
                                    img.setDelVisiable(true);
                                    img.setmVoiceImageClickListener(PublishImageActivity.this);
                                    voiceLayout.addView(img);
                                }
                            }
                        }
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                        if (start_boolear) {
                            if (!isRecordCancel) {
                                Rect r = new Rect();
                                view.getLocalVisibleRect(r);
                                boolean b = false;
                                if(event.getX()>0&&event.getX()<view.getWidth()&&event.getY()>0&&event.getY()<view.getHeight()){
                                    b = true;
                                }
//                                boolean b = r.contains((int) (event.getX()), (int) (event.getY()));
                                if (!b) {
                                    Toast.makeText(PublishImageActivity.this, "录音取消", Toast.LENGTH_SHORT).show();
                                    isRecordCancel = true;
                                    timer.cancel();
                                    ll_volume_control.setVisibility(View.GONE);
                                    mediaRecorderUtil.stopRecorder();
                                }
                            }
                        }
                    break;
            }
            return true;
        }

        private void Timer_Time() {
            recLen = 30;
            timer = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    recLen--;
                    Message message = new Message();
                    message.what = 1;
                    handler.sendMessage(message);
                }
            };
            timer.schedule(task, 30, 1000);
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.large_img:
                finish();
                break;
            case R.id.left_btn:
                finish();
                break;
            case R.id.confirm_btn:
                Activity_Stu_Add_Voice.urllist = voiceList;
                setResult(RESULT_OK);
                finish();
                break;
        }
    }

    @Override
    protected void onStop() {
        mediaPlayer.Stop_Play();
        super.onStop();
    }

    @Override
    public void onImageClick(StuTapeImage img) {

    }

    @Override
    public void onDelClick(StuTapeImage img) {
        voiceList.remove(img.getVoicePath());
        voideSize--;
    }
}
