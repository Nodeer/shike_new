package com.yshow.shike.activities;

import com.umeng.analytics.MobclickAgent;
import com.yshow.shike.R;
import com.yshow.shike.fragments.Fragment_Account_Prepraid;
import com.yshow.shike.fragments.Fragment_Account_Use;
import com.yshow.shike.utils.ScreenSizeUtil;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View.OnClickListener;
import android.view.animation.TranslateAnimation;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class Activity_Student_Account_Message extends BaseActivity {
    private FragmentManager manager; //Fragment 管理器
    private FragmentTransaction ft; //Fragment 转换器
    private TextView tv_cz_reg, tv_sy_reg;

    private boolean isLeft = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_account);
        initData();
    }

    private void initData() {


        TextView titleText = (TextView) findViewById(R.id.title_text);
        titleText.setText("账户明细");

        findViewById(R.id.left_btn).setOnClickListener(listener);
        tv_cz_reg = (TextView) findViewById(R.id.tv_cz_reg);
        tv_sy_reg = (TextView) findViewById(R.id.tv_sy_reg);
        tv_cz_reg.setOnClickListener(listener);
        tv_sy_reg.setOnClickListener(listener);
        manager = getSupportFragmentManager();
        ft = manager.beginTransaction();
        ft.replace(R.id.li_th_content, new Fragment_Account_Prepraid());
        ft.commit();
        tv_cz_reg.setSelected(true);
        tv_sy_reg.setSelected(false);
        isLeft = true;
    }

    private OnClickListener listener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            ft = manager.beginTransaction();
            switch (v.getId()) {
                case R.id.left_btn:
                    finish();
                    break;
                case R.id.tv_cz_reg:
                    if (!isLeft) {
                        tv_cz_reg.setSelected(true);
                        tv_sy_reg.setSelected(false);
                        ft.replace(R.id.li_th_content, new Fragment_Account_Prepraid());
                        isLeft = true;
                    }
                    break;
                case R.id.tv_sy_reg:
                    if (isLeft) {
                        tv_cz_reg.setSelected(false);
                        tv_sy_reg.setSelected(true);
                        ft.replace(R.id.li_th_content, new Fragment_Account_Use());
                        isLeft = false;
                    }
                    break;
            }
            ft.commit();
        }
    };
}
