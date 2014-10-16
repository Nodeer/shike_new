package com.yshow.shike.entity;

import java.util.ArrayList;

public class My_Student_Info {
	String questions;
	private ArrayList<Star_Teacher_Parse> classes;

	public String getQuestions() {
		return questions;
	}

	public void setQuestions(String questions) {
		this.questions = questions;
	}

	public ArrayList<Star_Teacher_Parse> getClasses() {
		return classes;
	}

	public void setClasses(ArrayList<Star_Teacher_Parse> classes) {
		this.classes = classes;
	}

	@Override
	public String toString() {
		return "My_Student_Info [questions=" + questions + ", classes="
				+ classes + "]";
	}

}
