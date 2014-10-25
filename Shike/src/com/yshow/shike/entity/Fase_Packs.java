package com.yshow.shike.entity;

import java.io.Serializable;

public class Fase_Packs implements Serializable{
   String id;
   String face_id;
   String name;
   String fileId;
   String fase_url;
public String getFase_url() {
	return fase_url;
}
public void setFase_url(String fase_url) {
	this.fase_url = fase_url;
}
public String getId() {
	return id;
}
public void setId(String id) {
	this.id = id;
}
public String getFace_id() {
	return face_id;
}
public void setFace_id(String face_id) {
	this.face_id = face_id;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public String getFileId() {
	return fileId;
}
public void setFileId(String fileId) {
	this.fileId = fileId;
}
@Override
public String toString() {
	return "Fase_Packs [id=" + id + ", face_id=" + face_id + ", name=" + name
			+ ", fileId=" + fileId + ", fase_url=" + fase_url + "]";
}
}
