package com.yshow.shike.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.Locale;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.Handler;
import android.text.format.DateFormat;
import android.widget.ImageView;

import com.yshow.shike.widget.StuTapeImage;

public class MediaPlayerUtil {
    private MediaPlayer mPlayer;
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 123:
                    String url = (String) msg.obj;
                    try {
                        Stop_Play();
                        mPlayer.setDataSource(url);
                        mPlayer.prepare();
                        mPlayer.start();
                    } catch (Exception e) {
                        mPlayer = null;
                        e.printStackTrace();
                    }
                    break;
            }
        }

        ;
    };

    public MediaPlayerUtil() {
        mPlayer = new MediaPlayer();
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (StuTapeImage.surrentPlayingTapeView != null) {
                    StuTapeImage.surrentPlayingTapeView.stopPlay();
                }
            }
        });
        File f = new File(Environment.getExternalStorageDirectory() + File.separator + "shike" + File.separator + "record");
        if (!f.exists()) {
            f.mkdirs();
        }
    }


    /**
     * 下载 语音
     *
     * @param urlStr
     */
    public void Down_Void(final String urlStr, final Context context) {
        new Thread(new Runnable() {
            private File file;

            @Override
            public void run() {
                try {
                    Net_Servse.getInstence().creat_file(context);
                    String voice_path = Environment.getExternalStorageDirectory() + File.separator + "shike" + File.separator + "voice";
                    String filename = urlStr.substring(urlStr.lastIndexOf("/") + 1);
                    file = new File(voice_path, filename);
                    if (file.exists()) {
                        handler.sendMessage(handler.obtainMessage(123, file.getAbsolutePath()));// 这地方..你怎么能只传文件夹名字
                    } else {
                        URL url = new URL(urlStr);
                        HttpURLConnection urlCon = (HttpURLConnection) url.openConnection();
                        urlCon.setRequestMethod("GET");
                        urlCon.setDoInput(true);
                        urlCon.connect();
                        InputStream inputStream = urlCon.getInputStream();
                        FileOutputStream outputStream = new FileOutputStream(file);
                        byte buffer[] = new byte[1024];
                        int bufferLength = 0;
                        while ((bufferLength = inputStream.read(buffer)) > 0) {
                            outputStream.write(buffer, 0, bufferLength);
                        }
                        outputStream.flush();
                        handler.sendMessage(handler.obtainMessage(123, file.getAbsolutePath()));// 这地方..你怎么能只传文件夹名字
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if (file != null) {
                        file.delete();
                    }
                }

            }
        }).start();
    }

    public void Stop_Play() {
        if (mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.stop();
        }
        mPlayer.reset();
    }

    /**
     * 本地播放
     */
    public void VoidePlay(String url) {
        handler.handleMessage(handler.obtainMessage(123, url));
    }
}
