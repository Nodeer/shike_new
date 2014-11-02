package com.yshow.shike.activities;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yshow.shike.R;
import com.yshow.shike.SubjectIds;
import com.yshow.shike.UIApplication;
import com.yshow.shike.entity.SKMessage;
import com.yshow.shike.entity.SKTeacherOrSubject;
import com.yshow.shike.entity.SkUpLoadQuestion;
import com.yshow.shike.utils.*;

/**
 * 题目编辑完成以后,选择老师页面,
 */
public class ActivitySelectTeacher extends BaseActivity implements OnClickListener {
    private String subjectid = "-1";
    private RelativeLayout myTeacherButton;
    private String teacherId = "0";
    private Bitmap bitmap;
    private SkUpLoadQuestion skUpLoadQuestion;
    private boolean isReSendmessge;
    private String isTry_questionId;
    private Context context;
    private ImageView isOnlineImg, isMyTeaImg;// 在线老师红色背景图
    private ArrayList<String> urllist;// 语音的本地路径
    private Send_Message send_Message; // 创建发送消息对象

    private RelativeLayout onLineTeacherButton;

    public static Bitmap saveBitmap;

    private TextView mTeacherName;
    private ImageView mTeacherImg;
    private ImageLoader imageLoader;

    private TextView mTeaTextView, onlineTeaTextview;
    private ImageView onLineTeacherImg;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_teacher_layout);
        context = this;
        initData();
    }

    private void initData() {
        findViewById(R.id.tv_tool_back).setOnClickListener(this);
        findViewById(R.id.next_btn).setOnClickListener(this);
        mTeaTextView = (TextView) findViewById(R.id.my_teacher_text);
        onlineTeaTextview = (TextView) findViewById(R.id.online_tea_text);
        onLineTeacherButton = (RelativeLayout) findViewById(R.id.online_tea_button);
        onLineTeacherButton.setOnClickListener(this);
        myTeacherButton = (RelativeLayout) findViewById(R.id.my_tea_button);
        myTeacherButton.setOnClickListener(this);
        isOnlineImg = (ImageView) findViewById(R.id.online_tea_select_icon);
        isMyTeaImg = (ImageView) findViewById(R.id.my_tea_select_icon);
        mTeacherName = (TextView) findViewById(R.id.teacher_name);
        mTeacherImg = (ImageView) findViewById(R.id.teacher_img);
        onLineTeacherImg = (ImageView) findViewById(R.id.online_tea_img);
        isReSendmessge = getIntent().getBooleanExtra("try_messge", false);

        imageLoader = ImageLoader.getInstance();
        if (isReSendmessge) {
            SKMessage message = (SKMessage) getIntent().getSerializableExtra("message");
            isTry_questionId = message.getQuestionId();
            subjectid = message.getSubjectId();
        } else {
            bitmap = saveBitmap;
            urllist = (ArrayList<String>) getIntent().getExtras().get("urllist");
            subjectid = SubjectIds.mSubjectId;
            skUpLoadQuestion = new SkUpLoadQuestion(bitmap, urllist);
        }
        // 对是否是从我的老师的提问按钮进行交互的判断
        String tiwen_teacher_Id = PartnerConfig.TEATHER_ID;
        String tea_name = PartnerConfig.TEATHER_NAME;
        String tiwen_subject_id = PartnerConfig.SUBJECT_ID;
        String sub_name = PartnerConfig.SUBJECT_NAME;
        String tea_img = PartnerConfig.TEACHER_IMG;
        if (tiwen_teacher_Id != null && tea_name != null && tiwen_subject_id != null && sub_name != null && tea_img != null) {//如果是从我的老师提问过来的
            subjectid = tiwen_subject_id;
            teacherId = tiwen_teacher_Id;
            isMyTeaImg.setVisibility(View.VISIBLE);
            imageLoader.displayImage(tea_img, mTeacherImg, ImageLoadOption.getImageOption(R.drawable.my_tea_big_icon_green));
            mTeacherName.setVisibility(View.VISIBLE);
            mTeacherName.setText(tea_name);
            mTeaTextView.setTextColor(Color.rgb(135, 184, 93));
            myTeacherButton.setBackgroundResource(R.drawable.white_btn_green_bg);
            onlineTeaTextview.setTextColor(Color.WHITE);
            onLineTeacherButton.setBackgroundResource(R.drawable.stu_slide_btn1_bg);
            onLineTeacherImg.setImageResource(R.drawable.online_tea_big_icon);

        } else {
            isOnlineImg.setVisibility(View.VISIBLE);
            myTeacherButton.setBackgroundResource(R.drawable.stu_slide_btn1_bg);
            mTeaTextView.setTextColor(Color.WHITE);
            mTeacherImg.setImageResource(R.drawable.my_tea_big_icon);
            onLineTeacherButton.setBackgroundResource(R.drawable.white_btn_green_bg);
            onLineTeacherImg.setImageResource(R.drawable.online_tea_big_icon_green);
            onlineTeaTextview.setTextColor(Color.rgb(135, 184, 93));

        }
    }


    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            // 上一步
            case R.id.tv_tool_back:
                finish();
                break;
            // 下一步 按钮
            case R.id.next_btn:
                Non_User();
                break;
            // 选择老师
            case R.id.my_tea_button:
                intent = new Intent(context, ActivitySelectTeacherList.class);
                intent.putExtra("subjectid", subjectid);
                startActivityForResult(intent, 102);
                break;
            // 在线老师
            case R.id.online_tea_button:
                isOnlineImg.setVisibility(View.VISIBLE);
                isMyTeaImg.setVisibility(View.GONE);
                teacherId = "0";
                mTeacherName.setVisibility(View.GONE);
                myTeacherButton.setBackgroundResource(R.drawable.stu_slide_btn2_bg);
                mTeaTextView.setTextColor(Color.WHITE);
                mTeacherImg.setImageResource(R.drawable.my_tea_big_icon);
                onLineTeacherButton.setBackgroundResource(R.drawable.white_btn_green_bg);
                onLineTeacherImg.setImageResource(R.drawable.online_tea_big_icon_green);
                onlineTeaTextview.setTextColor(R.color.stu_slide_btn1_bg_nor);
                break;
        }
    }

    /**
     * 注册用户和体验用户区别
     */
    private void Non_User() {
        // 获取自动登录 昵称
        FileService auto_sp = new FileService(context);
        String auto_nickName = auto_sp.getSp_Date("auto_user_name");
        if (!UIApplication.getInstance().isTestUser) {
            if (isReSendmessge) {
                tryQusetion();
            } else {
                //正常注册 发送信息
                Send_New_Mess();
            }
        } else {
            //从立即登录进来的 发送信息
            if (TextUtils.isEmpty(auto_nickName)) {
                if (!subjectid.equals("-1")) {
                    Bundle bundle = new Bundle();
                    bundle.putString("subjectid", subjectid);
                    bundle.putStringArrayList("urllist", urllist);
                    Dialog.intent(context, Com_Per_Data.class, bundle);
                } else {
                    Toast.makeText(this, "请选择学科", Toast.LENGTH_SHORT).show();
                }
            } else if (isReSendmessge) {
                tryQusetion();
            } else {
                Send_New_Mess();
            }
        }
    }

    /**
     * 发送新消息
     */
    private void Send_New_Mess() {
        if (!teacherId.equals("0")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("同学，您现在是找“我的老师”为您解答，如果长时间不回答，希望您重发给其他在线老师！");
            builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    send_Message = new Send_Message(context, bitmap, skUpLoadQuestion, teacherId);
                    send_Message.skCreateQuestion(subjectid);
                }
            });
            builder.show();
        } else {//发给在线老师
            send_Message = new Send_Message(context, bitmap, skUpLoadQuestion, teacherId);
            send_Message.skCreateQuestion(subjectid);
        }
    }

    /**
     * 消息从发
     */
    private void tryQusetion() {
        SKAsyncApiController.ChongFa(isTry_questionId, teacherId,
                new MyAsyncHttpResponseHandler(this, true) {
                    @Override
                    public void onSuccess(String json) {
                        super.onSuccess(json);
                        boolean isSuccess = SKResolveJsonUtil.getInstance().resolveIsSuccess(json, ActivitySelectTeacher.this);
                        if (isSuccess) {
                            ActivitySelectTeacher.this.finish();
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            SKTeacherOrSubject item = (SKTeacherOrSubject) data.getSerializableExtra("selectTeacher");
            if (item != null) {
                teacherId = item.getTeacherId();
                isMyTeaImg.setVisibility(View.VISIBLE);
                isOnlineImg.setVisibility(View.GONE);
                imageLoader.displayImage(item.icon, mTeacherImg, ImageLoadOption.getImageOption(R.drawable.my_tea_big_icon_green));
                mTeacherName.setVisibility(View.VISIBLE);
                mTeacherName.setText(item.getName());
                myTeacherButton.setBackgroundResource(R.drawable.white_btn_green_bg);
                mTeaTextView.setTextColor(Color.rgb(135, 184, 93));
                onlineTeaTextview.setTextColor(Color.WHITE);
                onLineTeacherButton.setBackgroundResource(R.drawable.stu_slide_btn1_bg);
                onLineTeacherImg.setImageResource(R.drawable.online_tea_big_icon);
            }
        }
    }

    /**
     * 发送完消息回返回小子页面的回调接口
     */
    public interface Callback {
        public void back(String str);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PartnerConfig.TEATHER_ID = null;
        PartnerConfig.SUBJECT_ID = null;
        PartnerConfig.TEATHER_NAME = null;
        PartnerConfig.SUBJECT_NAME = null;
    }
}