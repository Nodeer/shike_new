package com.yshow.shike.fragments;

import java.util.ArrayList;
import com.yshow.shike.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yshow.shike.activities.Activity_Recommend_Teacher;
import com.yshow.shike.activities.Activity_Teacher_Info;
import com.yshow.shike.entity.SKArea;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.yshow.shike.widget.TopAndBottomLoadListView;
import com.yshow.shike.widget.XListView;

/**
 * 明星老师
 */
public class Fragment_Star_Teacher extends Fragment implements OnClickListener, TopAndBottomLoadListView.OnRefreshListener,
		XListView.IXListViewListener {
	private LinearLayout seleck_subject;
	private LinearLayout ll_diqu;
	private TextView tv_subject;
	private TextView tv_diqu;
	private XListView starListView;
	private Context context;
	private String currentSubjiect, currentArea;

	private int page = 1;
	private ArrayList<Star_Teacher_Parse> mDataList = new ArrayList<Star_Teacher_Parse>();
	private String mSubjectId = "0";
	private String mAreaId = "0";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		context = inflater.getContext();
		View view = inflater.inflate(R.layout.fragment_my_teacher, null);
		ll_diqu = (LinearLayout) view.findViewById(R.id.ll_diqu);
		tv_diqu = (TextView) view.findViewById(R.id.tv_diqu);
		seleck_subject = (LinearLayout) view.findViewById(R.id.ll_seleck_subject1);
		tv_subject = (TextView) view.findViewById(R.id.tv_subject);
		starListView = (XListView) view.findViewById(R.id.start_listView);
		starListView.setXListViewListener(this);
		ll_diqu.setOnClickListener(this);
		seleck_subject.setOnClickListener(this);
		adapter = new RegionAdapter();
		starListView.setAdapter(adapter);
		Start_Teacher(mSubjectId, mAreaId);
		starListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                if(arg3 == -1) {
                    // 点击的是headerView或者footerView
                    return;
                }
                int realPosition=(int)arg3;
				Star_Teacher_Parse item = adapter.getItem(realPosition);
				Intent intent = new Intent(getActivity(), Activity_Teacher_Info.class);
				intent.putExtra("mark", "Fragment_My_Teacher");
				intent.putExtra("headpicture", item);
				getActivity().startActivity(intent);
			}
		});
		return view;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 地区选择按钮
		case R.id.ll_diqu:
			Intent regionintent = new Intent(getActivity(), Fragment_Region.class);
			regionintent.putExtra("select", Activity_Recommend_Teacher.ISSUBJECT);
			startActivityForResult(regionintent, Fragment_Region.ISSUBJECT_CODE);
			break;
		// 科目选择按钮
		case R.id.ll_seleck_subject1:
			Intent intent = new Intent(getActivity(), Activity_Recommend_Teacher.class);
			intent.putExtra("select", Activity_Recommend_Teacher.ISSUBJECT);
			startActivityForResult(intent, Activity_Recommend_Teacher.ISSUBJECT_CODE);
			break;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity_Recommend_Teacher.ISSUBJECT_CODE) {
			SKTeacherOrSubject item = (SKTeacherOrSubject) data.getSerializableExtra("selectSubject");
			if (item != null) {
				mSubjectId = item.getSubjectId();
				tv_subject.setText(item.getName());
				page = 1;
				mDataList.clear();
				Start_Teacher(mSubjectId, currentArea);
			}
		}
		if (resultCode == Fragment_Region.DIQU) {
			SKArea diqu = (SKArea) data.getSerializableExtra("0");
			if (diqu != null) {
				mAreaId = diqu.getId();
				String name = diqu.getName();
				tv_diqu.setText(name);
				page = 1;
				mDataList.clear();
				Start_Teacher(currentSubjiect, mAreaId);
			}
		}
	}

	@Override
	public void onRefresh() {
		page = 1;
		mDataList.clear();
		refreshStarTeacher(mSubjectId, mAreaId);
	}

	@Override
	public void onLoadMore() {
		page++;
		getMoreStarTeacher(mSubjectId, mAreaId);
	}

	private void onLoad() {
		starListView.stopRefresh();
		starListView.stopLoadMore();
		starListView.setRefreshTime(Timer_Uils.getCurrentTime());
	}

	class RegionAdapter extends BaseAdapter {
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
			Star_Teacher_Parse teacher_Parse = mDataList.get(position);
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
			ImageView teather_sele = (ImageView) convertView.findViewById(R.id.iv_teather_sele);
			if (PartnerConfig.list.contains(teacher_Parse.getUid())) {
				teather_sele.setVisibility(View.VISIBLE);
			} else {
				teather_sele.setVisibility(View.GONE);
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

	private RegionAdapter adapter;

	/**
	 * 获取明星老师
	 * 
	 * @param subjectId
	 * @param aId
	 */
	public void Start_Teacher(String subjectId, String aId) {
		currentSubjiect = subjectId;
		currentArea = aId;
		SKAsyncApiController.start_teather(subjectId, aId, page, new MyAsyncHttpResponseHandler(context, true) {
			public void onSuccess(String json) {
				super.onSuccess(json);
				ArrayList<Star_Teacher_Parse> list = SKResolveJsonUtil.getInstance().start_teather(json);
				mDataList.addAll(list);
				if (list.size() < 20) {
					starListView.setPullLoadEnable(false);
				} else {
					starListView.setPullLoadEnable(true);
				}
				if (mDataList.size() == 0) {
					Toast.makeText(context, "没有明星老师", Toast.LENGTH_SHORT).show();
				}
				adapter.notifyDataSetChanged();
				onLoad();
			};
		});
	}

	public void getMoreStarTeacher(String subjectId, String aId) {
		currentSubjiect = subjectId;
		currentArea = aId;
		SKAsyncApiController.start_teather(subjectId, aId, page, new MyAsyncHttpResponseHandler(context, false) {
			public void onSuccess(String json) {
				super.onSuccess(json);
				ArrayList<Star_Teacher_Parse> list = SKResolveJsonUtil.getInstance().start_teather(json);
				mDataList.addAll(list);
				if (list.size() < 20) {
					starListView.setPullLoadEnable(false);
				} else {
					starListView.setPullLoadEnable(true);
				}
				if (mDataList.size() == 0) {
					Toast.makeText(context, "没有明星老师", Toast.LENGTH_SHORT).show();
				}
				adapter.notifyDataSetChanged();
				onLoad();
			};
		});
	}

	public void refreshStarTeacher(String subjectId, String aId) {
		currentSubjiect = subjectId;
		currentArea = aId;
		SKAsyncApiController.start_teather(subjectId, aId, page, new MyAsyncHttpResponseHandler(context, false) {
			public void onSuccess(String json) {
				super.onSuccess(json);
				ArrayList<Star_Teacher_Parse> list = SKResolveJsonUtil.getInstance().start_teather(json);
				mDataList.addAll(list);
				if (list.size() < 20) {
					starListView.setPullLoadEnable(false);
				} else {
					starListView.setPullLoadEnable(true);
				}
				if (mDataList.size() == 0) {
					Toast.makeText(context, "没有明星老师", Toast.LENGTH_SHORT).show();
				}
				adapter.notifyDataSetChanged();
				onLoad();
			};
		});
	}

	@Override
	public void onResume() {
		super.onResume();
		if (adapter != null) {
			adapter.notifyDataSetChanged();
		}
	}
}