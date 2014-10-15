package com.yshow.shike.utils;

import com.yshow.shike.R;
import com.yshow.shike.activities.StudentRegisterActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
public class Dialog {
	public static void DeleteCache(Context context) {
		Builder builder = new Builder(context);
		builder.setTitle("     提示");
		builder.setMessage("你还没有输入你的手机号");
		builder.setPositiveButton("确定",
				new android.content.DialogInterface.OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {
					}
				});
		builder.setNegativeButton("取消",
				new android.content.DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
					}
				});
		try {
			builder.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 完成注册提示框
	 */
	public static void finsh_Reg_Dialog(final Context context) {
		Builder builder = new Builder(context);
		builder.setTitle("     提示");
		builder.setMessage(PartnerConfig.AUTO_CONTEBT);
		builder.setPositiveButton("立即注册",
				new android.content.DialogInterface.OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {
						Dialog.Intent(context, StudentRegisterActivity.class);
					}
				});
		builder.setNegativeButton("以后", null);
		try {
			builder.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 返回按钮提示框
	public static void Back_Control(Context context) {
		Builder builder = new Builder(context);
		builder.setTitle("     提示");
		builder.setMessage("你还没有输入你的手机号");
		builder.setPositiveButton("确定",
				new android.content.DialogInterface.OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {
					}
				});
		builder.setNegativeButton("取消",
				new android.content.DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
					}
				});
		try {
			builder.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 感谢老师发送0分
	 * 
	 * @param context
	 * @param clazz
	 */
	public static void Zone_Think(final Context context) {
		new AlertDialog.Builder(context).setMessage("你确定退出师课么？").setTitle("师课")
				.setIcon(R.drawable.ic_launcher)
				.setPositiveButton("退出", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						((Activity) context).finish();
					}
				}).setNegativeButton("返回", null).create().show();
	}

	// 跳转到下一个Activity中
	public static <T> void Intent(Context context, Class<T> clazz) {
		Intent intent = new Intent(context, clazz);
//		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

	// 跳转到下一个Activity中带参
	public static <T> void intent(Context context, Class<T> clazz, Bundle bundle) {
		Intent intent = new Intent(context, clazz);
		intent.putExtras(bundle);
		context.startActivity(intent);
	}

	// 带数据跳转到下一个Activity中
	public static <T> void IntentParamet(Context context, Class<T> clazz,
			String key, Bitmap bitmap) {
		Intent intent = new Intent(context, clazz);
		Bundle bundle = new Bundle();
		bundle.putParcelable(key, bitmap);
		intent.putExtras(bundle);
		context.startActivity(intent);
	}

	// 带数据跳转到下一个Activity中
	public static <T> void IntentParamet2(Context context, Class<T> clazz,
			String key, int id) {
		Intent intent = new Intent(context, clazz);
		intent.putExtra("KEY", id);
		context.startActivity(intent);
	}
	// 上拉下拉动画
	public static void Animation(Context context, int id, View view) {
		Animation animation = AnimationUtils.loadAnimation(context, id);
		view.setAnimation(animation);
		animation.setFillEnabled(true);
		animation.startNow();
	}
	/**
	 * 获取缩放宽和高的设置
	 * @param bm
	 * @param newWidth
	 * @param newHeight
	 */
	public static Bitmap getbitmap(String imagePath, int width, int height) {
		Bitmap bitmap = null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		// 获取这个图片的宽和高，注意此处的bitmap为null
		bitmap = BitmapFactory.decodeFile(imagePath, options);
		options.inJustDecodeBounds = false; // 设为 false
		// 计算缩放比
		int h = options.outHeight;
		int w = options.outWidth;
		int beWidth = w / width;
		int beHeight = h / height;
		int be = 1;
		if (beWidth < beHeight) {
			be = beWidth;
		} else {
			be = beHeight;
		}
		if (be <= 0) {
			be = 1;
		}
		options.inSampleSize = be;
		bitmap = BitmapFactory.decodeFile(imagePath, options);
		bitmap = Dialog.scaleImg(bitmap, width, height);
		return bitmap;
	}
    /**
     * 图片压缩
     * @param bm
     * @param newWidth 图片宽
     * @param newHeight 图片高
     */
	public static Bitmap scaleImg(Bitmap bm, int newWidth, int newHeight) {
		// 获得图片的宽高
		int width = bm.getWidth();
		int height = bm.getHeight();
		// 设置想要的大小
		int newWidth1 = newWidth;
		int newHeight1 = newHeight;
		// 计算缩放比例
		float scaleWidth = ((float) newWidth1) / width;
		float scaleHeight = ((float) newHeight1) / height;
		// 取得想要缩放的matrix参数
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		// 得到新的图片
		Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix,true);
		return newbm;
	}
	/**
	 * 退出程序
	 * 
	 * @param context
	 */
	public static void QuitHintDialog(final Context context, int id) {
		new AlertDialog.Builder(context).setMessage("亲，你确定退出师课么？")
				.setTitle("师课").setIcon(id)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						try {
							((Activity) context).finish();
						} catch (Exception e) {
							Log.e("close", "close error");
						}
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				}).create().show();
	}

	// 自定义Dillog
	public static void showMyDialog(Context context) {
		LayoutInflater factory = LayoutInflater.from(context);
		View textEntryView = factory.inflate(R.layout.dialog_item, null);
		textEntryView.findFocus();
		AlertDialog myDialog = new AlertDialog.Builder(context).create();
		myDialog.show();
		myDialog.getWindow().setContentView(textEntryView);
		((Activity) context).getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(textEntryView, 0); // 显示软键盘
		imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS); // 显示软键盘
	}
	/**
	 * 通用Dialog
	 * @param context
	 */
	public static void  Common_Use(Context context) {
		Builder builder = new Builder(context);
		builder.setTitle("     提示");
		builder.setMessage("关注该老师后，才能提问");
		builder.setPositiveButton("确定",null);
		try {
			builder.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


    public static AlertDialog showSimpleDialog(Context context,String s) {
        Builder builder = new Builder(context);
        builder.setMessage(s);
        AlertDialog dialog = null;
        try {
            dialog = builder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dialog;
    }
}
