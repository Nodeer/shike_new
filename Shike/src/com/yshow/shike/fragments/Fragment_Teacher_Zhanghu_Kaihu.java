package com.yshow.shike.fragments;

import java.lang.reflect.Type;

import com.yshow.shike.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yshow.shike.activities.Activity_Teacher_zhanghu;
import com.yshow.shike.entity.Bank;
import com.yshow.shike.entity.LoginManage;
import com.yshow.shike.entity.User_Info;
import com.yshow.shike.utils.*;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

/**
 * 老师银行账户填写页面
 */
public class Fragment_Teacher_Zhanghu_Kaihu extends Fragment {
    private RelativeLayout reportLayout;
    private LinearLayout bankLayout;
    private Bank bank;
    private TextView bangdingText;
    private Context context;
    private EditText bank_name, card_number, kaihu_name;// 开户行名称 银行卡号
    private TextView text;
    private String bank_id2 = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_teacher_kaihu, null);
        reportLayout = (RelativeLayout) view.findViewById(R.id.teacher_account_kaihu);
        bankLayout = (LinearLayout) view.findViewById(R.id.choose_bank);
        bangdingText = (TextView) view.findViewById(R.id.kaihu_bangbank_edittext);
        bank_name = (EditText) view.findViewById(R.id.kaihu_bank_edittext);
        card_number = (EditText) view.findViewById(R.id.kaihu_kahao);
        kaihu_name = (EditText) view.findViewById(R.id.kaihu_edittext);
        // SKStudent student = LoginManage.getInstance().getStudent();
        // if(student != null){
        // String nickname = student.getNickname();
        // if(!nickname.equals("")){
        // kaihu_name.setText(student.getNickname());
        // }else {
        // kaihu_name.setText(student.getName());
        // }
        // }
        bangdingText.setOnClickListener(listener);
        view.findViewById(R.id.teacher_account_dengjitext).setOnClickListener(listener);
        String uid = LoginManage.getInstance().getStudent().getUid();
        SKAsyncApiController.User_Info(uid, new MyAsyncHttpResponseHandler(getActivity(), true) {
            public void onSuccess(int arg0, String json) {
                User_Info my_teather = SKResolveJsonUtil.getInstance().My_teather1(json);
                if (my_teather.bankId != null) {
                    kaihu_name.setText(my_teather.getName());
                    card_number.setText(my_teather.bankNO);
                    bank_name.setText(my_teather.bankAddr);
                    bank_id2 = my_teather.bankId;
                    bangdingText.setText(my_teather.bankName);
                }
            }

            ;
        });
        getBanksFromNet();
        return view;
    }

    /**
     * 按钮点击事件
     */
    private OnClickListener listener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.kaihu_bangbank_edittext:
                    reportLayout.setVisibility(View.INVISIBLE);
                    bankLayout.setVisibility(View.VISIBLE);
                    break;
                // 兑换按钮
                case R.id.teacher_account_dengjitext:
                    String name = kaihu_name.getText().toString();
                    String bank_adr = bank_name.getText().toString().trim();
                    String bank_card_num = card_number.getText().toString().trim();
                    if (bank_id2.equals("") || TextUtils.isEmpty(bank_adr) || TextUtils.isEmpty(bank_card_num)) {
                        Toast.makeText(getActivity(), "请填写上述信息", Toast.LENGTH_SHORT).show();
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
                Integer bankId = null;
                Type tp = new TypeToken<Bank>() {
                }.getType();
                Gson gs = new Gson();
                bank = gs.fromJson(arg0, tp);
                if (bank != null) {
                    for (int i = 0; i < bank.getData().size(); i++) {
                        String str = bank.getData().get(i).getName();
                        String bank_id = bank.getData().get(i).getId();
                        try {
                            bankId = new Integer(bank_id);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                                LayoutParams.WRAP_CONTENT);
                        params.bottomMargin = 10;
                        params.leftMargin = 10;
                        params.rightMargin = 10;
                        params.width = ScreenSizeUtil.Dp2Px(getActivity(), 100f);
                        params.topMargin = 10;
                        params.weight = 1;
                        params.gravity = Gravity.CENTER;
                        if (isAdded()) {
                            text = new TextView(context);
                            text.setGravity(Gravity.CENTER);
                            text.setId(bankId);
                            text.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    bangdingText.setText(bank.getData().get(v.getId() - 1).getName());
                                    bank_id2 = bank.getData().get(v.getId() - 1).getId();
                                    bankLayout.setVisibility(View.INVISIBLE);
                                    reportLayout.setVisibility(View.VISIBLE);
                                }
                            });
                            text.setTextSize(15);
                            text.setTextColor(GetColor(R.color.button_typeface_color));
                            text.setLayoutParams(params);
                            text.setBackgroundColor(GetColor(R.color.bottom_widow_color));
                            text.setText(str);
                            bankLayout.addView(text);
                        }
                    }
                }
            }

            private int GetColor(int color) {
                int bank_color = getActivity().getResources().getColor(color);
                return bank_color;
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
        SKAsyncApiController.changeAccountInfo(name, bankId, bankAddr, bankNO, new MyAsyncHttpResponseHandler(getActivity(), true) {
            @Override
            public void onSuccess(String json) {
                super.onSuccess(json);
                boolean success = SKResolveJsonUtil.getInstance().resolveIsSuccess(json, getActivity());
                if (success) {
                    Toast.makeText(getActivity(), "更新银行信息成功", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
