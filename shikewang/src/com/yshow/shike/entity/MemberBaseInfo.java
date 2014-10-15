package com.yshow.shike.entity;

public class MemberBaseInfo {
   public String memberName;
   public String password;
	public String getMemberName() {
		return memberName;
	}
	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	@Override
	public String toString() {
		return "MemberBaseInfo [memberName=" + memberName + ", password="
				+ password + "]";
	}
}
