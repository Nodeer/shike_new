package com.yshow.shike.activities;

import java.util.ArrayList;

import android.content.Context;
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
import com.yshow.shike.utils.MyAsyncHttpResponseHandler;
import com.yshow.shike.utils.Net_Servse;
import com.yshow.shike.utils.SKAsyncApiController;
import com.yshow.shike.utils.SKResolveJsonUtil;

/**
 * 我的老师页面,可以看到老师列表
 */
public class MyTeacherListActivity extends BaseActivity implements View.OnClickListener {
    private DisplayImageOptions options;
    private ImageLoader imageLoader;
    private LinearLayout ll_my_teather;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_teacher);
        TextView titletext = (TextView) findViewById(R.id.title_text);
        titletext.setText("我的老师");
        findViewById(R.id.left_btn).setOnClickListener(this);

        ll_my_teather = (LinearLayout) findViewById(R.id.ll_my_teather);
        options = Net_Servse.getInstence().Picture_Shipei(R.drawable.my_tea_phon);
        imageLoader = ImageLoader.getInstance();

    }

    public View getView(ArrayList<Star_Teacher_Parse> star_Teacher_Parses) {
        View item_view = View.inflate(this, R.layout.my_teather_item, null);
        TextView iv_picture = (TextView) item_view.findViewById(R.id.tv_subject1);
        GridView gridview = (GridView) item_view.findViewById(R.id.gv_my_teather_gridview);
        final MyAdapter adapter = new MyAdapter(star_Teacher_Parses);
        gridview.setAdapter(adapter);
        iv_picture.setText(star_Teacher_Parses.get(0).getSubiect());
        gridview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                Star_Teacher_Parse item = (Star_Teacher_Parse) adapter.getItem(arg2);
                Intent intent = new Intent(MyTeacherListActivity.this, Activity_My_Teacher_Info.class);
                intent.putExtra("mark", "Fragment_My_Teacher");
                intent.putExtra("headpicture", item);
                MyTeacherListActivity.this.startActivity(intent);
            }
        });
        return item_view;
    }

    private void My_Teather() {
        SKAsyncApiController.My_Taeather_Parse(new MyAsyncHttpResponseHandler(this, true) {
            @Override
            public void onSuccess(String json) {
                super.onSuccess(json);
                boolean success = SKResolveJsonUtil.getInstance().resolveIsSuccess(json, MyTeacherListActivity.this);
                if (success) {
                    ArrayList<ArrayList<Star_Teacher_Parse>> my_Teather = SKResolveJsonUtil.getInstance().My_Teather(json);
                    for (int i = 0; i < my_Teather.size(); i++) {
                        ll_my_teather.addView(getView(my_Teather.get(i)));
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        MyTeacherListActivity.this.finish();
    }

    class MyAdapter extends BaseAdapter {
        ArrayList<Star_Teacher_Parse> star_Teacher_Parses;

        public MyAdapter(ArrayList<Star_Teacher_Parse> star_Teacher_Parses) {
            super();
            this.star_Teacher_Parses = star_Teacher_Parses;
        }

        @Override
        public int getCount() {
            return star_Teacher_Parses.size();
        }

        @Override
        public Object getItem(int arg0) {
            return star_Teacher_Parses.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        @Override
        public View getView(int arg0, View arg1, ViewGroup arg2) {
            Star_Teacher_Parse teacher_Parse = star_Teacher_Parses.get(arg0);
            View item_view = View.inflate(MyTeacherListActivity.this, R.layout.my_gridview_teather_item, null);
            TextView tv_wenben = (TextView) item_view.findViewById(R.id.tv_wenben);
            ImageView my_teather_img = (ImageView) item_view.findViewById(R.id.my_teather_img);
            tv_wenben.setText(teacher_Parse.getNickname());
            imageLoader.displayImage(teacher_Parse.getIcon(), my_teather_img, options);
            return item_view;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ll_my_teather.removeAllViews();
        My_Teather();
    }
}
