package com.yshow.shike.fragments;
import com.yshow.shike.R;
import com.yshow.shike.activities.StudentRegisterActivity;
import com.yshow.shike.activities.TeacherRegisterActivity;
import com.yshow.shike.utils.Auto_Login_User;
import com.yshow.shike.utils.Dilog_Share;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
public class UserRegisterFragment extends Fragment{
	private ImageView teacher_register;
	private LinearLayout student_register;
	private Auto_Login_User auto_Login;
	private Dialog dialog;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.sk_fragment_userregister, null);
		auto_Login = new Auto_Login_User(getActivity());
		dialog = Dilog_Share.Dilog_Anim_Reg(getActivity(), listener);
		teacher_register=(ImageView) view.findViewById(R.id.teacher_register);
		student_register=(LinearLayout) view.findViewById(R.id.student_register);
		teacher_register.setOnClickListener(listener);
		student_register.setOnClickListener(listener);
		return view;
	}
	private OnClickListener listener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.teacher_register:
				Intent intent=new Intent(getActivity(),TeacherRegisterActivity.class);
				getActivity().startActivity(intent);
				break;
			case R.id.student_register:
				dialog.show();
				break;
			case R.id.tv_TiWen_Que:
				auto_Login.auto_login_info();
				dialog.dismiss();
				break;
			case R.id.tv_MaSg_Reg:
				Intent intent2=new Intent(getActivity(),StudentRegisterActivity.class);
				getActivity().startActivity(intent2);
				dialog.dismiss();
				break;
			case R.id.tv_Dil_cancel:
				dialog.dismiss();
				break;
			}
		}
	};
}
