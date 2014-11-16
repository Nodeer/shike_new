package com.yshow.shike.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXWebpageObject;
import com.yshow.shike.R;

public class WeixinManager {
	private final String title = "师课分享";
	private final String content = "嗨！我发现一个帮我学习的好应用“师课家教宝”，任何难题都能找到老师一对一辅导，快速、方便、准确。下载地址：www.schoool.cn/ipa";
	private Activity context;
	private Bitmap picture;
	private static final String APP_ID = "wx24f646c87357277a";
	public static final int SUPPORT_FRIENDS_VERSION = 0x21020001;
	private IWXAPI api;

	public IWXAPI getApi() {
		return api;
	}

	public WeixinManager(Activity context) {
		super();
		this.context = context;
        LogUtil.d("分享初始化");
		regToWx();
	}

	private void regToWx() {
		api = WXAPIFactory.createWXAPI(context, APP_ID, true);
		api.registerApp(APP_ID);
	}

	/**
	 * 分享微信
	 */
	public void shareWeixin() {
//		WeixinManager wxMgr = new WeixinManager(context);
		if (getApi().isWXAppInstalled()) {
			Bundle shareBundle = new Bundle();
			shareBundle.putBoolean("isWeixinCircle", false);
			sendTextMsgToWx(title, content, false);
		} else {
			Toast.makeText(context, "未安装微信", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 分享微信朋友圈
	 */
	public void shareWeixinCircle() {
//		WeixinManager wxMgr = new WeixinManager(context);
		if (getApi().isWXAppInstalled()) {
			if (getApi().isWXAppSupportAPI()) {
				sendTextMsgToWx(title, content, true);
			} else {
				Toast.makeText(context, "微信版本不支持朋友圈", Toast.LENGTH_SHORT).show();
			}
		} else {
			Toast.makeText(context, "未安装微信", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 微信分享 集成方法
	 * 
	 * @param title
	 * @param description
	 * @param isToTimeline
	 *            true:朋友圈 false:为分享微信朋友圈
	 */
	public void sendTextMsgToWx(String title, String description,boolean isToTimeline) {
		picture = BitmapFactory.decodeResource(context.getResources(),R.drawable.icon);
		WXWebpageObject webObj = new WXWebpageObject();
        webObj.webpageUrl = "http://www.schoool.cn/ipa";
		final SendMessageToWX.Req req = new SendMessageToWX.Req();
		if (isToTimeline) {
			req.scene = SendMessageToWX.Req.WXSceneTimeline;
		} else {
			req.scene = SendMessageToWX.Req.WXSceneSession;
		}
		final WXMediaMessage msg = new WXMediaMessage();
		msg.mediaObject = webObj;
		msg.description = description;
		msg.title = title;
		msg.setThumbImage(picture);
		req.transaction = String.valueOf(System.currentTimeMillis());
		req.message = msg;
		api.sendReq(req);
	}

	public void unRegToWx() {
		api.unregisterApp();
	}

	private Bitmap comp(Bitmap image) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		if (baos.toByteArray().length / 1024 > 1024) {// 判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
			baos.reset();// 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, 50, baos);// 这里压缩50%，把压缩后的数据存放到baos中
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		// 长宽设为200x100
		float hh = 100f;// 这里设置高度为100f
		float ww = 200f;// 这里设置宽度为200f
		// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;// be=1表示不缩放
		if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// 设置缩放比例
		// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		isBm = new ByteArrayInputStream(baos.toByteArray());
		bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
	}

	private Bitmap compressImage(Bitmap image) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
			baos.reset();// 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
			options -= 10;// 每次都减少10
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
		return bitmap;
	}

}
