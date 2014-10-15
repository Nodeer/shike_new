package com.yshow.shike.adapter;
import java.util.ArrayList;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.yshow.shike.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yshow.shike.db.DatabaseDao;
import com.yshow.shike.entity.LoginManage;
import com.yshow.shike.entity.SKMessage;
import com.yshow.shike.entity.SkMessage_Res;
import com.yshow.shike.entity.SkMessage_Voice;
import com.yshow.shike.utils.Net_Servse;
public class SKQuestsAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<SkMessage_Res> list;
	private DisplayImageOptions options;
	private ImageLoader imageLoader;
	public SKQuestsAdapter(Context context,SKMessage message) {
	this.context = context;
	list = message.getRes();
	options = Net_Servse.getInstence().Picture_Shipei(R.drawable.xiaoxi_moren);
	imageLoader = ImageLoader.getInstance();
   }
	@Override
	public int getCount() {
		return list.size();
	}
	@Override
	public SkMessage_Res getItem(int position) {
		return list.get(position);
	}
	@Override
	public long getItemId(int position) {
		return position;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		SkMessage_Res skMessage_Res = list.get(position);
		ArrayList<SkMessage_Voice> voice = skMessage_Res.getVoice();
		String uid2 = LoginManage.getInstance().getStudent().getUid();
		if (convertView == null) {
			convertView = View.inflate(context, R.layout.sk_question_item, null);
		}
		ImageView imageView = (ImageView) convertView.findViewById(R.id.iv_question_picture);
		ImageView red_small_point = (ImageView) convertView.findViewById(R.id.iv_red_small_point);
		// 对数据语音集合的数据进行判断
		Boolean boolean1 = false;
		for (int i = 0; i < voice.size(); i++) {
			SkMessage_Voice skMessage_Voice = voice.get(i);
			String file = skMessage_Voice.getFile();
			String uid = skMessage_Voice.getUid();
			if(!uid.equals(uid2)){
				String locad_url = new DatabaseDao(context).Query_Condition(file);
				if (locad_url == null) {
					boolean1 = true;
					break;
				}
			}
		}
		if(boolean1){
			red_small_point.setVisibility(View.VISIBLE);
		}else {
			red_small_point.setVisibility(View.GONE);
		}
		boolean teacher = LoginManage.getInstance().isTeacher();
		if(teacher){
			imageView.setBackgroundResource(R.drawable.teather_bg_message_image);
		}
		String file = skMessage_Res.getFile_tub();
		imageLoader.displayImage(file, imageView, options);
		return convertView;
	}
}
