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
import android.widget.Toast;

import com.ant.liao.GifView;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.yshow.shike.R;
import com.yshow.shike.UIApplication;
import com.yshow.shike.activities.Activity_Stu_Ask_Step1;
import com.yshow.shike.activities.MessageActivity;
import com.yshow.shike.activities.WebViewActivity;
import com.yshow.shike.adapter.SKMessageAdapter;
import com.yshow.shike.entity.HomeImgModel;
import com.yshow.shike.service.MySKService;
import com.yshow.shike.utils.MyAsyncHttpResponseHandler;
import com.yshow.shike.utils.PartnerConfig;
import com.yshow.shike.utils.SKAsyncApiController;
import com.yshow.shike.utils.SKResolveJsonUtil;
import com.yshow.shike.utils.ScreenSizeUtil;
import com.yshow.shike.widget.GalleryView;
import com.yshow.shike.widget.MyPagerAdapter;

import java.util.ArrayList;

/**
 * Created by Administrator on 2014-10-16.
 * 学生首页
 */
public class StuContentFragment extends Fragment implements View.OnClickListener {
    private ImageView mMessRedIcon;
    private RelativeLayout mAskButton, mReStudyButton;
    private TextView mOnlineTextView;
    private ImageView mTitleRight;
    private AnimationDrawable ani;
    private String[] urls = {
            "http://www.shikeke.com/news.php?id=4",
            "http://www.shikeke.com/news.php?id=5",
            "http://www.shikeke.com/news.php?id=6"
    };

    private ArrayList<HomeImgModel> list;

    private GalleryView gallery;

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
        View view = inflater.inflate(R.layout.stu_content_layout, null);


        gallery = (GalleryView) view.findViewById(R.id.gallery);

        ImageView mTitleLeft = (ImageView) view.findViewById(R.id.title_left);
        mTitleLeft.setOnClickListener(this);

        mTitleRight = (ImageView) view.findViewById(R.id.title_right);
        mTitleRight.setOnClickListener(this);

        ani = (AnimationDrawable) mTitleRight.getDrawable();
        ani.start();
        mMessRedIcon = (ImageView) view.findViewById(R.id.mess_red);
        mAskButton = (RelativeLayout) view.findViewById(R.id.ask_question_btn);
        mAskButton.setOnClickListener(this);
        mReStudyButton = (RelativeLayout) view.findViewById(R.id.restudy_btn);
        mReStudyButton.setOnClickListener(this);
        mOnlineTextView = (TextView) view.findViewById(R.id.now_online);


        SKAsyncApiController.getHomePageImgs(new MyAsyncHttpResponseHandler(getActivity(), true) {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                boolean issuccess = SKResolveJsonUtil.getInstance().resolveIsSuccess(s);
                if (issuccess) {
                    list = SKResolveJsonUtil.getInstance().getHomePageImgs(s);
                    ArrayList<String> imgs = new ArrayList<String>();
                    for (HomeImgModel model : list) {
                        imgs.add(model.pic);
                    }
                    gallery.setData(imgs);
                    gallery.setOnItemClickListaner(new MyPagerAdapter.OnMyClickListener() {
                        @Override
                        public void clickItem(int position) {
                            Intent it = new Intent(getActivity(), WebViewActivity.class);
                            it.putExtra("url", list.get(position).url);
                            startActivity(it);
                        }
                    });
                } else {
                    gallery.setData(new int[]{R.drawable.blackboard_ad_img, R.drawable.blackboard_ad_img}, false);
                    gallery.setOnItemClickListaner(new MyPagerAdapter.OnMyClickListener() {
                        @Override
                        public void clickItem(int position) {
                            Intent it = new Intent(getActivity(), WebViewActivity.class);
                            it.putExtra("url", urls[position]);
                            startActivity(it);
                        }
                    });
                }
            }
        });

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
            case R.id.ask_question_btn:
                UIApplication.getInstance().cleanPicUrls();
                UIApplication.getInstance().cleanbitmaplist();
                PartnerConfig.TEATHER_ID = null;
                PartnerConfig.SUBJECT_ID = null;
                PartnerConfig.TEATHER_NAME = null;
                PartnerConfig.SUBJECT_NAME = null;
                PartnerConfig.TEACHER_IMG = null;
                startActivity(new Intent(getActivity(), Activity_Stu_Ask_Step1.class));
                break;
            case R.id.restudy_btn:
                Toast.makeText(getActivity(), "即将推出", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(getActivity(), My_Question_Count.class);
//                startActivity(intent);
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
