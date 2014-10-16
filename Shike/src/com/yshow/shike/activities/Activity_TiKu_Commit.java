package com.yshow.shike.activities;
import java.util.ArrayList;

import com.umeng.analytics.MobclickAgent;
import com.yshow.shike.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yshow.shike.entity.Question_Bank;
import com.yshow.shike.entity.SkMessage_Res;
import com.yshow.shike.entity.SkMessage_Voice;
import com.yshow.shike.utils.Animation;
import com.yshow.shike.utils.MyAsyncHttpResponseHandler;
import com.yshow.shike.utils.SKAsyncApiController;
import com.yshow.shike.utils.SKResolveJsonUtil;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
public class Activity_TiKu_Commit extends Activity {
	private Boolean isUnfold = true;
	private Boolean bac_mack = true;
	private SkMessage_Res skMessage_Res ;
	private ViewPager tea_commit;
	private RelativeLayout tea_anim;
	Context context;
   	private ImageLoader imageLoader;
   	private DisplayImageOptions options;
	private ArrayList<SkMessage_Res> TiMulist;
     @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.avy_tea_pic_commit);
    	context = this;
    	initData();
    	PictureAdd();
    }
	private void initData() {
		findViewById(R.id.tea_three_point).setOnClickListener(listener);
		findViewById(R.id.tea_cit_back).setOnClickListener(listener);
		findViewById(R.id.tea_commfig).setOnClickListener(listener);
		tea_commit = (ViewPager) findViewById(R.id.tea_comm_vp);
		tea_anim = (RelativeLayout) findViewById(R.id.teacomm_anim);
		Question_Bank bank = (Question_Bank) getIntent().getSerializableExtra("Question_Bank");
		if(bank!=null){
			TiMulist = bank.getRes();
			pageAdapter = new ViewPageAdapter(TiMulist);
			tea_commit.setAdapter(pageAdapter);
			tea_commit.setCurrentItem(TiMulist.size()-1);
		}
	}
	private OnClickListener listener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.tea_three_point:
				Animation animation = new Animation(context, tea_anim, -26,24);
				animation.initData(isUnfold);
				isUnfold = !isUnfold;
				break;
			case R.id.tea_commfig:
				if(bac_mack){
					Toast.makeText(context,"请选择图片", 0).show();
				}else {
					Uploading_Picture();
				}
				break;
			case R.id.tea_cit_back:
				finish();
				break;
			}
		}
	};
	private ViewPageAdapter pageAdapter;
	class ViewPageAdapter extends PagerAdapter{
		ArrayList<SkMessage_Res> TiMulist;
		public ViewPageAdapter(ArrayList<SkMessage_Res> tiMulist) {
			super();
			TiMulist = tiMulist;
		}
		@Override
		public int getCount() {
			return TiMulist.size();
		}
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1 ;
		}
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
		}
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			View view = View.inflate(context, R.layout.mess_save_item, null);
			skMessage_Res = TiMulist.get(position);
			String file = skMessage_Res.getFile();
			ArrayList<SkMessage_Voice> VoiceList = skMessage_Res.getVoice();
			ImageView iv_void_save = (ImageView) view.findViewById(R.id.iv_void_save);
			final ImageView iv_bac_pirture = (ImageView) view.findViewById(R.id.iv_bac_pirture);
			GridView voide_save = (GridView) view.findViewById(R.id.gf_mess_save);
			imageLoader.displayImage(file, iv_void_save,options);
			MyAdapter adapter = new MyAdapter(VoiceList);
			voide_save.setAdapter(adapter);
			iv_void_save.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					if(bac_mack){
						iv_bac_pirture.setVisibility(View.VISIBLE);
						bac_mack = false;
					}else {
						iv_bac_pirture.setVisibility(View.GONE);
						bac_mack = true;
					}
				}
			});
			container.addView(view);
			return view;
		}
	}
/**
 *音频数据适配
 */
	class MyAdapter extends BaseAdapter{
		ArrayList<SkMessage_Voice> VoiceList;
		public MyAdapter(ArrayList<SkMessage_Voice> voiceList) {
			super();
			VoiceList = voiceList;
		}
		@Override
		public int getCount() {
			return VoiceList.size();
		}
		@Override
		public Object getItem(int arg0) {
			return VoiceList.get(arg0);
		}
		@Override
		public long getItemId(int arg0) {
			return arg0;
		}
		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			View view = View.inflate(context, R.layout.activity_que_board2_item, null);
			return view;
		}
	}
//	图片适配
	private void PictureAdd() {
		options = new DisplayImageOptions.Builder().cacheOnDisc(true).showImageForEmptyUri(R.drawable.xiaoxi_moren)
				.showImageOnFail(R.drawable.xiaoxi_moren).cacheInMemory(true).build();
		imageLoader = ImageLoader.getInstance();
	}
//图片上传
	private void Uploading_Picture() {
		String questionId = skMessage_Res.getQuestionId();
		View view = (View) pageAdapter.instantiateItem(tea_commit, tea_commit.getCurrentItem());
		ImageView picture = (ImageView) view.findViewById(R.id.iv_void_save);
		BitmapDrawable bd = (BitmapDrawable)picture.getDrawable();
		Bitmap bitmap = bd.getBitmap();
		SKAsyncApiController.skUploadImage(questionId, bitmap, new MyAsyncHttpResponseHandler(this,true){
			@Override
			public void onSuccess(String json) {
				super.onSuccess(json);
				boolean success = SKResolveJsonUtil.getInstance().resolveIsSuccess(json, context);
			    if(success){
			    	Toast.makeText(context,"图片上传成功", 0).show();
                     //	语音上传	    
			    	Toast.makeText(context,"语音等待上传", 0).show();
			    }
			}
		});
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
