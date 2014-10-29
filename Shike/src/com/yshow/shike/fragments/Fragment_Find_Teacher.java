package com.yshow.shike.fragments;

import com.yshow.shike.R;
import com.yshow.shike.activities.MyTeacherListActivity;
import com.yshow.shike.activities.OnlineTeaListActivity;
import com.yshow.shike.activities.RecommendTeacherListActivity;
import com.yshow.shike.activities.SearchTeacherActivity;
import com.yshow.shike.activities.StarTeacherListActivity;
import com.yshow.shike.utils.Dialog;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class Fragment_Find_Teacher extends Fragment implements OnClickListener {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_find_teacher, null);
        TextView titletext = (TextView) view.findViewById(R.id.title_text);
        titletext.setText("找老师");
        view.findViewById(R.id.left_btn).setOnClickListener(this);
        view.findViewById(R.id.online_tea_btn).setOnClickListener(this);
        view.findViewById(R.id.my_tea_btn).setOnClickListener(this);
        view.findViewById(R.id.tuijian_tea_btn).setOnClickListener(this);
        view.findViewById(R.id.mingxing_tea_btn).setOnClickListener(this);
        view.findViewById(R.id.search_tea_btn).setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 推荐老师
            case R.id.tuijian_tea_btn:
                Dialog.Intent(getActivity(), RecommendTeacherListActivity.class);
                break;
            // 明星老师
            case R.id.mingxing_tea_btn:
                Dialog.Intent(getActivity(), StarTeacherListActivity.class);
                break;
            //在线老师
            case R.id.online_tea_btn:
                Dialog.Intent(getActivity(), OnlineTeaListActivity.class);
                break;
            // 搜索老师
            case R.id.search_tea_btn:
                Dialog.Intent(getActivity(), SearchTeacherActivity.class);
                break;
            //我的老师
            case R.id.my_tea_btn:
                Dialog.Intent(getActivity(), MyTeacherListActivity.class);
                break;
            case R.id.left_btn:
                getActivity().finish();
                break;
        }
    }
}
