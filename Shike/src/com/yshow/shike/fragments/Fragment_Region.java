package com.yshow.shike.fragments;

import java.util.ArrayList;
import com.yshow.shike.R;
import com.yshow.shike.entity.SKArea;
import com.yshow.shike.utils.MyAsyncHttpResponseHandler;
import com.yshow.shike.utils.SKAsyncApiController;
import com.yshow.shike.utils.SKResolveJsonUtil;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class Fragment_Region extends Activity implements OnClickListener {
	public static int ISSUBJECT_CODE = 101;
	public static int ISTEACHER_CODE = 102;
	public static int DIQU = 103;
	private ListView mListview;
	public static String ISTEACHER = "isTeacher";
	public static String ISSUBJECT = "isSubject";
	private RegionAdapter adapter;
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_region);
		context = this;
		mListview = (ListView) findViewById(R.id.lv_listview);
		skGetArea();
		mListview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
				SKArea item = adapter.getItem(position);
				Intent intent = getIntent();
				Log.e("item", item.toString());
				intent.putExtra("0", item);
				setResult(DIQU, intent);
				finish();
			}
		});
		findViewById(R.id.ll_my_board_back).setOnClickListener(this);
	}

	private void skGetArea() {
		SKAsyncApiController.skGetArea(new MyAsyncHttpResponseHandler(context, true) {
			@Override
			public void onSuccess(String arg0) {
				super.onSuccess(arg0);
				ArrayList<SKArea> resolveArea = SKResolveJsonUtil.getInstance().resolveArea(arg0);
				SKArea skArea = new SKArea();
				skArea.setId("0");
				skArea.setName("不限");
				resolveArea.add(0, skArea);
				adapter = new RegionAdapter(resolveArea, context);
				mListview.setAdapter(adapter);
			}
		});
	}

	class RegionAdapter extends BaseAdapter {
		ArrayList<SKArea> subjects;
		Context context;

		public RegionAdapter(ArrayList<SKArea> subjects, Context context) {
			super();
			this.subjects = subjects;
			this.context = context;
		}

		@Override
		public int getCount() {
			return subjects.size();
		}

		@Override
		public SKArea getItem(int position) {
			return subjects.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			SKArea skSubject = subjects.get(position);
			if (convertView == null) {
				convertView = View.inflate(context, R.layout.fragment_regiog_text, null);
			}
			TextView subject_text = (TextView) convertView.findViewById(R.id.subject_text1);
			subject_text.setText(skSubject.getName());
			return convertView;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_my_board_back:
			finish();
			break;
		}
	}
}
