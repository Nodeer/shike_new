package com.yshow.shike.utils;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import com.yshow.shike.R;

/**
 * Created by Administrator on 14-8-27.
 * 显示引导浮层的帮助类
 */
public class HelpUtil {
    public static final int HELP_STU_1 = 1;
    public static final int HELP_STU_2 = 2;
    public static final int HELP_STU_3 = 3;
    public static final int HELP_STU_4 = 4;
    public static final int HELP_STU_5 = 5;
    public static final int HELP_STU_6 = 6;
    public static final int HELP_TEA_1 = 7;
    public static final int HELP_TEA_2 = 8;
    public static final int HELP_TEA_3 = 9;
    public static final int HELP_TEA_4 = 10;
    public static final int HELP_TEA_5 = 11;
    public static final int HELP_TEA_6 = 12;
    private static PopupWindow sPopWindow;

    public static void showHelp(Activity context,int type,View root) {
        boolean istest = true;
        if(!istest){
            return;
        }

        FileService fileService = new FileService(context);
        boolean b = fileService.getBoolean("help" + type, false);
        if (b) {//已经显示过了
            return;
        }
        if (sPopWindow == null) {
            sPopWindow = new PopupWindow(WindowManager.LayoutParams.FILL_PARENT, WindowManager.LayoutParams.FILL_PARENT);
            sPopWindow.setFocusable(true);
            sPopWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        View content = context.getLayoutInflater().inflate(R.layout.help_mask_layout, null);
        content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sPopWindow.dismiss();
            }
        });
        ImageView img = (ImageView) content.findViewById(R.id.img);
        switch (type) {
            case HELP_STU_1:
                img.setImageResource(R.drawable.stu_help1);
                break;
            case HELP_STU_2:
                img.setImageResource(R.drawable.stu_help2);
                break;
            case HELP_STU_3:
                img.setImageResource(R.drawable.stu_help3);
                break;
            case HELP_STU_4:
                img.setImageResource(R.drawable.stu_help4);
                break;
            case HELP_STU_5:
                img.setImageResource(R.drawable.stu_help5);
                break;
            case HELP_STU_6:
                img.setImageResource(R.drawable.stu_help6);
                break;
            case HELP_TEA_1:
                img.setImageResource(R.drawable.tea_help1);
                break;
            case HELP_TEA_2:
                img.setImageResource(R.drawable.tea_help2);
                break;
            case HELP_TEA_3:
                img.setImageResource(R.drawable.tea_help3);
                break;
            case HELP_TEA_4:
                img.setImageResource(R.drawable.tea_help4);
                break;
            case HELP_TEA_5:
                img.setImageResource(R.drawable.tea_help5);
                break;
            case HELP_TEA_6:
                img.setImageResource(R.drawable.tea_help6);
                break;
        }
        sPopWindow.setContentView(content);
        fileService.putBoolean(context, "help" + type, true);
        if(root==null){
            sPopWindow.showAtLocation(context.getWindow().getDecorView(), Gravity.NO_GRAVITY, 0, 0);
        }else{
            sPopWindow.showAtLocation(root, Gravity.NO_GRAVITY, 0, 0);
        }
    }
}
