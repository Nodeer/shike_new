package com.yshow.shike.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yshow.shike.R;
import com.yshow.shike.utils.MyAsyncHttpResponseHandler;
import com.yshow.shike.utils.SKAsyncApiController;

/**
 * Created by Administrator on 2014-10-30.
 */
public class FankuiActivity extends BaseActivity {
    private EditText fankuiEdit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fankui_layout);

        TextView titletext = (TextView) findViewById(R.id.title_text);
        titletext.setText("问题反馈");

        fankuiEdit = (EditText) findViewById(R.id.fankui_edit);
        findViewById(R.id.confirm_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = fankuiEdit.getText().toString().trim();
                if (TextUtils.isEmpty(content)) {
                    Toast.makeText(FankuiActivity.this, "请输入内容", Toast.LENGTH_SHORT).show();
                } else {
                    Feed_Back_Question(content);
                }
            }
        });
        findViewById(R.id.left_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    // 意见反馈
    private void Feed_Back_Question(String contents) {
        SKAsyncApiController.Feed_Back(contents, new MyAsyncHttpResponseHandler(FankuiActivity.this, true) {
            @Override
            public void onSuccess(String json) {
                super.onSuccess(json);
                Toast.makeText(FankuiActivity.this, "你的问题已接受", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
