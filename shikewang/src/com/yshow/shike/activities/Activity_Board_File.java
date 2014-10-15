package com.yshow.shike.activities;
import java.util.List;

import com.umeng.analytics.MobclickAgent;
import com.yshow.shike.R;
import com.yshow.shike.entity.File_Paser;
import com.yshow.shike.utils.MyAsyncHttpResponseHandler;
import com.yshow.shike.utils.MyCustomDialog;
import com.yshow.shike.utils.SKAsyncApiController;
import com.yshow.shike.utils.SKResolveJsonUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
public class Activity_Board_File extends Activity {
	private GridView gv_file;
	private Context context;
	private MyFileAdapter fileAdapter;
//	private File_Paser file_Paser;
	private List<File_Paser> paser_File ;
//	private File_Paser paser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_board_file);
    	context = this;
//    	paser = new File_Paser();
    	initData();
    }
	private void initData() {
		Acquire_Files();
		gv_file = (GridView) findViewById(R.id.gv_file);
		findViewById(R.id.tv_file_back).setOnClickListener(listener);
	}
	private OnClickListener listener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.tv_file_back:
				finish();
				break;
			}
		}
	};
	class MyFileAdapter extends BaseAdapter{
		List<File_Paser> list_file;
		public MyFileAdapter(List<File_Paser> list_file) {
			super();
			this.list_file = list_file;
		}
		@Override
		public int getCount() {
			return list_file.size()+1;
		}
		public void addFiles(File_Paser itme) {
				list_file.add(itme);
				this.notifyDataSetChanged();
		}
		@Override
		public Object getItem(int position) {
			return list_file.get(position);
		}
		@Override
		public long getItemId(int position) {
			return position;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView == null){
				convertView = View.inflate(context, R.layout.gridview_file_item, null);
			}
				TextView file_name = (TextView) convertView.findViewById(R.id.file_name);
				ImageView sing_file = (ImageView) convertView.findViewById(R.id.iv_sing_file);
				LinearLayout ll_file = (LinearLayout) convertView.findViewById(R.id.ll_file);
				RelativeLayout iv_point_file = (RelativeLayout) convertView.findViewById(R.id.iv_point_file);
					if(list_file.size() == position){
						iv_point_file.setVisibility(View.VISIBLE);
						ll_file.setVisibility(View.GONE);
						iv_point_file.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								MyCustomDialog dialog = new MyCustomDialog(context, new MyCustomDialog.OnCustomDialogListener() {
									@Override
									public void back(String fileName) {
										Creat_File(fileName);
									}
								});
								dialog.show();
							}
						});
					}else {
//						file_Paser = list_file.get(position);
						final File_Paser paser = list_file.get(position);
						ll_file.setVisibility(View.VISIBLE);
						iv_point_file.setVisibility(View.GONE);
						file_name.setText(paser.getName());
						sing_file.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View arg0) {
								Intent intent = getIntent();
								intent.putExtra("file_name", paser.getName());
								intent.putExtra("file_cid", paser.getCid());
								setResult(1, intent);
								finish();
							}
						});
					}
			return convertView;
		}
	}
//	获得所有文件文件
	private void Acquire_Files() {
		SKAsyncApiController.Acquire_File(new MyAsyncHttpResponseHandler(this,true){
			@Override
			public void onSuccess(String json) {
				super.onSuccess(json);
				paser_File = SKResolveJsonUtil.getInstance().Paser_File(json);
				fileAdapter = new MyFileAdapter(paser_File);
				gv_file.setAdapter(fileAdapter);
			}
		});
	}
//	以文件的形式添加一個文件夾 并请求网络
	private void Creat_File(final String fileName ) {
        SKAsyncApiController.Creat_File(fileName, new MyAsyncHttpResponseHandler(this,true){
        	@Override
        	public void onSuccess(String json) {
        		super.onSuccess(json);
        		boolean success = SKResolveJsonUtil.getInstance().resolveIsSuccess(json, context);
        		if(success){
//        			if(file_Paser != null){
//        				paser.setCid(file_Paser.getCid());
//        				paser.setId(file_Paser.getId());
//        				paser.setName(fileName);
//        				paser.setUid(file_Paser.getUid());
//        				fileAdapter.addFiles(paser);
//        			}
        			fileAdapter.addFiles(SKResolveJsonUtil.getInstance().Paser_File2(json));
        		}
        	}
        });
	}

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
