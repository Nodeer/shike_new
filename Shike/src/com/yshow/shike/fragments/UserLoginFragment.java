package com.yshow.shike.fragments;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import com.yshow.shike.R;
import com.yshow.shike.UIApplication;
import com.yshow.shike.activities.Forget_Pass_Activity;
import com.yshow.shike.activities.Student_Main_Activity;
import com.yshow.shike.activities.Teather_Main_Activity;
import com.yshow.shike.entity.LoginManage;
import com.yshow.shike.entity.SKStudent;
import com.yshow.shike.utils.Dialog;
import com.yshow.shike.utils.FileService;
import com.yshow.shike.utils.MyAsyncHttpResponseHandler;
import com.yshow.shike.utils.SKAsyncApiController;
import com.yshow.shike.utils.SKResolveJsonUtil;
public class UserLoginFragment extends Fragment implements OnClickListener {
	private CheckBox isRemember;
	private AutoCompleteTextView username_edit;
	private EditText password_edit;
	private String username;
	private String password;
	private Context context;
	private TextView tv_pasword;
	private FileService fileService; //实例化业务对象
	private ArrayAdapter<String> adapter;
	private boolean checked = false;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		context = getActivity();
		View view = inflater.inflate(R.layout.sk_fragment_userlogin, null);
		fileService = new FileService(context);
		username_edit = (AutoCompleteTextView) view.findViewById(R.id.login_name_auto_edit);
        String name = fileService.getSp_Date("name");
        if(!TextUtils.isEmpty(name)){
            username_edit.setText(name);
        }
		password_edit = (EditText) view.findViewById(R.id.login_pwd_edit);
        String password = fileService.getSp_Date("pass");
        if(!TextUtils.isEmpty(password)){
            password_edit.setText(password);
        }
		isRemember = (CheckBox) view.findViewById(R.id.isremember);
		view.findViewById(R.id.tv_register).setOnClickListener(this);;
		tv_pasword = (TextView) view.findViewById(R.id.tv_pasword);
		// 用户名回显
		SharedPreferences sp = context.getSharedPreferences("shared_prefs", Context.MODE_WORLD_READABLE);
		String str_name = sp.getString("savename", "");
		String[] auto_array = str_name.split(",");
		adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_dropdown_item_1line, auto_array);
		username_edit.setAdapter(adapter);
		isRemember.setOnClickListener(this);
		tv_pasword.setOnClickListener(this);
		return view;
	}
	// 用户名和密码判断  修改
	public boolean checkRegister() {
		username = username_edit.getText().toString().trim();
		password = password_edit.getText().toString().trim();
		if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
			return false;
		}
        fileService.saveAddString("savename",username);
		return true;
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_register:
//            String s = null;
//            System.out.println(s);
            if (checkRegister()) {
				skLogin();
			}
			break;
		case R.id.tv_pasword:
			Dialog.Intent(getActivity(), Forget_Pass_Activity.class);
		case R.id.isremember:
			checked = isRemember.isChecked();
			break;
		}
	}
	/**
	 * 登录成功后记住密码
	 */
	private void skLogin() {
		SKAsyncApiController.skLogin(username, password,
				new MyAsyncHttpResponseHandler(getActivity(),true) {
					@Override
					public void onSuccess(String json) {
						super.onSuccess(json);
						boolean success = SKResolveJsonUtil.getInstance().resolveIsSuccess(json, context);
						if (success) {
							LoginManage instance = LoginManage.getInstance();
							instance.setmLoginSuccess(true);
							SKStudent student = SKResolveJsonUtil.getInstance().resolveLoginInfo(json);
							instance.setStudent(student);
							fileService.putBoolean("is_tea", student.getTypes().equals("1"));
							Bundle bundle = new Bundle();
							//如果不是老师
							if (!LoginManage.getInstance().isTeacher(context)) {
								//是学生
								UIApplication.getInstance().isTestUser = false;
								Dialog.Intent(getActivity(),Student_Main_Activity.class);
							}else{
								//是老师
								Dialog.Intent(getActivity(),Teather_Main_Activity.class);
							}
							if(checked){
                                fileService.saveString("name",username);
                                fileService.saveString("pass",password);
							}
                            fileService.saveString("autologin_name",username);
                            fileService.saveString("autologin_pass",password);
							getActivity().finish();
						}
					}
				});
	}
}