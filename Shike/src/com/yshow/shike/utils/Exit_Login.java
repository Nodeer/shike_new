package com.yshow.shike.utils;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.yshow.shike.activities.Login_Reg_Activity;
import com.yshow.shike.entity.LoginManage;
import com.yshow.shike.entity.Update_User_Info;
import com.yshow.shike.entity.User_Info;
import com.yshow.shike.service.MySKService;

public class Exit_Login {
    private long exitTime = 0;
    private static Exit_Login exit_Login = new Exit_Login();

    private Exit_Login() {
    }

    ;

    public static Exit_Login getInLogin() {
        return exit_Login;
    }

    /**
     * 用户退出
     */
    public void Back_Login(final Context context) {
        final ProgressDialogUtil util = new ProgressDialogUtil(context, "正在退出");
        util.show();

        SKAsyncApiController.Back_Login(new MyAsyncHttpResponseHandler(context,false) {
            @Override
            public void onSuccess(String json) {
                super.onSuccess(json);
                boolean success = SKResolveJsonUtil.getInstance().resolveIsSuccess(json);
//                if (success) {
                    SharePreferenceUtil.getInstance().putBoolean("autologin", false);
                    NotificationManager mManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    mManager.cancelAll();
                    Intent service = new Intent(context, MySKService.class);
                    context.stopService(service);
                    LoginManage.getInstance().setStudent(null);
                    Intent it = new Intent(context, Login_Reg_Activity.class);
                    context.startActivity(it);
                    ((Activity) context).finish();
//                } else {
//                    Toast.makeText(context, "退出失败，请检查您的网络！", Toast.LENGTH_SHORT).show();
//                }
                util.dismiss();
            }

            @Override
            public void onFailure(Throwable error, String content) {
                util.dismiss();
                Toast.makeText(context, "退出失败，请检查您的网络！", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 按下键盘上返回按钮 时间控制
     */
    public void Back_Key(Context context) {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(context, "再按一次返回键退出师课", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            ((Activity) context).finish();
//			System.exit(0);
        }
    }

    /**
     * 更新用户信息
     *
     * @param context
     * @param user_info 用户信息封装
     */
    public void save_user_info(final Context context, Update_User_Info user_info) {
        SKAsyncApiController.Save_Info(user_info, new MyAsyncHttpResponseHandler(context, true) {
            @Override
            public void onSuccess(String json) {
                super.onSuccess(json);
                boolean success = SKResolveJsonUtil.getInstance().resolveIsSuccess(json, context);
                if (success) {
                    Toast.makeText(context, "保存成功", Toast.LENGTH_SHORT).show();
                    User_Info info = SKResolveJsonUtil.getInstance().My_teather1(json);
                    Intent it = new Intent();
                    it.putExtra("info", info);
                    ((Activity) context).setResult(Activity.RESULT_OK, it);
                    ((Activity) context).finish();
                }
            }
        });
    }
}
