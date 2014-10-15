package com.yshow.shike.entity;

public class Mob_Info {
	String subject;
	String area;
	String fromGrade;
	String fromGrade_num;

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getFromGrade() {
		return fromGrade;
	}

	public void setFromGrade(String fromGrade) {
		this.fromGrade = fromGrade;
	}

	public String getFromGrade_num() {
		return fromGrade_num;
	}

	public void setFromGrade_num(String fromGrade_num) {
		this.fromGrade_num = fromGrade_num;
	}

	@Override
	public String toString() {
		return "Mob_Info [subject=" + subject + ", area=" + area
				+ ", fromGrade=" + fromGrade + ", fromGrade_num="
				+ fromGrade_num + "]";
	}

}
