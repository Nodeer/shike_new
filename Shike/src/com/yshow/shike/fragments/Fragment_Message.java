package com.yshow.shike.fragments;

import java.util.ArrayList;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.yshow.shike.R;
import com.yshow.shike.activities.Activity_Stu_Ask_Step1;
import com.yshow.shike.activities.Login_Reg_Activity;
import com.yshow.shike.activities.StartingUp;
import com.yshow.shike.activities.Student_Main_Activity;
import com.yshow.shike.activities.Teather_Main_Activity;
import com.yshow.shike.adapter.SKMessageAdapter;
import com.yshow.shike.entity.LoginManage;
import com.yshow.shike.entity.SKMessageList;
import com.yshow.shike.service.MySKService;
import com.yshow.shike.utils.MyAsyncHttpResponseHandler;
import com.yshow.shike.utils.PartnerConfig;
import com.yshow.shike.utils.SKAsyncApiController;
import com.yshow.shike.utils.SKResolveJsonUtil;
import com.yshow.shike.utils.Timer_Uils;
import com.yshow.shike.widget.XListView;

/**
 * 学生的消息列表
 */
public class Fragment_Message extends Fragment implements View.OnClickListener, XListView.IXListViewListener {
    private View view = null;
    private static XListView mListview;
    private static Context context;
    private static int page = 1;
    private static ArrayList<SKMessageList> resolveMessage;
    private static SKMessageAdapter skMessageAdapter;
    /**
     * 单例对象实例
     */
    private static Fragment_Message instance = null;

    public synchronized static Fragment_Message getInstance() {
        instance = new Fragment_Message();
        return instance;
    }

    public static Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case MySKService.HAVE_NEW_MESSAGE:
                    getSKMessage();
                    break;
                case 5168:
                    if (mListview != null) {
                        SKMessageAdapter adapter = (SKMessageAdapter) mListview.getAdapter();
                        if (adapter != null) {
                            adapter.Dete_Mess();
                        }
                    }
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = inflater.getContext();
        if (LoginManage.getInstance().getStudent() == null) {
            startActivity(new Intent(getActivity(), Login_Reg_Activity.class));
            getActivity().finish();
            return super.onCreateView(inflater, container, savedInstanceState);
        }
        view = inflater.inflate(R.layout.fragment_message, null);
        initView();
        return view;
    }

    private void initView() {
        mListview = (XListView) view.findViewById(R.id.listview);
        mListview.setXListViewListener(this);
        ImageView backBtn = (ImageView) view.findViewById(R.id.tv_tool_back);
        backBtn.setOnClickListener(this);

        if (LoginManage.getInstance().isTeacher()) {
            view.findViewById(R.id.right_button).setVisibility(View.GONE);
        } else {
            view.findViewById(R.id.right_button).setOnClickListener(this);
        }
        // getSKMessage();
    }

    @Override
    public void onResume() {
        super.onResume();
        getSKMessage();
        MySKService.handler = handler;
    }

    @Override
    public void onPause() {
        super.onPause();
        MySKService.handler = null;
    }

    private static void getSKMessage() {
        page = 1;
        SKAsyncApiController.skGetMessage(page, new MyAsyncHttpResponseHandler(context, true) {
            @Override
            public void onSuccess(String json) {
                boolean isSuccess = SKResolveJsonUtil.getInstance().resolveIsSuccess(json, context);
                if (isSuccess) {
                    resolveMessage = SKResolveJsonUtil.getInstance().resolveMessage(json);
                    skMessageAdapter = new SKMessageAdapter(context, resolveMessage);
                    mListview.setAdapter(skMessageAdapter);
                    if (resolveMessage.size() > 0) {
                        mListview.setPullLoadEnable(true);
                    } else {
                        mListview.setPullLoadEnable(false);
                    }
                    onLoad();
                }
            }
        });
    }

    private void getModeSKMessage() {
        SKAsyncApiController.skGetMessage(page, new MyAsyncHttpResponseHandler(context, true) {
            @Override
            public void onSuccess(String json) {
                super.onSuccess(json);
                boolean isSuccess = SKResolveJsonUtil.getInstance().resolveIsSuccess(json, getActivity());
                if (isSuccess) {
                    ArrayList<SKMessageList> more = SKResolveJsonUtil.getInstance().resolveMessage(json);
                    if (more.size() > 0) {
                        skMessageAdapter.addMordList(more);
                        mListview.setPullLoadEnable(true);
                    } else {
                        mListview.setPullLoadEnable(false);
                    }
                    onLoad();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_tool_back:
                getActivity().finish();
                break;
            case R.id.right_button:
                PartnerConfig.TEATHER_ID = null;
                PartnerConfig.SUBJECT_ID = null;
                PartnerConfig.TEATHER_NAME = null;
                PartnerConfig.SUBJECT_NAME = null;
                PartnerConfig.TEACHER_IMG = null;
                startActivity(new Intent(getActivity(), Activity_Stu_Ask_Step1.class));
                break;
        }
    }

    @Override
    public void onRefresh() {
        page = 1;
        getSKMessage();
    }

    @Override
    public void onLoadMore() {
        page++;
        getModeSKMessage();
    }

    private static void onLoad() {
        mListview.stopRefresh();
        mListview.stopLoadMore();
        mListview.setRefreshTime(Timer_Uils.getCurrentTime());
    }
}