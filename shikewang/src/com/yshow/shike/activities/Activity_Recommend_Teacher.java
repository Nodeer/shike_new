package com.yshow.shike.activities;
import java.util.ArrayList;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.umeng.analytics.MobclickAgent;
import com.yshow.shike.R;
import com.yshow.shike.entity.SKTeacherOrSubject;
import com.yshow.shike.utils.MyAsyncHttpResponseHandler;
import com.yshow.shike.utils.SKAsyncApiController;
import com.yshow.shike.utils.SKResolveJsonUtil;

public class Activity_Recommend_Teacher extends Activity implements
		OnClickListener {
	public static int ISSUBJECT_CODE = 101;
	public static int ISTEACHER_CODE = 102;
	private ListView mListview;
	private SubjectAdapter subjectAdapter;
	private SKTeacherOrSubject item;
	public static String ISTEACHER = "isTeacher";
	public static String ISSUBJECT = "isSubject";
	private String select;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_subject);
		select = getIntent().getStringExtra("select");
		findViewById(R.id.ll_my_board_back).setOnClickListener(this);
		findViewById(R.id.ll_configm).setOnClickListener(this);
		mListview = (ListView) findViewById(R.id.listview);
		mListview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (select.equals(ISSUBJECT)) {
					item = subjectAdapter.getItem(position);
					Intent intent = getIntent();
					intent.putExtra("selectSubject", item);
					setResult(ISSUBJECT_CODE, intent);
					finish();
				}
				if (select.equals(ISTEACHER)) {
					item = subjectAdapter.getItem(position);
					Intent intent = getIntent();
					intent.putExtra("selectTeacher", item);
					setResult(ISTEACHER_CODE, intent);
					finish();
				}
			}
		});
		if (select.equals(ISSUBJECT)) {
			skGetSubject();
		}
		if (select.equals(ISTEACHER)) {
			skGetTeacher(getIntent().getStringExtra("subjectid"));
		}
	}

	private void skGetTeacher(final String subjectId) {
		SKAsyncApiController.skGetMyTeacher(new MyAsyncHttpResponseHandler(
				this, true) {
			@Override
			public void onSuccess(String arg0) {
				super.onSuccess(arg0);
				ArrayList<SKTeacherOrSubject> subjects = SKResolveJsonUtil.getInstance().resolveTeacher(arg0, subjectId);
				subjectAdapter = new SubjectAdapter(subjects,Activity_Recommend_Teacher.this);
				mListview.setAdapter(subjectAdapter);
				if (subjects.size() == 0) {
					AlertDialog.Builder dialog = new Builder(Activity_Recommend_Teacher.this);
					dialog.setTitle("提示");
					dialog.setMessage("当前没有该科目的老师");
					dialog.setPositiveButton("确定",
							new AlertDialog.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									finish();
								}
							});
					dialog.show();
				}
			}

		});
	}

	class SubjectAdapter extends BaseAdapter {
		ArrayList<SKTeacherOrSubject> subjects;
		Context context;

		protected SubjectAdapter(ArrayList<SKTeacherOrSubject> subjects,
				Context context) {
			this.context = context;
			this.subjects = subjects;
		}

		@Override
		public int getCount() {
			return subjects.size();
		}

		@Override
		public SKTeacherOrSubject getItem(int position) {
			return subjects.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			SKTeacherOrSubject skSubject = subjects.get(position);
			if (convertView == null) {
				convertView = View.inflate(context,R.layout.sh_select_subject_itme, null);
			}
			TextView subject_text = (TextView) convertView.findViewById(R.id.subject_text);
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
		case R.id.ll_configm:
			if (select.equals(ISSUBJECT)) {
				if (item == null) {
					Toast.makeText(this, "你还没选择学科呢！", Toast.LENGTH_LONG).show();
				}
				skGetSubject();
			}
			if (select.equals(ISTEACHER)) {
				if (item == null) {
					Toast.makeText(this, "你还没选择老师呢！", Toast.LENGTH_LONG).show();
				}
			}
			break;
		}
	}

	// 联网那一科目为条件的科目
	private void skGetSubject() {
		SKAsyncApiController.skGetSubject(new MyAsyncHttpResponseHandler(this,true) {
			@Override
			public void onSuccess(String arg0) {
				super.onSuccess(arg0);
				ArrayList<SKTeacherOrSubject> subjects = SKResolveJsonUtil.getInstance().resolveSubject(arg0);
				subjectAdapter = new SubjectAdapter(subjects,Activity_Recommend_Teacher.this);
				SKTeacherOrSubject skTeacherOrSubject = new SKTeacherOrSubject();
				skTeacherOrSubject.setName("不限");
				skTeacherOrSubject.setSubjectId("0");
				subjects.add(0, skTeacherOrSubject);
				subjectAdapter = new SubjectAdapter(subjects,Activity_Recommend_Teacher.this);
				mListview.setAdapter(subjectAdapter);
			}
		});
	}

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
