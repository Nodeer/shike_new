package com.yshow.shike.entity;

import java.io.Serializable;

@SuppressWarnings("serial")
public class User_Info implements Serializable{
	String points;
    String Name ;
    String Nickname;
    String Icon;
    String questions;
    String gradeName = " ";
    String info;
    String FansNum;
    String Subjectid;
    String Subject;
    String picurl;
    String greatid;
    String claim_question_num;
    String like_num;
    String uid;
    String subjectId;
    String fromGradeId;
    String toGradeId;
    String shool;
    String email;
    String Mob;
    String grade;
    String birthday;
    String sex;
    String area;

    public String bankId;//兑换现金时的银行id
    public String bankName;//兑换现金时的银行名字
    public String bankAddr;//开户行名称
    public String bankNO;//银行卡号
    public String receiver;//兑换商品时收件人名字
    public String phone;//兑换商品手机号码
    public String addressee;//地址
    public String paper;//教师资格证id

	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getShool() {
		return shool;
	}

	public void setShool(String shool) {
		this.shool = shool;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMob() {
		return Mob;
	}

	public void setMob(String mob) {
		Mob = mob;
	}

	public String getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}

	public String getFromGradeId() {
		return fromGradeId;
	}

	public void setFromGradeId(String fromGradeId) {
		this.fromGradeId = fromGradeId;
	}

	public String getToGradeId() {
		return toGradeId;
	}

	public void setToGradeId(String toGradeId) {
		this.toGradeId = toGradeId;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getClaim_question_num() {
		return claim_question_num;
	}

	public void setClaim_question_num(String claim_question_num) {
		this.claim_question_num = claim_question_num;
	}

	public String getLike_num() {
		return like_num;
	}

	public void setLike_num(String like_num) {
		this.like_num = like_num;
	}

	public String getGreatid() {
		return greatid;
	}

	public void setGreatid(String greatid) {
		this.greatid = greatid;
	}

	public String getPicurl() {
		return picurl;
	}

	public void setPicurl(String picurl) {
		this.picurl = picurl;
	}

	public String getSubject() {
		return Subject;
	}

	public void setSubject(String subject) {
		Subject = subject;
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

	public String getName() {
		return Name;
	}

	public String getFansNum() {
		return FansNum;
	}

	public void setFansNum(String fansNum) {
		FansNum = fansNum;
	}

	public String getSubjectid() {
		return Subjectid;
	}

	public void setSubjectid(String subjectid) {
		Subjectid = subjectid;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getNickname() {
		return Nickname;
	}

	public void setNickname(String nickname) {
		Nickname = nickname;
	}

	public String getIcon() {
		return Icon;
	}

	public void setIcon(String icon) {
		Icon = icon;
	}

	public String getQuestions() {
		return questions;
	}

	public void setQuestions(String questions) {
		this.questions = questions;
	}


	public String getPoints() {
		return points;
	}

	public void setPoints(String points) {
		this.points = points;
	}	
	@Override
	public String toString() {
		return "User_Info [points=" + points + ", Name=" + Name + ", Nickname="
				+ Nickname + ", Icon=" + Icon + ", questions=" + questions
				+ ", gradeName=" + gradeName + ", info=" + info + ", FansNum="
				+ FansNum + ", Subjectid=" + Subjectid + ", Subject=" + Subject
				+ ", picurl=" + picurl + ", greatid=" + greatid
				+ ", claim_question_num=" + claim_question_num + ", like_num="
				+ like_num + ", uid=" + uid + ", subjectId=" + subjectId
				+ ", fromGradeId=" + fromGradeId + ", toGradeId=" + toGradeId
				+ ", shool=" + shool + ", email=" + email + ", Mob=" + Mob
				+ ", grade=" + grade + ", birthday=" + birthday + ", sex="
				+ sex + ", area=" + area + "]";
	}
}
