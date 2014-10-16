package com.yshow.shike.fragments;

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
import com.yshow.shike.entity.Teacher_Duihuan_Record;
import com.yshow.shike.utils.MyAsyncHttpResponseHandler;
import com.yshow.shike.utils.SKAsyncApiController;

import java.lang.reflect.Type;

/**
 * 积分使用记录
 */
public class Fragment_Teacher_Zhanghu_duihuan extends Fragment {
	private Teacher_Duihuan_Record tdr;
	private LinearLayout linearLayout;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_duihuan_record, null);
		linearLayout = (LinearLayout) view.findViewById(R.id.duihuan_record_linearlayout);
		getData();
		return view;
	}

	public void getData() {
		SKAsyncApiController.duihuanRecord(new MyAsyncHttpResponseHandler(getActivity(), true) {
            @Override
            public void onSuccess(String arg0) {
                super.onSuccess(arg0);
                Type tp = new TypeToken<Teacher_Duihuan_Record>() {
                }.getType();
                Gson gs = new Gson();
                tdr = gs.fromJson(arg0, tp);
                if (tdr != null) {
                    for (int i = 0; i < tdr.getData().size(); i++) {
                        String str = tdr.getData().get(i).getDates() + " " + tdr.getData().get(i).getMessge();
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT);
                        params.bottomMargin = 5;
                        if (isAdded()) {
                            TextView text = new TextView(getActivity());
                            text.setTextSize(20);
                            text.setLayoutParams(params);
                            text.setText(str);
                            linearLayout.addView(text);
                            text.setTextColor(getResources().getColor(R.color.blue));
                        }
                    }
                }
            }
        });
	}
}