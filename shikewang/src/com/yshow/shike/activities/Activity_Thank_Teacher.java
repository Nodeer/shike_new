package com.yshow.shike.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.umeng.analytics.MobclickAgent;
import com.yshow.shike.R;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yshow.shike.entity.Fase_1;
import com.yshow.shike.entity.Fase_Packs;
import com.yshow.shike.entity.SKMessage;
import com.yshow.shike.entity.Send_Gife;
import com.yshow.shike.fragments.Fragment_Message;
import com.yshow.shike.service.MySKService;
import com.yshow.shike.utils.*;

/**
 * 感谢老师页面
 *
 * @param <T>
 */
public class Activity_Thank_Teacher<T> extends Activity implements OnClickListener, DialogInterface.OnDismissListener {
    private List<View> viewlist = new ArrayList<View>();
    private GridView think_gridview, send_gridview; // 给老师发送赞数图标的    给老师发积分的
    private ImageLoader imageLoader;
    private DisplayImageOptions options;
    private int isSelectGif = -1;// 送给老师的赞数
    private Context context;
    private SKMessage message;
    //	private ImageView iv_bac_picture;
    private Bundle bundle;
    private int count = 0; // 送给老师的师课分
    private Integer extra_record;//记录点击过的分数
    private ArrayList<Send_Gife> gif;// 获取送给老师的积分
    private ArrayList<Fase_Packs> arrayList; //获取送给老师表情的集合
    private String zan_fileId, fileId;//赞美老师fileid,给老师积分的Fileid,学生问题的id
    private ArrayList<String> Jifen_file_id = new ArrayList<String>();
    private String questionId;
    //    private String teacher_id;
    /**
     * 是否是我的老师
     */
    private boolean isMyTeacher = false;
    private String useTeacherId;
    private ProgressDialogUtil progress;
    private int dialogInt = 0;
    private String title = "他/她还不是您的老师,现在是否添加他/她为您的老师？";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.thank_teacher);
        context = this;
        initData();
        options = Net_Servse.getInstence().Picture_Shipei(R.drawable.student_picture);
        imageLoader = ImageLoader.getInstance();
        Face_Package();
        Send_LiWu();
    }

    private void initData() {
        message = (SKMessage) getIntent().getExtras().getSerializable("message");
        if (message != null) {
            String claim_uid = message.getClaim_uid();
            questionId = message.getQuestionId();
            String teacher_id = message.getTeachUid();
            String s = message.getiSmyteach();
            if (s.equals("1")) {
                isMyTeacher = true;
                useTeacherId = claim_uid;
            } else {
                useTeacherId = claim_uid;
            }
        }
        findViewById(R.id.tv_give_up).setOnClickListener(this);
        findViewById(R.id.tv_think_confg).setOnClickListener(this);
        think_gridview = (GridView) findViewById(R.id.think_gridview);
        send_gridview = (GridView) findViewById(R.id.send_gridview);
        Jifen_file_id = new ArrayList<String>();
        send_gridview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3) {
                ImageView iv_bac_picture = (ImageView) view.findViewById(R.id.iv_bac_picture);
                TextView jifen_view = (TextView) view.findViewById(R.id.tv_send_fen);
//				ViewGroup view = (ViewGroup) send_gridview.getChildAt(arg2);
//				TextView  view2 = (TextView) view.getChildAt(1);
                String text = (String) jifen_view.getText();//xxx分
                String subtext = text.substring(0, text.length() - 1);
                extra_record = Integer.valueOf(subtext);
                if (iv_bac_picture.getVisibility() == View.VISIBLE) {
                    count -= extra_record;
                    iv_bac_picture.setVisibility(View.GONE);
                    Send_Gife send_Gife = gif.get(arg2);
                    fileId = send_Gife.getFileId();
                    Jifen_file_id.remove(fileId);
                } else {
                    count += extra_record;
                    iv_bac_picture.setVisibility(View.VISIBLE);
                    Send_Gife send_Gife = gif.get(arg2);
                    fileId = send_Gife.getFileId();
                    Jifen_file_id.add(fileId);
                }
            }
        });
        think_gridview.setOnItemClickListener(new OnItemClickListener() {
            private View view;

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                ImageView iv_img1 = (ImageView) arg1.findViewById(R.id.iv_img1);
                for (int i = 0; i < viewlist.size(); i++) {
                    view = viewlist.get(i);
                    if (iv_img1.equals(view)) {
                        view.setVisibility(View.VISIBLE);
                    } else {
                        isSelectGif = i;
                        view.setVisibility(View.GONE);
                        Fase_Packs face_Pack = arrayList.get(arg2);
                        zan_fileId = face_Pack.getFileId();
                    }
                }
            }
        });
        progress = new ProgressDialogUtil(this);
        progress.setOndismissListener(this);
    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.tv_give_up:
                IsGood_Bad();
                break;
            // 感谢老师或投诉老师
            case R.id.tv_think_confg:
                confing();
                break;
        }
    }

    /**
     * 点击放弃
     */
    private void IsGood_Bad() {
        Builder builder = new Builder(context);
        builder.setMessage("你是否满意这个老师");
        builder.setPositiveButton("满意",
                new android.content.DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        ThankTeatherWithOutJifen();
                    }
                });
        builder.setNegativeButton("不满意",
                new android.content.DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Complain_Teather();
                    }
                });
        builder.show();
    }


    /**
     * 点击确定
     */
    private void confing() {
        if (count > 900) {//大于100分
            Toast.makeText(Activity_Thank_Teacher.this, "送分过多", 0).show();
            // 发送 赞美
        } else if (count != 0) {//积分不为0
            thankConfirm();
        } else {//无积分,无赞
            Thank_Teather(questionId);
        }
    }

    /**
     * 送积分确认
     */
    private void thankConfirm() {
        Builder builder = new Builder(context);
        builder.setMessage("你确定要送" + count + "分給这位老师吗？");
        builder.setPositiveButton("确定",
                new android.content.DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        Thank_Teather(questionId);
                    }
                });
        builder.setNegativeButton("取消",
                new android.content.DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });
        builder.show();
    }

    /**
     * 获取给老师赞美的表情包
     */
    private void Face_Package() {
        SKAsyncApiController.Think_Teather(new MyAsyncHttpResponseHandler(this, true) {
            @Override
            public void onSuccess(int arg0, String arg1) {
                super.onSuccess(arg0, arg1);
                Fase_1 face_Pack = SKResolveJsonUtil.getInstance().Face_Package_Pase(arg1);
                arrayList = face_Pack.getRes();
                think_gridview.setAdapter(new MyAdapter(arrayList));
            }
        });
    }


    /**
     * 给老师送赞
     */
    private void Send_Fase_Tea(String zanId, final String questionId) {
        SKAsyncApiController.Send_Fase(zanId, questionId, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String json) {
                super.onSuccess(json);
                dialogInt--;
                if (dialogInt == 0) {
                    progress.dismiss();
                }
            }


            @Override
            public void onStart() {
                super.onStart();
                if (dialogInt != 0) {
                    progress.show();
                }
            }
        });
    }

    /**
     * 给老师送积分
     */
    private void Send_Jifen_Tea(String jifenId, final String questionId) {
        SKAsyncApiController.Think_Teather2(questionId, useTeacherId, jifenId, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String json) {
                super.onSuccess(json);
                dialogInt--;
                if (dialogInt == 0) {
                    progress.dismiss();
                }
            }

            @Override
            public void onStart() {
                super.onStart();
                if (dialogInt != 0) {
                    progress.show();
                }
            }
        });
    }

    // 投诉老师
    private void Complain_Teather() {
        Builder builder = new Builder(context);
        builder.setMessage("你是否要投诉这位老师");
        builder.setPositiveButton("是",
                new android.content.DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        bundle = new Bundle();
                        bundle.putSerializable("message", message);
                        Dialog.intent(context, Activity_Compain_Teather.class, bundle);
                        finish();
                    }
                });
        builder.setNegativeButton("否", new android.content.DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                Fragment_Message.handler.sendEmptyMessage(MySKService.HAVE_NEW_MESSAGE);
                finish();
            }
        });
        builder.show();
    }

    // 结束问题的最后流程,赞已经在前面发送过了.这里主要是标记问题已解决,同时如果有积分,也发过去
    public void Thank_Teather(final String questionId) {
        dialogInt = 0;
        dialogInt += Jifen_file_id.size();
        if (count != 0) {
            dialogInt++;
        }
        if (isSelectGif != -1) { //送老师赞的图片
            dialogInt++;
        }
        if (dialogInt != 0) {
            if (count != 0) {//发积分图片,送积分
                for (int i = 0; i < Jifen_file_id.size(); i++) {
                    String jifen_id = Jifen_file_id.get(i);
                    Send_Fase_Tea(jifen_id, questionId);
                }
                Send_Jifen_Tea(String.valueOf(count), questionId);
            }
            if (isSelectGif != -1) { //送老师赞的图片
                Send_Fase_Tea(zan_fileId, questionId);
            }
        } else {
            thankTeacher();
        }
    }

    // 送0分.不送赞
    public void ThankTeatherWithOutJifen() {
        thankTeacher();
    }

    private void addMyTeacher() {
        Builder builder = new Builder(context);
        builder.setMessage("他/她还不是您的老师,现在是否添加他/她为您的老师？");
        builder.setPositiveButton("确定",
                new android.content.DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        add_Teather(useTeacherId);
                    }
                });
        builder.setNegativeButton("取消",
                new android.content.DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Fragment_Message.handler.sendEmptyMessage(MySKService.HAVE_NEW_MESSAGE);
                        finish();
                    }
                });
        builder.show();
    }

    private void add_Teather(String uid) {
        SKAsyncApiController.Attention_Taeather_Parse(uid,
                new MyAsyncHttpResponseHandler(this, true) {
                    public void onSuccess(int arg0, String jion) {
                        super.onSuccess(arg0, jion);
                        Fragment_Message.handler.sendEmptyMessage(MySKService.HAVE_NEW_MESSAGE);
                        finish();
                    }
                });
    }


    /**
     * 获取给老师送积分的Adapter
     */
    private void Send_LiWu() {
        SKAsyncApiController.Gain_Gif(new MyAsyncHttpResponseHandler(this, true) {
            public void onSuccess(int arg0, String json) {
                gif = SKResolveJsonUtil.getInstance().Huo_Gif(json);
                send_gridview.setAdapter(new Mydapter(gif));
            }
        });
    }

    private void thankTeacher() {
        SKAsyncApiController.Think_Teather2(questionId, useTeacherId, "0", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String json) {
                super.onSuccess(json);
                boolean issuccess = SKResolveJsonUtil.getInstance().resolveIsSuccess(json);
                if (issuccess) {
                    if (!isMyTeacher) {
                        addMyTeacher();
                    } else {
                        Fragment_Message.handler.sendEmptyMessage(MySKService.HAVE_NEW_MESSAGE);
                        finish();
                    }
                }
            }
        });
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if (Jifen_file_id.size() != 0) {
            if (!isMyTeacher) {
                addMyTeacher();
            } else {
                Fragment_Message.handler.sendEmptyMessage(MySKService.HAVE_NEW_MESSAGE);
                finish();
            }
        } else {
            thankTeacher();
        }
    }

    /**
     * 感谢老师送积分
     *
     * @author Administrator
     */
    class Mydapter extends BaseAdapter {
        ArrayList<Send_Gife> list;
        private ImageView iv_send_picture;
        private TextView tv_send_fen;

        public Mydapter(ArrayList<Send_Gife> list) {
            super();
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int arg0) {
            return list.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        @Override
        public View getView(int arg0, View contentView, ViewGroup arg2) {
            View view = View.inflate(Activity_Thank_Teacher.this, R.layout.send_gif, null);
            iv_send_picture = (ImageView) view.findViewById(R.id.iv_send_picture);
            tv_send_fen = (TextView) view.findViewById(R.id.tv_send_fen);
            imageLoader.displayImage(list.get(arg0).getFaceuri(), iv_send_picture, options);
            tv_send_fen.setText(list.get(arg0).getPoints() + "分");
            return view;
        }
    }

    /**
     * 展示表情包的Adapter
     *
     * @author Administrator
     */
    class MyAdapter extends BaseAdapter {
        ArrayList<T> reslist;

        @SuppressWarnings("unchecked")
        public MyAdapter(ArrayList<Fase_Packs> reslist) {
            super();
            this.reslist = (ArrayList<T>) reslist;
        }

        @Override
        public int getCount() {
            return reslist.size();
        }

        @Override
        public Object getItem(int arg0) {
            return reslist.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        @Override
        public View getView(final int arg0, View arg1, ViewGroup arg2) {
            View view = View.inflate(Activity_Thank_Teacher.this, R.layout.teather_thanks, null);
            ImageView large_img1 = (ImageView) view.findViewById(R.id.large_img1);
            ImageView iv_img1 = (ImageView) view.findViewById(R.id.iv_img1);
            iv_img1.setTag(arg1);
            viewlist.add(iv_img1);
            imageLoader.displayImage(((Fase_Packs) reslist.get(arg0)).getFase_url(), large_img1, options);
            return view;
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}