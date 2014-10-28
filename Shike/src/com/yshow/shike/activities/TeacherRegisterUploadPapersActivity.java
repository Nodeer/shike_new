package com.yshow.shike.activities;

import java.lang.reflect.Type;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yshow.shike.R;
import com.yshow.shike.UIApplication;
import com.yshow.shike.entity.SKStudent;
import com.yshow.shike.entity.UpLoad_Image;
import com.yshow.shike.entity.UpLoad_Image.Upload_Filed;
import com.yshow.shike.utils.Dilog_Share;
import com.yshow.shike.utils.MyAsyncHttpResponseHandler;
import com.yshow.shike.utils.SKAsyncApiController;
import com.yshow.shike.utils.SKResolveJsonUtil;

public class TeacherRegisterUploadPapersActivity extends BaseActivity {
    private SKStudent sKStudent;
    private Context context;
    private Intent intent = null;
    private Dialog tea_Reg; // 拍照和照相
    private Button skipButton;
    private ImageView uploadButton;
    private Bitmap bitmap;
    private String bitmapName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_register_uploadpaper_layout);
        initData();
    }

    private void initData() {
        context = this;
        tea_Reg = Dilog_Share.Tea_Reg(context, listener);
        sKStudent = (SKStudent) getIntent().getExtras().getSerializable("teather");

        TextView titleText = (TextView) findViewById(R.id.title_text);
        titleText.setText("教师资格证");

        uploadButton = (ImageView) findViewById(R.id.paper_btn);
        uploadButton.setOnClickListener(listener);
        skipButton = (Button) findViewById(R.id.next_btn);
        skipButton.setOnClickListener(listener);
        findViewById(R.id.left_btn).setOnClickListener(listener);
    }

    private OnClickListener listener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.left_btn:
                    finish();
                    break;
                case R.id.next_btn:
                    if (bitmap == null) {
                        intent = new Intent(context, TeacherRegisterUserInfoActivity.class);
                        intent.putExtra("teather", sKStudent);
                        startActivity(intent);
                    } else {
                        Reg_Imag(TeacherRegisterUploadPapersActivity.this, bitmap, bitmapName);
                    }
                    break;
                // 上传按钮
                case R.id.paper_btn:
                    tea_Reg.show();
                    break;
                // 拍照
                case R.id.tv_pai_zhao:
                    Take_Pickture();
                    UIApplication.getInstance().setReg_tea_three(true);
                    tea_Reg.dismiss();
                    break;
                // 相册
                case R.id.tv_xiagnc:
                    Take_Phone();
                    UIApplication.getInstance().setReg_tea_three(true);
                    tea_Reg.dismiss();
                    break;
                // 取消
                case R.id.tv_tea_cancel:
                    tea_Reg.dismiss();
                    break;
            }

        }
    };

    // 从相机拿数据
    private void Take_Pickture() {
        Intent take_phon = new Intent("android.media.action.IMAGE_CAPTURE");
        startActivityForResult(take_phon, 1);
    }

    // 相册拍照
    private void Take_Phone() {
        Intent photo = new Intent(Intent.ACTION_GET_CONTENT);
        photo.setType("image/*");
        startActivityForResult(photo, 2);
    }

    // 获取照相后的图片
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            long time = System.currentTimeMillis();
            bitmapName = time + "";
            String sd_state = Environment.getExternalStorageState();
            boolean SD_STATE = sd_state.equals(Environment.MEDIA_MOUNTED);
            if (!SD_STATE) {
                Toast.makeText(context, "请检查内存卡是否存在", Toast.LENGTH_SHORT).show();
                return;
            }
            ContentResolver resolver = getContentResolver();
            Uri originalUri; // 获得图片的uri
            switch (requestCode) {
                // 相机
                case 1:
                    originalUri = data.getData();
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(resolver, originalUri);
                        uploadButton.setImageBitmap(bitmap);
//                        Reg_Imag(context, bitmap, phon_name);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                // 相册
                case 2:
                    originalUri = data.getData();
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(resolver, originalUri);
                        uploadButton.setImageBitmap(bitmap);
//                        Reg_Imag(context, bitmap, phon_name);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }

    /**
     * 上传图片
     *
     * @param bitmap
     * @param path
     * @return
     */
    public void Reg_Imag(final Context context, Bitmap bitmap, String path) {
        SKAsyncApiController.Up_Loading_Tea(bitmap, path, new MyAsyncHttpResponseHandler(context, true) {
            @Override
            public void onSuccess(String json) {
                super.onSuccess(json);

                boolean success = SKResolveJsonUtil.getInstance().resolveIsSuccess(json, TeacherRegisterUploadPapersActivity.this);
                if (success) {
                    Type tp = new TypeToken<UpLoad_Image>() {
                    }.getType();
                    Gson gs = new Gson();
                    UpLoad_Image up_image = gs.fromJson(json, tp);
                    Upload_Filed data = up_image.getData();
                    String fileId = data.getFileId();
                    sKStudent.setPaper(fileId);
                    intent = new Intent(context, TeacherRegisterUserInfoActivity.class);
                    intent.putExtra("teather", sKStudent);
                    startActivity(intent);
                } else {
                    Toast.makeText(context, "图片上传失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}