package com.yshow.shike.entity;

import java.io.Serializable;
import java.util.ArrayList;

public class SkMessage_Res implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4931555605890082982L;
	private String id;
	private String uid;
	private String questionId;
	private String fileId;
	private String resId;
	private String isStudent;
	private String types;
	private String isFirst;
	private String iSpublic;
	private String face;
	private String file;
	private String file_tub;
	@Override
	public String toString() {
		return "SkMessage_Res [id=" + id + ", uid=" + uid + ", questionId="
				+ questionId + ", fileId=" + fileId + ", resId=" + resId
				+ ", isStudent=" + isStudent + ", types=" + types
				+ ", isFirst=" + isFirst + ", iSpublic=" + iSpublic + ", face="
				+ face + ", file=" + file + ", file_tub=" + file_tub
				+ ", voice=" + voice + "]";
	}
	public String getFile_tub() {
		return file_tub;
	}

	public void setFile_tub(String file_tub) {
		this.file_tub = file_tub;
	}

	private ArrayList<SkMessage_Voice> voice;
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getQuestionId() {
		return questionId;
	}

	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getResId() {
		return resId;
	}

	public void setResId(String resId) {
		this.resId = resId;
	}

	public String getIsStudent() {
		return isStudent;
	}

	public void setIsStudent(String isStudent) {
		this.isStudent = isStudent;
	}

	public String getIsFirst() {
		return isFirst;
	}

	public void setIsFirst(String isFirst) {
		this.isFirst = isFirst;
	}

	public String getiSpublic() {
		return iSpublic;
	}

	public void setiSpublic(String iSpublic) {
		this.iSpublic = iSpublic;
	}

	public String getFace() {
		return face;
	}

	public void setFace(String face) {
		this.face = face;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public ArrayList<SkMessage_Voice> getVoice() {
		return voice;
	}

	public void setVoice(ArrayList<SkMessage_Voice> voice) {
		this.voice = voice;
	}

	public String getTypes() {
		return types;
	}

	public void setTypes(String types) {
		this.types = types;
	}

}
