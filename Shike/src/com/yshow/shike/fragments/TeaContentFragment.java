package com.yshow.shike.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yshow.shike.activities.MessageActivity;
import com.yshow.shike.service.MySKService;
import com.yshow.shike.widget.GalleryView;

import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.yshow.shike.R;

/**
 * Created by Administrator on 2014-10-16.
 * 老师首页
 */
public class TeaContentFragment extends Fragment implements View.OnClickListener {
    ImageView mMessRedIcon;
    RelativeLayout mMakeTopicBtn, mTikuButton;
    private TextView mOnlineTextView;
    private ImageView mTitleRight;
    private AnimationDrawable ani;

    public Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case MySKService.HAVE_NEW_MESSAGE:
                    ani.stop();
                    ani.start();
                    mMessRedIcon.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tea_content_layout, null);

        GalleryView gallery = (GalleryView) view.findViewById(R.id.gallery);
        gallery.setData(new int[]{R.drawable.blackboard_ad_img, R.drawable.blackboard_ad_img}, false);

        mTitleRight = (ImageView) view.findViewById(R.id.title_right);
        mTitleRight.setOnClickListener(this);

        ani = (AnimationDrawable) mTitleRight.getDrawable();
        ani.start();

        ImageView mTitleLeft = (ImageView) view.findViewById(R.id.title_left);
        mTitleLeft.setOnClickListener(this);
        mMessRedIcon = (ImageView) view.findViewById(R.id.mess_red);
        mMakeTopicBtn = (RelativeLayout) view.findViewById(R.id.make_topic_btn);
        mMakeTopicBtn.setOnClickListener(this);
        mTikuButton = (RelativeLayout) view.findViewById(R.id.tiku_btn);
        mTikuButton.setOnClickListener(this);
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
                mMessRedIcon.setVisibility(View.GONE);
                goMessageActivity();
                break;

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MySKService.handler = handler;
    }

    private void goMessageActivity() {
        Intent it = new Intent(getActivity(), MessageActivity.class);
        getActivity().startActivity(it);
    }
}
