package com.yshow.shike.activities;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.MediaStore;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;
import com.yshow.shike.R;
import com.yshow.shike.entity.LoginManage;
import com.yshow.shike.entity.SKArea;
import com.yshow.shike.entity.SKGrade;
import com.yshow.shike.entity.SKStudent;
import com.yshow.shike.entity.UpLoad_Image;
import com.yshow.shike.entity.UpLoad_Image.Upload_Filed;
import com.yshow.shike.entity.Update_User_Info;
import com.yshow.shike.entity.User_Info;
import com.yshow.shike.utils.AreaSeltorUtil;
import com.yshow.shike.utils.DataSeleUtil;
import com.yshow.shike.utils.DataSeleUtil.Data_Seltor_Listening;
import com.yshow.shike.utils.Dilog_Share;
import com.yshow.shike.utils.Exit_Login;
import com.yshow.shike.utils.GradeSeltorUtil.SystemDialogButtonOnclickListening;
import com.yshow.shike.utils.Grade_Level_Utils.GradeSeltorUtilButtonOnclickListening;
import com.yshow.shike.utils.Dialog;
import com.yshow.shike.utils.GradeSeltorUtil;
import com.yshow.shike.utils.Grade_Level_Utils;
import com.yshow.shike.utils.MyAsyncHttpResponseHandler;
import com.yshow.shike.utils.Net_Servse;
import com.yshow.shike.utils.SKAsyncApiController;
import com.yshow.shike.utils.SKResolveJsonUtil;
import com.yshow.shike.utils.AreaSeltorUtil.AreaSeltorUtilButtonOnclickListening;
import com.yshow.shike.utils.Take_Phon_album;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 老师个人信息页面
 */
public class TeaPersonInfoActivity extends BaseActivity {
    private EditText base_name, base_nick, base_schoo, ed_old_pwd, ed_new_pwd, base_email, ed_pwd_comfig, jieshao;
    // 姓名 昵称 地区 学校 旧密码 新密码 邮箱 年 月日 密码确认
    private TextView tv_user_mob, bace_area, ed_day_bir, study_year, tea_sunject, tv_tea_subject; // 个人信息电话
    private SKStudent stu_info; // 获取学生信息
    private RadioButton sex_men, sex_miss;// 性别为男 性别为女
    private ImageView head_pic; // 设置学生头像
    private DisplayImageOptions options;
    private ImageLoader imageLoader;
    private android.app.Dialog stu_Take_Phon;
    private final int TAKE_PICTURE = 1;
    private final int TAKE_PHONE = 2;
    private final int SETTO_IMAGEVIEW = 4;
    private TextView tea_upload;
    private Update_User_Info user_info;
    private Exit_Login instence;
    private boolean isHeadImg = true;
    private User_Info info;

    private TextView points, questions, fanNum, zanmeiNum;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        stu_Take_Phon = Dilog_Share.Stu_Take_Phon(this, listener);
        imageLoader = ImageLoader.getInstance();
        user_info = new Update_User_Info();
        instence = Exit_Login.getInLogin();
        stu_info = LoginManage.getInstance().getStudent();
        String uid = stu_info.getUid();
        options = Net_Servse.getInstence().Picture_Shipei(R.drawable.s_teacher_icon);
        setContentView(R.layout.tea_person_info_layout);
        Tea_initData();
        Set_tea_Base_Info();
        update_info(uid);
    }

    // 老师端投标体控制
    private void Tea_initData() {
        findViewById(R.id.left_btn).setOnClickListener(listener);
        findViewById(R.id.confirm_btn).setOnClickListener(listener);
        findViewById(R.id.xuelingduan_btn).setOnClickListener(listener);
        findViewById(R.id.xueke_btn).setOnClickListener(listener);
        findViewById(R.id.diqu_btn).setOnClickListener(listener);


        points = (TextView) findViewById(R.id.huode_xuefen);
        questions = (TextView) findViewById(R.id.jieti_cishu);
        fanNum = (TextView) findViewById(R.id.xuesheng_guanzhu);
        zanmeiNum = (TextView) findViewById(R.id.huode_zanmei);
    }


    // 老师端 控件初始化
    private void Set_tea_Base_Info() {
        base_name = (EditText) findViewById(R.id.tv_tea_base_name);
        base_nick = (EditText) findViewById(R.id.tv_tea_base_nike); // 昵称
        bace_area = (TextView) findViewById(R.id.tv_tea_bace_area); // 地区
        base_schoo = (EditText) findViewById(R.id.tv_tea_base_schoo);// 学校
        base_email = (EditText) findViewById(R.id.ed_tea_base_email); // 邮箱
        sex_men = (RadioButton) findViewById(R.id.tea_sex_men); // 男性选择框
        sex_miss = (RadioButton) findViewById(R.id.tea_sex_miss); // 女性选择框
        head_pic = (ImageView) findViewById(R.id.tea_head_pic);
        ed_old_pwd = (EditText) findViewById(R.id.old_pwd);
        ed_new_pwd = (EditText) findViewById(R.id.new_pwd);
        ed_pwd_comfig = (EditText) findViewById(R.id.ed_pwd_comfig);
        jieshao = (EditText) findViewById(R.id.tv_base_jieshao);
        ed_day_bir = (TextView) findViewById(R.id.ed_tea_day_bir);
        tv_user_mob = (TextView) findViewById(R.id.tv_tea_mob);
        tea_sunject = (TextView) findViewById(R.id.age_tea_sunject);
        tea_upload = (TextView) findViewById(R.id.tv_tea_upload);
        tv_tea_subject = (TextView) findViewById(R.id.tv_tea_subject);
        tea_upload.setOnClickListener(listener);
        head_pic.setOnClickListener(listener);
        ed_day_bir.setOnClickListener(listener);
    }

    // 老师端 和 学生端的点击判断
    private OnClickListener listener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            // 老师端个人修改点击事件
            switch (v.getId()) {
                case R.id.left_btn:
                    finish();
                    break;
                // 老师端 保存个人信息
                case R.id.confirm_btn:
                    Sex_Dec();
                    break;
                case R.id.diqu_btn:
                    getArea();
                    break;
                case R.id.ed_tea_day_bir:
                    Data_Sele();
                    break;
                case R.id.xuelingduan_btn:// 初中,高中选择
                    getJieDuan();
                    break;
                // 上传教师资格证
                case R.id.tv_tea_upload:
                    isHeadImg = false;
                    stu_Take_Phon.show();
                    break;
                // 头像设置
                case R.id.tea_head_pic:
                    stu_Take_Phon.show();
                    break;
                // 老师端科目的id
                case R.id.xueke_btn:
                    getSubject();
                    break;
                // 拍照
                case R.id.tv_pai_zhao:
                    Take_Phon_album.getIntence().Take_Phone(TAKE_PHONE, TeaPersonInfoActivity.this);
                    stu_Take_Phon.dismiss();
                    break;
                // 相册
                case R.id.tv_xiagnc:
                    Take_Phon_album.getIntence().Take_Pickture(TAKE_PICTURE, TeaPersonInfoActivity.this);
                    stu_Take_Phon.dismiss();
                    break;
                // 取消
                case R.id.tv_tea_cancel:
                    stu_Take_Phon.dismiss();
                    break;
            }
        }
    };

    /**
     * 性别的判断
     */
    private void Sex_Dec() {
        if (sex_men.isChecked()) {
            user_info.setSex("0");
            Save();
        } else if (sex_miss.isChecked()) {
            user_info.setSex("1");
            Save();
        } else {
            user_info.setSex("0");
            Save();
        }
    }

    /**
     * 获取控件中的内容 提交用户更新的数据
     *
     * @param sex
     */
    private void Save() {
        user_info.setName(base_name.getText().toString().trim());
        user_info.setNickname(base_nick.getText().toString().trim());
        user_info.setInfo(jieshao.getText().toString().trim());
        user_info.setSchool(base_schoo.getText().toString().trim());
        user_info.setTypes(stu_info.getTypes());
        user_info.setBirthday(ed_day_bir.getText().toString());
        user_info.setEmail(base_email.getText().toString().trim());
        String old_pwd = ed_old_pwd.getText().toString().trim(); // 获取旧密码
        user_info.setOld_pwd(old_pwd);
        String pwd = ed_new_pwd.getText().toString().trim(); // 获取新密码
        String comfig_pas = ed_pwd_comfig.getText().toString().trim(); // 密码确认
        head_pic.setDrawingCacheEnabled(true);
        if (TextUtils.isEmpty(old_pwd) || TextUtils.isEmpty(pwd)) {
            instence.save_user_info(TeaPersonInfoActivity.this, user_info);
        } else {
            // 判断两次密码是否一致
            if (pwd.equals(comfig_pas)) {
                user_info.setPwd(pwd);
                instence.save_user_info(TeaPersonInfoActivity.this, user_info);
            } else {
                Toast.makeText(TeaPersonInfoActivity.this, "密码不一致", 0).show();
            }
        }
    }

    // 获取地区
    private void getArea() {
        SKAsyncApiController.skGetArea(new MyAsyncHttpResponseHandler(this, true) {
            @Override
            public void onSuccess(String arg0) {
                super.onSuccess(arg0);
                ArrayList<SKArea> resolveArea = SKResolveJsonUtil.getInstance().resolveArea(arg0);
                final AreaSeltorUtil systemDialog = new AreaSeltorUtil(TeaPersonInfoActivity.this, resolveArea);
                systemDialog.setLeftButtonText("完成");
                systemDialog.show();
                systemDialog.setAreaSeltorUtilButtonOnclickListening(new AreaSeltorUtilButtonOnclickListening() {
                    @Override
                    public void onClickRight() {
                    }

                    @Override
                    public void onClickLeft() {
                        user_info.setaId(systemDialog.getGradeId());
                        bace_area.setText(systemDialog.getSeltotText());
                    }
                });
            }
        });
    }

    /**
     * 日期选择器
     *
     * @return
     */
    private void Data_Sele() {
        DataSeleUtil dataSeleUtil = new DataSeleUtil(this);
        dataSeleUtil.setLeftButtonText("完成");
        dataSeleUtil.show();
        dataSeleUtil.setDtaListening(new Data_Seltor_Listening() {
            @Override
            public void onClickLeft(String years, String month, String day_shu) {
                user_info.setBirthday(years + "年" + month + "月" + day_shu + "日");
                ed_day_bir.setText(years + "年" + month + "月" + day_shu + "日");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        Bundle bundle = data.getExtras();
        String time_make = System.currentTimeMillis() + "";
        switch (requestCode) {
            case SETTO_IMAGEVIEW:
                if (resultCode == RESULT_OK) {
                    if (bundle != null) {
                        Bitmap bitmap = (Bitmap) bundle.getParcelable("data");
                        seting_image(time_make, bitmap);
                    }
                }
                break;
            case TAKE_PICTURE:
                if (isHeadImg) {
                    Take_Phon_album.getIntence().startPhotoZoom(TeaPersonInfoActivity.this, data.getData(), SETTO_IMAGEVIEW);
                } else {
                    ContentResolver resolver = getContentResolver();
                    Uri originalUri = data.getData();        //获得图片的uri
                    try {
                        Bitmap bm = MediaStore.Images.Media.getBitmap(resolver, originalUri);
                        seting_image(time_make, bm);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case TAKE_PHONE:
                if (resultCode == RESULT_OK) {
                    if (isHeadImg) {
                        Take_Phon_album.getIntence().startPhotoZoom(TeaPersonInfoActivity.this, data.getData(), SETTO_IMAGEVIEW);
                    } else {
                        ContentResolver resolver = getContentResolver();
                        Uri originalUri = data.getData();        //获得图片的uri
                        try {
                            Bitmap bm = MediaStore.Images.Media.getBitmap(resolver, originalUri);
                            seting_image(time_make, bm);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
        }
    }

    /**
     * 设置头像和上传教师职格证
     *
     * @param time_make
     * @param bitmap
     */
    private void seting_image(String time_make, Bitmap bitmap) {
        if (LoginManage.getInstance().isTeacher()) {
            if (isHeadImg) {
                Bitmap newbm = Dialog.scaleImg(bitmap, 400, 400);
                head_pic.setImageBitmap(newbm);
                Reg_Imag(TeaPersonInfoActivity.this, bitmap, time_make);
            } else {
                Tea_Imag(TeaPersonInfoActivity.this, bitmap, time_make, tea_upload);
            }
        } else {
            Bitmap newbm = Dialog.scaleImg(bitmap, 400, 400);
            head_pic.setImageBitmap(newbm);
            Reg_Imag(TeaPersonInfoActivity.this, bitmap, time_make);
        }
    }

    // 获取学龄段
    private void getJieDuan() {
        SKAsyncApiController.skGetGrade(new MyAsyncHttpResponseHandler(this, true) {
            @Override
            public void onSuccess(final int arg0, final String json) {
                super.onSuccess(arg0, json);
                ArrayList<SKGrade> SKGrades = SKResolveJsonUtil.getInstance().resolveGrade(json);
                final Grade_Level_Utils grade_utils = new Grade_Level_Utils(TeaPersonInfoActivity.this, SKGrades);
                grade_utils.setLeftButtonText("完成");
                grade_utils.setGradeSeltorUtilButtonOnclickListening(new GradeSeltorUtilButtonOnclickListening() {
                    @Override
                    public void onClickRight() {
                    }

                    @Override
                    public void onClickLeft() {
                        String seltotText = grade_utils.getSeltotText();
                        tea_sunject.setText(seltotText);
                        user_info.setFromGradeId(grade_utils.getFormGradeId());
                        user_info.setToGradeId(grade_utils.getToGradeId());
                    }
                });
                grade_utils.show();
            }
        });
    }

    // 获取学科
    private void getSubject() {
        SKAsyncApiController.skGetSubjeck(new MyAsyncHttpResponseHandler(this, true) {
            @Override
            public void onSuccess(final int arg0, final String arg1) {
                super.onSuccess(arg0, arg1);
                ArrayList<SKArea> paseSubject = SKResolveJsonUtil.getInstance().PaseSubject(arg1);
                final AreaSeltorUtil subjectId = new AreaSeltorUtil(TeaPersonInfoActivity.this, paseSubject);
                subjectId.setLeftButtonText("完成");
                subjectId.show();
                subjectId.setAreaSeltorUtilButtonOnclickListening(new AreaSeltorUtilButtonOnclickListening() {
                    @Override
                    public void onClickRight() {
                    }

                    @Override
                    public void onClickLeft() {
                        user_info.setSubjectId(subjectId.getGradeId());
                        tv_tea_subject.setText(subjectId.getSeltotText());
                    }
                });
            }
        });
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
                user_info.setIcon(data.getFileId());
            }
        });
    }

    /**
     * 查看用户信息
     */
    private void update_info(String uid) {
        SKAsyncApiController.User_Info(uid, new MyAsyncHttpResponseHandler(this, true) {
            public void onSuccess(int arg0, String json) {
                super.onSuccess(arg0, json);
                boolean atent_Success = SKResolveJsonUtil.getInstance().resolveIsSuccess(json, TeaPersonInfoActivity.this);
                if (atent_Success) {
                    info = SKResolveJsonUtil.getInstance().My_teather1(json);
                    skGetArea();
                    base_name.setText("" + info.getName());
                    base_nick.setText("" + info.getNickname());
                    base_schoo.setText(info.getShool());
                    questions.setText(info.getClaim_question_num());


                    points.setText(info.getPoints());
                    fanNum.setText(info.getFansNum());
                    zanmeiNum.setText(info.getLike_num());


                    base_email.setText(info.getEmail());
                    jieshao.setText(info.getInfo());
                    tv_user_mob.setText(info.getMob());
                    ed_day_bir.setText(info.getBirthday());

                    points.setText(info.getPoints());
                    questions.setText(info.getQuestions());
                    fanNum.setText(info.getFansNum());


                    try {
                        study_year.setText(info.getGrade() + "" + info.getGradeName());
                    } catch (Exception e) {
                    }
                    String sex = info.getSex();
                    if (sex.equals("0")) {
                        sex_men.setChecked(true);
                    } else {
                        sex_miss.setChecked(true);
                    }

                    if (!TextUtils.isEmpty(info.getPicurl())) {
                        imageLoader.displayImage(info.getPicurl(), head_pic);
                    }
                    if (!info.getGreatid().equals("")) {
                        if (tv_tea_subject != null) {
                            tv_tea_subject.setText(info.getGreatid());
                        }
                    }
                    if (!info.getGrade().equals("")) {
                        if (tea_sunject != null) {
                            tea_sunject.setText(info.getGrade());
                        }
                    }
                    if (!TextUtils.isEmpty(info.paper)) {
                        tea_upload.setText("已上传");
                    }
                }
            }

            ;
        });
    }

    /**
     * 上传教师资格证
     *
     * @param bitmap
     * @param path
     * @return
     */
    public void Tea_Imag(final Context context, Bitmap bitmap, String path, final TextView view) {
        SKAsyncApiController.Up_Loading_Tea(bitmap, path, new MyAsyncHttpResponseHandler(context, true) {
            @Override
            public void onSuccess(String json) {
                super.onSuccess(json);
                view.setText("已上传");
                Type tp = new TypeToken<UpLoad_Image>() {
                }.getType();
                Gson gs = new Gson();
                UpLoad_Image up_image = gs.fromJson(json, tp);
                Upload_Filed data = up_image.getData();
                user_info.setPaper(data.getFileId());
            }
        });
    }

    /**
     * 获取所有的地区
     */
    private void skGetArea() {
        SKAsyncApiController.skGetArea(new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String arg0) {
                super.onSuccess(arg0);
                ArrayList<SKArea> resolveArea = SKResolveJsonUtil.getInstance().resolveArea(arg0);
                for (int i = 0; i < resolveArea.size(); i++) {
                    SKArea skArea = resolveArea.get(i);
                    try {
                        String area = info.getArea();
                        if (skArea.getId().equals(area)) {
                            bace_area.setText(skArea.getName());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}