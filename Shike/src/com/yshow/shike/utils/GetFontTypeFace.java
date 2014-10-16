package com.yshow.shike.utils;

import android.content.Context;
import android.graphics.Typeface;

public class GetFontTypeFace {

	public static Typeface getFontStyle(Context context){
		return Typeface.createFromAsset(context.getAssets(), "fonts/shikeke.ttf");
	}
	
}
