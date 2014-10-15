package com.yshow.shike.activities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.graphics.Rect;
import com.umeng.analytics.MobclickAgent;
import com.yshow.shike.utils.*;
import org.json.JSONException;
import org.json.JSONObject;
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
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.yshow.shike.R;
import com.yshow.shike.db.DatabaseDao;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yshow.shike.entity.LoginManage;
import com.yshow.shike.entity.SKMessage;
import com.yshow.shike.entity.SKMessageList;
import com.yshow.shike.entity.SkMessage_Res;
import com.yshow.shike.entity.SkMessage_Voice;
import com.yshow.shike.fragments.Fragment_Message;
import com.yshow.shike.service.MySKService;

/**
 * 点击某个消息中的某张图片后进入的页面.里面有1张图片,N个语音
 */
public class Activity_Message_Three extends Activity implements OnClickListener {
	private RelativeLayout bottom_navigation;
	private ArrayList<SkMessage_Res> reslist;
	private List<String> bitmap_list = new ArrayList<String>(); // ViewPager显示的一个集合
	private DisplayImageOptions options;
	private ImageLoader imageLoader;
	private int pointion;
	private String curquestionId;
	private ViewPager viewPager;
	private MyAdapter myAdapter;
	private RelativeLayout ll_volume_control;
	private LinearLayout ti_ban_three;
	private Context context;
	private int recLen = 0;
	private Timer timer = null;
	private MediaRecorderUtil mediaRecorderUtil;
	private TextView tv_visits, tv_data;
	private RelativeLayout ll_bottom_navigation2;
	private SKMessage sKMessage;
	private RelativeLayout ll_bottom_JieShpouGuo;
	private String claim_uid;
	private LinearLayout tv_i_is_void;
	private LinearLayout tv_teather_jiazai;
	private TextView tv_teather_comfimg2;
	private TextView back_time, tea_end;
	// private String red_point_control = "show";
	// private ImageView TEA_YD2,TEA_YD3;
	private DatabaseDao databaseDao;
	private List<String> query_voidce; // 查询所有数据库的录音
	private MediaPlayerUtil mediaPlayer;

	private boolean isNeedShowEndDialog = true;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MySKService.HAVE_NEW_MESSAGE:
				Log.e("Activity_Message_Three", "得到通知消刷新列表");
				@SuppressWarnings("unchecked")
				ArrayList<SKMessage> newresolveMessage = (ArrayList<SKMessage>) msg.obj;
				setSKMNewessage(newresolveMessage);
				Teather_Decide();
				break;
			case 1:
				back_time.setText("录音剩余时间：" + recLen);
				if (recLen < 0) {
					mediaRecorderUtil.Give_Up_voide();
					timer.cancel();
					back_time.setText("录音剩余时间：" + 0);
					recLen = 1;
				}
			}
		}
	};

	private boolean setSKMNewessage(ArrayList<SKMessage> list) {
		for (SKMessage skMessage : list) {
			String questionId = skMessage.getQuestionId();
			if (curquestionId.equals(questionId)) {
				sKMessage = skMessage;
				claim_uid = sKMessage.getClaim_uid();
				String count = sKMessage.getResCount();
				if (!count.equals("")) {
					Integer valueOf = Integer.valueOf(count);
					int t = (valueOf + 9) / 10 * 10;
					if (t == 0) {// 这地方做一下兼容
						t = 10;
					}
					tv_visits.setText("提问次数：" + count + "/" + t);
				}
				Log.e("Activity_Message_Three", "得到通知消刷新");
				myAdapter = new MyAdapter(skMessage.getRes());
				viewPager.setAdapter(myAdapter);
				ArrayList<SkMessage_Res> res = skMessage.getRes();
				int size = res.size();
				int size2 = reslist.size();
				if (size > size2) {
					viewPager.setCurrentItem(0);
					reslist = res;
					curquestionId = questionId;
					return true;
				} else if (size == size2) {
					for (int j = 0; j < size2; j++) {
						SkMessage_Res skMessage_Res = res.get(j);
						SkMessage_Res skMessage_Res2 = reslist.get(j);
						ArrayList<SkMessage_Voice> voice2 = skMessage_Res.getVoice();
						ArrayList<SkMessage_Voice> voice3 = skMessage_Res2.getVoice();
						if (voice2.size() > voice3.size()) {
							viewPager.setCurrentItem(j);
							reslist = res;
							curquestionId = questionId;
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_message_three);
		context = this;
		mediaPlayer = new MediaPlayerUtil();
		databaseDao = new DatabaseDao(context);
		query_voidce = databaseDao.Query();
		iniData();
		mediaRecorderUtil = new MediaRecorderUtil(this);
		MySKService.handler = handler;
		if (!sKMessage.getMsgType().equals("1")) {
			Teather_Decide();
		}
	}

	@Override
	protected void onStop() {
		mediaPlayer.Stop_Play();
		super.onStop();
	}

	private void iniData() {
		// TEA_YD2 = (ImageView) findViewById(R.id.tea_yindao_2);
		// TEA_YD3 = (ImageView) findViewById(R.id.tea_yindao_3);
		Bundle bundle = getIntent().getExtras();
		sKMessage = (SKMessage) bundle.getSerializable("sKMessage");
		if (sKMessage.isDone()) {
			isNeedShowEndDialog = false;
		}
		curquestionId = sKMessage.getQuestionId();
		claim_uid = sKMessage.getClaim_uid();
		ll_volume_control = (RelativeLayout) findViewById(R.id.ll_volume_control);
		bottom_navigation = (RelativeLayout) findViewById(R.id.ll_bottom_navigation);
		LinearLayout stu_voice = (LinearLayout) findViewById(R.id.tv_stu_voice);
		TextView tv_tianjian = (TextView) findViewById(R.id.tv_tianjian);
		tea_end = (TextView) findViewById(R.id.tv_tea_end);
		findViewById(R.id.tv_teather_comfimg).setOnClickListener(this);
		if (sKMessage.isDone()) {
			bottom_navigation.setVisibility(View.GONE);
			tea_end.setVisibility(View.GONE);
		}
		TextView tv_xiaohongyu = (TextView) findViewById(R.id.tv_xiaohongyu);
		back_time = (TextView) findViewById(R.id.tv_back_time);
		ll_bottom_navigation2 = (RelativeLayout) findViewById(R.id.ll_bottom_navigation2);
		ll_bottom_JieShpouGuo = (RelativeLayout) findViewById(R.id.ll_bottom_JieShpouGuo);
		tv_i_is_void = (LinearLayout) findViewById(R.id.tv_i_is_void);
		tv_i_is_void.setOnClickListener(this);
		tv_teather_jiazai = (LinearLayout) findViewById(R.id.tv_teather_jiazai);
		tv_teather_jiazai.setOnClickListener(this);
		tv_teather_comfimg2 = (TextView) findViewById(R.id.tv_teather_comfimg);
		findViewById(R.id.tv_comfing2).setOnClickListener(this);
		findViewById(R.id.tv_give_up).setOnClickListener(this);
		tv_visits = (TextView) findViewById(R.id.tv_visits);
		tv_data = (TextView) findViewById(R.id.tv_data);
		tv_xiaohongyu.setText(sKMessage.getNickname());
		String count = sKMessage.getResCount();
		if (!count.equals("")) {
			Integer valueOf = Integer.valueOf(count);
			int t = (valueOf + 9) / 10 * 10;
			if (t == 0) {// 这地方做一下兼容
				t = 10;
			}
			tv_visits.setText("提问次数：" + count + "/" + t);
		}
		// 提问的次数
		long updateTime = Long.valueOf(sKMessage.getUpdateTime());
		tv_data.setText(sKMessage.getDate() + "  " + DateUtils.formatDateH(new Date(Long.valueOf(updateTime) * 1000)));
		stu_voice.setOnClickListener(this);
		tv_tianjian.setOnClickListener(this);
		tea_end.setOnClickListener(this);
		tv_i_is_void.setOnTouchListener(touchlistener);
		stu_voice.setOnTouchListener(touchlistener);
		viewPager = (ViewPager) findViewById(R.id.message_viewpager);
		pointion = bundle.getInt("pointion");
		reslist = sKMessage.getRes();
		options = Net_Servse.getInstence().Picture_Shipei(R.drawable.back);
		imageLoader = ImageLoader.getInstance();
		myAdapter = new MyAdapter(reslist);
		viewPager.setAdapter(myAdapter);
		viewPager.setOffscreenPageLimit(3);
		viewPager.setCurrentItem(pointion);
		ti_ban_three = (LinearLayout) findViewById(R.id.ti_ban_three);
		findViewById(R.id.tv_three_back).setOnClickListener(this);
		ti_ban_three.setOnClickListener(this);

		if (sKMessage.getMsgType().equals("1")) {// 这里表示是系统消息
			tea_end.setVisibility(View.GONE);
			bottom_navigation.setVisibility(View.GONE);
			ll_bottom_navigation2.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		// case R.id.tea_yindao_2:
		// TEA_YD2.setVisibility(View.GONE);
		// break;
		// case R.id.tea_yindao_3:
		// TEA_YD3.setVisibility(View.GONE);
		// break;
		case R.id.tv_three_back:
			finish();
			break;
		case R.id.tv_tianjian:// 学生端点击拍照提问
			intent = new Intent(this, Activity_Tea_Tool_Sele.class);
			intent.putExtra("isContinue", true);
			intent.putExtra("questionId", curquestionId);
			intent.putExtra("tea_add_tool", "1");
			startActivityForResult(intent, 1);
			// finish();
			break;
		// 老师端结束按钮
		case R.id.tv_tea_end:
			Thank_Teacher(Activity_Message_Three.this);
			break;
		// 接受学生端消息 按钮
		case R.id.tv_comfing2:
			Qiang_Da(this);
			break;
		case R.id.tv_give_up:
			Giew_Up(this);
			break;
		case R.id.tv_teather_jiazai:// 老师端点击拍照解答
			intent = new Intent(this, Activity_Tea_Tool_Sele.class);
			intent.putExtra("isContinue", true);
			intent.putExtra("questionId", curquestionId);
			startActivityForResult(intent, 1);
			// finish();
			break;
		case R.id.tv_teather_comfimg:
			Toast.makeText(Activity_Message_Three.this, "功能开发中,敬请期待", Toast.LENGTH_SHORT).show();
			// if (sKMessage.isDone()) {
			// Save_Dialog();
			// }else{
			//
			// }
			break;
		}
	}

	private boolean isRecordCancel = false;
	/**
	 * 开始录音
	 */
	private OnTouchListener touchlistener = new OnTouchListener() {
		private Long uptime;
		private Long downtime;
		private boolean start_boolear; // 对SD卡进行判断

		@Override
		public boolean onTouch(View view, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				downtime = System.currentTimeMillis();
				mediaRecorderUtil = new MediaRecorderUtil(Activity_Message_Three.this);
				start_boolear = mediaRecorderUtil.startRecorder();
				isRecordCancel = false;
				if (start_boolear) {
					Timer_Time();
					ll_volume_control.setVisibility(View.VISIBLE);
				}
				break;
			case MotionEvent.ACTION_UP:
				if (!isRecordCancel) {
					if (start_boolear) {
						if (timer != null) {
							timer.cancel();
						}
						uptime = System.currentTimeMillis();
						ll_volume_control.setVisibility(View.GONE);
						mediaRecorderUtil.stopRecorder();
						if (uptime - downtime < 1000) {
							Toast.makeText(Activity_Message_Three.this, "说话时间太短", Toast.LENGTH_SHORT).show();
						} else {
							int currentItem = viewPager.getCurrentItem();
							SkMessage_Res skMessage_Res = reslist.get(currentItem);
							String fileId = skMessage_Res.getId();
							skUploadMp3(curquestionId, fileId, mediaRecorderUtil.getFilePath(), skMessage_Res.getVoice().size() + 1 + "");
						}
					}
				}
				break;
			case MotionEvent.ACTION_MOVE:
				if (!isRecordCancel) {
					if (start_boolear) {
						if (!isRecordCancel) {
							Rect r = new Rect();
							view.getLocalVisibleRect(r);
							boolean b = r.contains((int) (event.getX()), (int) (event.getY()));
							if (!b) {
								Toast.makeText(Activity_Message_Three.this, "录音取消", Toast.LENGTH_SHORT).show();
								isRecordCancel = true;
								timer.cancel();
								ll_volume_control.setVisibility(View.GONE);
								mediaRecorderUtil.stopRecorder();
							}
						}
					}
				}
				break;
			}
			return false;
		}

		private void Timer_Time() {
			recLen = 30;
			timer = new Timer();
			TimerTask task = new TimerTask() {
				@Override
				public void run() {
					recLen--;
					Message message = new Message();
					message.what = 1;
					handler.sendMessage(message);
				}
			};
			timer.schedule(task, 30, 1000);
		}
	};

	/**
	 * 页面数据适配器
	 */
	public class MyAdapter extends PagerAdapter {
		ArrayList<SkMessage_Res> reslist;

		public MyAdapter(ArrayList<SkMessage_Res> reslist) {
			this.reslist = reslist;
		}

		public int getCount() {
			return reslist.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(View container, int position, Object object) {
		}

		/**
		 * 载入图片进去，用当前的position 除以 图片数组长度取余数是关键
		 */
		@Override
		public Object instantiateItem(ViewGroup container, final int position) {
			final SkMessage_Res skMessage_Res = reslist.get(position);
			final String files = skMessage_Res.getFile_tub();
			View view = View.inflate(Activity_Message_Three.this, R.layout.activity_message_three, null);
			ImageView iv_picture = (ImageView) view.findViewById(R.id.iv_picture);
			iv_picture.setTag(files);
			GridView itme_gridview = (GridView) view.findViewById(R.id.itme_gridview);
			FrameLayout iv_picture1 = (FrameLayout) view.findViewById(R.id.iv_picture1);
			if (LoginManage.getInstance().isTeacher()) {
				iv_picture1.setBackgroundResource(R.drawable.teather_bg_message_image);
			}
			// 这个是用来显示数据的一个集合
			ArrayList<SkMessage_Voice> voice = reslist.get(position).getVoice();
			for (int voice_count = 0; voice_count < voice.size(); voice_count++) {
				itme_gridview.setSelection(voice.size());
			}
			itme_gridview.setAdapter(new GridviewAdapter(voice, Activity_Message_Three.this));
			// 添加是否读过语音的标识
			itme_gridview.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					GridviewAdapter adapter = (GridviewAdapter) parent.getAdapter();
					SkMessage_Voice item = adapter.getItem(position);
					String file = item.getFile();
					// 对语音进行添加和判断判断
					if (!query_voidce.contains(file)) {
						databaseDao.insert(file);
					}
					adapter.notifyDataSetChanged();
					// 通知上一个界面刷新页面
					mediaPlayer.Down_Void(file, context);
				}
			});
			iv_picture.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					String file = skMessage_Res.getFile();
					Bundle bundle = new Bundle();
					bundle.putString("Message_Three", file);
					bundle.putSerializable("res", sKMessage.getRes().get(position));
                    bundle.putBoolean("isdone",sKMessage.isDone());
                    Intent intent = new Intent(context, ImageActivity.class);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 1);
				}
			});
			imageLoader.displayImage(files, iv_picture, options);
			container.addView(view);
			bitmap_list.add(files);
			return view;
		}
	}

	class GridviewAdapter extends BaseAdapter {
		private ArrayList<SkMessage_Voice> voice;
		private Context context;

		public GridviewAdapter(ArrayList<SkMessage_Voice> voice, Context context) {
			super();
			this.context = context;
			this.voice = voice;
		}

		@Override
		public int getCount() {
			return voice.size();
		}

		@Override
		public SkMessage_Voice getItem(int position) {
			return voice.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			SkMessage_Voice skMessage_Voice = voice.get(position);
			String isStudent = skMessage_Voice.getIsStudent();
			String uid = skMessage_Voice.getUid();
			String uid2 = LoginManage.getInstance().getStudent().getUid();
			View view = View.inflate(context, R.layout.mess_three_item, null);
			ImageView voidce_image = (ImageView) view.findViewById(R.id.mess_voidce_make);
			ImageView red_point = (ImageView) view.findViewById(R.id.iv_red_point);
			List<String> query = databaseDao.Query();
			if (isStudent.equals("1")) { // 显示学生录音标示
				voidce_image.setBackgroundResource(R.drawable.teather_student);
				if (uid.equals(uid2)) {
					red_point.setVisibility(View.GONE);
				} else {
					if (query.contains(skMessage_Voice.getFile())) {
						red_point.setVisibility(View.GONE);
					} else {
						red_point.setVisibility(View.VISIBLE);
					}
				}
			} else {// 显示老师录音标示
				voidce_image.setBackgroundResource(R.drawable.teather_void);
				if (uid.equals(uid2)) {
					red_point.setVisibility(View.GONE);
				} else {
					if (query.contains(skMessage_Voice.getFile())) {
						red_point.setVisibility(View.GONE);
					} else {
						red_point.setVisibility(View.VISIBLE);
					}
				}
			}
			return view;
		}
	}

	private void skUploadMp3(final String questionId, final String imgid, String mapPath, String posID) {
		SKAsyncApiController.skUploadMp3(questionId, imgid, mapPath, posID, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String json) {
				super.onSuccess(json);
				Log.e("skUploadeImage", json);
				boolean resolveIsSuccess = SKResolveJsonUtil.getInstance().resolveIsSuccess(json, Activity_Message_Three.this);
				if (resolveIsSuccess) {
					skSend_messge(questionId, "0");
				}
			}
		});
	}

	public void Thank_Teacher(final Context context) {
		Builder builder = new Builder(context);
		builder.setTitle("     提示");
		builder.setMessage("确定该题已经解决了吗？");
		builder.setPositiveButton("确定", new android.content.DialogInterface.OnClickListener() {
			public void onClick(DialogInterface arg0, int arg1) {
				SKAsyncApiController.Topic_End(curquestionId, new MyAsyncHttpResponseHandler(context, true) {
					@Override
					public void onSuccess(String json) {
						super.onSuccess(json);
						boolean isSuccess = SKResolveJsonUtil.getInstance().resolveIsSuccess(json, context);
						if (isSuccess) {
							Fragment_Message.handler.sendEmptyMessage(MySKService.HAVE_NEW_MESSAGE);
							Toast.makeText(context, "確定結束消息", Toast.LENGTH_SHORT).show();
							Bundle bundle = new Bundle();
							bundle.putSerializable("message", sKMessage);
							Dialog.intent(context, Activity_Thank_Teacher.class, bundle);
							finish();
						} else {
							Toast.makeText(context, "確定結束消息失败", Toast.LENGTH_SHORT).show();
						}
					}
				});
			}
		});
		builder.setNegativeButton("取消", null);
		try {
			builder.show();
		} catch (Exception e) {
		}
	}

	private void skSend_messge(final String questionId, String isSend) {
		Log.e("questionId", questionId);
		SKAsyncApiController.skSend_messge(questionId, "", isSend, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                Log.e("skSend_messge", content);
                boolean resolveIsSuccess = SKResolveJsonUtil.getInstance().resolveIsSuccess(content, Activity_Message_Three.this);
                if (resolveIsSuccess) {
                    try {
                        JSONObject jsonObject = new JSONObject(content);
                        String data = jsonObject.optString("data");
                        String type = jsonObject.optString("type");
                        if (type.equals("confirm")) {
                            AlertDialog.Builder dia = new Builder(Activity_Message_Three.this);
                            dia.setMessage(data);
                            dia.setNegativeButton("取消", null);
                            dia.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    skSend_messge(questionId, "1");
                                }
                            });
                            dia.show();
                        } else {
                            Toast.makeText(Activity_Message_Three.this, "发送成功", Toast.LENGTH_SHORT).show();
                            getSKMessage();
                            Fragment_Message.handler.sendEmptyMessage(MySKService.HAVE_NEW_MESSAGE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
	}

	/**
	 * 获取消息列表
	 */
	private void getSKMessage() {
		SKAsyncApiController.skGetMessage(1, new MyAsyncHttpResponseHandler(this, true) {
			@Override
			public void onSuccess(String json) {
				super.onSuccess(json);
				boolean isSuccess = SKResolveJsonUtil.getInstance().resolveIsSuccess(json);
				if (isSuccess) {
					ArrayList<SKMessageList> resolveMessage = SKResolveJsonUtil.getInstance().resolveMessage(json);
					for (SKMessageList skMessageList2 : resolveMessage) {
						ArrayList<SKMessage> list = skMessageList2.getList();
						boolean setSKMNewessage = setSKMNewessage(list);
						if (setSKMNewessage) {
							return;
						}
					}
				}
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            if (requestCode == 1) {
                getSKMessage();
            }
        }
    }

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus) {
			if (!sKMessage.getMsgType().equals("1")) {
				if (claim_uid.equals("0")) {
					HelpUtil.showHelp(this, HelpUtil.HELP_TEA_2, null);
				}
			}
		}
	}

	// 没接收过该题，显示返回，接收，放弃
	private void Teather_Decide() {
		boolean teacher = LoginManage.getInstance().isTeacher();
		if (teacher) {
			tv_data.setTextColor(getResources().getColor(R.color.bottom_widow_color));
			ti_ban_three.setBackgroundResource(R.color.bottom_widow_color);
			tea_end.setVisibility(View.GONE);
			tv_visits.setVisibility(View.GONE);
			if (claim_uid.equals("0")) {
				// 没接收过该题，显示返回，接收，放弃 claim_uid=0
				// YD.getInstence().setTea_YD_Tool(this,context, PartnerConfig.TEA_YD_TOOL,TEA_YD2, findViewById(R.id.tea_yindao2));
				ll_bottom_navigation2.setVisibility(View.VISIBLE);
				bottom_navigation.setVisibility(View.GONE);

			} else if (!claim_uid.equals("0")) {
				ll_bottom_navigation2.setVisibility(View.GONE);
				bottom_navigation.setVisibility(View.GONE);
				ll_bottom_JieShpouGuo.setVisibility(View.VISIBLE);
				// 如果题目完成 语音隐藏 添加 显示 确定
				if (sKMessage.isDone()) {
					tv_i_is_void.setVisibility(View.GONE);
					tv_teather_jiazai.setVisibility(View.GONE);
					tv_teather_comfimg2.setVisibility(View.VISIBLE);
					tv_teather_comfimg2.setText("存入题库");
					if (isNeedShowEndDialog) {
						Builder builder = new Builder(context);
						builder.setMessage("当前提问已结束");
						builder.setNegativeButton("确定", null);
						builder.show();
						isNeedShowEndDialog = false;
					}
					Net_Servse.getInstence().setTextImage_Replace(context, tv_teather_comfimg2);
				}
			}
		}
	}

	public void Qiang_Da(Context context) {
		Builder builder = new Builder(context);
		builder.setMessage("你是否要开始回答学生问题");
		builder.setPositiveButton("是", new android.content.DialogInterface.OnClickListener() {
			public void onClick(DialogInterface arg0, int arg1) {
				Teather_QuestionId();
			}
		});
		builder.setNegativeButton("否", null);
		builder.show();
	}

	private void Teather_QuestionId() {
		String questionId = sKMessage.getQuestionId();
		SKAsyncApiController.Teather_Reception(questionId, new MyAsyncHttpResponseHandler(this, true) {
			@Override
			public void onSuccess(int arg0, String arg1) {
				super.onSuccess(arg0, arg1);
				boolean success = SKResolveJsonUtil.getInstance().resolveIsSuccess(arg1, context);
				if (success) {
					ll_bottom_navigation2.setVisibility(View.GONE);
					bottom_navigation.setVisibility(View.GONE);
					ll_bottom_JieShpouGuo.setVisibility(View.VISIBLE);
					Fragment_Message.handler.sendEmptyMessage(MySKService.HAVE_NEW_MESSAGE);
					// YD.getInstence().setTea_YD_Tool(Activity_Message_Three.this,context,PartnerConfig.TEA_YD_TOOL, TEA_YD3,
					// findViewById(R.id.tea_yindao2));
					YD.getInstence().getYD(context, PartnerConfig.TEA_YD_TOOL, true);
					Toast.makeText(context, "接收成功", Toast.LENGTH_SHORT).show();
					HelpUtil.showHelp(Activity_Message_Three.this, HelpUtil.HELP_TEA_3, null);
				}
			}
		});
	}

	public void Save_Dialog() {
		Builder builder = new Builder(context);
		builder.setTitle("提示");
		builder.setMessage("该题已交互结束，需要把该题存入题库吗？");
		builder.setPositiveButton("保存", new android.content.DialogInterface.OnClickListener() {
			public void onClick(DialogInterface arg0, int arg1) {
				Bundle bundle = new Bundle();
				bundle.putSerializable("sKMessage", sKMessage);
				Dialog.intent(context, Activity_Que_board2.class, bundle);
			}
		});
		builder.setNegativeButton("取消", null);
		builder.show();
	}

	// 保存題庫
	private void Giew_Up(Context context) {
		Builder builder = new Builder(context);
		builder.setMessage("你是否要放弃回答学生问题");
		builder.setPositiveButton("是", new android.content.DialogInterface.OnClickListener() {
			public void onClick(DialogInterface arg0, int arg1) {
				Record_Stu(sKMessage.getQuestionId());
				Fragment_Message.handler.sendEmptyMessage(MySKService.HAVE_NEW_MESSAGE);
				finish();
			}
		});
		builder.setNegativeButton("否", null);
		builder.show();
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		if (!sKMessage.getMsgType().equals("1")) {
			Teather_Decide();
		}
	}

	private void Record_Stu(String quesid) {
		SKAsyncApiController.record_Stu(quesid, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String json) {
				super.onSuccess(json);
				SKResolveJsonUtil.getInstance().resolveIsSuccess(json, context);
				Toast.makeText(context, "放弃成功", Toast.LENGTH_SHORT).show();
			}
		});
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

}