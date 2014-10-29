package com.yshow.shike.utils;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;

import com.yshow.shike.R;
import com.yshow.shike.entity.SKArea;
import com.yshow.shike.entity.SKTeacherOrSubject;

import java.util.ArrayList;

import kankan.wheel.widget.OnWheelScrollListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.AbstractWheelTextAdapter;

public class XuekeSelectUtil implements DialogInterface.OnClickListener {
    @SuppressWarnings("unused")
    private Context context;
    private Builder alertDialog;
    private AreaSeltorUtilButtonOnclickListening listening;
    private ArrayList<SKTeacherOrSubject> sKArea = null;
    private WheelView area;
    private String areaId = "";
    private String seltotText = "请选择";
    @SuppressWarnings("unused")
    private boolean gradeScrolled;
    @SuppressWarnings("unused")
    private boolean gradeChanged;

    public XuekeSelectUtil(Context context, final ArrayList<SKTeacherOrSubject> sKArea) {
        this.sKArea = sKArea;
        this.context = context;
        alertDialog = new Builder(context);
        View inflate = View.inflate(context, R.layout.area_seltor_layout, null);
        alertDialog.setView(inflate);
        area = (WheelView) inflate.findViewById(R.id.country);
        area.setVisibleItems(5);
        area.setViewAdapter(new CountryAdapter(context, sKArea));
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
        SKTeacherOrSubject skArea2 = sKArea.get(currentItem);
        seltotText = skArea2.getName();
        areaId = skArea2.getSubjectId();
    }

    public void setAreaSeltorUtilButtonOnclickListening(
            AreaSeltorUtilButtonOnclickListening listening) {
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

    public interface AreaSeltorUtilButtonOnclickListening {
        public void onClickLeft();

        public void onClickRight();
    }

    private class CountryAdapter extends AbstractWheelTextAdapter {
        private ArrayList<SKTeacherOrSubject> sKArea;

        protected CountryAdapter(Context context, ArrayList<SKTeacherOrSubject> sKArea) {
            super(context, R.layout.country_layout, NO_RESOURCE);
            this.sKArea = sKArea;
            setItemTextResource(R.id.country_name);
        }

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            View view = super.getItem(index, cachedView, parent);
            return view;
        }

        @Override
        public int getItemsCount() {
            return sKArea.size();
        }

        @Override
        protected CharSequence getItemText(int index) {
            return sKArea.get(index).getName();
        }
    }

    public String getGradeId() {
        return areaId;
    }

    public String getSeltotText() {
        return seltotText;
    }

}
