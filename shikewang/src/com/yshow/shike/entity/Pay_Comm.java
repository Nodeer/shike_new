package com.yshow.shike.entity;
 /**
  *商品信息data
  * @author Administrator
  */
public class Pay_Comm {
    Pay_Comm_info data;
	public Pay_Comm_info getData() {
		return data;
	}
	public void setData(Pay_Comm_info data) {
		this.data = data;
	}
	@Override
	public String toString() {
		return "Pay_Comm [data=" + data + "]";
	}
	/**
	 *内部类 商品详细信息解析
	 * @author Administrator
	 */
	public class Pay_Comm_info{
    String total_fee;
    String notify_url;
    String subject;
    String body;
    String partner;
    String out_trade_no;
	public String getTotal_fee() {
		return total_fee;
	}
	public void setTotal_fee(String total_fee) {
		this.total_fee = total_fee;
	}
	public String getNotify_url() {
		return notify_url;
	}
	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getPartner() {
		return partner;
	}
	public void setPartner(String partner) {
		this.partner = partner;
	}
	public String getOut_trade_no() {
		return out_trade_no;
	}
	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}
	@Override
	public String toString() {
		return "Pay_Comm_info [total_fee=" + total_fee + ", notify_url="
				+ notify_url + ", subject=" + subject + ", body=" + body
				+ ", partner=" + partner + ", out_trade_no=" + out_trade_no
				+ "]";
	}
   }
}