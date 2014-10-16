package com.yshow.shike.entity;

import java.io.Serializable;
import java.util.ArrayList;

public class SKMessage implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1679937360532594605L;
	private String id;
	private String uid;
	private String teachUid;
	private String subjectId;
	private String questionId;
	private String resNum;
	private String updateNum;
	private String createTime;
	private String last_uid;
	private String acknowledge;
	private String complain;
	private String claim_uid;
	private String claim_time;
	private boolean isDone;
	private String updateTime;
	private String newM;
	private String iSmyteach;
	private String ResCount;
	private String date;
	private String userName;
	private String icon;
	private String nickname;
    /**1表示系统消息,禁止交互,0表示普通消息*/
    private String msgType = "0";
	private SKAcknowledge_list sKAcknowledge_list;
	private ArrayList<SkMessage_Res> res;

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public SKAcknowledge_list getsKAcknowledge_list() {
		return sKAcknowledge_list;

	}

	public void setsKAcknowledge_list(SKAcknowledge_list sKAcknowledge_list) {
		this.sKAcknowledge_list = sKAcknowledge_list;
	}

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

	public String getTeachUid() {
		return teachUid;
	}

	public void setTeachUid(String teachUid) {
		this.teachUid = teachUid;
	}

	public String getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}

	public String getQuestionId() {
		return questionId;
	}

	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}

	public String getResNum() {
		return resNum;
	}

	public void setResNum(String resNum) {
		this.resNum = resNum;
	}

	public String getUpdateNum() {
		return updateNum;
	}

	public void setUpdateNum(String updateNum) {
		this.updateNum = updateNum;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getLast_uid() {
		return last_uid;
	}

	public void setLast_uid(String last_uid) {
		this.last_uid = last_uid;
	}

	public String getAcknowledge() {
		return acknowledge;
	}

	public void setAcknowledge(String acknowledge) {
		this.acknowledge = acknowledge;
	}

	public String getComplain() {
		return complain;
	}

	public void setComplain(String complain) {
		this.complain = complain;
	}

	public String getClaim_uid() {
		return claim_uid;
	}

	public void setClaim_uid(String claim_uid) {
		this.claim_uid = claim_uid;
	}

	public String getClaim_time() {
		return claim_time;
	}

	public void setClaim_time(String claim_time) {
		this.claim_time = claim_time;
	}

	public boolean isDone() {
		return isDone;
	}

	public void setDone(boolean isDone) {
		this.isDone = isDone;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getNewM() {
		return newM;
	}

	public void setNewM(String newM) {
		this.newM = newM;
	}

	public String getiSmyteach() {
		return iSmyteach;
	}

	public void setiSmyteach(String iSmyteach) {
		this.iSmyteach = iSmyteach;
	}

	public String getResCount() {
		return ResCount;
	}

	public void setResCount(String resCount) {
		ResCount = resCount;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public ArrayList<SkMessage_Res> getRes() {
		return res;
	}

	public void setRes(ArrayList<SkMessage_Res> res) {
		this.res = res;
	}

	@Override
	public String toString() {
		return "SKMessage [id=" + id + ", uid=" + uid + ", teachUid="
				+ teachUid + ", subjectId=" + subjectId + ", questionId="
				+ questionId + ", resNum=" + resNum + ", updateNum="
				+ updateNum + ", createTime=" + createTime + ", last_uid="
				+ last_uid + ", acknowledge=" + acknowledge + ", complain="
				+ complain + ", claim_uid=" + claim_uid + ", claim_time="
				+ claim_time + ", isDone=" + isDone + ", updateTime="
				+ updateTime + ", newM=" + newM + ", iSmyteach=" + iSmyteach
				+ ", ResCount=" + ResCount + ", date=" + date + ", userName="
				+ userName + ", icon=" + icon + ", nickname=" + nickname
				+ ", res=" + res + "]";
	}

}
