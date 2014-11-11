package com.yshow.shike.activities;

import java.util.ArrayList;

import com.yshow.shike.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yshow.shike.entity.SKArea;
import com.yshow.shike.entity.SKGrade;
import com.yshow.shike.entity.SKTeacherOrSubject;
import com.yshow.shike.entity.Star_Teacher_Parse;
import com.yshow.shike.utils.*;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yshow.shike.widget.XListView;

public class RecommendTeacherListActivity extends BaseActivity implements OnClickListener,
        XListView.IXListViewListener {
    private XListView lv_recommend;
    private Star_Teacher_Parse teacher_Parse;
    private MyAdapter adapter;
    private int page = 1;
    private ArrayList<Star_Teacher_Parse> mDataList = new ArrayList<Star_Teacher_Parse>();
    private DisplayImageOptions options;
    private ImageLoader imageLoader;
    private DisplayImageOptions grayOption;

    private String mSubjectId = "0";
    private String mAreaId = "0";
    private String mJieduanId = "0";

    private TextView jieduanText, xuekeText, diquText;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_recommend_teacher);
        TextView titletext = (TextView) findViewById(R.id.title_text);
        titletext.setText("推荐老师");
        findViewById(R.id.left_btn).setOnClickListener(this);
        options = ImageLoadOption.getTeaHeadImageOption();
        grayOption = ImageLoadOption.getTeaHeadGrayImageOption();
        imageLoader = ImageLoader.getInstance();
        jieduanText = (TextView) findViewById(R.id.jieduan_text);
        xuekeText = (TextView) findViewById(R.id.xueke_text);
        diquText = (TextView) findViewById(R.id.diqu_text);
        jieduanText.setOnClickListener(this);
        xuekeText.setOnClickListener(this);
        diquText.setOnClickListener(this);
        lv_recommend = (XListView) findViewById(R.id.lv_recommend);
        lv_recommend.setXListViewListener(this);
        lv_recommend.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                if (arg3 == -1) {
                    // 点击的是headerView或者footerView
                    return;
                }
                int realPosition = (int) arg3;
                Star_Teacher_Parse item = adapter.getItem(realPosition);
                Intent intent = new Intent(RecommendTeacherListActivity.this, TeacherInfoActivity.class);
                intent.putExtra("headpicture", item);
                RecommendTeacherListActivity.this.startActivity(intent);
            }
        });
        adapter = new MyAdapter();
        lv_recommend.setAdapter(adapter);
        searchRecommendTeather(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.jieduan_text:
                getJieDuan();
                break;
            case R.id.xueke_text:
                skGetSubject();
                break;
            case R.id.diqu_text:
                skGetArea();
                break;
            case R.id.left_btn:
                RecommendTeacherListActivity.this.finish();
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

                final XuelingDuanSeltorUtil grade_utils = new XuelingDuanSeltorUtil(RecommendTeacherListActivity.this, SKGrades);
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
                        onRefresh();
                    }
                });
                grade_utils.show();
            }
        });
    }


    // 联网那一科目为条件的科目
    private void skGetSubject() {
        SKAsyncApiController.skGetSubject(new MyAsyncHttpResponseHandler(RecommendTeacherListActivity.this, true) {
            @Override
            public void onSuccess(String arg0) {
                super.onSuccess(arg0);
                ArrayList<SKTeacherOrSubject> subjects = SKResolveJsonUtil.getInstance().resolveSubject(arg0);
                SKTeacherOrSubject skTeacherOrSubject = new SKTeacherOrSubject();
                skTeacherOrSubject.setName("不限");
                skTeacherOrSubject.setSubjectId("0");
                subjects.add(0, skTeacherOrSubject);


                final XuekeSelectUtil subjectId = new XuekeSelectUtil(RecommendTeacherListActivity.this, subjects);
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
                        onRefresh();
                    }
                });

            }
        });
    }

    private void skGetArea() {
        SKAsyncApiController.skGetArea(new MyAsyncHttpResponseHandler(RecommendTeacherListActivity.this, true) {
            @Override
            public void onSuccess(String arg0) {
                super.onSuccess(arg0);
                ArrayList<SKArea> resolveArea = SKResolveJsonUtil.getInstance().resolveArea(arg0);
                SKArea skArea = new SKArea();
                skArea.setId("0");
                skArea.setName("不限");
                resolveArea.add(0, skArea);


                final AreaSeltorUtil systemDialog = new AreaSeltorUtil(RecommendTeacherListActivity.this, resolveArea);
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
                        onRefresh();
                    }
                });
            }
        });
    }


    /**
     * 获取推荐老师
     */
    public void searchRecommendTeather(boolean needProgress) {
        SKAsyncApiController.recommend_teather(mJieduanId, mSubjectId, mAreaId, page, new MyAsyncHttpResponseHandler(this, needProgress) {
            public void onSuccess(int arg0, String json) {
                super.onSuccess(arg0, json);
                boolean success = SKResolveJsonUtil.getInstance().resolveIsSuccess(json, RecommendTeacherListActivity.this);
                if (success) {
                    ArrayList<Star_Teacher_Parse> list = SKResolveJsonUtil.getInstance().start_teather(json);
                    mDataList.addAll(list);
                    if (list.size() < 20) {
                        lv_recommend.setPullLoadEnable(false);
                    } else {
                        lv_recommend.setPullLoadEnable(true);
                    }
                    if (mDataList.size() == 0) {
                        Toast.makeText(RecommendTeacherListActivity.this, "没有推荐老师", Toast.LENGTH_SHORT).show();
                    }
                    adapter.notifyDataSetChanged();
                    onLoad();
                }
            }

            ;
        });
    }

    private void onLoad() {
        lv_recommend.stopRefresh();
        lv_recommend.stopLoadMore();
        lv_recommend.setRefreshTime(Timer_Uils.getCurrentTime());
    }

    @Override
    public void onLoadMore() {
        page++;
        searchRecommendTeather(false);
    }

    @Override
    public void onRefresh() {
        page = 1;
        mDataList.clear();
        searchRecommendTeather(false);
    }

    class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mDataList.size();
        }

        @Override
        public Star_Teacher_Parse getItem(int arg0) {
            return mDataList.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            teacher_Parse = mDataList.get(position);
            if (convertView == null) {
                convertView = View.inflate(RecommendTeacherListActivity.this, R.layout.fragment_start_text, null);
            }
            ImageView teather_picture = (ImageView) convertView.findViewById(R.id.iv_teather_picture);
            TextView tv_nicheng = (TextView) convertView.findViewById(R.id.tv_nicheng);
            TextView tv_subject = (TextView) convertView.findViewById(R.id.tv_subject);
            TextView tv_grade = (TextView) convertView.findViewById(R.id.tv_grade);
            TextView tv_diqu = (TextView) convertView.findViewById(R.id.tv_diqu);
            TextView tv_gerenxinxi = (TextView) convertView.findViewById(R.id.tv_gerenxinxi);
            View iv_teather_online = convertView.findViewById(R.id.iv_teather_isonline);
            TextView isonline = (TextView) convertView.findViewById(R.id.tv_isonline);
            if (!teacher_Parse.isOnline) {
//                iv_teather_online.setVisibility(View.VISIBLE);
                isonline.setText("离线");
                imageLoader.displayImage(teacher_Parse.getIcon(), teather_picture, grayOption);
            } else {
//                iv_teather_online.setVisibility(View.GONE);
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

    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }
}
