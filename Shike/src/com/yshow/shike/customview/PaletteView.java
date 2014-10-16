package com.yshow.shike.customview;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Environment;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import com.yshow.shike.R;
import com.yshow.shike.utils.Bitmap_Manger_utils;
import com.yshow.shike.utils.Dialog;
import com.yshow.shike.utils.ImageUtil;
import com.yshow.shike.utils.ScreenSizeUtil;
import com.yshow.shike.utils.ImageUtil.ScalingLogic;
public class PaletteView extends SurfaceView implements Runnable,SurfaceHolder.Callback{
	// 画板的坐标以及宽高
	private int bgBitmapX = 0;
	private int bgBitmapY = 0;
	private int bgBitmapHeight;
	private int bgBitmapWidth;
	private int currentColor = Color.BLACK;
	private int currentSize = 3; // 1,3,5
	private int currentPaintIndex = -1;
	private boolean isBackPressed = false;
	private boolean isForwardPressed = false;
	// 存储画笔的动作
	private ArrayList<Action> actionList = null;
	// 当前的画笔实�?
	private Action curAction = null;
	boolean mLoop = true;
	SurfaceHolder mSurfaceHolder = null;
	// 绘图区背景图片
	private Bitmap bgBitmap = null;
	// 临时画板用来显示之前已经绘制过的图像
//	private Bitmap newbit = null;
	private Paint mPaint = null;
//	private Canvas canvasTemp;
//	private Context context;
	private Bitmap_Manger_utils bitmap_utils;
	private float scaleX;
	private float scaleY;
	private String bgBitmap_path;
    private Bitmap resultBitmap;
	// 设置画板颜色
	public void setCurrentColor(int currentColor) {
		this.currentColor = currentColor;
	}
	// 设置画笔的大小
	public void setCurrentSize(int currentSize) {
		this.currentSize = currentSize;
	}
	public PaletteView(Context context, AttributeSet arr) {
		super(context, arr);
//		this.context = context;
		bgBitmapHeight = ScreenSizeUtil.getScreenHeight(getContext()); // 获取手机屏幕的高
		bgBitmapWidth = ScreenSizeUtil.getScreenWidth(getContext(), 1);// 获取手机屏幕的宽
		mPaint = new Paint();
		actionList = new ArrayList<Action>();
		mSurfaceHolder = this.getHolder();
		mSurfaceHolder.addCallback(this);
		this.setFocusable(true);
		Resources res = context.getResources();
		bgBitmap = BitmapFactory.decodeResource(res, R.drawable.transparent).copy(Bitmap.Config.ARGB_8888, true);
        bitmap_utils = Bitmap_Manger_utils.getIntence();
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return super.onKeyDown(keyCode, event);
	}

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int antion = event.getAction();
        if (antion == MotionEvent.ACTION_CANCEL) {
            return false;
        }
        float touchX = event.getX();
        float touchY = event.getY();
//		touchX = touchX/scaleX;//这地方缩放你妹啊...超过范围了就画不上去
//		touchY = touchY/scaleY;
        // 点击 按下的动作
        if (antion == MotionEvent.ACTION_DOWN) {
            curAction = new MyPath(getRealX(touchX), getRealY(touchY),currentSize, currentColor);
            clearSpareAction();
        }
        // 拖动移动
        if (antion == MotionEvent.ACTION_MOVE) {
            if (curAction != null) {
                curAction.move(getRealX(touchX), getRealY(touchY));
            }
        }
        // 抬起时
        if (antion == MotionEvent.ACTION_UP) {
            if (curAction != null) {
                curAction.move(getRealX(touchX), getRealY(touchY));
                actionList.add(curAction);
                currentPaintIndex++;
                curAction = null;
            }
            isBackPressed = false;
            isForwardPressed = false;
        }
        return true;
    }

	@Override
	public void run() {
		while (mLoop) {
			try {
				Thread.sleep(50);
				synchronized (mSurfaceHolder) {
					Draw();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		mLoop = true;
		new Thread(this).start();
	}
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		mLoop = true;
	}
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		mLoop = false;
	}
	// 后退删除的操作
	public boolean Delete() {
		if (isBackPressed) {
			return false;
		}
		if (currentPaintIndex >= 0) {
			currentPaintIndex--;
		}
		return true;
	}
	// 恢复的操作
	public boolean Recover() {
		if (isForwardPressed) {
			return false;
		}
		if ((currentPaintIndex + 1) < actionList.size()) {
			currentPaintIndex++;
		}
		return true;
	}
	// 绘图
	protected void Draw() {
        Canvas canvas = mSurfaceHolder.lockCanvas();
		if (mSurfaceHolder == null || canvas == null) {
			return;
		}
		// 填充背景
		canvas.drawColor(Color.WHITE);
		// 画主画板
		drawMainPallent(canvas);
		mSurfaceHolder.unlockCanvasAndPost(canvas);
	}
	// 画主画板
	private void drawMainPallent(Canvas canvas) {
		// 设置画笔没有锯齿
		mPaint.setAntiAlias(true);
		mPaint.setStyle(Paint.Style.STROKE);
		// 画板绘图区背景图片
		Matrix matrix = new Matrix();
		matrix.reset();
		scaleX = (float) getWidth() / bgBitmap.getWidth();
		scaleY = (float) getHeight() / bgBitmap.getHeight();
		matrix.setScale((float) getWidth() / bgBitmap.getWidth(),(float) getHeight() / bgBitmap.getHeight());
//		newbit = Bitmap.createBitmap(getWidth(), getHeight(), Config.ARGB_4444);
//		canvasTemp = new Canvas(newbit);
//		canvasTemp.drawColor(Color.TRANSPARENT);
        canvas.drawBitmap(bgBitmap, matrix, mPaint);
        for (int i = 0; i <= currentPaintIndex; i++) {
            actionList.get(i).draw(canvas);
        }
        // 画当前画笔痕迹
        if (curAction != null) {
            curAction.draw(canvas);
        }
//		canvas.drawBitmap(newbit,new Matrix(), mPaint);
	}
	public String getBigBitmapUrl() {
        resultBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Config.ARGB_4444);
        Canvas canvas = new Canvas(resultBitmap);
        drawMainPallent(canvas);
        File f = new File(Environment.getExternalStorageDirectory() + File.separator + "shike" + File.separator + "img");
        if(!f.exists()){
            f.mkdirs();
        }
        String url = Environment.getExternalStorageDirectory() + File.separator + "shike" + File.separator + "img" + File.separator + new DateFormat().format("yyyyMMdd_HHmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";
        File file = new File(url);
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ImageUtil.writeBitmap(resultBitmap, url);//此处保存图片至本地大约耗时2S.
		return url;
	}
    // 获取处理过的图片
	public Bitmap getNewBitmap() {
		Bitmap new_bm = Dialog.scaleImg(resultBitmap, 360, 360);
		return new_bm;
	}
	// 根据接触点x坐标得到画板上对应x坐标
	public float getRealX(float x) {
		return x - bgBitmapX;
	}
	// 根据接触点y坐标得到画板上对应y坐标
	public float getRealY(float y) {
		return y - bgBitmapY;
	}
	public void setBgBitmap1(Bitmap bgBitmap,String bgBitmap_path) {
		this.bgBitmap = bgBitmap;
		this.bgBitmap_path = bgBitmap_path;
}
	// 后退和前进完成后，缓存的动动作
	void clearSpareAction() {
		for (int i = actionList.size() - 1; i > currentPaintIndex; i--) {
			actionList.remove(i);
		}
	}
}