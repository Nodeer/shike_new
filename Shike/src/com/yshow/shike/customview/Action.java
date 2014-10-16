package com.yshow.shike.customview;

import android.graphics.*;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

public class Action {
	public int color;

	Action() {
		color = Color.BLACK;
	}

	Action(int color) {
		this.color = color;
	}

	public void draw(Canvas canvas) {
	}

	public void move(float mx, float my) {
	}
}

class MyPath extends Action {
	Path path;
	int size;

	MyPath(float x, float y, int size, int color) {
		super(color);
		path = new Path();
		this.size = size;
		path.moveTo(x, y);
		path.lineTo(x, y);
	}

	public void draw(Canvas canvas) {
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setDither(true);
		paint.setColor(color);
		paint.setStrokeWidth(size);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeJoin(Paint.Join.ROUND);
		paint.setStrokeCap(Paint.Cap.ROUND);
		canvas.drawPath(path, paint);
	}
	public void move(float mx, float my) {
		path.lineTo(mx, my);
	}
}