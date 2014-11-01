package com.yshow.shike.activities;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yshow.shike.R;
import com.yshow.shike.utils.Dialog;
import com.yshow.shike.utils.SharePreferenceUtil;

/**
 * Created by iceman.xu on 2014/11/1.
 */
public class StartHelpActivity extends BaseActivity {
    private int[] imgs = {R.drawable.starting_01, R.drawable.starting_02, R.drawable.starting_03};

    private View[] mViews;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_slide_layout);
        View view1 = getLayoutInflater().inflate(R.layout.start_viewpager_item, null);
        ImageView img1 = (ImageView) view1.findViewById(R.id.img);
        img1.setImageResource(imgs[0]);
        View view2 = getLayoutInflater().inflate(R.layout.start_viewpager_item, null);
        ImageView img2 = (ImageView) view2.findViewById(R.id.img);
        img2.setImageResource(imgs[1]);
        View view3 = getLayoutInflater().inflate(R.layout.start_viewpager_item, null);
        view3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharePreferenceUtil.getInstance().putBoolean("need_slide", true);
                Dialog.Intent(StartHelpActivity.this, Login_Reg_Activity.class);
                finish();
            }
        });
        ImageView img3 = (ImageView) view3.findViewById(R.id.img);
        img3.setImageResource(imgs[2]);


        mViews = new View[]{view1, view2, view3};


        ViewPager pager = (ViewPager) findViewById(R.id.viewpager);
        MyPageAdapter adapter = new MyPageAdapter();
        pager.setAdapter(adapter);
    }

    private class MyPageAdapter extends PagerAdapter {

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mViews[position]);
            return mViews[position];
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mViews[position]);
        }

        @Override
        public int getCount() {
            return imgs.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view == o;
        }
    }

}
