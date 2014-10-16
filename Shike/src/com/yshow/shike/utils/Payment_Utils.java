package com.yshow.shike.utils;
import java.net.URLEncoder;
import android.content.Context;
import com.yshow.shike.entity.Pay_Comm.Pay_Comm_info;
/**
 *支付工具类
 * @author Administrator
 *
 */
public class Payment_Utils {
     private static Payment_Utils payment = new Payment_Utils();
     private Payment_Utils (){}
     public static Payment_Utils getIntence(){
    	 return payment;
     }
     /**
      *对充值成功结果进行判断    数据的结局
      * @param context
      * @param result
      */
     public Boolean Judge_Results(Context context,String result){
    		 String returnCode = result.substring(14, 18);
    		 String isSuccess = result.substring(result.lastIndexOf("success=\"")+9, result.lastIndexOf("\"&sign_type="));
    		 if(returnCode.equals(9000) &&  isSuccess.equals("true")){
    			 return true;
    		 }
    		 return false;
     }
     /**
      * 对订单进行处理
     * @return 
      */
     public String Order_Process (String info){
    		 info += "&sign=\"" +Paly_Info(info) + "\"&" +getSignType();
    		 String orderInfo = info;
    		 return orderInfo;
     }
     /**
      *对订单进行编码
      * @param info
      * @return
      */
     private String Paly_Info(String info){
		  String sign = Rsa.sign(info, PartnerConfig.PRIVATE).trim();
		  sign = URLEncoder.encode(sign);
		  return sign;
     }
     /**
      * 生成订单
      * @param info
      * @return
      */
	public String getNewOrderInfo(Pay_Comm_info info) {
		StringBuilder sb = new StringBuilder();
		sb.append("partner=\"");
		sb.append(PartnerConfig.DEFAULT_PARTNER);
		sb.append("\"&out_trade_no=\"");
		sb.append(info.getOut_trade_no());
		sb.append("\"&subject=\"");
		sb.append(info.getSubject());
		sb.append("\"&body=\"");
		sb.append(info.getBody());
		sb.append("\"&total_fee=\"");
		sb.append(info.getTotal_fee());
		sb.append("\"&notify_url=\"");
		// 网址需要做URL编码
//		sb.append(URLEncodedUtils.isEncoded(info.getNotify_url()));
//		sb.append("\"&_input_charset=\"UTF-8");
		sb.append(info.getNotify_url());
		sb.append("\"&service=\"mobile.securitypay.pay");
		sb.append("\"&payment_type=\"1");
		sb.append("\"&seller_id=\"");
		sb.append(PartnerConfig.DEFAULT_SELLER);
		sb.append("\"&it_b_pay=\"1m");
		sb.append("\"");
		return new String(sb);
	}
 	/**
 	 *签收类型
 	 * @return
 	 */
 	private String getSignType() {
		return "sign_type=\"RSA\"";
	}
} 
