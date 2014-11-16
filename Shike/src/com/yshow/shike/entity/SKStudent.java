package com.yshow.shike.entity;

import java.io.InputStream;
import java.io.Serializable;

public class SKStudent implements Serializable {
	/**
	 */
	private static final long serialVersionUID = -453408085297914928L;
	private String name;
	private String nickname;
	private String aId;
	private String vcodeRes;
	private String info;
	private String mob;
	private String email;
	private String pwd;
	private String sex;
	private String types;
	private String gradeId;
	private String uid;
	private String school;
	private String birthday;
	private String points;
	private int new_messges;
    private String subject;
    private String fromGradeId  ;
    private String toGradeId ;
    private String icon;
    private String Paper;
    private InputStream stream;

    public int gradePart = 0;

    public String reference = "";
    public String getPaper() {
		return Paper;
	}
	public void setPaper(String paper) {
		Paper = paper;
	}
	public InputStream getStream() {
		return stream;
	}
	public void setStream(InputStream stream) {
		this.stream = stream;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
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

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
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

	public String getaId() {
		return aId;
	}

	public void setaId(String aId) {
		this.aId = aId;
	}

	public String getVcodeRes() {
		return vcodeRes;
	}

	public void setVcodeRes(String vcodeRes) {
		this.vcodeRes = vcodeRes;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getMob() {
		return mob;
	}

	public void setMob(String mob) {
		this.mob = mob;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getTypes() {
		return types;
	}

	public void setTypes(String types) {
		this.types = types;
	}

	public String getGradeId() {
		return gradeId;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getPoints() {
		return points;
	}

	public void setPoints(String points) {
		this.points = points;
	}

	public int getNew_messges() {
		return new_messges;
	}

	public void setNew_messges(int new_messges) {
		this.new_messges = new_messges;
	}

	public SKStudent(String name, String nickname, String aId, String vcodeRes,
			String info, String mob, String email, String pwd, String sex,
			String types, String gradeId) {
		super();
		this.name = name;
		this.nickname = nickname;
		this.aId = aId;
		this.vcodeRes = vcodeRes;
		this.info = info;
		this.mob = mob;
		this.email = email;
		this.pwd = pwd;
		this.sex = sex;
		this.types = types;
		this.gradeId = gradeId;
	}

	public SKStudent() {
		super();
	}

	@Override
	public String toString() {
		return "SKStudent [name=" + name + ", nickname=" + nickname + ", aId="
				+ aId + ", vcodeRes=" + vcodeRes + ", info=" + info + ", mob="
				+ mob + ", email=" + email + ", pwd=" + pwd + ", sex=" + sex
				+ ", types=" + types + ", gradeId=" + gradeId + ", uid=" + uid
				+ ", school=" + school + ", birthday=" + birthday + ", points="
				+ points + ", new_messges=" + new_messges + ", subject="
				+ subject + ", fromGradeId=" + fromGradeId + ", toGradeId="
				+ toGradeId + ", icon=" + icon + ", Paper=" + Paper
				+ ", stream=" + stream + "]";
	}
}
