package com.yshow.shike.entity;

import java.io.Serializable;

/**
 * 用于提问时解析地區数据
 */
public class SKTeacherOrSubject implements Serializable {

	private static final long serialVersionUID = 8049001092579301464L;
	private String teacherId;
	private String name;
	private String subjectId;
	public SKTeacherOrSubject() {
		super();
	}

	public String getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}

	@Override
	public String toString() {
		return "SKTeacherOnQuestion [teacherId=" + teacherId + ", name=" + name
				+ ", subjectId=" + subjectId + "]";
	}
   
}
