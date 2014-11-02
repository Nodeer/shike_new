package com.yshow.shike.activities;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yshow.shike.R;
import com.yshow.shike.db.DatabaseDao;
import com.yshow.shike.entity.LoginManage;
import com.yshow.shike.entity.SKMessage;
import com.yshow.shike.entity.SKMessageList;
import com.yshow.shike.entity.SkMessage_Res;
import com.yshow.shike.entity.SkMessage_Voice;
import com.yshow.shike.fragments.Fragment_Message;
import com.yshow.shike.service.MySKService;
import com.yshow.shike.utils.DateUtils;
import com.yshow.shike.utils.Dialog;
import com.yshow.shike.utils.HelpUtil;
import com.yshow.shike.utils.ImageLoadOption;
import com.yshow.shike.utils.MediaPlayerUtil;
import com.yshow.shike.utils.MediaRecorderUtil;
import com.yshow.shike.utils.MyAsyncHttpResponseHandler;
import com.yshow.shike.utils.Net_Servse;
import com.yshow.shike.utils.PartnerConfig;
import com.yshow.shike.utils.SKAsyncApiController;
import com.yshow.shike.utils.SKResolveJsonUtil;
import com.yshow.shike.utils.YD;
import com.yshow.shike.widget.StuTapeImage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 老师的交互主页面
 */
public class Tea_Message_Detail_Activity extends Activity implements OnClickListener, ViewPager.OnPageChangeListener {
    private LinearLayout teaDecideLayout;
    private ArrayList<SkMessage_Res> reslist;
    private List<String> bitmap_list = new ArrayList<String>(); // ViewPager显示的一个集合
    private DisplayImageOptions options;
    private ImageLoader imageLoader;
    private String curquestionId;
    private ViewPager viewPager;
    private MyAdapter myAdapter;
    private View ll_volume_control;
    private Context context;
    private int recLen = 0;
    private Timer timer = null;
    private MediaRecorderUtil mediaRecorderUtil;
    private TextView tv_data;
    private LinearLayout bottomLayout;
    private SKMessage sKMessage;
    private String claim_uid;
    private TextView back_time;
    private DatabaseDao databaseDao;
    private List<String> query_voidce; // 查询所有数据库的录音
    private MediaPlayerUtil mediaPlayer;

    private Button sendVoiceButton;

//    private LinearLayout mSaveTikuBtn;

    private boolean hasGetQuestion = false;

    private boolean isNeedShowEndDialog = true;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MySKService.HAVE_NEW_MESSAGE:
                    Log.e("Activity_Message_Three", "得到通知消刷新列表");
                    @SuppressWarnings("unchecked")
                    ArrayList<SKMessage> newresolveMessage = (ArrayList<SKMessage>) msg.obj;
                    setSKMNewessage(newresolveMessage);
                    Teather_Decide();
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
    private LinearLayout mVoiceShowLayout;

    private boolean setSKMNewessage(ArrayList<SKMessage> list) {
        for (SKMessage skMessage : list) {
            String questionId = skMessage.getQuestionId();
            if (curquestionId.equals(questionId)) {
                sKMessage = skMessage;
                claim_uid = sKMessage.getClaim_uid();
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
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        getSKMessage();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tea_message_detail_layout);
        context = this;
        mediaPlayer = new MediaPlayerUtil();
        databaseDao = new DatabaseDao(context);
        query_voidce = databaseDao.Query();
        initData();
        mediaRecorderUtil = new MediaRecorderUtil(this);
        mediaRecorderUtil.setVoiceLevelImg((ImageView) findViewById(R.id.voice_level_img));
        MySKService.handler = handler;
        if (!sKMessage.getMsgType().equals("1")) {//如果此题还没有进入交互状态
            Teather_Decide();
        }
    }

    @Override
    protected void onStop() {
        mediaPlayer.Stop_Play();
        super.onStop();
    }

    private void initData() {
        Bundle bundle = getIntent().getExtras();
        sKMessage = (SKMessage) bundle.getSerializable("sKMessage");
        if (sKMessage.isDone()) {
            isNeedShowEndDialog = false;
        }
        curquestionId = sKMessage.getQuestionId();
        claim_uid = sKMessage.getClaim_uid();
//        mSaveTikuBtn = (LinearLayout) findViewById(R.id.save_ques_layout);
//        mSaveTikuBtn.setOnClickListener(this);
        ll_volume_control = findViewById(R.id.voice_recordding_layout);
        ll_volume_control.setVisibility(View.GONE);
        teaDecideLayout = (LinearLayout) findViewById(R.id.tea_decide_layout);

        mVoiceShowLayout = (LinearLayout) findViewById(R.id.voice_layout);
        sendVoiceButton = (Button) findViewById(R.id.record_btn);
        Button tv_tianjian = (Button) findViewById(R.id.img_button);
        if (sKMessage.isDone()) {
            teaDecideLayout.setVisibility(View.GONE);
//            mSaveTikuBtn.setVisibility(View.VISIBLE);
        }
        TextView tv_xiaohongyu = (TextView) findViewById(R.id.nick_name);
        back_time = (TextView) findViewById(R.id.record_remain_text);
        bottomLayout = (LinearLayout) findViewById(R.id.bottom_layout);
        findViewById(R.id.get_ques_btn).setOnClickListener(this);
        findViewById(R.id.giveup_ques_btn).setOnClickListener(this);
        findViewById(R.id.tv_tool_back).setOnClickListener(this);
        tv_data = (TextView) findViewById(R.id.tv_data);
        tv_xiaohongyu.setText(sKMessage.getNickname());
        long updateTime = Long.valueOf(sKMessage.getUpdateTime());
        tv_data.setText(sKMessage.getDate() + "  " + DateUtils.formatDateH(new Date(Long.valueOf(updateTime) * 1000)));
        tv_tianjian.setOnClickListener(this);
        sendVoiceButton.setOnTouchListener(touchlistener);
        viewPager = (ViewPager) findViewById(R.id.message_viewpager);
        reslist = sKMessage.getRes();
        options = ImageLoadOption.getBigImageOption(R.drawable.xiaoxi_moren);
        imageLoader = ImageLoader.getInstance();
        myAdapter = new MyAdapter(reslist);
        viewPager.setAdapter(myAdapter);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setOnPageChangeListener(this);
        addVoiceView(0);
        if (sKMessage.getMsgType().equals("1")) {// 这里表示是系统消息
            teaDecideLayout.setVisibility(View.GONE);
            bottomLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.tv_tool_back:
                finish();
                break;
            // 接受学生端消息 按钮
            case R.id.get_ques_btn:
                Qiang_Da(this);
                break;
            case R.id.giveup_ques_btn:
                Giew_Up(this);
                break;
            case R.id.img_button:// 老师端点击拍照解答
                intent = new Intent(this, Activity_Stu_Ask_Step1.class);
                intent.putExtra("isContinue", true);
                intent.putExtra("questionId", curquestionId);
                startActivityForResult(intent, 1);
                // finish();
                break;
            case R.id.save_ques_layout:// 存入题库
                Toast.makeText(this, "即将推出", Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(Tea_Message_Detail_Activity.this, "说话时间太短", Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(Tea_Message_Detail_Activity.this, "录音取消", Toast.LENGTH_SHORT).show();
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
        for (int voice_count = 0; voice_count < voice.size(); voice_count++) {
            SkMessage_Voice voiceitem = voice.get(voice_count);
            StuTapeImage tapeimg = new StuTapeImage(this);
            LinearLayout.LayoutParams pa = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            pa.leftMargin = 20;
            tapeimg.setLayoutParams(pa);
            tapeimg.setIsTeacher(!voiceitem.getIsStudent().equals("1"));
            tapeimg.setPlayer(mediaPlayer);
            tapeimg.setVoicePath(voiceitem.getFile());
            mVoiceShowLayout.addView(tapeimg);
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
            View view = View.inflate(Tea_Message_Detail_Activity.this, R.layout.img_page_item, null);
            ImageView iv_picture = (ImageView) view.findViewById(R.id.iv_picture);
            iv_picture.setTag(files);
            iv_picture.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    String file = skMessage_Res.getFile();
                    Bundle bundle = new Bundle();
                    bundle.putString("Message_Three", file);
                    bundle.putSerializable("res", sKMessage.getRes().get(position));

                    if (sKMessage.getMsgType().equals("1")) {// 这里表示是系统消息
                        bundle.putBoolean("isdone", true);
                    } else if (hasGetQuestion) {
                        bundle.putBoolean("isdone", false);
                    } else if (sKMessage.getClaim_uid().equals("0")) {
                        bundle.putBoolean("isdone", true);
                    } else {
                        bundle.putBoolean("isdone", sKMessage.isDone());
                    }

                    Intent intent = new Intent(context, ImageActivity.class);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 1);
                }
            });
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
                boolean resolveIsSuccess = SKResolveJsonUtil.getInstance().resolveIsSuccess(json, Tea_Message_Detail_Activity.this);
                if (resolveIsSuccess) {
                    skSend_messge(questionId, "0");
                }
            }
        });
    }


    private void skSend_messge(final String questionId, String isSend) {
        Log.e("questionId", questionId);
        SKAsyncApiController.skSend_messge(questionId, "", isSend, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                Log.e("skSend_messge", content);
                boolean resolveIsSuccess = SKResolveJsonUtil.getInstance().resolveIsSuccess(content, Tea_Message_Detail_Activity.this);
                if (resolveIsSuccess) {
                    try {
                        JSONObject jsonObject = new JSONObject(content);
                        String data = jsonObject.optString("data");
                        String type = jsonObject.optString("type");
                        if (type.equals("confirm")) {
                            Builder dia = new Builder(Tea_Message_Detail_Activity.this);
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
                            Toast.makeText(Tea_Message_Detail_Activity.this, "发送成功", Toast.LENGTH_SHORT).show();
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

    // 没接收过该题，显示返回，接收，放弃
    private void Teather_Decide() {
        if (claim_uid.equals("0") && !hasGetQuestion) {
            // 没接收过该题，显示返回，接收，放弃 claim_uid=0
            bottomLayout.setVisibility(View.GONE);
            teaDecideLayout.setVisibility(View.VISIBLE);

        } else {
            bottomLayout.setVisibility(View.VISIBLE);
            teaDecideLayout.setVisibility(View.GONE);
            // 如果题目完成 语音隐藏 添加 显示 确定
            if (sKMessage.isDone()) {
                bottomLayout.setVisibility(View.GONE);
//                mSaveTikuBtn.setVisibility(View.VISIBLE);
                if (isNeedShowEndDialog) {
                    Builder builder = new Builder(context);
                    builder.setMessage("当前提问已结束");
                    builder.setNegativeButton("确定", null);
                    builder.show();
                    isNeedShowEndDialog = false;
                }
            }
        }
    }

    public void Qiang_Da(Context context) {
        Builder builder = new Builder(context);
        builder.setMessage("你是否要开始回答学生问题");
        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                Teather_QuestionId();
            }
        });
        builder.setNegativeButton("否", null);
        builder.show();
    }

    private void Teather_QuestionId() {
        String questionId = sKMessage.getQuestionId();
        SKAsyncApiController.Teather_Reception(questionId, new MyAsyncHttpResponseHandler(this, true) {
            @Override
            public void onSuccess(int arg0, String arg1) {
                super.onSuccess(arg0, arg1);
                boolean success = SKResolveJsonUtil.getInstance().resolveIsSuccess(arg1, context);
                if (success) {
                    bottomLayout.setVisibility(View.VISIBLE);
                    teaDecideLayout.setVisibility(View.GONE);
                    Fragment_Message.handler.sendEmptyMessage(MySKService.HAVE_NEW_MESSAGE);
                    YD.getInstence().getYD(context, PartnerConfig.TEA_YD_TOOL, true);
                    Toast.makeText(context, "接收成功", Toast.LENGTH_SHORT).show();
                    hasGetQuestion = true;
//                    HelpUtil.showHelp(Tea_Message_Detail_Activity.this, HelpUtil.HELP_TEA_3, null);
                }
            }
        });
    }

    public void Save_Dialog() {
        Builder builder = new Builder(context);
        builder.setTitle("提示");
        builder.setMessage("该题已交互结束，需要把该题存入题库吗？");
        builder.setPositiveButton("保存", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("sKMessage", sKMessage);
                Dialog.intent(context, Activity_Que_board2.class, bundle);
            }
        });
        builder.setNegativeButton("取消", null);
        builder.show();
    }

    // 保存題庫
    private void Giew_Up(Context context) {
        Builder builder = new Builder(context);
        builder.setMessage("你是否要放弃回答学生问题");
        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                Record_Stu(sKMessage.getQuestionId());
                Fragment_Message.handler.sendEmptyMessage(MySKService.HAVE_NEW_MESSAGE);
                finish();
            }
        });
        builder.setNegativeButton("否", null);
        builder.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!sKMessage.getMsgType().equals("1")) {
            Teather_Decide();
        }
    }

    private void Record_Stu(String quesid) {
        SKAsyncApiController.record_Stu(quesid, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String json) {
                super.onSuccess(json);
                SKResolveJsonUtil.getInstance().resolveIsSuccess(json, context);
                Toast.makeText(context, "放弃成功", Toast.LENGTH_SHORT).show();
            }
        });
    }

}