package com.yshow.shike.activities;

import java.util.ArrayList;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;
import com.umeng.analytics.MobclickAgent;
import com.yshow.shike.R;
import com.yshow.shike.adapter.SKQuestsAdapter;
import com.yshow.shike.entity.SKMessage;
import com.yshow.shike.service.MySKService;

/**
 * 这个页面没用到
 */
public class Activity_Teather_ReadQuestion extends Activity implements OnClickListener {
	Context context;
	private GridView mGridView;
	private TextView questionDate;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			int what = msg.what;
			if (what == MySKService.HAVE_NEW_MESSAGE) {
				Log.e("Activity_ReadQuestion", "得到通知消刷新列表");
				@SuppressWarnings("unchecked")
				ArrayList<SKMessage> newresolveMessage = (ArrayList<SKMessage>) msg.obj;
				setSKMNewessage(newresolveMessage);
			}
		};
	};
	private SKMessage sKMessage;
	private SKQuestsAdapter skQuestsAdapter;
	private String curquestionId;

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
		findViewById(R.id.ll_back).setOnClickListener(this);
		sKMessage = (SKMessage) getIntent().getSerializableExtra("SKMessage");
		curquestionId = sKMessage.getQuestionId();
		questionDate.setText(sKMessage.getDate());
		// skQuestsAdapter = new SKQuestsAdapter(context, sKMessage.getRes());
		skQuestsAdapter = new SKQuestsAdapter(context, sKMessage);
		mGridView.setAdapter(skQuestsAdapter);
		mGridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pointion, long arg3) {
				Intent intent = new Intent(context, Activity_Message_Three.class);
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
				// skQuestsAdapter = new SKQuestsAdapter(context,skMessage.getRes());
				skQuestsAdapter = new SKQuestsAdapter(context, skMessage);
				mGridView.setAdapter(skQuestsAdapter);
				curquestionId = questionId;
				return;
			}
		}
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
