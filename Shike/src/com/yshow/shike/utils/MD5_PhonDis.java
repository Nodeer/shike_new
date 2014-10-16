package com.yshow.shike.utils;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
public class MD5_PhonDis {
	/**
	 * 单例对象实例 
	 */
	private static MD5_PhonDis insDis = new MD5_PhonDis();
	public static MD5_PhonDis getInstance() {
		return insDis;
	}
	private static final char HEX_DIGITS[] = { '0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
	private String toHexString(byte[] b) {
		// String to byte
		StringBuilder sb = new StringBuilder(b.length * 2);
		for (int i = 0; i < b.length; i++) {
			sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);
			sb.append(HEX_DIGITS[b[i] & 0x0f]);
		}
		return sb.toString();
	}
	public String md5(String s) {
		try {
			// Create MD5 Hash
			MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
			digest.update(s.getBytes());
			byte messageDigest[] = digest.digest();
			return toHexString(messageDigest);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return "";
	}
	/**
	 * 获取手机的唯一标识
	 * @return 
	 */
	public String Get_Ident(Context context) {
		String Sim_DEVICEId = null;
		// 获取SIM卡的设备
		TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
		Sim_DEVICEId = tm.getSimSerialNumber(); 
		Sim_DEVICEId = tm.getDeviceId();
		if(Sim_DEVICEId != null){
			return Sim_DEVICEId;
		}else {
			//获取手机设备的串号 
			return Sim_DEVICEId;
		}
	}
	/**
	 * 获取手机的唯一标识
	 * @return 
	 */
	public String getMacAdd(Context context) { 
        WifiManager wifi = (WifiManager)context.getSystemService(Context.WIFI_SERVICE); 
        WifiInfo info = wifi.getConnectionInfo(); 
        return info.getMacAddress(); 
   } 
}
