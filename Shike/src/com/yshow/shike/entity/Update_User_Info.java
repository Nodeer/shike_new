package com.yshow.shike.entity;

public class Update_User_Info {
    String name;
    String nickname;
    String  aId;
    String birthday;
    String info;
    String school;
    String subjectId;
    String fromGradeId = "-1";
    String toGradeId = "-1";
    String old_pwd;
    String pwd;
    String types;
    String icon;
    String sex;
    String Paper;
    String email;
    String gradeId;
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
	public String getaId() {
		return aId;
	}
	public void setaId(String aId) {
		this.aId = aId;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public String getSchool() {
		return school;
	}
	public void setSchool(String school) {
		this.school = school;
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
	public String getOld_pwd() {
		return old_pwd;
	}
	public void setOld_pwd(String old_pwd) {
		this.old_pwd = old_pwd;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getTypes() {
		return types;
	}
	public void setTypes(String types) {
		this.types = types;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getPaper() {
		return Paper;
	}
	public void setPaper(String paper) {
		Paper = paper;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getGradeId() {
		return gradeId;
	}
	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}
	@Override
	public String toString() {
		return "Update_User_Info [name=" + name + ", nickname=" + nickname
				+ ", aId=" + aId + ", birthday=" + birthday + ", info=" + info
				+ ", school=" + school + ", subjectId=" + subjectId
				+ ", fromGradeId=" + fromGradeId + ", toGradeId=" + toGradeId
				+ ", old_pwd=" + old_pwd + ", pwd=" + pwd + ", types=" + types
				+ ", icon=" + icon + ", sex=" + sex + ", Paper=" + Paper
				+ ", email=" + email + ", gradeId=" + gradeId + "]";
	}
    
}
