package com.yshow.shike.utils;
import java.util.Calendar;
import com.yshow.shike.R;
import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;
import kankan.wheel.widget.adapters.NumericWheelAdapter;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
/**
 * 日期选择工具类   +自定义Dialog
 * @author Administrator
 *
 */
public class DataSeleUtil implements DialogInterface.OnClickListener {
	private Context context;
	private Builder builder;
	private Data_Seltor_Listening listening;
	private WheelView month;
	private WheelView year;
	private WheelView day;
	private String months[];
	private int curDay; 
	private DateNumericAdapter year_Adapter; //年份Adapter
	private DateNumericAdapter day_Adapter;
	public DataSeleUtil(Context context) {
		super();
		this.context = context;
		InitData();
	}
	private void InitData() {
		builder = new AlertDialog.Builder(context);
		View view = View.inflate(context, R.layout.date_layout, null);
		Calendar calendar = Calendar.getInstance();
		month = (WheelView) view.findViewById(R.id.month);
		year = (WheelView) view.findViewById(R.id.year);
		day = (WheelView) view.findViewById(R.id.day);
		builder.setView(view);
		OnWheelChangedListener listener = new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				updateDays(year, month, day);
			}
		};
		// 每一个月
		int curMonth = calendar.get(Calendar.MONTH);
		months = new String[] { "1", "2", "3", "4", "5", "6", "7", "8","9", "10", "11", "12" };
		month.setViewAdapter(new DateArrayAdapter(context, months, curMonth));
		month.setCurrentItem(curMonth);
		month.addChangingListener(listener);
		// 年
		int curYear = calendar.get(Calendar.YEAR);
		year_Adapter = new DateNumericAdapter(context, curYear -65,curYear, 0);
		year.setViewAdapter(year_Adapter);
		year.setCurrentItem(curYear);
		year.addChangingListener(listener);
		// 每一个天
		updateDays(year, month, day);
		day.setCurrentItem(calendar.get(Calendar.DAY_OF_MONTH) - 1);
	}
	/**
	 * Updates day wheel. Sets max days according to selected month and year
	 */
	void updateDays(WheelView year, WheelView month, WheelView day) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR,calendar.get(Calendar.YEAR) + year.getCurrentItem());
		calendar.set(Calendar.MONTH, month.getCurrentItem());
		int maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		day_Adapter = new DateNumericAdapter(context, 1, maxDays, calendar.get(Calendar.DAY_OF_MONTH) - 1);
		day.setViewAdapter(day_Adapter);
		curDay = Math.min(maxDays, day.getCurrentItem() + 1);
		day.setCurrentItem(curDay - 1, true);
	}
	/**
	 * Adapter for numeric wheels. Highlights the current value.
	 */
	private class DateNumericAdapter extends NumericWheelAdapter {
		// Index of current item
		int day_currentItem;
		// Index of item to be highlighted
		int currentValue;
		/**
		 * Constructor
		 */
		public DateNumericAdapter(Context context, int minValue, int maxValue,
				int current) {
			super(context, minValue, maxValue);
			this.currentValue = current;
			setTextSize(16);
		}
		@Override
		protected void configureTextView(TextView view) {
			super.configureTextView(view);
			if (day_currentItem == currentValue) {
				view.setTextColor(0xFF0000F0);
			}
			view.setTypeface(Typeface.SANS_SERIF);
		}
		
		@Override
		public CharSequence getItemText(int index) {
			return super.getItemText(index);
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			day_currentItem = index;
			return super.getItem(index, cachedView, parent);
		}
	}
	
	public void setDtaListening(Data_Seltor_Listening listening) {
		this.listening = listening;
	}

	/**
	 * Adapter for string based wheel. Highlights the current value.
	 */
	private class DateArrayAdapter extends ArrayWheelAdapter<String> {
		int currentValue;
		int month_currentItem; // 月份的一个当前位置

		/**
		 * Constructor
		 */
		public DateArrayAdapter(Context context, String[] items, int current) {
			super(context, items);
			this.currentValue = current;
			setTextSize(16);
		}

		@Override
		protected void configureTextView(TextView view) {
			super.configureTextView(view);
			if (month_currentItem == currentValue) {
				view.setTextColor(0xFF0000F0);
			}
			view.setTypeface(Typeface.SANS_SERIF);
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			month_currentItem = index;
			return super.getItem(index, cachedView, parent);
		}
	}

	public void setLeftButtonText(String text) {
		builder.setPositiveButton(text, this);
	}

	public void show() {
		builder.show();
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		switch (which) {
		case DialogInterface.BUTTON_POSITIVE:
			if (listening != null) {
				// 得到 月份
				int currentItem = month.getCurrentItem();
				String month = months[currentItem];
				// 得到 年份
				int year_item = year.getCurrentItem();
				String years = (String) year_Adapter.getItemText(year_item);
				// 得到天份
				int day_point = day.getCurrentItem();
				String day_shu = (String) day_Adapter.getItemText(day_point);
				listening.onClickLeft( years,month,day_shu);
			}
			break;
		}
	}

	public interface Data_Seltor_Listening {
		public void onClickLeft(String dates,String day_item,String years);
	}
}
