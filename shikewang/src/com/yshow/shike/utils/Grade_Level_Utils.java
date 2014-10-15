package com.yshow.shike.utils;
import java.util.ArrayList;

import kankan.wheel.widget.OnWheelScrollListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.AbstractWheelTextAdapter;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;

import com.yshow.shike.R;
import com.yshow.shike.entity.SKGrade;
import com.yshow.shike.entity.SkClasses;
public class Grade_Level_Utils implements DialogInterface.OnClickListener{
	private Context context;
	private Builder alertDialog;
	private GradeSeltorUtilButtonOnclickListening listening;
	private ArrayList<SKGrade> sKGrade = null;
	private WheelView area;
	private String gradeID = "";
	private String seltotText = "请选择";
	@SuppressWarnings("unused")
	private boolean gradeScrolled;
	@SuppressWarnings("unused")
	private boolean gradeChanged;
	private String toGradeId="";
	private String formGradeId="";
	private String toGradeIdName="";
	private String formGradeIdName="";
	public Grade_Level_Utils(Context context, final ArrayList<SKGrade> sKGrade) {
		this.sKGrade = sKGrade;
		this.context = context;
		alertDialog = new AlertDialog.Builder(context);
		View inflate = View.inflate(context, R.layout.area_seltor_layout, null);
		alertDialog.setView(inflate);
		area = (WheelView) inflate.findViewById(R.id.country);
		area.setVisibleItems(5);
		area.setViewAdapter(new CountryAdapter(context, sKGrade));
		area.addScrollingListener(scrollListener);
		area.addScrollingListener(new OnWheelScrollListener() {
			public void onScrollingStarted(WheelView wheel) {
			}
			public void onScrollingFinished(WheelView wheel) {
			}
		});
		area.setCurrentItem(1);
		ininSeltorInfo();
	}
	OnWheelScrollListener scrollListener = new OnWheelScrollListener() {
		public void onScrollingStarted(WheelView wheel) {
			gradeScrolled = true;
		}
		public void onScrollingFinished(WheelView wheel) {
			gradeScrolled = false;
			gradeChanged = true;
			ininSeltorInfo();
			gradeChanged = false;
		}
	};
	private void ininSeltorInfo() {
		int currentItem = area.getCurrentItem();
		SKGrade sKGradItme = sKGrade.get(currentItem);
		 ArrayList<SkClasses> classes = sKGradItme.getClasses();
		 
		 SkClasses skClasses = classes.get(sKGradItme.getClasses().size()-1);
		 formGradeId = skClasses.getId();
		 formGradeIdName= skClasses.getName();
		 
		 SkClasses skClasses2 = classes.get(0);
		toGradeId  =skClasses2.getId() ;
		toGradeIdName = skClasses2.getName();
		
		seltotText = sKGradItme.getName();
		gradeID = sKGradItme.getId();
	}
	public void setGradeSeltorUtilButtonOnclickListening(
			GradeSeltorUtilButtonOnclickListening listening) {
		this.listening = listening;
	}
	public void settitle(String title) {
		alertDialog.setTitle(title);
	}
	public void setMessage(String message) {
		alertDialog.setMessage(message);
	}
	public void setLeftButtonText(String text) {
		alertDialog.setPositiveButton(text, this);
	}
	public void setRightButtonText(String text) {
		alertDialog.setNegativeButton(text, this);
	}
	public void show() {
		alertDialog.show();
	}
	@Override
	public void onClick(DialogInterface dialog, int which) {
		switch (which) {
		case DialogInterface.BUTTON_POSITIVE:
			if (listening != null) {
				listening.onClickLeft();
			}
			break;
		case DialogInterface.BUTTON_NEGATIVE:
			if (listening != null) {
				listening.onClickRight();
			}
			break;
		}
	}
	public interface GradeSeltorUtilButtonOnclickListening {
		public void onClickLeft();
		public void onClickRight();
	}
	private class CountryAdapter extends AbstractWheelTextAdapter {
		private ArrayList<SKGrade> sKGrade;
		protected CountryAdapter(Context context, ArrayList<SKGrade> sKGrade) {
			super(context, R.layout.country_layout, NO_RESOURCE);
			this.sKGrade = sKGrade;
			setItemTextResource(R.id.country_name);
		}
		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			View view = super.getItem(index, cachedView, parent);
			return view;
		}
		@Override
		public int getItemsCount() {
			return sKGrade.size();
		}
		@Override
		protected CharSequence getItemText(int index) {
			return sKGrade.get(index).getName();
		}
	}
	public String getGradeId() {
		return gradeID;
	}
	public String getSeltotText() {
		return seltotText;
	}
	public String getToGradeId() {
		return toGradeId;
	}
	public String getToGradeIdName() {
		return toGradeIdName;
	}
	
	public String getFormGradeId() {
		return toGradeId;
	}
	public String getFormGradeIdName() {
		return formGradeIdName;
	}
 }

