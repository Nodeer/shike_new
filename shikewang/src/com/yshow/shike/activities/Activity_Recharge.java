package com.yshow.shike.activities;
import java.lang.reflect.Type;

import android.app.*;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View.OnClickListener;
import android.widget.Toast;
import com.alipay.android.app.sdk.AliPay;
import com.umeng.analytics.MobclickAgent;
import com.unionpay.UPPayAssistEx;
import com.yshow.shike.entity.YinLianOrderModel;
import com.yshow.shike.utils.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.yshow.shike.R;
import com.yshow.shike.entity.Pay_Comm;
import com.yshow.shike.entity.Pay_Comm.Pay_Comm_info;

import android.view.View;
public class Activity_Recharge extends Activity{
	private Payment_Utils pay_util;
	private final int RQF_PAY = 1;
	private final int PAY_SUCC = 2;
    private String payType = "";
	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			Result result = new Result((String) msg.obj);
			switch (msg.what) {
			case RQF_PAY:
				Toast.makeText(Activity_Recharge.this, result.getResult(),0).show();
				break;
			case PAY_SUCC:
				Toast.makeText(Activity_Recharge.this,"支付成功",0).show();
				break;
			}
		};
	};
    private Dialog rechargeTypeDialog;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recharge);
		initData();
	}
	private void initData() {
		pay_util = Payment_Utils.getIntence();
		findViewById(R.id.re_five_bai).setOnClickListener(listener);
		findViewById(R.id.re_one_qian).setOnClickListener(listener);
		findViewById(R.id.iv_two_qian).setOnClickListener(listener);
		findViewById(R.id.re_five_qain).setOnClickListener(listener);
		findViewById(R.id.tv_buttom_back).setOnClickListener(listener);
	}
	private OnClickListener listener = new OnClickListener() {
		@Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.re_five_bai:
                    payType = "1";
                    showReChargeType();
                    break;
                case R.id.re_one_qian:
                    payType = "2";
                    showReChargeType();
                    break;
                case R.id.iv_two_qian:
                    payType = "3";
                    showReChargeType();
                    break;
                case R.id.re_five_qain:
                    payType = "4";
                    showReChargeType();
                    break;
                case R.id.tv_buttom_back:
                    finish();
                    break;
                case R.id.tv_pai_zhao://套用了现成的layout文件...这里代表支付宝方式
                    getAlipayOrder();
                    rechargeTypeDialog.dismiss();
                    break;
                case R.id.tv_xiagnc://套用了现成的layout文件...这里代表银联方式
                    getYinLianOrder();
                    rechargeTypeDialog.dismiss();
                    break;
                case R.id.tv_tea_cancel://套用了现成的layout文件...这里代表取消
                    rechargeTypeDialog.dismiss();
                    break;
            }
        }
    };

    //{"success":1,"data":{"respCode":"00","tn":"201407221631260053012","signMethod":"MD5","transType":"01","charset":"UTF-8","signature":"2531fcdf736828c68b4e91adccf97df3","version":"1.0.0"},"type":"ok"}
    /**
     * 生成银联订单
     */
    private void getYinLianOrder() {
        SKAsyncApiController.Get_YinLian_Comm_Info(payType, new AsyncHttpResponseHandler() {
            public void onSuccess(String json) {
                boolean Success = SKResolveJsonUtil.getInstance().resolveIsSuccess(json, Activity_Recharge.this);
                if (Success) {
                    YinLianOrderModel model = SKResolveJsonUtil.getInstance().GetYinlianOrder(json);
                    commYinlianPay(model.tn);
                }
            };
        });
    }

    private void commYinlianPay(final String tn) {
                int result = UPPayAssistEx.startPay(Activity_Recharge.this,null,null,tn,"00");
                Message msg = new Message();
                msg.obj = result;

        if (result == UPPayAssistEx.PLUGIN_NOT_FOUND) {
            // 需要重新安装控件
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("提示");
            builder.setMessage("完成购买需要安装银联支付控件，是否安装？");
            builder.setNegativeButton("确定",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog,
                                            int which) {
                            UPPayAssistEx.installUPPayPlugin(Activity_Recharge.this);
                            dialog.dismiss();
                        }
                    });
            builder.setPositiveButton("取消",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog,
                                            int which) {
                            dialog.dismiss();
                        }
                    });
            builder.create().show();
        }

    }

    /**
     * 生成支付宝订单
     */
    private void getAlipayOrder() {
        SKAsyncApiController.Get_Comm_Info(payType, new AsyncHttpResponseHandler() {
            public void onSuccess(String json) {
                boolean Success = SKResolveJsonUtil.getInstance().resolveIsSuccess(json, Activity_Recharge.this);
                if (Success) {
                    Type tp = new TypeToken<Pay_Comm>() {
                    }.getType();
                    Gson gs = new Gson();
                    Pay_Comm pay_comm = gs.fromJson(json, tp);
                    Pay_Comm_info comm_data = pay_comm.getData();
                    String info = pay_util.getNewOrderInfo(comm_data);
                    CommAliPay(info);
                }
            };
        });
    }

    private void CommAliPay(String info) {
		final String orderInfo = pay_util.Order_Process(info).trim();
		new Thread() {
			public void run() {
				AliPay alipay = new AliPay(Activity_Recharge.this, mHandler);
				String result = alipay.pay(orderInfo);
				Message msg = new Message();
				msg.obj = result;
				if(result != null){
					String returnCode = result.substring(14, 18);
					if(returnCode.equals("9000")){
                        msg.what = PAY_SUCC;
                    }else {
                        msg.what = RQF_PAY;
					}
					mHandler.sendMessage(msg);
				}
			}
		}.start();
	}

    private void showReChargeType(){
        rechargeTypeDialog = Dilog_Share.SelectReChargeTypeDialog(this, listener);
        rechargeTypeDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /*************************************************
         *
         *  步骤3：处理银联手机支付控件返回的支付结果
         *
         ************************************************/
        if (data == null) {
            return;
        }

        String msg = "";
        /*
         * 支付控件返回字符串:success、fail、cancel
         *      分别代表支付成功，支付失败，支付取消
         */
        String str = data.getExtras().getString("pay_result");
        if (str.equalsIgnoreCase("success")) {
            msg = "支付成功！";
        } else if (str.equalsIgnoreCase("fail")) {
            msg = "支付失败！";
        } else if (str.equalsIgnoreCase("cancel")) {
            msg = "用户取消了支付";
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("支付结果通知");
        builder.setMessage(msg);
        builder.setInverseBackgroundForced(true);
        //builder.setCustomTitle();
        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
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
