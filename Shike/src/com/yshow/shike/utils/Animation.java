package com.yshow.shike.utils;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
public class Animation {
	private  static TranslateAnimation translateAnimation;
	static Context context;
	private View translateView;
	private int viewWidth;
	private int translateY;
	public Animation() {
		super();
	}
	@SuppressWarnings("static-access")
	public Animation(Context context, View translateView, int viewWidth,int translateY) {
		super();
		this.context = context;
		this.translateView = translateView;
		this.viewWidth = viewWidth;
		this.translateY = translateY;
	}
	public void initData(boolean isUnfold) {
		if (isUnfold) {
			translateAnimation = new TranslateAnimation(0, 0, 0, dip2px(translateY, context));
			layoutDown();
			translateAnimation.setFillAfter(true);
			translateAnimation.setDuration(500);
			translateView.setAnimation(translateAnimation);
			translateView.startAnimation(translateAnimation);
		} else {
			translateAnimation = new TranslateAnimation(0, 0, 0, -dip2px(translateY, context));
			layoutUp();
			translateAnimation.setFillAfter(true);
			translateAnimation.setDuration(500);
			translateView.setAnimation(translateAnimation);
			translateView.startAnimation(translateAnimation);
		}
	}
	/**
	 * 按下时位移动画
	 */
	public void layoutUp() {
		translateAnimation.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationEnd(android.view.animation.Animation arg0) {
				translateView.clearAnimation();
				@SuppressWarnings("deprecation")
				LayoutParams lp = new LayoutParams(LayoutParams.FILL_PARENT,
						dip2px(viewWidth, context));
				lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,
						RelativeLayout.TRUE);
				lp.bottomMargin = 0;
				translateView.setLayoutParams(lp);
			}

			@Override
			public void onAnimationRepeat(android.view.animation.Animation arg0) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onAnimationStart(android.view.animation.Animation arg0) {
				// TODO Auto-generated method stub

			}
		});
	}

	/**
	 * 抬起时位移动画
	 */
	public void layoutDown() {
		translateAnimation.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationEnd(android.view.animation.Animation arg0) {
				translateView.clearAnimation();
				@SuppressWarnings("deprecation")
				LayoutParams lp = new LayoutParams(LayoutParams.FILL_PARENT,
						dip2px(viewWidth, context));
				lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,
						RelativeLayout.TRUE);
				lp.bottomMargin = -dip2px(translateY, context);
				translateView.setLayoutParams(lp);
			}
			@Override
			public void onAnimationRepeat(android.view.animation.Animation arg0) {

			}
			@Override
			public void onAnimationStart(android.view.animation.Animation arg0) {

			}
		});
	}
	public int dip2px(float dip, Context context) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dip * scale + 0.5f);
	}
}