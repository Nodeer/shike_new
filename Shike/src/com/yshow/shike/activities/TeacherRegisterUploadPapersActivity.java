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
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.umeng.analytics.MobclickAgent;
import com.yshow.shike.R;
import com.yshow.shike.UIApplication;
import com.yshow.shike.entity.SKStudent;
import com.yshow.shike.entity.UpLoad_Image;
import com.yshow.shike.entity.UpLoad_Image.Upload_Filed;
import com.yshow.shike.utils.Dilog_Share;
import com.yshow.shike.utils.MyAsyncHttpResponseHandler;
import com.yshow.shike.utils.SKAsyncApiController;

public class TeacherRegisterUploadPapersActivity extends BaseActivity {
    private SKStudent sKStudent;
    private Context context;
    private Intent intent = null;
    private Dialog tea_Reg; // 拍照和照相
    private TextView tv_skip, ShagnChuan;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teather_material_two);
        initData();
    }

    private void initData() {
        context = this;
        tea_Reg = Dilog_Share.Tea_Reg(context, listener);
        sKStudent = (SKStudent) getIntent().getExtras().getSerializable("teather");
        ShagnChuan = (TextView) findViewById(R.id.tv_ShagnChuan);
        ShagnChuan.setOnClickListener(listener);
        tv_skip = (TextView) findViewById(R.id.tv_give_up);
        tv_skip.setOnClickListener(listener);
        findViewById(R.id.tv_back).setOnClickListener(listener);
        findViewById(R.id.next_btn).setOnClickListener(listener);
    }

    private OnClickListener listener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_back:
                    finish();
                    break;
                case R.id.tv_comfig:
                    intent = new Intent(context, TeacherRegisterUserInfoActivity.class);
                    intent.putExtra("teather", sKStudent);
                    startActivity(intent);
                    break;
                // 上传按钮
                case R.id.tv_ShagnChuan:
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
                // 跳过按钮
                case R.id.tv_give_up:
                    intent = new Intent(context, Activity_Teather_Material_Three.class);
                    intent.putExtra("student", sKStudent);
                    startActivity(intent);
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
            String phon_name = time + "";
            String sd_state = Environment.getExternalStorageState();
            boolean SD_STATE = sd_state.equals(Environment.MEDIA_MOUNTED);
            if (!SD_STATE) {
                Toast.makeText(context, "请检查内存卡是否存在", 0).show();
                return;
            }
            Bitmap bitmap;
            ContentResolver resolver = getContentResolver();
            Uri originalUri; // 获得图片的uri
            switch (requestCode) {
                // 相机
                case 1:
                    originalUri = data.getData();
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(resolver, originalUri);
                        Reg_Imag(context, bitmap, phon_name);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                // 相册
                case 2:
                    originalUri = data.getData();
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(resolver, originalUri);
                        Reg_Imag(context, bitmap, phon_name);
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
                Type tp = new TypeToken<UpLoad_Image>() {
                }.getType();
                Gson gs = new Gson();
                UpLoad_Image up_image = gs.fromJson(json, tp);
                Upload_Filed data = up_image.getData();
                String fileId = data.getFileId();
                sKStudent.setPaper(fileId);
                ShagnChuan.setText("已上传");
                tv_skip.setBackgroundColor(R.color.gray);
                tv_skip.setOnClickListener(null);
            }
        });
    }

}