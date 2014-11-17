package com.yshow.shike.activities;

import java.io.Serializable;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;
import com.yshow.shike.R;
import com.yshow.shike.entity.SKMessage;
import com.yshow.shike.entity.Star_Teacher_Parse;
import com.yshow.shike.entity.User_Info;
import com.yshow.shike.utils.Dialog;
import com.yshow.shike.utils.ImageLoadOption;
import com.yshow.shike.utils.MyAsyncHttpResponseHandler;
import com.yshow.shike.utils.Net_Servse;
import com.yshow.shike.utils.PartnerConfig;
import com.yshow.shike.utils.SKAsyncApiController;
import com.yshow.shike.utils.SKResolveJsonUtil;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 学生登录状态下,显示我的老师详情页面
 */
public class TeacherInfoActivity extends BaseActivity implements OnClickListener {
    private Star_Teacher_Parse item;
    private boolean flag = true;
    private TextView tv_attention;
    private Context context;
    private TextView tv_shike_fen;
    private DisplayImageOptions options, options2;
    private ImageLoader imageLoader;

    private RelativeLayout guanzhuBtn;
    private ImageView guanzhuImg;
    private TextView tv_guan_zhu;
    private ImageView tudent_picture;
    private TextView tv_zan_shu;
    private TextView teacher_nick;
    private TextView tv_subject;
    private TextView strdent_count;
    private SKMessage extra;

    private String teaUid;


    private User_Info my_teather;

    private boolean isFromMyTeacher = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_info);
        initDate();
    }

    private void initDate() {
        context = this;

        isFromMyTeacher = getIntent().getBooleanExtra("ismyTeacher", false);

        TextView titletext = (TextView) findViewById(R.id.title_text);
        titletext.setText("老师信息");
        findViewById(R.id.left_btn).setOnClickListener(this);


        options = ImageLoadOption.getTeaHeadImageOption();
        options2 = ImageLoadOption.getTeaHeadGrayImageOption();
        imageLoader = ImageLoader.getInstance();

        teacher_nick = (TextView) findViewById(R.id.tv_teacher_nick);
        tv_subject = (TextView) findViewById(R.id.tv_subject);

        strdent_count = (TextView) findViewById(R.id.tv_strdent_count);

        tv_guan_zhu = (TextView) findViewById(R.id.tv_guan_zhu);
        tudent_picture = (ImageView) findViewById(R.id.tudent_picture);
        tv_attention = (TextView) findViewById(R.id.guanzhu_text);

        tv_zan_shu = (TextView) findViewById(R.id.tv_zan_coun);
        guanzhuImg = (ImageView) findViewById(R.id.guanzhu_img);

        guanzhuBtn = (RelativeLayout) findViewById(R.id.guanzhu_btn);
        guanzhuBtn.setOnClickListener(this);

        tv_shike_fen = (TextView) findViewById(R.id.tv_jieti_count);
        item = (Star_Teacher_Parse) getIntent().getSerializableExtra("headpicture");
        if (item != null) {
            teaUid = item.getUid();
            MyShiKeInfo(teaUid);
        } else {
            String uid = "";
            extra = (SKMessage) getIntent().getSerializableExtra("teather_sKMessage");
            if (!extra.getTeachUid().equals("0")) {
                uid = extra.getTeachUid();
            } else {
                uid = extra.getClaim_uid();
            }
            MyShiKeInfo(uid);
        }
    }

    private void MyShiKeInfo(String uid) {
        SKAsyncApiController.User_Info(uid, new MyAsyncHttpResponseHandler(this, true) {
            public void onSuccess(int arg0, String json) {
                boolean atent_Success = SKResolveJsonUtil.getInstance().resolveIsSuccess(json, context);
                if (atent_Success) {
                    my_teather = SKResolveJsonUtil.getInstance().My_teather1(json);
                    teaUid = my_teather.getUid();
                    teacher_nick.setText(my_teather.getNickname());
                    strdent_count.setText("学生数量:" + my_teather.getFansNum());
                    tv_subject.setText("学科：" + my_teather.getGrade() + my_teather.getSubject());
                    tv_guan_zhu.setText(my_teather.getInfo());
                    if (my_teather.isonline) {
                        imageLoader.displayImage(my_teather.getPicurl(), tudent_picture, options);
                    } else {
                        imageLoader.displayImage(my_teather.getPicurl(), tudent_picture, options2);
                    }
                    tv_shike_fen.setText("接题次数：" + my_teather.getClaim_question_num());
                    tv_zan_shu.setText("学生赞美：" + my_teather.getLike_num());
                    if (my_teather.is_myteacher) {
                        if (isFromMyTeacher) {
                            tv_attention.setText("取消关注");
                        } else {
                            tv_attention.setText("已关注");
                        }
                        guanzhuImg.setImageResource(R.drawable.guanzhu_big_icon_balck);
                        guanzhuBtn.setBackgroundResource(R.drawable.gray_btn_bg);
                        flag = true;
                    } else {
                        tv_attention.setText("关注");
                        guanzhuImg.setImageResource(R.drawable.guanzhu_big_icon_white);
                        guanzhuBtn.setBackgroundResource(R.drawable.stu_slide_btn1_bg);
                        flag = false;
                    }
                    refreshRightBtn();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_btn:
                finish();
                break;
            case R.id.guanzhu_btn:
                if (flag) {
                    Teather_Info_QuXiao();
                } else {
                    Teather_Info_Attention();
                }
                break;
            case R.id.next_btn:
                PartnerConfig.TEATHER_ID = my_teather.getUid();
                PartnerConfig.SUBJECT_ID = my_teather.getSubjectid();
                PartnerConfig.TEATHER_NAME = my_teather.getNickname();
                PartnerConfig.SUBJECT_NAME = my_teather.getSubject();
                PartnerConfig.TEACHER_IMG = my_teather.getPicurl();
                Dialog.Intent(context, Activity_Stu_Ask_Step1.class);
                break;
        }
    }

    private void refreshRightBtn() {
        if (flag) {
            TextView tiwenBtn = (TextView) findViewById(R.id.next_btn);
            tiwenBtn.setVisibility(View.VISIBLE);
            tiwenBtn.setOnClickListener(this);
        } else {
            TextView tiwenBtn = (TextView) findViewById(R.id.next_btn);
            tiwenBtn.setVisibility(View.GONE);
        }
    }

    // 关注老师 联网操作
    private void Teather_Info_Attention() {
        SKAsyncApiController.Attention_Taeather_Parse(teaUid, new MyAsyncHttpResponseHandler(this, true) {
            public void onSuccess(int arg0, String jion) {
                boolean success = SKResolveJsonUtil.getInstance().resolveIsSuccess(jion, context);
                if (success) {
                    if (isFromMyTeacher) {
                        tv_attention.setText("取消关注");
                    } else {
                        tv_attention.setText("已关注");
                    }
                    guanzhuImg.setImageResource(R.drawable.guanzhu_big_icon_balck);
                    guanzhuBtn.setBackgroundResource(R.drawable.gray_btn_bg);
                    flag = true;
                    refreshRightBtn();
                }
            }
        });
    }

    // 取消关注老师 联网操作
    private void Teather_Info_QuXiao() {
        SKAsyncApiController.Qu_Xiao_GuanZhu(teaUid, new MyAsyncHttpResponseHandler(this, true) {
            public void onSuccess(String jion) {
                boolean success = SKResolveJsonUtil.getInstance().resolveIsSuccess(jion, context);
                if (success) {
                    tv_attention.setText("关注");
                    guanzhuImg.setImageResource(R.drawable.guanzhu_big_icon_white);
                    guanzhuBtn.setBackgroundResource(R.drawable.stu_slide_btn1_bg);
                    flag = false;
                    refreshRightBtn();
                }
            }
        });
    }
}
