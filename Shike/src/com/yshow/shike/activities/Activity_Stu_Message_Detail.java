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
import com.umeng.analytics.MobclickAgent;
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
import com.yshow.shike.utils.MediaPlayerUtil;
import com.yshow.shike.utils.MediaRecorderUtil;
import com.yshow.shike.utils.MyAsyncHttpResponseHandler;
import com.yshow.shike.utils.Net_Servse;
import com.yshow.shike.utils.PartnerConfig;
import com.yshow.shike.utils.SKAsyncApiController;
import com.yshow.shike.utils.SKResolveJsonUtil;
import com.yshow.shike.utils.YD;

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
public class Activity_Stu_Message_Detail extends BaseActivity implements OnClickListener {
    private LinearLayout bottom_navigation;
    private ArrayList<SkMessage_Res> reslist;
    private List<String> bitmap_list = new ArrayList<String>(); // ViewPager显示的一个集合
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
    private DatabaseDao databaseDao;
    private List<String> query_voidce; // 查询所有数据库的录音
    private MediaPlayerUtil mediaPlayer;

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
        databaseDao = new DatabaseDao(context);
        query_voidce = databaseDao.Query();
        iniData();
        mediaRecorderUtil = new MediaRecorderUtil(this);
        MySKService.handler = handler;
    }

    @Override
    protected void onStop() {
        mediaPlayer.Stop_Play();
        super.onStop();
    }

    private void iniData() {
        Bundle bundle = getIntent().getExtras();
        sKMessage = (SKMessage) bundle.getSerializable("SKMessage");
        curquestionId = sKMessage.getQuestionId();
        ll_volume_control = findViewById(R.id.voice_recordding_layout);
        bottom_navigation = (LinearLayout) findViewById(R.id.ll_bottom_navigation);
        Button stu_voice = (Button) findViewById(R.id.record_btn);
        Button tv_tianjian = (Button) findViewById(R.id.img_button);
        mEndButton = (TextView) findViewById(R.id.right_button);
//        if (sKMessage.isDone()) {
            bottom_navigation.setVisibility(View.GONE);
            mEndButton.setVisibility(View.GONE);
//        }
        TextView nickName = (TextView) findViewById(R.id.nick_name);
        back_time = (TextView) findViewById(R.id.record_remain_text);
        tv_visits = (TextView) findViewById(R.id.tiwen_times);
        tv_data = (TextView) findViewById(R.id.tv_data);
        nickName.setText(sKMessage.getNickname());
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
        options = Net_Servse.getInstence().Picture_Shipei(R.drawable.back);
        imageLoader = ImageLoader.getInstance();
        myAdapter = new MyAdapter(reslist);
        viewPager.setAdapter(myAdapter);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setCurrentItem(pointion);
        findViewById(R.id.tv_tool_back).setOnClickListener(this);

        if (sKMessage.getMsgType().equals("1")) {// 这里表示是系统消息
            mEndButton.setVisibility(View.GONE);
            bottom_navigation.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.tv_three_back:
                finish();
                break;
            case R.id.img_button:// 学生端点击拍照提问
                intent = new Intent(this, Activity_Tea_Tool_Sele.class);
                intent.putExtra("isContinue", true);
                intent.putExtra("questionId", curquestionId);
                intent.putExtra("tea_add_tool", "1");
                startActivityForResult(intent, 1);
                // finish();
                break;
            case R.id.tv_tea_end:
                Thank_Teacher(Activity_Stu_Message_Detail.this);
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
                    mediaRecorderUtil = new MediaRecorderUtil(Activity_Stu_Message_Detail.this);
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
                                Toast.makeText(Activity_Stu_Message_Detail.this, "说话时间太短", Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(Activity_Stu_Message_Detail.this, "录音取消", Toast.LENGTH_SHORT).show();
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
            View view = View.inflate(Activity_Stu_Message_Detail.this, R.layout.img_page_item, null);
            ImageView iv_picture = (ImageView) view.findViewById(R.id.iv_picture);
            iv_picture.setTag(files);
//            GridView itme_gridview = (GridView) view.findViewById(R.id.itme_gridview);
//            FrameLayout iv_picture1 = (FrameLayout) view.findViewById(R.id.iv_picture1);
            if (LoginManage.getInstance().isTeacher()) {
//                iv_picture1.setBackgroundResource(R.drawable.teather_bg_message_image);
            }
//            // 这个是用来显示数据的一个集合
//            ArrayList<SkMessage_Voice> voice = reslist.get(position).getVoice();
//            for (int voice_count = 0; voice_count < voice.size(); voice_count++) {
//                itme_gridview.setSelection(voice.size());
//            }
//            itme_gridview.setAdapter(new GridviewAdapter(voice, Activity_Stu_Message_Detail.this));
//            // 添加是否读过语音的标识
//            itme_gridview.setOnItemClickListener(new OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    GridviewAdapter adapter = (GridviewAdapter) parent.getAdapter();
//                    SkMessage_Voice item = adapter.getItem(position);
//                    String file = item.getFile();
//                    // 对语音进行添加和判断判断
//                    if (!query_voidce.contains(file)) {
//                        databaseDao.insert(file);
//                    }
//                    adapter.notifyDataSetChanged();
//                    // 通知上一个界面刷新页面
//                    mediaPlayer.Down_Void(file, context);
//                }
//            });
            iv_picture.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    String file = skMessage_Res.getFile();
                    Bundle bundle = new Bundle();
                    bundle.putString("Message_Three", file);
                    bundle.putSerializable("res", sKMessage.getRes().get(position));
                    bundle.putBoolean("isdone", sKMessage.isDone());
                    Intent intent = new Intent(context, ImageActivity.class);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 1);
                }
            });
            imageLoader.displayImage(files, iv_picture, options);
            container.addView(view);
            bitmap_list.add(files);
            return view;
        }
    }

    class GridviewAdapter extends BaseAdapter {
        private ArrayList<SkMessage_Voice> voice;
        private Context context;

        public GridviewAdapter(ArrayList<SkMessage_Voice> voice, Context context) {
            super();
            this.context = context;
            this.voice = voice;
        }

        @Override
        public int getCount() {
            return voice.size();
        }

        @Override
        public SkMessage_Voice getItem(int position) {
            return voice.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            SkMessage_Voice skMessage_Voice = voice.get(position);
            String isStudent = skMessage_Voice.getIsStudent();
            String uid = skMessage_Voice.getUid();
            String uid2 = LoginManage.getInstance().getStudent().getUid();
            View view = View.inflate(context, R.layout.mess_three_item, null);
            ImageView voidce_image = (ImageView) view.findViewById(R.id.mess_voidce_make);
            ImageView red_point = (ImageView) view.findViewById(R.id.iv_red_point);
            List<String> query = databaseDao.Query();
            if (isStudent.equals("1")) { // 显示学生录音标示
                voidce_image.setBackgroundResource(R.drawable.teather_student);
                if (uid.equals(uid2)) {
                    red_point.setVisibility(View.GONE);
                } else {
                    if (query.contains(skMessage_Voice.getFile())) {
                        red_point.setVisibility(View.GONE);
                    } else {
                        red_point.setVisibility(View.VISIBLE);
                    }
                }
            } else {// 显示老师录音标示
                voidce_image.setBackgroundResource(R.drawable.teather_void);
                if (uid.equals(uid2)) {
                    red_point.setVisibility(View.GONE);
                } else {
                    if (query.contains(skMessage_Voice.getFile())) {
                        red_point.setVisibility(View.GONE);
                    } else {
                        red_point.setVisibility(View.VISIBLE);
                    }
                }
            }
            return view;
        }
    }

    private void skUploadMp3(final String questionId, final String imgid, String mapPath, String posID) {
        SKAsyncApiController.skUploadMp3(questionId, imgid, mapPath, posID, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String json) {
                super.onSuccess(json);
                Log.e("skUploadeImage", json);
                boolean resolveIsSuccess = SKResolveJsonUtil.getInstance().resolveIsSuccess(json, Activity_Stu_Message_Detail.this);
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
                            Toast.makeText(context, "確定結束消息", Toast.LENGTH_SHORT).show();
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("message", sKMessage);
                            Dialog.intent(context, Activity_Thank_Teacher.class, bundle);
                            finish();
                        } else {
                            Toast.makeText(context, "確定結束消息失败", Toast.LENGTH_SHORT).show();
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
                boolean resolveIsSuccess = SKResolveJsonUtil.getInstance().resolveIsSuccess(content, Activity_Stu_Message_Detail.this);
                if (resolveIsSuccess) {
                    try {
                        JSONObject jsonObject = new JSONObject(content);
                        String data = jsonObject.optString("data");
                        String type = jsonObject.optString("type");
                        if (type.equals("confirm")) {
                            Builder dia = new Builder(Activity_Stu_Message_Detail.this);
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
                            Toast.makeText(Activity_Stu_Message_Detail.this, "发送成功", Toast.LENGTH_SHORT).show();
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