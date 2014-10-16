package com.yshow.shike.entity;

import java.util.ArrayList;

public class SKGrade {
	private String id;
	private String name;
	private ArrayList<SkClasses> classes;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<SkClasses> getClasses() {
		return classes;
	}

	public void setClasses(ArrayList<SkClasses> classes) {
		this.classes = classes;
	}

	public SKGrade() {
		super();
	}

	@Override
	public String toString() {
		return "SKGrade [id=" + id + ", name=" + name + ", classes=" + classes
				+ "]";
	}

}
