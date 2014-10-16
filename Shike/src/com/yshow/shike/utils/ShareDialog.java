package com.yshow.shike.utils;
import java.util.ArrayList;
import com.yshow.shike.R;
import com.yshow.shike.entity.Creat_File;
import com.yshow.shike.entity.Question_Bank;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.GridView;
public class ShareDialog extends Dialog {
	private Window window = null;
//	private Context context;
	public ShareDialog(Context context) {
		super(context);
//		this.context = context;
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
	}
	public void showDialog(View view, int id, int x, int y) {
		setContentView(view);
		windowDeploy(id, x, y);
		// 设置触摸对话框意外的地方取消对话框
		show();
	}
	public void finishDialog(int id, int x, int y) {
		windowDeploy(id, x, y);
		setCanceledOnTouchOutside(true);
		// dismiss();
	}
	// 设置窗口显示
	public void windowDeploy(int id, int x, int y) {
		window = getWindow(); // 得到对话框
		if (id == 1) {
			window.setWindowAnimations(R.style.shareFriendAnimationin); // 设置窗口弹出动画
		} else {
			window.setWindowAnimations(R.style.shareFriendAnimationout);// 设置窗口结束动画
		}
		// //设置对话框背景为透明
		WindowManager.LayoutParams wl = window.getAttributes();
		// 根据x，y坐标设置窗口需要显示的位置
		wl.x = x; // x小于0左移，大于0右移
		wl.y = y; // y小于0上移，大于0下移
		window.setAttributes(wl);
	}
	/**
	 * 分享时的应用程序跳转
	 * @param packageName
	 * @param context
	 */
	public static void openCLD(String emailBody, Context context) {
		  Intent email =  new  Intent(android.content.Intent.ACTION_SEND);   
		   email.setType( "plain/text" );   
		  String  emailSubject =  "共享软件" ;   
		  //设置邮件默认标题   
		  email.putExtra(android.content.Intent.EXTRA_SUBJECT, emailSubject);  
		  //设置要默认发送的内容   
		  email.putExtra(android.content.Intent.EXTRA_TEXT, emailBody);  
		  //调用系统的邮件系统   
		  ((Activity)context).startActivityForResult(Intent.createChooser(email,  "请选择邮件发送软件" ), 1001 );  
}
    /**
     * 短信分享
     */
    public static void sendSMS(Context context,String content){  
 		Uri smsToUri = Uri.parse("smsto:");  
 		Intent sendIntent = new Intent(Intent.ACTION_VIEW, smsToUri);  
 		sendIntent.putExtra("sms_body",content);  
 		sendIntent.setType("vnd.android-dir/mms-sms");  
 		((Activity)context).startActivityForResult(sendIntent,1002);  
 	}
	// 以文件的形式添加一個文件夾
	public static <T> void creat_new_file(String fileName,
			final BaseAdapter adapter, final GridView gr_file_count,
			final ArrayList<T> files, final Context context) {
			SKAsyncApiController.Creat_File(fileName,
					new MyAsyncHttpResponseHandler(context, true) {
						@Override
						public void onSuccess(String json) {
							super.onSuccess(json);
							boolean success = SKResolveJsonUtil.getInstance().resolveIsSuccess(json, context);
							if (success) {
								Creat_File pase_Creat_File = SKResolveJsonUtil.getInstance().Pase_Creat_File(json);
								ArrayList<Question_Bank> quesList = new ArrayList<Question_Bank>();
								Question_Bank question_Bank = new Question_Bank();
								question_Bank.setCategory1(pase_Creat_File.getName());
								question_Bank.setCid(pase_Creat_File.getUid());
								quesList.add(question_Bank);
								files.add((T) quesList);
								gr_file_count.setAdapter(adapter);
								adapter.notifyDataSetChanged();
							}
						}
					});
		}
}
