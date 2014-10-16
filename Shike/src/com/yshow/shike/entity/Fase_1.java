package com.yshow.shike.entity;

import java.util.ArrayList;

public class Fase_1 {
   String id;
   String fileId;
   String name;
   String price;
   String count;
   private ArrayList<Fase_Packs> res;;
public ArrayList<Fase_Packs> getRes() {
	return res;
}
public void setRes(ArrayList<Fase_Packs> res) {
	this.res = res;
}
public String getId() {
	return id;
}
public void setId(String id) {
	this.id = id;
}
public String getFileId() {
	return fileId;
}
public void setFileId(String fileId) {
	this.fileId = fileId;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public String getPrice() {
	return price;
}
public void setPrice(String price) {
	this.price = price;
}
public String getCount() {
	return count;
}
public void setCount(String count) {
	this.count = count;
}
@Override
public String toString() {
	return "Fase_1 [id=" + id + ", fileId=" + fileId + ", name=" + name
			+ ", price=" + price + ", count=" + count + ", res=" + res + "]";
} 
}
