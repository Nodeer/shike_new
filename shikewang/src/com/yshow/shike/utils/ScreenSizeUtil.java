package com.yshow.shike.utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

public class ScreenSizeUtil {

	private static DisplayMetrics metrics;

	/**
	 * 鏍规嵁缁濆灏哄寰�?��鐩稿灏哄锛屽湪涓嶅悓鐨勫垎杈ㄧ巼璁惧涓婃湁涓�嚧鐨勬樉�?��晥鏋�?dip->pix
	 * 
	 * @param context
	 * @param givenAbsSize
	 *            缁濆灏哄
	 * @return
	 */
	public static int getSizeByGivenAbsSize(Context context, int givenAbsSize) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				givenAbsSize, context.getResources().getDisplayMetrics());
	}
	private static DisplayMetrics getDisplayMetrics(Context context) {
		if (metrics != null) {
			return metrics;
		}
		metrics = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay()
				.getMetrics(metrics);
		return metrics;
	}
	/**
	 * 把手机屏幕平分
	 * @param context
	 * @return
	 */
	public static int ScreenWidth;
	
	public static int ScreenHeight;

	public static int getScreenWidth(Context context,int i) {
		int widthPixels = getDisplayMetrics(context).widthPixels;
		int j = widthPixels /i;
		return j; 
	}
    
	public static int getScreenHeight(Context context) {
		return getDisplayMetrics(context).heightPixels;// 灞忓箷楂樺害锛堝儚绱狅級
	}

	public static float getScreenDensity(Context context) {
		return getDisplayMetrics(context).density; // 灞忓箷�?嗗害锛�.75 / 1.0 / 1.5锛�	
		}
    
	public static int getScreenDensityDpi(Context context) {
		return getDisplayMetrics(context).densityDpi;
		}

	public static int Dp2Px(Context context, float dp) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dp * scale + 0.5f);
	}

	public static int Px2Dp(Context context, float px) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (px / scale + 0.5f);
	}
	/**
	 *可以强制测量View的宽和高
	 */
	public static int View_Measure(View view){
		int w = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);  
		int h = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);  
		view.measure(w, h);  
//		int Width = view.getMeasuredWidth();
        return view.getMeasuredHeight();
	}
}
