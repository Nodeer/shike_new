package com.yshow.shike.utils;
import java.util.ArrayList;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.widget.BaseAdapter;
import android.widget.Toast;
/**
 *对题库 文件分类文件夹和文件夹以及单个问题的删除
 * @author Administrator
 *
 */
public class Delete_Ques_Utile {
      private static Delete_Ques_Utile delete_ques = new Delete_Ques_Utile();
      private Delete_Ques_Utile(){}
      public static Delete_Ques_Utile getIntence(){
    	     return delete_ques;
    	  }
	  	/**
	  	 * 刪除 分类
	  	 * @param list
	  	 * @param cid
	  	 * @param position
	  	 * @param adapter
	  	 * @param context
	  	 */
		public <T> void Delete_Classify_File(final ArrayList<T> list, String cid,final int position, final BaseAdapter adapter, final Context context) {
			  		SKAsyncApiController.Delete_File(cid, new MyAsyncHttpResponseHandler(context, true) {
			  			@Override
			  			public void onSuccess(String json) {
			  				super.onSuccess(json);
			  				boolean success = SKResolveJsonUtil.getInstance().resolveIsSuccess(json,context);
			  				if (success) {
			  					list.remove(position);
			  					adapter.notifyDataSetChanged();
			  					Toast.makeText(context, "删除成功", 0).show();
			  			}
			  		}
			  	});
	  	}
	  	/**
	  	 * 刪除 文件夾
	  	 * @param list
	  	 * @param cid
	  	 * @param position
	  	 * @param adapter
	  	 * @param context
	  	 */
		public <T> void Delete_File(final Context context,String questionId,final ArrayList<T> list,final int potion,final BaseAdapter adapter) {
			SKAsyncApiController.Delete_Topic_Resource(questionId,
					new MyAsyncHttpResponseHandler(context, true) {
						@Override
						public void onSuccess(String json) {
							super.onSuccess(json);
							boolean success = SKResolveJsonUtil.getInstance().resolveIsSuccess(json, context);
							if (success) {
								list.remove(potion);
								adapter.notifyDataSetChanged();
								Toast.makeText(context, "删除成功", 0).show();
						}
					}
				});
		}
	  	/**
	  	 * 刪除 文件夾
	  	 * @param list
	  	 * @param cid
	  	 * @param position
	  	 * @param adapter
	  	 * @param context
	  	 */
		public <T> void stu_Delete_File(final Context context,String questionId,final ArrayList<T> list,final int potion,final PagerAdapter adapter) {
			SKAsyncApiController.Delete_Topic_Resource(questionId,
					new MyAsyncHttpResponseHandler(context, true) {
						@Override
						public void onSuccess(String json) {
							super.onSuccess(json);
							boolean success = SKResolveJsonUtil.getInstance().resolveIsSuccess(json, context);
							if (success) {
								list.remove(potion);
								adapter.notifyDataSetChanged();
								PartnerConfig.start_two = true;
								Toast.makeText(context, "删除成功", 0).show();
						}
					}
				});
		}
		/**
		 *删除题里面的每一个资源
		 * @param context
		 * @param id
		 */
	 public void Delete_Res(final Context context,final String id,final ArrayList list,final int potion,final PagerAdapter adapter){
		  SKAsyncApiController.Delete_Topic(id, new MyAsyncHttpResponseHandler(context, true){
			  @Override
			public void onSuccess(String json) {
				super.onSuccess(json);
				boolean success = SKResolveJsonUtil.getInstance().resolveIsSuccess(json, context);
				if(success){
					PartnerConfig.three_indexs.add(id);
					list.remove(potion);
					adapter.notifyDataSetChanged();
					Toast.makeText(context, "删除成功", 0).show();
				}
			}
		  });
	 }
  }
