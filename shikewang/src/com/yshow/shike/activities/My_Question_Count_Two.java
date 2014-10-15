package com.yshow.shike.activities;
import java.util.ArrayList;

import android.widget.*;
import com.umeng.analytics.MobclickAgent;
import com.yshow.shike.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yshow.shike.entity.LoginManage;
import com.yshow.shike.entity.Question_Bank;
import com.yshow.shike.entity.SkMessage_Res;
import com.yshow.shike.entity.SkMessage_Voice;
import com.yshow.shike.utils.*;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;

/**
 * 从学生端题库点击某个题库,进入的题板页面
 */
public class My_Question_Count_Two extends Activity implements OnClickListener {
	private static boolean flag = true;
	private Context context;
	private Question_Bank picture_count;
	private DisplayImageOptions options;
	private ImageLoader imageLoader;
	private ViewPager count_viewpager;
	private MyPagerAdapter adapter;
	private ArrayList<SkMessage_Res> reslist;
	private Bitmap bitmap;
	private Boolean upload_flag = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_question_count_two);
		context = this;
		initData();
		options = Net_Servse.getInstence().Picture_Shipei(R.drawable.back);
		imageLoader = ImageLoader.getInstance();
	}
	private void initData() {
		picture_count = (Question_Bank) getIntent().getExtras().getSerializable("picture_count");
		if (picture_count != null) {
			reslist = picture_count.getRes();
		}
		findViewById(R.id.tv_count_back).setOnClickListener(this);
		TextView tv_count_delete = (TextView) findViewById(R.id.tv_count_delete);
		TextView comfig_buttom = (TextView) findViewById(R.id.tv_que_count_comfg);
		tv_count_delete.setOnClickListener(this);
		comfig_buttom.setOnClickListener(this);
		count_viewpager = (ViewPager) findViewById(R.id.two_count_viewpager);
		if(PartnerConfig.TEATHER_ID != null || PartnerConfig.SUBJECT_ID != null){
			findViewById(R.id.rl_count_relativeLayout).setVisibility(View.GONE);
			comfig_buttom.setVisibility(View.VISIBLE);
			flag = false;
		}
		adapter = new MyPagerAdapter(reslist);
		count_viewpager.setAdapter(adapter);
	}
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.tv_count_back:
			finish();
			break;
		case R.id.tv_count_delete:
			flag = !flag;
			int item = count_viewpager.getCurrentItem();
			count_viewpager.setAdapter(adapter);
			count_viewpager.setCurrentItem(item);
			break;
		case R.id.tv_que_count_comfg:
			if(upload_flag){
				String tea_id = PartnerConfig.TEATHER_ID;
				String sub_id = PartnerConfig.SUBJECT_ID;
				if(tea_id!= null && sub_id != null && bitmap != null){
					Send_Message send_Message = new Send_Message(context, bitmap,null, tea_id);
					send_Message.skCreateQuestion(sub_id);
				}
			}else {
				Toast.makeText(context,"请选择图片", 0).show();
			}
			break;
		}
	}
	/**
	 * 页面数据适配器
	 */
	class MyPagerAdapter extends PagerAdapter {
		ArrayList<SkMessage_Res> reslist;
        MediaPlayerUtil mediaPlayer;
		private ImageView student_delete;
		public MyPagerAdapter(ArrayList<SkMessage_Res> reslist) {
			this.reslist = reslist;
            mediaPlayer = new MediaPlayerUtil();
		}
		@Override
		public int getCount() {
			return reslist.size();
		}
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
		@Override
		public void destroyItem(ViewGroup container, int position, Object view) {
		}
		@Override
		public View instantiateItem(ViewGroup container, final int position) {
			final String file = reslist.get(position).getFile_tub();
			View view = View.inflate(context, R.layout.activity_message_three,null);
			final ImageView img_count = (ImageView) view.findViewById(R.id.iv_picture);
			GridView count_gridview = (GridView) view.findViewById(R.id.itme_gridview);
			student_delete = (ImageView) view.findViewById(R.id.iv_student_delete);
			final ImageView select_bac = (ImageView) view.findViewById(R.id.iv_select_bac);
			if (flag == true) {
				student_delete.setVisibility(View.GONE);
			} else {
				student_delete.setVisibility(View.VISIBLE);
				student_delete.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						String questionId = reslist.get(position).getQuestionId();
						Delete_Ques_Utile.getIntence().stu_Delete_File(context, questionId, reslist, position,adapter);
					}
				});
			}
			ArrayList<SkMessage_Voice> voice = reslist.get(position).getVoice();
			count_gridview.setAdapter(new GridviewAdapter(voice, context));
            count_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                    GridviewAdapter adapter = (GridviewAdapter) parent.getAdapter();
                    SkMessage_Voice item = adapter.getItem(position);
                    String file = item.getFile();
                    mediaPlayer.Down_Void(file,context);
                }
            });
			imageLoader.displayImage(file, img_count, options);
			img_count.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					upload_flag =! upload_flag;
					if(upload_flag){
						select_bac.setVisibility(View.VISIBLE);
					}else {
						select_bac.setVisibility(View.GONE);
					}
					Drawable drawable = img_count.getDrawable();
					BitmapDrawable bd = (BitmapDrawable) drawable;
					bitmap = bd.getBitmap();
				}
			});
			container.addView(view);
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
			View view = View.inflate(context, R.layout.ti_ku_itme, null);
			ImageView ti_ku_itme = (ImageView) view.findViewById(R.id.ti_ku_itme1);
			ImageView iv_stu_delete = (ImageView) view.findViewById(R.id.iv_stu_delete);
			if (flag) {
				iv_stu_delete.setVisibility(View.GONE);
			} else {
				iv_stu_delete.setVisibility(View.VISIBLE);
			}
			if (LoginManage.getInstance().isTeacher()) {
				ti_ku_itme.setBackgroundResource(R.drawable.record);
			} else {
				ti_ku_itme.setImageResource(R.drawable.teather_student);
				iv_stu_delete.setVisibility(View.GONE);
			}
			return view;
		}
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		PartnerConfig.TEATHER_ID = null;
		PartnerConfig.SUBJECT_ID = null;
		PartnerConfig.TEATHER_NAME = null;
		PartnerConfig.SUBJECT_NAME = null;
	}

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}