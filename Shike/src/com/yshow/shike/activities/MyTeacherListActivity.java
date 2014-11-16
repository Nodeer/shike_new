package com.yshow.shike.activities;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.yshow.shike.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yshow.shike.entity.SKArea;
import com.yshow.shike.entity.SKGrade;
import com.yshow.shike.entity.SKTeacherOrSubject;
import com.yshow.shike.entity.Star_Teacher_Parse;
import com.yshow.shike.utils.AreaSeltorUtil;
import com.yshow.shike.utils.ImageLoadOption;
import com.yshow.shike.utils.MyAsyncHttpResponseHandler;
import com.yshow.shike.utils.SKAsyncApiController;
import com.yshow.shike.utils.SKResolveJsonUtil;
import com.yshow.shike.utils.XuekeSelectUtil;
import com.yshow.shike.utils.XuelingDuanSeltorUtil;
import com.yshow.shike.widget.XListView;

/**
 * 我的老师页面,可以看到老师列表
 */
public class MyTeacherListActivity extends BaseActivity implements View.OnClickListener {
    private DisplayImageOptions options;
    private ImageLoader imageLoader;
    private DisplayImageOptions grayOption;
    private ArrayList<Star_Teacher_Parse> mDataList = new ArrayList<Star_Teacher_Parse>();
    private MyAdapter adapter;
    private ListView mOnlineListView;

    private String mSubjectId = "0";
    private String mAreaId = "0";
    private String mJieduanId = "0";

    private TextView jieduanText, xuekeText, diquText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_teacher_layout);
        TextView titletext = (TextView) findViewById(R.id.title_text);
        titletext.setText("我的老师");
        findViewById(R.id.left_btn).setOnClickListener(this);

        options = ImageLoadOption.getTeaHeadImageOption();
        grayOption = ImageLoadOption.getTeaHeadGrayImageOption();
        imageLoader = ImageLoader.getInstance();


        mOnlineListView = (ListView) findViewById(R.id.online_lv);
        adapter = new MyAdapter();
        mOnlineListView.setAdapter(adapter);

        mOnlineListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                Star_Teacher_Parse item = (Star_Teacher_Parse) adapter.getItem(arg2);
                Intent intent = new Intent(MyTeacherListActivity.this, TeacherInfoActivity.class);
                intent.putExtra("ismyTeacher", true);
                intent.putExtra("headpicture", item);
                MyTeacherListActivity.this.startActivity(intent);
            }
        });
        getMyTeacher();

        jieduanText = (TextView) findViewById(R.id.jieduan_text);
        jieduanText.setOnClickListener(this);
        xuekeText = (TextView) findViewById(R.id.xueke_text);
        xuekeText.setOnClickListener(this);
        diquText = (TextView) findViewById(R.id.diqu_text);
        diquText.setOnClickListener(this);

    }


    private class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mDataList.size();
        }

        @Override
        public Object getItem(int arg0) {
            return mDataList.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        @Override
        public View getView(int arg0, View convertView, ViewGroup arg2) {
            final Star_Teacher_Parse on_Tea = mDataList.get(arg0);
            if (convertView == null) {
                convertView = View.inflate(MyTeacherListActivity.this, R.layout.fragment_start_text, null);
            }
            TextView tv_grade = (TextView) convertView.findViewById(R.id.tv_grade);
            ImageView tea_piture = (ImageView) convertView.findViewById(R.id.iv_teather_picture);
            TextView tea_name = (TextView) convertView.findViewById(R.id.tv_nicheng);
            TextView tea_subject = (TextView) convertView.findViewById(R.id.tv_subject);
            TextView diqu = (TextView) convertView.findViewById(R.id.tv_diqu);
            TextView tea_info = (TextView) convertView.findViewById(R.id.tv_gerenxinxi);
            TextView isonline = (TextView) convertView.findViewById(R.id.tv_isonline);
            if (on_Tea != null) {
                tea_name.setText(on_Tea.getNickname());
                tv_grade.setText(on_Tea.getGrade());
                tea_subject.setText(on_Tea.getSubiect());
                diqu.setText(on_Tea.getArea());
                tea_info.setText(on_Tea.getInfo());
                if (!on_Tea.isOnline) {
                    isonline.setText("离线");
                    imageLoader.displayImage(on_Tea.getIcon(), tea_piture, grayOption);
                } else {
                    isonline.setText("在线");
                    imageLoader.displayImage(on_Tea.getIcon(), tea_piture, options);
                }
            }
            return convertView;
        }
    }

    private void getMyTeacher() {
        SKAsyncApiController.My_Taeather_Parse(mJieduanId, mSubjectId, mAreaId, new MyAsyncHttpResponseHandler(this, true) {
            @Override
            public void onSuccess(String json) {
                super.onSuccess(json);
                boolean success = SKResolveJsonUtil.getInstance().resolveIsSuccess(json, MyTeacherListActivity.this);
                if (success) {
                    ArrayList<Star_Teacher_Parse> my_Teather = SKResolveJsonUtil.getInstance().My_Teather(json);
                    mDataList = my_Teather;
                    if (my_Teather.size() == 0) {
                        findViewById(R.id.nodata_layout).setVisibility(View.VISIBLE);
                    }else{
                        findViewById(R.id.nodata_layout).setVisibility(View.GONE);
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_btn:
                finish();
                break;
            case R.id.jieduan_text:
                getJieDuan();
                break;
            case R.id.xueke_text:
                skGetSubject();
                break;
            case R.id.diqu_text:
                skGetArea();
                break;
        }
    }

    // 获取学龄段
    private void getJieDuan() {
        SKAsyncApiController.skGetGrade(new MyAsyncHttpResponseHandler(this, true) {
            @Override
            public void onSuccess(final int arg0, final String json) {
                super.onSuccess(arg0, json);
                ArrayList<SKGrade> SKGrades = SKResolveJsonUtil.getInstance().resolveGrade(json);
                SKGrade grade = new SKGrade();
                grade.setName("不限");
                grade.setId("0");
                SKGrades.add(0, grade);

                final XuelingDuanSeltorUtil grade_utils = new XuelingDuanSeltorUtil(MyTeacherListActivity.this, SKGrades);
                grade_utils.setLeftButtonText("完成");
                grade_utils.setXuelingSeltorUtilButtonOnclickListening(new XuelingDuanSeltorUtil.XuelingSeltorUtilButtonOnclickListening() {
                    @Override
                    public void onClickRight() {
                    }

                    @Override
                    public void onClickLeft() {
                        String seltotText = grade_utils.getSeltotText();
                        jieduanText.setText(seltotText);
                        mJieduanId = grade_utils.getGradeId();
                        getMyTeacher();
                    }
                });
                grade_utils.show();
            }
        });
    }


    // 联网那一科目为条件的科目
    private void skGetSubject() {
        SKAsyncApiController.skGetSubject(new MyAsyncHttpResponseHandler(MyTeacherListActivity.this, true) {
            @Override
            public void onSuccess(String arg0) {
                super.onSuccess(arg0);
                ArrayList<SKTeacherOrSubject> subjects = SKResolveJsonUtil.getInstance().resolveSubject(arg0);
                SKTeacherOrSubject skTeacherOrSubject = new SKTeacherOrSubject();
                skTeacherOrSubject.setName("不限");
                skTeacherOrSubject.setSubjectId("0");
                subjects.add(0, skTeacherOrSubject);


                final XuekeSelectUtil subjectId = new XuekeSelectUtil(MyTeacherListActivity.this, subjects);
                subjectId.setLeftButtonText("完成");
                subjectId.show();
                subjectId.setAreaSeltorUtilButtonOnclickListening(new XuekeSelectUtil.AreaSeltorUtilButtonOnclickListening() {
                    @Override
                    public void onClickRight() {
                    }

                    @Override
                    public void onClickLeft() {
                        mSubjectId = subjectId.getGradeId();
                        xuekeText.setText(subjectId.getSeltotText());
                        getMyTeacher();
                    }
                });

            }
        });
    }

    private void skGetArea() {
        SKAsyncApiController.skGetArea(new MyAsyncHttpResponseHandler(MyTeacherListActivity.this, true) {
            @Override
            public void onSuccess(String arg0) {
                super.onSuccess(arg0);
                ArrayList<SKArea> resolveArea = SKResolveJsonUtil.getInstance().resolveArea(arg0);
                SKArea skArea = new SKArea();
                skArea.setId("0");
                skArea.setName("不限");
                resolveArea.add(0, skArea);


                final AreaSeltorUtil systemDialog = new AreaSeltorUtil(MyTeacherListActivity.this, resolveArea);
                systemDialog.setLeftButtonText("完成");
                systemDialog.show();
                systemDialog.setAreaSeltorUtilButtonOnclickListening(new AreaSeltorUtil.AreaSeltorUtilButtonOnclickListening() {
                    @Override
                    public void onClickRight() {
                    }

                    @Override
                    public void onClickLeft() {
                        mAreaId = systemDialog.getGradeId();
                        diquText.setText(systemDialog.getSeltotText());
                        getMyTeacher();
                    }
                });
            }
        });
    }

}
