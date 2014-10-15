package com.yshow.shike.utils;

public class SkArea_Item {
   String id;
   String gradeId;
   String name;
   String num;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public String getGradeId() {
		return gradeId;
	}
	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	@Override
	public String toString() {
		return "SkArea_Item [id=" + id + ", gradeId=" + gradeId + ", name="
				+ name + ", num=" + num + "]";
	}
}
