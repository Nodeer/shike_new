package com.yshow.shike.activities;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.umeng.analytics.MobclickAgent;
import com.yshow.shike.R;
import com.yshow.shike.activities.Activity_Select_Tea.Callback;
import com.yshow.shike.entity.LoginManage;
import com.yshow.shike.entity.SKStudent;
import com.yshow.shike.entity.VersionModel;
import com.yshow.shike.fragments.StuContentFragment;
import com.yshow.shike.fragments.Fragment_Message;
import com.yshow.shike.fragments.Fragment_My_Teacher;
import com.yshow.shike.fragments.Fragment_Student_GuanYu;
import com.yshow.shike.fragments.StuSlideMenuFragment;
import com.yshow.shike.service.MySKService;
import com.yshow.shike.utils.*;

import java.io.File;

/**
 * 学生登录后的主页,有我要提问,消息,我的老师,找老师4个tab
 */
public class Student_Main_Activity extends SlidingFragmentActivity implements Callback {
    private FragmentManager manager;
    private FragmentTransaction ft;
    private View undline, bac_huise;
    private int mea_view;
    private int startX = 0;
    private Context context;
    private SKStudent student; // 获取登录的对象
    private RelativeLayout in_wo_de_shi_ke;
    private boolean isUnfold = false;
    private android.app.Dialog dialog;
    private TextView tv_mess, tv_my_tea, tv_find_tea, tv_ques, tv_delete;// 学生端 提问 消息 我的老师 找老师 切换按钮 消息刪除按鈕 退出按钮
    private ImageView mess_num;
    public static Student_Main_Activity mInstance;
    private android.widget.RelativeLayout.LayoutParams layoutParams;
    private View view_button;
    private WeixinManager weixinManager;

    private String localVersion = "";
    private Fragment mContent;
    private SlidingMenu sm;

    @Override
    protected void onNewIntent(Intent intent) {// 从"完成注册"成功注册以后的回调
        super.onNewIntent(intent);
        student = LoginManage.getInstance().getStudent();
        if (student.getMob() != null) {
        }
    }

    @Override
    public void onCreate(Bundle saveInsanceState) {
        super.onCreate(saveInsanceState);
//		setContentView(R.layout.student_main_activity);
        mInstance = this;
        context = this;
//		RelativeLayout stu_main_page = (RelativeLayout) findViewById(R.id.stu_main_page);
//		weixinManager = new WeixinManager(Student_Main_Activity.this);
//		dialog = new Dilog_Share().Dilog_Anim(context, listener);
//		tv_ques = (TextView) findViewById(R.id.tv_stu_ques);
//		tv_find_tea = (TextView) findViewById(R.id.tv_find_tea);
//		tv_mess = (TextView) findViewById(R.id.tv_mess);
//		mess_num = (ImageView) findViewById(R.id.mess_num);
//		tv_my_tea = (TextView) findViewById(R.id.tv_my_tea);
//		view_button = findViewById(R.id.view_button);
//		tv_ques.setOnClickListener(listener);
//		tv_find_tea.setOnClickListener(listener);
//		tv_my_tea.setOnClickListener(listener);
//		tv_mess.setOnClickListener(listener);
//		undline = findViewById(R.id.stu_move_undline);
//		bac_huise = findViewById(R.id.stu_bac_huise);
//		tv_back = (TextView) findViewById(R.id.tv_stumain_back);
//		tv_back.setOnClickListener(listener);
//		findViewById(R.id.three_point).setOnClickListener(listener);
//		findViewById(R.id.tv_information).setOnClickListener(listener);
//		findViewById(R.id.tv_recharge).setOnClickListener(listener);
//		findViewById(R.id.tv_account_information).setOnClickListener(listener);
//		findViewById(R.id.tv_share_student).setOnClickListener(listener);
//		findViewById(R.id.tv_my_shike).setOnClickListener(listener);
//		findViewById(R.id.tv_stu_chongz).setOnClickListener(listener);
//		in_wo_de_shi_ke = (RelativeLayout) findViewById(R.id.in_wo_de_shi_ke);
//		tv_delete = (TextView) findViewById(R.id.tv_delete);
//		tv_delete.setOnClickListener(listener);
//		student = LoginManage.getInstance().getStudent();
//		if (student.getMob() != null) {
//			tv_back.setText("退出");
//			tv_back.setTextColor(context.getResources().getColor(R.color.button_typeface_color));
//		} else {
//		}
//		Init_Anim();
        SKAsyncApiController.Sof_Info(new MyAsyncHttpResponseHandler(context, true) {
            @Override
            public void onSuccess(String json) {
                super.onSuccess(json);
                VersionModel model = SKResolveJsonUtil.getInstance().getNewVersion(json);
                checkNewVersion(model);
            }
        });

        PackageManager packageManager = getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        localVersion = packInfo.versionName;
//        TextView versiontext = (TextView) findViewById(R.id.version);
//        versiontext.setText("版本号: V" + localVersion);

        initSlide();

    }

    private void initSlide() {
        setContentView(R.layout.main_frame);
        setBehindContentView(R.layout.menu_frame);
        getSlidingMenu().setSlidingEnabled(true);
        getSlidingMenu()
                .setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        mContent = new StuContentFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, mContent).commit();
        // set the Behind View Fragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.menu_frame, new StuSlideMenuFragment()).commit();
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
        startService(new Intent(this, MySKService.class));
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
        Toast.makeText(Student_Main_Activity.this, "开始下载新版本", Toast.LENGTH_SHORT).show();
    }

//	@Override
//	public void onWindowFocusChanged(boolean hasFocus) {
//		super.onWindowFocusChanged(hasFocus);
//		if (hasFocus) {
//			if (startX == 0) {
//				if (student.getMob() != null) {
//					HelpUtil.showHelp(this, HelpUtil.HELP_STU_6, null);
//				} else {
//					HelpUtil.showHelp(this, HelpUtil.HELP_STU_1, null);
//				}
//			}
//		}
//	}

    private OnClickListener listener = new OnClickListener() {
        @Override
        public void onClick(View arg0) {
            ft = manager.beginTransaction();
            switch (arg0.getId()) {
                case R.id.tv_stu_ques:// 我要提问tab
                    hideBottomLayout();
                    tv_delete.setVisibility(View.GONE);
                    view_button.setVisibility(View.GONE);
                    tv_my_tea.setTextColor(getResources().getColor(R.color.sk_student_main_color));
                    tv_find_tea.setTextColor(getResources().getColor(R.color.sk_student_main_color));
                    tv_ques.setTextColor(getResources().getColor(R.color.reg));
                    tv_mess.setTextColor(getResources().getColor(R.color.sk_student_main_color));
                    ft.replace(R.id.content, new Stu_madequestionFragment());
                    StartTransAnim(startX, 0, undline);
                    StartTransAnim(startX, 0, bac_huise);
                    startX = 0;
                    break;
                // 我的时刻
                case R.id.tv_my_shike:
                    Dialog.Intent(context, Activity_MyShiKe.class);
                    break;
                case R.id.tv_mess:// 消息tab
                    hideBottomLayout();
                    tv_delete.setVisibility(View.VISIBLE);
                    tv_my_tea.setTextColor(getResources().getColor(R.color.sk_student_main_color));
                    tv_find_tea.setTextColor(getResources().getColor(R.color.sk_student_main_color));
                    tv_ques.setTextColor(getResources().getColor(R.color.sk_student_main_color));
                    tv_mess.setTextColor(getResources().getColor(R.color.reg));
                    ft.replace(R.id.content, Fragment_Message.getInstance());
                    StartTransAnim(startX, 1, undline);
                    StartTransAnim(startX, 1, bac_huise);
                    view_button.setVisibility(View.VISIBLE);
                    startX = 1;
                    break;
                case R.id.tv_my_tea:// 我的老师
                    hideBottomLayout();
                    tv_delete.setVisibility(View.GONE);
                    view_button.setVisibility(View.GONE);
                    tv_my_tea.setTextColor(getResources().getColor(R.color.reg));
                    tv_find_tea.setTextColor(getResources().getColor(R.color.sk_student_main_color));
                    tv_ques.setTextColor(getResources().getColor(R.color.sk_student_main_color));
                    tv_mess.setTextColor(getResources().getColor(R.color.sk_student_main_color));
                    ft.replace(R.id.content, new Fragment_My_Teacher());
                    StartTransAnim(startX, 2, undline);
                    StartTransAnim(startX, 2, bac_huise);
                    startX = 2;
                    break;
                // 充值
                case R.id.tv_stu_chongz:
                    if (student.getMob() != null) {
                        Dialog.Intent(context, Activity_Recharge.class);
                    } else {
                        Dialog.finsh_Reg_Dialog(context);
                    }
                    break;
                case R.id.three_point:
                    Animation animation = new Animation(Student_Main_Activity.this, in_wo_de_shi_ke, -235, 235);
                    animation.initData(isUnfold);
                    isUnfold = !isUnfold;
                    break;
                // 关于师课
                case R.id.tv_recharge:
                    Dialog.Intent(context, Fragment_Student_GuanYu.class);
                    break;
                // 账户信息
                case R.id.tv_account_information:
                    if (student.getMob() != null) {
                        Dialog.Intent(context, Activity_Student_Account_Message.class);
                    } else {
                        Dialog.finsh_Reg_Dialog(context);
                    }
                    break;
                // 完成注册按钮/退出
                case R.id.tv_stumain_back:
                    if (student.getMob() != null) {// 如果是注册用户,就是退出按钮
                        Exit_Login.getInLogin().Back_Login(context);
                    } else {
                        Dialog.Intent(context, StudentRegisterActivity.class);
                    }
                    break;
                // 发送删除信息的消息
                case R.id.tv_delete:
                    Fragment_Message.handler.sendEmptyMessage(5168);
                    break;
                // Dialog 里面微信分享的按钮
                case R.id.share_dialog_weixin:
                    dialog.dismiss();
                    weixinManager.shareWeixin();
                    break;
                // Dialog 里面短信分享的按钮
                case R.id.share_dialog_message:
                    ShareDialog.sendSMS(context, PartnerConfig.CONTEBT);
                    break;
                // Dialog 里面e_mail分享的按钮
                case R.id.share_dialog_email:
                    dialog.dismiss();
                    ShareDialog.openCLD(PartnerConfig.CONTEBT, context);
                    break;
                case R.id.share_weixin_friend:
                    dialog.dismiss();
                    weixinManager.shareWeixinCircle();
                    break;
                // Dialog 里面取消分享的按钮
                case R.id.share_dialog_cancle:
                    dialog.dismiss();
                    break;
            }
            ft.commit();
        }
    };

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

    /**
     * 从题版发完消息后跳转到消息页面
     */
    @Override
    public void back(String str) {
        FragmentTransaction beginTransaction = manager.beginTransaction();
        beginTransaction.replace(R.id.content, Fragment_Message.getInstance());
        beginTransaction.commitAllowingStateLoss();
        StartTransAnim(startX, 1, undline);
        StartTransAnim(startX, 1, bac_huise);
    }

    // @Override
    // protected void onResume() {
    // super.onResume();
    // student = LoginManage.getInstance().getStudent();
    // if(student.getMob() != null){
    // tv_back.setText("退出");
    // tv_back.setTextColor(context.getResources().getColor(R.color.button_typeface_color));
    // }
    // }

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
            Animation animation = new Animation(Student_Main_Activity.this, in_wo_de_shi_ke, -235, 235);
            animation.initData(isUnfold);
            isUnfold = !isUnfold;
        }
    }
}