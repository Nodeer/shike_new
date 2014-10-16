package com.yshow.shike.entity;

public class SkClasses {
	private String id;
	private String gradeId;
	private String name;
	private String num;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGradeId() {
		return gradeId;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	public String getName() {
		return name;
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

	public SkClasses() {
		super();
	}

	@Override
	public String toString() {
		return "SkClasses [id=" + id + ", gradeId=" + gradeId + ", name="
				+ name + ", num=" + num + "]";
	}

}
