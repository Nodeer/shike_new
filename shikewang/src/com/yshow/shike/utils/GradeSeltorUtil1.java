//package com.yshow.shike.utils;
//
//import java.util.ArrayList;
//
//import kankan.wheel.widget.OnWheelChangedListener;
//import kankan.wheel.widget.OnWheelScrollListener;
//import kankan.wheel.widget.WheelView;
//import kankan.wheel.widget.adapters.AbstractWheelTextAdapter;
//import android.app.AlertDialog;
//import android.app.AlertDialog.Builder;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.view.View;
//import android.view.ViewGroup;
//
//import com.yshow.shike.R;
//import com.yshow.shike.entity.SKGrade;
//import com.yshow.shike.entity.SkClasses;
//
//public class GradeSeltorUtil1 implements DialogInterface.OnClickListener {
//	private Context context;
//	private Builder alertDialog;
//	private SystemDialogButtonOnclickListening listening;
//	@SuppressWarnings("unused")
//	private boolean scrolling;
//	@SuppressWarnings("unused")
//	private ArrayList<SKGrade> sKGrades = null;
//	@SuppressWarnings("unused")
//	private boolean gradeChanged = false;
//	@SuppressWarnings("unused")
//	private boolean gradeScrolled = false;
//	private WheelView country;
//	private WheelView city;
//	private String gradeId = "";
//	private String seltotText = "请选择";
//	private SKGrade skGrade;
//	private int cityCurrentItem;
//	private String downGradeid = "";
//	private String upGradeid = "";
//	public GradeSeltorUtil1(Context context, final ArrayList<SKGrade> sKGrades,
//			String jieduan) {
//		this.sKGrades = sKGrades;
//		this.context = context;
//		for (SKGrade skGrade : sKGrades) {
//			boolean equals = skGrade.getId().equals(jieduan);
//			if (equals) {
//				this.skGrade = skGrade;
//				// return;
//			}
//		}
//		alertDialog = new AlertDialog.Builder(context);
//		View inflate = View
//				.inflate(context, R.layout.grade_seltor_layout, null);
//		alertDialog.setView(inflate);
//
//		country = (WheelView) inflate.findViewById(R.id.country);
//		country.setVisibleItems(3);
//		ArrayList<SkClasses> classes = this.skGrade.getClasses();
//		country.setViewAdapter(new CountryAdapter(context, classes));
//
//		city = (WheelView) inflate.findViewById(R.id.city);
//		city.setVisibleItems(5);
//
//		ArrayWheelAdapter adapter = new ArrayWheelAdapter(context,
//				skGrade.getClasses());
//		adapter.setTextSize(18);
//		city.setViewAdapter(adapter);
//		country.addScrollingListener(scrollListener);
//		city.addScrollingListener(scrollListener);
//
//		country.addChangingListener(new OnWheelChangedListener() {
//
//			public void onChanged(WheelView wheel, int oldValue, int newValue) {
//				cityCurrentItem = newValue;
//				city.setCurrentItem(newValue);
//			}
//		});
//		city.addChangingListener(new OnWheelChangedListener() {
//			public void onChanged(WheelView wheel, int oldValue, int newValue) {
//				city.setCurrentItem(newValue);
//				if (newValue < cityCurrentItem) {
//					country.setCurrentItem(newValue);
//				}
//			}
//		});
//		country.addScrollingListener(new OnWheelScrollListener() {
//			public void onScrollingStarted(WheelView wheel) {
//				scrolling = true;
//			}
//
//			public void onScrollingFinished(WheelView wheel) {
//				scrolling = false;
//				// updateCities(city, sKGrades, country.getCurrentItem());
//			}
//		});
//
//		country.setCurrentItem(1);
//		ininSeltorInfo();
//	}
//
//	OnWheelScrollListener scrollListener = new OnWheelScrollListener() {
//
//		public void onScrollingStarted(WheelView wheel) {
//			gradeScrolled = true;
//		}
//
//		public void onScrollingFinished(WheelView wheel) {
//			gradeScrolled = false;
//			gradeChanged = true;
//			ininSeltorInfo();
//			gradeChanged = false;
//		}
//	};
//
//	private void ininSeltorInfo() {
//		int currentItem = country.getCurrentItem();
//		int currentItem2 = city.getCurrentItem();
//		if (currentItem==currentItem2) {
//			seltotText=currentItem+"";
//		}else{
//			seltotText= currentItem+"-"+currentItem2;
//		}
//		downGradeid = 	skGrade.getClasses().get(currentItem).getId();
//		upGradeid  = skGrade.getClasses().get(currentItem2).getId();
//		
//	}
//
//	public void setSystemDialogButtonOnclickListening(
//			SystemDialogButtonOnclickListening listening) {
//		this.listening = listening;
//	}
//
//	public void settitle(String title) {
//		alertDialog.setTitle(title);
//	}
//
//	public void setMessage(String message) {
//		alertDialog.setMessage(message);
//	}
//
//	public void setLeftButtonText(String text) {
//		alertDialog.setPositiveButton(text, this);
//	}
//
//	public void setRightButtonText(String text) {
//		alertDialog.setNegativeButton(text, this);
//	}
//
//	public void show() {
//		alertDialog.show();
//	}
//
//	@Override
//	public void onClick(DialogInterface dialog, int which) {
//
//		switch (which) {
//		case DialogInterface.BUTTON_POSITIVE:
//			if (listening != null) {
//				listening.onClickLeft();
//			}
//			break;
//		case DialogInterface.BUTTON_NEGATIVE:
//			if (listening != null) {
//				listening.onClickRight();
//			}
//			break;
//		default:
//
//			break;
//		}
//	}
//
//	public interface SystemDialogButtonOnclickListening {
//		public void onClickLeft();
//
//		public void onClickRight();
//	}
//
//	/**
//	 * Updates the city wheel
//	 */
//	@SuppressWarnings("unused")
//	private void updateCities(WheelView city, ArrayList<SKGrade> sKGrades,
//			int index) {
//		ArrayWheelAdapter adapter = new ArrayWheelAdapter(context, sKGrades
//				.get(index).getClasses());
//		adapter.setTextSize(18);
//		city.setViewAdapter(adapter);
//		city.setCurrentItem(sKGrades.get(index).getClasses().size() / 2);
//
//	}
//
//	/**
//	 * Adapter for countries
//	 */
//	private class CountryAdapter extends AbstractWheelTextAdapter {
//		private ArrayList<SkClasses> sKGrades;
//
//		/**
//		 * Constructor
//		 */
//		protected CountryAdapter(Context context, ArrayList<SkClasses> sKGrades) {
//			super(context, R.layout.country_layout, NO_RESOURCE);
//			this.sKGrades = sKGrades;
//			setItemTextResource(R.id.country_name);
//		}
//
//		@Override
//		public View getItem(int index, View cachedView, ViewGroup parent) {
//			View view = super.getItem(index, cachedView, parent);
//			return view;
//		}
//
//		@Override
//		public int getItemsCount() {
//			return sKGrades.size();
//		}
//
//		@Override
//		protected CharSequence getItemText(int index) {
//			return sKGrades.get(index).getName();
//		}
//	}
//
//	public class ArrayWheelAdapter extends AbstractWheelTextAdapter {
//
//		private ArrayList<SkClasses> items;
//
//		public ArrayWheelAdapter(Context context, ArrayList<SkClasses> items) {
//			super(context);
//
//			this.items = items;
//		}
//
//		@Override
//		public CharSequence getItemText(int index) {
//			if (index >= 0 && index < items.size()) {
//				String item = items.get(index).getName();
//				if (item instanceof CharSequence) {
//					return (CharSequence) item;
//				}
//				return item.toString();
//			}
//			return null;
//		}
//
//		@Override
//		public int getItemsCount() {
//			return items.size();
//		}
//	}
//
//	public String getGradeId() {
//		return gradeId;
//	}
//
//	public void setGradeId(String gradeId) {
//		this.gradeId = gradeId;
//	}
//
//	public String getSeltotText() {
//		return seltotText;
//	}
//
//	public void setSeltotText(String seltotText) {
//		this.seltotText = seltotText;
//	}
//
//	public String getDownGradeid() {
//		return downGradeid;
//	}
//
//
//	public String getUpGradeid() {
//		return upGradeid;
//	}
//
//	
//}
