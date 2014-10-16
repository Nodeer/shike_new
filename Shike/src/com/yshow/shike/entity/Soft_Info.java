package com.yshow.shike.entity;

public class Soft_Info {
	String text;
	String url;

	public String newversion = "0.0";
    public String updateurl = "";

    public String agreement = "";
    public String agreementurl = "";
    public String introduce = "";
    public String introduceurl = "";
    public String FAQ = "";
    public String FAQurl = "";
    public String FAQ_T = "";
    public String FAQ_Turl = "";

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		return "Soft_Info [text=" + text + ", url=" + url + "]";
	}

}
