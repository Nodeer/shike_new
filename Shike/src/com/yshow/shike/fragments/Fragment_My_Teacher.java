package com.yshow.shike.fragments;
import java.util.ArrayList;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.yshow.shike.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yshow.shike.activities.Activity_My_Teacher_Info;
import com.yshow.shike.entity.LoginManage;
import com.yshow.shike.entity.SKStudent;
import com.yshow.shike.entity.Star_Teacher_Parse;
import com.yshow.shike.entity.User_Info;
import com.yshow.shike.utils.MyAsyncHttpResponseHandler;
import com.yshow.shike.utils.Net_Servse;
import com.yshow.shike.utils.SKAsyncApiController;
import com.yshow.shike.utils.SKResolveJsonUtil;

/**
 * 我的老师页面,可以看到老师列表
 */
public class Fragment_My_Teacher extends Fragment {
	private View view;
	private DisplayImageOptions options;
	private ImageLoader imageLoader;
	private LinearLayout ll_my_teather;
	private Context context;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		context = inflater.getContext();
		view = inflater.inflate(R.layout.fragment_teacher, null);
		SKStudent student = LoginManage.getInstance().getStudent();
		ll_my_teather = (LinearLayout) view.findViewById(R.id.ll_my_teather);
		TextView tv_audo = (TextView) view.findViewById(R.id.tv_audo);
		options = Net_Servse.getInstence().Picture_Shipei(R.drawable.my_tea_phon);
		imageLoader = ImageLoader.getInstance();
        try {
    		if(student.getMob() != null){
    			ll_my_teather.setVisibility(View.VISIBLE);
    			tv_audo.setVisibility(View.GONE);
    		} else {
    			tv_audo.setVisibility(View.VISIBLE);
    			ll_my_teather.setVisibility(View.GONE);
    		}
		} catch (Exception e) {}
		return view;
	}
	public View getView(ArrayList<Star_Teacher_Parse> star_Teacher_Parses) {
		View item_view = View.inflate(context, R.layout.my_teather_item, null);
		TextView iv_picture = (TextView) item_view.findViewById(R.id.tv_subject1);
		GridView gridview = (GridView) item_view.findViewById(R.id.gv_my_teather_gridview);
		final MyAdapter adapter = new MyAdapter(star_Teacher_Parses);
		gridview.setAdapter(adapter);
		iv_picture.setText(star_Teacher_Parses.get(0).getSubiect());
		gridview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
				Star_Teacher_Parse item = (Star_Teacher_Parse) adapter.getItem(arg2);
				Intent intent = new Intent(getActivity(),Activity_My_Teacher_Info.class);
				intent.putExtra("mark", "Fragment_My_Teacher");
				intent.putExtra("headpicture", item);
				getActivity().startActivity(intent);
			}
		});
		return item_view;
	}
	private void My_Teather() {
		SKAsyncApiController.My_Taeather_Parse(new MyAsyncHttpResponseHandler(context, true) {
			@Override
			public void onSuccess(String json) {
				super.onSuccess(json);
				boolean success = SKResolveJsonUtil.getInstance().resolveIsSuccess(json,getActivity());
				if (success) {
					ArrayList<ArrayList<Star_Teacher_Parse>> my_Teather = SKResolveJsonUtil.getInstance().My_Teather(json);
					for (int i = 0; i < my_Teather.size(); i++) {
						ll_my_teather.addView(getView(my_Teather.get(i)));
					}
				}
			}
		});
	}
	class MyAdapter extends BaseAdapter {
		ArrayList<Star_Teacher_Parse> star_Teacher_Parses;
		public MyAdapter(ArrayList<Star_Teacher_Parse> star_Teacher_Parses) {
			super();
			this.star_Teacher_Parses = star_Teacher_Parses;
		}
		@Override
		public int getCount() {
			return star_Teacher_Parses.size();
		}
		@Override
		public Object getItem(int arg0) {
			return star_Teacher_Parses.get(arg0);
		}
		@Override
		public long getItemId(int arg0) {
			return arg0;
		}
		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			Star_Teacher_Parse teacher_Parse = star_Teacher_Parses.get(arg0);
			View item_view = View.inflate(getActivity(),R.layout.my_gridview_teather_item, null);
			TextView tv_wenben = (TextView) item_view.findViewById(R.id.tv_wenben);
			ImageView my_teather_img = (ImageView) item_view.findViewById(R.id.my_teather_img);
			tv_wenben.setText(teacher_Parse.getNickname());
			imageLoader.displayImage(teacher_Parse.getIcon(), my_teather_img,options);
			return item_view;
		}
	}
	@Override
	public void onResume() {
		super.onResume();
		ll_my_teather.removeAllViews();
		My_Teather();
	}
}
