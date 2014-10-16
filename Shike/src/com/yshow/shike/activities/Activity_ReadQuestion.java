package com.yshow.shike.activities;
import java.util.ArrayList;
import java.util.Date;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.umeng.analytics.MobclickAgent;
import com.yshow.shike.R;
import com.yshow.shike.adapter.SKQuestsAdapter;
import com.yshow.shike.entity.LoginManage;
import com.yshow.shike.entity.SKMessage;
import com.yshow.shike.service.MySKService;
import com.yshow.shike.utils.DateUtils;
import com.yshow.shike.utils.Send_Message;

/**
 * 老师主页点击某条消息以后进入的页面,里面有学生在本次提问中提交的图片.如果有新的语音,会有小红点
 * 学生主页点击某条消息以后进入的页面,里面有学生在本次提问中提交的图片
 */
public class Activity_ReadQuestion extends Activity implements OnClickListener {
	private Context context;
	private GridView mGridView;
	private TextView questionDate;
	private LinearLayout rl_mess_two;
	private SKMessage sKMessage;
	private SKQuestsAdapter skQuestsAdapter;
	private String curquestionId;
	private Handler handler = new Handler() {
		@SuppressWarnings("unchecked")
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MySKService.HAVE_NEW_MESSAGE:
				ArrayList<SKMessage> newresolveMessage = (ArrayList<SKMessage>) msg.obj;
				setSKMNewessage(newresolveMessage);
				break;
			}
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_read_question);
		context = this;
		intview();
	}

	private void intview() {
		mGridView = (GridView) findViewById(R.id.gridview);
		questionDate = (TextView) findViewById(R.id.tv_time);
		rl_mess_two = (LinearLayout) findViewById(R.id.rl_mess_two);
		findViewById(R.id.ll_back).setOnClickListener(this);
		sKMessage = (SKMessage) getIntent().getSerializableExtra("SKMessage");
		boolean teacher = LoginManage.getInstance().isTeacher();
		if (teacher) {
			rl_mess_two.setBackgroundColor(getResources().getColor(R.color.bottom_widow_color));
		}
		curquestionId = sKMessage.getQuestionId();
		// 提问的次数
		long updateTime = Long.valueOf(sKMessage.getUpdateTime());
		questionDate.setText(sKMessage.getDate()+"  "+ DateUtils.formatDateH(new Date(Long.valueOf(updateTime) * 1000)));
		skQuestsAdapter = new SKQuestsAdapter(context, sKMessage);
		mGridView.setAdapter(skQuestsAdapter);
		mGridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int pointion, long arg3) {
				if (sKMessage.getMsgType().equals("0")&&sKMessage.getClaim_uid().equals("0") && !LoginManage.getInstance().isTeacher()) {
					AlertDialog.Builder dia = new Builder(context);
					dia.setTitle("提示");
					dia.setMessage("当前还没有老师解答该题是否重发？");
					dia.setPositiveButton("是",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,int which) {
									reSendMessage(sKMessage);
								}
							});
					dia.setNegativeButton("否",null);
					dia.show();
					return;
				}
				Intent intent = new Intent(context,Activity_Message_Three.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("sKMessage", sKMessage);
				bundle.putInt("pointion", pointion);
				intent.putExtras(bundle);
				context.startActivity(intent);
				finish();
			}
		});
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_back:
			finish();
			break;
		}
	}
	private void setSKMNewessage(ArrayList<SKMessage> list) {
		for (SKMessage skMessage : list) {
			String questionId = skMessage.getQuestionId();
			if (curquestionId.equals(questionId)) {
				skQuestsAdapter = new SKQuestsAdapter(context,skMessage);
				mGridView.setAdapter(skQuestsAdapter);
				curquestionId = questionId;
				new Send_Message(context).Look_Message(questionId);
				return;
			}
		}
	}
	// 重发消息
	private void reSendMessage(SKMessage message) {
		Intent intent = new Intent(this, Activity_My_Board2.class);
		intent.putExtra("try_messge", true);
		intent.putExtra("message", message);
		context.startActivity(intent);
		finish();
	}

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        MySKService.handler = handler;
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        MySKService.handler = null;
    }
}