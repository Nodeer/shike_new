package com.yshow.shike.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.yshow.shike.R;

/**
 * Created by iceman.xu on 2014/10/24.
 */
public class StuTapeImage extends FrameLayout implements View.OnClickListener {
    private String voicePath;
    private VoiceImageClickListener mVoiceImageClickListener;
    private ImageView del;

    public StuTapeImage(Context context) {
        super(context);
        addChilds();
    }

    public StuTapeImage(Context context, AttributeSet attrs) {
        super(context, attrs);
        addChilds();
    }

    public StuTapeImage(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        addChilds();
    }

    private void addChilds() {
        ImageView imageView = new ImageView(getContext());
        imageView.setImageResource(R.drawable.stu_tape_bg);
        addView(imageView);
        imageView.setTag("img");
        imageView.setOnClickListener(this);

        del = new ImageView(getContext());
        del.setImageResource(R.drawable.icon_delete);
        FrameLayout.LayoutParams pa = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        pa.gravity = Gravity.RIGHT;
        del.setLayoutParams(pa);
        del.setTag("del");
        del.setVisibility(View.GONE);
        del.setOnClickListener(this);
        addView(del);
    }

    @Override
    public void onClick(View v) {
        if (mVoiceImageClickListener != null) {
            if (v.getTag().equals("img")) {
                mVoiceImageClickListener.onImageClick(this);
            } else {
                mVoiceImageClickListener.onDelClick(this);
            }
        }
    }

    public interface VoiceImageClickListener {
        void onImageClick(StuTapeImage img);

        void onDelClick(StuTapeImage img);
    }

    public void setVoicePath(String path) {
        this.voicePath = path;
    }


    public String getVoicePath() {
        return this.voicePath;
    }

    public void setmVoiceImageClickListener(VoiceImageClickListener listener) {
        this.mVoiceImageClickListener = listener;
    }

    public void changeDelView() {
        del.setVisibility(del.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
    }
}
