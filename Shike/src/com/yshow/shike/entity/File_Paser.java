package com.yshow.shike.entity;

public class File_Paser {
   String id;
   String uid;
   String name;
   String cid;
public String getId() {
	return id;
}
public void setId(String id) {
	this.id = id;
}
public String getUid() {
	return uid;
}
public void setUid(String uid) {
	this.uid = uid;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public String getCid() {
	return cid;
}
public void setCid(String cid) {
	this.cid = cid;
}
@Override
public String toString() {
	return "File_Paser [id=" + id + ", uid=" + uid + ", name=" + name
			+ ", cid=" + cid + "]";
}
}
