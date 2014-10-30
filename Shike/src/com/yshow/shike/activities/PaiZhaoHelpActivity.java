package com.yshow.shike.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.yshow.shike.R;

/**
 * Created by Administrator on 2014-10-30.
 */
public class PaiZhaoHelpActivity extends BaseActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paizhao_zhuyi_layout);

        TextView titletext = (TextView) findViewById(R.id.title_text);
        titletext.setText("拍照注意事项");

        findViewById(R.id.left_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
