package com.yshow.shike.activities;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.format.DateFormat;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.TranslateAnimation;
import android.widget.*;
import android.widget.LinearLayout.LayoutParams;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.umeng.analytics.MobclickAgent;
import com.yshow.shike.R;
import com.yshow.shike.entity.LoginManage;
import com.yshow.shike.entity.VersionModel;
import com.yshow.shike.fragments.Fragment_Message;
import com.yshow.shike.fragments.Fragment_Teacher_About_Shike;
import com.yshow.shike.fragments.Fragment_Teacher_My_Student;
import com.yshow.shike.fragments.Fragment_Teacher_Writing_Topic;
import com.yshow.shike.fragments.StuContentFragment;
import com.yshow.shike.fragments.StuSlideMenuFragment;
import com.yshow.shike.fragments.TeaContentFragment;
import com.yshow.shike.fragments.TeaSlideMenuFragment;
import com.yshow.shike.service.MySKService;
import com.yshow.shike.utils.*;

import java.io.File;
import java.util.Calendar;
import java.util.Locale;

/**
 * 老师主页面
 */
public class Teather_Main_Activity extends SlidingFragmentActivity implements OnClickListener {
    private int mea_view; // 长度测量
    private View undline, bac_topic; // 需要测量的红线 和字体的背景颜色
    private FragmentManager manager; // Fragment管理器
    private FragmentTransaction ft; // Fragment 转化活动
    private int startX = 0; // 当前一道第几个位置
    private Context context; // 上下文
    private RelativeLayout ic_tea_buttom;// 底部隱藏部分
    private Boolean isUnfold = false;// 底部动画转换标记
    private android.app.Dialog dialog; // 分享功能的dialo动画
    private TextView tv_mess_del; // 消息删除
    private TextView my_stu, tea_topic, tea_mess;// 我的学生 题库 消息 标题按钮
    public static Teather_Main_Activity Tea_Main_mInstance;
    private WeixinManager weixinManager;
    private ImageView mess_num;

    private String localVersion = "";
    private TeaContentFragment mContent;
    private SlidingMenu sm;

    // private ImageView TEA_YD,TEA_TOOL_YD;
    // private View tea_main_view_bac,tea_tool_view_white;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teather_main_activity);
        context = this;
        Tea_Main_mInstance = this;
        initData();
        SKAsyncApiController.Sof_Info(new MyAsyncHttpResponseHandler(context, true) {
            @Override
            public void onSuccess(String json) {
                super.onSuccess(json);
                VersionModel model = SKResolveJsonUtil.getInstance().getNewVersion(json);
                checkNewVersion(model);
            }
        });

        initSlide();
    }


    private void initSlide() {
        setContentView(R.layout.main_frame);
        setBehindContentView(R.layout.menu_frame);
        getSlidingMenu().setSlidingEnabled(true);
        getSlidingMenu()
                .setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        mContent = new TeaContentFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, mContent).commit();
        // set the Behind View Fragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.menu_frame, new TeaSlideMenuFragment()).commit();
        // customize the SlidingMenu
        sm = getSlidingMenu();
        sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        sm.setFadeEnabled(true);
        sm.setBehindScrollScale(0.5f);//位移.数字越大,水平位移越小
        sm.setFadeDegree(0.5f);//透明度变化.数字越大,透明度变化越明显
        sm.setBehindCanvasTransformer(new SlidingMenu.CanvasTransformer() {
            @Override
            public void transformCanvas(Canvas canvas, float percentOpen) {
                float scale = (float) (percentOpen * 0.25 + 0.75);
                canvas.scale(scale, scale, -canvas.getWidth() / 2,
                        canvas.getHeight() / 2);
            }
        });

        sm.setAboveCanvasTransformer(new SlidingMenu.CanvasTransformer() {
            @Override
            public void transformCanvas(Canvas canvas, float percentOpen) {
                float scale = (float) (1 - percentOpen * 0.25);
                canvas.scale(scale, scale, 0, canvas.getHeight() / 2);
            }
        });
    }

    private void checkNewVersion(VersionModel model) {
        if (model != null) {
            float old = Float.parseFloat(localVersion);
            float newversion = Float.parseFloat(model.version);
            final String url = model.url;
            if (old < newversion) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("检测到新版本,是否更新?");
                builder.setNegativeButton("取消", null);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        doUpdate(url);
                    }
                });
                builder.show();
            }
        }
    }

    private void doUpdate(String url) {
        DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        File dir = new File(Environment.getExternalStorageDirectory() + "/shike/app");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        request.setDestinationInExternalPublicDir("/shike/app", "shike.apk");
        if (Build.VERSION.SDK_INT >= 11) {
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        }
        request.setTitle("新版本下载中");
        request.setDescription(url);
        downloadManager.enqueue(request);
        Toast.makeText(Teather_Main_Activity.this, "开始下载新版本", Toast.LENGTH_SHORT).show();
    }

    // 各个控件初始化
    private void initData() {
//        dialog = new Dilog_Share().Dilog_Anim(context, this);
//        weixinManager = new WeixinManager(Teather_Main_Activity.this);
//        tea_mess = (TextView) findViewById(R.id.tv_tea_mess);
//        mess_num = (ImageView) findViewById(R.id.mess_num);
//        my_stu = (TextView) findViewById(R.id.tv_my_stu);
//        tea_topic = (TextView) findViewById(R.id.tv_tea_topic);
//        tea_mess.setOnClickListener(this);
//        my_stu.setOnClickListener(this);
//        tea_topic.setOnClickListener(this);
//        findViewById(R.id.tea_wire).setOnClickListener(this);
//        findViewById(R.id.tv_exchg_sp).setOnClickListener(this);
//        findViewById(R.id.tv_my_shike).setOnClickListener(this);
//        findViewById(R.id.tv_tea_shar).setOnClickListener(this);
//        findViewById(R.id.tv_tea_main_back).setOnClickListener(this);
//        findViewById(R.id.tv_exchg_mon).setOnClickListener(this);
//        findViewById(R.id.tv_tea_info).setOnClickListener(this);
//        findViewById(R.id.tv_tea_count).setOnClickListener(this);
//        findViewById(R.id.tv_tea_about).setOnClickListener(this);
//        tv_mess_del = (TextView) findViewById(R.id.tv_mess_del);
//        tv_mess_del.setOnClickListener(this);
//        undline = findViewById(R.id.tea_move_undline);
//        bac_topic = findViewById(R.id.tea_bac_huise);
//        ic_tea_buttom = (RelativeLayout) findViewById(R.id.ic_tea_buttom);
//        // 初始化红线的长度
//        mea_view = ScreenSizeUtil.getScreenWidth(context, 3);
//        LayoutParams params = new LinearLayout.LayoutParams(mea_view, 3);
//        int view_hig = ScreenSizeUtil.Dp2Px(context, 45);
//        android.widget.RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(mea_view, view_hig);
//        bac_topic.setLayoutParams(layoutParams);
//        undline.setLayoutParams(params);
//        manager = getSupportFragmentManager();
//        ft = manager.beginTransaction();
//        ft.replace(R.id.tea_mid_cot, Fragment_Message.getInstance());
//        ft.commit();
        startService(new Intent(this, MySKService.class));

        PackageManager packageManager = getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        localVersion = packInfo.versionName;
    }

//    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//        super.onWindowFocusChanged(hasFocus);
//        if (hasFocus) {
//            if (startX == 0) {
//                HelpUtil.showHelp(this, HelpUtil.HELP_TEA_1, null);
//            }
//        }
//    }

    // 控件的点击事件
    @Override
    public void onClick(View v) {
        ft = manager.beginTransaction();
        switch (v.getId()) {
            // case R.id.tea_main_view_bac:
            // TEA_YD.setVisibility(View.GONE);
            // tea_main_view_bac.setVisibility(View.GONE);
            // tea_tool_view_white.setVisibility(View.GONE);
            // break;
            // case R.id.tea_yd_tool1:
            // TEA_TOOL_YD.setVisibility(View.GONE);
            // tea_main_view_bac.setVisibility(View.GONE);
            // tea_tool_view_white.setVisibility(View.GONE);
            // break;
            // 老师端 消息页面 控件
            case R.id.tv_tea_topic:
                hideBottomLayout();
                tea_mess.setTextColor(getResources().getColor(R.color.log));
                my_stu.setTextColor(getResources().getColor(R.color.log));
                tea_topic.setTextColor(getResources().getColor(R.color.reg));
                tv_mess_del.setVisibility(View.VISIBLE);
                StartTransAnim(startX, 0, undline);
                StartTransAnim(startX, 0, bac_topic);
                ft.replace(R.id.tea_mid_cot, Fragment_Message.getInstance());
                startX = 0;
                break;
            // 老师端 我的学生页面 控件
            case R.id.tv_tea_mess:
                hideBottomLayout();
                tea_mess.setTextColor(getResources().getColor(R.color.reg));
                my_stu.setTextColor(getResources().getColor(R.color.log));
                tea_topic.setTextColor(getResources().getColor(R.color.log));
                tv_mess_del.setVisibility(View.GONE);
                StartTransAnim(startX, 1, undline);
                StartTransAnim(startX, 1, bac_topic);
                ft.replace(R.id.tea_mid_cot, new Fragment_Teacher_My_Student());
                startX = 1;
                break;
            // 制作题目tab
            case R.id.tv_my_stu:
                hideBottomLayout();
                // YD.getInstence().setTea_YD_Tool(this,context, PartnerConfig.TEA_TOOL,TEA_TOOL_YD,TEA_YD);
                tea_mess.setTextColor(getResources().getColor(R.color.log));
                my_stu.setTextColor(getResources().getColor(R.color.reg));
                tea_topic.setTextColor(getResources().getColor(R.color.log));
                tv_mess_del.setVisibility(View.GONE);
                StartTransAnim(startX, 2, undline);
                StartTransAnim(startX, 2, bac_topic);
                ft.replace(R.id.tea_mid_cot, new Fragment_Teacher_Writing_Topic());
                startX = 2;
                break;
            case R.id.tea_wire:
                Animation animation = new Animation(context, ic_tea_buttom, -258, 258);
                animation.initData(isUnfold);
                isUnfold = !isUnfold;
                break;
            // 我的师课
            case R.id.tv_my_shike:
                Dialog.Intent(context, Activity_Teather_ShiKe.class);
                break;
            // 删除消息
            case R.id.tv_mess_del:
                Fragment_Message.handler.sendEmptyMessage(5168);
                break;
            // 兑换现金
            case R.id.tv_exchg_mon:
                Dialog.Intent(this, Tea_chg_Mon_Acy.class);
                break;
            // 兑换商品
            case R.id.tv_exchg_sp:
                Dialog.Intent(this, Tea_chg_Comm_Act.class);
                break;
            // 个人信息
            case R.id.tv_tea_info:
                Dialog.Intent(context, Age_Person_Info.class);
                break;
            // 账户信息
            case R.id.tv_tea_count:
                Dialog.Intent(context, Activity_Teacher_zhanghu.class);
                break;
            // 分享同学
            case R.id.tv_tea_shar:
                dialog.show();
                break;
            // 关于师课
            case R.id.tv_tea_about:
                Dialog.Intent(context, Fragment_Teacher_About_Shike.class);
                break;
            // 退出键
            case R.id.tv_tea_main_back:
                Exit_Login.getInLogin().Back_Login(context);
                break;
            case R.id.share_dialog_weixin:
                dialog.dismiss();
                weixinManager.shareWeixin();
                break;
            case R.id.share_weixin_friend:
                dialog.dismiss();
                weixinManager.shareWeixinCircle();
                break;
            case R.id.share_dialog_message:
                ShareDialog.sendSMS(context, PartnerConfig.CONTEBT);
                dialog.dismiss();
                break;
            case R.id.share_dialog_email:
                dialog.dismiss();
                ShareDialog.openCLD(PartnerConfig.CONTEBT, this);
                break;
            case R.id.share_dialog_cancle:
                dialog.dismiss();
                break;
        }
        ft.commit();
    }

    // 开始执行动画的位移
    private void StartTransAnim(float fromXDelta, float toXDelta, View view) {
        TranslateAnimation anim = new TranslateAnimation(fromXDelta * mea_view, toXDelta * mea_view, 0, 0);
        anim.setDuration(500);
        anim.setFillAfter(true);
        view.startAnimation(anim);
    }

    /**
     * 分发按键事件, 按下键盘上返回按钮
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            Exit_Login.getInLogin().Back_Key(context);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void changeMessNum(boolean isShow) {
        mess_num.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onResume() {
        if (LoginManage.getInstance().getStudent() == null) {
            finish();//如果是重复登陆被挤下去了.这里resume的时候要关闭掉
        }
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    /**
     * 收起底部的layout
     */
    public void hideBottomLayout() {
        if (isUnfold) {
            Animation animation = new Animation(context, ic_tea_buttom, -258, 258);
            animation.initData(isUnfold);
            isUnfold = !isUnfold;
        }
    }
}