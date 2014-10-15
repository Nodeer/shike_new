package com.yshow.shike.utils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import android.graphics.Bitmap;
import android.util.Log;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;
import com.yshow.shike.entity.SKStudent;
import com.yshow.shike.entity.Update_User_Info;

/**
 * 联网工具
 */
public class SKAsyncApiController {
	private static String SHIKE_VALUE_API_SERVER_URL = "http://apitest.shikeke.com/";// 测试环境
//	private static String SHIKE_VALUE_API_SERVER_URL = "http://api.shikeke.com/";
	public static AsyncHttpClient client = new AsyncHttpClient();
	public static SyncHttpClient clientSync = new SyncHttpClient() {
		@Override
		public String onRequestFailed(Throwable arg0, String arg1) {
			// 这里定义如果失败就返�?1
			return "-1";
		}
	};

	private static void myPost(String url, RequestParams params, AsyncHttpResponseHandler handler) {
		LogUtil.i("post", url + "\n" + params);
		client.post(getAbsoluteUrl(url), params, handler);
	}

	private static void myGet(String url, RequestParams params, AsyncHttpResponseHandler handler) {
		LogUtil.i("get", url + "\n" + params);
		client.get(getAbsoluteUrl(url), params, handler);
	}

	private static String getAbsoluteUrl(String url) {
		return SHIKE_VALUE_API_SERVER_URL + url.trim();
	}

	// 登录
	public static void skLogin(String account, String password, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("account", account);
		params.put("password", password);
		myPost("?m=login&a=login&os=android", params, handler);
	}

	// 注册学生
	public static void skRegister(SKStudent stud, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("name", stud.getName());
		params.put("nickname", stud.getNickname());
		params.put("aId", stud.getaId());
		params.put("vcodeRes", stud.getVcodeRes());
		params.put("info", stud.getInfo());
		params.put("mob", stud.getMob());
		params.put("email", stud.getEmail());
		params.put("pwd", stud.getPwd());
		params.put("types", stud.getTypes());
		params.put("gradeId", stud.getGradeId());
		myPost("/?m=register", params, handler);
	}

	// 消息完成后 注册
	public static void Auto_Save_Info(SKStudent stud, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("name", stud.getName());
		params.put("nickname", stud.getNickname());
		params.put("aId", stud.getaId());
		params.put("vcodeRes", stud.getVcodeRes());
		params.put("info", stud.getInfo());
		params.put("mob", stud.getMob());
		params.put("email", stud.getEmail());
		params.put("pwd", stud.getPwd());
		params.put("types", stud.getTypes());
		params.put("gradeId", stud.getGradeId());
		myPost("?m=update_user_info", params, handler);
	}

	// 注册老师
	public static void skRegister_Teather(SKStudent stud, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("name", stud.getName());
		params.put("icon", stud.getIcon());
		params.put("nickname", stud.getNickname());
		params.put("aId", stud.getaId());
		params.put("vcodeRes", stud.getVcodeRes());
		params.put("info", stud.getInfo());
		params.put("Paper", stud.getPaper());
		params.put("mob", stud.getMob());
		params.put("email", stud.getEmail());
		params.put("pwd", stud.getPwd());
		params.put("types", stud.getTypes());
		params.put("subjectId", stud.getSubject());
		params.put("fromGradeId", stud.getFromGradeId());
		params.put("toGradeId", stud.getToGradeId());
		myPost("/?m=register", params, handler);
	}

	// 获取验证码
	public static void skGetVcode(String mob, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("mob", mob);
		myPost("?m=vcode", params, handler);
	}

	// 获取修改密码的验证码
	public static void getPassword(String mob, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("mob", mob);
		myPost("?m=get_password", params, handler);
	}

	// 获取年级
	public static void skGetGrade(AsyncHttpResponseHandler handler) {
		myGet("?m=grade", null, handler);
	}

	// 获取地区
	public static void skGetArea(AsyncHttpResponseHandler handler) {
		myGet("?m=area", null, handler);
	}

	// 获取学科
	public static void skGetSubjeck(AsyncHttpResponseHandler handler) {
		myGet("?m=subject", null, handler);
	}

	// 获取学科
	public static void skGetJieDuan(AsyncHttpResponseHandler handler) {
		myGet("?m=subject", null, handler);
	}

	// 获取消息
	public static void skGetMessage(int page, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("page", page + "");
		myGet("?m=get_messge", params, handler);
	}

	// 获取新消息
	public static void skGetNewMessage(AsyncHttpResponseHandler handler) {
		myGet("?m=new_messge", null, handler);
	}

	// 创建题目
	public static void skCreateQuestion(String subjectId, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("subjectId", subjectId);
		myGet("?m=question", params, handler);
	}

	// 获取学科
	public static void skGetSubject(AsyncHttpResponseHandler handler) {
		myGet("?m=subject ", null, handler);
	}

	// 获取我的老师
	public static void skGetMyTeacher(AsyncHttpResponseHandler handler) {
		myGet("?m=myteacher", null, handler);
	}

	// 上传图片
	public static void skUploadImage(String questionId, Bitmap bitmap, AsyncHttpResponseHandler handler) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int quity = 100;
		bitmap.compress(Bitmap.CompressFormat.JPEG, quity, baos);
		LogUtil.d("图片按90%压缩后原始大小:" + quity + ".尺寸" + baos.size());
		while (baos.size() > 1024 * 1000*2) {
			LogUtil.d("图片大于1M.当前压缩比例:" + quity + ".实际尺寸" + baos.size());
			quity -= 10;
			baos.reset();
			bitmap.compress(Bitmap.CompressFormat.JPEG, quity, baos);
		}
		InputStream isBm = new ByteArrayInputStream(baos.toByteArray());
		RequestParams params = new RequestParams();
		params.put("questionId", questionId);
		params.put("file", isBm, questionId + ".jpg");
		myPost("?m=do_question", params, handler);
	}

	// 上传MP3
	public static void skUploadMp3(String questionId, String imgid, String path, String posID, AsyncHttpResponseHandler handler) {
		try {
			FileInputStream fileInputStream = new FileInputStream(path);
			RequestParams params = new RequestParams();
			params.put("questionId", questionId);
			params.put("imgid", imgid);
			params.put("file", fileInputStream, questionId + "_" + posID + ".amr");
			myPost("?m=do_question", params, handler);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	// 发消
	public static void skSend_messge(String questionId, String teachUid, String confirm, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("questionId", questionId);
		params.put("teachUid", teachUid);
		params.put("confirm", confirm);
		myPost("?m=send_messge", params, handler);
	}

	// 明星老师
	public static void start_teather(String subjectId, String aId, int page, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("subjectId", subjectId);
		params.put("aId", aId);
		params.put("page", "" + page);
		myPost("?m=top_teacher&a=star", params, handler);
	}

	// 删除消息
	// 参数：messge_id
	public static void skDel_messge(String messge_id, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("messge_id", messge_id);
		myPost("?m=messge_id", params, handler);
	}

	// 消息重发
	// public static void skTry_messge(String questionId, String teachUid,
	// String confirm, AsyncHttpResponseHandler handler) {
	// RequestParams params = new RequestParams();
	// params.put(questionId, questionId);
	// params.put(teachUid, teachUid);
	// myPost("?m=try_messge", params, handler);
	// }
	public static void ChongFa(String questionId, String teachUid, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("questionId", questionId);
		params.put("teachUid", teachUid);
		myGet("?m=try_messge", params, handler);
	}

	// 撤销提问
	// 参数：questionId
	public static void skCancel_messge(String questionId, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("questionId", questionId);
		myPost("?m=cancel_messge", params, handler);
	}

	// 完成消息
	// 参数：questionId
	public static void skDonemessge(String questionId, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("questionId", questionId);
		myPost("?m=done_messge", params, handler);
	}

	// 关注老师
	public static void Attention_Taeather_Parse(String uid, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("uid", uid);
		myGet("?m=fans", params, handler);
	}

	// 我的老师
	public static void My_Taeather_Parse(AsyncHttpResponseHandler handler) {
		myGet("?m=myteacher", null, handler);
	}

	// 推荐老师
	public static void recommend_teather(String subjectId, String aId, int page, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("subjectId", subjectId);
		params.put("aId", aId);
		params.put("page", String.valueOf(page));
		myPost("?m=top_teacher&a=recommend", params, handler);
	}

	// 我的师课
	public static void User_Info(String uid, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("uid", uid);
		myGet("?m=user_info", params, handler);
	}

	// 我的师课2
	// public static void User_Info2(String uid, AsyncHttpResponseHandler handler) {
	// RequestParams params = new RequestParams();
	// params.put("uid", uid);
	// myPost("?m=user_info", params, handler);
	// }
	public void getBitmap(String urlStr, File file) {
		try {
			URL url = new URL(urlStr);
			HttpURLConnection urlCon = (HttpURLConnection) url.openConnection();
			urlCon.setRequestMethod("GET");
			urlCon.setDoInput(true);
			urlCon.connect();
			InputStream inputStream = urlCon.getInputStream();
			@SuppressWarnings("resource")
			FileOutputStream outputStream = new FileOutputStream(file);
			byte buffer[] = new byte[1024];
			int bufferLength = 0;
			while ((bufferLength = inputStream.read(buffer)) > 0) {
				outputStream.write(buffer, 0, bufferLength);
			}
			outputStream.flush();
		} catch (Exception e) {
			e.printStackTrace();
			if (file != null) {
				file.delete();
			}
		}
	}

	// 投诉老师
	public static void Complaint_Teather(String teacheld, String questionId, String contents, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("teacheld", teacheld);
		params.put("questionId", questionId);
		params.put("contents", contents);
		myPost("?m=complain", params, handler);
	}

	/**
	 * 感谢老师
	 * 
	 * @param teacheld
	 * @param questionId
	 * @param handler
	 */
	public static void thank_teacher(String teacheld, String questionId, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("teacheId", teacheld);
		params.put("questionId", questionId);
		params.put("action", "think");
		myPost("?m=acknowledge", params, handler);
	}

	// 老师端接收题�?
	public static void Teather_Reception(String questionId, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("questionId", questionId);
		myPost("?m=accept_task", params, handler);
	}

	// 感谢老师 获取表情
	public static void Think_Teather(AsyncHttpResponseHandler handler) {
		myPost("?m=face_list", null, handler);
	}

	// 感谢老师2
	public static void Think_Teather2(String questionId, String tea_uid, String points, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("questionId", questionId);
		params.put("teacheId", tea_uid);
		params.put("points", points);
		myPost("?m=gift2points", params, handler);
	}

	// 获取gif图片
	public static void Gain_Gif(AsyncHttpResponseHandler handler) {
		myPost("?m=gift_face_list", null, handler);
	}

	/**
	 * 给老师发送感谢表情
	 */
	public static void Send_Fase(String fileId, String questionId, AsyncHttpResponseHandler handler) {
		RequestParams param = new RequestParams();
		param.put("fileId", fileId);
		param.put("questionId", questionId);
		myPost("/?m=send_face", param, handler);
	}

	// 投诉老师
	public static void Complain_Teather(String questionId, String claim_uid, String contents, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("questionId", questionId);
		params.put("teacheId", claim_uid);
		params.put("contents", contents);
		myPost("?m=complain", params, handler);
	}

	// 给老师发表情
	public static void Give_Teather_picture(String fileId, String questionId, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("fileId", fileId);
		params.put("questionId", questionId);
		myPost("?m=send_face", params, handler);
	}

	// 确定该题已经结束
	public static void Topic_End(String questionId, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("questionId", questionId);
		myPost("?m=done_messge", params, handler);
	}

	// 获取题库数据
	public static void Gain_Data(AsyncHttpResponseHandler handler) {
		myGet("?m=lib", null, handler);
	}

	// 意见反馈
	public static void Feed_Back(String contents, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("contents", contents);
		myPost("?m=feedback", params, handler);
	}

	// 功能介绍
	public static void Function_Introduce(AsyncHttpResponseHandler handler) {
		myPost("/?m=content&a=introduce", null, handler);
	}

	// 老师的FAQ问答
	public static void Sof_Info(AsyncHttpResponseHandler handler) {
		myPost("/?m=soft_info", null, handler);
	}

	// 电话收缩老师
	public static void Searth_Teather_Mob(String mob, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("mob", mob);
		myPost("/?m=search&a=s", params, handler);
	}

	// 条件搜索
	public static void Searth_Teather_TiaoJian(String nickname, String school, String subjectId, String areaId, String sex,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("nickname", nickname);
		params.put("school", school);
		if (subjectId != null && !subjectId.equals("-1")) {
			params.put("subjectId", subjectId);
		}
		params.put("areaId", areaId);
		if (sex != null && !sex.equals("-1")) {
			params.put("sex", sex);
		}
		myPost("/?m=search&a=term-s", params, handler);
	}

	// 取消關注
	public static void Qu_Xiao_GuanZhu(String uid, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("uid", uid);
		myGet("?m=unfans", params, handler);
	}

	// 消息重發
	// public static void ChongFa(String questionId, String teachUid,
	// AsyncHttpResponseHandler handler) {
	// RequestParams params = new RequestParams();
	// params.put("questionId", questionId);
	// params.put("teachUid", teachUid);
	// myGet("?m=try_messge", params, handler);
	// }

	// 删除题库里边的题
	public static void Delete_Bank(String questionId, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("questionId", questionId);
		myGet("?m=del_lib_question", params, handler);
	}

	// 添加分类接口
	public static void Acquire_File(AsyncHttpResponseHandler handler) {
		myGet("?m=category", null, handler);
	}

	// 老師端上傳圖片和語音
	public static void Mess_Save(String cid, String name, String questionId, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("cid", cid);
		params.put("name", name);
		params.put("questionId", questionId);
		myGet("?m=edit_question", params, handler);
	}

	// 保存題庫
	public static void Save_Ques_Count(String cid, String questionId, String name, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("cid", cid);
		params.put("name", name);
		params.put("questionId", questionId);
		myGet("?m=category", params, handler);
	}

	// 保存題庫
	public static void Save_TiKu(String cid, String name, String questionId, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("cid", cid);
		params.put("name", name);
		params.put("questionId", questionId);
		myPost("?m=save2lib", params, handler);
	}

	// 查看题库数据
	public static void Look_TiKu(AsyncHttpResponseHandler handler) {
		myGet("/?m=lib", null, handler);
	}

	// 删除分类文件夹（1）
	public static void Delete_File(String cid, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("cid", cid);
		myPost("?m=category&a=del", params, handler);
	}

	// 創建文件夹
	public static void Creat_File(String fileName, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("name", fileName);
		myPost("?m=category&a=add", params, handler);
	}

	// 刪除每一道題裏面的资源
	public static void Delete_Topic(String resId, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("resId", resId);
		myGet("?m=del_lib_res", params, handler);
	}

	// 刪除二级文件夹
	public static void Delete_Topic_Resource(String questionId, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("questionId", questionId);
		myPost("?m=del_lib_question", params, handler);
	}

	// public static void Delete_Bank(String questionId,
	// AsyncHttpResponseHandler handler) {
	// RequestParams params = new RequestParams();
	// params.put("questionId", questionId);
	// myGet("?m=del_lib_question", params, handler);
	// }
	// 积分使用记录
	public static void duihuanRecord(AsyncHttpResponseHandler handler) {
		myGet("?m=points&types=use", null, handler);
	}

	// 老师收入记录
	public static void inComeRecord(AsyncHttpResponseHandler handler) {
		myGet("?m=points&types=cash", null, handler);
	}

	// 获取银行列表
	public static void getBankList(AsyncHttpResponseHandler handler) {
		myGet("?m=bank_list", null, handler);
	}

	// 获取礼品列表
	public static void getGiftList(String page, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("page", page);
		myPost("?m=gift", params, handler);
	}

	// 我的学生
	public static void getStu_Info(AsyncHttpResponseHandler handler) {
		myPost("?m=mystudent", null, handler);
	}

	// 学生端消息删除
	public static void Dele_Mess(String messge_id, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("messge_id", messge_id);
		myPost("?m=del_messge", params, handler);
	}

	/**
	 * 学生 laos更改信息
	 */
	public static void Save_Info(Update_User_Info user_info, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("name", user_info.getName());
		params.put("nickname", user_info.getNickname());
		params.put("aId", user_info.getaId());
		params.put("birthday", user_info.getBirthday());
		params.put("info", user_info.getInfo());
		params.put("school", user_info.getSchool());
		params.put("subjectId", user_info.getSubjectId());
		params.put("fromGradeId", user_info.getFromGradeId());
		params.put("toGradeId", user_info.getToGradeId());
		params.put("old_pwd", user_info.getOld_pwd());
		params.put("pwd", user_info.getPwd());
		params.put("types", user_info.getTypes());
		params.put("icon", user_info.getIcon());
		params.put("sex", user_info.getSex());
		params.put("Paper", user_info.getPaper());
		params.put("email", user_info.getEmail());
		params.put("gradeId", user_info.getGradeId());
		myPost("?m=update_user_info", params, handler);
	}

	// 老师端 我的时课
	public static void save_teask_icon(com.yshow.shike.entity.User_Info user_info, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("fromGradeId", user_info.getFromGradeId());
		params.put("toGradeId", user_info.getToGradeId());
		params.put("icon", user_info.getIcon());
		myPost("?m=update_user_info", params, handler);
	}

	// 重置密码
	public static void Reset_Password(String mob, String vcodeRes, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("mob", mob);
		params.put("vcodeRes", vcodeRes);
		myPost("/?m=change_pwd", params, handler);
	}

	// 自动注册登陆
	public static void AuTo_log(String mac, String ckey, String time, String key, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("mac", mac);
		params.put("ckey", ckey);
		params.put("t", time);
		params.put("key", key);
		myPost("/?m=auto_login", params, handler);
	}

	// 学生自动登录
	public static void auto_Info(String nickname, String gradeId, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("nickname", nickname);
		params.put("gradeId", gradeId);
		myPost("?m=update_user_info", params, handler);
	}

	// 退出登录
	public static void Back_Login(AsyncHttpResponseHandler handler) {
		myPost("/?a=logout", null, handler);
	}

	// 在线老师
	public static void OnLine_Tea(int page, String subjectId, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("page", String.valueOf(page));
		params.put("subjectId", subjectId);
		myPost("/?m=online_teacher", params, handler);
	}

	// 消息查看
	public static void Look_Mess(String questionId, AsyncHttpResponseHandler handler) {
		myPost("/?m=read_messge&questionId=" + questionId, null, handler);
	}

	// 上传教师职格证
	public static void Up_Loading_Tea(Bitmap bitmap, String path, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int quity = 100;
		bitmap.compress(Bitmap.CompressFormat.JPEG, quity, baos);
		LogUtil.d("图片原始大小:" + quity + ".尺寸" + baos.size());
		while (baos.size() > 1024 * 1000*2) {
			LogUtil.d("图片大于1M.当前压缩比例:" + quity + ".实际尺寸" + baos.size());
			quity -= 10;
			baos.reset();
			bitmap.compress(Bitmap.CompressFormat.JPEG, quity, baos);
		}
		InputStream isBm = new ByteArrayInputStream(baos.toByteArray());
		params.put("filename", isBm, path + ".jpg");
		myPost("/?m=file&a=upload", params, handler);
	}

	// 拒接学生发给老师的题目
	public static void record_Stu(String questionId, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("questionId", questionId);
		myPost("/?m=reject_task", params, handler);
	}

	// 支付宝支付
	public static void Get_Comm_Info(String itemid, AsyncHttpResponseHandler handler) {
		myPost("/alipay/?m=create&itemId=" + itemid + "&Platform=android", null, handler);
	}

	// 银联支付
	public static void Get_YinLian_Comm_Info(String itemid, AsyncHttpResponseHandler handler) {
		myPost("/pay.php/?m=purchase&itemId=" + itemid + "&Platform=android", null, handler);
	}

	// 更新账户中的银行卡信息
	public static void changeAccountInfo(String name, String bankId, String bankAddr, String bankNO, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("bankId", bankId);
		params.put("bankNO", bankNO);
		params.put("bankAddr", bankAddr);
		params.put("name", name);
		myPost("?m=bank", params, handler);
	}

	// 修改兑换商品的邮寄信息
	public static void changeMailAddress(String receiver, String phone, String addressee, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("receiver", receiver);
		params.put("phone", phone);
		params.put("addressee", addressee);
		myPost("?m=addressee", params, handler);
	}

	/** 兑换现金 */
	public static void exchangeJifen(String points, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("points", points);
		myPost("?m=exchange_money", params, handler);
	}

	/** 兑换商品 */
	public static void exchangeGoods(String giftId, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("giftId", giftId);
		myPost("?m=exchange_gift", params, handler);
	}
}