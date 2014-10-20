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

import com.yshow.shike.activities.MessageActivity;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tea_content_layout, null);

        GalleryView gallery = (GalleryView) view.findViewById(R.id.gallery);
//        gallery.setData();

        ImageView mTitleLeft = (ImageView) view.findViewById(R.id.title_left);
        mTitleLeft.setOnClickListener(this);
        ImageView mTitleRight = (ImageView) view.findViewById(R.id.title_right);
        mTitleRight.setOnClickListener(this);
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
                goMessageActivity();
                break;

        }
    }

    private void goMessageActivity() {
        Intent it = new Intent(getActivity(), MessageActivity.class);
        getActivity().startActivity(it);
    }
}
