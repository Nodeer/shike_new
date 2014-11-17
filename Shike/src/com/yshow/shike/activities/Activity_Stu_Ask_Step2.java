package com.yshow.shike.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.yshow.shike.R;
import com.yshow.shike.SubjectIds;
import com.yshow.shike.entity.LoginManage;
import com.yshow.shike.entity.SKStudent;
import com.yshow.shike.utils.Bitmap_Manger_utils;
import com.yshow.shike.utils.Dialog;
import com.yshow.shike.utils.ScreenSizeUtil;

/**
 * Created by Administrator on 2014-10-19.
 * 学生提问第二步,显示照片预览图,选择科目
 */
public class Activity_Stu_Ask_Step2 extends BaseActivity implements View.OnClickListener {
    private View mLastButton, mLastSelectIcon;
    private RelativeLayout mYuwenButton, mShuxueButton, mYingyuButton, mWuliButton, mHuaxueButton;
    private ImageView mYuwenSelectIcon, mShuxueSelectIcon, mYingyuSelectIcon, mWuliSelectIcon, mHuaxueSelectIcon;
    private ImageView[] mSelectIcons;
    private ImageView mImageview, mRemarkButton;
    private Bitmap_Manger_utils bitmapUtil;
    String imgPath;
    private Bitmap mBitmap;

    public static Bitmap sUploadBitmap;

    private String mSubjectId = "-1";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stu_ask_step2_layout);
        ImageView backBtn = (ImageView) findViewById(R.id.tv_tool_back);
        backBtn.setOnClickListener(this);
        mYuwenButton = (RelativeLayout) findViewById(R.id.yuwen_btn);
        mYuwenButton.setOnClickListener(this);
        mShuxueButton = (RelativeLayout) findViewById(R.id.shuxue_btn);
        mShuxueButton.setOnClickListener(this);
        mYingyuButton = (RelativeLayout) findViewById(R.id.yingyu_btn);
        mYingyuButton.setOnClickListener(this);
        mWuliButton = (RelativeLayout) findViewById(R.id.wuli_btn);
        mWuliButton.setOnClickListener(this);
        mHuaxueButton = (RelativeLayout) findViewById(R.id.huaxue_btn);
        mHuaxueButton.setOnClickListener(this);

        mYuwenSelectIcon = (ImageView) findViewById(R.id.yuwen_select_icon);
        mShuxueSelectIcon = (ImageView) findViewById(R.id.shuxue_select_icon);
        mYingyuSelectIcon = (ImageView) findViewById(R.id.yingyu_select_icon);
        mWuliSelectIcon = (ImageView) findViewById(R.id.wuli_select_icon);
        mHuaxueSelectIcon = (ImageView) findViewById(R.id.huaxue_select_icon);
        mImageview = (ImageView) findViewById(R.id.image);
        mRemarkButton = (ImageView) findViewById(R.id.remark_btn);
        mRemarkButton.setOnClickListener(this);
        mSelectIcons = new ImageView[]{mYuwenSelectIcon, mShuxueSelectIcon, mYingyuSelectIcon, mWuliSelectIcon, mHuaxueSelectIcon};
        bitmapUtil = Bitmap_Manger_utils.getIntence();
        imgPath = getIntent().getStringExtra("bitmap");
        if (imgPath != null) {
            mBitmap = press_bitmap(imgPath);
            mImageview.setImageBitmap(mBitmap);
        }
//        SKStudent aa = LoginManage.getInstance().getStudent();
//        if (aa.gradePart < 2) {//小学1,初中2,高中3.小学生屏蔽物理化学
//            mWuliButton.setEnabled(false);
//            mHuaxueButton.setEnabled(false);
//        } else {
//            mWuliButton.setOnClickListener(this);
//            mHuaxueButton.setOnClickListener(this);
//        }
        findViewById(R.id.next_btn).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.yuwen_btn:
                changeSelectBtn(v, 0);
                mSubjectId = SubjectIds.YUWEN;
                break;
            case R.id.shuxue_btn:
                changeSelectBtn(v, 1);
                mSubjectId = SubjectIds.SHUXUE;
                break;
            case R.id.yingyu_btn:
                changeSelectBtn(v, 2);
                mSubjectId = SubjectIds.YINGYU;
                break;
            case R.id.wuli_btn:
                if (checkAge()) {
                    changeSelectBtn(v, 3);
                    mSubjectId = SubjectIds.WULI;
                }
                break;
            case R.id.huaxue_btn:
                if (checkAge()) {
                    changeSelectBtn(v, 4);
                    mSubjectId = SubjectIds.HUAXUE;
                }
                break;
            case R.id.remark_btn:
                goRemarkScreen();
                break;
            case R.id.tv_tool_back:
                finish();
                break;
            case R.id.next_btn:
                if (!mSubjectId.equals("-1")) {
                    SubjectIds.mSubjectId = mSubjectId;
                    Bundle bun = new Bundle();
                    sUploadBitmap = mBitmap;
                    Dialog.intent(Activity_Stu_Ask_Step2.this, Activity_Stu_Add_Voice.class, bun);
                } else {
                    Toast.makeText(Activity_Stu_Ask_Step2.this, "请选择学科", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private boolean checkAge() {
        SKStudent aa = LoginManage.getInstance().getStudent();
        if (aa.gradePart < 2) {//小学1,初中2,高中3.小学生屏蔽物理化学
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("小学没有物理和化学,请重新选择科目");
            builder.setNegativeButton("确定", null);
            builder.show();
            return false;
        }
        return true;
    }

    private void goRemarkScreen() {
        Bundle bundle = new Bundle();
        bundle.putString("bitmap", imgPath);
        Intent it = new Intent(this, Activity_Add_Remark.class);
        it.putExtras(bundle);
        startActivityForResult(it, 1);
    }

    private void changeSelectBtn(View v, int index) {
        if (mLastButton == null) {
            mLastButton = v;
            mLastButton.setSelected(true);
            mLastSelectIcon = mSelectIcons[index];
            mLastSelectIcon.setVisibility(View.VISIBLE);
        } else {
            mLastButton.setSelected(false);
            mLastSelectIcon.setVisibility(View.GONE);
            mLastButton = v;
            mLastButton.setSelected(true);
            mLastSelectIcon = mSelectIcons[index];
            mLastSelectIcon.setVisibility(View.VISIBLE);
        }
    }

    private Bitmap press_bitmap(String path) {
        // 手机屏幕的宽
        int screenWidth = ScreenSizeUtil.getScreenWidth(this, 1);
        BitmapFactory.Options op = new BitmapFactory.Options();
        op.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, op);
        int op_h = op.outHeight - 1;//防止图片分辨率和屏幕分辨率一样的情况下被缩小
        int op_w = op.outWidth - 1;
        int rotateDegree = bitmapUtil.readPictureDegree(path);
        if (rotateDegree == 90) {
            int f = (int) (((float) op_h) / ((float) screenWidth));
            if (f == 0) {
                f = 1;
            }
            op.inSampleSize = f;
//            if(op_h>screenWidth*2){
//                op.inSampleSize = op_h / screenWidth;
//            }else{
//                op.inSampleSize = 1;
//            }
        } else {
            int f = (int) (((float) op_w) / ((float) screenWidth));
            if (f == 0) {
                f = 1;
            }
            op.inSampleSize = f;
//            if(op_w>screenWidth*2){
//                op.inSampleSize = op_w / screenWidth;
//            }else{
//                op.inSampleSize = 1;
//            }
        }
//        op.inSampleSize++;
        op.inJustDecodeBounds = false;
        op.inDensity = DisplayMetrics.DENSITY_DEFAULT;
        op.inTargetDensity = DisplayMetrics.DENSITY_DEFAULT;
        Bitmap getbitmap = BitmapFactory.decodeFile(path, op).copy(Bitmap.Config.RGB_565, true);
        while (getbitmap.getHeight() * getbitmap.getRowBytes() > Runtime.getRuntime().maxMemory() / 8) {
            op.inSampleSize++;
            getbitmap.recycle();
            getbitmap = BitmapFactory.decodeFile(path, op);
        }
        //对图片进行旋转
//        Bitmap new_rotae_bitmap = bitmapUtil.rotaingImageView(rotateDegree, getbitmap);
        return getbitmap;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                mBitmap = press_bitmap(data.getStringExtra("path"));
                mImageview.setImageBitmap(mBitmap);
            }
        }
    }
}
