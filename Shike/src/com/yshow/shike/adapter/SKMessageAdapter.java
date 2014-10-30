package com.yshow.shike.adapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.yshow.shike.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yshow.shike.activities.*;
import com.yshow.shike.entity.LoginManage;
import com.yshow.shike.entity.SKAcknowledge_list;
import com.yshow.shike.entity.SKMessage;
import com.yshow.shike.entity.SKMessageList;
import com.yshow.shike.entity.SkMessage_Res;
import com.yshow.shike.entity.SkMessage_Voice;
import com.yshow.shike.fragments.Fragment_Message;
import com.yshow.shike.service.MySKService;
import com.yshow.shike.utils.DateUtils;
import com.yshow.shike.utils.Dialog;
import com.yshow.shike.utils.MyAsyncHttpResponseHandler;
import com.yshow.shike.utils.Net_Servse;
import com.yshow.shike.utils.SKAsyncApiController;
import com.yshow.shike.utils.SKResolveJsonUtil;
import com.yshow.shike.utils.ScreenSizeUtil;
import com.yshow.shike.utils.Send_Message;

/**
 * 消息列表,每一个item中显示了某一个日期下面的消息
 */
public class SKMessageAdapter extends BaseAdapter implements ListAdapter {
    private Context context;
    private ArrayList<SKMessageList> list;
    private DisplayImageOptions s_options, t_options;
    private ImageLoader imageLoader;

    public SKMessageAdapter(Context context, ArrayList<SKMessageList> list) {
        this.context = context;
        this.list = list;
        Net_Servse image_loding = Net_Servse.getInstence();
        s_options = image_loding.Picture_Shipei(R.drawable.my_tea_phon);
        t_options = image_loding.Picture_Shipei(R.drawable.teather_stu_picture);
        imageLoader = ImageLoader.getInstance();
    }

    public void addMordList(ArrayList<SKMessageList> more) {
        list.addAll(more);
        notifyDataSetChanged();
    }

    // 改变删除按钮的标记
    public void Dete_Mess() {
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public SKMessageList getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SKMessageList skMessageList = list.get(position);
        convertView = getMessageList(skMessageList, position);
        return convertView;
    }

    private View getMessageList(SKMessageList skMessageList, int position) {
        View inflate = View.inflate(context, R.layout.sk_message_list, null);
//		TextView date = (TextView) inflate.findViewById(R.id.message_date);
        LinearLayout messageList = (LinearLayout) inflate.findViewById(R.id.message_list);
//		date.setText(skMessageList.getDate());
        ArrayList<SKMessage> sKMessagelist = skMessageList.getList();//这里获取当前日期下的消息列表
        int size = sKMessagelist.size();
        for (int i = 0; i < size; i++) {
            View messageView = getMessageView(sKMessagelist.get(i), position);
            messageList.addView(messageView);
        }
        return inflate;
    }

    /**
     * 获取单条消息的view
     *
     * @param sKMessage
     * @param position
     * @return
     */
    private View getMessageView(final SKMessage sKMessage, final int position) {
        boolean isTeacher = LoginManage.getInstance().isTeacher();
        final MessageHolder viewholer = new MessageHolder();
        View inflate = null;
        if (LoginManage.getInstance().isTeacher()) {
            //老师端
            inflate = View.inflate(context, R.layout.teather_message_list_item, null);
            viewholer.score = (TextView) inflate.findViewById(R.id.tv_small_gift);
            viewholer.delete_bt = (Button) inflate.findViewById(R.id.bt_delete_message);
            viewholer.delete_bt.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    SKMessageList skMessageList = list.get(position);
                    Dele_Mess(sKMessage.getId(), skMessageList.getList(), (Object) sKMessage);
                }
            });
        } else {
            inflate = View.inflate(context, R.layout.sk_message_list_item, null);
            viewholer.delete_bt = (Button) inflate.findViewById(R.id.bt_delete_message);
            viewholer.delete_bt.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    SKMessageList skMessageList = list.get(position);
                    Dele_Mess(sKMessage.getId(), skMessageList.getList(), (Object) sKMessage);
                }
            });
        }
        viewholer.sKMessage = sKMessage;
        viewholer.teacher = (ImageView) inflate.findViewById(R.id.iv_message_teacher);
        viewholer.teacher.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (!LoginManage.getInstance().isTeacher()) {
                    if (sKMessage.getTeachUid().equals("0")) {
                        // 沒人接 顯示自己信息 個人信息id
                        if (!sKMessage.getClaim_uid().equals("0")) {
                            // 顯示 我的老師信息
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("teather_sKMessage", sKMessage);
                            Dialog.intent(context, TeacherInfoActivity.class, bundle);
                        } else {
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("student_sKMessage", sKMessage);
                            Dialog.intent(context, Activity_MyShiKe.class, bundle);
                        }
                    } else {
                        // 在綫老師 直接顯示老師信息
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("teather_sKMessage", sKMessage);
                        Dialog.intent(context, TeacherInfoActivity.class, bundle);
                    }
                } else {
                    // 学生信息
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("teather_sKMessage", sKMessage);
                    Dialog.intent(context, Acticity_Ttudent_Info.class, bundle);
                }
            }
        });
        viewholer.teacherName = (TextView) inflate.findViewById(R.id.tv_message_teacher_name);
        viewholer.messageContent = (TextView) inflate.findViewById(R.id.tv_message_content);
        viewholer.time = (TextView) inflate.findViewById(R.id.tv_message_time);
        viewholer.cancle_bt = (Button) inflate.findViewById(R.id.bt_cancle_message);
        viewholer.updatanum = (TextView) inflate.findViewById(R.id.tv_message_updatanum);
        LinearLayout messageitme = (LinearLayout) inflate.findViewById(R.id.message_item);
        messageitme.setOnClickListener(onclickMessage);
        messageitme.setTag(viewholer);

        LinearLayout messageContentLayout = (LinearLayout) inflate.findViewById(R.id.message_content_layout);
        ViewGroup.LayoutParams pa = messageContentLayout.getLayoutParams();
        pa.width = ScreenSizeUtil.getScreenWidth(context, 1);
        messageContentLayout.setLayoutParams(pa);


        messageItmeContent(viewholer, !isTeacher, position);
        if (!viewholer.sKMessage.getUpdateNum().equals("0") && viewholer.sKMessage.getNewM().equals("1")) {
            viewholer.updatanum.setText(viewholer.sKMessage.getUpdateNum());
            viewholer.updatanum.setVisibility(View.VISIBLE);
//            showMessNum();
        } else {
            viewholer.updatanum.setVisibility(View.GONE);
        }
        return inflate;
    }

//    private void showMessNum() {
//        if (context != null) {
//            if (context instanceof Student_Main_Activity) {
//                ((Student_Main_Activity) context).changeMessNum(true);
//            } else if (context instanceof Teather_Main_Activity) {
//                ((Teather_Main_Activity) context).changeMessNum(true);
//            }
//        }
//    }

    private void messageItmeContent(MessageHolder viewholer, boolean isStudent, int position) {
        String icon = viewholer.sKMessage.getIcon();
        SKAcknowledge_list getsKAcknowledge_list = viewholer.sKMessage.getsKAcknowledge_list();
        ArrayList<SkMessage_Res> res = viewholer.sKMessage.getRes();
        String face = res.get(res.size() - 1).getFace();
        boolean isDone = viewholer.sKMessage.isDone();
        String acknowledge = viewholer.sKMessage.getAcknowledge();
        String claim_uid = viewholer.sKMessage.getClaim_uid();
        String complain = viewholer.sKMessage.getComplain();
        viewholer.time.setText(viewholer.sKMessage.getDate());
        viewholer.teacherName.setText(viewholer.sKMessage.getNickname());

        if (isStudent) {
            //学生端端消息
            imageLoader.displayImage(icon, viewholer.teacher, s_options);
            if (isDone) {
                if (!claim_uid.equals("0")) {
                    if (acknowledge.equals("1")) {
                        viewholer.messageContent.setText("提问结束，您已感谢老师！");
                    } else if (complain.equals("1")) {
                        viewholer.messageContent.setText("提问结束，您已投诉老师！");
                    } else {
                        viewholer.messageContent.setText("提问结束，您还未感谢老师！");
                    }
                } else {
                    viewholer.messageContent.setText("提问超时");
                }
            } else {
                if (claim_uid.equals("0")) {
                    imageLoader.displayImage(icon, viewholer.teacher, s_options);
                    viewholer.cancle_bt.setVisibility(View.VISIBLE);
                    viewholer.cancle_bt.setTag(viewholer.sKMessage);
                    viewholer.cancle_bt.setOnClickListener(cancelMessage);
                }
            }
        } else {
            // 老师端消息
            if (isDone) {
                if (!face.equals("0")) {
                    if (getsKAcknowledge_list != null) {
                        if (!getsKAcknowledge_list.getGift2points().equals("0")) {
                            viewholer.messageContent.setText("学生奖励你了，快去看看！");
                        } else {
                            viewholer.messageContent.setText("学生赞美你啦，快去看看！");
                        }
                    }
                } else if (acknowledge.equals("1") && getsKAcknowledge_list != null) {
                    if (!getsKAcknowledge_list.getGift2points().equals("0")) {
                        viewholer.messageContent.setText("学生奖励你了，快去看看！");
                    } else {
                        viewholer.messageContent.setText("答题结束，谢谢 !");
                    }
                } else {
                    viewholer.messageContent.setText("答题结束，谢谢 !");
                }
            }
            imageLoader.displayImage(icon, viewholer.teacher, t_options);
        }
        if (viewholer.sKMessage.getMsgType().equals("1")) {//这里表示是系统消息
            viewholer.cancle_bt.setVisibility(View.GONE);//系统消息不能撤销
        }
    }

    private OnClickListener cancelMessage = new OnClickListener() {
        @Override
        public void onClick(View v) {
            SKMessage sKMessage = (SKMessage) v.getTag();
            cancelMessage(sKMessage);
        }
    };
    // 点击消息
    private OnClickListener onclickMessage = new OnClickListener() {
        @Override
        public void onClick(View v) {
            boolean isTeacher = LoginManage.getInstance().isTeacher();
            final MessageHolder viewholer = (MessageHolder) v.getTag();
            final String questionId = viewholer.sKMessage.getQuestionId();
            boolean isDone = viewholer.sKMessage.isDone();
            Long updateTime = Long.valueOf(viewholer.sKMessage.getUpdateTime());
            String acknowledge = viewholer.sKMessage.getAcknowledge();
            String claim_uid = viewholer.sKMessage.getClaim_uid();
            String complain = viewholer.sKMessage.getComplain();
            String last_uid = viewholer.sKMessage.getLast_uid();
            String teachUid = viewholer.sKMessage.getTeachUid();
            if (!isTeacher) {
                if (isDone) {
                    if (acknowledge.equals("0") && complain.equals("0")
                            && !claim_uid.equals("0")) {
                        AlertDialog.Builder dia = new Builder(context);
                        dia.setTitle("提示");
                        dia.setMessage("该题已交互完成是否去感谢老师？");
                        dia.setPositiveButton("是",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        acknowledgeTeacher(viewholer.sKMessage);
                                    }
                                });
                        dia.setNegativeButton("否",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        complainTeacher(viewholer.sKMessage);
                                    }
                                });
                        dia.show();
                        return;
                    }
                } else {
                    long currentTimeMillis = System.currentTimeMillis();
                    if ((currentTimeMillis - updateTime * 1000) > 12 * 60 * 1000) {
                        boolean teacherRead = isTeacherRead(claim_uid,
                                viewholer.sKMessage.getRes());
                        if (teacherRead) {
                            if (claim_uid.equals(last_uid)) {
                                AlertDialog.Builder dia = new Builder(context);
                                dia.setTitle("提示");
                                dia.setMessage("你的老师已回答，该题是否解决？");
                                dia.setPositiveButton("继续提问",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                readQuest(viewholer.sKMessage);
                                            }
                                        });
                                dia.setNegativeButton("感谢老师", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        acknowledgeTeacher(viewholer.sKMessage);
                                    }
                                });
                                dia.show();
                                return;
                            }
                        } else {
                            if (!teachUid.equals("0")) {
                                if (!claim_uid.equals("0")) {
                                    AlertDialog.Builder dia = new Builder(context);
                                    dia.setMessage("你的老师可能走开了，是否继续等待？");
                                    dia.setPositiveButton("继续等待", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                        }
                                    });
                                    dia.setNegativeButton(
                                            "结束提问",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    doneMessage(questionId);
                                                }
                                            });
                                    dia.show();
                                    return;
                                } else {
                                    AlertDialog.Builder dia = new Builder(context);
                                    dia.setTitle("提示");
                                    dia.setMessage("你的老师在忙，是否将题目发送给其他在线老师？");
                                    dia.setPositiveButton(
                                            "重新发送",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(
                                                        DialogInterface dialog,
                                                        int which) {
                                                    reSendMessage(viewholer.sKMessage);
                                                }
                                            });
                                    dia.setNegativeButton(
                                            "撤销题目",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(
                                                        DialogInterface dialog,
                                                        int which) {
                                                    cancelMessage(viewholer.sKMessage);
                                                }
                                            });
                                    dia.show();
                                    return;
                                }
                            }
                        }
                    } else if (viewholer.sKMessage.getMsgType().equals("0") && viewholer.sKMessage.getClaim_uid().equals("0") && !LoginManage.getInstance().isTeacher()) {
                        AlertDialog.Builder dia = new Builder(context);
                        dia.setTitle("提示");
                        dia.setMessage("当前还没有老师解答该题是否重发？");
                        dia.setPositiveButton("是",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        reSendMessage(viewholer.sKMessage);
                                    }
                                });
                        dia.setNegativeButton("否", null);
                        dia.show();
                        return;
                    }
                }
            }
            new Send_Message(context).Look_Message(questionId);
            // 查看消息
            readQuest(viewholer.sKMessage);
        }
    };

    // 查看消息
    private void readQuest(SKMessage message) {
        boolean isTeacher = LoginManage.getInstance().isTeacher();
        if (isTeacher) {
            Intent intent = new Intent(context, Tea_Message_Detail_Activity.class);
            intent.putExtra("sKMessage", message);
            intent.putExtra("tag", "2");
            context.startActivity(intent);
            Fragment_Message.handler.sendEmptyMessage(MySKService.HAVE_NEW_MESSAGE);
        } else {
            Intent intent = new Intent(context, Stu_Message_Detail_Activity.class);
            intent.putExtra("sKMessage", message);
            intent.putExtra("tag", "2");
            context.startActivity(intent);
            Fragment_Message.handler.sendEmptyMessage(MySKService.HAVE_NEW_MESSAGE);
        }
    }

    // 感谢老师
    private void acknowledgeTeacher(SKMessage message) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("message", message);
        Dialog.intent(context, AppraiseMainActivity.class, bundle);
    }

    // 投诉老师
    private void complainTeacher(SKMessage message) {
        Log.e("SKMessageAdapter", "投诉老师");
    }

    // 重发消息
    private void reSendMessage(SKMessage message) {
        Log.e("SKMessageAdapter", "重发消息");
        Intent intent = new Intent(context, Activity_Select_Tea.class);
        intent.putExtra("try_messge", true);
        intent.putExtra("message", message);
        context.startActivity(intent);
    }

    // 撤销消息
    private void cancelMessage(SKMessage message) {
        SKAsyncApiController.skCancel_messge(message.getQuestionId(),
                new MyAsyncHttpResponseHandler(context, true) {
                    @Override
                    public void onSuccess(String json) {
                        super.onSuccess(json);
                        boolean isSuccess = SKResolveJsonUtil.getInstance().resolveIsSuccess(
                                json, context);
                        if (isSuccess) {
                            Fragment_Message.handler
                                    .sendEmptyMessage(MySKService.HAVE_NEW_MESSAGE);
                        }
                    }

                });
    }

    // 结束消息
    private void doneMessage(String questionid) {
        SKAsyncApiController.skDonemessge(questionid,
                new MyAsyncHttpResponseHandler(context, true) {
                });
    }

    private boolean isTeacherRead(String claim_uid, ArrayList<SkMessage_Res> res) {
        if (claim_uid.equals("0")) {
            return false;
        }
        for (SkMessage_Res skMessage_Res : res) {
            String isStudent = skMessage_Res.getIsStudent();
            if (!isStudent.equals("0")) {
                return true;
            }
            ArrayList<SkMessage_Voice> voice = skMessage_Res.getVoice();
            for (SkMessage_Voice skMessage_Voice : voice) {
                String isStudent2 = skMessage_Voice.getIsStudent();
                if (!isStudent2.equals("0")) {
                    return true;
                }
            }
        }
        return false;
    }

    class MessageHolder {
        TextView score;
        ImageView teacher;
        TextView teacherName;
        TextView messageContent;
        TextView updatanum;
        TextView time;
        Button delete_bt;
        Button cancle_bt;
        LinearLayout messageitme;
        SKMessage sKMessage;
    }

    private <T> void Dele_Mess(String messge_id, final List<T> list,
                               final Object object) {
        SKAsyncApiController.Dele_Mess(messge_id,
                new MyAsyncHttpResponseHandler(context, true) {
                    @Override
                    public void onSuccess(String json) {
                        super.onSuccess(json);
                        boolean success = SKResolveJsonUtil.getInstance()
                                .resolveIsSuccess(json);
                        if (success) {
                            list.remove(object);
//							notifyDataSetChanged();
                            Fragment_Message.handler.sendEmptyMessage(MySKService.HAVE_NEW_MESSAGE);
//							Toast.makeText(context, "消息删除成功", 0).show();
                        }
                    }
                });
    }
}
