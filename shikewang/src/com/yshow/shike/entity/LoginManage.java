package com.yshow.shike.entity;

import com.yshow.shike.utils.FileService;

import android.content.Context;

public class LoginManage{
	private boolean mLoginSuccess;
	private SKStudent student;
	public static LoginManage instance;
	private LoginManage() {}
	public static LoginManage getInstance() {
		if (instance == null) {
			instance = new LoginManage();
		}
		return instance;
	}
	public boolean isTeacher() {
		boolean isTeacher = student.getTypes().equals("1");
		return isTeacher;
	}
	public boolean isTeacher(Context context){
		return new FileService(context).getBoolean("is_tea", false);
	}
	public boolean ismLoginSuccess() {
		return mLoginSuccess;
	}
	public void setmLoginSuccess(boolean mLoginSuccess) {
		this.mLoginSuccess = mLoginSuccess;
	}
	public SKStudent getStudent() {
		return student;
	}
	public void setStudent(SKStudent student) {
		this.student = student;
	}
	@Override
	public String toString() {
		return "LoginManage [mLoginSuccess=" + mLoginSuccess + ", student="+ student + "]";
	}
}