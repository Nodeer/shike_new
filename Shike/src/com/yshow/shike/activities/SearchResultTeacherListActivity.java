package com.yshow.shike.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yshow.shike.R;
import com.yshow.shike.entity.Star_Teacher_Parse;

import java.util.ArrayList;

/**
 * Created by Administrator on 2014-10-29.
 */
public class SearchResultTeacherListActivity extends BaseActivity implements View.OnClickListener {

    private ListView list_view;

    private MyAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_result_teacher_llist_layout);

        TextView titletext = (TextView) findViewById(R.id.title_text);
        titletext.setText("老师列表");
        findViewById(R.id.left_btn).setOnClickListener(this);


        ArrayList<Star_Teacher_Parse> list = (ArrayList<Star_Teacher_Parse>) getIntent().getSerializableExtra("data");
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
        finish();
    }

    // listview 适配器
    class MyAdapter extends BaseAdapter {
        ArrayList<Star_Teacher_Parse> list;

        public MyAdapter(ArrayList<Star_Teacher_Parse> list) {
            super();
            this.list = list;
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
            DisplayImageOptions options = new DisplayImageOptions.Builder()
                    .cacheOnDisc(true)
                    .showImageForEmptyUri(R.drawable.my_tea_phon)
                    .showImageOnFail(R.drawable.my_tea_phon)
                    .cacheInMemory(true).build();
            ImageLoader imageLoader = ImageLoader.getInstance();
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
            ImageView iv_teather_sele = (ImageView) convertView
                    .findViewById(R.id.iv_teather_sele);
            if (teacher_Parse.getiSmyteath().equals("1")) {
                iv_teather_sele.setVisibility(View.VISIBLE);
                imageLoader.displayImage(teacher_Parse.getIcon(),
                        teather_picture, options);
            } else {
                iv_teather_sele.setVisibility(View.GONE);
                imageLoader.displayImage(teacher_Parse.getIcon(),
                        teather_picture, options);
            }
            tv_nicheng.setText(teacher_Parse.getNickname());
            tv_subject.setText(teacher_Parse.getSubiect());
            tv_grade.setText(teacher_Parse.getGrade());
            tv_diqu.setText(teacher_Parse.getArea());
            tv_gerenxinxi.setText(teacher_Parse.getInfo());
            return convertView;
        }
    }
}
