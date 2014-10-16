package com.yshow.shike.entity;
import java.util.List;
import android.annotation.SuppressLint;
/**
 * 上传问题实体
 */
import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
@SuppressLint("ParcelCreator")
public class SkUpLoadQuestion implements Parcelable {
	private Bitmap bitmap;
	private List<String> urllist;
	public Bitmap getBitmap() {
		return bitmap;
	}
	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}
	public List<String> getUrllist() {
		return urllist;
	}
	public void setUrllist(List<String> urllist) {
		this.urllist = urllist;
	}
	public SkUpLoadQuestion(Bitmap bitmap, List<String> urllist) {
		super();
		this.bitmap = bitmap;
		this.urllist = urllist;
	}
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {

	}
}
