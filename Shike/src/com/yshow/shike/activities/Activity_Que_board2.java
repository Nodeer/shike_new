package com.yshow.shike.activities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.umeng.analytics.MobclickAgent;
import com.yshow.shike.R;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yshow.shike.UIApplication;
import com.yshow.shike.customview.NoScrGridView;
import com.yshow.shike.entity.*;
import com.yshow.shike.utils.Dialog;
import com.yshow.shike.utils.MyAsyncHttpResponseHandler;
import com.yshow.shike.utils.Net_Servse;
import com.yshow.shike.utils.ProgressDialogUtil;
import com.yshow.shike.utils.SKAsyncApiController;
import com.yshow.shike.utils.SKResolveJsonUtil;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 老师拍照制作题目以后的标题和文件夹选择页面
 */
public class Activity_Que_board2 extends Activity implements OnClickListener {
	private boolean flag = false; // 编辑文件标记
	private Context context;
	// private Bitmap bitmap;
    /**题目使用的文件夹名字*/
	private String name;
	private EditText et_title;
	private TextView tv_select_file;
	private DisplayImageOptions options;
	private ImageLoader imageLoader;
	private SKMessage sKMessage;
    /**题目使用的文件夹id*/
	private String file_cid;
	private MyAdapter myAdapter;// UIA适配器
	@SuppressWarnings("rawtypes")
	private List UIA_list;
	private ProgressDialogUtil progress;
	private List<?> bank_list;

    /**这个是保存题库的信息*/
    private Question_Bank bank;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_que_board2);
		context = this;
		progress = new ProgressDialogUtil(context);
		initData();
        bank = Activity_File_Three.mSaveTiku;
        if(bank!=null){
            tv_select_file.setText(bank.getCategory1());
            et_title.setText(bank.getName());
            file_cid = bank.getCid();//文件夹id
        }
	}

	private void initData() {
		bank_list = ((UIApplication) UIApplication.getInstance()).getList();
		options = Net_Servse.getInstence().Picture_Shipei(R.drawable.back);
		imageLoader = ImageLoader.getInstance();
		tv_select_file = (TextView) findViewById(R.id.tv_select_file);//文件夹
		TextView tv_edit = (TextView) findViewById(R.id.tv_edit);
		NoScrGridView gr_que_board = (NoScrGridView) findViewById(R.id.gr_que_board2);
		Bundle bundle = getIntent().getExtras();
		sKMessage = (SKMessage) bundle.getSerializable("sKMessage");
		if (sKMessage != null) {
			tv_edit.setVisibility(View.GONE);
			ArrayList<SkMessage_Res> bitmaplist = sKMessage.getRes();
			gr_que_board.setAdapter(new sKMessageAdapter(bitmaplist));
		} else {
			myAdapter = new MyAdapter(false);
			gr_que_board.setAdapter(myAdapter);
		}
		et_title = (EditText) findViewById(R.id.et_title);//标题
		tv_select_file.setOnClickListener(this);
		findViewById(R.id.tv_back).setOnClickListener(this);
		tv_edit.setOnClickListener(this);
		findViewById(R.id.tv_save_tiku).setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.tv_back:
			Back_Control();
			break;
		// 对文件进行编辑
		case R.id.tv_edit:
			myAdapter.notifyDataSetChanged();
			flag = !flag;
			break;
		case R.id.tv_save_tiku:
			if (TextUtils.isEmpty(et_title.getText())) {
				Toast.makeText(context, "请先输入标题", Toast.LENGTH_SHORT).show();
			} else if (TextUtils.isEmpty(tv_select_file.getText())) {
				Toast.makeText(context, "请选择文件夹", Toast.LENGTH_SHORT).show();
			} else {
				if (sKMessage != null) {//从题目中直接保存过来的
					// 根据科目id生成问题id
					String questionId = sKMessage.getQuestionId();
					Mess_Save(file_cid, et_title.getText().toString(), questionId);
				} else {
                    if(bank!=null){
                        addTikuImg();
                    }else{
                        String uid = LoginManage.getInstance().getStudent().getUid();
                        Tea_Info(uid);
                    }
                }
            }
			break;
		case R.id.tv_select_file:
			Intent intent = new Intent(context, Activity_Board_File.class);
			startActivityForResult(intent, 0);
			break;
		}
	}

	class MyAdapter extends BaseAdapter {
		public MyAdapter(Boolean flag) {
			super();
			UIA_list = ((UIApplication) UIApplication.getInstance()).getList();
		}

		@Override
		public int getCount() {
			if (flag) {
				return UIA_list.size() + 1;
			} else {
				return UIA_list.size();
			}
		}

		@Override
		public Object getItem(int arg0) {
			return UIA_list.get(arg0);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			View view = View.inflate(context, R.layout.activity_que_board2_item1, null);
			ImageView im_que2_item = (ImageView) view.findViewById(R.id.im_que2_item1);
			final ImageView iv_stu_dele = (ImageView) view.findViewById(R.id.iv_stu_dele);
			if (flag) {
				// 删除按钮的设置
				iv_stu_dele.setVisibility(View.VISIBLE);
				iv_stu_dele.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						UIA_list.remove(position);
						myAdapter.notifyDataSetChanged();
					}
				});
			} else {
				iv_stu_dele.setVisibility(View.GONE);
			}
			if (UIA_list.size() == position) {
				im_que2_item.setImageResource(R.drawable.select_file);
				iv_stu_dele.setVisibility(View.GONE);
				// 文件添加点击事件
				im_que2_item.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						Dialog.Intent(context, Activity_Tea_Tool_Sele.class);
					}
				});
			} else {
				Bitmap object = (Bitmap) UIA_list.get(position);
				im_que2_item.setImageBitmap(object);
			}
			return view;
		}
	}

	// 消息条目过来的数据适配器 Adapter
	class sKMessageAdapter extends BaseAdapter {
		ArrayList<SkMessage_Res> list;

		public sKMessageAdapter(ArrayList<SkMessage_Res> list) {
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
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			String file = list.get(position).getFile();
			View view = View.inflate(context, R.layout.activity_que_board2_item, null);
			ImageView im_que2_item = (ImageView) view.findViewById(R.id.im_que2_item);
			imageLoader.displayImage(file, im_que2_item, options);
			return view;
		}
	}

	// 访问接口 保存题库
	private void Mess_Save(String cid, String name, String questionId) {
		SKAsyncApiController.Save_TiKu(cid, name, questionId, new MyAsyncHttpResponseHandler(this, true) {
			@Override
			public void onSuccess(String json) {
				super.onSuccess(json);
				boolean success = SKResolveJsonUtil.getInstance().resolveIsSuccess(json, context);
				if (success) {
					bank_list.clear();
					Toast.makeText(context, "更新题库成功", Toast.LENGTH_SHORT).show();
					Dialog.Intent(context, Teather_Main_Activity.class);
				}
			}
		});
	}

	// 获取文件夹名称和cid
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 1) {
			name = data.getStringExtra("file_name");
			file_cid = data.getStringExtra("file_cid");
			tv_select_file.setText(name);
		}
	}

	// 返回按钮提示框
	private void Back_Control() {
		Builder builder = new Builder(context);
		builder.setTitle("     提示");
		builder.setMessage("确定放弃本次题目制作吗？");
		builder.setPositiveButton("放弃", new android.content.DialogInterface.OnClickListener() {
			public void onClick(DialogInterface arg0, int arg1) {
				bank_list.clear();
				finish();
			}
		});
		builder.setNegativeButton("取消", null);
		builder.show();
	}

	// 根据用户uid 生成科目id
	private void Tea_Info(String uid) {
		SKAsyncApiController.User_Info(uid, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String json) {
				super.onSuccess(json);
				boolean sub_id_success = SKResolveJsonUtil.getInstance().resolveIsSuccess(json, context);
				if (sub_id_success) {
					progress.show();
					User_Info my_teather = SKResolveJsonUtil.getInstance().My_teather(json);
					String subjectid = my_teather.getSubjectid();
					if (subjectid == null) {
						Toast.makeText(context, "请选择学科", Toast.LENGTH_SHORT).show();
					} else {
						Creat_questionid(subjectid);
					}
				}
			}
		});
	}

	// 有科目id生成问题id
	private void Creat_questionid(String subjectid) {
		SKAsyncApiController.skCreateQuestion(subjectid, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String json) {
				super.onSuccess(json);
				boolean ques_id_success = SKResolveJsonUtil.getInstance().resolveIsSuccess(json, context);
				if (ques_id_success) {
					Ceater_Question Questionid = SKResolveJsonUtil.getInstance().Pase_Questionid(json);
					String questionid2 = Questionid.getQuestionid();
					for (int i = 0; i < UIA_list.size(); i++) {
						Bitmap up_bitmap = (Bitmap) UIA_list.get(i);
						Up_Image(questionid2, up_bitmap);
						Toast.makeText(context, "上传图片" + (i + 1), Toast.LENGTH_SHORT).show();
					}
					edit_save(file_cid, et_title.getText().toString(), questionid2);
				}
			}
		});
	}

    private void addTikuImg(){
        String questionid2 = bank.getQuestion();
        for (int i = 0; i < UIA_list.size(); i++) {
            Bitmap up_bitmap = (Bitmap) UIA_list.get(i);
            Up_Image(questionid2, up_bitmap);
            Toast.makeText(context, "上传图片" + (i + 1), Toast.LENGTH_SHORT).show();
        }
        edit_save(file_cid, et_title.getText().toString(), questionid2);
    }

	// 有问题id 上传图片
	private void Up_Image(final String questionId, Bitmap bitmap) {
		SKAsyncApiController.skUploadImage(questionId, bitmap, new MyAsyncHttpResponseHandler(context, true) {
			@Override
			public void onSuccess(String json) {
				super.onSuccess(json);
				if (progress != null) {
					progress.dismiss();
				}
			}
		});
	}

	private void edit_save(String cid, String name, String questionId) {
		SKAsyncApiController.Mess_Save(cid, name, questionId, new MyAsyncHttpResponseHandler(this, true) {
			@Override
			public void onSuccess(String json) {
				super.onSuccess(json);
				boolean success = SKResolveJsonUtil.getInstance().resolveIsSuccess(json, context);
				if (success) {
					bank_list.clear();
					UIApplication.getInstance().getPicUrls().clear();
					Toast.makeText(context, "更新题库成功", Toast.LENGTH_SHORT).show();
					Dialog.Intent(context, Teather_Main_Activity.class);
				}
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
