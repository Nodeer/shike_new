package com.yshow.shike.entity;

import java.io.Serializable;

@SuppressWarnings("serial")
public class SKAcknowledge_list implements Serializable {
	private String answer_messge;
	private String gift2points;
	public String getAnswer_messge() {
		return answer_messge;
	}
	public void setAnswer_messge(String answer_messge) {
		this.answer_messge = answer_messge;
	}
	public String getGift2points() {
		return gift2points;
	}

	public void setGift2points(String gift2points) {
		this.gift2points = gift2points;
	}

	public SKAcknowledge_list() {
		super();
	}

	@Override
	public String toString() {
		return "SKAcknowledge_list [answer_messge=" + answer_messge
				+ ", gift2points=" + gift2points + "]";
	}

}
