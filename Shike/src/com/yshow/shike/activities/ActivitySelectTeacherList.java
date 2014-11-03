package com.yshow.shike.activities;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yshow.shike.R;
import com.yshow.shike.entity.SKTeacherOrSubject;
import com.yshow.shike.utils.ImageLoadOption;
import com.yshow.shike.utils.MyAsyncHttpResponseHandler;
import com.yshow.shike.utils.SKAsyncApiController;
import com.yshow.shike.utils.SKResolveJsonUtil;

public class ActivitySelectTeacherList extends BaseActivity implements
        OnClickListener {
    private ListView mListview;
    private TeacherAdapter teaAdapter;
    private SKTeacherOrSubject item;
    private ImageLoader imgLoader;
    private DisplayImageOptions option;
    private DisplayImageOptions grayOption;
    ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_list_layout);
        findViewById(R.id.tv_tool_back).setOnClickListener(this);
        mListview = (ListView) findViewById(R.id.listview);
        mListview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                item = teaAdapter.getItem(position);
                Intent intent = getIntent();
                intent.putExtra("selectTeacher", item);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
        imgLoader = ImageLoader.getInstance();
        skGetTeacher(getIntent().getStringExtra("subjectid"));


        option = ImageLoadOption.getTeaHeadImageOption();
        grayOption = ImageLoadOption.getTeaHeadGrayImageOption();
    }

    private void skGetTeacher(final String subjectId) {
        SKAsyncApiController.skGetMyTeacher(new MyAsyncHttpResponseHandler(
                this, true) {
            @Override
            public void onSuccess(String arg0) {
                super.onSuccess(arg0);
                ArrayList<SKTeacherOrSubject> subjects = SKResolveJsonUtil.getInstance().resolveTeacher(arg0, subjectId);
                teaAdapter = new TeacherAdapter(subjects, ActivitySelectTeacherList.this);
                mListview.setAdapter(teaAdapter);
                if (subjects.size() == 0) {
                    AlertDialog.Builder dialog = new Builder(ActivitySelectTeacherList.this);
                    dialog.setTitle("提示");
                    dialog.setMessage("当前没有该科目的老师");
                    dialog.setPositiveButton("确定",
                            new AlertDialog.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            });
                    dialog.show();
                }
            }

        });
    }

    class TeacherAdapter extends BaseAdapter {
        ArrayList<SKTeacherOrSubject> subjects;
        Context context;

        protected TeacherAdapter(ArrayList<SKTeacherOrSubject> subjects,
                                 Context context) {
            this.context = context;
            this.subjects = subjects;
        }

        @Override
        public int getCount() {
            return subjects.size();
        }

        @Override
        public SKTeacherOrSubject getItem(int position) {
            return subjects.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            SKTeacherOrSubject skSubject = subjects.get(position);
            if (convertView == null) {
                convertView = View.inflate(context, R.layout.sh_select_subject_itme, null);
            }
            TextView subject_text = (TextView) convertView.findViewById(R.id.subject_text);
            subject_text.setText(skSubject.subject);
            TextView teacherName = (TextView) convertView.findViewById(R.id.teacher_name);
            teacherName.setText(skSubject.getName());
            ImageView teacherImage = (ImageView) convertView.findViewById(R.id.teacher_img);
            TextView isonline = (TextView) convertView.findViewById(R.id.tv_isonline);
            if (!skSubject.isOnline) {
                isonline.setText("离线");
                imgLoader.displayImage(skSubject.icon, teacherImage, grayOption);
            } else {
                isonline.setText("在线");
                imgLoader.displayImage(skSubject.icon, teacherImage, option);
            }
            return convertView;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_tool_back:
                finish();
                break;
        }
    }

}
