package com.yshow.shike.utils;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

public class Take_Phon_album {
	private static Take_Phon_album album = new Take_Phon_album();
	private Take_Phon_album() {}
	public static Take_Phon_album getIntence() {
		return album;
	}
	public void startImageCapture(Activity act, int resultCode) {
		// 统计启动相机的次数
		long maxM = Runtime.getRuntime().maxMemory() / 1024 / 1024;
		long nowM = android.os.Debug.getNativeHeapAllocatedSize() / 1024 / 1024;
		if (maxM - nowM < 2) {
			Toast.makeText(act, "内存不足,请释放部分内存再试", Toast.LENGTH_SHORT).show();
			return;
		}
		File cameraFile = new File(Environment.getExternalStorageDirectory().getPath(), "camera.jpg");
		Uri outputUri = Uri.fromFile(cameraFile);
		Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
		intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
		act.startActivityForResult(intent, resultCode);
	}
	/**
	 * 启动系统相册
	 * 
	 * @param resultCode
	 */
	public void gotoSysPic(Activity act, int resultCode) {
		long maxM = Runtime.getRuntime().maxMemory() / 1024 / 1024;
		long nowM = android.os.Debug.getNativeHeapAllocatedSize() / 1024 / 1024;
		if (maxM - nowM < 2) {
			Toast.makeText(act, "内存不足,请释放部分内存再试", Toast.LENGTH_SHORT).show();
			return;
		}

        //2014-7-23修改调用系统相册的方法...之前的在某些机型上会报错...//徐斌
//		Intent getImage = new Intent(Intent.ACTION_GET_CONTENT);
        Intent getImage = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//		getImage.addCategory(Intent.CATEGORY_OPENABLE);
//		getImage.setType("image/*");
		act.startActivityForResult(getImage, resultCode);
	}

	public String uriToPath(Activity act, Uri uri) {
		Cursor cursor = null;
		try {
			String[] proj = { MediaStore.Images.Media.DATA };
			cursor = act.managedQuery(uri, proj, null, null, null);
			int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return null;
	}

	// 从相册拿数据
	public void Take_Pickture(Context context, int requestCode) {
		Intent xiagnche = new Intent(Intent.ACTION_GET_CONTENT);
		xiagnche.setType("image/*");
		((Activity) context).startActivityForResult(xiagnche, requestCode);
	}

	// 从相册拿数据 可以截图 配合 startPhotoZoom（）；
	public void Take_Pickture(int requestCode, Context context) {
		Intent xiagnche = new Intent(Intent.ACTION_PICK, null);
		xiagnche.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				"image/*");
		((Activity) context).startActivityForResult(xiagnche, requestCode);
	}

	// 相机拍照
	public void Take_Phone(int requestCode, Context context) {
		Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
		((Activity) context).startActivityForResult(intent, requestCode);
	}

	// 取相册里面的图片 进行显示
	public void startPhotoZoom(Context context, Uri uri, int SETTO_IMAGEVIEW) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", true);
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 150);
		intent.putExtra("outputY", 150);
		intent.putExtra("return-data", true);
		((Activity) context).startActivityForResult(intent, SETTO_IMAGEVIEW);
	}
}
