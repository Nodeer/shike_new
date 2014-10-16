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
import com.yshow.shike.utils.MyAsyncHttpResponseHandler;
import com.yshow.shike.utils.SKAsyncApiController;
import com.yshow.shike.utils.SKResolveJsonUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
public class Fragment_Search_Teacher extends Fragment implements OnClickListener {
	private EditText user_phon;
	private String pho_Num,nickname,school;  // 获取文本框电话号码
	private ListView list_view; // 数据适配器
	private MyAdapter adapter;
	private LinearLayout phon_name; // 精确搜索
	private RelativeLayout tiaojian_sether; //  条件搜索
	private TextView precise_seather,option_seather; //精确搜索 ,选项搜索,
	private TextView tvSubject,tv_eare;
	private String subject_id,eare_id; //学科id 地区id
	private EditText et_user_name,et_school;//用户名 学校名
	private RadioButton sex_men,sex_miss;
	private Context context;
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				if(phon_name.getVisibility() == View.VISIBLE){
					tiaojian_sether.setVisibility(View.GONE);
					phon_name.setVisibility(View.VISIBLE);
					pho_Num = user_phon.getText().toString().trim();
					Searth_Teather_Mob(pho_Num);
				}else {
					nickname = et_user_name.getText().toString().trim();
					school = et_school.getText().toString().trim();
					phon_name.setVisibility(View.GONE);
					if((sex_men.isChecked()&&sex_miss.isChecked())||(!sex_men.isChecked()&&!sex_miss.isChecked())){
						Searth_Teather_TiaoJian(nickname,school,"-1");
					}else if (sex_miss.isChecked()) {
						Searth_Teather_TiaoJian(nickname,school,"1");
					}else {
						Searth_Teather_TiaoJian(nickname,school,"0");
					}
				}
				break;
			}
		};
	};
	public Handler getHandler() {
		return handler;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		context = inflater.getContext();
		View view = inflater.inflate(R.layout.fragment_search_teacher, null);
		view.findViewById(R.id.option_seather).setOnClickListener(this);
		view.findViewById(R.id.precise_seather).setOnClickListener(this);
		user_phon = (EditText) view.findViewById(R.id.et_user_phon);
		list_view = (ListView) view.findViewById(R.id.lv_list_view);
		phon_name = (LinearLayout) view.findViewById(R.id.LinearLayout);
		tiaojian_sether = (RelativeLayout) view.findViewById(R.id.rl_tiaojian_sether);
		precise_seather = (TextView) view.findViewById(R.id.precise_seather);
		option_seather = (TextView) view.findViewById(R.id.option_seather);
		et_user_name = (EditText) view.findViewById(R.id.et_user_name);
		et_school = (EditText) view.findViewById(R.id.et_school);
		tvSubject = (TextView) view.findViewById(R.id.tv_shool);
		tv_eare = (TextView) view.findViewById(R.id.tv_eare);
		sex_men = (RadioButton) view.findViewById(R.id.rb_sex_men);
		sex_miss = (RadioButton) view.findViewById(R.id.rb_sex_miss);
		tvSubject.setOnClickListener(this);
		tv_eare.setOnClickListener(this);
		list_view.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Star_Teacher_Parse item = (Star_Teacher_Parse) adapter.getItem(arg2);
				Intent intent = new Intent(getActivity(),Activity_Teacher_Info.class);
				intent.putExtra("headpicture", item);
				getActivity().startActivity(intent);
			}
		});
		return view;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 选项搜索
		case R.id.option_seather:
			tiaojian_sether.setVisibility(View.VISIBLE);
			phon_name.setVisibility(View.GONE);
			precise_seather.setTextColor(context.getResources().getColor(R.color.sk_student_main_color));
			option_seather.setBackgroundResource(R.color.sk_student_main_color);
			precise_seather.setBackgroundResource(R.color.button_typeface_color);
			option_seather.setTextColor(context.getResources().getColor(R.color.button_typeface_color));
			break;
		// 精确搜索
		case R.id.precise_seather:
			tiaojian_sether.setVisibility(View.GONE);
			phon_name.setVisibility(View.VISIBLE);
			option_seather.setTextColor(context.getResources().getColor(R.color.sk_student_main_color));
			precise_seather.setBackgroundResource(R.color.sk_student_main_color);
			option_seather.setBackgroundResource(R.color.button_typeface_color);
			precise_seather.setTextColor(context.getResources().getColor(R.color.button_typeface_color));
			break;
		// 学校按钮
		case R.id.tv_shool:
			Intent intent = new Intent(getActivity(),Activity_Recommend_Teacher.class);
			intent.putExtra("select", Activity_Recommend_Teacher.ISSUBJECT);
			startActivityForResult(intent,Activity_Recommend_Teacher.ISSUBJECT_CODE);
			break;
		// 地区按钮
		case R.id.tv_eare:
			Intent regionintent = new Intent(getActivity(),Fragment_Region.class);
			regionintent.putExtra("select",Activity_Recommend_Teacher.ISSUBJECT);
			startActivityForResult(regionintent, Fragment_Region.ISSUBJECT_CODE);
			break;
		}
	}

	/**
	 *  电话搜索老师
	 */
	private void Searth_Teather_Mob(String user_name) {
		SKAsyncApiController.Searth_Teather_Mob(user_name,
				new MyAsyncHttpResponseHandler(getActivity(), true) {
					@Override
					public void onSuccess(String json) {
						super.onSuccess(json);
						boolean success = SKResolveJsonUtil.getInstance().resolveIsSuccess(json, getActivity());
						if (success) {
							ArrayList<Star_Teacher_Parse> list = SKResolveJsonUtil.getInstance().Search_Terms(json);
							adapter = new MyAdapter(list);
							list_view.setAdapter(adapter);
						}
					}
				});
	}
	// 条件搜索
	private void Searth_Teather_TiaoJian(String nickname,String school,String sex) {
		SKAsyncApiController.Searth_Teather_TiaoJian(nickname, school, subject_id,
				eare_id, sex, new MyAsyncHttpResponseHandler(context, true) {
					@Override
					public void onSuccess(String json) {
						super.onSuccess(json);
						SKResolveJsonUtil.getInstance().resolveIsSuccess(json, getActivity());
						ArrayList<Star_Teacher_Parse> list = SKResolveJsonUtil.getInstance().Search_Terms(json);
						adapter = new MyAdapter(list);
						list_view.setAdapter(adapter);
					}
				});
	}

	// listview 适配器
	class MyAdapter extends BaseAdapter {
		ArrayList<Star_Teacher_Parse> list;
		public MyAdapter(ArrayList<Star_Teacher_Parse> list) {
			super();
			this.list = list;
		}
		@Override
		public int getCount() {
			return list.size();
		}
		@Override
		public Object getItem(int arg0) {
			return list.get(arg0);
		}
		@Override
		public long getItemId(int arg0) {
			return 0;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Star_Teacher_Parse teacher_Parse = list.get(position);
			DisplayImageOptions options = new DisplayImageOptions.Builder()
					.cacheOnDisc(true)
					.showImageForEmptyUri(R.drawable.my_tea_phon)
					.showImageOnFail(R.drawable.my_tea_phon)
					.cacheInMemory(true).build();
			ImageLoader imageLoader = ImageLoader.getInstance();
			if (convertView == null) {
				convertView = View.inflate(getActivity(),
						R.layout.fragment_start_text, null);
			}
			ImageView teather_picture = (ImageView) convertView
					.findViewById(R.id.iv_teather_picture);
			TextView tv_nicheng = (TextView) convertView
					.findViewById(R.id.tv_nicheng);
			TextView tv_subject = (TextView) convertView
					.findViewById(R.id.tv_subject);
			TextView tv_grade = (TextView) convertView
					.findViewById(R.id.tv_grade);
			TextView tv_diqu = (TextView) convertView
					.findViewById(R.id.tv_diqu);
			TextView tv_gerenxinxi = (TextView) convertView
					.findViewById(R.id.tv_gerenxinxi);
			ImageView iv_teather_sele = (ImageView) convertView
					.findViewById(R.id.iv_teather_sele);
			if (teacher_Parse.getiSmyteath().equals("1")) {
				iv_teather_sele.setVisibility(View.VISIBLE);
				imageLoader.displayImage(teacher_Parse.getIcon(),
						teather_picture, options);
			} else {
				iv_teather_sele.setVisibility(View.GONE);
				imageLoader.displayImage(teacher_Parse.getIcon(),
						teather_picture, options);
			}
			tv_nicheng.setText(teacher_Parse.getNickname());
			tv_subject.setText(teacher_Parse.getSubiect());
			tv_grade.setText(teacher_Parse.getGrade());
			tv_diqu.setText(teacher_Parse.getArea());
			tv_gerenxinxi.setText(teacher_Parse.getInfo());
			return convertView;
		}
	}
	/**
	 * 数据回显
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity_Recommend_Teacher.ISSUBJECT_CODE) {
			SKTeacherOrSubject item = (SKTeacherOrSubject) data.getSerializableExtra("selectSubject");
			if (item != null) {
				subject_id = item.getSubjectId();
				tvSubject.setText(item.getName());
			}
		}
		if (resultCode == Fragment_Region.DIQU) {
			SKArea diqu = (SKArea) data.getSerializableExtra("0");
			if (diqu != null) {
				eare_id = diqu.getId();
				tv_eare.setText(diqu.getName());
			}
		}
	}
	@Override
	public void onResume() {
		super.onResume();
		if(adapter != null){
			adapter.notifyDataSetChanged();
		}
	}
}
