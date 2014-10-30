package com.yshow.shike.activities;

import java.util.ArrayList;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yshow.shike.R;
import com.yshow.shike.entity.Star_Teacher_Parse;
import com.yshow.shike.utils.*;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yshow.shike.widget.TopAndBottomLoadListView;
import com.yshow.shike.widget.XListView;

public class OnlineTeaListActivity extends BaseActivity implements TopAndBottomLoadListView.OnRefreshListener, XListView.IXListViewListener, View.OnClickListener {
    private XListView mOnlineListView;
    private DisplayImageOptions options;
    private ImageLoader imageLoader;
    private MyAdapter adapter;
    private int page = 1;
    private ArrayList<Star_Teacher_Parse> mDataList = new ArrayList<Star_Teacher_Parse>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.online_tea_listview);
        TextView titletext = (TextView) findViewById(R.id.title_text);
        titletext.setText("在线老师");
        findViewById(R.id.left_btn).setOnClickListener(this);

        mOnlineListView = (XListView) findViewById(R.id.online_lv);
        options = Net_Servse.getInstence().Picture_Shipei(R.drawable.my_tea_phon);
        imageLoader = ImageLoader.getInstance();
        adapter = new MyAdapter();
        mOnlineListView.setAdapter(adapter);
        mOnlineListView.setXListViewListener(this);
        Seather_Tea(true);
        mOnlineListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                if (arg3 == -1) {
                    // 点击的是headerView或者footerView
                    return;
                }
                int realPosition = (int) arg3;
                Star_Teacher_Parse item = (Star_Teacher_Parse) adapter.getItem(realPosition);
                Bundle bundle = new Bundle();
                bundle.putSerializable("headpicture", item);
                Dialog.intent(OnlineTeaListActivity.this, TeacherInfoActivity.class, bundle);

            }
        });
    }

    @Override
    public void onLoadMore() {
        page++;
        Seather_Tea(false);
    }

    @Override
    public void onRefresh() {
        page = 1;
        mDataList.clear();
        Seather_Tea(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_btn:
                finish();
                break;
        }
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
                convertView = View.inflate(OnlineTeaListActivity.this, R.layout.fragment_start_text, null);
            }
            TextView tv_grade = (TextView) convertView.findViewById(R.id.tv_grade);
            ImageView tea_piture = (ImageView) convertView.findViewById(R.id.iv_teather_picture);
            TextView tea_name = (TextView) convertView.findViewById(R.id.tv_nicheng);
            TextView tea_subject = (TextView) convertView.findViewById(R.id.tv_subject);
            TextView diqu = (TextView) convertView.findViewById(R.id.tv_diqu);
            TextView tea_info = (TextView) convertView.findViewById(R.id.tv_gerenxinxi);
            if (on_Tea != null) {
                tea_name.setText(on_Tea.getNickname());
                tv_grade.setText(on_Tea.getGrade());
                tea_subject.setText(on_Tea.getSubiect());
                diqu.setText(on_Tea.getArea());
                tea_info.setText(on_Tea.getInfo());
                imageLoader.displayImage(on_Tea.getIcon(), tea_piture, options);
            }
            return convertView;
        }
    }

    private void Seather_Tea(boolean b) {
        SKAsyncApiController.OnLine_Tea(page, "0", new MyAsyncHttpResponseHandler(this, b) {
            @Override
            public void onSuccess(String json) {
                super.onSuccess(json);
                boolean success = SKResolveJsonUtil.getInstance().resolveIsSuccess(json, OnlineTeaListActivity.this);
                if (success) {
                    ArrayList<Star_Teacher_Parse> list = SKResolveJsonUtil.getInstance().start_teather(json);
                    mDataList.addAll(list);
                    if (list.size() < 20) {
                        mOnlineListView.setPullLoadEnable(false);
                    } else {
                        mOnlineListView.setPullLoadEnable(true);
                    }
                    if (mDataList.size() == 0) {
                        Toast.makeText(OnlineTeaListActivity.this, "没有在线老师", Toast.LENGTH_SHORT).show();
                    }
                    adapter.notifyDataSetChanged();
                    onLoad();
                }
            }
        });
    }

    private void onLoad() {
        mOnlineListView.stopRefresh();
        mOnlineListView.stopLoadMore();
        mOnlineListView.setRefreshTime(Timer_Uils.getCurrentTime());
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }
}