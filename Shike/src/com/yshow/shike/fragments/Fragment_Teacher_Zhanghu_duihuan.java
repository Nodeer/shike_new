package com.yshow.shike.fragments;

import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yshow.shike.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.yshow.shike.entity.Count_Info;
import com.yshow.shike.entity.RecordData;
import com.yshow.shike.entity.Teacher_Duihuan_Record;
import com.yshow.shike.utils.MyAsyncHttpResponseHandler;
import com.yshow.shike.utils.SKAsyncApiController;
import com.yshow.shike.utils.SKResolveJsonUtil;

import java.lang.reflect.Type;
import java.util.List;

/**
 * 积分使用记录
 */
public class Fragment_Teacher_Zhanghu_duihuan extends Fragment {
    private Teacher_Duihuan_Record tdr;
    private ListView listview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_duihuan_record, null);
        listview = (ListView) view.findViewById(R.id.account_linearout);
        getData();
        return view;
    }

    class MyAdapter extends BaseAdapter {
        List<RecordData> count_Info_pase;

        public MyAdapter(List<RecordData> count_Info_pase) {
            super();
            this.count_Info_pase = count_Info_pase;
        }

        @Override
        public int getCount() {
            return count_Info_pase.size();
        }

        @Override
        public Object getItem(int arg0) {
            return count_Info_pase.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        @Override
        public View getView(int arg0, View convertView, ViewGroup arg2) {
            RecordData info = count_Info_pase.get(arg0);
            View view = View.inflate(getActivity(), R.layout.count_info_item, null);
            TextView tv_day = (TextView) view.findViewById(R.id.tv_day);
            TextView tv_time = (TextView) view.findViewById(R.id.tv_time);
            TextView tv_reg = (TextView) view.findViewById(R.id.tv_reg);
            tv_day.setText(info.getDates());
//            tv_time.setText(info.get() + ":" + info.getI());
            tv_reg.setText(info.getMessge());
            return view;
        }
    }

    public void getData() {
        SKAsyncApiController.duihuanRecord(new MyAsyncHttpResponseHandler(getActivity(), true) {
            @Override
            public void onSuccess(String arg0) {
                super.onSuccess(arg0);
                boolean success = SKResolveJsonUtil.getInstance().resolveIsSuccess(arg0,
                        getActivity());
                if (success) {
                    Type tp = new TypeToken<Teacher_Duihuan_Record>() {
                    }.getType();
                    Gson gs = new Gson();
                    tdr = gs.fromJson(arg0, tp);

                    List<RecordData> count_Info_pase = tdr.getData();
                    listview.setAdapter(new MyAdapter(count_Info_pase));
                }
            }
        });
    }
}