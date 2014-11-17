package com.yshow.shike.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yshow.shike.R;
import com.yshow.shike.entity.LoginManage;
import com.yshow.shike.entity.Send_Gife;
import com.yshow.shike.entity.User_Info;
import com.yshow.shike.utils.ImageLoadOption;
import com.yshow.shike.utils.MyAsyncHttpResponseHandler;
import com.yshow.shike.utils.SKAsyncApiController;
import com.yshow.shike.utils.SKResolveJsonUtil;

import java.util.ArrayList;

/**
 * Created by Administrator on 2014-10-25.
 */
public class GiveGiftActivity extends BaseActivity implements View.OnClickListener {
    private GridView mGridView;
    private ImageLoader mImageloader;
    private DisplayImageOptions mImageOption;
    private MyAdapter mAdapter;
    private ArrayList<Send_Gife> arrayList = new ArrayList<Send_Gife>();

    private StringBuilder mSelcetFileIds = new StringBuilder();
    private int mTotalCount;

    private boolean[] mSelectedTags;

    private String url;

    private int userTotalPoint = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.give_gift_layout);
        findViewById(R.id.tv_tool_back).setOnClickListener(this);
        findViewById(R.id.next_btn).setOnClickListener(this);
        mGridView = (GridView) findViewById(R.id.gridview);
        mImageloader = ImageLoader.getInstance();
        mImageOption = ImageLoadOption.getImageOption(R.drawable.xiaoxi_moren);
        update_info(LoginManage.getInstance().getStudent().getUid());
        getGiftImgs();
        mAdapter = new MyAdapter();
        mGridView.setAdapter(mAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mSelectedTags[position] = mSelectedTags[position] ? false : true;

                ImageView select_img = (ImageView) view.findViewById(R.id.select_icon);
                select_img.setVisibility(mSelectedTags[position] ? View.VISIBLE : View.GONE);

//                mAdapter.notifyDataSetChanged();
            }
        });
    }

    private void update_info(String uid) {
        SKAsyncApiController.User_Info(uid, new MyAsyncHttpResponseHandler(this, false) {
            public void onSuccess(int arg0, String json) {
                super.onSuccess(arg0, json);
                boolean atent_Success = SKResolveJsonUtil.getInstance().resolveIsSuccess(json, GiveGiftActivity.this);
                if (atent_Success) {
                    User_Info info = SKResolveJsonUtil.getInstance().My_teather1(json);
                    userTotalPoint = Integer.parseInt(info.getPoints());
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_tool_back:
                finish();
                break;
            case R.id.next_btn:
                Intent it = new Intent();
                if (arrayList.size() > 0) {
                    calIdAndCounts();
                    if (userTotalPoint != -1 && mTotalCount > userTotalPoint) {
                        Toast.makeText(GiveGiftActivity.this, "送分过多,请不要超过现有学分", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    it.putExtra("count", mTotalCount);
                    it.putExtra("data", mSelcetFileIds.toString());
                    it.putExtra("url", url);
                    setResult(Activity.RESULT_OK, it);
                    finish();
                } else {
                    it.putExtra("count", mTotalCount);
                    setResult(Activity.RESULT_OK, it);
                    finish();
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void calIdAndCounts() {
        mTotalCount = 0;
        mSelcetFileIds = new StringBuilder();
        int lastFileid = -1;
        if (arrayList.size() > 0) {
            int size = arrayList.size();
            for (int i = 0; i < size; i++) {
                Send_Gife item = arrayList.get(i);
                if (mSelectedTags[i]) {
                    mTotalCount += Integer.parseInt(item.getPoints());
                    mSelcetFileIds.append(item.getFileId() + ",");
                    lastFileid = i;
                }
            }
            if (mSelcetFileIds.length() != 0) {
                mSelcetFileIds.deleteCharAt(mSelcetFileIds.length() - 1);
                url = arrayList.get(lastFileid).getFaceuri();
            }
        }
    }


    /**
     * 感谢老师送积分
     *
     * @author Administrator
     */
    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int arg0) {
            return arrayList.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        @Override
        public View getView(final int arg0, View contentView, ViewGroup arg2) {
            View view = View.inflate(GiveGiftActivity.this, R.layout.give_gift_item, null);
            WebView image = (WebView) view.findViewById(R.id.web_image);
            final ImageView select_img = (ImageView) view.findViewById(R.id.select_icon);
            TextView tv_send_fen = (TextView) view.findViewById(R.id.fenshu_text);

            final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressbar);

            image.setWebViewClient(new WebViewClient(){
                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                    progressBar.setVisibility(View.VISIBLE);
                }
            });

            image.getSettings().setLoadWithOverviewMode(true);
            image.getSettings().setUseWideViewPort(true);
            image.loadUrl(arrayList.get(arg0).getFaceuri());

            tv_send_fen.setText(arrayList.get(arg0).getPoints() + "分");
            if (mSelectedTags[arg0]) {
                select_img.setVisibility(View.VISIBLE);
            } else {
                select_img.setVisibility(View.GONE);
            }


            View clickView = view.findViewById(R.id.click_view);
            clickView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSelectedTags[arg0] = mSelectedTags[arg0] ? false : true;
                    select_img.setVisibility(mSelectedTags[arg0] ? View.VISIBLE : View.GONE);
//                    mAdapter.notifyDataSetChanged();
                }
            });
            return view;
        }
    }

    /**
     * 获取给老师送积分的Adapter
     */
    private void getGiftImgs() {
        SKAsyncApiController.Gain_Gif(new MyAsyncHttpResponseHandler(this, true) {
            public void onSuccess(int arg0, String json) {
                arrayList = SKResolveJsonUtil.getInstance().Huo_Gif(json);
                mSelectedTags = new boolean[arrayList.size()];
                mAdapter.notifyDataSetChanged();
            }
        });
    }
}
