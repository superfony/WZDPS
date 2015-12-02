package com.epsmart.wzdp.bean;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.epsmart.wzdp.R;
import com.epsmart.wzdp.activity.supply.bean.Field;

public class DailyViewBuildNotice extends ViewBuilder<WorkOrder> {

	@Override
	public View createView(LayoutInflater inflater, int position, WorkOrder data) {
		View convertView = inflater.inflate(R.layout.daily_notice_item_bak,
				null);
		ViewHolder holder = new ViewHolder();
		holder.icon = (ImageView) convertView.findViewById(R.id.icon);
		holder.title = (TextView) convertView.findViewById(R.id.title);
		holder.titleValue = (TextView) convertView
				.findViewById(R.id.titleValue);
		holder.num = (TextView) convertView.findViewById(R.id.num);
		holder.numValue = (TextView) convertView.findViewById(R.id.numValue);
		holder.time = (TextView) convertView.findViewById(R.id.time);
		holder.timeValue = (TextView) convertView.findViewById(R.id.timeValue);
		convertView.setTag(holder);

		fillData(holder, data);
		return convertView;
	}

	@Override
	public void updateView(View view, int position, WorkOrder data) {
		fillData((ViewHolder) view.getTag(), data);
	}

	private void fillData(ViewHolder holder, WorkOrder workOrder) {

		Field field = workOrder.fields.get("noticetime");
		// holder.title.setText(null == field ? "" : (field.fieldChName + ":"));
		holder.titleValue.setText(null == field ? "" : "" + field.fieldContent);

		field = null;
		field = workOrder.fields.get("noticetitle");
		holder.num.setText(null == field ? "" : (field.fieldChName + ":"));
		holder.numValue.setText(null == field ? "" : (field.fieldContent + ""));

		field = null;
		field = workOrder.fields.get("Infostate");
		String isRead = field.fieldContent;
		if (!TextUtils.isEmpty(isRead)) {
			if ("0".equals(isRead)) {// 未读
				holder.icon.setBackgroundResource(R.drawable.daily_notice);
			} else {
				holder.icon.setBackgroundResource(R.drawable.daily_noticed);
			}
		}

		field = null;
		field = workOrder.fields.get("noticedetail");
		String state = "";
		if (null != field) {
			state = field.fieldContent;
		}

		if (null != field) {
			holder.time.setText(field.fieldChName + ":");
			String str = field.fieldContent;
			if (!TextUtils.isEmpty(str) && str.contains("*#*")) {
				str = str.replace("*#*", "\n\r");
				holder.timeValue.setText(str);
			} else {
				holder.timeValue.setText(str);
			}
			// holder.timeValue.setText(""
			// + (null == field.fieldContent
			// || "null".equals(field.fieldContent) ? ""
			// : field.fieldContent));
		}
	}

	class ViewHolder {
		ImageView icon;
		TextView title;
		TextView titleValue;
		TextView num;
		TextView numValue;
		TextView time;
		TextView timeValue;
		TextView itme;
		TextView itmeValue;
		TextView colour;
		TextView colourValue;
	}
}
