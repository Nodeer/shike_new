package com.yshow.shike.fragments;
import java.util.ArrayList;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yshow.shike.R;
import com.yshow.shike.activities.My_Question_Count_Two;
import com.yshow.shike.entity.LoginManage;
import com.yshow.shike.entity.Question_Bank;
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
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
/**
 *学生端按时间进行题库分类
 * @author Administrator
 */
public class Stu_QueCount_Time extends Fragment {
	private static MyPagerAdapte adapte ; //ViewPager 数据适配
	private static ViewPager vp_ques; // 图片页数显示翻页控件
	private String questionId; // 获取 删除题库的问题id
	private MyQGridViewAdapter qGridViewAdapter; // 具体显示图片的控件
	private static boolean flag = true; // 控制删除标记的显示与隐藏
	private Context context;
	public static Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(msg.what == 2){
				flag = !flag;
				if(adapte != null){
					adapte.notifyDataSetChanged();
					int currentItem = vp_ques.getCurrentItem();
					vp_ques.setAdapter(adapte);
					vp_ques.setCurrentItem(currentItem);
				}
			}
		}
	};
      @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
    		Bundle savedInstanceState) {
    	  context = getActivity();
    	  View view = View.inflate(getActivity(), R.layout.quecount_subject, null);
    	  questionId = getActivity().getIntent().getStringExtra("questionId");
    	  vp_ques = (ViewPager)view.findViewById(R.id.vp_ques_coun);
    	  timeCount_Data();
    	return view;
    }
      @Override
    public void onResume() {
    	super.onResume();
    	if(PartnerConfig.start_two){
    		timeCount_Data();
    	}
    	PartnerConfig.start_two = false;
    }
   // 它里面嵌套的有一个BaseAdapter
  	class MyPagerAdapte extends PagerAdapter {
  		ArrayList<ArrayList<Question_Bank>> question_Banks;
  		public MyPagerAdapte(ArrayList<ArrayList<Question_Bank>> question_Banks) {
  			this.question_Banks = question_Banks;
  		}
  		@Override
  		public int getCount() {
  			return question_Banks.size();
  		}
  		@Override
  		public boolean isViewFromObject(View arg0, Object arg1) {
  			return arg0 == arg1;
  		}
  		@Override
  		public void destroyItem(ViewGroup container, int position, Object view) {
  			((ViewPager) container).removeView((View) view);
  		}
  		@Override
  		public Object instantiateItem(ViewGroup container, int position) {
  			final ArrayList<Question_Bank> question_Bank = question_Banks.get(position);
  			View view = View.inflate(getActivity(),R.layout.my_question_count_item, null);
  			TextView tv_data = (TextView) view.findViewById(R.id.tv_data);
  			GridView count_itme_gridview = (GridView) view.findViewById(R.id.count_itme_gridview);
  				tv_data.setText(question_Bank.get(0).getData());
  			if (question_Bank != null) {
  				qGridViewAdapter = new MyQGridViewAdapter(question_Bank);
  				count_itme_gridview.setAdapter(qGridViewAdapter);
  			}
  			container.addView(view);
  			count_itme_gridview.setOnItemClickListener(new OnItemClickListener() {
  						@Override
  						public void onItemClick(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
  							Bundle bundle = new Bundle();
  								Question_Bank picture_count = question_Bank.get(arg2);
  								bundle.putSerializable("picture_count",picture_count);
  								bundle.putString("questionId", questionId);
  								Dialog.intent(getActivity(),My_Question_Count_Two.class, bundle);	
  							}
  					});
  			return view;
  		}
  	}
  	class MyQGridViewAdapter extends BaseAdapter {
		private ArrayList<Question_Bank> list;
		DisplayImageOptions options;
		ImageLoader imageLoader;
		public MyQGridViewAdapter(ArrayList<Question_Bank> list) {
			options = Net_Servse.getInstence().Picture_Shipei(R.drawable.back);
			imageLoader = ImageLoader.getInstance();
			this.list = list;
		}
		@Override
		public int getCount() {
			return list.size();
		}
		@Override
		public Question_Bank getItem(int position) {
			return list.get(position);
		}
		@Override
		public long getItemId(int position) {
			return position;
		}
		@Override
		public View getView(final int position, View convertView,ViewGroup parent) {
			Question_Bank skMessage_Res = list.get(position);
			if (convertView == null) {
				convertView = View.inflate(getActivity(),R.layout.sk_question_item, null);
			}
			ImageView imageView = (ImageView) convertView.findViewById(R.id.iv_question_picture);
			ImageView stu_delete = (ImageView) convertView.findViewById(R.id.iv_stu_delete);
			// 这是要最终要刷新的数据 只不过他是在PagerAdapter里面的 是不是直接刷新PagerAdapet 就可以了吗
			if (flag == true) {
				stu_delete.setVisibility(View.GONE);
			} else {
				stu_delete.setVisibility(View.VISIBLE);
				stu_delete.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						String questionId = list.get(position).getQuestion();
						Delete_boart(questionId,list,qGridViewAdapter,position);
					}
				});
			}
			boolean teacher = LoginManage.getInstance().isTeacher();
			if (teacher) {
				imageView.setBackgroundResource(R.drawable.teather_bg_message_image);
			}
			String file = skMessage_Res.getRes().get(0).getFile_tub();
			imageLoader.displayImage(file, imageView, options);
			return convertView;
		}
	}
   // 按时间进行分类
  	private void timeCount_Data() {
		SKAsyncApiController.Gain_Data(new MyAsyncHttpResponseHandler(context, true) {
			private ArrayList<Question_Bank> time = new ArrayList<Question_Bank>();;
			@Override
			public void onSuccess(String arg0) {
				super.onSuccess(arg0);
				boolean IsSuccess = SKResolveJsonUtil.getInstance().resolveIsSuccess(arg0,getActivity());
				if (IsSuccess) {
					ArrayList<Question_Bank> arrayList = SKResolveJsonUtil.getInstance().Count_Pase(arg0);
					ArrayList<ArrayList<Question_Bank>> times = new ArrayList<ArrayList<Question_Bank>>();
					String curdata = "";
					for (Question_Bank question_Bank : arrayList) {
						String data = question_Bank.getData();
						if (!curdata.equals(data)) {
							time = new ArrayList<Question_Bank>();
							times.add(time);
							curdata = data;
						}
						time.add(question_Bank);
					}
					adapte = new MyPagerAdapte(times);
					vp_ques.setAdapter(adapte);
				}
			}
		});
	}
    /**
     * 删除本地题库  并通知服务器
     * @param questionId
     * @param lsit
     * @param adapter
     * @param i
     */
	public  void Delete_boart(String questionId,final ArrayList<Question_Bank> lsit,final BaseAdapter adapter,final int i) {
		SKAsyncApiController.Delete_Bank(questionId,
				new MyAsyncHttpResponseHandler(context, true) {
					@Override
					public void onSuccess(String json) {
						super.onSuccess(json);
						boolean success = SKResolveJsonUtil.getInstance().resolveIsSuccess(json, getActivity());
						if (success == true) {
							lsit.remove(i);
							adapter.notifyDataSetChanged();
							Toast.makeText(getActivity(), "删除成功", 0).show();
						}
					}
				});
	}
}
