package com.yshow.shike.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.yshow.shike.utils.ScreenSizeUtil;

/**
 * Created by Administrator on 2014-10-28.
 */
public class CustomLinearLayout extends LinearLayout {
    public CustomLinearLayout(Context context) {
        super(context);
    }

    public CustomLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomLinearLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        int mTotalWidth = 0;
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            int measureHeight = childView.getMeasuredHeight();
            int measuredWidth = childView.getMeasuredWidth();

            if (i == 0) {
                measuredWidth = ScreenSizeUtil.getScreenWidth(getContext(), 1);
            }
            // 获取在onMeasure中计算的视图尺寸
            childView.layout(mTotalWidth, t, measuredWidth + mTotalWidth, b);
            mTotalWidth += measuredWidth;
        }
    }
}
