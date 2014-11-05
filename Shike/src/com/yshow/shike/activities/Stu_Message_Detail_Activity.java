package com.yshow.shike.activities;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.yshow.shike.R;
import com.yshow.shike.entity.LoginManage;
import com.yshow.shike.entity.SKMessage;
import com.yshow.shike.entity.SKMessageList;
import com.yshow.shike.entity.SkMessage_Res;
import com.yshow.shike.entity.SkMessage_Voice;
import com.yshow.shike.fragments.Fragment_Message;
import com.yshow.shike.service.MySKService;
import com.yshow.shike.utils.DateUtils;
import com.yshow.shike.utils.Dialog;
import com.yshow.shike.utils.ImageLoadOption;
import com.yshow.shike.utils.MediaPlayerUtil;
import com.yshow.shike.utils.MediaRecorderUtil;
import com.yshow.shike.utils.MyAsyncHttpResponseHandler;
import com.yshow.shike.utils.Net_Servse;
import com.yshow.shike.utils.SKAsyncApiController;
import com.yshow.shike.utils.SKResolveJsonUtil;
import com.yshow.shike.utils.ScreenSizeUtil;
import com.yshow.shike.widget.StuTapeImage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 学生进入的消息交互页面
 */
public class Stu_Message_Detail_Activity extends BaseActivity implements OnClickListener, ViewPager.OnPageChangeListener {
    private LinearLayout bottom_navigation;
    private ArrayList<SkMessage_Res> reslist;
    private DisplayImageOptions options;
    private ImageLoader imageLoader;
    private int pointion;
    private String curquestionId;
    private ViewPager viewPager;
    private MyAdapter myAdapter;
    private View ll_volume_control;
    private Context context;
    private int recLen = 0;
    private Timer timer = null;
    private MediaRecorderUtil mediaRecorderUtil;
    private TextView tv_visits, tv_data;
    private SKMessage sKMessage;
    private TextView back_time, mEndButton;
    private MediaPlayerUtil mediaPlayer;


    private String mTeacherName;
    private String mName;

    private LinearLayout mVoiceShowLayout;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MySKService.HAVE_NEW_MESSAGE:
                    Log.e("Activity_Message_Three", "得到通知消刷新列表");
                    ArrayList<SKMessage> newresolveMessage = (ArrayList<SKMessage>) msg.obj;
                    setSKMNewessage(newresolveMessage);
                    break;
                case 1:
                    back_time.setText("录音剩余时间：" + recLen);
                    if (recLen < 0) {
                        mediaRecorderUtil.Give_Up_voide();
                        timer.cancel();
                        back_time.setText("录音剩余时间：" + 0);
                        recLen = 1;
                    }
            }
        }
    };

    private boolean setSKMNewessage(ArrayList<SKMessage> list) {
        for (SKMessage skMessage : list) {
            String questionId = skMessage.getQuestionId();
            if (curquestionId.equals(questionId)) {
                sKMessage = skMessage;
                String count = sKMessage.getResCount();
                if (!count.equals("")) {
                    Integer valueOf = Integer.valueOf(count);
                    int t = (valueOf + 9) / 10 * 10;
                    if (t == 0) {// 这地方做一下兼容
                        t = 10;
                    }
                    tv_visits.setText("提问次数：" + count + "/" + t);
                }
                Log.e("Activity_Message_Three", "得到通知消刷新");
                myAdapter = new MyAdapter(skMessage.getRes());
                viewPager.setAdapter(myAdapter);
                ArrayList<SkMessage_Res> res = skMessage.getRes();
                int size = res.size();
                int size2 = reslist.size();
                if (size > size2) {
                    viewPager.setCurrentItem(0);
                    reslist = res;
                    curquestionId = questionId;
                    addVoiceView(0);
                    return true;
                } else if (size == size2) {
                    for (int j = 0; j < size2; j++) {
                        SkMessage_Res skMessage_Res = res.get(j);
                        SkMessage_Res skMessage_Res2 = reslist.get(j);
                        ArrayList<SkMessage_Voice> voice2 = skMessage_Res.getVoice();
                        ArrayList<SkMessage_Voice> voice3 = skMessage_Res2.getVoice();
                        if (voice2.size() > voice3.size()) {
                            viewPager.setCurrentItem(j);
                            reslist = res;
                            curquestionId = questionId;
                            addVoiceView(j);
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stu_message_detail_layout);
        context = this;
        mediaPlayer = new MediaPlayerUtil();
        iniData();
        mediaRecorderUtil = new MediaRecorderUtil(this);
        mediaRecorderUtil.setVoiceLevelImg((ImageView) findViewById(R.id.voice_level_img));
        MySKService.handler = handler;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        getSKMessage();
    }

    @Override
    protected void onStop() {
        mediaPlayer.Stop_Play();
        super.onStop();
    }

    private void iniData() {
        Bundle bundle = getIntent().getExtras();
        sKMessage = (SKMessage) bundle.getSerializable("sKMessage");
        curquestionId = sKMessage.getQuestionId();
        mVoiceShowLayout = (LinearLayout) findViewById(R.id.voice_layout);
        ll_volume_control = findViewById(R.id.voice_recordding_layout);
        ll_volume_control.setVisibility(View.GONE);
        bottom_navigation = (LinearLayout) findViewById(R.id.bottom_layout);
        Button stu_voice = (Button) findViewById(R.id.record_btn);
        Button tv_tianjian = (Button) findViewById(R.id.img_button);
        mEndButton = (TextView) findViewById(R.id.right_button);
        if (sKMessage.isDone()) {
            bottom_navigation.setVisibility(View.INVISIBLE);
            mEndButton.setVisibility(View.INVISIBLE);
        }
        TextView nickName = (TextView) findViewById(R.id.nick_name);
        back_time = (TextView) findViewById(R.id.record_remain_text);
        tv_visits = (TextView) findViewById(R.id.tiwen_times);
        tv_data = (TextView) findViewById(R.id.tv_data);
        mTeacherName = sKMessage.getNickname();
        mName = LoginManage.getInstance().getStudent().getName();
        nickName.setText(mTeacherName);
        String count = sKMessage.getResCount();
        if (!count.equals("")) {
            Integer valueOf = Integer.valueOf(count);
            int t = (valueOf + 9) / 10 * 10;
            if (t == 0) {// 这地方做一下兼容
                t = 10;
            }
            tv_visits.setText("提问次数：" + count + "/" + t);
        }
        // 提问的次数
        long updateTime = Long.valueOf(sKMessage.getUpdateTime());
        tv_data.setText(sKMessage.getDate() + "  " + DateUtils.formatDateH(new Date(Long.valueOf(updateTime) * 1000)));
        stu_voice.setOnClickListener(this);
        tv_tianjian.setOnClickListener(this);
        mEndButton.setOnClickListener(this);
        stu_voice.setOnTouchListener(touchlistener);
        viewPager = (ViewPager) findViewById(R.id.message_viewpager);
//        pointion = bundle.getInt("pointion");
        pointion = 0;
        reslist = sKMessage.getRes();
        options = ImageLoadOption.getBigImageOption(R.drawable.xiaoxi_moren);
        imageLoader = ImageLoader.getInstance();
        myAdapter = new MyAdapter(reslist);
        viewPager.setOnPageChangeListener(this);
        viewPager.setAdapter(myAdapter);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setCurrentItem(pointion);
        addVoiceView(0);
        findViewById(R.id.tv_tool_back).setOnClickListener(this);

        if (sKMessage.getMsgType().equals("1")) {// 这里表示是系统消息
            mEndButton.setVisibility(View.GONE);
            bottom_navigation.setVisibility(View.GONE);
            tv_visits.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.tv_tool_back:
                finish();
                break;
            case R.id.img_button:// 学生端点击拍照提问
                intent = new Intent(this, Activity_Stu_Ask_Step1.class);
                intent.putExtra("isContinue", true);
                intent.putExtra("questionId", curquestionId);
                intent.putExtra("tea_add_tool", "1");
                startActivityForResult(intent, 1);
                // finish();
                break;
            case R.id.right_button:
                Thank_Teacher(Stu_Message_Detail_Activity.this);
                break;
        }
    }

    private boolean isRecordCancel = false;
    /**
     * 开始录音
     */
    private OnTouchListener touchlistener = new OnTouchListener() {
        private Long uptime;
        private Long downtime;
        private boolean start_boolear; // 对SD卡进行判断

        @Override
        public boolean onTouch(View view, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    downtime = System.currentTimeMillis();
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
                                Toast.makeText(Stu_Message_Detail_Activity.this, "说话时间太短", Toast.LENGTH_SHORT).show();
                            } else {
                                int currentItem = viewPager.getCurrentItem();
                                SkMessage_Res skMessage_Res = reslist.get(currentItem);
                                String fileId = skMessage_Res.getId();
                                skUploadMp3(curquestionId, fileId, mediaRecorderUtil.getFilePath(), skMessage_Res.getVoice().size() + 1 + "");
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
                                    Toast.makeText(Stu_Message_Detail_Activity.this, "录音取消", Toast.LENGTH_SHORT).show();
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
            return false;
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
    public void onPageScrolled(int i, float v, int i2) {

    }

    @Override
    public void onPageSelected(int i) {
        addVoiceView(i);
    }

    private void addVoiceView(int index) {
        mVoiceShowLayout.removeAllViews();
        ArrayList<SkMessage_Voice> voice = reslist.get(index).getVoice();
        LinearLayout row = null;
        int toppadding = ScreenSizeUtil.Dp2Px(this, 5);
        for (int i = 0; i < voice.size(); i++) {
            SkMessage_Voice voiceitem = voice.get(i);
            StuTapeImage tapeimg = new StuTapeImage(this);
            LinearLayout.LayoutParams pa = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            pa.leftMargin = 20;
            tapeimg.setLayoutParams(pa);
            tapeimg.setIsTeacher(!voiceitem.getIsStudent().equals("1"));
            tapeimg.setPlayer(mediaPlayer);
            tapeimg.setVoicePath(voiceitem.getFile());
//            mVoiceShowLayout.addView(tapeimg);
            if (i % 5 == 0) {
                row = new LinearLayout(this);
                row.setPadding(0, toppadding, 0, 0);
                mVoiceShowLayout.addView(row);
            }
            row.addView(tapeimg);
        }
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    /**
     * 页面数据适配器
     */
    public class MyAdapter extends PagerAdapter {
        ArrayList<SkMessage_Res> reslist;

        public MyAdapter(ArrayList<SkMessage_Res> reslist) {
            this.reslist = reslist;
        }

        public int getCount() {
            return reslist.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(View container, int position, Object object) {
        }

        /**
         * 载入图片进去，用当前的position 除以 图片数组长度取余数是关键
         */
        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            final SkMessage_Res skMessage_Res = reslist.get(position);
            final String files = skMessage_Res.getFile_tub();
            View view = View.inflate(Stu_Message_Detail_Activity.this, R.layout.img_page_item, null);
            final ImageView iv_picture = (ImageView) view.findViewById(R.id.iv_picture);
            iv_picture.setTag(files);

            OnClickListener lister = new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    String file = skMessage_Res.getFile();
                    Bundle bundle = new Bundle();
                    bundle.putString("Message_Three", file);
                    bundle.putSerializable("res", sKMessage.getRes().get(position));

                    if (sKMessage.getMsgType().equals("1")) {// 这里表示是系统消息
                        bundle.putBoolean("isdone", true);
                    } else {
                        bundle.putBoolean("isdone", sKMessage.isDone());
                    }


                    Intent intent = new Intent(context, ImageActivity.class);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 1);
                }
            };

            iv_picture.setOnClickListener(lister);
            imageLoader.displayImage(files, iv_picture, options);
            container.addView(view);
            return view;
        }
    }


    private void skUploadMp3(final String questionId, final String imgid, String mapPath, String posID) {
        SKAsyncApiController.skUploadMp3(questionId, imgid, mapPath, posID, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String json) {
                super.onSuccess(json);
                Log.e("skUploadeImage", json);
                boolean resolveIsSuccess = SKResolveJsonUtil.getInstance().resolveIsSuccess(json, Stu_Message_Detail_Activity.this);
                if (resolveIsSuccess) {
                    skSend_messge(questionId, "0");
                }
            }
        });
    }

    public void Thank_Teacher(final Context context) {
        Builder builder = new Builder(context);
        builder.setTitle("     提示");
        builder.setMessage("确定该题已经解决了吗？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                SKAsyncApiController.Topic_End(curquestionId, new MyAsyncHttpResponseHandler(context, true) {
                    @Override
                    public void onSuccess(String json) {
                        super.onSuccess(json);
                        boolean isSuccess = SKResolveJsonUtil.getInstance().resolveIsSuccess(json, context);
                        if (isSuccess) {
                            Fragment_Message.handler.sendEmptyMessage(MySKService.HAVE_NEW_MESSAGE);
                            Toast.makeText(context, "確定结束消息", Toast.LENGTH_SHORT).show();
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("message", sKMessage);
                            Dialog.intent(context, AppraiseMainActivity.class, bundle);
                            finish();
                        } else {
                            Toast.makeText(context, "確定结束消息失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        builder.setNegativeButton("取消", null);
        try {
            builder.show();
        } catch (Exception e) {
        }
    }

    private void skSend_messge(final String questionId, String isSend) {
        Log.e("questionId", questionId);
        SKAsyncApiController.skSend_messge(questionId, "", isSend, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                Log.e("skSend_messge", content);
                boolean resolveIsSuccess = SKResolveJsonUtil.getInstance().resolveIsSuccess(content, Stu_Message_Detail_Activity.this);
                if (resolveIsSuccess) {
                    try {
                        JSONObject jsonObject = new JSONObject(content);
                        String data = jsonObject.optString("data");
                        String type = jsonObject.optString("type");
                        if (type.equals("confirm")) {
                            Builder dia = new Builder(Stu_Message_Detail_Activity.this);
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
                            Toast.makeText(Stu_Message_Detail_Activity.this, "发送成功", Toast.LENGTH_SHORT).show();
                            getSKMessage();
                            Fragment_Message.handler.sendEmptyMessage(MySKService.HAVE_NEW_MESSAGE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * 获取消息列表
     */
    private void getSKMessage() {
        SKAsyncApiController.skGetMessage(1, new MyAsyncHttpResponseHandler(this, true) {
            @Override
            public void onSuccess(String json) {
                super.onSuccess(json);
                boolean isSuccess = SKResolveJsonUtil.getInstance().resolveIsSuccess(json);
                if (isSuccess) {
                    ArrayList<SKMessageList> resolveMessage = SKResolveJsonUtil.getInstance().resolveMessage(json);
                    for (SKMessageList skMessageList2 : resolveMessage) {
                        ArrayList<SKMessage> list = skMessageList2.getList();
                        boolean setSKMNewessage = setSKMNewessage(list);
                        if (setSKMNewessage) {
                            return;
                        }
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                getSKMessage();
            }
        }
    }


}