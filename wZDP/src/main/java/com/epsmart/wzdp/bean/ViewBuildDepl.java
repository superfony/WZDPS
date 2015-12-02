package com.epsmart.wzdp.bean;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.epsmart.wzdp.R;
import com.epsmart.wzdp.activity.supply.bean.Field;

public class ViewBuildDepl extends ViewBuilder<WorkOrder> {

	@Override
	public View createView(LayoutInflater inflater, int position, WorkOrder data) {
		View convertView = inflater.inflate(R.layout.nowwork_content_item_bak,
				null);
		ViewHolder holder = new ViewHolder();
		holder.icon = (ImageView) convertView.findViewById(R.id.icon);
		holder.img_end = (ImageView) convertView.findViewById(R.id.img_end);
		holder.title = (TextView) convertView.findViewById(R.id.title);
		holder.titleValue = (TextView) convertView
				.findViewById(R.id.titleValue);
		holder.num = (TextView) convertView.findViewById(R.id.num);
		holder.numValue = (TextView) convertView.findViewById(R.id.numValue);
		holder.itme = (TextView) convertView.findViewById(R.id.itme);
		holder.itmeValue = (TextView) convertView.findViewById(R.id.itmeValue);
		holder.time = (TextView) convertView.findViewById(R.id.time);
		holder.timeValue = (TextView) convertView.findViewById(R.id.timeValue);
		holder.colour = (TextView) convertView.findViewById(R.id.colour);
		holder.colourValue = (TextView) convertView.findViewById(R.id.colourValue);
		holder.six = (TextView) convertView.findViewById(R.id.six);
		holder.sixValue = (TextView) convertView.findViewById(R.id.sixValue);
		convertView.setTag(holder);

		fillData(holder, data);
		return convertView;
	}

	@Override
	public void updateView(View view, int position, WorkOrder data) {
		fillData((ViewHolder) view.getTag(), data);
	}

	private void fillData(ViewHolder holder, WorkOrder workOrder) {

		holder.icon.setVisibility(View.VISIBLE);
		holder.icon.setImageResource(R.drawable.dproject_start);
		holder.img_end.setVisibility(View.VISIBLE);
		holder.img_end.setImageResource(R.drawable.point_end);
		
		holder.colour.setVisibility(View.VISIBLE);
		holder.colourValue.setVisibility(View.VISIBLE);
		holder.six.setVisibility(View.VISIBLE);
		holder.sixValue.setVisibility(View.VISIBLE);

		Field field = workOrder.fields.get("ganth");
		holder.title.setText(null == field ? "" : (field.fieldChName + ":"));
		holder.titleValue.setText(null == field ? "" : "" + field.fieldContent);

		field = null;
		field = workOrder.fields.get("jhhh");
		if (null != field) {
			holder.itme.setVisibility(View.VISIBLE);
			holder.itmeValue.setVisibility(View.VISIBLE);
			holder.itme.setText(null == field ? "" : (field.fieldChName + ":"));
			holder.itmeValue.setText(null == field ? ""
					: (field.fieldContent + ""));
		}

		field = null;
		field = workOrder.fields.get("jhfhsj");
		holder.time.setText(null == field ? "" : (field.fieldChName + ":"));
		holder.timeValue.setText(null == field ? "" : (field.fieldContent + ""));

		field = null;
		field = workOrder.fields.get("biaod");
		holder.num.setText(null == field ? "" : (field.fieldChName + ":"));
		holder.numValue.setText(null == field ? "" : (field.fieldContent + ""));
		field = null;
		field = workOrder.fields.get("jhzcsl");
		holder.colour.setText(null == field ? "" : (field.fieldChName + ":"));
		holder.colourValue.setText(null == field ? "" : (field.fieldContent + ""));

		field = null;
		field = workOrder.fields.get("jhdhsj");
		// String state = "";
		// if (null != field) {
		// state = field.fieldContent;
		// }

		if (null != field) {
			holder.six.setText(field.fieldChName + ":");
			holder.sixValue.setText(""
					+ (null == field.fieldContent
							|| "null".equals(field.fieldContent) ? ""
							: field.fieldContent));
		}
	}

	class ViewHolder {
		ImageView icon;
		ImageView img_end;
		TextView title;
		TextView titleValue;
		TextView num;
		TextView numValue;
		TextView itme;
		TextView itmeValue;
		TextView time;
		TextView timeValue;
		TextView colour;
		TextView colourValue;
		TextView six;
		TextView sixValue;
	}

}
