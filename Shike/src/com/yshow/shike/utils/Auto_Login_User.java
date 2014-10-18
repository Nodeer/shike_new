package com.yshow.shike.utils;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.yshow.shike.UIApplication;
import com.yshow.shike.activities.Student_Main_Activity;
import com.yshow.shike.entity.LoginManage;
import com.yshow.shike.entity.SKStudent;

public class Auto_Login_User {
	private MD5_PhonDis md5_phondis;
	private FileService save_auto_uid;
	private Context context;
   public Auto_Login_User(Context context) {
		super();
		this.context = context;
		md5_phondis = MD5_PhonDis.getInstance();
		save_auto_uid = new FileService(context);
	}
/**
    *自动登录 联网操作
    */
	public void auto_login_info(){
		String macAdd = md5_phondis.getMacAdd(context); // 获取wife网卡
		String key = "Yshow";
		String time = System.currentTimeMillis()+""; //获取时间戳
		String md5 = md5_phondis.md5(macAdd +""+ key +""+ time);//md5加密
		SKAsyncApiController.AuTo_log(macAdd,md5,time,key,new AsyncHttpResponseHandler(){
			@SuppressWarnings("deprecation")
			@Override
			public void onFailure(Throwable json) {
				super.onFailure(json);
				Toast.makeText(context,"请检查你的网络", 0).show();
			}
			@Override
			public void onSuccess(String json) {
				super.onSuccess(json);
				boolean success = SKResolveJsonUtil.getInstance().resolveIsSuccess(json,context);
				if(success){
					Log.e("自动登录",""+json);
					com.yshow.shike.entity.Auto_Login auTo_Pase = SKResolveJsonUtil.getInstance().AuTo_Pase(json);
                    if(TextUtils.isEmpty(auTo_Pase.mob)){
                        save_auto_uid.set_auto_info(null, auTo_Pase.getUid());
                        UIApplication.getInstance().setAuid_flag(false);
                        // 设置学生端登陆的默认值 防止空指针出现
                        SKStudent student = new SKStudent();
                        student.setTypes("-1");
                        student.setUid(auTo_Pase.getUid());
                        LoginManage.getInstance().setStudent(student);
                        save_auto_uid.putBoolean("is_tea", false);
                        com.yshow.shike.utils.Dialog.Intent(context, Student_Main_Activity.class);
                        ((Activity)context).finish();
					} else {
						LoginManage instance = LoginManage.getInstance();
						instance.setmLoginSuccess(true);
						SKStudent student = SKResolveJsonUtil.getInstance().resolveLoginInfo(json);
						instance.setStudent(student);
						save_auto_uid.putBoolean("is_tea", student.getTypes().equals("1"));
						Bundle bundle = new Bundle();
						UIApplication.getInstance().setAuid_flag(true);
						bundle.putString("!reg_user", student.getName());
						Dialog.intent(context, Student_Main_Activity.class, bundle);
						((Activity) context).finish();
					}

				}
			}
		});
	}
}
