package com.yshow.shike.entity;

public class Student_Info {
   String icon;
   String uid;
   String name;
   String nickname;
   String points;
   String info;
   String gradeName;
   String grade;
   String questions;
   public String area;
    public String areaid;

@Override
public String toString() {
	return "Student_Info [icon=" + icon + ", uid=" + uid + ", name=" + name
			+ ", nickname=" + nickname + ", points=" + points + ", info="
			+ info + ", gradeName=" + gradeName + ", grade=" + grade
			+ ", questions=" + questions + "]";
}
public String getIcon() {
	return icon;
}
public void setIcon(String icon) {
	this.icon = icon;
}
public String getUid() {
	return uid;
}
public void setUid(String uid) {
	this.uid = uid;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public String getNickname() {
	return nickname;
}
public void setNickname(String nickname) {
	this.nickname = nickname;
}
public String getPoints() {
	return points;
}
public void setPoints(String points) {
	this.points = points;
}
public String getInfo() {
	return info;
}
public void setInfo(String info) {
	this.info = info;
}
public String getGradeName() {
	return gradeName;
}
public void setGradeName(String gradeName) {
	this.gradeName = gradeName;
}
public String getGrade() {
	return grade;
}
public void setGrade(String grade) {
	this.grade = grade;
}
public String getQuestions() {
	return questions;
}
public void setQuestions(String questions) {
	this.questions = questions;
}
}
