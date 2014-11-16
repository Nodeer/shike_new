package com.yshow.shike.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yshow.shike.R;
import com.yshow.shike.entity.Fase_1;
import com.yshow.shike.entity.Fase_Packs;
import com.yshow.shike.utils.ImageLoadOption;
import com.yshow.shike.utils.MyAsyncHttpResponseHandler;
import com.yshow.shike.utils.SKAsyncApiController;
import com.yshow.shike.utils.SKResolveJsonUtil;

import java.util.ArrayList;

/**
 * Created by Administrator on 2014-10-25.
 */
public class GivePraiseActivity extends BaseActivity implements View.OnClickListener {
    private GridView mGridView;
    private ImageLoader mImageloader;
    private DisplayImageOptions mImageOption;
    private int mSelectedIndex = -1;
    private MyAdapter mAdapter;
    private ArrayList<Fase_Packs> arrayList = new ArrayList<Fase_Packs>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.give_appraise_layout);
        findViewById(R.id.tv_tool_back).setOnClickListener(this);
        mGridView = (GridView) findViewById(R.id.gridview);
        mImageloader = ImageLoader.getInstance();
        mImageOption = ImageLoadOption.getImageOption(R.drawable.xiaoxi_moren);
        getPraiseImgs();
        mAdapter = new MyAdapter();
        mGridView.setAdapter(mAdapter);
//        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (position == mSelectedIndex) {
//                    mSelectedIndex = -1;
//                } else {
//                    mSelectedIndex = position;
//                }
//                Intent it = new Intent();
//                it.putExtra("index", mSelectedIndex);
//                if (mSelectedIndex != -1) {
//                    it.putExtra("data", arrayList.get(mSelectedIndex));
//                }
//                setResult(Activity.RESULT_OK, it);
//                finish();
//            }
//        });
    }

    /**
     * 获取给老师赞美的表情包
     */
    private void getPraiseImgs() {
        SKAsyncApiController.Think_Teather(new MyAsyncHttpResponseHandler(this, true) {
            @Override
            public void onSuccess(int arg0, String arg1) {
                super.onSuccess(arg0, arg1);
                Fase_1 face_Pack = SKResolveJsonUtil.getInstance().Face_Package_Pase(arg1);
                arrayList = face_Pack.getRes();
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_tool_back:
                Intent it = new Intent();
                it.putExtra("index", mSelectedIndex);
                if (mSelectedIndex != -1) {
                    it.putExtra("data", arrayList.get(mSelectedIndex));
                }
                setResult(Activity.RESULT_OK, it);
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Intent it = new Intent();
        it.putExtra("index", mSelectedIndex);
        if (mSelectedIndex != -1) {
            it.putExtra("data", arrayList.get(mSelectedIndex));
        }
        setResult(Activity.RESULT_OK, it);
        finish();
    }

    /**
     * 展示表情包的Adapter
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
        public View getView(final int arg0, View arg1, ViewGroup arg2) {
            View view = View.inflate(GivePraiseActivity.this, R.layout.praise_item_view, null);
            WebView imageView = (WebView) view.findViewById(R.id.web_image);
            ImageView select_icon = (ImageView) view.findViewById(R.id.select_icon);

            imageView.getSettings().setLoadWithOverviewMode(true);
            imageView.getSettings().setUseWideViewPort(true);
            imageView.loadUrl(arrayList.get(arg0).getFase_url());

            if (arg0 == mSelectedIndex) {
                select_icon.setVisibility(View.VISIBLE);
            } else {
                select_icon.setVisibility(View.GONE);
            }


            View clickView = view.findViewById(R.id.click_view);
            clickView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent it = new Intent();
                    it.putExtra("index", arg0);
                    it.putExtra("data", arrayList.get(arg0));
                    setResult(Activity.RESULT_OK, it);
                    finish();
                }
            });
            return view;
        }

    }
}


