package com.yshow.shike.activities;
import com.umeng.analytics.MobclickAgent;
import com.yshow.shike.R;
import com.yshow.shike.entity.SKMessage;
import com.yshow.shike.fragments.Fragment_Message;
import com.yshow.shike.service.MySKService;
import com.yshow.shike.utils.Dialog;
import com.yshow.shike.utils.MyAsyncHttpResponseHandler;
import com.yshow.shike.utils.SKAsyncApiController;
import com.yshow.shike.utils.SKResolveJsonUtil;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

/**
 * 投诉老师的页面
 */
public class Activity_Compain_Teather extends Activity implements OnClickListener{
	private EditText et_compain;
	Context context;
     @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.compain_teather);
        context = this;
        initData();
    }
	private void initData() {
        findViewById(R.id.tv_compain_back).setOnClickListener(this);
        findViewById(R.id.tv_compain_cmf).setOnClickListener(this);	
        et_compain = (EditText) findViewById(R.id.et_compain);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_compain_back:
            Fragment_Message.handler.sendEmptyMessage(MySKService.HAVE_NEW_MESSAGE);
            Dialog.Intent(Activity_Compain_Teather.this, Student_Main_Activity.class);
			break;
		case R.id.tv_compain_cmf:
			String compain_content = et_compain.getText().toString().trim();
//			if(!compain_content.equals("")){
				SKMessage message = (SKMessage) getIntent().getExtras().getSerializable("message");
				String claim_uid = message.getClaim_uid();
				String questionId = message.getQuestionId();
				Conpain_Teather(questionId, claim_uid,compain_content);
			/*}else {
				Toast.makeText(context, "请输入投诉内容", 0).show();
			}*/
			break;
		}
	}
	// 投诉老师
	private  void Conpain_Teather(String questionId,String claim_uid,String points) {
		SKAsyncApiController.Complain_Teather(questionId, claim_uid, points,new MyAsyncHttpResponseHandler(this,true){
			@Override
			public void onSuccess(int arg0, String json) {
				super.onSuccess(arg0, json);
				boolean success = SKResolveJsonUtil.getInstance().resolveIsSuccess(json, context);
				if(success){
                    Fragment_Message.handler.sendEmptyMessage(MySKService.HAVE_NEW_MESSAGE);
                    Dialog.Intent(Activity_Compain_Teather.this, Student_Main_Activity.class);
				}
			}
		});
	}

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}