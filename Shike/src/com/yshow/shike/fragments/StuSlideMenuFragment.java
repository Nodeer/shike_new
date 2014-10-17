package com.yshow.shike.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yshow.shike.R;
import com.yshow.shike.activities.Activity_Recharge;
import com.yshow.shike.activities.Age_Person_Info;
import com.yshow.shike.activities.FindTeacherActivity;
import com.yshow.shike.activities.My_Question_Count;
import com.yshow.shike.activities.StudentRegisterActivity;
import com.yshow.shike.entity.LoginManage;
import com.yshow.shike.entity.SKStudent;
import com.yshow.shike.utils.Dialog;
import com.yshow.shike.utils.Dilog_Share;
import com.yshow.shike.utils.Exit_Login;
import com.yshow.shike.utils.PartnerConfig;
import com.yshow.shike.utils.ShareDialog;
import com.yshow.shike.utils.WeixinManager;

/**
 * Created by Administrator on 2014-10-16.
 */
public class StuSlideMenuFragment extends Fragment implements View.OnClickListener {
    private TextView mUserName, mGrade;
    private ImageView mHeadIcon;
    private SKStudent student;
    private android.app.Dialog dialog;
    private WeixinManager weixinManager;

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
        student = LoginManage.getInstance().getStudent();
        dialog = new Dilog_Share().Dilog_Anim(getActivity(), this);
        weixinManager = new WeixinManager(getActivity());
        return view;
    }

    @Override
    public void onClick(View v) {
        Intent it;
        switch (v.getId()) {
            case R.id.find_tea_btn:
                it = new Intent(getActivity(), FindTeacherActivity.class);
                startActivity(it);
                break;
            case R.id.restudy_btn:
                it = new Intent(getActivity(), My_Question_Count.class);
                startActivity(it);
                break;
            case R.id.account_btn:
                if (student.getMob() != null) {
                    Dialog.Intent(getActivity(), Activity_Recharge.class);
                } else {
                    Dialog.finsh_Reg_Dialog(getActivity());
                }
                break;
            case R.id.user_info_btn:
                if (student.getMob() != null) {
                    Dialog.Intent(getActivity(), Age_Person_Info.class);
                } else {
                    Dialog.finsh_Reg_Dialog(getActivity());
                }
                break;
            case R.id.share_btn:
                dialog.show();
                break;
            case R.id.about_shike_btn:
                Dialog.Intent(getActivity(), Fragment_Student_GuanYu.class);
                break;
            case R.id.exit_btn:
                if (student.getMob() != null) {// 如果是注册用户,就是退出按钮
                    Exit_Login.getInLogin().Back_Login(getActivity());
                } else {
                    Dialog.Intent(getActivity(), StudentRegisterActivity.class);
                }
                break;
            // Dialog 里面微信分享的按钮
            case R.id.share_dialog_weixin:
                dialog.dismiss();
                weixinManager.shareWeixin();
                break;
            // Dialog 里面短信分享的按钮
            case R.id.share_dialog_message:
                ShareDialog.sendSMS(getActivity(), PartnerConfig.CONTEBT);
                break;
            // Dialog 里面e_mail分享的按钮
            case R.id.share_dialog_email:
                dialog.dismiss();
                ShareDialog.openCLD(PartnerConfig.CONTEBT, getActivity());
                break;
            case R.id.share_weixin_friend:
                dialog.dismiss();
                weixinManager.shareWeixinCircle();
                break;
            // Dialog 里面取消分享的按钮
            case R.id.share_dialog_cancle:
                dialog.dismiss();
                break;
        }
    }
}
