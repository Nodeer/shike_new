/**
 * @ProjectName:CYFrameAndroid
 * @Title: CYouPagerAdapter.java
 * @Package com.cyou.cyframeandroid.adapter
 * @Description: TODO(继承pageadapter, 实现了页面的添加和切换, 对于viewpage都适用)
 * @author liuqi qiliu_17173@cyou-inc.com   
 * @date 2013-8-6 下午1:45:08 
 * @version V1.0
 * Copyright (c) 2013搜狐公司-版权所有
 */
package com.yshow.shike.widget;

import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * @author yuquan_y yuquanyan@cyou-inc.com
 * @ClassName: CYouPagerAdapter
 * @Description: CyouGallery的adapter
 * 可在其内设置点击事件,继承pageadapter,实现了页面的添加和切换,对于viewpage都适用
 * @date 2013-8-6 下午5:15:52
 */
@SuppressLint("UseSparseArrays")
public class MyPagerAdapter extends PagerAdapter {

    @SuppressWarnings("unused")
    private Context mContext;

    /**
     * 数据源.
     */
    private List<View> mListViews = null;

    /**
     * views
     */
    @SuppressWarnings("unused")
    private HashMap<Integer, View> mViews = null;

    private OnMyClickListener listener;


    /**
     * 构造方法
     *
     * @param context the context
     * @param
     */
    public MyPagerAdapter(Context context, List<View> mListViews) {
        this.mContext = context;
        this.mListViews = mListViews;
        this.mViews = new HashMap<Integer, View>();
    }

    /**
     * 描述：获取数量.
     *
     * @return the count
     */
    @Override
    public int getCount() {
        return mListViews.size();
    }

    /**
     * 描述：Object是否对应这个View.
     *
     * @param arg0 the arg0
     * @param arg1 the arg1
     * @return true, if is view from object
     */
    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == (arg1);
    }

    /**
     * 描述：显示View.
     *
     * @param container the container
     * @param position  the position
     * @return the object
     */
    @Override
    public Object instantiateItem(View container, final int position) {
        View v = mListViews.get(position);
        /*if(mListener != null){
			v.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					mListener.clickItem(position);
				}
			});
		}*/
        ((ViewPager) container).addView(v);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null){
                    listener.clickItem(position);
                }
            }
        });
        return v;
    }

    /**
     * 描述：移除View.
     *
     * @param container the container
     * @param position  the position
     * @param object    the object
     */
    @Override
    public void destroyItem(View container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }

    /**
     * 描述：很重要，否则不能notifyDataSetChanged.
     *
     * @param object the object
     * @return the item position
     */
    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public void setMyClickListener(OnMyClickListener lis) {
        this.listener = lis;
    }

    public interface OnMyClickListener {
        public void clickItem(int position);
    }
}