package com.yshow.shike.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by iceman on 2014/8/7.
 */
public class MatrixImageview extends ImageView implements View.OnTouchListener {
	private ImageView mImageView;
	private final Matrix matrix = new Matrix();
	private final Matrix savedMatrix = new Matrix();
	private DisplayMetrics mDisplyMetrcs;
	private Bitmap mBitmap;

	private float minScaleR;// 最小缩放比例
	private static final float MAX_SCALE = 8f;// 最大缩放比例

	private static final int NONE = 0;// 初始状态
	private static final int DRAG = 1;// 拖动
	private static final int ZOOM = 2;// 缩放
	private int mode = NONE;
    private boolean hasZoom = false;

	private final PointF prev = new PointF();
	private final PointF mid = new PointF();
	private float dist = 1f;
    private int downX,downY;

	public MatrixImageview(Context context) {
		super(context);
	}

	public MatrixImageview(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MatrixImageview(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void setBitmap(Bitmap bit) {
		mImageView = this;
		mBitmap = bit;
		mImageView.setImageBitmap(mBitmap);
		// 为图片控件添加触控事件
		mImageView.setOnTouchListener(this);
		// 获取当前屏幕分辨率对象
		mDisplyMetrcs = new DisplayMetrics();
		Activity activity = (Activity) this.getContext();
		activity.getWindowManager().getDefaultDisplay().getMetrics(mDisplyMetrcs);
		this.setMinZoom();
		this.setCenter();
		new Handler().postDelayed(new Runnable() {
			public void run() {
				// 程序启动0.5秒以后设置图片控件的缩放属性
				// 如果在描述文件或一开始就设置，那么，图片就会出现在屏幕的左上角，而我们希望图片出现在屏幕的中间位置
				mImageView.setScaleType(ScaleType.MATRIX);
			}
		}, 500);
		mImageView.setImageMatrix(matrix);
	}

	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		// 主点按下
		case MotionEvent.ACTION_DOWN:
            hasZoom = false;
			savedMatrix.set(matrix);
			prev.set(event.getX(), event.getY());
            downX = (int) event.getX();
            downY = (int) event.getY();
			break;
		// 副点按下
		case MotionEvent.ACTION_POINTER_DOWN:
			dist = spacing(event);
			// 如果连续两点距离大于10，则判定为多点模式
			if (spacing(event) > 10f) {
				savedMatrix.set(matrix);
				midPoint(mid, event);
				mode = ZOOM;
			}
			break;
		case MotionEvent.ACTION_UP:
            int disX = (int) (event.getX()-downX);
            int disY = (int) (event.getY()-downY);
            if(!hasZoom&&Math.abs(disX)<10&&Math.abs(disY)<10){
                ((Activity)getContext()).finish();
            }
            mode = NONE;
            break;
		case MotionEvent.ACTION_POINTER_UP:
            savedMatrix.set(matrix);
			mode = NONE;
			break;
		case MotionEvent.ACTION_MOVE:
            if (mode == ZOOM) {
                hasZoom = true;
                float newDist = spacing(event);
				if (newDist > 10f) {
					matrix.set(savedMatrix);
					float tScale = newDist / dist;
                    matrix.postScale(tScale, tScale, mid.x, mid.y);
				}
			} else {
                mode = DRAG;
				matrix.set(savedMatrix);
				matrix.postTranslate(event.getX() - prev.x, event.getY() - prev.y);
			}
			break;
		}
		mImageView.setImageMatrix(matrix);
		this.checkView();
		return true;
	}

	private void checkView() {
		float p[] = new float[9];
		matrix.getValues(p);
        if (mode == ZOOM) {
			if (p[0] < minScaleR){
                matrix.setScale(minScaleR, minScaleR);
            }
            if (p[0] > MAX_SCALE){
                matrix.set(savedMatrix);
            }
        }
        this.setCenter();
	}

	/**
	 * 设置最小缩放比列，最大值和图片大小相等
	 */
	private void setMinZoom() {
		minScaleR = Math.min((float) mDisplyMetrcs.widthPixels / (float) mBitmap.getWidth(), (float) mDisplyMetrcs.heightPixels
				/ (float) mBitmap.getHeight());
		// if (minScaleR < 1.0)
		matrix.postScale(minScaleR, minScaleR);
	}

	private void setCenter() {
		this.setCenter(true, true);
	}

	private void setCenter(boolean horizontal, boolean vertical) {
		Matrix mMatrix = new Matrix();
		mMatrix.set(matrix);
		RectF mRectF = new RectF(0, 0, mBitmap.getWidth(), mBitmap.getHeight());
		mMatrix.mapRect(mRectF);

		float height = mRectF.height();
		float width = mRectF.width();

		float deltaX = 0, deltaY = 0;

		if (vertical) {
			// 图片小于屏幕大小，则居中显示。大于屏幕，上方留空则往上移，下方留空则往下移
			int screenHeight = mDisplyMetrcs.heightPixels;
			if (height < screenHeight)
				deltaY = (screenHeight - height) / 2 - mRectF.top;
			else if (mRectF.top > 0)
				deltaY = -mRectF.top;
			else if (mRectF.bottom < screenHeight)
				deltaY = mImageView.getHeight() - mRectF.bottom;
		}

		if (horizontal) {
			int screenWidth = mDisplyMetrcs.widthPixels;
			if (width < screenWidth)
				deltaX = (screenWidth - width) / 2 - mRectF.left;
			else if (mRectF.left > 0)
				deltaX = -mRectF.left;
			else if (mRectF.right < screenWidth)
				deltaX = screenWidth - mRectF.right;
		}
		matrix.postTranslate(deltaX, deltaY);
	}

	/**
	 * 两点的距离
	 * 
	 * @param event
	 * @return
	 */
	private float spacing(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return FloatMath.sqrt(x * x + y * y);
	}

	/**
	 * 两点的中点
	 * 
	 * @param point
	 * @param event
	 */
	private void midPoint(PointF point, MotionEvent event) {
		float x = event.getX(0) + event.getX(1);
		float y = event.getY(0) + event.getY(1);
		point.set(x / 2, y / 2);
	}
}
