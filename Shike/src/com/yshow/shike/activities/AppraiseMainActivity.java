package com.yshow.shike.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.yshow.shike.R;

/**
 * Created by Administrator on 2014-10-25.
 */
public class AppraiseMainActivity extends BaseActivity implements View.OnClickListener {
    private final int GIVE_PRAISE = 2001;
    private final int GIVE_GIFT = 2002;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appraise_main_layout);
        findViewById(R.id.tv_tool_back).setOnClickListener(this);
        findViewById(R.id.next_btn).setOnClickListener(this);
        findViewById(R.id.give_gift_btn).setOnClickListener(this);
        findViewById(R.id.give_praise_btn).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_tool_back:
                finish();
                break;
            case R.id.next_btn:
                startPraise();
                break;
            case R.id.give_gift_btn:
                startActivityForResult(new Intent(AppraiseMainActivity.this,GivePraiseActivity.class),GIVE_GIFT);
                break;
            case R.id.give_praise_btn:
                startActivityForResult(new Intent(AppraiseMainActivity.this,GiveGiftActivity.class),GIVE_GIFT);
                break;
        }

    }

    private void startPraise() {

    }
}
