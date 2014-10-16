package com.yshow.shike.entity;

public class Count_Info {
   String id;
   String uid;
   String questionId;
   String points;
   String types;
   String num;
   String messge;
   String adds;
   String Dates;
   String H;
   String i;
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
public String getQuestionId() {
	return questionId;
}
public void setQuestionId(String questionId) {
	this.questionId = questionId;
}
public String getPoints() {
	return points;
}
public void setPoints(String points) {
	this.points = points;
}
public String getTypes() {
	return types;
}
public void setTypes(String types) {
	this.types = types;
}
public String getNum() {
	return num;
}
public void setNum(String num) {
	this.num = num;
}
public String getMessge() {
	return messge;
}
public void setMessge(String messge) {
	this.messge = messge;
}
public String getAdds() {
	return adds;
}
public void setAdds(String adds) {
	this.adds = adds;
}
public String getDates() {
	return Dates;
}
public void setDates(String dates) {
	Dates = dates;
}
public String getH() {
	return H;
}
public void setH(String h) {
	H = h;
}
public String getI() {
	return i;
}
public void setI(String i) {
	this.i = i;
}
@Override
public String toString() {
	return "Count_Info [id=" + id + ", uid=" + uid + ", questionId="
			+ questionId + ", points=" + points + ", types=" + types + ", num="
			+ num + ", messge=" + messge + ", adds=" + adds + ", Dates="
			+ Dates + ", H=" + H + ", i=" + i + "]";
} 
}
