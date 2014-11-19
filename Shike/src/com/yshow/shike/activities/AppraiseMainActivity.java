package com.yshow.shike.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yshow.shike.R;
import com.yshow.shike.entity.Fase_Packs;
import com.yshow.shike.entity.SKMessage;
import com.yshow.shike.fragments.Fragment_Message;
import com.yshow.shike.service.MySKService;
import com.yshow.shike.utils.Dialog;
import com.yshow.shike.utils.MyAsyncHttpResponseHandler;
import com.yshow.shike.utils.ProgressDialogUtil;
import com.yshow.shike.utils.SKAsyncApiController;
import com.yshow.shike.utils.SKResolveJsonUtil;

import java.util.ArrayList;

import pl.droidsonroids.gif.GifImageView;

/**
 * Created by Administrator on 2014-10-25.
 */
public class AppraiseMainActivity extends BaseActivity implements View.OnClickListener, DialogInterface.OnDismissListener {
    private final int GIVE_PRAISE = 2001;
    private final int GIVE_GIFT = 2002;
    private String praiseFileId;
    private String questionId;
    private SKMessage message;
    private boolean isMyTeacher;
    private String useTeacherId;
    private ProgressDialogUtil progress;
    private String zan_fileId, fileId;//赞美老师fileid,给老师积分的Fileid,学生问题的id
    private String[] Jifen_file_id = new String[0];
    private int count;
    private int dialogInt;

    private Fase_Packs mFaceModel;

    private GifImageView mGiftImg, appraise_img;

    private ImageLoader mImageLoader;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appraise_main_layout);
        findViewById(R.id.tv_tool_back).setOnClickListener(this);
        findViewById(R.id.next_btn).setOnClickListener(this);
        findViewById(R.id.give_gift_btn).setOnClickListener(this);
        findViewById(R.id.give_praise_btn).setOnClickListener(this);
        mGiftImg = (GifImageView) findViewById(R.id.gift_img);
        appraise_img = (GifImageView) findViewById(R.id.appraise_img);
        mImageLoader = ImageLoader.getInstance();
        message = (SKMessage) getIntent().getExtras().getSerializable("message");
        if (message != null) {
            String claim_uid = message.getClaim_uid();
            questionId = message.getQuestionId();
            String s = message.getiSmyteach();
            if (s.equals("1")) {
                isMyTeacher = true;
                useTeacherId = claim_uid;
            } else {
                useTeacherId = claim_uid;
            }
        }
        progress = new ProgressDialogUtil(this);
        progress.setOndismissListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.next_btn:
                confing();
                break;
            case R.id.give_gift_btn:
                startActivityForResult(new Intent(AppraiseMainActivity.this, GiveGiftActivity.class), GIVE_GIFT);
                break;
            case R.id.give_praise_btn:
                startActivityForResult(new Intent(AppraiseMainActivity.this, GivePraiseActivity.class), GIVE_PRAISE);
                break;
            case R.id.tv_tool_back:
                IsGood_Bad();
                break;
        }

    }

    /**
     * 点击放弃
     */
    private void IsGood_Bad() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("你是否满意这个老师");
        builder.setPositiveButton("满意",
                new android.content.DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        ThankTeatherWithOutJifen();
                    }
                });
        builder.setNegativeButton("不满意",
                new android.content.DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Complain_Teather();
                    }
                });
        builder.show();
    }


    /**
     * 点击确定
     */
    private void confing() {
        if (count != 0) {//积分不为0
            thankConfirm();
        } else {//无积分,无赞
            Thank_Teather(questionId);
        }
    }

    /**
     * 送积分确认
     */
    private void thankConfirm() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("你确定要送" + count + "分給这位老师吗？");
        builder.setPositiveButton("确定",
                new android.content.DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        Thank_Teather(questionId);
                    }
                });
        builder.setNegativeButton("取消",
                new android.content.DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });
        builder.show();
    }

    /**
     * 给老师送赞
     */
    private void Send_Fase_Tea(String zanId, final String questionId) {
        SKAsyncApiController.Send_Fase(zanId, questionId, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String json) {
                super.onSuccess(json);
                dialogInt--;
                if (dialogInt == 0) {
                    progress.dismiss();
                }
            }


            @Override
            public void onStart() {
                super.onStart();
                if (dialogInt != 0) {
                    progress.show();
                }
            }
        });
    }

    /**
     * 给老师送积分
     */
    private void Send_Jifen_Tea(String jifenId, final String questionId) {
        SKAsyncApiController.Think_Teather2(questionId, useTeacherId, jifenId, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String json) {
                super.onSuccess(json);
                dialogInt--;
                if (dialogInt == 0) {
                    progress.dismiss();
                }
            }

            @Override
            public void onStart() {
                super.onStart();
                if (dialogInt != 0) {
                    progress.show();
                }
            }
        });
    }

    // 投诉老师
    private void Complain_Teather() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("你是否要投诉这位老师");
        builder.setPositiveButton("是",
                new android.content.DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("message", message);
                        Dialog.intent(AppraiseMainActivity.this, Activity_Compain_Teather.class, bundle);
                        finish();
                    }
                });
        builder.setNegativeButton("否", new android.content.DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                Fragment_Message.handler.sendEmptyMessage(MySKService.HAVE_NEW_MESSAGE);
                finish();
            }
        });
        builder.show();
    }

    // 结束问题的最后流程,赞已经在前面发送过了.这里主要是标记问题已解决,同时如果有积分,也发过去
    public void Thank_Teather(final String questionId) {
        dialogInt = 0;
        dialogInt += Jifen_file_id.length;
        if (count != 0) {
            dialogInt++;
        }
        if (mFaceModel != null) { //送老师赞的图片
            dialogInt++;
            zan_fileId = mFaceModel.getFileId();
        }
        if (dialogInt != 0) {
            if (count != 0) {//发积分图片,送积分
                for (int i = 0; i < Jifen_file_id.length; i++) {
                    String jifen_id = Jifen_file_id[i];
                    Send_Fase_Tea(jifen_id, questionId);
                }
                Send_Jifen_Tea(String.valueOf(count), questionId);
            }
            if (mFaceModel != null) { //送老师赞的图片
                Send_Fase_Tea(zan_fileId, questionId);
            }
        } else {
            thankTeacher();
        }
    }

    // 送0分.不送赞
    public void ThankTeatherWithOutJifen() {
        thankTeacher();
    }

    private void addMyTeacher() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("他/她还不是您的老师,现在是否添加他/她为您的老师？");
        builder.setPositiveButton("确定",
                new android.content.DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        add_Teather(useTeacherId);
                    }
                });
        builder.setNegativeButton("取消",
                new android.content.DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Fragment_Message.handler.sendEmptyMessage(MySKService.HAVE_NEW_MESSAGE);
                        finish();
                    }
                });
        builder.show();
    }

    private void add_Teather(String uid) {
        SKAsyncApiController.Attention_Taeather_Parse(uid,
                new MyAsyncHttpResponseHandler(this, true) {
                    public void onSuccess(int arg0, String jion) {
                        super.onSuccess(arg0, jion);
                        Fragment_Message.handler.sendEmptyMessage(MySKService.HAVE_NEW_MESSAGE);
                        finish();
                    }
                });
    }

    private void thankTeacher() {
        SKAsyncApiController.Think_Teather2(questionId, useTeacherId, "0", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String json) {
                super.onSuccess(json);
                boolean issuccess = SKResolveJsonUtil.getInstance().resolveIsSuccess(json);
                if (issuccess) {
                    if (!isMyTeacher) {
                        addMyTeacher();
                    } else {
                        Fragment_Message.handler.sendEmptyMessage(MySKService.HAVE_NEW_MESSAGE);
                        finish();
                    }
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GIVE_PRAISE) {
                int index = data.getIntExtra("index", -1);
                if (index != -1) {
                    mFaceModel = (Fase_Packs) data.getSerializableExtra("data");
                    int resId = getResources().getIdentifier("gif" + mFaceModel.getFileId(), "drawable", getPackageName());
                    if (resId != 0) {
                        appraise_img.setImageResource(resId);
                    } else {
                        appraise_img.setImageResource(R.drawable.icon_give_praise);
                    }
                } else {
                    mFaceModel = null;
                    appraise_img.setImageResource(R.drawable.icon_give_praise);
                }
            } else if (requestCode == GIVE_GIFT) {
                count = data.getIntExtra("count", 0);
                if (count != 0) {
                    String files = data.getStringExtra("data");
                    Jifen_file_id = files.split(",");
                    String url = data.getStringExtra("url");//这里已经改成fileId了
                    int resId = getResources().getIdentifier("gif" + url, "drawable", getPackageName());
                    if (resId != 0) {
                        mGiftImg.setImageResource(resId);
                    } else {
                        mGiftImg.setImageResource(R.drawable.icon_give_gift);
                    }
                } else {
                    Jifen_file_id = new String[0];
                    mGiftImg.setImageResource(R.drawable.icon_give_gift);
                }
            }
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if (Jifen_file_id.length != 0) {
            if (!isMyTeacher) {
                addMyTeacher();
            } else {
                Fragment_Message.handler.sendEmptyMessage(MySKService.HAVE_NEW_MESSAGE);
                finish();
            }
        } else {
            thankTeacher();
        }
    }
}
