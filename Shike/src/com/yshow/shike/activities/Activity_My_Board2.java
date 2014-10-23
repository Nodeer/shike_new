package com.yshow.shike.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;
import com.yshow.shike.R;
import com.yshow.shike.UIApplication;
import com.yshow.shike.entity.SKMessage;
import com.yshow.shike.entity.SKTeacherOrSubject;
import com.yshow.shike.entity.SkUpLoadQuestion;
import com.yshow.shike.utils.*;

/**
 * 题目编辑完成以后,选择老师页面,
 */
public class Activity_My_Board2 extends Activity implements OnClickListener {
    private String subjectid = "-1";
    private TextView subjectText, myTeacherText; // 被选的科目
    private String teacherId = "-1";
    private Boolean mark = false; // 在线老师背景框的控制
    private Bitmap bitmap;
    private SkUpLoadQuestion skUpLoadQuestion;
    private boolean isTry_messge;
    private String isTry_questionId;
    private Context context;
    private ImageView isOnlineImg, isMyTeaImg;// 在线老师红色背景图
    private ArrayList<String> urllist;// 语音的本地路径
    private Send_Message send_Message; // 创建发送消息对象

    //    private FrameLayout selectTeacherFrame;
//    private RelativeLayout onlineTeachereaFrame;
    private TextView onLineTeacherButton;

    public static Bitmap saveBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_board2);
        context = this;
        IniDate();
    }

    @SuppressWarnings("unchecked")
    private void IniDate() {
        ImageView board_picture = (ImageView) findViewById(R.id.iv_board_picture);
        findViewById(R.id.tv_up_step).setOnClickListener(this);
        findViewById(R.id.tv_down_step).setOnClickListener(this);
//		selectTeacherFrame = (FrameLayout) findViewById(R.id.ll_seleck_teacher);
//        onlineTeachereaFrame = (RelativeLayout) findViewById(R.id.online_tea_frame);
        onLineTeacherButton = (TextView) findViewById(R.id.tv_online);
        onLineTeacherButton.setOnClickListener(this);
        subjectText = (TextView) findViewById(R.id.tv_select_subject);
        myTeacherText = (TextView) findViewById(R.id.tv_seleck_teacher);
        subjectText.setOnClickListener(this);
        myTeacherText.setOnClickListener(this);
        isOnlineImg = (ImageView) findViewById(R.id.is_online);
        isMyTeaImg = (ImageView) findViewById(R.id.iv_my_image);
        View select_subject = findViewById(R.id.ll_seleck_subject);
        select_subject.setOnClickListener(this);
        isTry_messge = getIntent().getBooleanExtra("try_messge", false);
        if (isTry_messge) {
            select_subject.setClickable(false);
            SKMessage message = (SKMessage) getIntent().getSerializableExtra("message");
            String file = message.getRes().get(0).getFile();
            ImageLoader imageLoader = ImageLoader.getInstance();
            imageLoader.displayImage(file, board_picture);
            isTry_questionId = message.getQuestionId();
            subjectid = message.getSubjectId();
            skGetSubject(subjectid);
            board_picture.setImageBitmap(bitmap);
        } else {
            bitmap = saveBitmap;
            urllist = (ArrayList<String>) getIntent().getExtras().get("urllist");

            skUpLoadQuestion = new SkUpLoadQuestion(bitmap, urllist);
            board_picture.setImageBitmap(bitmap);
            // image 原图
            List<String> picUrls = UIApplication.getInstance().getPicUrls();
            int screenWidth = ScreenSizeUtil.ScreenWidth;
            int screenHeight = ScreenSizeUtil.ScreenHeight;
            //这里换成之前的高清大图
//            bitmap = Dialog.getbitmap(picUrls.get(0), screenWidth, screenHeight);
        }
        // 对是否是从我的老师的提问按钮进行交互的判断
        String tiwen_teacher_Id = PartnerConfig.TEATHER_ID;
        String tea_name = PartnerConfig.TEATHER_NAME;
        String tiwen_subject_id = PartnerConfig.SUBJECT_ID;
        String sub_name = PartnerConfig.SUBJECT_NAME;
        if (tiwen_teacher_Id != null || tea_name != null || tiwen_subject_id != null || sub_name != null) {//如果是从我的老师提问过来的
            subjectid = tiwen_subject_id;
            teacherId = tiwen_teacher_Id;
            subjectText.setText(sub_name);
            myTeacherText.setText(tea_name);
//            onLineTeacherButton.setEnabled(false);
//            onlineTeachereaFrame.setBackgroundResource(R.color.bg_gray);
            isMyTeaImg.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus){
            HelpUtil.showHelp(this, HelpUtil.HELP_STU_5, findViewById(R.id.stu_slet_tea));
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            // 上一步
            case R.id.tv_up_step:
                finish();
                break;
            // 下一步 按钮
            case R.id.tv_down_step:
                Non_User();
                break;
            // 选择学科
            case R.id.tv_select_subject:
                intent = new Intent(context, Activity_SelectSubjectOrTeacher.class);
                intent.putExtra("select", Activity_SelectSubjectOrTeacher.ISSUBJECT);
                startActivityForResult(intent, 101);
                break;
            // 选择老师
            case R.id.tv_seleck_teacher:
                if (!subjectid.equals("-1")) {
                    intent = new Intent(context, Activity_SelectSubjectOrTeacher.class);
                    intent.putExtra("select", Activity_SelectSubjectOrTeacher.ISTEACHER);
                    intent.putExtra("subjectid", subjectid);
                    startActivityForResult(intent, 102);
                } else {
                    Toast.makeText(this, "请选择学科", Toast.LENGTH_LONG).show();
                }
                break;
            // 在线老师
            case R.id.tv_online:
                if (!mark) {
                    isOnlineImg.setVisibility(View.VISIBLE);
                    isMyTeaImg.setVisibility(View.GONE);
                    teacherId = "0";
                } else {
                    isOnlineImg.setVisibility(View.GONE);
                    isMyTeaImg.setVisibility(View.GONE);
                }
                mark = !mark;
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
        if (UIApplication.getInstance().getAuid_flag()) {
            if (isTry_messge) {
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
                    Toast.makeText(this, "请选择学科", 0).show();
                }
            } else if (isTry_messge) {
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
        if (!subjectid.equals("-1")) {
            if (!teacherId.equals("-1")) {
                if(!teacherId.equals("0")){
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
                }else{//发给在线老师
                    send_Message = new Send_Message(context, bitmap, skUpLoadQuestion, teacherId);
                    send_Message.skCreateQuestion(subjectid);
                }
            } else {
                Toast.makeText(this, "你还没选择老师呢！", 0).show();
            }
        } else {
            Toast.makeText(this, "请选择学科", 0).show();
        }
    }

    // 获取学科
    private void skGetSubject(final String subject) {
        SKAsyncApiController.skGetSubject(new MyAsyncHttpResponseHandler(this, true) {
            @Override
            public void onSuccess(String arg0) {
                super.onSuccess(arg0);
                ArrayList<SKTeacherOrSubject> subjects = SKResolveJsonUtil.getInstance().resolveSubject(arg0);
                for (SKTeacherOrSubject skTeacherOrSubject : subjects) {
                    boolean equals = skTeacherOrSubject.getSubjectId().equals(subject);
                    if (equals) {
                        subjectText.setText(skTeacherOrSubject.getName());
                    }
                }
            }
        });
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
                        boolean isSuccess = SKResolveJsonUtil.getInstance().resolveIsSuccess(json, Activity_My_Board2.this);
                        if (isSuccess) {
                            Activity_My_Board2.this.finish();
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 101) {
            SKTeacherOrSubject item = (SKTeacherOrSubject) data.getSerializableExtra("selectSubject");
            if (item != null) {
                subjectid = item.getSubjectId();
                subjectText.setText(item.getName());
            }
        }
        if (resultCode == 102) {
            SKTeacherOrSubject item = (SKTeacherOrSubject) data.getSerializableExtra("selectTeacher");
            if (item != null) {
                subjectid = item.getSubjectId();
                teacherId = item.getTeacherId();
                myTeacherText.setText(item.getName());
                isOnlineImg.setVisibility(View.GONE);
                isMyTeaImg.setVisibility(View.VISIBLE);
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
//        saveBitmap.recycle();
//        saveBitmap = null;
        PartnerConfig.TEATHER_ID = null;
        PartnerConfig.SUBJECT_ID = null;
        PartnerConfig.TEATHER_NAME = null;
        PartnerConfig.SUBJECT_NAME = null;
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