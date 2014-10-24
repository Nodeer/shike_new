package com.yshow.shike.activities;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.*;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.umeng.analytics.MobclickAgent;
import com.yshow.shike.R;
import com.yshow.shike.UIApplication;
import com.yshow.shike.db.DatabaseDao;
import com.yshow.shike.entity.LoginManage;
import com.yshow.shike.entity.SKMessage;
import com.yshow.shike.entity.SkMessage_Res;
import com.yshow.shike.fragments.Fragment_Message;
import com.yshow.shike.service.MySKService;
import com.yshow.shike.utils.*;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.RelativeLayout.LayoutParams;

import com.yshow.shike.widget.MatrixImageview;

import org.json.JSONException;
import org.json.JSONObject;

public class ImageActivity extends Activity implements OnClickListener {
    private Bitmap bitmap;
    private MatrixImageview large_img;
    private Net_Servse net_Servse;
    private ProgressDialogUtil dialogUtil;
    private TextView back_time, tea_end;
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
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
    private RelativeLayout ll_volume_control;
    private RelativeLayout mBottomLayout;

    private SkMessage_Res mMessageRes;
    private boolean isTeacher;
    private boolean isNeedRefresh = false;

    private boolean isDone = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_down_picture);
        net_Servse = Net_Servse.getInstence();
        dialogUtil = new ProgressDialogUtil(this);
        mediaPlayer = new MediaPlayerUtil();
        mediaRecorderUtil = new MediaRecorderUtil(this);
        InitData();
    }

    private void InitData() {
        isTeacher = LoginManage.getInstance().isTeacher();
        mVoiceBtn = (TextView) findViewById(R.id.voice);
        mVoiceBtn.setOnTouchListener(touchlistener);
        ll_volume_control = (RelativeLayout) findViewById(R.id.ll_volume_control);
        mBottomLayout = (RelativeLayout) findViewById(R.id.bottom_layout);
        voiceLayout = (LinearLayout) findViewById(R.id.voice_layout);
        if (isTeacher) {
            mBottomLayout.setBackgroundResource(R.color.bottom_widow_color);
        } else {
            mBottomLayout.setBackgroundResource(R.color.sk_student_main_color);

        }
        back_time = (TextView) findViewById(R.id.tv_back_time);
        large_img = (MatrixImageview) findViewById(R.id.large_img);
        ImageView ima_download = (ImageView) findViewById(R.id.ima_download);
        ima_download.setOnClickListener(this);
        large_img.setOnClickListener(this);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        final String Message_Three_url = (String) bundle.get("Message_Three");
        if (Message_Three_url != null) {
            mBottomLayout.setVisibility(View.VISIBLE);
            mMessageRes = (SkMessage_Res) bundle.getSerializable("res");
            isDone = bundle.getBoolean("isdone");
            if (isDone) {
                mBottomLayout.setVisibility(View.GONE);
            } else {
                mBottomLayout.setVisibility(View.VISIBLE);
            }
            dialogUtil.show();
            new Thread() {
                public void run() {
                    try {
                        bitmap = net_Servse.getImage(Message_Three_url);
                        if (bitmap != null) {
                            handler.sendEmptyMessage(0);
                        } else {
                            dialogUtil.dismiss();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        dialogUtil.dismiss();
                    }
                }

                ;
            }.start();
        } else {
            mBottomLayout.setVisibility(View.GONE);
            List<String> picUrls = UIApplication.getInstance().getPicUrls();
            String path = picUrls.get(picUrls.size() - 1);// 是要显示的图片的绝对地址
            bitmap = Bitmap_Manger_utils.getIntence().press_bitmap(ImageActivity.this, path);
            // Bitmap decodeFile = path.decodeFile(path,ScreenSizeUtil.getScreenWidth(this,1),ScreenSizeUtil.getScreenHeight(this));
            large_img.setImageBitmap(bitmap);
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
                    mediaRecorderUtil = new MediaRecorderUtil(ImageActivity.this);
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
                                Toast.makeText(ImageActivity.this, "说话时间太短", Toast.LENGTH_SHORT).show();
                            } else {
                                final String file = mediaRecorderUtil.getFilePath();
                                if (isTeacher) {
                                    ImageView img = new ImageView(ImageActivity.this);
                                    img.setBackgroundResource(R.drawable.teather_void);
                                    img.setOnClickListener(new OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            mediaPlayer.VoidePlay(file);
                                        }
                                    });
                                    voiceLayout.addView(img);
                                } else {
                                    ImageView img = new ImageView(ImageActivity.this);
                                    img.setBackgroundResource(R.drawable.teather_student);
                                    img.setOnClickListener(new OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            mediaPlayer.VoidePlay(file);
                                        }
                                    });
                                    voiceLayout.addView(img);
                                }
                                skUploadMp3(mMessageRes.getQuestionId(), mMessageRes.getId(), file, mMessageRes.getVoice().size() + 1 + "");
                            }
                        }
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (!isRecordCancel) {
                        if (start_boolear) {
                            if (!isRecordCancel) {
                                Rect r = new Rect();
                                view.getLocalVisibleRect(r);
                                boolean b = r.contains((int) (event.getX()), (int) (event.getY()));
                                if (!b) {
                                    Toast.makeText(ImageActivity.this, "录音取消", Toast.LENGTH_SHORT).show();
                                    isRecordCancel = true;
                                    timer.cancel();
                                    ll_volume_control.setVisibility(View.GONE);
                                    mediaRecorderUtil.stopRecorder();
                                }
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

    private void skUploadMp3(final String questionId, final String imgid, String mapPath, String posID) {
        SKAsyncApiController.skUploadMp3(questionId, imgid, mapPath, posID, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String json) {
                super.onSuccess(json);
                LogUtil.d(json);
                boolean resolveIsSuccess = SKResolveJsonUtil.getInstance().resolveIsSuccess(json, ImageActivity.this);
                if (resolveIsSuccess) {
                    skSend_messge(questionId, "0");
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                super.onFailure(throwable);
                Toast.makeText(ImageActivity.this, "语音上传失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void skSend_messge(final String questionId, String isSend) {
        Log.e("questionId", questionId);
        SKAsyncApiController.skSend_messge(questionId, "", isSend, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                LogUtil.d(content);
                boolean resolveIsSuccess = SKResolveJsonUtil.getInstance().resolveIsSuccess(content, ImageActivity.this);
                if (resolveIsSuccess) {
                    // Toast.makeText(ImageActivity.this, "语音上传成功", Toast.LENGTH_SHORT).show();
                    try {
                        JSONObject jsonObject = new JSONObject(content);
                        String data = jsonObject.optString("data");
                        String type = jsonObject.optString("type");
                        if (type.equals("confirm")) {
                            AlertDialog.Builder dia = new AlertDialog.Builder(ImageActivity.this);
                            dia.setMessage(data);
                            dia.setNegativeButton("取消", null);
                            dia.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    skSend_messge(questionId, "1");
                                }
                            });
                            dia.show();
                        } else {
                            Toast.makeText(ImageActivity.this, "语音上传成功", Toast.LENGTH_SHORT).show();
                            isNeedRefresh = true;
                            Fragment_Message.handler.sendEmptyMessage(MySKService.HAVE_NEW_MESSAGE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ima_download:
                net_Servse.saveBitmap(bitmap, this);
                break;
            case R.id.large_img:
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (isNeedRefresh) {
            setResult(RESULT_OK);
            finish();
        } else {
            finish();
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

    @Override
    protected void onStop() {
        mediaPlayer.Stop_Play();
        super.onStop();
    }
}
