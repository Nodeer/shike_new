package com.yshow.shike.widget;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.yshow.shike.R;
import com.yshow.shike.utils.MediaPlayerUtil;

/**
 * Created by iceman.xu on 2014/10/24.
 */
public class StuTapeImage extends FrameLayout implements View.OnClickListener {
    private String voicePath;
    private VoiceImageClickListener mVoiceImageClickListener;
    private ImageView del;
    private ImageView isReadImg;
    private MediaPlayerUtil mediaPlayerUtil;
    private ImageView imageView;
    private boolean isTeacher = false;
    private boolean isPlaying = false;
    private boolean isRead = false;

    public static StuTapeImage surrentPlayingTapeView;

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

    public void setPlayer(MediaPlayerUtil player) {
        this.mediaPlayerUtil = player;
    }

    public void setIsTeacher(boolean b) {
        this.isTeacher = b;
        if (isTeacher) {
            imageView.setImageResource(R.drawable.tea_tape_bg);
        } else {
            imageView.setImageResource(R.drawable.stu_tape_bg);
        }
    }

    private void addChilds() {
        imageView = new ImageView(getContext());
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

        isReadImg = new ImageView(getContext());
        isReadImg.setImageResource(R.drawable.red_icon);
        FrameLayout.LayoutParams pa2 = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        pa2.gravity = Gravity.RIGHT;
        isReadImg.setLayoutParams(pa2);
        isReadImg.setVisibility(View.GONE);
        addView(isReadImg);

    }

    @Override
    public void onClick(View v) {
        if (v.getTag().equals("img")) {
            if (surrentPlayingTapeView != null) {
                surrentPlayingTapeView.stopPlay();
            }
            if (voicePath.contains("http")) {
                mediaPlayerUtil.Down_Void(voicePath, getContext());
            } else {
                mediaPlayerUtil.VoidePlay(voicePath);
            }
            if (isTeacher) {
                imageView.setImageResource(R.drawable.tea_tape_ani);
            } else {
                imageView.setImageResource(R.drawable.stu_tape_ani);
            }
            AnimationDrawable drawable = (AnimationDrawable) imageView.getDrawable();
            drawable.stop();
            drawable.setOneShot(false);
            drawable.start();
            isPlaying = true;
            surrentPlayingTapeView = this;
            if (mVoiceImageClickListener != null) {
                mVoiceImageClickListener.onImageClick(this);
            }
        } else {
            if (isPlaying) {
                return;
            }
            ((ViewGroup) (getParent())).removeView(this);
            if (mVoiceImageClickListener != null) {
                mVoiceImageClickListener.onDelClick(this);
            }
        }
    }

    public void stopPlay() {
        Drawable drawable = imageView.getDrawable();
        if (drawable instanceof AnimationDrawable) {
            ((AnimationDrawable) drawable).stop();
        }
        if (isTeacher) {
            imageView.setImageResource(R.drawable.tea_tape_bg);
        } else {
            imageView.setImageResource(R.drawable.stu_tape_bg);
        }
        isPlaying = false;
    }

    public interface VoiceImageClickListener {
        void onImageClick(StuTapeImage img);

        void onDelClick(StuTapeImage img);
    }

    public void startPlaying() {

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

    public void setDelVisiable(boolean isShow) {
        if (isShow) {
            del.setVisibility(View.VISIBLE);
        } else {
            del.setVisibility(View.GONE);
        }
    }

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
        this.isReadImg.setVisibility(isRead ? View.GONE : View.VISIBLE);
    }

    public boolean getIsRead() {
        return this.isRead;
    }
}
