package com.yshow.shike.activities;

import com.yshow.shike.R;
import com.yshow.shike.fragments.Fragment_Teacher_Zhanghu_Shouru;
import com.yshow.shike.fragments.Fragment_Teacher_Zhanghu_duihuan;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

/**
 * 老师设置账户信息
 */
public class Activity_Teacher_zhanghu extends BaseActivity {
    private FragmentTransaction ft; // Fragment 转化活动
    private FragmentManager manager; //Fragment管理器
    private TextView duihuan_btn, shouru_btn;//银行账户 收入明细 兑换信息
    private boolean isLeft = true;

    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_teacher_zhanghu);
        init();
    }

    private void init() {
        boolean isShowDuihuan = getIntent().getBooleanExtra("isShowDuihuan", false);
        TextView titleText = (TextView) findViewById(R.id.title_text);
        titleText.setText("账户明细");
        findViewById(R.id.left_btn).setOnClickListener(listener);
        shouru_btn = (TextView) findViewById(R.id.tv_exchange_info);
        shouru_btn.setOnClickListener(listener);
        duihuan_btn = (TextView) findViewById(R.id.tv_acc_balance);
        duihuan_btn.setOnClickListener(listener);
        manager = getSupportFragmentManager();
        ft = manager.beginTransaction();
        if (!isShowDuihuan) {
            shouru_btn.setSelected(true);
            duihuan_btn.setSelected(false);
            ft.replace(R.id.li_th_content, new Fragment_Teacher_Zhanghu_Shouru());
        } else {
            shouru_btn.setSelected(false);
            duihuan_btn.setSelected(true);
            isLeft = false;
            ft.replace(R.id.li_th_content, new Fragment_Teacher_Zhanghu_duihuan());
        }
        ft.commitAllowingStateLoss();
    }

    private OnClickListener listener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            ft = manager.beginTransaction();
            switch (v.getId()) {
                // 返回键
                case R.id.left_btn:
                    finish();
                    break;
                // 兑换信息
                case R.id.tv_acc_balance:
                    if (isLeft) {
                        shouru_btn.setSelected(false);
                        duihuan_btn.setSelected(true);
                        isLeft = false;
                    }
                    ft.replace(R.id.li_th_content, new Fragment_Teacher_Zhanghu_duihuan());
                    break;
                //收入明细
                case R.id.tv_exchange_info:
                    if (!isLeft) {
                        shouru_btn.setSelected(true);
                        duihuan_btn.setSelected(false);
                        isLeft = true;
                    }
                    ft.replace(R.id.li_th_content, new Fragment_Teacher_Zhanghu_Shouru());
                    break;
            }
            ft.commit();
        }
    };
}
