package com.yshow.shike.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yshow.shike.R;
import com.yshow.shike.activities.Activity_Teacher_zhanghu;
import com.yshow.shike.activities.MyStudentActivity;
import com.yshow.shike.activities.TeaPersonInfoActivity;
import com.yshow.shike.activities.MessageActivity;
import com.yshow.shike.activities.TeacherExchangeMainActivity;
import com.yshow.shike.activities.TeacherExchangeMoneyActivity;
import com.yshow.shike.entity.LoginManage;
import com.yshow.shike.entity.SKStudent;
import com.yshow.shike.entity.User_Info;
import com.yshow.shike.utils.Dilog_Share;
import com.yshow.shike.utils.Exit_Login;
import com.yshow.shike.utils.ImageLoadOption;
import com.yshow.shike.utils.MyAsyncHttpResponseHandler;
import com.yshow.shike.utils.PartnerConfig;
import com.yshow.shike.utils.SKAsyncApiController;
import com.yshow.shike.utils.SKResolveJsonUtil;
import com.yshow.shike.utils.ShareDialog;
import com.yshow.shike.utils.WeixinManager;

/**
 * Created by Administrator on 2014-10-16.
 * 老师侧滑页面
 */
public class TeaSlideMenuFragment extends Fragment implements View.OnClickListener {
    private TextView mUserName, mGrade, mFenbiNum;
    private ImageView mHeadIcon;
    private SKStudent student;
    private android.app.Dialog dialog;
    private WeixinManager weixinManager;
    private ImageLoader mImageLoader;
    private DisplayImageOptions mOption;

    RefreshUserinfoBroadCastReceiver receiver;

    private class RefreshUserinfoBroadCastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            update_info(student.getUid());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tea_slide_layout, null);
        RelativeLayout btn1 = (RelativeLayout) view.findViewById(R.id.my_stu_btn);
        btn1.setOnClickListener(this);
        RelativeLayout btn2 = (RelativeLayout) view.findViewById(R.id.exchange_btn);
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
        mFenbiNum = (TextView) view.findViewById(R.id.fenbi_num);
        mHeadIcon = (ImageView) view.findViewById(R.id.stu_headicon);
        student = LoginManage.getInstance().getStudent();
        dialog = new Dilog_Share().Dilog_Anim(getActivity(), this);
        weixinManager = new WeixinManager(getActivity());
        mImageLoader = ImageLoader.getInstance();

        mUserName.setText("用户名:" + student.getNickname());


        mOption = ImageLoadOption.getTeaHeadImageOption();

        update_info(student.getUid());
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        receiver = new RefreshUserinfoBroadCastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("update_user_info");
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(receiver,filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(receiver);
    }


    private void update_info(String uid) {
        SKAsyncApiController.User_Info(uid, new MyAsyncHttpResponseHandler(getActivity(), false) {
            public void onSuccess(int arg0, String json) {
                super.onSuccess(arg0, json);
                boolean atent_Success = SKResolveJsonUtil.getInstance().resolveIsSuccess(json, getActivity());
                if (atent_Success) {
                    User_Info info = SKResolveJsonUtil.getInstance().My_teather1(json);
                    mUserName.setText("用户名:" + info.getNickname());
                    mGrade.setText("授    课:" + info.getGrade() + info.getSubject());
                    mFenbiNum.setText("赞美数:" + info.getLike_num());
                    mImageLoader.displayImage(info.getPicurl(), mHeadIcon, mOption);

                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent it;
        switch (v.getId()) {
            case R.id.my_stu_btn:
                it = new Intent(getActivity(), MyStudentActivity.class);
                startActivity(it);
                break;
            case R.id.exchange_btn:
                it = new Intent(getActivity(), TeacherExchangeMainActivity.class);
                startActivity(it);
                break;
            case R.id.account_btn:
                it = new Intent(getActivity(), Activity_Teacher_zhanghu.class);
                startActivity(it);
                break;
            case R.id.user_info_btn:
                it = new Intent(getActivity(), TeaPersonInfoActivity.class);
                startActivity(it);
                break;
            case R.id.share_btn:
                dialog.show();
                break;
            case R.id.about_shike_btn:
                it = new Intent(getActivity(), Fragment_Teacher_About_Shike.class);
                startActivity(it);
                break;
            case R.id.exit_btn:
                Exit_Login.getInLogin().Back_Login(getActivity());
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
