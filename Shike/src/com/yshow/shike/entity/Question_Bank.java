package com.yshow.shike.entity;
import java.io.Serializable;
import java.util.ArrayList;
@SuppressWarnings("serial")
public class Question_Bank implements Serializable{
	private String question;
	private String name;
	private String data;
	private String subject;
	private String cid;
//	private Boolean category;
	private String category1;
	private ArrayList<SkMessage_Res> res;
	public String getCategory1() {
		return category1;
	}
	public void setCategory1(String category1) {
		this.category1 = category1;
	}
//	public void setCategory(Boolean category) {
//		this.category = category;
//	}
	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public ArrayList<SkMessage_Res> getRes() {
		return res;
	}

	public void setRes(ArrayList<SkMessage_Res> res) {
		this.res = res;
	}

	@Override
	public String toString() {
		return "Question_Bank [question=" + question + ", name=" + name
				+ ", data=" + data + ", subject=" + subject + ", cid=" + cid
				+ ", category1=" + category1
				+ ", res=" + res + "]";
	}

}
