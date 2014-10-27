package com.yshow.shike;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Environment;

import com.baidu.frontia.FrontiaApplication;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.yshow.shike.utils.SharePreferenceUtil;

public class UIApplication extends FrontiaApplication {
    private List<Bitmap> bitmaplist = new ArrayList<Bitmap>();
    private static UIApplication instance;
    public Boolean isTestUser;// 自动注册标识
    //	private String NickName;// 自动登录用户名
    private String mac;// wifi网卡地址
    private Boolean reg_tea_three = false; // 老师注册页面3
    private Boolean attention_tea;
    private String user_name; // 把用户名显示到账户信息
    private List<String> myPicUrls = new ArrayList<String>();

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public Boolean getAttention_tea() {
        return attention_tea;
    }

    public void setAttention_tea(Boolean attention_tea) {
        this.attention_tea = attention_tea;
    }

    public Boolean getReg_tea_three() {
        return reg_tea_three;
    }

    public void setReg_tea_three(Boolean reg_tea_three) {
        this.reg_tea_three = reg_tea_three;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public void onCreate() {
        super.onCreate();
        initImageLoader(getApplicationContext());
        instance = this;
        SharePreferenceUtil.init(this);
//		CrashHandler crashHandler = CrashHandler.getInstance();
//	    crashHandler.init(getApplicationContext());

    }

    public static void initImageLoader(Context context) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .writeDebugLogs().build();
        ImageLoader.getInstance().init(config);
    }

    @SuppressWarnings("rawtypes")
    public List getList() {
        return bitmaplist;
    }

    public static UIApplication getInstance() {
        return instance;
    }

    public void addPicUrls(String picurl) {
        myPicUrls.add(picurl);
    }

    public List<String> getPicUrls() {
        return myPicUrls;
    }

    public void cleanbitmaplist() {
        for (Bitmap bitmap : bitmaplist) {
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
                bitmap = null;
            }
        }
        System.gc();
        bitmaplist.clear();
        bitmaplist = new ArrayList<Bitmap>();
    }

    public void cleanPicUrls() {
        for (String path : myPicUrls) {
            File file = new File(path);
            if (file.exists()) {
                file.delete();
            }
        }
        myPicUrls.clear();
        myPicUrls = new ArrayList<String>();
    }

    public File getExternalStorageDirectory() {
        File externalStorageDirectory = Environment.getExternalStorageDirectory();
        @SuppressWarnings("deprecation")
        int sdk = Integer.valueOf(Build.VERSION.SDK);
        if (sdk >= 8 && externalStorageDirectory == null) {
            externalStorageDirectory = getExternalFilesDir(null);
        }
        if (externalStorageDirectory != null) {
            externalStorageDirectory = new File(externalStorageDirectory.getAbsolutePath(), "shikeke");
            if (!externalStorageDirectory.exists()) {
                if (!externalStorageDirectory.mkdirs()) {
                    externalStorageDirectory = null;
                }
            }
        }
        return externalStorageDirectory;
    }

    @Override
    public String toString() {
        return "UIApplication [bitmaplist=" + bitmaplist + ", Auid_flag="
                + isTestUser + ", mac=" + mac + ", reg_tea_three="
                + reg_tea_three + ", attention_tea=" + attention_tea
                + ", user_name=" + user_name + ", myPicUrls=" + myPicUrls;
    }
}
