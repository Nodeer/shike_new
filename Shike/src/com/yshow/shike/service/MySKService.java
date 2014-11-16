package com.yshow.shike.service;

import java.util.ArrayList;
import java.util.List;

import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import com.yshow.shike.R;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.yshow.shike.activities.Login_Reg_Activity;
import com.yshow.shike.activities.MessageActivity;
import com.yshow.shike.activities.Student_Main_Activity;
import com.yshow.shike.activities.Teather_Main_Activity;
import com.yshow.shike.entity.LoginManage;
import com.yshow.shike.entity.SKMessage;
import com.yshow.shike.entity.SkMessage_Res;
import com.yshow.shike.entity.SkMessage_Voice;
import com.yshow.shike.utils.LogUtil;
import com.yshow.shike.utils.SKAsyncApiController;
import com.yshow.shike.utils.SKResolveJsonUtil;

public class MySKService extends Service {
    public static final int HAVE_NEW_MESSAGE = 999;
    private Notification mNotification;
    private NotificationManager mManager;
    private SoundPool soundPool;
    private boolean flag;
    private int soundId;
    // private int num = 0;
    private ArrayList<SKMessage> newresolveMessage;
    private Thread thread;
    private boolean resolveIsSuccess;
    public static Handler handler = null;
    public static Integer newNum = 0;
    boolean isLoop = true;

    @Override
    public void onCreate() {
        super.onCreate();
        initNotifiManager();
        soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        soundId = soundPool.load(getApplicationContext(), R.raw.shike, 1);
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                flag = true;
            }
        });
        // // 设置标示，及时间间隔 5分钟
        thread = new Thread(new MyMessageLoop());
        thread.start();
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        soundPool.release();
        isLoop = false;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void getNewMessage() {
        SKAsyncApiController.skGetNewMessage(new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int code, String content) {
                super.onSuccess(code, content);
//                LogUtil.d("推送内容:" + content);
                resolveIsSuccess = SKResolveJsonUtil.getInstance().resolveIsSuccess(content, getApplicationContext());
                if (resolveIsSuccess) {
                    newresolveMessage = SKResolveJsonUtil.getInstance().resolveNewMessage(content);
                    synchronized (newresolveMessage) {
                        ArrayList<SKMessage> isDone = SKResolveJsonUtil.getInstance().resolveISDoneNewMessage(content);
                        int newsNum = newresolveMessage.size();
                        int newCount = 0;
                        try {
                            for (int i = 0; i < newsNum; i++) {
                                SKMessage newskMessage = newresolveMessage.get(i);
                                ArrayList<SkMessage_Res> newres = newskMessage.getRes();
                                int newResSize = newres.size();
                                newCount += newResSize;
                                for (int j = 0; j < newResSize; j++) {
                                    SkMessage_Res skMessage_Res = newres.get(j);
                                    ArrayList<SkMessage_Voice> voice = skMessage_Res.getVoice();
                                    newCount += voice.size();
                                }
                            }
                        } catch (Exception e) {
                        }

                        if (newNum < newCount) {
                            haveNewMeassage(newresolveMessage);
                        } else {
                            int size = isDone.size();
                            if (size > 0) {
                                haveNewMeassage(newresolveMessage);
                            }
                        }
                        newNum = newCount;
                    }
                } else {
                    isLoop = false;
                }
            }

        });
    }

    private void haveNewMeassage(ArrayList<SKMessage> newresolveMessage) {
        boolean needNotifi = true;
        ActivityManager mActivityManager = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> appList = mActivityManager.getRunningTasks(1);
        String name = "";
        if (appList.size() != 0) {
            ActivityManager.RunningTaskInfo info = appList.get(0);
            name = info.topActivity.getShortClassName();
        }

        if (name.contains("Stu_Message_Detail_Activity") || name.contains("Tea_Message_Detail_Activity")) {
            needNotifi = false;
        } else if (name.contains("Student_Main_Activity")) {
            needNotifi = false;
        } else if (name.contains("Teather_Main_Activity")) {
            needNotifi = false;
        }
        if (needNotifi) {
            if (LoginManage.getInstance().isTeacher()) {
                showNotificationT();
            } else {
                showNotificationS();
            }
        } else {
            if (flag) {
                soundPool.play(soundId, 1.0f, 0.5f, 1, 0, 1.0f);
            }
        }
        if (handler != null) {
            handler.sendMessage(handler.obtainMessage(HAVE_NEW_MESSAGE, newresolveMessage));
        }
    }

    private void initNotifiManager() {
        mManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        int icon = R.drawable.icon;
        mNotification = new Notification();
        mNotification.icon = icon;
        mNotification.sound = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + R.raw.shike);
        mNotification.tickerText = "您有一条新师课消息";
        mNotification.defaults |= Notification.DEFAULT_SOUND;
        mNotification.flags = Notification.FLAG_AUTO_CANCEL;
    }

    // 弹出Notification
    @SuppressWarnings("deprecation")
    private void showNotificationS() {
        mNotification.when = System.currentTimeMillis();
        Intent i = new Intent(this, MessageActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, Intent.FLAG_ACTIVITY_NEW_TASK);
        mNotification.setLatestEventInfo(this, getResources().getString(R.string.app_name), "您有一条新师课消息,请注意查看!", pendingIntent);
        mManager.notify(0, mNotification);
    }

    @SuppressWarnings("deprecation")
    private void showNotificationT() {
        mNotification.when = System.currentTimeMillis();
        Intent i = new Intent(this, MessageActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, Intent.FLAG_ACTIVITY_NEW_TASK);
        mNotification.setLatestEventInfo(this, getResources().getString(R.string.app_name), "您有一条新师课消息,请注意查看!", pendingIntent);
        mManager.notify(0, mNotification);
    }

    class MyMessageLoop implements Runnable {
        public MyMessageLoop() {
            Log.e("MySKService", "MyMessageLoop");
        }
        @Override
        public void run() {
            while (isLoop) {
                try {
                    getNewMessage();
                    Thread.sleep(5 * 1000);
                } catch (InterruptedException e) {
                }
            }
        }
    }
}
