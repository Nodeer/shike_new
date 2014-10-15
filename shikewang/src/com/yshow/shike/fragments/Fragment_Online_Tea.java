package com.yshow.shike.fragments;

import java.util.ArrayList;
import java.util.List;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yshow.shike.R;
import com.yshow.shike.activities.Activity_Teacher_Info;
import com.yshow.shike.entity.Star_Teacher_Parse;
import com.yshow.shike.utils.*;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
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

public class Fragment_Online_Tea extends Fragment implements TopAndBottomLoadListView.OnRefreshListener, XListView.IXListViewListener {
	private XListView mOnlineListView;
	private DisplayImageOptions options;
	private ImageLoader imageLoader;
	private MyAdapter adapter;
	private int page = 1;
	private ArrayList<Star_Teacher_Parse> mDataList = new ArrayList<Star_Teacher_Parse>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = View.inflate(getActivity(), R.layout.online_tea_listview, null);
		mOnlineListView = (XListView) view.findViewById(R.id.online_lv);
		options = Net_Servse.getInstence().Picture_Shipei(R.drawable.my_tea_phon);
		imageLoader = ImageLoader.getInstance();
		adapter = new MyAdapter();
		mOnlineListView.setAdapter(adapter);
		mOnlineListView.setXListViewListener(this);
		Seather_Tea();
		mOnlineListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                if(arg3 == -1) {
                    // 点击的是headerView或者footerView
                    return;
                }
                int realPosition=(int)arg3;
				Star_Teacher_Parse item = (Star_Teacher_Parse) adapter.getItem(realPosition);
				Bundle bundle = new Bundle();
				bundle.putSerializable("headpicture", item);
				Dialog.intent(getActivity(), Activity_Teacher_Info.class, bundle);

			}
		});
		return view;
	}

	@Override
	public void onLoadMore() {
		page++;
		Seather_Tea();
	}

	@Override
	public void onRefresh() {
		page = 1;
		mDataList.clear();
		Seather_Tea();
	}

	private class MyAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return mDataList.size();
		}

		@Override
		public Object getItem(int arg0) {
			return mDataList.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int arg0, View convertView, ViewGroup arg2) {
			final Star_Teacher_Parse on_Tea = mDataList.get(arg0);
			if (convertView == null) {
				convertView = View.inflate(getActivity(), R.layout.fragment_start_text, null);
			}
			TextView tv_grade = (TextView) convertView.findViewById(R.id.tv_grade);
			tv_grade.setVisibility(View.INVISIBLE);
			ImageView tea_piture = (ImageView) convertView.findViewById(R.id.iv_teather_picture);
			TextView tea_name = (TextView) convertView.findViewById(R.id.tv_nicheng);
			TextView tea_subject = (TextView) convertView.findViewById(R.id.tv_subject);
			TextView jieduan = (TextView) convertView.findViewById(R.id.tv_diqu);
			TextView tea_info = (TextView) convertView.findViewById(R.id.tv_gerenxinxi);
			if (on_Tea != null) {
				tea_name.setText(on_Tea.getNickname());
				tea_subject.setText(on_Tea.getSubiect());
				jieduan.setText(on_Tea.getGrade());
				tea_info.setText(on_Tea.getInfo());
				imageLoader.displayImage(on_Tea.getIcon(), tea_piture, options);
			}
			return convertView;
		}
	}

	private void Seather_Tea() {
		SKAsyncApiController.OnLine_Tea(page, "0", new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String json) {
				super.onSuccess(json);
				boolean success = SKResolveJsonUtil.getInstance().resolveIsSuccess(json, getActivity());
				if (success) {
					ArrayList<Star_Teacher_Parse> list = SKResolveJsonUtil.getInstance().start_teather(json);
					mDataList.addAll(list);
                    if (list.size() < 20) {
                        mOnlineListView.setPullLoadEnable(false);
                    } else {
                        mOnlineListView.setPullLoadEnable(true);
                    }
					if (mDataList.size() == 0) {
						Toast.makeText(getActivity(), "没有在线老师", Toast.LENGTH_SHORT).show();
					}
					adapter.notifyDataSetChanged();
                    onLoad();
				}
			}
		});
	}

    private void onLoad() {
        mOnlineListView.stopRefresh();
        mOnlineListView.stopLoadMore();
        mOnlineListView.setRefreshTime(Timer_Uils.getCurrentTime());
    }

	@Override
	public void onResume() {
		super.onResume();
		if (adapter != null) {
			adapter.notifyDataSetChanged();
		}
	}
}