package com.yshow.shike.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yshow.shike.R;

/**
 * Created by Administrator on 2014-10-16.
 * 老师侧滑页面
 */
public class TeaSlideMenuFragment extends Fragment implements View.OnClickListener {
    private TextView mUserName,mGrade,mFenbiNum;
    private ImageView mHeadIcon;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.stu_slide_layout, null);
        RelativeLayout btn1 = (RelativeLayout) view.findViewById(R.id.find_tea_btn);
        btn1.setOnClickListener(this);
        RelativeLayout btn2 = (RelativeLayout) view.findViewById(R.id.restudy_btn);
        btn2.setOnClickListener(this);
        RelativeLayout btn3 = (RelativeLayout) view.findViewById(R.id.account_btn);
        btn3.setOnClickListener(this);
        RelativeLayout btn4 = (RelativeLayout) view.findViewById(R.id.user_info_btn);
        btn4.setOnClickListener(this);
        RelativeLayout btn5 = (RelativeLayout) view.findViewById(R.id.share_btn);
        btn5.setOnClickListener(this);
        RelativeLayout btn6 = (RelativeLayout) view.findViewById(R.id.about_shike_btn);
        btn6.setOnClickListener(this);
        RelativeLayout btn7 = (RelativeLayout) view.findViewById(R.id.exit_btn);
        btn7.setOnClickListener(this);
        mUserName = (TextView) view.findViewById(R.id.username);
        mGrade = (TextView) view.findViewById(R.id.grade);
        mHeadIcon = (ImageView) view.findViewById(R.id.stu_headicon);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.find_tea_btn:
                break;
            case R.id.restudy_btn:
                break;
            case R.id.account_btn:
                break;
            case R.id.user_info_btn:
                break;
            case R.id.share_btn:
                break;
            case R.id.about_shike_btn:
                break;
            case R.id.exit_btn:
                break;
        }
    }
}
