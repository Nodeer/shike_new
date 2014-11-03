package com.yshow.shike.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.assist.LoadedFrom;
import com.nostra13.universalimageloader.core.display.BitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

/**
 * Created by iceman.xu on 2014/11/3.
 */
public class GrayAndRoundBitmapDisplayer extends RoundedBitmapDisplayer {
    private final int mRoundPixels;

    public GrayAndRoundBitmapDisplayer(int roundPixels) {
        super(roundPixels);
        this.mRoundPixels = roundPixels;
    }


    @Override
    public Bitmap display(Bitmap old, ImageView imageView, LoadedFrom loadedFrom) {
        Bitmap roundedBitmap = roundCorners(old, imageView, mRoundPixels);
        int height = roundedBitmap.getHeight();
        int width = roundedBitmap.getWidth();
        Bitmap newBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas c = new Canvas(newBitmap);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawARGB(255,250,250,250);
        c.drawBitmap(roundedBitmap, 0, 0, paint);
        imageView.setImageBitmap(newBitmap);
        return newBitmap;
    }
}
