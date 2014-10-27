package com.yshow.shike.fragments;

import com.yshow.shike.R;
import com.yshow.shike.activities.Login_Reg_Activity;
import com.yshow.shike.activities.StudentRegisterActivity;
import com.yshow.shike.activities.TeacherRegisterActivity;
import com.yshow.shike.utils.Auto_Login_User;
import com.yshow.shike.utils.Dilog_Share;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * 用户注册界面
 */
public class UserRegisterFragment extends Fragment {
    private RelativeLayout teacher_register;
    private RelativeLayout student_register;
    private RelativeLayout ask_first;
    private Auto_Login_User auto_Login;
    private ImageView mSlideBottomImg;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sk_fragment_userregister, null);
        auto_Login = new Auto_Login_User(getActivity());
        teacher_register = (RelativeLayout) view.findViewById(R.id.tea_reg_btn);
        student_register = (RelativeLayout) view.findViewById(R.id.stu_reg_btn);
        ask_first = (RelativeLayout) view.findViewById(R.id.ask_question_btn);
        teacher_register.setOnClickListener(listener);
        student_register.setOnClickListener(listener);
        ask_first.setOnClickListener(listener);
        mSlideBottomImg = (ImageView) view.findViewById(R.id.slide_img);
        mSlideBottomImg.setOnClickListener(listener);

//        setCancelable(false);

        return view;
    }

    private OnClickListener listener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tea_reg_btn:
                    Intent intent = new Intent(getActivity(), TeacherRegisterActivity.class);
                    getActivity().startActivity(intent);
                    break;
                case R.id.ask_question_btn:
//                    auto_Login.auto_login_info();
                    break;
                case R.id.stu_reg_btn:
                    Intent intent2 = new Intent(getActivity(), StudentRegisterActivity.class);
                    getActivity().startActivity(intent2);
                    break;
                case R.id.slide_img:
                    KeyEvent keyEvent = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK);
                    getActivity().onKeyDown(KeyEvent.KEYCODE_BACK, keyEvent);
                    break;
            }
        }
    };


    @Override
    public void onDestroy() {
        ((Login_Reg_Activity)getActivity()).changeSlide(false);
        super.onDestroy();
    }
}
