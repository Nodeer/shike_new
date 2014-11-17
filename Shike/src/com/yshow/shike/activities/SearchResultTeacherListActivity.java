package com.yshow.shike.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yshow.shike.R;
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

import java.util.ArrayList;

/**
 * Created by Administrator on 2014-10-29.
 */
public class SearchResultTeacherListActivity extends BaseActivity implements View.OnClickListener {

    private ListView list_view;

    private MyAdapter adapter;
    private DisplayImageOptions options;
    private ImageLoader imageLoader;
    private DisplayImageOptions grayOption;

    private String mSubjectId = "0";
    private String mAreaId = "0";
    private String mJieduanId = "0";


    private String nickname, sex, school;


    private TextView jieduanText, xuekeText, diquText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_result_teacher_llist_layout);

        TextView titletext = (TextView) findViewById(R.id.title_text);
        titletext.setText("老师列表");
        findViewById(R.id.left_btn).setOnClickListener(this);

        xuekeText = (TextView) findViewById(R.id.xueke_text);
        xuekeText.setOnClickListener(this);
        diquText = (TextView) findViewById(R.id.diqu_text);
        diquText.setOnClickListener(this);
        jieduanText = (TextView) findViewById(R.id.jieduan_text);
        jieduanText.setOnClickListener(this);

        ArrayList<Star_Teacher_Parse> list = (ArrayList<Star_Teacher_Parse>) getIntent().getSerializableExtra("data");

        if (list.size() == 0) {
            Toast.makeText(this, "没有找到符合条件的老师", Toast.LENGTH_SHORT).show();
        }

        Intent it = getIntent();
        mSubjectId = it.getStringExtra("subjectId");
        xuekeText.setText(it.getStringExtra("subjectName"));
        mAreaId = it.getStringExtra("areaId");
        diquText.setText(it.getStringExtra("areaName"));
        sex = it.getStringExtra("sex");
        nickname = it.getStringExtra("nickname");
        school = it.getStringExtra("school");

        boolean isFromPhone = it.getBooleanExtra("isPhone", false);
        if (isFromPhone) {
            LinearLayout layout = (LinearLayout) findViewById(R.id.tiaojian_layout);
            layout.setVisibility(View.GONE);
        }

        options = ImageLoadOption.getTeaHeadImageOption();
        grayOption = ImageLoadOption.getTeaHeadGrayImageOption();
        imageLoader = ImageLoader.getInstance();
        imageLoader = ImageLoader.getInstance();
        adapter = new MyAdapter(list);
        list_view = (ListView) findViewById(R.id.lv_list_view);
        list_view.setAdapter(adapter);
        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                Star_Teacher_Parse item = (Star_Teacher_Parse) adapter.getItem(arg2);
                Intent intent = new Intent(SearchResultTeacherListActivity.this, TeacherInfoActivity.class);
                intent.putExtra("headpicture", item);
                SearchResultTeacherListActivity.this.startActivity(intent);
            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_btn:
                finish();
                break;
            case R.id.xueke_text:
                skGetSubject();
                break;
            case R.id.diqu_text:
                skGetArea();
                break;
            case R.id.jieduan_text:
                getJieDuan();
                break;

        }
    }

    // listview 适配器
    class MyAdapter extends BaseAdapter {
        ArrayList<Star_Teacher_Parse> list;

        public MyAdapter(ArrayList<Star_Teacher_Parse> list) {
            super();
            this.list = list;
        }

        public void setData(ArrayList<Star_Teacher_Parse> list) {
            this.list = list;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int arg0) {
            return list.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Star_Teacher_Parse teacher_Parse = list.get(position);
            if (convertView == null) {
                convertView = View.inflate(SearchResultTeacherListActivity.this,
                        R.layout.fragment_start_text, null);
            }
            ImageView teather_picture = (ImageView) convertView
                    .findViewById(R.id.iv_teather_picture);
            TextView tv_nicheng = (TextView) convertView
                    .findViewById(R.id.tv_nicheng);
            TextView tv_subject = (TextView) convertView
                    .findViewById(R.id.tv_subject);
            TextView tv_grade = (TextView) convertView
                    .findViewById(R.id.tv_grade);
            TextView tv_diqu = (TextView) convertView
                    .findViewById(R.id.tv_diqu);
            TextView tv_gerenxinxi = (TextView) convertView
                    .findViewById(R.id.tv_gerenxinxi);
            TextView isonline = (TextView) convertView.findViewById(R.id.tv_isonline);
            if (!teacher_Parse.isOnline) {
                isonline.setText("离线");
                imageLoader.displayImage(teacher_Parse.getIcon(), teather_picture, grayOption);
            } else {
                isonline.setText("在线");
                imageLoader.displayImage(teacher_Parse.getIcon(), teather_picture, options);
            }
            tv_nicheng.setText(teacher_Parse.getNickname());
            tv_subject.setText(teacher_Parse.getSubiect());
            tv_grade.setText(teacher_Parse.getGrade());
            tv_diqu.setText(teacher_Parse.getArea());
            tv_gerenxinxi.setText(teacher_Parse.getInfo());
            return convertView;
        }
    }

    private void searchTeachers() {
        SKAsyncApiController.Searth_Teather_TiaoJian(mJieduanId, nickname, school, mSubjectId,
                mAreaId, sex, new MyAsyncHttpResponseHandler(this, true) {
                    @Override
                    public void onSuccess(String json) {
                        super.onSuccess(json);
                        boolean isSuccess = SKResolveJsonUtil.getInstance().resolveIsSuccess(json, SearchResultTeacherListActivity.this);
                        if (isSuccess) {
                            ArrayList<Star_Teacher_Parse> list = SKResolveJsonUtil.getInstance().Search_Terms(json);
                            adapter.setData(list);
                        }
                    }
                });
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

                final XuelingDuanSeltorUtil grade_utils = new XuelingDuanSeltorUtil(SearchResultTeacherListActivity.this, SKGrades);
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
                        searchTeachers();
                    }
                });
                grade_utils.show();
            }
        });
    }

    // 联网那一科目为条件的科目
    private void skGetSubject() {
        SKAsyncApiController.skGetSubject(new MyAsyncHttpResponseHandler(SearchResultTeacherListActivity.this, true) {
            @Override
            public void onSuccess(String arg0) {
                super.onSuccess(arg0);
                ArrayList<SKTeacherOrSubject> subjects = SKResolveJsonUtil.getInstance().resolveSubject(arg0);
                SKTeacherOrSubject skTeacherOrSubject = new SKTeacherOrSubject();
                skTeacherOrSubject.setName("不限");
                skTeacherOrSubject.setSubjectId("0");
                subjects.add(0, skTeacherOrSubject);


                final XuekeSelectUtil subjectId = new XuekeSelectUtil(SearchResultTeacherListActivity.this, subjects);
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
                        searchTeachers();
                    }
                });

            }
        });
    }

    private void skGetArea() {
        SKAsyncApiController.skGetArea(new MyAsyncHttpResponseHandler(SearchResultTeacherListActivity.this, true) {
            @Override
            public void onSuccess(String arg0) {
                super.onSuccess(arg0);
                ArrayList<SKArea> resolveArea = SKResolveJsonUtil.getInstance().resolveArea(arg0);
                SKArea skArea = new SKArea();
                skArea.setId("0");
                skArea.setName("不限");
                resolveArea.add(0, skArea);


                final AreaSeltorUtil systemDialog = new AreaSeltorUtil(SearchResultTeacherListActivity.this, resolveArea);
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
                        searchTeachers();
                    }
                });
            }
        });
    }
}
