package com.yshow.shike.fragments;
import java.util.ArrayList;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yshow.shike.R;
import com.yshow.shike.activities.Activity_TiKu_second;
import com.yshow.shike.entity.Question_Bank;
import com.yshow.shike.utils.Delete_Ques_Utile;
import com.yshow.shike.utils.Dialog;
import com.yshow.shike.utils.MyAsyncHttpResponseHandler;
import com.yshow.shike.utils.MyCustomDialog;
import com.yshow.shike.utils.Net_Servse;
import com.yshow.shike.utils.PartnerConfig;
import com.yshow.shike.utils.SKAsyncApiController;
import com.yshow.shike.utils.SKResolveJsonUtil;
import com.yshow.shike.utils.ShareDialog;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
/**
 * 老师端题库按文件分类
 * @author Administrator
 * 
 */
public class Tea_Tiku_Files extends Fragment {
	private ArrayList<ArrayList<Question_Bank>> files;// 定义一个大集合 用来添加各个文件夹
	private static MyAdapter filedapter; // 自定义的一个BaseAdapter 用来对文件分类数据适配
	private static boolean flag = false;
	private static GridView gr_file_count;
	private String question_into;
	private DisplayImageOptions options;
	private ImageLoader imageLoader;
	private Context context;
	private ArrayList<Question_Bank> arrayList;

	public static Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			// 编辑按钮控制
			case 0:
				gr_file_count.setAdapter(filedapter);
				flag =! flag;
				break;
			}
		};
	};
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		context = getActivity();
		View view = View.inflate(context, R.layout.activity_file_item,null);
		options = Net_Servse.getInstence().Picture_Shipei(R.drawable.back);
		imageLoader = ImageLoader.getInstance();
		question_into = getActivity().getIntent().getStringExtra("question_into");
		gr_file_count = (GridView) view.findViewById(R.id.gr_file1_count);
		Look_File_TiKu(false);
		return view;
	}
	public void onResume() {
		super.onResume();
		if(PartnerConfig.clickPosition != -1){
			Look_File_TiKu(false);
		}
	};
	// 按文件夾 搜索
	class MyAdapter extends BaseAdapter {
		ArrayList<ArrayList<Question_Bank>> count_Pase;
		public MyAdapter(ArrayList<ArrayList<Question_Bank>> count_Pase,Boolean flag) {
			super();
			this.count_Pase = count_Pase;
		}
		@Override
		public int getCount() {
			if (flag) {
				return count_Pase.size() + 1;
			} else {
				return count_Pase.size();
			}
		}
		@Override
		public Object getItem(int arg0) {
			return count_Pase.get(arg0);
		}
		@Override
		public long getItemId(int arg0) {
			return arg0;
		}
		@Override
		public View getView(final int position, View convertView, ViewGroup arg2) {
			if (convertView == null) {
				convertView = View.inflate(context,R.layout.gridview_file_item, null);
			}
			RelativeLayout iv_point_file = (RelativeLayout) convertView.findViewById(R.id.iv_point_file);
			ImageView iv_file = (ImageView) convertView.findViewById(R.id.iv_sing_file);
			TextView file_name = (TextView) convertView.findViewById(R.id.file_name);
			LinearLayout ll_file = (LinearLayout) convertView.findViewById(R.id.ll_file);
			ImageView iv_tea_delete = (ImageView) convertView.findViewById(R.id.iv_tea_delete);
			if (count_Pase.size() == position) {
				iv_point_file.setVisibility(View.VISIBLE);
				ll_file.setVisibility(View.GONE);
				iv_point_file.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						MyCustomDialog dialog = new MyCustomDialog(context,
								new MyCustomDialog.OnCustomDialogListener() {
									@Override
									public void back(String fileName) {
										ShareDialog.creat_new_file(fileName,filedapter, gr_file_count,files, context);
									}
								});
						dialog.show();
					}
				});
			} else {
				iv_point_file.setVisibility(View.GONE);
				final ArrayList<Question_Bank> arrayList = count_Pase.get(position);
				final Question_Bank question_Bank = arrayList.get(0);
				final String cid = question_Bank.getCid();
				if (flag) {
					if( ! question_Bank.getCategory1().equals("false")){
						iv_tea_delete.setVisibility(View.VISIBLE);
						// 按文件形式 删除文件夹
						iv_tea_delete.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View arg0) {
								Delete_Ques_Utile.getIntence().Delete_Classify_File(count_Pase, cid, position,filedapter, context);
							}
						});
					}
				} else {
					iv_tea_delete.setVisibility(View.GONE);
				}
				if (question_Bank.getCategory1().equals("false")) {
					file_name.setText("未分类" + "("+ count_Pase.get(position).size() + ")");
				} else {
					file_name.setText(question_Bank.getCategory1() + "("+ count_Pase.get(position).size() + ")");
				}
				if (question_Bank.getRes() != null) {
					imageLoader.displayImage(question_Bank.getRes().get(0).getFile(), iv_file, options);
				}
				ll_file.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						PartnerConfig.clickPosition = position;
						Bundle bundle = new Bundle();
						bundle.putSerializable("TiMu_Bank", arrayList);
						bundle.putString("question_into", question_into);
						Dialog.intent(context,Activity_TiKu_second.class, bundle);
					}
				});
			}
			return convertView;
		}
	}
	// 以文件方式查看题库
	private void Look_File_TiKu(final Boolean flag) {
		SKAsyncApiController.Look_TiKu(new MyAsyncHttpResponseHandler(context,true) {
			@Override
			public void onSuccess(String json) {
				super.onSuccess(json);
				boolean IsSuccess = SKResolveJsonUtil.getInstance().resolveIsSuccess(json,context);
				if (IsSuccess) {
					files = new ArrayList<ArrayList<Question_Bank>>();
					arrayList = SKResolveJsonUtil.getInstance().Count_Pase(json);
					String category = "";
					ArrayList<Question_Bank> curcategorys = null;
					for (Question_Bank question_Bank : arrayList) {
						boolean isExist = false;
						String curcategory = question_Bank.getCategory1();
						for (ArrayList<Question_Bank> file : files) {
							String category1 = file.get(0).getCategory1();
							if (curcategory.equals(category1)) {
								curcategorys = file;
								isExist = true;
								break;
							}
						}
						if (!isExist && !category.equals(curcategory)) {
							curcategorys = new ArrayList<Question_Bank>();
							category = curcategory;
							files.add(curcategorys);
						}
						curcategorys.add(question_Bank);
					}
					filedapter = new MyAdapter(files, flag);
					gr_file_count.setAdapter(filedapter);
				}
			}
		});
	}
}
