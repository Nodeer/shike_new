package com.yshow.shike.activities;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yshow.shike.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yshow.shike.entity.Star_Teacher_Parse;
import com.yshow.shike.utils.ImageLoadOption;
import com.yshow.shike.utils.MyAsyncHttpResponseHandler;
import com.yshow.shike.utils.Net_Servse;
import com.yshow.shike.utils.SKAsyncApiController;
import com.yshow.shike.utils.SKResolveJsonUtil;
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
    private XListView mOnlineListView;

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


        mOnlineListView = (XListView) findViewById(R.id.online_lv);
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

    private void My_Teather() {
        SKAsyncApiController.My_Taeather_Parse(new MyAsyncHttpResponseHandler(this, true) {
            @Override
            public void onSuccess(String json) {
                super.onSuccess(json);
                boolean success = SKResolveJsonUtil.getInstance().resolveIsSuccess(json, MyTeacherListActivity.this);
                if (success) {
                    ArrayList<Star_Teacher_Parse> my_Teather = SKResolveJsonUtil.getInstance().My_Teather(json);
                    mDataList = my_Teather;
                    if (my_Teather.size() == 0) {
                        findViewById(R.id.nodata_layout).setVisibility(View.VISIBLE);
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        MyTeacherListActivity.this.finish();
    }


    @Override
    public void onResume() {
        super.onResume();
        My_Teather();
    }
}
