package com.yshow.shike.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.yshow.shike.R;

/**
 * Created by Administrator on 2014-10-19.
 * 学生提问第二步,显示照片预览图,选择科目
 */
public class Activity_Stu_Ask_Step2 extends BaseActivity implements View.OnClickListener {
    private View mLastButton, mLastSelectIcon;
    private RelativeLayout mYuwenButton, mShuxueButton, mYingyuButton, mWuliButton, mHuaxueButton;
    private ImageView mYuwenSelectIcon, mShuxueSelectIcon, mYingyuSelectIcon, mWuliSelectIcon, mHuaxueSelectIcon;
    private ImageView[] mSelectIcons;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stu_ask_step2_layout);
        mYuwenButton = (RelativeLayout) findViewById(R.id.yuwen_btn);
        mYuwenButton.setOnClickListener(this);
        mShuxueButton = (RelativeLayout) findViewById(R.id.shuxue_btn);
        mShuxueButton.setOnClickListener(this);
        mYingyuButton = (RelativeLayout) findViewById(R.id.yingyu_btn);
        mYingyuButton.setOnClickListener(this);
        mWuliButton = (RelativeLayout) findViewById(R.id.wuli_btn);
        mWuliButton.setOnClickListener(this);
        mHuaxueButton = (RelativeLayout) findViewById(R.id.huaxue_btn);
        mHuaxueButton.setOnClickListener(this);
        mYuwenSelectIcon = (ImageView) findViewById(R.id.yuwen_select_icon);
        mShuxueSelectIcon = (ImageView) findViewById(R.id.shuxue_select_icon);
        mYingyuSelectIcon = (ImageView) findViewById(R.id.yingyu_select_icon);
        mWuliSelectIcon = (ImageView) findViewById(R.id.wuli_select_icon);
        mHuaxueSelectIcon = (ImageView) findViewById(R.id.huaxue_select_icon);
        mSelectIcons = new ImageView[]{mYuwenSelectIcon, mShuxueSelectIcon, mYingyuSelectIcon, mWuliSelectIcon, mHuaxueSelectIcon};
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.yuwen_btn:
                changeSelectBtn(v, 0);
                break;
            case R.id.shuxue_btn:
                changeSelectBtn(v, 1);
                break;
            case R.id.yingyu_btn:
                changeSelectBtn(v, 2);
                break;
            case R.id.wuli_btn:
                changeSelectBtn(v, 3);
                break;
            case R.id.huaxue_btn:
                changeSelectBtn(v, 4);
                break;
        }
    }

    private void changeSelectBtn(View v, int index) {
        if (mLastButton == null) {
            mLastButton = v;
            mLastButton.setSelected(true);
            mLastSelectIcon = mSelectIcons[index];
            mLastSelectIcon.setVisibility(View.VISIBLE);
        } else {
            mLastButton.setSelected(false);
            mLastSelectIcon.setVisibility(View.GONE);
            mLastButton = v;
            mLastButton.setSelected(true);
            mLastSelectIcon = mSelectIcons[index];
            mLastSelectIcon.setVisibility(View.VISIBLE);
        }
    }
}
