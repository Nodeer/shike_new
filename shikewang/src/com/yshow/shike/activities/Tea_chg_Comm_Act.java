package com.yshow.shike.activities;

import java.lang.reflect.Type;
import java.util.List;

import android.app.AlertDialog;
import android.text.TextUtils;
import android.widget.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;
import com.yshow.shike.R;
import com.yshow.shike.customview.CustomListView;
import com.yshow.shike.customview.CustomListView.OnRefreshListener;
import com.yshow.shike.entity.LoginManage;
import com.yshow.shike.entity.User_Info;
import com.yshow.shike.utils.Duihuan_Goods;
import com.yshow.shike.utils.MyAsyncHttpResponseHandler;
import com.yshow.shike.utils.Net_Servse;
import com.yshow.shike.utils.SKAsyncApiController;
import com.yshow.shike.utils.SKResolveJsonUtil;
import com.yshow.shike.utils.Tgoods;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 兑换商品
 */
public class Tea_chg_Comm_Act extends Activity implements OnClickListener {
	private CustomListView listview;
	private String points;
	private TextView pointText;
	private MyListAdapter adapter;
	private int shikePoint;
	private int needPoint;
	private Context context;
	private DisplayImageOptions picture_Shipei;
	private ImageLoader instance;
	private AlertDialog dialog;
	private Tgoods mSelectGoods;

	private EditText name, phone, address;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		setContentView(R.layout.fragment_teacher_changegoods);
		picture_Shipei = Net_Servse.getInstence().Picture_Shipei(R.drawable.s_teacher_icon);
		instance = ImageLoader.getInstance();
		pointText = (TextView) findViewById(R.id.teacher_changegoods_text);
		findViewById(R.id.tv_count_skback).setOnClickListener(this);
		findViewById(R.id.tv_cun_comg).setOnClickListener(this);
		findViewById(R.id.tv_cun_comg).setOnClickListener(this);
		String uid = LoginManage.getInstance().getStudent().getUid();
		if (uid != null) {
			MyShiKeInfo(uid);
		}
		listview = (CustomListView) findViewById(R.id.listview);
		getGiftList();
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			}
		});
		listview.setonRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				getRefreshGiftList();
			}
		});

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		View view = LayoutInflater.from(context).inflate(R.layout.duihuan_shangpin_dialog, null);
		view.findViewById(R.id.button).setOnClickListener(this);
		name = (EditText) view.findViewById(R.id.name);
		phone = (EditText) view.findViewById(R.id.phonenum);
		address = (EditText) view.findViewById(R.id.address);
		builder.setView(view);
		dialog = builder.create();
	}

	private void getRefreshGiftList() {
		SKAsyncApiController.getGiftList("1", new MyAsyncHttpResponseHandler(this, true) {
			@Override
			public void onSuccess(String json) {
				super.onSuccess(json);
				Type tp = new TypeToken<Duihuan_Goods>() {
				}.getType();
				Gson gs = new Gson();
				Duihuan_Goods ecg = gs.fromJson(json, tp);
				adapter = new MyListAdapter(ecg.getData().getList());
				listview.setAdapter(adapter);
				listview.onRefreshComplete();
			}
		});
	}

	class MyListAdapter extends BaseAdapter {
		private List<Tgoods> lists;

		public MyListAdapter(List<Tgoods> lists) {
			this.lists = lists;
		}

		@Override
		public int getCount() {
			return lists.size();
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(final int id, View arg1, ViewGroup arg2) {
			Tgoods tgoods = lists.get(id);
			View view = null;
			if (arg1 == null) {
				view = LayoutInflater.from(context).inflate(R.layout.choosegoods_listview_item, null);
			} else {
				view = arg1;
			}
			ImageView commodity = (ImageView) view.findViewById(R.id.goodslist_item_img);
			TextView name = (TextView) view.findViewById(R.id.goods_list_item_msgtext);
			TextView jifen = (TextView) view.findViewById(R.id.goods_list_item_matchtext);
			name.setText(lists.get(id).getName());
			jifen.setText(lists.get(id).getPoints() + "学分");
			Button button = (Button) view.findViewById(R.id.goods_list_item_duihuan);
			instance.displayImage(tgoods.getIcon(), commodity, picture_Shipei);
			button.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mSelectGoods = lists.get(id);
					try {
						needPoint = new Integer(lists.get(id).getPoints());
						shikePoint = new Integer(points);
					} catch (Exception e) {
						shikePoint = 0;
					}
					if (shikePoint >= needPoint) {
						dialog.show();
					} else {
						Toast.makeText(context, "学分不够", 0).show();
					}
				}
			});
			return view;
		}
	}

	public void getGiftList() {
		SKAsyncApiController.getGiftList("1", new MyAsyncHttpResponseHandler(this, true) {
			@Override
			public void onSuccess(String json) {
				super.onSuccess(json);
				Type tp = new TypeToken<Duihuan_Goods>() {
				}.getType();
				Gson gs = new Gson();
				Duihuan_Goods ecg = gs.fromJson(json, tp);
				adapter = new MyListAdapter(ecg.getData().getList());
				listview.setAdapter(adapter);
			}
		});
	}

	private void MyShiKeInfo(String uid) {
		SKAsyncApiController.User_Info(uid, new MyAsyncHttpResponseHandler(this, true) {
			public void onSuccess(String json) {
				User_Info my_teather = SKResolveJsonUtil.getInstance().My_teather(json);
				points = my_teather.getPoints();
				pointText.setText("学分：" + points);
			};
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_count_skback:
			finish();
			break;
		case R.id.tv_cun_comg:
			finish();
			break;
		case R.id.button:
			changeMailInfo();
			break;
		}
	}

	private boolean checkMailAddress() {
		if (TextUtils.isEmpty(name.getText().toString())) {
			Toast.makeText(context, "请输入收件人姓名", Toast.LENGTH_SHORT).show();
			return false;
		}
		String s = phone.getText().toString();
		if (TextUtils.isEmpty(s) || s.length() != 11) {
			Toast.makeText(context, "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
			return false;
		}
		if (TextUtils.isEmpty(address.getText().toString())) {
			Toast.makeText(context, "请输入地址", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}

	private void changeMailInfo() {
		if (checkMailAddress()) {
			SKAsyncApiController.changeMailAddress(name.getText().toString(), phone.getText().toString(), address.getText().toString(),
					new MyAsyncHttpResponseHandler(this, true) {
						public void onSuccess(String json) {
							dialog.dismiss();
							submitExchange();
						};
					});
		}
	}

	private void submitExchange() {
		SKAsyncApiController.exchangeGoods(mSelectGoods.getId(), new MyAsyncHttpResponseHandler(this, true) {
			public void onSuccess(String json) {
				Toast.makeText(context, "兑换成功", Toast.LENGTH_SHORT).show();
				String uid = LoginManage.getInstance().getStudent().getUid();
				MyShiKeInfo(uid);
			};
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
