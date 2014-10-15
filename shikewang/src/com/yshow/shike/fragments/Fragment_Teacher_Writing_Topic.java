package com.yshow.shike.fragments;

import android.widget.Toast;
import com.yshow.shike.R;
import com.yshow.shike.UIApplication;
import com.yshow.shike.activities.Activity_File_Three;
import com.yshow.shike.activities.Activity_Teather_Ti_Ku;
import com.yshow.shike.activities.Activity_Tea_Tool_Sele;
import com.yshow.shike.utils.Dialog;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * 老师端点击制作题目tab以后显示的页面,里面有制作题目和题库两个页面
 */
public class Fragment_Teacher_Writing_Topic extends Fragment implements
		OnClickListener {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_teacher_writing_topic,container, false);
		LinearLayout stu_gongju_text = (LinearLayout) view.findViewById(R.id.stu_gongju_text);
		LinearLayout stu_tiku_text = (LinearLayout) view.findViewById(R.id.stu_tiku_text);
		stu_gongju_text.setOnClickListener(this);
		stu_tiku_text.setOnClickListener(this);
		return view;
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.stu_gongju_text://制作题目
            Toast.makeText(getActivity(),"功能开发中,敬请期待",Toast.LENGTH_SHORT).show();
//            Activity_File_Three.mSaveTiku = null;
//			UIApplication.getInstance().cleanPicUrls();
//			UIApplication.getInstance().cleanbitmaplist();
//			Dialog.Intent(getActivity(), Activity_Tea_Tool_Sele.class);
			break;
		case R.id.stu_tiku_text://题库
            Toast.makeText(getActivity(),"功能开发中,敬请期待",Toast.LENGTH_SHORT).show();
//			Intent intent = new Intent(getActivity(),Activity_Teather_Ti_Ku.class);
//			intent.putExtra("question_into", "TiKu");
//			startActivity(intent);
			break;
		}
	}
}