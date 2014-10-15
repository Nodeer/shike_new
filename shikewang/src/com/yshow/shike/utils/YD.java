package com.yshow.shike.utils;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;

public class YD {
   private static YD yd = new YD();
   private YD(){}
   public static YD getInstence(){
	   return yd;
   }
   /**
    * @param context
    * @param view1 引导业的显示和隐藏
    * @param view2 被引导主页是否有点击事件
    */
   public void setYD(OnClickListener listener,Context context,View view1,View view2){
	   FileService fileService = new FileService(context);
	   boolean SYD = fileService.getBoolean("YD", false);
	   if(!SYD){
		   view1.setVisibility(View.VISIBLE);
		   view1.setOnClickListener(listener);
		   view2.setClickable(false);
	   }
   }
   /**
    *老师端接收消息的引导
    * @param listener
    * @param context
    * @param view1
    * @param view2
    */
   public void setTea_YD_Tool(OnClickListener listener,Context context,String tea_yd,View view1,View view2){
	   FileService fileService = new FileService(context);
	   boolean SYD = fileService.getBoolean(tea_yd, false);
	   if(!SYD){
		   view1.setVisibility(View.VISIBLE);
		   view1.setOnClickListener(listener);
		   view2.setClickable(false);
	   }
   }
   public boolean getYD(Context context,String Key,Boolean Value){
	   FileService fileService = new FileService(context);
	   boolean SYD = fileService.putBoolean(context, Key, Value);
	   return SYD;
   }
}
