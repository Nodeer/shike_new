package com.yshow.shike.utils;

import java.util.ArrayList;

import kankan.wheel.widget.OnWheelChangedListener;
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

public class GradeSeltorUtil implements DialogInterface.OnClickListener {
	private Context context;
	private Builder alertDialog;
	private SystemDialogButtonOnclickListening listening;
	private boolean scrolling;
	private ArrayList<SKGrade> sKGrades = null;
//	private boolean gradeChanged = false;
//	private boolean gradeScrolled = false;
	private WheelView year_period;
	private WheelView grade;
	private String gradeId = "";
	private String seltotText = "请选择";
	public GradeSeltorUtil(Context context, final ArrayList<SKGrade> sKGrades) {
		this.sKGrades = sKGrades;
		this.context = context;
		alertDialog = new AlertDialog.Builder(context);
		View inflate = View.inflate(context, R.layout.grade_seltor_layout, null);
		alertDialog.setView(inflate);
		year_period = (WheelView) inflate.findViewById(R.id.year_period);
		year_period.setVisibleItems(3);
		year_period.setViewAdapter(new CountryAdapter(context, sKGrades));
		grade = (WheelView) inflate.findViewById(R.id.grade);
		grade.setVisibleItems(5);
		year_period.addChangingListener(new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				if (!scrolling) {
					updateCities(grade, sKGrades, newValue);
					ininSeltorInfo();
				}
			}
		});
		year_period.addScrollingListener(new OnWheelScrollListener() {
			public void onScrollingStarted(WheelView wheel) {
				scrolling = true;
			}
			public void onScrollingFinished(WheelView wheel) {
				scrolling = false;
				updateCities(grade, sKGrades, year_period.getCurrentItem());
				ininSeltorInfo();
			}
		});
		grade.addScrollingListener(new OnWheelScrollListener() {
			@Override
			public void onScrollingStarted(WheelView wheel) {

			}
			@Override
			public void onScrollingFinished(WheelView wheel) {
				ininSeltorInfo();
			}
		});
		updateCities(grade, sKGrades, 0);
	}
	private void ininSeltorInfo() {
		int currentItem = year_period.getCurrentItem();
		int currentItem2 = grade.getCurrentItem();
		SKGrade skGrade = sKGrades.get(currentItem);
		SkClasses skClasses = skGrade.getClasses().get(currentItem2);
		seltotText = skGrade.getName() + skClasses.getName();
		gradeId = skClasses.getId();
	}
	public void setSystemDialogButtonOnclickListening(
			SystemDialogButtonOnclickListening listening) {
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

	public interface SystemDialogButtonOnclickListening {
		public void onClickLeft();
		public void onClickRight();
	}

	/**
	 * Updates the city wheel
	 */
	private void updateCities(WheelView city, ArrayList<SKGrade> sKGrades,
			int index) {
		ArrayWheelAdapter adapter = new ArrayWheelAdapter(context, sKGrades
				.get(index).getClasses());
		adapter.setTextSize(18);
		city.setViewAdapter(adapter);
		city.setCurrentItem(sKGrades.get(index).getClasses().size() / 2);
		ininSeltorInfo();
	}

	/**
	 * Adapter for countries
	 */
	private class CountryAdapter extends AbstractWheelTextAdapter {
		private ArrayList<SKGrade> sKGrades;

		/**
		 * Constructor
		 */
		protected CountryAdapter(Context context, ArrayList<SKGrade> sKGrades) {
			super(context, R.layout.country_layout, NO_RESOURCE);
			this.sKGrades = sKGrades;
			setItemTextResource(R.id.country_name);
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			View view = super.getItem(index, cachedView, parent);
			return view;
		}

		@Override
		public int getItemsCount() {
			return sKGrades.size();
		}

		@Override
		protected CharSequence getItemText(int index) {
			return sKGrades.get(index).getName();
		}
	}

	public class ArrayWheelAdapter extends AbstractWheelTextAdapter {
		private ArrayList<SkClasses> items;

		public ArrayWheelAdapter(Context context, ArrayList<SkClasses> items) {
			super(context);
			this.items = items;
		}

		@Override
		public CharSequence getItemText(int index) {
			if (index >= 0 && index < items.size()) {
				String item = items.get(index).getName();
				if (item instanceof CharSequence) {
					return (CharSequence) item;
				}
				return item.toString();
			}
			return null;
		}

		@Override
		public int getItemsCount() {
			return items.size();
		}
	}

	public String getGradeId() {
		return gradeId;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	public String getSeltotText() {
		return seltotText;
	}

	public void setSeltotText(String seltotText) {
		this.seltotText = seltotText;
	}
}
