package com.yshow.shike.fragments;
import java.lang.reflect.Type;
import com.yshow.shike.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yshow.shike.entity.Teacher_Duihuan_Record;
import com.yshow.shike.utils.MyAsyncHttpResponseHandler;
import com.yshow.shike.utils.SKAsyncApiController;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
 /**
  * 老师端兑换信息
  * @author Administrator
  *
  */
public class Fragment_Teacher_Zhanghu_Shouru extends Fragment {
	private Teacher_Duihuan_Record tdr;
	private LinearLayout linearLayout;
	private Context context;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = getActivity();
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.feagment_shouru_record, null);
		linearLayout = (LinearLayout) view.findViewById(R.id.shouru_record_linearlayout);
		getData();
		return view;
	}
	public void getData() {
		SKAsyncApiController.inComeRecord(new MyAsyncHttpResponseHandler(context, true) {
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
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                        params.bottomMargin = 5;
                        if (isAdded()) {
                            TextView text = new TextView(context);
                            text.setTextSize(20);
                            text.setLayoutParams(params);
                            text.setText(str);
                            linearLayout.addView(text);
                            text.setTextColor(context.getResources().getColor(R.color.blue));
                        }
                    }
                }
            }
        });
	}
}
