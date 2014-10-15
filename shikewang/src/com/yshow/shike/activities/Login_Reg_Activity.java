package com.yshow.shike.activities;

import com.umeng.analytics.MobclickAgent;
import com.yshow.shike.R;
import com.yshow.shike.activities.Age_Person_Info.Callback;
import com.yshow.shike.fragments.UserLoginFragment;
import com.yshow.shike.fragments.UserRegisterFragment;
import com.yshow.shike.utils.ScreenSizeUtil;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MotionEvent;
import android.view.View;
import android.content.Context;
import android.view.View.OnClickListener;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class Login_Reg_Activity extends FragmentActivity implements Callback {
    private FragmentManager manager;
    private FragmentTransaction ft;
    private TextView tv_reg;
    private TextView tv_login;
    private View undline, bac_huise;
    private int mea_view;
    private int startX = 0;
    private Context context;

    //	private Login_Reg_Activity reg_Activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//		reg_Activity = this;
        context = this;
        setContentView(R.layout.login_reg_activity);
        tv_reg = (TextView) findViewById(R.id.tv_reg);
        tv_login = (TextView) findViewById(R.id.tv_login);
        undline = findViewById(R.id.move_undline);
        bac_huise = findViewById(R.id.bac_huise);
        mea_view = ScreenSizeUtil.getScreenWidth(context, 2);
        LayoutParams params = new LinearLayout.LayoutParams(mea_view, 2);
        int view_hig = ScreenSizeUtil.Dp2Px(context, 48);
        android.widget.RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(mea_view, view_hig);
        bac_huise.setLayoutParams(layoutParams);
        undline.setLayoutParams(params);
        tv_reg.setOnClickListener(listener);
        tv_login.setOnClickListener(listener);
        manager = getSupportFragmentManager();
        ft = manager.beginTransaction();
        ft.replace(R.id.content, new UserRegisterFragment());
        ft.commit();
    }

    private OnClickListener listener = new OnClickListener() {
        @Override
        public void onClick(View arg0) {
            ft = manager.beginTransaction();
            switch (arg0.getId()) {
                case R.id.tv_reg:
                    ft.replace(R.id.content, new UserRegisterFragment());
                    StartTransAnim(startX, 0, undline);
                    StartTransAnim(startX, 0, bac_huise);
                    tv_login.setTextColor(getResources().getColor(R.color.log));
                    tv_reg.setTextColor(getResources().getColor(R.color.reg));
                    startX = 0;
                    break;
                case R.id.tv_login:
                    ft.replace(R.id.content, new UserLoginFragment());
                    StartTransAnim(startX, 1, undline);
                    StartTransAnim(startX, 1, bac_huise);
                    tv_reg.setTextColor(getResources().getColor(R.color.log));
                    tv_login.setTextColor(getResources().getColor(R.color.reg));
                    startX = 1;
                    break;
            }
            ft.commit();
        }
    };

    //开始执行动画的位移
    private void StartTransAnim(float fromXDelta, float toXDelta, View view) {
        TranslateAnimation anim = new TranslateAnimation(fromXDelta * mea_view, toXDelta * mea_view, 0, 0);
        anim.setDuration(500);
        anim.setFillAfter(true);
        view.startAnimation(anim);
    }

    // 修改个人信息页面回到
    @Override
    public void back() {
        FragmentTransaction beginTransaction = manager.beginTransaction();
        beginTransaction.replace(R.id.content, new UserLoginFragment());
        beginTransaction.commit();
        StartTransAnim(startX, 1, undline);
        StartTransAnim(startX, 1, bac_huise);
    }

    //点击屏幕 关闭输入弹出框
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (getCurrentFocus() != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(getCurrentFocus().getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

}
