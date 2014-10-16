package com.yshow.shike.utils;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.media.ExifInterface;
/**
 *图片处理
 * @author Administrator
 *
 */
public class Bitmap_Manger_utils {
   private static Bitmap_Manger_utils bitmap_Manger = new Bitmap_Manger_utils();
   private Bitmap_Manger_utils(){};
   public static Bitmap_Manger_utils getIntence(){
	   return bitmap_Manger;
   }
   /**
    *图片压缩
    * @param context
    * @param path
    * @return
    */
   public Bitmap press_bitmap(Context context,String url){
	   BitmapFactory.Options newOpts = new BitmapFactory.Options();   
       //开始读入图片，此时把options.inJustDecodeBounds 设回true了   
       newOpts.inJustDecodeBounds = true;   
       Bitmap bitmap = BitmapFactory.decodeFile(url,newOpts);//此时返回bm为空   
       newOpts.inJustDecodeBounds = false;   
       newOpts.inSampleSize = 1;//设置缩放比例   
       bitmap = BitmapFactory.decodeFile(url, newOpts);   
       return bitmap;//压缩好比例大小后再进行质量压缩    
   } 
   /**
    *计算图片旋转90度
    * @param bitmap
    * @return
    */
   public int readPictureDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}
	/*
	 * 旋转图片
	 * @param angle
	 * @param bitmap
	 * @return Bitmap
	 */
	public Bitmap rotaingImageView(int angle, Bitmap bitmap) {
		// 旋转图片 动作
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		// 创建新的图片
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		return resizedBitmap;
	}
	/**
	 * 合并图片
	 * @param background
	 * @param foreground
	 * @return
	 */
	public Bitmap toConformBitmap(Bitmap background, Bitmap foreground) {
		if (background == null) {
			return null;
		}
		int bgWidth = background.getWidth();
		int bgHeight = background.getHeight();
		Bitmap newbmp = Bitmap.createBitmap(bgWidth, bgHeight, Config.ARGB_8888);
		Canvas cv = new Canvas(newbmp);
		cv.drawBitmap(background, 0, 0, null);// 在 0，0坐标开始画入bg
		cv.drawBitmap(foreground, 0, 0, null);// 在 0，0坐标开始画入fg ，可以从任意位置画入
		cv.save(Canvas.ALL_SAVE_FLAG);// 保存
		cv.restore();// 存储
		return newbmp;
	}
//	public Bitmap toConformBitmap(Bitmap background, Bitmap foreground,Matrix m) {
//		if (background == null) {
//			return null;
//		}
//		int bgWidth = background.getWidth();
//		int bgHeight = background.getHeight();
//		Bitmap newbmp = Bitmap.createBitmap(bgWidth, bgHeight, Config.ARGB_8888);
//		Canvas cv = new Canvas(newbmp);
//		cv.drawBitmap(background, 0, 0, null);// 在 0，0坐标开始画入bg
//		Bitmap scaleBitmap = Bitmap.createBitmap(foreground, 0, 0, background.getWidth(), background.getWidth(), m, false);
//		cv.drawBitmap(scaleBitmap, 0, 0, null);
//		cv.save(Canvas.ALL_SAVE_FLAG);// 保存
//		cv.restore();// 存储
//		return newbmp;
//	}
} 
