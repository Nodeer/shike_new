package com.yshow.shike.entity;

public class RecordData {

	
	private String messge;
	private String Dates;
	public String getMessge() {
		return messge;
	}
	public void setMessge(String messge) {
		this.messge = messge;
	}
	public String getDates() {
		return Dates;
	}
	public void setDates(String dates) {
		Dates = dates;
	}
	@Override
	public String toString() {
		return "RecordData [messge=" + messge + ", Dates=" + Dates + "]";
	}
	
	
	
}
