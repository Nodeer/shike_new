package com.yshow.shike.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yshow.shike.R;
import com.yshow.shike.entity.Send_Gife;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.give_gift_layout);
        findViewById(R.id.tv_tool_back).setOnClickListener(this);
        findViewById(R.id.next_btn).setOnClickListener(this);
        mGridView = (GridView) findViewById(R.id.gridview);
        mImageloader = ImageLoader.getInstance();
        mImageOption = ImageLoadOption.getImageOption(R.drawable.xiaoxi_moren);
        getGiftImgs();
        mAdapter = new MyAdapter();
        mGridView.setAdapter(mAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mSelectedTags[position] = mSelectedTags[position] ? false : true;
                mAdapter.notifyDataSetChanged();
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
                    if(mTotalCount>100){
                        Toast.makeText(GiveGiftActivity.this, "送分过多,请不要超过100分", Toast.LENGTH_SHORT).show();
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
        public View getView(int arg0, View contentView, ViewGroup arg2) {
            View view = View.inflate(GiveGiftActivity.this, R.layout.give_gift_item, null);
            ImageView image = (ImageView) view.findViewById(R.id.image);
            ImageView select_img = (ImageView) view.findViewById(R.id.select_icon);
            TextView tv_send_fen = (TextView) view.findViewById(R.id.fenshu_text);
            mImageloader.displayImage(arrayList.get(arg0).getFaceuri(), image, mImageOption);
            tv_send_fen.setText(arrayList.get(arg0).getPoints() + "分");
            if (mSelectedTags[arg0]) {
                select_img.setVisibility(View.VISIBLE);
            } else {
                select_img.setVisibility(View.GONE);
            }
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
