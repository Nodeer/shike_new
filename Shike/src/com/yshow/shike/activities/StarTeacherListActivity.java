package com.yshow.shike.activities;

import java.util.ArrayList;

import com.yshow.shike.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yshow.shike.entity.SKArea;
import com.yshow.shike.entity.SKTeacherOrSubject;
import com.yshow.shike.entity.Star_Teacher_Parse;
import com.yshow.shike.utils.*;

import android.content.Context;
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

import com.yshow.shike.widget.TopAndBottomLoadListView;
import com.yshow.shike.widget.XListView;

/**
 * 明星老师
 */
public class StarTeacherListActivity extends BaseActivity implements OnClickListener, TopAndBottomLoadListView.OnRefreshListener,
        XListView.IXListViewListener {
    private TextView seleck_subject;
    private TextView ll_diqu;
    private XListView starListView;
    private Context context;
    private String currentSubjiect, currentArea;

    private int page = 1;
    private ArrayList<Star_Teacher_Parse> mDataList = new ArrayList<Star_Teacher_Parse>();
    private String mSubjectId = "0";
    private String mAreaId = "0";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_my_teacher);
        TextView titletext = (TextView) findViewById(R.id.title_text);
        titletext.setText("明星老师");
        findViewById(R.id.left_btn).setOnClickListener(this);
        context = this;

        ll_diqu = (TextView) findViewById(R.id.ll_diqu);
        seleck_subject = (TextView) findViewById(R.id.ll_seleck_subject1);
        starListView = (XListView) findViewById(R.id.start_listView);
        starListView.setXListViewListener(this);
        ll_diqu.setOnClickListener(this);
        seleck_subject.setOnClickListener(this);
        adapter = new RegionAdapter();
        starListView.setAdapter(adapter);
        startSearchTeacher(mSubjectId, mAreaId);
        starListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                if (arg3 == -1) {
                    // 点击的是headerView或者footerView
                    return;
                }
                int realPosition = (int) arg3;
                Star_Teacher_Parse item = adapter.getItem(realPosition);
                Intent intent = new Intent(StarTeacherListActivity.this, TeacherInfoActivity.class);
                intent.putExtra("headpicture", item);
                StarTeacherListActivity.this.startActivity(intent);
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 地区选择按钮
            case R.id.ll_diqu:
                skGetArea();
                break;
            // 科目选择按钮
            case R.id.ll_seleck_subject1:
                skGetSubject();
                break;
            case R.id.left_btn:
                StarTeacherListActivity.this.finish();
                break;
        }
    }

    // 联网那一科目为条件的科目
    private void skGetSubject() {
        SKAsyncApiController.skGetSubject(new MyAsyncHttpResponseHandler(StarTeacherListActivity.this, true) {
            @Override
            public void onSuccess(String arg0) {
                super.onSuccess(arg0);
                ArrayList<SKTeacherOrSubject> subjects = SKResolveJsonUtil.getInstance().resolveSubject(arg0);
                SKTeacherOrSubject skTeacherOrSubject = new SKTeacherOrSubject();
                skTeacherOrSubject.setName("不限");
                skTeacherOrSubject.setSubjectId("0");
                subjects.add(0, skTeacherOrSubject);


                final XuekeSelectUtil subjectId = new XuekeSelectUtil(StarTeacherListActivity.this, subjects);
                subjectId.setLeftButtonText("完成");
                subjectId.show();
                subjectId.setAreaSeltorUtilButtonOnclickListening(new XuekeSelectUtil.AreaSeltorUtilButtonOnclickListening() {
                    @Override
                    public void onClickRight() {
                    }

                    @Override
                    public void onClickLeft() {
                        mSubjectId = subjectId.getGradeId();
                        seleck_subject.setText(subjectId.getSeltotText());
                        page = 1;
                        mDataList.clear();
                        startSearchTeacher(mSubjectId, mAreaId);
                    }
                });

            }
        });
    }

    private void skGetArea() {
        SKAsyncApiController.skGetArea(new MyAsyncHttpResponseHandler(context, true) {
            @Override
            public void onSuccess(String arg0) {
                super.onSuccess(arg0);
                ArrayList<SKArea> resolveArea = SKResolveJsonUtil.getInstance().resolveArea(arg0);
                SKArea skArea = new SKArea();
                skArea.setId("0");
                skArea.setName("不限");
                resolveArea.add(0, skArea);


                final AreaSeltorUtil systemDialog = new AreaSeltorUtil(StarTeacherListActivity.this, resolveArea);
                systemDialog.setLeftButtonText("完成");
                systemDialog.show();
                systemDialog.setAreaSeltorUtilButtonOnclickListening(new AreaSeltorUtil.AreaSeltorUtilButtonOnclickListening() {
                    @Override
                    public void onClickRight() {
                    }

                    @Override
                    public void onClickLeft() {
                        mAreaId = systemDialog.getGradeId();
                        ll_diqu.setText(systemDialog.getSeltotText());
                        page = 1;
                        mDataList.clear();
                        startSearchTeacher(mSubjectId, mAreaId);
                    }
                });
            }
        });
    }


    @Override
    public void onRefresh() {
        page = 1;
        mDataList.clear();
        refreshStarTeacher(mSubjectId, mAreaId);
    }

    @Override
    public void onLoadMore() {
        page++;
        getMoreStarTeacher(mSubjectId, mAreaId);
    }

    private void onLoad() {
        starListView.stopRefresh();
        starListView.stopLoadMore();
        starListView.setRefreshTime(Timer_Uils.getCurrentTime());
    }

    class RegionAdapter extends BaseAdapter {
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
            Star_Teacher_Parse teacher_Parse = mDataList.get(position);
            DisplayImageOptions options = Net_Servse.getInstence().Picture_Shipei(R.drawable.my_tea_phon);
            ImageLoader imageLoader = ImageLoader.getInstance();
            if (convertView == null) {
                convertView = View.inflate(StarTeacherListActivity.this, R.layout.fragment_start_text, null);
            }
            ImageView teather_picture = (ImageView) convertView.findViewById(R.id.iv_teather_picture);
            TextView tv_nicheng = (TextView) convertView.findViewById(R.id.tv_nicheng);
            TextView tv_subject = (TextView) convertView.findViewById(R.id.tv_subject);
            TextView tv_grade = (TextView) convertView.findViewById(R.id.tv_grade);
            TextView tv_diqu = (TextView) convertView.findViewById(R.id.tv_diqu);
            TextView tv_gerenxinxi = (TextView) convertView.findViewById(R.id.tv_gerenxinxi);
            ImageView teather_sele = (ImageView) convertView.findViewById(R.id.iv_teather_sele);
            if (PartnerConfig.list.contains(teacher_Parse.getUid())) {
                teather_sele.setVisibility(View.VISIBLE);
            } else {
                teather_sele.setVisibility(View.GONE);
            }
            imageLoader.displayImage(teacher_Parse.getIcon(), teather_picture, options);
            tv_nicheng.setText(teacher_Parse.getNickname());
            tv_subject.setText(teacher_Parse.getSubiect());
            tv_grade.setText(teacher_Parse.getGrade());
            tv_diqu.setText(teacher_Parse.getArea());
            tv_gerenxinxi.setText(teacher_Parse.getInfo());
            return convertView;
        }
    }

    private RegionAdapter adapter;

    /**
     * 获取明星老师
     *
     * @param subjectId
     * @param aId
     */
    public void startSearchTeacher(String subjectId, String aId) {
        currentSubjiect = subjectId;
        currentArea = aId;
        SKAsyncApiController.start_teather(subjectId, aId, page, new MyAsyncHttpResponseHandler(context, true) {
            public void onSuccess(String json) {
                super.onSuccess(json);
                ArrayList<Star_Teacher_Parse> list = SKResolveJsonUtil.getInstance().start_teather(json);
                mDataList.clear();
                mDataList.addAll(list);
                if (list.size() < 20) {
                    starListView.setPullLoadEnable(false);
                } else {
                    starListView.setPullLoadEnable(true);
                }
                if (mDataList.size() == 0) {
                    Toast.makeText(context, "没有明星老师", Toast.LENGTH_SHORT).show();
                }
                adapter.notifyDataSetChanged();
                onLoad();
            }

            ;
        });
    }

    public void getMoreStarTeacher(String subjectId, String aId) {
        currentSubjiect = subjectId;
        currentArea = aId;
        SKAsyncApiController.start_teather(subjectId, aId, page, new MyAsyncHttpResponseHandler(context, false) {
            public void onSuccess(String json) {
                super.onSuccess(json);
                ArrayList<Star_Teacher_Parse> list = SKResolveJsonUtil.getInstance().start_teather(json);
                mDataList.addAll(list);
                if (list.size() < 20) {
                    starListView.setPullLoadEnable(false);
                } else {
                    starListView.setPullLoadEnable(true);
                }
                if (mDataList.size() == 0) {
                    Toast.makeText(context, "没有明星老师", Toast.LENGTH_SHORT).show();
                }
                adapter.notifyDataSetChanged();
                onLoad();
            }

            ;
        });
    }

    public void refreshStarTeacher(String subjectId, String aId) {
        currentSubjiect = subjectId;
        currentArea = aId;
        SKAsyncApiController.start_teather(subjectId, aId, page, new MyAsyncHttpResponseHandler(context, false) {
            public void onSuccess(String json) {
                super.onSuccess(json);
                ArrayList<Star_Teacher_Parse> list = SKResolveJsonUtil.getInstance().start_teather(json);
                mDataList.addAll(list);
                if (list.size() < 20) {
                    starListView.setPullLoadEnable(false);
                } else {
                    starListView.setPullLoadEnable(true);
                }
                if (mDataList.size() == 0) {
                    Toast.makeText(context, "没有明星老师", Toast.LENGTH_SHORT).show();
                }
                adapter.notifyDataSetChanged();
                onLoad();
            }

            ;
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }
}