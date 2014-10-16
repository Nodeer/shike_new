package com.yshow.shike.utils;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

import com.yshow.shike.R;
public class Dilog_Share {
	// 分享Dialog
	 public static Dialog Dilog_Anim( Context context, OnClickListener dialogListener) {
		 View view_share = LayoutInflater.from(context).inflate(R.layout.share_friend_dialog, null);
		 view_share.findViewById(R.id.share_dialog_cancle).setOnClickListener(dialogListener);
		 view_share.findViewById(R.id.share_dialog_email).setOnClickListener(dialogListener);
		 view_share.findViewById(R.id.share_dialog_message).setOnClickListener(dialogListener);
		 view_share.findViewById(R.id.share_dialog_weixin).setOnClickListener(dialogListener);
		 view_share.findViewById(R.id.account_pop_layout).setOnClickListener(dialogListener);
		 view_share.findViewById(R.id.share_weixin_friend).setOnClickListener(dialogListener);
		 android.app.Dialog dialog = new android.app.Dialog(context,R.style.dialog);
		 dialog.setContentView(view_share);
         Window window = dialog.getWindow();
         window.setGravity(Gravity.CENTER_HORIZONTAL); //此处可以设置dialog显示的位置    
         window.setWindowAnimations(R.style.mystyle); //添加动画  
         return dialog;
	}
    //学生免注册动画
		public static Dialog Dilog_Anim_Reg( Context context,OnClickListener listener) {
			 View view_share = LayoutInflater.from(context).inflate(R.layout.stu_reg_dialog, null);
			 view_share.findViewById(R.id.tv_TiWen_Que).setOnClickListener(listener);
			 view_share.findViewById(R.id.tv_MaSg_Reg).setOnClickListener(listener);
			 view_share.findViewById(R.id.tv_Dil_cancel).setOnClickListener(listener);
			 android.app.Dialog dialog = new android.app.Dialog(context,R.style.dialog);
			 dialog.setContentView(view_share);
	         Window window = dialog.getWindow();
	         window.setGravity(Gravity.CENTER_HORIZONTAL); //此处可以设置dialog显示的位置    
	         window.setWindowAnimations(R.style.mystyle); //添加动画  
	        return dialog;
		}
	    //学生免注册动画
			public static Dialog Tea_Reg( Context context,OnClickListener listener) {
				 View view_share = LayoutInflater.from(context).inflate(R.layout.tea_reg_dialog, null);
				 view_share.findViewById(R.id.tv_pai_zhao).setOnClickListener(listener);
				 view_share.findViewById(R.id.tv_xiagnc).setOnClickListener(listener);
				 view_share.findViewById(R.id.tv_tea_cancel).setOnClickListener(listener);
				 android.app.Dialog dialog = new android.app.Dialog(context,R.style.dialog);
				 dialog.setContentView(view_share);
		        Window window = dialog.getWindow();
		        window.setGravity(Gravity.CENTER_HORIZONTAL); //此处可以设置dialog显示的位置    
		        window.setWindowAnimations(R.style.mystyle); //添加动画  
		        return dialog;
			}
		    //学生免注册动画
				public static Dialog Stu_Take_Phon( Context context,OnClickListener listener) {
					 View view_share = LayoutInflater.from(context).inflate(R.layout.tea_reg_dialog, null);
					 TextView take_phon = (TextView) view_share.findViewById(R.id.tv_pai_zhao);
					TextView tv_xiagnc = (TextView) view_share.findViewById(R.id.tv_xiagnc);
					TextView Tea_cancel = (TextView) view_share.findViewById(R.id.tv_tea_cancel);
					take_phon.setOnClickListener(listener);
					tv_xiagnc.setOnClickListener(listener);
					Tea_cancel.setOnClickListener(listener);
					take_phon.setText("相机");
					tv_xiagnc.setText("相册");
					Tea_cancel.setText("取消");
					android.app.Dialog dialog = new android.app.Dialog(context,R.style.dialog);
					dialog.setContentView(view_share);
			        Window window = dialog.getWindow();
			        window.setGravity(Gravity.CENTER_HORIZONTAL); //此处可以设置dialog显示的位置    
			        window.setWindowAnimations(R.style.mystyle); //添加动画  
			        return dialog;
				}
		    //充值方式对话框
				public static Dialog SelectReChargeTypeDialog( Context context,OnClickListener listener) {
					 View view_share = LayoutInflater.from(context).inflate(R.layout.tea_reg_dialog, null);
					 TextView take_phon = (TextView) view_share.findViewById(R.id.tv_pai_zhao);
					TextView tv_xiagnc = (TextView) view_share.findViewById(R.id.tv_xiagnc);
					TextView Tea_cancel = (TextView) view_share.findViewById(R.id.tv_tea_cancel);
					take_phon.setOnClickListener(listener);
					tv_xiagnc.setOnClickListener(listener);
					Tea_cancel.setOnClickListener(listener);
					take_phon.setText("支付宝充值");
					tv_xiagnc.setText("银联充值");
					Tea_cancel.setText("取消");
					android.app.Dialog dialog = new android.app.Dialog(context,R.style.dialog);
					dialog.setContentView(view_share);
			        Window window = dialog.getWindow();
			        window.setGravity(Gravity.CENTER_HORIZONTAL); //此处可以设置dialog显示的位置
			        window.setWindowAnimations(R.style.mystyle); //添加动画
			        return dialog;
				}
}
