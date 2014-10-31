package com.yshow.shike.utils;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.os.Handler;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.yshow.shike.UIApplication;
import com.yshow.shike.activities.Student_Main_Activity;
import com.yshow.shike.entity.SkUpLoadQuestion;
public class Send_Message {
	private Context context;
	private Bitmap bitmap;
	private SkUpLoadQuestion skUpLoadQuestion;
	private String teacherId = "-1";
	private ProgressDialogUtil progress;
	public Send_Message(Context context) {
		super();
		this.context = context;
	}
	public Send_Message(Context context, Bitmap bitmap,
			SkUpLoadQuestion skUpLoadQuestion, String teacherId) {
		super();
		this.context = context;
		this.bitmap = bitmap;
		this.skUpLoadQuestion = skUpLoadQuestion;
		this.teacherId = teacherId;
		progress = new ProgressDialogUtil(context);
		progress.show();
	}
	// 获取问题ID
	public void skCreateQuestion(String subjectid) {
		SKAsyncApiController.skCreateQuestion(subjectid,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(String json) {
						super.onSuccess(json);
                        Log.i("result","返回:"+json);
						boolean success = SKResolveJsonUtil.getInstance().resolveIsSuccess(json, context);
						if(success){
							try {
								JSONObject jsonObject = new JSONObject(json);
								jsonObject = jsonObject.getJSONObject("data");
								String questionId = jsonObject.optString("questionId");
								skUploadeImage(questionId);
							} catch (JSONException e) {
								e.printStackTrace();
							}
						  }
						}
				});
	}

	// 上传图片
	private void skUploadeImage(final String questionId) {
		SKAsyncApiController.skUploadImage(questionId, bitmap,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(String arg0) {
						super.onSuccess(arg0);
                        Log.i("result", "返回:" + arg0);
						String error = null;
						try {
							JSONObject jsonObject = new JSONObject(arg0);
							int optBoolean = jsonObject.optInt("success");
							if (optBoolean == 1) {
								jsonObject = jsonObject.getJSONObject("data");
								String imgid = jsonObject.optString("resid");
								if (skUpLoadQuestion != null) {
									List<String> urllist = skUpLoadQuestion.getUrllist();
									int size = urllist.size();
									for (int i = 0; i < size; i++) {
										skUploadMp3(questionId, imgid,urllist.get(i), i + "");
										Toast.makeText(context, "发送音频" +(i+1),Toast.LENGTH_SHORT).show();
									}
								}
								skSend_messge(questionId, teacherId, "0");
							} else {
								error = jsonObject.optString("error");
								Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
							}
						} catch (JSONException e) {
							Toast.makeText(context, "服务异常", Toast.LENGTH_SHORT).show();
						}
					}

                    @Override
                    public void onFailure(Throwable throwable, String s) {
                        super.onFailure(throwable, s);
                    }
                });
	}

	// 发送音频
	private void skUploadMp3(final String questionId, final String imgid,
			String mapPath, String posID) {
		Log.e("", imgid);
		SKAsyncApiController.skUploadMp3(questionId, imgid, mapPath, posID,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(String arg0) {
						super.onSuccess(arg0);
                        Log.i("result", "返回:" + arg0);
						String error = null;
						try {
							JSONObject jsonObject = new JSONObject(arg0);
							int optBoolean = jsonObject.optInt("success");
							if (optBoolean == 1) {
							} else {
								error = jsonObject.optString("error");
								Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
							}
						} catch (JSONException e) {
							Toast.makeText(context, "服务异常", Toast.LENGTH_SHORT).show();
						}
					}
				});
	}
	// 发送消息
	private void skSend_messge(final String questionId, final String teachUid,
			String isSend) {
		SKAsyncApiController.skSend_messge(questionId, teachUid, isSend,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(String content) {
						super.onSuccess(content);
                        Log.i("result", "返回:" + content);
						progress.dismiss();
						String error = null;
						try {
							JSONObject jsonObject = new JSONObject(content);
							int optBoolean = jsonObject.optInt("success");
							if (optBoolean == 1) {
								String data = jsonObject.optString("data");
								String type = jsonObject.optString("type");
								if (type.equals("confirm")) {
									AlertDialog.Builder dia = new Builder(context);
									dia.setTitle("提示");
									dia.setMessage(data);
									dia.setNegativeButton("取消", new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface arg0, int arg1) {
											progress.dismiss();
										}
									});
									dia.setPositiveButton("确定",new DialogInterface.OnClickListener() {
												@Override
												public void onClick(DialogInterface dialog,
														int which) {
													skSend_messge(questionId,teachUid, "1");
												}
											});
									dia.show();
								} else {
									Toast.makeText(context, "发送成功", Toast.LENGTH_SHORT).show();
									Back_Main_Page();
									progress.dismiss();
								}
							} else {
								error = jsonObject.optString("error");
								Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
							}
						} catch (JSONException e) {
							Toast.makeText(context, "服务异常", Toast.LENGTH_SHORT).show();
						}
					}
				});
	}

	/**
	 * 回到消息主页面
	 */
	public void Back_Main_Page() {
        final AlertDialog dia = Dialog.showSimpleDialog(context,"题目已发送,老师正在接题,\n请稍候......");
        Timer timeer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                new Handler(context.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        dia.dismiss();
                        PartnerConfig.TEATHER_ID = null;
                        PartnerConfig.TEATHER_NAME = null;
                        PartnerConfig.SUBJECT_ID = null;
                        PartnerConfig.SUBJECT_NAME = null;
                        UIApplication.getInstance().getPicUrls().clear();
                        Dialog.Intent(context, Student_Main_Activity.class);
//                        Student_Main_Activity.mInstance.back("dd");
                        ((Activity) context).finish();
                    }
                });
            }
        };
        timeer.schedule(task,3000);
	}

	public void Back_Main_Page12() {
		Bundle bundle = new Bundle();
		bundle.putString("fasong", "1");
		Dialog.intent(context, Student_Main_Activity.class, bundle);
		((Activity) context).finish();
	}

	/**
	 * 查看消息界面
	 */
	public void Look_Message(String questionId) {
		SKAsyncApiController.Look_Mess(questionId,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(String json) {
						super.onSuccess(json);
					}
				});
	}
}
