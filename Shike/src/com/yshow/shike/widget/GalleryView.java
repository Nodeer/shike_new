package com.yshow.shike.widget;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yshow.shike.R;
import com.yshow.shike.utils.ImageLoadOption;
import com.yshow.shike.utils.Net_Servse;

/**
 * @author yuquan_y yuquanyan@cyou-inc.com
 * @ClassName: CYouGalleryView
 * @Description: TODO 自定义gallery 可监听滑动 设置滑动速度、可添加 删除视图、可停止滑动
 * @date 2013-8-7 上午10:31:54
 * <p/>
 * modify by 徐斌.2013.10.23.增加了手动向前/向后切换图片的功能.
 */
public class GalleryView extends LinearLayout {
    /**
     * 切换图片成功,而且可以继续切换
     */
    public static final int SWITCH_CONTINUE = 2;
    /**
     * 切换图片成功,但已经到底了
     */
    public static final int SWITCH_STOP = 1;
    /**
     * 当前已经在尽头,无法切换
     */
    public static final int SWITCH_INALID = 0;
    /**
     * The context.
     */
    private Context context;

    private ViewPager mViewPager;

    /**
     * 圆点布局.
     */
    private LinearLayout pageLineLayout;

    private int count, i;
    /**
     * 当前的圆点和普通圆点
     */
    private Bitmap currentImage, publicImage;

    /**
     * 滑动改变监听器.
     */
    private CYouOnChangeListener mCYouChangeListener;

    /**
     * 滑动监听.
     */
    private CYouOnScrolledListener mCYouScrolledListener;

    /**
     * The layout params ww.
     */
    public LayoutParams layoutParamsWW = null;

    /**
     * The m list views.
     */
    private ArrayList<View> mListViews = null;

    /**
     * The m ab view pager adapter.
     */
    private MyPagerAdapter mCYouViewPagerAdapter = null;

    /**
     * 默认 320*40 The width.
     */
    @SuppressWarnings("unused")
    private int width = 320;

    /**
     * The height.
     */
    @SuppressWarnings("unused")
    private int height = 480;

    /**
     * 时间间隔
     */
    private long time = 4000;

    /**
     * 圆点默认居右
     */
    private int pageLineHorizontalGravity = Gravity.CENTER;

    DisplayImageOptions options;
    ImageLoader loader;

    /**
     * Instantiates.
     *
     * @param context the context
     * @param attrs   the attrs
     */
    @SuppressWarnings("deprecation")
    public GalleryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        layoutParamsWW = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        this.setOrientation(LinearLayout.VERTICAL);

        mViewPager = new ViewPager(context);

        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
        lp1.weight = 1;
        addView(mViewPager, lp1);
// 位置的点
        pageLineLayout = new LinearLayout(context);
        pageLineLayout.setBackgroundColor(context.getResources().getColor(R.color.pageline_bg));
        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        addView(pageLineLayout, lp2);


        currentImage = BitmapFactory.decodeResource(getResources(), R.drawable.img_navigation_orange);
        publicImage = BitmapFactory.decodeResource(getResources(), R.drawable.img_navigation_grey);

        mListViews = new ArrayList<View>();
        mCYouViewPagerAdapter = new MyPagerAdapter(context, mListViews);
        mViewPager.setAdapter(mCYouViewPagerAdapter);
        mViewPager.setFadingEdgeLength(0);

        mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                makesurePosition();
                onPageSelectedCallBack(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                onPageScrolledCallBack(position);
            }

        });
        WindowManager wManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wManager.getDefaultDisplay();
        width = display.getWidth();
        height = display.getHeight();

//        options = Net_Servse.getInstence().Picture_Shipei(R.drawable.blackboard_ad_img);
        options = ImageLoadOption.getBigImageOption(R.drawable.blackboard_ad_img);

        loader = ImageLoader.getInstance();
    }

    public void setOnItemClickListaner(MyPagerAdapter.OnMyClickListener listener) {
        mCYouViewPagerAdapter.setMyClickListener(listener);
    }

    /**
     * 创建圆点
     */
    public void creatIndex() {
        pageLineLayout.removeAllViews();
        pageLineLayout.setHorizontalGravity(pageLineHorizontalGravity);
        count = mListViews.size();
        for (int j = 0; j < count; j++) {
            ImageView imageView = new ImageView(context);
            layoutParamsWW.setMargins(2, 5, 2, 5);
            imageView.setLayoutParams(layoutParamsWW);
            if (j == 0) {
                imageView.setImageBitmap(currentImage);
            } else {
                imageView.setImageBitmap(publicImage);
            }
            pageLineLayout.addView(imageView, j);
        }
    }

    /**
     * 重新设置圆点位置.
     */
    public void makesurePosition() {
        i = mViewPager.getCurrentItem();
        for (int j = 0; j < count; j++) {
            if (i == j) {
                ((ImageView) pageLineLayout.getChildAt(i)).setImageBitmap(currentImage);
            } else {
                ((ImageView) pageLineLayout.getChildAt(j)).setImageBitmap(publicImage);
            }
        }
    }

    /**
     * @Title: getTime
     * @Description: 返回时间间隔
     */
    public long getTime() {
        return time;
    }

    /**
     * @Title: setTime
     * @Description: 设置时间间隔
     */
    public void setTime(long time) {
        this.time = time;
    }

    /**
     * 描述：添加数据以默认的方式显示数据(数据源来自网络). map 中图片key(img) 文字key(text) type=1 为有图 有字
     * type=2 仅有图 isAdd:是否为添加数据
     */
    public void setData(List<Map<String, String>> data, int type, boolean isAdd) {
        int count = data.size();

        if (count < 2) {
            pageLineLayout.setVisibility(View.GONE);
        } else {
            pageLineLayout.setVisibility(View.VISIBLE);
        }

        if (!isAdd) {
            removeAllViews();
        }
        switch (type) {
            case 1:
                for (int i = 0; i < count; i++) {
                    View view = LayoutInflater.from(context).inflate(R.layout.mygallery_view_item, null);
                    ImageView mImage = (ImageView) view.findViewById(R.id.mImage);
                    TextView mText = (TextView) view.findViewById(R.id.mText);
                    Map<String, String> map = data.get(i);
                    loader.displayImage(map.get("img"), mImage, options);
                    mText.setText(map.get("text"));

                    addView(view);
                }
                if (count != 1)
                    startPlay();
                break;
            case 2:
                for (int i = 0; i < count; i++) {
                    View view = LayoutInflater.from(context).inflate(R.layout.mygallery_view_item, null);
                    ImageView mImage = (ImageView) view.findViewById(R.id.mImage);
                    mImage.setScaleType(ImageView.ScaleType.FIT_XY);
                    TextView mText = (TextView) view.findViewById(R.id.mText);
                    mText.setVisibility(View.GONE);
                    final Map<String, String> map = data.get(i);
                    loader.displayImage(map.get("img"), mImage, options);
                    view.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                        }
                    });

                    addView(view);
                }
                if (count != 1)
                    startPlay();
                break;
        }

    }

    public void setData(List<String> data) {
        int count = data.size();

        if (count < 2) {
            pageLineLayout.setVisibility(View.GONE);
        } else {
            pageLineLayout.setVisibility(View.VISIBLE);
        }

        removeAllViews();
        for (int i = 0; i < count; i++) {
            View view = LayoutInflater.from(context).inflate(R.layout.mygallery_view_item, null);
            ImageView mImage = (ImageView) view.findViewById(R.id.mImage);
            TextView mText = (TextView) view.findViewById(R.id.mText);
            mText.setVisibility(View.GONE);
            final String s = data.get(i);
            loader.displayImage(s, mImage, options);
            view.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                }
            });

            addView(view);
        }
        if (count != 1)
            startPlay();

    }

    /**
     * 描述：添加数据以默认的方式显示数据(数据源来自本地).
     */
    public void setData(int[] images, boolean isAdd) {
        if (!isAdd) {
            removeAllViews();
        }
        int count = images.length;

        if (count < 2) {
            pageLineLayout.setVisibility(View.GONE);
        } else {
            pageLineLayout.setVisibility(View.VISIBLE);
        }

        for (int i = 0; i < count; i++) {
            View view = LayoutInflater.from(context).inflate(R.layout.mygallery_view_item, null);
            ImageView mImage = (ImageView) view.findViewById(R.id.mImage);
            TextView mText = (TextView) view.findViewById(R.id.mText);
            mText.setVisibility(View.GONE);
            addView(view);
            mImage.setImageResource(images[i]);

        }
//        if (count != 1) {
//            startPlay();
//        }

    }

    /**
     * 描述：添加可播放视图.
     */
    public void addView(View view) {
        mListViews.add(view);
        mCYouViewPagerAdapter.notifyDataSetChanged();
        creatIndex();
    }

    /**
     * 描述：添加可播放视图列表.
     */
    public void addViews(List<View> views) {
        mListViews.addAll(views);
        mCYouViewPagerAdapter.notifyDataSetChanged();
        creatIndex();
    }

    /**
     * 描述：删除可播放视图.
     */
    @Override
    public void removeAllViews() {
        mListViews.clear();
        mCYouViewPagerAdapter.notifyDataSetChanged();
        creatIndex();
    }

    /**
     * 描述：设置页面切换事件.
     */
    private void onPageScrolledCallBack(int position) {
        if (mCYouScrolledListener != null) {
            mCYouScrolledListener.onScroll(position);
        }

    }

    /**
     * 描述：设置页面切换事件.
     */
    private void onPageSelectedCallBack(int position) {
        if (mCYouChangeListener != null) {
            mCYouChangeListener.onChange(position);
        }

    }

    /**
     * The handler.
     */
    private Handler handler = new Handler();

    /**
     * The runnable.
     */
    private Runnable runnable = new Runnable() {
        public void run() {
            if (mViewPager != null) {
                mViewPager.post(new Runnable() {
                    public void run() {
                        int count = mListViews.size();
                        int i = mViewPager.getCurrentItem();
                        if (i < count - 1 || i == 0) {
                            i++;
                        } else {
                            i = 0;
                        }
                        mViewPager.setCurrentItem(i, true);
                    }
                });
            }
            handler.postDelayed(this, time);
        }
    };

    /**
     * 描述：自动轮播.
     */
    public void startPlay() {
        if (handler != null) {
            handler.postDelayed(runnable, time);
        }
    }

    /**
     * 描述：自动轮播.
     */
    public void stopPlay() {
        if (handler != null) {
            handler.removeCallbacks(runnable);
        }
    }

    /**
     * 描述：设置页面切换的监听器.
     */
    public void setOnPageChangeListener(CYouOnChangeListener abChangeListener) {
        mCYouChangeListener = abChangeListener;
    }

    /**
     * 描述：设置页面滑动的监听器.
     */
    public void setOnPageScrolledListener(CYouOnScrolledListener abScrolledListener) {
        mCYouScrolledListener = abScrolledListener;
    }

    /**
     * 设置圆点的图片
     *
     * @param displayImage 当前的图片圆点
     * @param hideImage    普通未选中的图片圆点
     */
    public void setPageLineImage(Bitmap displayImage, Bitmap hideImage) {
        this.currentImage = displayImage;
        this.publicImage = hideImage;
        creatIndex();

    }

    /**
     * 描述：获取这个滑动的ViewPager类.
     *
     * @return the view pager
     */
    public ViewPager getViewPager() {
        return mViewPager;
    }

    /**
     * 描述：获取当前的View的数量.
     *
     * @return the count
     */
    public int getCount() {
        return mListViews.size();
    }

    /**
     * 描述：设置页圆点显示条的位置,在AddView前设置.
     */
    public void setPageLineHorizontalGravity(int horizontalGravity) {
        pageLineHorizontalGravity = horizontalGravity;
    }

    /**
     * 显示下一张图片
     *
     * @return 本次操作返回码
     */
    public int goNext() {
        if (mListViews.size() == 0 || mViewPager.getCurrentItem() == (mListViews.size() - 1)) {
            return SWITCH_INALID;
        } else {
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
            makesurePosition();
            if (mViewPager.getCurrentItem() == (mListViews.size() - 1)) {
                return SWITCH_STOP;
            } else {
                return SWITCH_CONTINUE;
            }
        }
    }

    /**
     * 显示上一张图片
     *
     * @return 本次操作返回码
     */
    public int goPrevious() {
        if (mListViews.size() == 0 || mViewPager.getCurrentItem() == 0) {
            return SWITCH_INALID;
        } else {
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
            makesurePosition();
            if (mViewPager.getCurrentItem() == 0) {
                return SWITCH_STOP;
            } else {
                return SWITCH_CONTINUE;
            }
        }
    }

    // 以下为回调接口
    public interface CYouOnScrolledListener {

        /**
         * On scroll.
         *
         * @param position the position
         */
        public void onScroll(int position);

    }

    public interface CYouOnChangeListener {

        /**
         * On change.
         *
         * @param position the position
         */
        public void onChange(int position);

    }
}
