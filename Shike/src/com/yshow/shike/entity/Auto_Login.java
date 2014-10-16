package com.yshow.shike.entity;

public class Auto_Login {
	private String uid;
	private String mac;
	private String Type;
	private String icon;
	private String nickname;
	public String mob;

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getType() {
		return Type;
	}

	public void setType(String type) {
		Type = type;
	}

	public String getUid() {
		return uid;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	@Override
	public String toString() {
		return "Auto_Login [uid=" + uid + ", mac=" + mac + ", Type=" + Type + ", icon=" + icon + ", nickname=" + nickname + "]";
	}
}
