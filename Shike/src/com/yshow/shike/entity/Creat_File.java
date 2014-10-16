package com.yshow.shike.entity;

public class Creat_File {
    String name;
    String uid;
    String cid;
    
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	@Override
	public String toString() {
		return "Creat_File [name=" + name + ", uid=" + uid + ", cid=" + cid
				+ "]";
	}
}
