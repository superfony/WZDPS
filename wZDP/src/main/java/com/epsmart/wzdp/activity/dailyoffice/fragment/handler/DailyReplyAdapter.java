package com.epsmart.wzdp.activity.dailyoffice.fragment.handler;

import java.io.IOException;
import java.util.List;

import Decoder.BASE64Decoder;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.epsmart.wzdp.R;

/**
 * 指标列表适配器
 * 
 * @author fony
 */

public class DailyReplyAdapter extends BaseAdapter {
	private List<DailyReply> list;// 数据集合
	private Context mContext;
	private LayoutInflater listContainer;// 视图容器
	private int itemViewResource;// 自定义项视图源

	static class ListItemView { // 自定义控件集合
		public ImageView reply_head;// 回复人头像
		public TextView author;// 回复人名字
		public TextView date;// 回复时间
		public TextView count;// 回复内容
		// public ImageView flag;
	}

	public DailyReplyAdapter(Context mContext, List<DailyReply> list,
			int resource) {
		this.mContext = mContext;
		this.list = list;
		this.listContainer = LayoutInflater.from(mContext); // 创建视图容器并设置上下文
		this.itemViewResource = resource;
	}

	// public void updateListView(List<DailyReply> list) {
	// this.list = list;
	// notifyDataSetChanged();
	// }

	public int getCount() {
		return this.list.size();
		// return 5;
	}

	public Object getItem(int position) {
		return list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View view, ViewGroup arg2) {
		// 自定义视图
		ListItemView listItemView = null;

		if (view == null) {
			// 获取list_item布局文件的视图
			view = listContainer.inflate(this.itemViewResource, null);
			listItemView = new ListItemView();
			// 获取控件对象
			listItemView.reply_head = (ImageView) view
					.findViewById(R.id.daily_reply_img);
			listItemView.author = (TextView) view
					.findViewById(R.id.daily_reply_name);
			listItemView.count = (TextView) view
					.findViewById(R.id.daily_reply_content);
			listItemView.date = (TextView) view
					.findViewById(R.id.daily_reply_time);
			// listItemView.flag = (ImageView) view
			// .findViewById(R.id.news_listitem_flag);
			// 设置控件集到convertView
			view.setTag(listItemView);
		} else {
			listItemView = (ListItemView) view.getTag();
		}
		// 设置文字和图片
		DailyReply daReply = list.get(position);
		String img = daReply.reppersionimg;
		if(TextUtils.isEmpty(img)){
			listItemView.reply_head.setBackgroundResource(R.drawable.wm_icon_admin_normal);;
		}else{
			Bitmap bm = stringToBitmap(daReply.reppersionimg);
			listItemView.reply_head.setImageBitmap(parseRound(bm, 60));
			listItemView.reply_head.setTag(daReply);// 设置隐藏参数(实体类)
		}
		listItemView.author.setText(daReply.reppersionname);
		listItemView.count.setText(daReply.repcontent);
		listItemView.date.setText(daReply.reptime);	

		return view;
	}

	/**
	 * 字符串转图片格式
	 * 
	 * @return
	 */
	public Bitmap stringToBitmap(String imagStr) {
		BASE64Decoder decoder = new BASE64Decoder();
		byte[] bytes = null;
		try {
			bytes = decoder.decodeBuffer(imagStr);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(bytes==null){
			return null;
		}
		return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

	}

	public Bitmap parseRound(Bitmap bitmap, int pixels) {
		Bitmap output = null;
		output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(),
				Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = pixels;
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}

}