package com.yshow.shike.utils;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.yshow.shike.R;
public class ProgressDialogUtil {
	private Context context;
	private Dialog dialog;
	private TextView tv_loading;
	public ProgressDialogUtil(Context context) {
		super();
		this.context = context;
		View view = LayoutInflater.from(context).inflate(R.layout.sk_progress_layout, null);
		tv_loading = (TextView) view.findViewById(R.id.tv_loading);
		dialog = new Dialog(context, R.style.AsyncTaskDialog);
		dialog.setContentView(view);
		dialog.setCanceledOnTouchOutside(false);
	}
	public ProgressDialogUtil(Context context, String loadText) {
		super();
		this.context = context;
		View view = LayoutInflater.from(context).inflate(R.layout.sk_progress_layout, null);
		tv_loading = (TextView) view.findViewById(R.id.tv_loading);
		tv_loading.setText(loadText);
		dialog = new Dialog(context, R.style.AsyncTaskDialog);
		dialog.setContentView(view);
	}
   
	public void show() {
		if (dialog != null && !dialog.isShowing()) {
			try {
				dialog.show();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	public void dismiss() {
		if (dialog != null && dialog.isShowing()) {
			try {
				dialog.dismiss();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

    public void setOndismissListener(DialogInterface.OnDismissListener listener){
        dialog.setOnDismissListener(listener);
    }
}
