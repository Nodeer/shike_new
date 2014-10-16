package com.yshow.shike.activities;

import com.umeng.analytics.MobclickAgent;
import com.yshow.shike.R;
import com.yshow.shike.entity.LoginManage;
import com.yshow.shike.entity.User_Info;
import com.yshow.shike.utils.Dialog;
import com.yshow.shike.utils.MyAsyncHttpResponseHandler;
import com.yshow.shike.utils.SKAsyncApiController;
import com.yshow.shike.utils.SKResolveJsonUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 兑换现金
 */
public class Tea_chg_Mon_Acy extends Activity {
	private EditText edit;
	private Context context;
	private TextView money_text;
	private AlertDialog dialog;
	private User_Info my_teather;
    private int mPoint;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_teacher_changemoney);
		context = this;
		initData();
		InitDialog();
	}

	/**
	 * 控件初始化
	 */
	private void initData() {
		money_text = (TextView) findViewById(R.id.teacher_money_text);
		edit = (EditText) findViewById(R.id.teacher_inputEdit);
		findViewById(R.id.tv_cash_comf).setOnClickListener(listener);
		findViewById(R.id.tv_cash_bk).setOnClickListener(listener);
	}

	/**
	 * 初始化显示积分的Dillog
	 */
	private void InitDialog() {
		AlertDialog.Builder builder = new Builder(context);
		View view = LayoutInflater.from(context).inflate(R.layout.duihuan_xianjin_dialog, null);
		view.findViewById(R.id.bt_jifen_set).setOnClickListener(listener);
		view.findViewById(R.id.bt_jifen_cen).setOnClickListener(listener);
		builder.setView(view);
		dialog = builder.create();
	}

	private OnClickListener listener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.tv_cash_bk:
				finish();
				break;
			case R.id.tv_cash_comf:
				Ji_Fen_Dec();
				break;
			// Dialog 确定键和取消建的监听
			case R.id.bt_jifen_set:
				dialog.dismiss();
                Bundle bun = new Bundle();
                bun.putBoolean("isneedback",true);
				Dialog.intent(context, Activity_Teacher_zhanghu.class,bun);
				break;
			case R.id.bt_jifen_cen:
				dialog.dismiss();
				break;
			}
		}
	};

	private void MyShiKeInfo(String uid) {
		SKAsyncApiController.User_Info(uid, new MyAsyncHttpResponseHandler(this, true) {
			public void onSuccess(String json) {
				my_teather = SKResolveJsonUtil.getInstance().My_teather1(json);
				money_text.setText("学分：" + my_teather.getPoints() + "分");
                try {
                    mPoint = Integer.parseInt(my_teather.getPoints());
                } catch (Exception e) {
                    mPoint = 0;
                }
			};
		});
	}

	/**
	 * 对积分进行判断 是1000的倍数 又大于3000分
	 */
	private void Ji_Fen_Dec() {
		int number = 0;
		String ed_Jifen = edit.getText().toString().trim();
		if (TextUtils.isEmpty(ed_Jifen)) {
			Toast.makeText(context, "请输入你要兑换的学分", Toast.LENGTH_SHORT).show();
		} else {
			try {
				number = Integer.parseInt(ed_Jifen);
				if (number % 1000 == 0 && 3000 <= number) {
					checkAccountInfo(ed_Jifen);
				} else {
					Toast.makeText(context, "您输入的不是1000的倍数的数字或者最低兑换没超过3000分", Toast.LENGTH_SHORT).show();
				}
			} catch (Exception e) {
				number = 0;
				Toast.makeText(context, "数据异常", Toast.LENGTH_SHORT).show();
			}
		}
	}

	/**
	 * 检查账户是否已设置银行信息
	 */
	private void checkAccountInfo(String s) {
        int point = Integer.parseInt(s);
        if(mPoint<point){
            Toast.makeText(context, "学分不够", Toast.LENGTH_SHORT).show();
            return;
        }
		if ("0".equals(my_teather.bankId)) {
			dialog.show();
		} else {
			submitExchange();
		}
	}

	private void submitExchange() {
		String ed_Jifen = edit.getText().toString().trim();
		SKAsyncApiController.exchangeJifen(ed_Jifen, new MyAsyncHttpResponseHandler(this, true) {
			public void onSuccess(String json) {
                super.onSuccess(json);
                Toast.makeText(context, "兑换成功", Toast.LENGTH_SHORT).show();
                refreshUserInfo();
			};
		});
	}

    private void refreshUserInfo() {
        String uid = LoginManage.getInstance().getStudent().getUid();
        SKAsyncApiController.User_Info(uid, new MyAsyncHttpResponseHandler(this, true) {
            public void onSuccess(String json) {
                my_teather = SKResolveJsonUtil.getInstance().My_teather1(json);
                money_text.setText("学分：" + my_teather.getPoints() + "分");
            };
        });
    }

    @Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
        String uid = LoginManage.getInstance().getStudent().getUid();
        if (uid != null) {
            MyShiKeInfo(uid);
        }
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
}
