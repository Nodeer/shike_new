package com.yshow.shike.utils;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.yshow.shike.R;

/**
 * Created by Administrator on 2014-10-19.
 */
public class ImageLoadOption {
    public static DisplayImageOptions getImageOption(int failbitmap) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheOnDisc(true).showImageForEmptyUri(failbitmap).showImageOnLoading(R.drawable.loading_img).displayer(new RoundedBitmapDisplayer(10))
                .showImageOnFail(failbitmap).cacheInMemory(true).build();
        return options;
    }
    public static DisplayImageOptions getBigImageOption(int failbitmap) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheOnDisc(true).showImageForEmptyUri(failbitmap).showImageOnLoading(R.drawable.big_loading_img)
                .showImageOnFail(failbitmap).cacheInMemory(true).build();
        return options;
    }

}
