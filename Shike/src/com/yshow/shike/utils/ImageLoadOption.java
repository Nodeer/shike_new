package com.yshow.shike.utils;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.yshow.shike.R;
import com.yshow.shike.UIApplication;

/**
 * Created by Administrator on 2014-10-19.
 */
public class ImageLoadOption {
    public static DisplayImageOptions getImageOption(int failbitmap) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheOnDisc(true).showImageForEmptyUri(failbitmap).showImageOnLoading(R.drawable.loading_img)//.displayer(new RoundedBitmapDisplayer(ScreenSizeUtil.Dp2Px(UIApplication.getInstance().getApplicationContext(),10)))
                .showImageOnFail(failbitmap).cacheInMemory(true).build();
        return options;
    }

    public static DisplayImageOptions getStuHeadImageOption() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheOnDisc(true).showImageForEmptyUri(R.drawable.stu_head_defult).showImageOnLoading(R.drawable.loading_img).displayer(new RoundedBitmapDisplayer(ScreenSizeUtil.Dp2Px(UIApplication.getInstance().getApplicationContext(), 10)))
                .showImageOnFail(R.drawable.stu_head_defult).cacheInMemory(true).build();
        return options;
    }


    public static DisplayImageOptions getStuHeadGrayImageOption() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheOnDisc(true).showImageForEmptyUri(R.drawable.stu_head_defult).showImageOnLoading(R.drawable.loading_img).displayer(new GrayAndRoundBitmapDisplayer(ScreenSizeUtil.Dp2Px(UIApplication.getInstance().getApplicationContext(), 10)))
                .showImageOnFail(R.drawable.stu_head_defult).cacheInMemory(true).build();
        return options;
    }

    public static DisplayImageOptions getTeaHeadImageOption() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheOnDisc(true).showImageForEmptyUri(R.drawable.tea_head_defult).showImageOnLoading(R.drawable.loading_img).displayer(new RoundedBitmapDisplayer(ScreenSizeUtil.Dp2Px(UIApplication.getInstance().getApplicationContext(), 10)))
                .showImageOnFail(R.drawable.tea_head_defult).cacheInMemory(true).build();
        return options;
    }

    public static DisplayImageOptions getTeaHeadGrayImageOption() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheOnDisc(true).showImageForEmptyUri(R.drawable.tea_head_defult).showImageOnLoading(R.drawable.loading_img).displayer(new GrayAndRoundBitmapDisplayer(ScreenSizeUtil.Dp2Px(UIApplication.getInstance().getApplicationContext(), 10)))
                .showImageOnFail(R.drawable.tea_head_defult).cacheInMemory(true).build();
        return options;
    }

    public static DisplayImageOptions getBigImageOption(int failbitmap) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheOnDisc(true).showImageForEmptyUri(failbitmap).showImageOnLoading(R.drawable.big_loading_img)
                .showImageOnFail(failbitmap).cacheInMemory(true).build();
        return options;
    }

}
