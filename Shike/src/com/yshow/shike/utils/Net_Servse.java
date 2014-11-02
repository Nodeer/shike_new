package com.yshow.shike.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.yshow.shike.R;

/**
 * 图片下载 显示
 * 
 * @author Administrator
 * 
 */
public class Net_Servse {
	/**
	 * 单例模式
	 */
	private static Net_Servse netServse = null;
	private Net_Servse() {
	}
	public static synchronized Net_Servse getInstence() {
		if (netServse == null) {
			netServse = new Net_Servse();
		}
		return netServse;
	}
	/**
	 * 对图片进行缓存
	 * @param cache 本应用的默认缓存路径
	 * @param path  图片的网路路径
	 * @throws Exception
	 */
	public Bitmap getImage(String url) throws Exception {
		URL myFileUrl = null;
		Bitmap bitmap = null;
		try {
			myFileUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
			conn.setConnectTimeout(10000);
			conn.setDoInput(true);
			conn.connect();
			InputStream is = conn.getInputStream();
			bitmap = BitmapFactory.decodeStream(is);
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bitmap;
	}
	/**
	 * 保存图片到SD卡上
	 */
	public void saveBitmap(Bitmap bitmap, Context context) {
			creat_file(context);
			String image_path = Environment.getExternalStorageDirectory()+File.separator+"shike"+File.separator+"iamge";  
			File file = new File(image_path,System.currentTimeMillis() + ".png");
			FileOutputStream stream;
			try {
				stream = new FileOutputStream(file);
				bitmap.compress(CompressFormat.PNG, 100, stream);
				stream.flush();
				stream.close();
				Toast.makeText(context, "保存图片成功", 0).show();
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_MEDIA_MOUNTED);
				intent.setData(Uri.fromFile(Environment.getExternalStorageDirectory()));
				context.sendBroadcast(intent);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} 
	/**
	 * 保存图片到SD卡上
	 */
	public String saveBitmap(Context context, Bitmap bitmap) {
		creat_file(context);
		String ALBUM_PATH = Environment.getExternalStorageDirectory()+File.separator+"shike"+File.separator+"iamge";  
		File file = new File(ALBUM_PATH,System.currentTimeMillis() + ".png");
			try {
				FileOutputStream stream = new FileOutputStream(file);
				bitmap.compress(CompressFormat.PNG, 100, stream);
				stream.flush();
				stream.close();
				return file.getAbsolutePath();
			} catch (Exception e) {
				return "";
			}
	}
	/**
	 * 适配网络图片在本地显示
	 * 
	 * @param bitmap
	 * @return
	 */
	public DisplayImageOptions Picture_Shipei(int bitmap) {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.cacheOnDisc(true).showImageForEmptyUri(bitmap).showImageOnLoading(R.drawable.loading_img)
				.showImageOnFail(bitmap).cacheInMemory(true).build();
		return options;
	}

	/**
	 * TextView图片进行代码替换
	 */
	public void setTextImage_Replace(Context context, TextView view) {
		Drawable drawable = context.getResources().getDrawable(R.drawable.comgigm2);
		// / 这一步必须要做,否则不会显示.
		drawable.setBounds(0, 0, drawable.getMinimumWidth(),drawable.getMinimumHeight());
		view.setCompoundDrawables(drawable, null, null, null);
	}
	public void creat_file(Context context){
		String ALBUM_PATH = Environment.getExternalStorageDirectory()+File.separator+"shike";  
		boolean sdCardExist  = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
		if(!sdCardExist){
			Toast.makeText(context,"内存卡不存在", 0).show();
		}else {
			//创建一级目录
			File one_file = new File(ALBUM_PATH);
			if(!one_file.exists()){
				one_file.mkdir();
			}
			//创建二级目录
			File iamge = new File(ALBUM_PATH,"iamge");
			if(!iamge.exists()){
				iamge.mkdir();
			}
			File voice = new File(ALBUM_PATH,"voice");
			if(!voice.exists()){
				voice.mkdir();
			}
		}
	}
}
