package com.yshow.shike.activities;

import java.lang.reflect.Type;

import com.yshow.shike.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yshow.shike.entity.Bank;
import com.yshow.shike.entity.LoginManage;
import com.yshow.shike.entity.User_Info;
import com.yshow.shike.utils.*;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

/**
 * 老师银行账户填写页面
 */
public class TeacherBankEditActivity extends BaseActivity {
    private Bank bank;
    private TextView bindBankText;
    private Context context;
    private EditText bank_name, card_number, kaihu_name;// 开户行名称 银行卡号
    private String bank_id2 = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.fragment_teacher_kaihu);

        TextView title = (TextView) findViewById(R.id.title_text);
        title.setText("银行账户");

        bindBankText = (TextView) findViewById(R.id.kaihu_bangbank_edittext);
        bank_name = (EditText) findViewById(R.id.kaihu_bank_edittext);
        card_number = (EditText) findViewById(R.id.kaihu_kahao);
        kaihu_name = (EditText) findViewById(R.id.kaihu_edittext);
        LinearLayout bind_bank_btn = (LinearLayout) findViewById(R.id.bind_bank_btn);
        bind_bank_btn.setOnClickListener(listener);
        findViewById(R.id.teacher_account_dengjitext).setOnClickListener(listener);
        findViewById(R.id.left_btn).setOnClickListener(listener);
        String uid = LoginManage.getInstance().getStudent().getUid();
        SKAsyncApiController.User_Info(uid, new MyAsyncHttpResponseHandler(this, true) {
            public void onSuccess(int arg0, String json) {
                User_Info my_teather = SKResolveJsonUtil.getInstance().My_teather1(json);
                if (my_teather.bankId != null) {
                    kaihu_name.setText(my_teather.getName());
                    card_number.setText(my_teather.bankNO);
                    bank_name.setText(my_teather.bankAddr);
                    bank_id2 = my_teather.bankId;
                    bindBankText.setText(my_teather.bankName);
                }
            }
        });
    }


    /**
     * 按钮点击事件
     */
    private OnClickListener listener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.bind_bank_btn:
                    getBanksFromNet();
                    break;
                case R.id.left_btn:
                    finish();
                    break;
                // 兑换按钮
                case R.id.teacher_account_dengjitext:
                    String name = kaihu_name.getText().toString();
                    String bank_adr = bank_name.getText().toString().trim();
                    String bank_card_num = card_number.getText().toString().trim();
                    if (bank_id2.equals("") || TextUtils.isEmpty(bank_adr) || TextUtils.isEmpty(bank_card_num)) {
                        Toast.makeText(context, "请填写上述信息", Toast.LENGTH_SHORT).show();
                    } else {
                        changeAccountInfo(name, bank_id2, bank_adr, bank_card_num);
                    }
                    break;
            }
        }
    };

    public void getBanksFromNet() {
        SKAsyncApiController.getBankList(new MyAsyncHttpResponseHandler(context, true) {
            @Override
            public void onSuccess(String arg0) {
                super.onSuccess(arg0);
                Type tp = new TypeToken<Bank>() {
                }.getType();
                Gson gs = new Gson();
                bank = gs.fromJson(arg0, tp);

                final BankSeltorUtil systemDialog = new BankSeltorUtil(context, bank.getData());
                systemDialog.setLeftButtonText("完成");
                systemDialog.show();
                systemDialog.setBankSeltorUtilButtonOnclickListening(new BankSeltorUtil.BankSeltorUtilButtonOnclickListening() {
                    @Override
                    public void onClickRight() {
                    }

                    @Override
                    public void onClickLeft() {
                        bindBankText.setText(systemDialog.getSeltotText());
                        bank_id2 = systemDialog.getGradeId();
                    }
                });

            }
        });
    }

    /**
     * 兑换现金
     *
     * @param bankId   银行id 关联银行列表
     * @param bankAddr 开户行地址
     * @param bankNO   卡号
     */
    private void changeAccountInfo(String name, String bankId, String bankAddr, String bankNO) {
        SKAsyncApiController.changeAccountInfo(name, bankId, bankAddr, bankNO, new MyAsyncHttpResponseHandler(context, true) {
            @Override
            public void onSuccess(String json) {
                super.onSuccess(json);
                boolean success = SKResolveJsonUtil.getInstance().resolveIsSuccess(json, context);
                if (success) {
                    Toast.makeText(context, "更新银行信息成功", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
