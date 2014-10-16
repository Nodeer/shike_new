package com.yshow.shike.fragments;
import java.util.List;
import com.yshow.shike.R;
import com.yshow.shike.entity.Count_Info;
import com.yshow.shike.utils.MyAsyncHttpResponseHandler;
import com.yshow.shike.utils.SKAsyncApiController;
import com.yshow.shike.utils.SKResolveJsonUtil;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class Fragment_Account_Prepraid extends Fragment {
	private ListView listview;
	private Context context;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		context = inflater.getContext();
		View view = inflater.inflate(R.layout.account_fragment_layout, null);
		listview = (ListView) view.findViewById(R.id.account_linearout);
		getData();
		return view;
	}

	class MyAdapter extends BaseAdapter {
		List<Count_Info> count_Info_pase;

		public MyAdapter(List<Count_Info> count_Info_pase) {
			super();
			this.count_Info_pase = count_Info_pase;
		}

		@Override
		public int getCount() {
			return count_Info_pase.size();
		}

		@Override
		public Object getItem(int arg0) {
			return count_Info_pase.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int arg0, View convertView, ViewGroup arg2) {
			Count_Info info = count_Info_pase.get(arg0);
			View view = View.inflate(context, R.layout.count_info_item, null);
			TextView tv_day = (TextView) view.findViewById(R.id.tv_day);
			TextView tv_time = (TextView) view.findViewById(R.id.tv_time);
			TextView tv_reg = (TextView) view.findViewById(R.id.tv_reg);
			tv_day.setText(info.getDates());
			tv_time.setText(info.getH() + ":" + info.getI());
			tv_reg.setText(info.getMessge());
			return view;
		}
	}

	public void getData() {
		SKAsyncApiController.inComeRecord(new MyAsyncHttpResponseHandler(
                context, true) {
            @Override
            public void onSuccess(String json) {
                super.onSuccess(json);
                boolean success = SKResolveJsonUtil.getInstance().resolveIsSuccess(json,
                        getActivity());
                if (success) {
                    List<Count_Info> count_Info_pase = SKResolveJsonUtil.getInstance()
                            .Count_Info_pase(json);
                    listview.setAdapter(new MyAdapter(count_Info_pase));
                }
            }
        });
	}
}
