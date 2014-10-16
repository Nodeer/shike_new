package com.yshow.shike.fragments;
import java.util.ArrayList;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yshow.shike.R;
import com.yshow.shike.activities.Activity_File_Three;
import com.yshow.shike.entity.Question_Bank;
import com.yshow.shike.utils.Delete_Ques_Utile;
import com.yshow.shike.utils.Dialog;
import com.yshow.shike.utils.MyAsyncHttpResponseHandler;
import com.yshow.shike.utils.Net_Servse;
import com.yshow.shike.utils.PartnerConfig;
import com.yshow.shike.utils.SKAsyncApiController;
import com.yshow.shike.utils.SKResolveJsonUtil;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
public class Tea_Tiku_Time extends Fragment {
	private DisplayImageOptions options;
	private ImageLoader imageLoader;
	private static GridView gr_file_count;
	private static My_Time_Adapter my_Time_Adapter;
	private static boolean flag = false;
	private static boolean Delete_Mark = false;
	private static MyPageAdapter timeadapter ;
	private ViewPager vp_time_board;
	private Context context ;
	private Boolean start_tiku_three = false;
	public static Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg.what == 1){
					if(gr_file_count !=null){
						gr_file_count.setAdapter(my_Time_Adapter);
					}
					Delete_Mark =! Delete_Mark;
			}
		};
	};
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
    		Bundle savedInstanceState) {
    	context = getActivity();
    	View view = View.inflate(context, R.layout.quecount_subject, null);
    	vp_time_board = (ViewPager) view.findViewById(R.id.vp_ques_coun);
    	options = Net_Servse.getInstence().Picture_Shipei(R.drawable.back);
		imageLoader = ImageLoader.getInstance();
    	Time_File_Data(false);
    	return view;
    }
    @Override
    public void onResume() {
    	super.onResume();
    	if(start_tiku_three ){
    		Time_File_Data(false);
    	}
    }
 // 按時間 搜索
 	class MyPageAdapter extends PagerAdapter {
 		private ArrayList<ArrayList<Question_Bank>> files;
 		public MyPageAdapter(ArrayList<ArrayList<Question_Bank>> files,Boolean flag) {
 			super();
 			this.files = files;
 		}
 		@Override
 		public int getCount() {
 				return files.size();
 		}
 		@Override
 		public boolean isViewFromObject(View arg0, Object arg1) {
 			return arg0 == arg1;
 		}
 		@Override
 		public void destroyItem(ViewGroup container, int position, Object object) {
 		}
 		@Override
 		public Object instantiateItem(ViewGroup container, int position) {
 			View view = View.inflate(context, R.layout.activity_file_item, null);
 			ArrayList<Question_Bank> arrayList = files.get(position);
 			String data = arrayList.get(0).getData();
 			gr_file_count = (GridView) view.findViewById(R.id.gr_file1_count);
 			TextView tv_time = (TextView) view.findViewById(R.id.tv_time_TiMu);
 			tv_time.setText(data);
 			my_Time_Adapter = new My_Time_Adapter(arrayList);
 			gr_file_count.setAdapter(my_Time_Adapter);
 			container.addView(view);
 			return view;
 		}
 	}
 	class My_Time_Adapter extends BaseAdapter {
		ArrayList<Question_Bank> time_count;
		public My_Time_Adapter(ArrayList<Question_Bank> count_Pase) {
			super();
			this.time_count = count_Pase;
		}
		@Override
		public int getCount() {
			if(flag){
				return time_count.size()+1;
			}else {
				return time_count.size();
			}
		}
		@Override
		public Object getItem(int arg0) {
			return time_count.get(arg0);
		}
		@Override
		public long getItemId(int arg0) {
			return arg0;
		}
		@Override
		public View getView(final int pointion, View convertView, ViewGroup arg2) {
			final Question_Bank bank = time_count.get(pointion);
			final String file_cid = bank.getCid();
			if (convertView == null) {
				convertView = View.inflate(context,R.layout.gridview_file_item, null);
			}
			TextView file_name = (TextView) convertView.findViewById(R.id.file_name);
			ImageView iv_file = (ImageView) convertView.findViewById(R.id.iv_sing_file);
			LinearLayout ll_file = (LinearLayout) convertView.findViewById(R.id.ll_file);
			ImageView iv_tea_delete = (ImageView) convertView.findViewById(R.id.iv_tea_delete);
			convertView.findViewById(R.id.iv_point_file).setVisibility(View.INVISIBLE);
			if(Delete_Mark){
				iv_tea_delete.setVisibility(View.VISIBLE);
				iv_tea_delete.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
//						Delete_File_Topic(time_count, file_cid, pointion,my_Time_Adapter);
						Delete_Ques_Utile.getIntence().Delete_Classify_File(time_count, file_cid, pointion, my_Time_Adapter, context);
					}
				});
			}else {
				iv_tea_delete.setVisibility(View.GONE);
			}
				if (time_count.get(pointion).getName().equals("false")) {
					file_name.setText("未分类" + "(" + time_count.size()+ ")");
				} else {
					file_name.setText(time_count.get(pointion).getName() + "("+ time_count.size() + ")");
				}
				imageLoader.displayImage(time_count.get(pointion).getRes().get(0).getFile(),iv_file, options);
			    ll_file.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					start_tiku_three = true;
					Bundle bundle = new Bundle();
					bundle.putSerializable("Question_Bank", bank);
					Dialog.intent(context, Activity_File_Three.class, bundle);
				}
			  });
			return convertView;
		}
	}
 // 以时间方式查看题库
 	private void Time_File_Data(final Boolean flag) {
 		SKAsyncApiController.Gain_Data(new MyAsyncHttpResponseHandler(context,true) {
 			private ArrayList<Question_Bank> time = new ArrayList<Question_Bank>();
 			@Override
 			public void onSuccess(String json) {
 				super.onSuccess(json);
 				boolean IsSuccess = SKResolveJsonUtil.getInstance().resolveIsSuccess(json,context);
 				if (IsSuccess) {
 					ArrayList<Question_Bank> arrayList = SKResolveJsonUtil.getInstance().Count_Pase(json);
 					ArrayList<ArrayList<Question_Bank>> times = new ArrayList<ArrayList<Question_Bank>>();
 					String curdata = "";
 					for (Question_Bank question_Bank : arrayList) {
 						boolean isExist = false;
 						String data = question_Bank.getData();
 						for (ArrayList<Question_Bank> item : times) {
 							String getData = item.get(0).getData();
 							if (data.equals(getData)) {
 								time = item;
 								isExist = true;
 								break;
 							}
 						}
 						if (!isExist &&!curdata.equals(data)) {
 							time = new ArrayList<Question_Bank>();
 							times.add(time);
 							curdata = data;
 						}
 						time.add(question_Bank);
 					}
 					timeadapter = new MyPageAdapter(times,flag);
 					vp_time_board.setAdapter(timeadapter);
 					vp_time_board.setCurrentItem(times.size()-1);
 				}
 			}
 		});
 	}
  /**
   * 以时间的方式进行删除
   * @param time_count
   * @param resid
   * @param position
   * @param timeadapter2
   */
//  	private <T> void Delete_File_Topic(final ArrayList<Question_Bank> time_count,String resid,final int position,final BaseAdapter timeadapter2) {
//  		SKAsyncApiController.Delete_File(resid, new MyAsyncHttpResponseHandler(context,true){
//  		@Override
//  		public void onSuccess(String json) {
//  			super.onSuccess(json);
//  			boolean success = SKResolveJsonUtil.resolveIsSuccess(json,context);
//  			if(success){
//  				time_count.remove(position);
//  			    timeadapter2.notifyDataSetChanged();
//  				Toast.makeText(context, "删除成功", 0).show();
//  			}
//  		}
//  	  });
//    }
}
