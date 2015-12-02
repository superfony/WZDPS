package com.epsmart.wzdp.bean;

import java.io.IOException;

import Decoder.BASE64Decoder;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.epsmart.wzdp.R;
import com.epsmart.wzdp.activity.supply.bean.Field;
/**
 * 主题列表 item
 * @author fony
 */

public class DailyViewBuildOnline extends ViewBuilder<WorkOrder> {
	public OnDelClickListener onDelClick;
	public String userid;

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public void setOnDelClick(OnDelClickListener onDelClick) {
		this.onDelClick = onDelClick;
	}

	@Override
	public View createView(LayoutInflater inflater, int position, WorkOrder data) {
		View convertView = inflater.inflate(R.layout.daily_online_item_bak, null);
		ViewHolder holder = new ViewHolder();
		holder.post_head = (ImageView) convertView.findViewById(R.id.icon);
		holder.titleValue = (TextView) convertView.findViewById(R.id.titleValue);
		holder.numValue = (TextView) convertView.findViewById(R.id.numValue);
		holder.timeValue = (TextView) convertView.findViewById(R.id.timeValue);
		holder.persionname= (TextView) convertView.findViewById(R.id.persionname);
		holder.content=(TextView) convertView.findViewById(R.id.content);
		holder.new_img=(ImageView) convertView.findViewById(R.id.new_img);
		holder.del_btn=(ImageButton) convertView.findViewById(R.id.del_btn);
		convertView.setTag(holder);
		fillData(holder, data);
		return convertView;
	}

	@Override
	public void updateView(View view, int position, WorkOrder data) {
		fillData((ViewHolder) view.getTag(), data);
	}
	
	

	private void fillData(ViewHolder holder, WorkOrder workOrder) {
		
		Field field = workOrder.fields.get("subtitle");
		holder.titleValue.setText(null == field ? "" : "" + field.fieldContent);

		field = null;
		field = workOrder.fields.get("subpersionname");
		holder.persionname.setText(null == field ? "" : (field.fieldContent + ""));
		
		field = null;
		field = workOrder.fields.get("subcomnum");
		holder.numValue.setText(null == field ? "" : ("评论次数："+field.fieldContent + ""));
		
		field = null;
		field = workOrder.fields.get("subcontent");
		holder.content.setText(null == field ? "" : (field.fieldContent + ""));
		
		field = null;
		field = workOrder.fields.get("subtime");
		holder.timeValue.setText(null == field ? "" : (""+field.fieldContent + ""));
		
		field = null;
		field = workOrder.fields.get("subpersionimg");
		String img = field.fieldContent;
		if(TextUtils.isEmpty(img)){
			holder.post_head.setBackgroundResource(R.drawable.wm_icon_admin_normal);
		}else{
			Bitmap bm=stringToBitmap(img);
			 holder.post_head.setImageBitmap(parseRound(bm,60));
		}
		
		field = null;
		field = workOrder.fields.get("subisread");
		String isNew=field.fieldContent;
		if(!TextUtils.isEmpty(isNew)){
			if("0".equals(isNew)){//未读 
				holder.new_img.setVisibility(View.VISIBLE);
				holder.new_img.setBackgroundResource(R.drawable.deal_list_item_status_new);
			}else{
				holder.new_img.setVisibility(View.GONE);
			}
		}
		
		field = null;
		field = workOrder.fields.get("subpersionid");
		String persionid=field.fieldContent;
		
		field = null;
		field = workOrder.fields.get("subid");
		String subid=field.fieldContent;
		
		if(!TextUtils.isEmpty(persionid)){
			if(userid.equals(persionid)){
				holder.del_btn.setVisibility(View.VISIBLE);
			holder.del_btn.setOnClickListener(new OnDelClickListener(subid));
			}else{
				holder.del_btn.setVisibility(View.GONE);
			}
		}
	

	}
	
	public class OnDelClickListener implements OnClickListener{
		private String subid;
		
		public OnDelClickListener() {
		}
		
		public OnDelClickListener(String subid) {
			this.subid=subid;
		}
		
		@Override
		public void onClick(View v) {
			v.setTag(subid);
			onDelClick.onClick(v);
		}
	}
	
	
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

	class ViewHolder {
		TextView titleValue;// 标题
		TextView numValue;// 评论次数
		TextView timeValue;// 时间
		TextView persionname;//人名
		TextView content;//内容
		ImageView post_head;// 头像信息
		ImageView new_img;
		ImageButton del_btn;
	}


}
