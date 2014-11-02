package com.yshow.shike.activities;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.yshow.shike.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yshow.shike.entity.SKArea;
import com.yshow.shike.entity.SKMessage;
import com.yshow.shike.entity.Star_Teacher_Parse;
import com.yshow.shike.entity.Student_Info;
import com.yshow.shike.utils.MyAsyncHttpResponseHandler;
import com.yshow.shike.utils.SKAsyncApiController;
import com.yshow.shike.utils.SKResolveJsonUtil;

import java.util.ArrayList;

/**
 * 老师点击列表头像进入学生主页
 *
 * @author
 */
public class ActicityStudentInfo extends BaseActivity {
    private TextView tv_stu_nick;
    private TextView tv_stu_name;
    private TextView tv_stu_day;
    private TextView tv_stu_ti_wen;
    private ImageView tudent_picture;
    private Star_Teacher_Parse stu_Info;
    private SKMessage extra;

    private TextView diqu, ziwojieshao;
    Student_Info student_Info;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_student_info);
        initData();
    }

    private void initData() {
        Bundle bundleExtra = getIntent().getExtras();
        stu_Info = (Star_Teacher_Parse) bundleExtra.getSerializable("Star_Teacher_Parse");
        extra = (SKMessage) bundleExtra.getSerializable("teather_sKMessage");//这条比较常用..老师从消息列表点击头像进来
        tv_stu_nick = (TextView) findViewById(R.id.tv_stu_nick);
        tv_stu_name = (TextView) findViewById(R.id.tv_stu_name);
        tv_stu_day = (TextView) findViewById(R.id.tv_stu_day);
        tv_stu_ti_wen = (TextView) findViewById(R.id.tv_jieti_count);
        diqu = (TextView) findViewById(R.id.tv_zan_coun);
        ziwojieshao = (TextView) findViewById(R.id.tv_guan_zhu);
        tudent_picture = (ImageView) findViewById(R.id.tudent_picture);
        findViewById(R.id.left_btn).setOnClickListener(listener);
        None_Null();
    }

    private OnClickListener listener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.left_btn:
                    finish();
                    break;
            }
        }
    };

    // 对消息页面和我的学生页面的对象进行非空判断
    private void None_Null() {
        if (extra != null) {
            String stu_uid = extra.getUid();
            Student_InFo(stu_uid);
        }
        if (stu_Info != null) {
            stu_info();
        }
    }

    private void Student_InFo(String uid) {
        SKAsyncApiController.User_Info(uid, new MyAsyncHttpResponseHandler(this, true) {
            @Override
            public void onSuccess(String json) {
                super.onSuccess(json);
                boolean success = SKResolveJsonUtil.getInstance().resolveIsSuccess(json, ActicityStudentInfo.this);
                if (success) {
                    student_Info = SKResolveJsonUtil.getInstance().Student_Info_Pase(json);
                    tv_stu_nick.setText("用户名：" + student_Info.getNickname());
                    tv_stu_name.setText("姓    名：" + student_Info.getName());
                    tv_stu_day.setText("学龄段：" + student_Info.getGrade() + "" + student_Info.getGradeName());
                    tv_stu_ti_wen.setText("提问次数：" + student_Info.getQuestions());
                    ziwojieshao.setText(student_Info.getInfo());
                    Init_Picture(student_Info.getIcon(), tudent_picture);
                }
            }
        });
    }

    // 从我的学生页面跳转过来 设置学生的基本信息
    private void stu_info() {
        tv_stu_nick.setText("用户名 :" + stu_Info.getNickname());
        tv_stu_name.setText("姓    名:" + stu_Info.getName());
        tv_stu_day.setText("学龄段 :" + stu_Info.getGrade() + "" + stu_Info.getGradeName());
        tv_stu_ti_wen.setText("提问次数：" + stu_Info.getQuestions());
        Init_Picture(stu_Info.getIcon(), tudent_picture);
    }

    // 设置头像
    private void Init_Picture(String url, ImageView imageView) {
        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheOnDisc(true)
                .showImageForEmptyUri(R.drawable.teather_stu_picture).showImageOnFail(R.drawable.teather_stu_picture).cacheInMemory(true)
                .build();
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(url, imageView, options);
    }

    /**
     * 获取所有的地区
     */
    private void skGetArea() {
        SKAsyncApiController.skGetArea(new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String arg0) {
                super.onSuccess(arg0);
                ArrayList<SKArea> resolveArea = SKResolveJsonUtil.getInstance().resolveArea(arg0);
                for (int i = 0; i < resolveArea.size(); i++) {
                    SKArea skArea = resolveArea.get(i);
                    try {
                        String area = student_Info.areaid;
                        if (skArea.getId().equals(area)) {
                            diqu.setText("所在地区：" + skArea.getName());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}