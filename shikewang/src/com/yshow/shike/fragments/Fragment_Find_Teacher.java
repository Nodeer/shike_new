package com.yshow.shike.fragments;
import com.yshow.shike.R;
import com.yshow.shike.activities.Stu_FenLei_Findtea;
import com.yshow.shike.utils.Dialog;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
public class Fragment_Find_Teacher extends Fragment implements OnClickListener{
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
    		Bundle savedInstanceState) {
    	 View view = inflater.inflate(R.layout.fragment_find_teacher, null);
    	 TextView tuijian_teacher = (TextView) view.findViewById(R.id.tv_tuijian_teacher);
    	 TextView find_teacher = (TextView) view.findViewById(R.id.tv_find_teacher);
    	 TextView star_teacher = (TextView) view.findViewById(R.id.tv_star_teacher);
    	 view.findViewById(R.id.tv_zaixian_find_tea).setOnClickListener(this);
    	 tuijian_teacher.setOnClickListener(this);
    	 find_teacher.setOnClickListener(this);
    	 star_teacher.setOnClickListener(this);
		return view;
    }
	@Override
	public void onClick(View v) {
		Bundle bundle = new Bundle();
		switch (v.getId()) {
		// 推荐老师
		case R.id.tv_tuijian_teacher:
			bundle.putInt("tuijian_tea", 0);
			break;
		// 明星老师
		case R.id.tv_star_teacher:
			bundle.putInt("star_tea", 1);
			break;
		//在线老师
		case R.id.tv_zaixian_find_tea:
			bundle.putInt("online_tea", 2);
			break;
		// 搜索老师
		case R.id.tv_find_teacher:
			bundle.putInt("find_tea", 3);
			break;
		}
		Dialog.intent(getActivity(),  Stu_FenLei_Findtea.class, bundle);
	}
}
