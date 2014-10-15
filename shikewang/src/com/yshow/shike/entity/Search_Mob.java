package com.yshow.shike.entity;
import java.util.ArrayList;
public class Search_Mob {
	private String subject;
	private ArrayList<Mob_Info> classes;

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public ArrayList<Mob_Info> getClasses() {
		return classes;
	}

	public void setClasses(ArrayList<Mob_Info> classes) {
		this.classes = classes;
	}

	@Override
	public String toString() {
		return "Search_Mob [subject=" + subject + ", classes=" + classes + "]";
	}

}
