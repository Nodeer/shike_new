package com.yshow.shike.utils;

import java.io.File;
import java.util.Calendar;
import java.util.Locale;

import android.content.Context;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;
import android.text.format.DateFormat;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * 录音工具类
 *
 * @author Administrator
 */
public class MediaRecorderUtil {
    private MediaRecorder mediaRecorder;
    private String filePath;
    private Context context;

    public MediaRecorderUtil(Context context) {
        this.context = context;
        mediaRecorder = new MediaRecorder();
        // 从麦克风源进行录音
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        // 设置输出格式
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
        // 设置编码格式
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        //设置输出文件
        mHandler = new Handler(context.getMainLooper());
    }

    private ImageView mVoiceLevelImg;

    public void setVoiceLevelImg(ImageView img) {
        mVoiceLevelImg = img;
    }

    private Handler mHandler;
    private Runnable mUpdateMicStatusTimer = new Runnable() {
        public void run() {
            updateMicStatus();
        }
    };
    private int BASE = 600;
    private int SPACE = 200;// 间隔取样时间

    private void updateMicStatus() {
        int ratio = mediaRecorder.getMaxAmplitude() / BASE;
        int db = 0;// 分贝
        if (ratio > 1) {
            db = (int) (20 * Math.log10(ratio));
        }
        System.out.println("分贝值：" + db + "     " + Math.log10(ratio));
        mVoiceLevelImg.setImageLevel(db);
        mHandler.postDelayed(mUpdateMicStatusTimer, SPACE);
    }

    public boolean startRecorder() {
        if (mediaRecorder != null) {
            try {
                // 对SD卡是否存在进行判断
                boolean boolear = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
                if (boolear) {
                    new DateFormat();
                    filePath = Environment.getExternalStorageDirectory() + File.separator + "shike" + File.separator + "record" + File.separator + DateFormat.format("yyyyMMdd_HHmmss", Calendar.getInstance(Locale.CHINA)) + ".amr";
                    File file = new File(filePath);
                    mediaRecorder = new MediaRecorder();
                    // 从麦克风源进行录音
                    mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    // 设置输出格式
                    mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
                    // 设置编码格式
                    mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
                    mediaRecorder.setOutputFile(file.getAbsolutePath());
                    // 创建文件
                    file.createNewFile();
                    // 准备录制
                    mediaRecorder.prepare();
                    mediaRecorder.start();
                    mHandler.post(mUpdateMicStatusTimer);
                    return boolear;
                } else {
                    Toast.makeText(context, "SD卡不存在,请安装SD卡", 0).show();
                    return boolear;
                }
            } catch (Exception e) {
                Toast.makeText(context, "准备失败", 0).show();
                return false;
            }
        }
        return false;
    }

    public void stopRecorder() {
        if (mediaRecorder != null) {
            mediaRecorder.release();
            mHandler.removeCallbacks(mUpdateMicStatusTimer);
        }
    }

    public String getFilePath() {
        return filePath;
    }

    public void Give_Up_voide() {
        mediaRecorder.release();
        mHandler.removeCallbacks(mUpdateMicStatusTimer);
    }
}
