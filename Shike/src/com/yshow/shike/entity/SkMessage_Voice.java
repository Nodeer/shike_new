package com.yshow.shike.entity;

import java.io.Serializable;

public class SkMessage_Voice implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3605097760316801818L;
	private String id;
	private String uid;
	private String questionId;
	private String fileId;
	private String resId;
	private String types;
	private String isStudent;
	private String isFirst;
	private String iSpublic;
	private String file;
	public String getTypes() {
		return types;
	}

	public void setTypes(String types) {
		this.types = types;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

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

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

}
