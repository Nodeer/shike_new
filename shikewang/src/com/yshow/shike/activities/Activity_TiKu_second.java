package com.yshow.shike.activities;
import java.util.ArrayList;

import android.content.Intent;
import com.umeng.analytics.MobclickAgent;
import com.yshow.shike.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yshow.shike.entity.Question_Bank;
import com.yshow.shike.entity.SkMessage_Res;
import com.yshow.shike.utils.Delete_Ques_Utile;
import com.yshow.shike.utils.Dialog;
import com.yshow.shike.utils.Net_Servse;
import com.yshow.shike.utils.PartnerConfig;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 点击文件夹后进入的题库页面,显示该文件夹下的题库列表
 */
public class Activity_TiKu_second extends Activity {
	private static boolean flag = false;
	private Context context;
	private ArrayList<Question_Bank> fileList;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	private MyGridView gridView_adapter;
	private ArrayList<Question_Bank> arrayList;
	private GridView gv_file_second;
	private MyViewPager viewPager;
	private String question_into;
	private ArrayList<ArrayList<Question_Bank>> files;
	//是否开启过第三个界面
	private boolean isStartThree = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tiku_second);
		context = this;
		initData();
		options = Net_Servse.getInstence().Picture_Shipei(R.drawable.back);
		imageLoader = ImageLoader.getInstance();
	}
	@SuppressWarnings("unchecked")
	private void initData() {
		vp_second = (ViewPager) findViewById(R.id.vp_second);
		TextView tv_add = (TextView) findViewById(R.id.tv_add);
		TextView tv_delete = (TextView) findViewById(R.id.tv_second_delete);
		Bundle bundle = getIntent().getExtras();
		fileList = (ArrayList<Question_Bank>) bundle.getSerializable("TiMu_Bank");
		question_into = bundle.getString("question_into");
		if (question_into == null) {
			tv_add.setVisibility(View.GONE);
			tv_delete.setVisibility(View.GONE);
		}
		files = new ArrayList<ArrayList<Question_Bank>>();
		ArrayList<Question_Bank> time = null;
		for (Question_Bank question_Bank : fileList) {
			String data = question_Bank.getData();
			boolean isExist = false;
			for (ArrayList<Question_Bank> itme : files) {
				String data2 = itme.get(0).getData();
				if (data2.equals(data)) {
					isExist = true;
					time = itme;
					break;
				}
			}
			if (!isExist ) {
				time = new ArrayList<Question_Bank>();
				files.add(time);
			}
			time.add(question_Bank);
		}
		if(files.get(0).get(0).getQuestion() != null){
			viewPager = new MyViewPager(files);
			vp_second.setAdapter(viewPager);
			vp_second.setCurrentItem(files.size() - 1);
		}
		findViewById(R.id.tv_back).setOnClickListener(listener);
		tv_delete.setOnClickListener(listener);
		tv_add.setOnClickListener(listener);
	}
    @Override
    protected void onResume() {
		super.onResume();
        MobclickAgent.onResume(this);
		if(isStartThree && !TextUtils.isEmpty(PartnerConfig.two_index)){//如果开启过第三个界面
				Question_Bank bank = fileList.get(Integer.parseInt(PartnerConfig.two_index));
				ArrayList<SkMessage_Res> images = bank.getRes();
				ArrayList<String> ss = new ArrayList<String>();
				for(int x = 0;x<images.size();x++){
					if(PartnerConfig.three_indexs.contains(images.get(x).getId())){
						ss.add(x+"");
					}
				}
				for(int x = 0;x<ss.size();x++){
					images.remove(Integer.parseInt(ss.get(x)));
				}
				if(images.size() <= 0)
					fileList.remove(Integer.parseInt(PartnerConfig.two_index));
		}
		files = new ArrayList<ArrayList<Question_Bank>>();
		ArrayList<Question_Bank> time = null;
		for (Question_Bank question_Bank : fileList) {
			String data = question_Bank.getData();
			boolean isExist = false;
			for (ArrayList<Question_Bank> itme : files) {
				String data2 = itme.get(0).getData();
				if (data2.equals(data)) {
					isExist = true;
					time = itme;
					break;
				}
			}
			if (!isExist) {
				time = new ArrayList<Question_Bank>();
				files.add(time);
			}
			time.add(question_Bank);
		}
		if(files.get(0).get(0).getQuestion() != null){
			viewPager = new MyViewPager(files);
			vp_second.setAdapter(viewPager);
			vp_second.setCurrentItem(files.size() - 1);
		}
		PartnerConfig.three_indexs.clear();
		isStartThree = false;
	}
	private OnClickListener listener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			// 题库的返回按钮
			case R.id.tv_back:
				finish();
				break;
			// 题库的添加按钮
			case R.id.tv_add:
				Dialog.Intent(context, Activity_Tea_Tool_Sele.class);
				break;
			// 题库的删除按钮
			case R.id.tv_second_delete:
				if(files.get(0).get(0).getQuestion() != null){
					flag = !flag;
					gv_file_second.setAdapter(gridView_adapter);
				}
				break;
			}
		}
	};
	private ViewPager vp_second;
	class MyViewPager extends PagerAdapter {
		ArrayList<ArrayList<Question_Bank>> List;
		public MyViewPager(ArrayList<ArrayList<Question_Bank>> list) {
			super();
			List = list;
		}
		@Override
		public int getCount() {
			return List.size();
		}
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			((ViewPager)container).removeView((View)object);
		}
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			arrayList = List.get(position);//某一天的题库列表
			Question_Bank bank = arrayList.get(0);//取第一个,显示其名字
			View view = View.inflate(context, R.layout.second_pager_item, null);
			gv_file_second = (GridView) view.findViewById(R.id.gv_file_second);
			TextView tv_time = (TextView) view.findViewById(R.id.tv_time);
			String data = bank.getData();
			tv_time.setText(data);
			gridView_adapter = new MyGridView(arrayList);
			gv_file_second.setAdapter(gridView_adapter);
			container.addView(view);
			return view;
		}
	}
	class MyGridView extends BaseAdapter {
		ArrayList<Question_Bank> fileList;
		public MyGridView(ArrayList<Question_Bank> fileList) {
			super();
			this.fileList = fileList;
		}
		@Override
		public int getCount() {
			return fileList.size();
		}
		@Override
		public Object getItem(int arg0) {
			return fileList.get(arg0);
		}
		@Override
		public long getItemId(int arg0) {
			return arg0;
		}
		@Override
		public View getView(final int arg0, View arg1, ViewGroup arg2) {
			View view = View.inflate(context, R.layout.gridview_file_item, null);
			try {
				final Question_Bank bank = fileList.get(arg0);
				ImageView iv_file = (ImageView) view.findViewById(R.id.iv_sing_file);
				TextView file_name = (TextView) view.findViewById(R.id.file_name);
				ImageView iv_tea_delete = (ImageView) view.findViewById(R.id.iv_tea_delete);
			    view.findViewById(R.id.iv_point_file).setVisibility(View.INVISIBLE);
				if (flag) {
					iv_tea_delete.setVisibility(View.VISIBLE);
					iv_tea_delete.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							String questionId = bank.getRes().get(0).getQuestionId();
							Delete_Ques_Utile.getIntence().Delete_File(context,questionId,fileList,arg0,gridView_adapter);
						}
					});
				} else {
					iv_tea_delete.setVisibility(View.GONE);
				}
				if (bank.getName().equals("false")) {
					file_name.setText("未命名");
				} else {
					file_name.setText(bank.getName());
				}
				iv_file.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Bundle bundle = new Bundle();
						bundle.putSerializable("Question_Bank", bank);//把这个题库传了过去
						if (question_into != null) {
							Dialog.intent(context, Activity_File_Three.class,bundle);
							PartnerConfig.two_index = arg0+"";
							isStartThree = true;
						} else {
							Dialog.intent(context, Activity_TiKu_Commit.class,bundle);
						}
					}
				});
				imageLoader.displayImage(bank.getRes().get(0).getFile(), iv_file,options);
			} catch (Exception e) {
				
			}
			return view;
		}
	}

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }
}
