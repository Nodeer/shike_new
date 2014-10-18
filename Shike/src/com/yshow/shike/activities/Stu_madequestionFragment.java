package com.yshow.shike.activities;

import android.app.Activity;
import com.umeng.analytics.MobclickAgent;
import com.yshow.shike.R;
import com.yshow.shike.UIApplication;
import com.yshow.shike.entity.LoginManage;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.yshow.shike.utils.HelpUtil;
import com.yshow.shike.utils.PartnerConfig;

/**
 * 学生端点击"我要提问"进入的tab.有提问和复习两个按钮.
 */
public class Stu_madequestionFragment extends Fragment implements
        OnClickListener {
    private LinearLayout stu_gongju;
    private LinearLayout stu_gongju_tiku;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sk_fragment_stumake_answer, null);
        stu_gongju = (LinearLayout) view.findViewById(R.id.stu_gongju_text);
        stu_gongju_tiku = (LinearLayout) view.findViewById(R.id.stu_gongju_tiku);
        TextView tv_gong_ju = (TextView) view.findViewById(R.id.tv_gong_jv);
        TextView tv_tiku = (TextView) view.findViewById(R.id.tv_tiku_stu);
        if (!LoginManage.getInstance().isTeacher(getActivity())) {
            tv_gong_ju.setText("提问");
            tv_tiku.setText("复习");
        }
        stu_gongju.setOnClickListener(this);
        stu_gongju_tiku.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //进入题库按钮或者复习按钮
            case R.id.stu_gongju_tiku:
                Intent intent = new Intent(getActivity(), My_Question_Count.class);
                startActivity(intent);
                break;
            // 提问按钮
            case R.id.stu_gongju_text:
                UIApplication.getInstance().cleanPicUrls();
                UIApplication.getInstance().cleanbitmaplist();
                PartnerConfig.TEATHER_ID = null;
                PartnerConfig.SUBJECT_ID = null;
                PartnerConfig.TEATHER_NAME = null;
                PartnerConfig.SUBJECT_NAME = null;
                startActivity(new Intent(getActivity(), Activity_Stu_Tool_Sele.class));
                break;
        }
    }

}
