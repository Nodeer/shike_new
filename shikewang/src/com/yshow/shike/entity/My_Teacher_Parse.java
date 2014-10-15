package com.yshow.shike.entity;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class My_Teacher_Parse implements Serializable{
	String subject;
	List<Star_Teacher_Parse> list;
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public List<Star_Teacher_Parse> getList() {
		return list;
	}
	public void setList(List<Star_Teacher_Parse> list) {
		this.list = list;
	}
	@Override
	public String toString() {
		return "My_Teacher_Parse [subject=" + subject + ", list=" + list + "]";
	}	
}
