package com.yshow.shike.activities;

import android.content.Intent;
import android.util.DisplayMetrics;

import com.yshow.shike.entity.LoginManage;
import com.yshow.shike.fragments.Fragment_Message;
import com.yshow.shike.service.MySKService;
import com.yshow.shike.utils.*;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.yshow.shike.R;
import com.yshow.shike.customview.PaletteView;

/**
 * 老师题库页面,点添加以后进入/学生提问时,拍照或者选择照片以后进入..
 * 反正就是在图片上绘制的页面
 */
public class Activity_Add_Remark extends BaseActivity implements OnClickListener {
    private Context context;
    private PaletteView paletteView = null; // 初始化画笔
    private ProgressDialogUtil dialogUtil;// 连网精度条
    private TextView next_tool;
    private Bitmap bitmap;
    private Bundle extras;
    private String questionId;
    private boolean isContinue;
    private String bigBitmapUrl;
    private Bitmap_Manger_utils bitmap_intence;
    private ImageView mRedPaint, mBluePaint;
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    if (isContinue && !questionId.equals("")) {
                        skUploadeImage(questionId, bitmap);
                        dialogUtil.show();
                        return;
                    }
                    dialogUtil.dismiss();
                    Intent it = new Intent();
                    it.putExtra("path", bigBitmapUrl);
                    setResult(Activity.RESULT_OK, it);
                    finish();
                    break;
            }
        }

        ;
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stu_paint_activity);
        context = this;
        initData();
        dialogUtil = new ProgressDialogUtil(this);
    }

    private void initData() {
        bitmap_intence = Bitmap_Manger_utils.getIntence();
        paletteView = (PaletteView) findViewById(R.id.stu_palette);
        extras = getIntent().getExtras();
        if (extras != null) {//这个地方..有的页面没有传东西过来...做个空保护
            questionId = extras.getString("questionId");
            isContinue = extras.getBoolean("isContinue");
        }
        next_tool = (TextView) findViewById(R.id.next_btn);
        if(LoginManage.getInstance().isTeacher()){
            next_tool.setBackgroundResource(R.drawable.tea_blue_btn);
        }
        if (isContinue && !questionId.equals("")) {
            next_tool.setText("发送");
        }
        findViewById(R.id.tv_tool_back).setOnClickListener(this);
        next_tool.setOnClickListener(this);
        findViewById(R.id.pain_cexiao).setOnClickListener(this);
        findViewById(R.id.pain_clear).setOnClickListener(this);
        mRedPaint = (ImageView) findViewById(R.id.paint_red);
        mRedPaint.setOnClickListener(this);
        mBluePaint = (ImageView) findViewById(R.id.paint_blue);
        mBluePaint.setOnClickListener(this);
        paletteView.setCurrentColor(Color.RED);
        mRedPaint.setSelected(true);
        // 是否是 照相还是 从相册里面拿图片 进行判断
        String path = null;
        if (extras != null) {
            path = getIntent().getExtras().getString("bitmap");
        }
        if (path != null) {
            Bitmap press_bitmap = press_bitmap(path);
            int pbw = press_bitmap.getWidth();
            int pbh = press_bitmap.getHeight();
            ScreenSizeUtil.ScreenWidth = pbw;
            ScreenSizeUtil.ScreenHeight = pbh;
            paletteView.setBgBitmap1(press_bitmap, path);
        }
    }

    private Bitmap press_bitmap(String path) {
        // 手机屏幕的宽
        int screenWidth = ScreenSizeUtil.getScreenWidth(this, 1);
        Options op = new BitmapFactory.Options();
        op.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, op);
        int op_h = op.outHeight;
        int op_w = op.outWidth;
        int h = 0;
        int rotateDegree = bitmap_intence.readPictureDegree(path);
        if (rotateDegree == 90) {
            h = screenWidth * op_w / op_h;
        } else {
            h = screenWidth * op_h / op_w;
        }
        if (rotateDegree == 90) {
            op.inSampleSize = op_h / screenWidth;
        } else {
            op.inSampleSize = op_w / screenWidth;
        }
        op.inSampleSize++;
        op.inJustDecodeBounds = false;
//		op.inSampleSize = op_w/screenWidth;
        op.inDensity = DisplayMetrics.DENSITY_DEFAULT;
        op.inTargetDensity = DisplayMetrics.DENSITY_DEFAULT;
        Bitmap getbitmap = BitmapFactory.decodeFile(path, op).copy(Config.RGB_565, true);
        while (getbitmap.getHeight() * getbitmap.getRowBytes() > Runtime.getRuntime().maxMemory() / 8) {
            op.inSampleSize++;
            getbitmap.recycle();
            getbitmap = BitmapFactory.decodeFile(path, op);
        }
        //对图片进行旋转
        Bitmap new_rotae_bitmap = bitmap_intence.rotaingImageView(rotateDegree, getbitmap);
        return new_rotae_bitmap;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_tool_back:
                finish();
                break;
            case R.id.next_btn:
                dialogUtil.show();
                new Thread() {
                    public void run() {
                        //查看华过后的原图
                        bigBitmapUrl = paletteView.getBigBitmapUrl();
//                        UIApplication.getInstance().addPicUrls(bigBitmapUrl);
                        // 预览图片
//                        bitmap = paletteView.getNewBitmap();
                        handler.sendEmptyMessage(0);
                    }

                    ;
                }.start();
                break;
            // 返回上一步画板上的数据
            case R.id.pain_cexiao:
                paletteView.Delete();
                break;
            // 清除画板上的数据
            case R.id.pain_clear:
                paletteView.clear();
                break;
            case R.id.paint_red:
                mRedPaint.setSelected(true);
                mBluePaint.setSelected(false);
                paletteView.setCurrentColor(Color.RED);
                break;
            case R.id.paint_blue:
                mBluePaint.setSelected(true);
                mRedPaint.setSelected(false);
                paletteView.setCurrentColor(Color.BLUE);
                break;
        }
    }

    private void skUploadeImage(final String questionId, Bitmap bitmap) {
        // 改成 上传图片 改成原图的url了
        int screenWidth = ScreenSizeUtil.getScreenWidth(this, 1);
        int screenHeight = ScreenSizeUtil.getScreenHeight(this);
        screenWidth = ScreenSizeUtil.ScreenWidth;
        screenHeight = ScreenSizeUtil.ScreenHeight;
        Bitmap getbitmap = Dialog.getbitmap(bigBitmapUrl, screenWidth, screenHeight);
        SKAsyncApiController.skUploadImage(questionId, getbitmap,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(String json) {
                        super.onSuccess(json);
                        dialogUtil.dismiss();
                        boolean resolveIsSuccess = SKResolveJsonUtil.getInstance().resolveIsSuccess(json, context);
                        if (resolveIsSuccess) {
                            skSend_messge(questionId, "0");
                            dialogUtil.show();
                        }
                    }
                });
    }


    private void skSend_messge(final String questionId, String isSend) {
        SKAsyncApiController.skSend_messge(questionId, "", isSend,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(String content) {
                        super.onSuccess(content);
                        dialogUtil.dismiss();
                        String error = null;
                        try {
                            JSONObject jsonObject = new JSONObject(content);
                            int optBoolean = jsonObject.optInt("success");
                            if (optBoolean == 1) {
                                String data = jsonObject.optString("data");
                                String type = jsonObject.optString("type");
                                if (type.equals("confirm")) {
                                    AlertDialog.Builder dia = new Builder(context);
                                    dia.setMessage(data);
                                    dia.setNegativeButton("取消", null);
                                    dia.setPositiveButton(
                                            "确定",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    skSend_messge(questionId, "1");
                                                }
                                            });
                                    dia.show();
                                } else {
                                    Intent it = new Intent();
                                    if (LoginManage.getInstance().isTeacher()) {
                                        Fragment_Message.handler.sendEmptyMessage(MySKService.HAVE_NEW_MESSAGE);
                                        it.setClass(Activity_Add_Remark.this, Tea_Message_Detail_Activity.class);
                                        startActivity(it);
                                    } else {
                                        Fragment_Message.handler.sendEmptyMessage(MySKService.HAVE_NEW_MESSAGE);
                                        it.setClass(Activity_Add_Remark.this, Stu_Message_Detail_Activity.class);
                                        startActivity(it);
                                    }
                                }
                            } else {
                                error = jsonObject.optString("error");
                                Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(context, "服务异常", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }
        System.gc();
    }
}