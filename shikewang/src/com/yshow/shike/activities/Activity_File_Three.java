package com.yshow.shike.activities;

import java.util.ArrayList;
import java.util.List;

import android.widget.AdapterView;
import com.umeng.analytics.MobclickAgent;
import com.yshow.shike.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yshow.shike.UIApplication;
import com.yshow.shike.entity.LoginManage;
import com.yshow.shike.entity.Question_Bank;
import com.yshow.shike.entity.SkMessage_Res;
import com.yshow.shike.entity.SkMessage_Voice;
import com.yshow.shike.utils.Delete_Ques_Utile;
import com.yshow.shike.utils.Dialog;
import com.yshow.shike.utils.MediaPlayerUtil;
import com.yshow.shike.utils.Net_Servse;
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
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * 按文件夹浏览题库时,进入单个题库
 */
public class Activity_File_Three extends Activity {
	private boolean flag = false;
	private String questionId;
	private ViewPager vp_file_three;
	private Context context;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	private Question_Bank bank;
	private MediaPlayerUtil mediaPlayer;

	public static Question_Bank mSaveTiku;
	@SuppressWarnings("rawtypes")
	private MyViewPager myViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_file_three);
		context = this;
		options = Net_Servse.getInstence().Picture_Shipei(R.drawable.xiaoxi_moren);
		imageLoader = ImageLoader.getInstance();
		initData();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void initData() {
		vp_file_three = (ViewPager) findViewById(R.id.vp_file_three);
		findViewById(R.id.tv_file__back).setOnClickListener(listener);
		findViewById(R.id.tv_file_add).setOnClickListener(listener);
		findViewById(R.id.tv_file_dele).setOnClickListener(listener);
		findViewById(R.id.tv_file_comf).setOnClickListener(listener);
		Bundle bundle = getIntent().getExtras();
		// String time_mark = bundle.getString("time_mark");
		bank = (Question_Bank) bundle.getSerializable("Question_Bank");
		mSaveTiku = bank;
		ArrayList<SkMessage_Res> TiMulist = bank.getRes();
		if (TiMulist != null) {
			myViewPager = new MyViewPager(TiMulist);
			vp_file_three.setAdapter(myViewPager);
			vp_file_three.setCurrentItem(TiMulist.size() - 1);
		}
		mediaPlayer = new MediaPlayerUtil();
	}

	private OnClickListener listener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.tv_file__back:
				finish();
				break;
			case R.id.tv_file_dele:
				flag = !flag;
				myViewPager.notifyDataSetChanged();
				break;
			case R.id.tv_file_add:// ��ط�������.����ֱ�Ӿ���ת������ҳ��.����һƪ�հ�
				UIApplication.getInstance().cleanPicUrls();
				UIApplication.getInstance().cleanbitmaplist();
				Dialog.Intent(context, Activity_Tea_Tool_Sele.class);
				// Dialog.Intent(context, Stu_Paint_Activity.class);
				break;
			case R.id.tv_file_comf:
				int currentItem = vp_file_three.getCurrentItem();
				View view = myViewPager.instantiateItem(vp_file_three, currentItem);
				ImageView image = (ImageView) view.findViewById(R.id.iv_three_item);
				Drawable drawable = image.getDrawable();
				BitmapDrawable bd = (BitmapDrawable) drawable;
				Bitmap bitmap = bd.getBitmap();
				Bitmap compress_bitmap = Dialog.scaleImg(bitmap, 340, 340);
				Bundle bundle = new Bundle();
				bundle.putSerializable("Question_Bank", bank);
				bundle.putParcelable("bitmap", compress_bitmap);
				bundle.putString("questionId", questionId);
				Dialog.intent(context, Activity_File_four.class, bundle);
				break;
			}
		}
	};

	class MyViewPager<T> extends PagerAdapter {
		ArrayList<T> list;

		// private int mChildCount = 0;
		public MyViewPager(ArrayList<T> list) {
			super();
			this.list = list;
		}

		@Override
		public void notifyDataSetChanged() {
			// mChildCount = getCount();
			super.notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			((ViewPager) container).removeView((View) object);
		}

		@Override
		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}

		@Override
		public View instantiateItem(ViewGroup container, final int position) {
			ArrayList<SkMessage_Voice> voicelist = bank.getRes().get(position).getVoice();
			String file = bank.getRes().get(position).getFile();
			View view = View.inflate(context, R.layout.activity_file_three_item, null);
			ImageView iv_three_item = (ImageView) view.findViewById(R.id.iv_three_item);
			ImageView iv_three_delete = (ImageView) view.findViewById(R.id.iv_three_delete);
			if (flag) {
				iv_three_delete.setVisibility(View.VISIBLE);
				iv_three_delete.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						String id = bank.getRes().get(position).getId();
						Delete_Ques_Utile.getIntence().Delete_Res(context, id, list, position, myViewPager);
					}
				});
			} else {
				iv_three_delete.setVisibility(View.GONE);
			}
			GridView gv_void_item = (GridView) view.findViewById(R.id.gv_void_item);
			gv_void_item.setAdapter(new GridviewAdapter(voicelist, Activity_File_Three.this));
			gv_void_item.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					GridviewAdapter adapter = (GridviewAdapter) parent.getAdapter();
					SkMessage_Voice item = adapter.getItem(position);
					String file = item.getFile();
					mediaPlayer.Down_Void(file, context);
				}
			});
			imageLoader.displayImage(file, iv_three_item, options);
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
			View view = View.inflate(context, R.layout.mess_three_item, null);
			ImageView voidce_image = (ImageView) view.findViewById(R.id.mess_voidce_make);
			voidce_image.setBackgroundResource(R.drawable.teather_void);
			ImageView red_point = (ImageView) view.findViewById(R.id.iv_red_point);
			red_point.setVisibility(View.GONE);
			return view;
		}
	}

	@Override
	protected void onStop() {
		mediaPlayer.Stop_Play();
		super.onStop();
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
}
