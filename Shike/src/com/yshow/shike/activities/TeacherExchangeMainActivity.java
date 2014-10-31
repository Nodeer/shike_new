package com.yshow.shike.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.yshow.shike.R;
import com.yshow.shike.entity.LoginManage;
import com.yshow.shike.entity.User_Info;
import com.yshow.shike.utils.Dialog;
import com.yshow.shike.utils.MyAsyncHttpResponseHandler;
import com.yshow.shike.utils.SKAsyncApiController;
import com.yshow.shike.utils.SKResolveJsonUtil;

/**
 * Created by Administrator on 2014-10-31.
 * 老师兑换主页
 */
public class TeacherExchangeMainActivity extends BaseActivity implements View.OnClickListener {
    private TextView xuefen_point;
    private User_Info my_teather;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_exchange_main_layout);

        TextView title = (TextView) findViewById(R.id.title_text);
        title.setText("兑换");

        findViewById(R.id.exchange_gods_btn).setOnClickListener(this);
        findViewById(R.id.exchange_money_btn).setOnClickListener(this);
        findViewById(R.id.bank_account).setOnClickListener(this);
        findViewById(R.id.left_btn).setOnClickListener(this);
        xuefen_point = (TextView) findViewById(R.id.xuefen_point);


    }

    private void MyShiKeInfo(String uid) {
        SKAsyncApiController.User_Info(uid, new MyAsyncHttpResponseHandler(this, true) {
            public void onSuccess(String json) {
                my_teather = SKResolveJsonUtil.getInstance().My_teather1(json);
                xuefen_point.setText(my_teather.getPoints());
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        MyShiKeInfo(LoginManage.getInstance().getStudent().getUid());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.exchange_gods_btn:
                Dialog.Intent(this, TeacherExchangeGodsActivity.class);
                break;
            case R.id.exchange_money_btn:
                Dialog.Intent(this, TeacherExchangeMoneyActivity.class);
                break;
            case R.id.bank_account:
                break;
            case R.id.left_btn:
                finish();
                break;
        }
    }
}
