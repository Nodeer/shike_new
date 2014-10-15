package com.yshow.shike.activities;
import com.umeng.analytics.MobclickAgent;
import com.yshow.shike.R;
import com.yshow.shike.fragments.Fragment_Online_Tea;
import com.yshow.shike.fragments.Fragment_Recommend_Teacher;
import com.yshow.shike.fragments.Fragment_Search_Teacher;
import com.yshow.shike.fragments.Fragment_Star_Teacher;
import com.yshow.shike.utils.ScreenSizeUtil;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
/**
 * 找老师的所有控件
 * @author Administrator
 */
public class Stu_FenLei_Findtea extends FragmentActivity{
	  private Context context;
	  private TextView tuijian,start,tv_hunt,online;//推荐 明星 搜索 在线老师
	  private int mea_view; //测量控件的方法
	  private View undline,bac_huise; // 红线部分
	  private FragmentManager manager;
	  private FragmentTransaction ft;
	  private LinearLayout li_seather; // 搜索按钮
	  private int startX = 0;
	  private Fragment_Search_Teacher Fragment_Search_Teacher = new Fragment_Search_Teacher();
		@Override
        protected void onCreate(Bundle arg0) {
        	super.onCreate(arg0);
        	context = this;
        	setContentView(R.layout.stu_fenlei_findtea);
        	initData();
        }
		private void initData() {
			tuijian = (TextView) findViewById(R.id.tv_tuijian_tea);
			start = (TextView) findViewById(R.id.tv_start_tea);
			tv_hunt = (TextView) findViewById(R.id.tv_hunt);
			online = (TextView) findViewById(R.id.tv_online_tea);
			li_seather = (LinearLayout) findViewById(R.id.li_seather);
			findViewById(R.id.tv_fen_lei_back).setOnClickListener(listener);
			tuijian.setOnClickListener(listener);	
			li_seather.setOnClickListener(listener);
			start.setOnClickListener(listener);
			tv_hunt.setOnClickListener(listener);
			online.setOnClickListener(listener);
			undline = findViewById(R.id.stu_findtea_undline);
			bac_huise = findViewById(R.id.find_tea_huise);
	// 初始化红线的长度
			mea_view = ScreenSizeUtil.getScreenWidth(context,4);
			int view_hig = ScreenSizeUtil.Dp2Px(context, 48);
			android.widget.RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(mea_view,view_hig);
			bac_huise.setLayoutParams(layoutParams);
			LayoutParams params = new LinearLayout.LayoutParams(mea_view,2);
			undline.setLayoutParams(params);
			manager = getSupportFragmentManager();
			ft = manager.beginTransaction();
			Bundle bundle = getIntent().getExtras();
			if((Integer)bundle.get("tuijian_tea") != null){
				ft.replace(R.id.find_tea_content,new Fragment_Recommend_Teacher());
				li_seather.setVisibility(View.GONE);
			}else if((Integer)bundle.get("star_tea") != null) {
				Fragment_Move(R.color.reg,new Fragment_Star_Teacher(),1);
				start.setTextColor(getResources().getColor(R.color.reg));
				li_seather.setVisibility(View.GONE);
			}else if ((Integer)bundle.get("online_tea") != null) {
				Fragment_Move(R.color.reg,new Fragment_Online_Tea(),2);
				online.setTextColor(getResources().getColor(R.color.reg));
				li_seather.setVisibility(View.GONE);
			}else {
				Fragment_Move(R.color.reg,Fragment_Search_Teacher,3);
				tv_hunt.setTextColor(getResources().getColor(R.color.reg));
				li_seather.setVisibility(View.VISIBLE);
			}
			ft.commit();
		}
		private OnClickListener listener = new OnClickListener(){
			public void onClick(android.view.View v) {
				ft = manager.beginTransaction();
				switch (v.getId()) { 
				// 推荐老师
				case R.id.tv_tuijian_tea:
					Fragment_Move(R.color.reg,new Fragment_Recommend_Teacher(),0);
					tuijian.setTextColor(getResources().getColor(R.color.reg));
					li_seather.setVisibility(View.GONE);
					break;
				// 明星老师
				case R.id.tv_start_tea: 
					Fragment_Move(R.color.reg,new Fragment_Star_Teacher(),1);
					start.setTextColor(getResources().getColor(R.color.reg));
					li_seather.setVisibility(View.GONE);
					break;
				//在线老师
				case R.id.tv_online_tea:  
					Fragment_Move(R.color.reg,new Fragment_Online_Tea(),2);
					online.setTextColor(getResources().getColor(R.color.reg));
					li_seather.setVisibility(View.GONE);
					break;
				// 搜素老师
				case R.id.tv_hunt:
					Fragment_Move(R.color.reg,Fragment_Search_Teacher,3);
					tv_hunt.setTextColor(getResources().getColor(R.color.reg));
					li_seather.setVisibility(View.VISIBLE);
					break;
				case R.id.tv_fen_lei_back:
					finish();
					break;
				case R.id.li_seather:
					Fragment_Search_Teacher.getHandler().sendEmptyMessage(0);
					break;
				}
				ft.commit();
			};
		};
		//开始执行动画的位移
		private void StartTransAnim(float fromXDelta,float toXDelta,View view) {
			TranslateAnimation anim = new TranslateAnimation(fromXDelta*mea_view, toXDelta*mea_view, 0, 0);
			anim.setDuration(500);
			anim.setFillAfter(true);
			view.startAnimation(anim);
		}
		// Fargment 替换
		private void Fragment_Move(int colour,Fragment fragment,int loction) {
			start.setTextColor(getResources().getColor(R.color.sk_student_main_color));
			online.setTextColor(getResources().getColor(R.color.sk_student_main_color));
			tv_hunt.setTextColor(getResources().getColor(R.color.sk_student_main_color));
			tuijian.setTextColor(getResources().getColor(R.color.sk_student_main_color));
			ft.replace(R.id.find_tea_content,fragment);
			StartTransAnim(startX, loction,undline);
			StartTransAnim(startX, loction,bac_huise);
			startX = loction;
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
