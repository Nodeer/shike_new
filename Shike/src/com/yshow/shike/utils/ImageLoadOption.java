package com.yshow.shike.utils;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.yshow.shike.R;

/**
 * Created by Administrator on 2014-10-19.
 */
public class ImageLoadOption {
    public DisplayImageOptions getImageOption(int failbitmap) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheOnDisc(true).showImageForEmptyUri(failbitmap).showImageOnLoading(R.drawable.xiaoxi_moren)
                .showImageOnFail(failbitmap).cacheInMemory(true).build();
        return options;
    }

}
