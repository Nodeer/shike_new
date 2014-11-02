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
import com.yshow.shike.fragments.Fragment_Student_GuanYu;
import com.yshow.shike.fragments.StuSlideMenuFragment;
import com.yshow.shike.service.MySKService;
import com.yshow.shike.utils.*;

import java.io.File;

/**
 * 学生登录后的主页,有我要提问,消息,我的老师,找老师4个tab
 */
public class Student_Main_Activity extends SlidingFragmentActivity {
    private FragmentTransaction ft;
    private int startX = 0;
    private Context context;
    private boolean isUnfold = false;
    public static Student_Main_Activity mInstance;

    private String localVersion = "";
    private Fragment mContent;
    private SlidingMenu sm;

    @Override
    protected void onNewIntent(Intent intent) {// 从"完成注册"成功注册以后的回调
        super.onNewIntent(intent);
    }

    @Override
    public void onCreate(Bundle saveInsanceState) {
        super.onCreate(saveInsanceState);
        if (LoginManage.getInstance().getStudent() == null) {
            startActivity(new Intent(this, StartingUp.class));
            finish();
            return;
        }

        mInstance = this;
        context = this;
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
        sm.setBackgroundResource(R.drawable.main_activity_bg);
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
}