package com.yshow.shike.fragments;

import java.util.ArrayList;
import com.yshow.shike.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yshow.shike.activities.Activity_Recommend_Teacher;
import com.yshow.shike.activities.Activity_Teacher_Info;
import com.yshow.shike.entity.SKTeacherOrSubject;
import com.yshow.shike.entity.Star_Teacher_Parse;
import com.yshow.shike.utils.*;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.yshow.shike.widget.TopAndBottomLoadListView;
import com.yshow.shike.widget.XListView;

public class Fragment_Recommend_Teacher extends Fragment implements OnClickListener, TopAndBottomLoadListView.OnRefreshListener,
		XListView.IXListViewListener {
	static int ISSUBJECT_CODE = 101;
	private TextView recommend_name;
	private XListView lv_recommend;
	private Star_Teacher_Parse teacher_Parse;
	private MyAdapter adapter;
	private Context context;
	private int page = 1;
	private ArrayList<Star_Teacher_Parse> mDataList = new ArrayList<Star_Teacher_Parse>();
	private String mSubjectId = "0";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		context = inflater.getContext();
		View view = inflater.inflate(R.layout.fragment_recommend_teacher, null);
		recommend_name = (TextView) view.findViewById(R.id.tv_recommend_subject);
		recommend_name.setOnClickListener(this);
		lv_recommend = (XListView) view.findViewById(R.id.lv_recommend);
		lv_recommend.setXListViewListener(this);
		lv_recommend.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                if(arg3 == -1) {
                    // 点击的是headerView或者footerView
                    return;
                }
                int realPosition=(int)arg3;
				Star_Teacher_Parse item = adapter.getItem(realPosition);
				Intent intent = new Intent(getActivity(), Activity_Teacher_Info.class);
				intent.putExtra("headpicture", item);
				getActivity().startActivity(intent);
			}
		});
		adapter = new MyAdapter();
		lv_recommend.setAdapter(adapter);
		recommend_teather_pase(mSubjectId, true);

		return view;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 推荐老师
		case R.id.tv_recommend_subject:
			Intent intent = new Intent(getActivity(), Activity_Recommend_Teacher.class);
			intent.putExtra("select", Activity_Recommend_Teacher.ISSUBJECT);
			startActivityForResult(intent, Activity_Recommend_Teacher.ISSUBJECT_CODE);
			break;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity_Recommend_Teacher.ISSUBJECT_CODE) {
			SKTeacherOrSubject item = (SKTeacherOrSubject) data.getSerializableExtra("selectSubject");
			if (item != null) {
				mSubjectId = item.getSubjectId();
				recommend_name.setText(item.getName());
				page = 1;
				mDataList.clear();
				recommend_teather_pase(mSubjectId, true);
			}
			super.onActivityResult(requestCode, resultCode, data);
		}
	}

	/**
	 * 获取推荐老师
	 * 
	 * @param id
	 */
	public void recommend_teather_pase(String id, boolean needProgress) {
		SKAsyncApiController.recommend_teather(id, "0", page, new MyAsyncHttpResponseHandler(context, needProgress) {
			public void onSuccess(int arg0, String json) {
				super.onSuccess(arg0, json);
				boolean success = SKResolveJsonUtil.getInstance().resolveIsSuccess(json, getActivity());
				if (success) {
					ArrayList<Star_Teacher_Parse> list = SKResolveJsonUtil.getInstance().start_teather(json);
					mDataList.addAll(list);
					if (list.size() < 20) {
						lv_recommend.setPullLoadEnable(false);
					} else {
						lv_recommend.setPullLoadEnable(true);
					}
					if (mDataList.size() == 0) {
						Toast.makeText(context, "没有推荐老师", Toast.LENGTH_SHORT).show();
					}
					adapter.notifyDataSetChanged();
                    onLoad();
				}
			};
		});
	}

    private void onLoad() {
        lv_recommend.stopRefresh();
        lv_recommend.stopLoadMore();
        lv_recommend.setRefreshTime(Timer_Uils.getCurrentTime());
    }

	@Override
	public void onLoadMore() {
		page++;
		recommend_teather_pase(mSubjectId, false);
	}

	@Override
	public void onRefresh() {
		page = 1;
		mDataList.clear();
		recommend_teather_pase(mSubjectId, false);
	}

	class MyAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return mDataList.size();
		}

		@Override
		public Star_Teacher_Parse getItem(int arg0) {
			return mDataList.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			teacher_Parse = mDataList.get(position);
			DisplayImageOptions options = Net_Servse.getInstence().Picture_Shipei(R.drawable.my_tea_phon);
			ImageLoader imageLoader = ImageLoader.getInstance();
			if (convertView == null) {
				convertView = View.inflate(getActivity(), R.layout.fragment_start_text, null);
			}
			ImageView teather_picture = (ImageView) convertView.findViewById(R.id.iv_teather_picture);
			TextView tv_nicheng = (TextView) convertView.findViewById(R.id.tv_nicheng);
			TextView tv_subject = (TextView) convertView.findViewById(R.id.tv_subject);
			TextView tv_grade = (TextView) convertView.findViewById(R.id.tv_grade);
			TextView tv_diqu = (TextView) convertView.findViewById(R.id.tv_diqu);
			TextView tv_gerenxinxi = (TextView) convertView.findViewById(R.id.tv_gerenxinxi);
			if (PartnerConfig.list.contains(teacher_Parse.getUid())) {
				convertView.findViewById(R.id.iv_teather_sele).setVisibility(View.VISIBLE);
			} else {
				convertView.findViewById(R.id.iv_teather_sele).setVisibility(View.GONE);
			}
			imageLoader.displayImage(teacher_Parse.getIcon(), teather_picture, options);
			tv_nicheng.setText(teacher_Parse.getNickname());
			tv_subject.setText(teacher_Parse.getSubiect());
			tv_grade.setText(teacher_Parse.getGrade());
			tv_diqu.setText(teacher_Parse.getArea());
			tv_gerenxinxi.setText(teacher_Parse.getInfo());
			return convertView;
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		if (adapter != null) {
			adapter.notifyDataSetChanged();
		}
	}
}
