package com.yshow.shike.utils;

import com.yshow.shike.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
/**
 * 自定义dialog
 *
 */
public class MyCustomDialog extends Dialog {
	private OnCustomDialogListener customDialogListener;
	private Button iv_yes;
	private Button iv_no;
	EditText etName;
	//定义回调事件，用于dialog的点击事件
	public interface OnCustomDialogListener{
		public void back(String name);
	}
	public MyCustomDialog(Context context,OnCustomDialogListener customDialogListener) {
		super(context);
		this.customDialogListener = customDialogListener;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);        
		setContentView(R.layout.dialog_item);
		etName = (EditText)findViewById(R.id.et_file_name);
		iv_yes = (Button) findViewById(R.id.iv_yes);
		iv_no = (Button) findViewById(R.id.iv_no);
		iv_yes.setOnClickListener(clickListener);
		iv_no.setOnClickListener(clickListener);
	}
	private View.OnClickListener clickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.iv_yes:
				customDialogListener.back(String.valueOf(etName.getText()));
				MyCustomDialog.this.dismiss();
				break;
			case R.id.iv_no:
				MyCustomDialog.this.dismiss();
				break;
			}
		}
	};
   }