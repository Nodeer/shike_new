package com.yshow.shike.fragments;
import java.util.ArrayList;
import com.yshow.shike.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yshow.shike.activities.ActicityStudentInfo;
import com.yshow.shike.entity.Star_Teacher_Parse;
import com.yshow.shike.utils.Dialog;
import com.yshow.shike.utils.MyAsyncHttpResponseHandler;
import com.yshow.shike.utils.Net_Servse;
import com.yshow.shike.utils.SKAsyncApiController;
import com.yshow.shike.utils.SKResolveJsonUtil;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class Fragment_Teacher_My_Student extends Fragment {
	private ListView listview;
	private Context context;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = getActivity();
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_message, null);
		listview = (ListView) view.findViewById(R.id.listview);
		Stu_Info();
		return view;
	}
	class MyAdapter extends BaseAdapter {
		ArrayList<Star_Teacher_Parse> arrayList;
		public MyAdapter(ArrayList<Star_Teacher_Parse> arrayList) {
			super();
			this.arrayList = arrayList;
		}
		@Override
		public int getCount() {
			return arrayList.size();
		}
		@Override
		public Object getItem(int arg0) {
			return arrayList.get(arg0);
		}
		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int arg0, View convertView, ViewGroup arg2) {
			final Star_Teacher_Parse parse = arrayList.get(arg0);
			DisplayImageOptions options = Net_Servse.getInstence().Picture_Shipei(R.drawable.teather_stu_picture);
			ImageLoader imageLoader = ImageLoader.getInstance();
			if (convertView == null) {
				convertView = View.inflate(getActivity(),R.layout.my_student_item, null);
			}
			ImageView stu_nic = (ImageView) convertView.findViewById(R.id.iv_teather_picture);
			TextView tv_stu_nic = (TextView) convertView.findViewById(R.id.tv_stu_nic);
			TextView stu_info = (TextView) convertView.findViewById(R.id.tv_stu_info);
			tv_stu_nic.setText(parse.getNickname());
			stu_info.setText(parse.getInfo());
			imageLoader.displayImage(parse.getIcon(), stu_nic, options);
			stu_nic.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					Bundle bundle = new Bundle();
					bundle.putSerializable("Star_Teacher_Parse", parse);
					Dialog.intent(getActivity(), ActicityStudentInfo.class,bundle);
				}
			});
			return convertView;
		}
	}

	// 获取我的学生信息
	private void Stu_Info() {
		SKAsyncApiController.getStu_Info(new MyAsyncHttpResponseHandler(context, true) {
			@Override
			public void onSuccess(String json) {
				super.onSuccess(json);
				boolean success = SKResolveJsonUtil.getInstance().resolveIsSuccess(json,getActivity());
				if (success) {
					ArrayList<ArrayList<Star_Teacher_Parse>> stu_Pase = SKResolveJsonUtil.getInstance().My_Tea_Pase(json);
					for (ArrayList<Star_Teacher_Parse> arrayList : stu_Pase) {
						listview.setAdapter(new MyAdapter(arrayList));
					}
				}
			}
		});
	}
}