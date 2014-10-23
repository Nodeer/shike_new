package com.yshow.shike.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ant.liao.GifView;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.yshow.shike.R;
import com.yshow.shike.UIApplication;
import com.yshow.shike.activities.Activity_Stu_Ask_Step1;
import com.yshow.shike.activities.MessageActivity;
import com.yshow.shike.activities.My_Question_Count;
import com.yshow.shike.utils.PartnerConfig;
import com.yshow.shike.widget.GalleryView;

/**
 * Created by Administrator on 2014-10-16.
 * 学生首页
 */
public class StuContentFragment extends Fragment implements View.OnClickListener {
    private ImageView mMessRedIcon;
    private RelativeLayout mAskButton, mReStudyButton;
    private TextView mOnlineTextView;
    private GifView mTitleRight;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.stu_content_layout, null);

        GalleryView gallery = (GalleryView) view.findViewById(R.id.gallery);
        gallery.setData(new int[]{R.drawable.blackboard_ad_img, R.drawable.blackboard_ad_img}, false);

        ImageView mTitleLeft = (ImageView) view.findViewById(R.id.title_left);
        mTitleLeft.setOnClickListener(this);

        mTitleRight = (GifView) view.findViewById(R.id.title_right);
        mTitleRight.setGifImage(R.drawable.stu_ring_gif);
        mTitleRight.setOnClickListener(this);
        mMessRedIcon = (ImageView) view.findViewById(R.id.mess_red);
        mAskButton = (RelativeLayout) view.findViewById(R.id.ask_question_btn);
        mAskButton.setOnClickListener(this);
        mReStudyButton = (RelativeLayout) view.findViewById(R.id.restudy_btn);
        mReStudyButton.setOnClickListener(this);
        mOnlineTextView = (TextView) view.findViewById(R.id.now_online);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_left:
                Activity activity = getActivity();
                if (activity instanceof SlidingFragmentActivity) {
                    ((SlidingFragmentActivity) activity).showMenu();
                }
                break;
            case R.id.title_right:
                goMessageActivity();
                break;
            case R.id.ask_question_btn:
                UIApplication.getInstance().cleanPicUrls();
                UIApplication.getInstance().cleanbitmaplist();
                PartnerConfig.TEATHER_ID = null;
                PartnerConfig.SUBJECT_ID = null;
                PartnerConfig.TEATHER_NAME = null;
                PartnerConfig.SUBJECT_NAME = null;
                startActivity(new Intent(getActivity(), Activity_Stu_Ask_Step1.class));
                break;
            case R.id.restudy_btn:
                Intent intent = new Intent(getActivity(), My_Question_Count.class);
                startActivity(intent);
                break;

        }
    }

    private void goMessageActivity() {
        Intent it = new Intent(getActivity(), MessageActivity.class);
        getActivity().startActivity(it);
    }
}
