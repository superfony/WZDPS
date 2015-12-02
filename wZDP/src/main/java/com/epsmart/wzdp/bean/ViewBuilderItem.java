package com.epsmart.wzdp.bean;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.epsmart.wzdp.R;
import com.epsmart.wzdp.activity.supply.bean.Field;

public class ViewBuilderItem extends ViewBuilder<WorkOrder> {

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
		holder.time = (TextView) convertView.findViewById(R.id.time);
		holder.timeValue = (TextView) convertView.findViewById(R.id.timeValue);
		holder.itme = (TextView) convertView.findViewById(R.id.itme);
		holder.itmeValue = (TextView) convertView.findViewById(R.id.itmeValue);
		holder.colour = (TextView) convertView.findViewById(R.id.colour);
		holder.colourValue = (TextView) convertView.findViewById(R.id.colourValue);
		holder.count = (TextView) convertView.findViewById(R.id.count);
		holder.countValue = (TextView) convertView.findViewById(R.id.countValue);
		holder.six = (TextView) convertView.findViewById(R.id.six);
		holder.sixValue = (TextView) convertView.findViewById(R.id.sixValue);
		holder.seven = (TextView) convertView.findViewById(R.id.seven);
		holder.sevenValue = (TextView) convertView.findViewById(R.id.sevenValue);
		convertView.setTag(holder);

		fillData(holder, data);
		return convertView;
	}

	@Override
	public void updateView(View view, int position, WorkOrder data) {
		fillData((ViewHolder) view.getTag(), data);
	}

	private void fillData(ViewHolder holder, WorkOrder workOrder) {
		
		holder.colour.setVisibility(View.VISIBLE);
		holder.colourValue.setVisibility(View.VISIBLE);
		holder.count.setVisibility(View.VISIBLE);
		holder.countValue.setVisibility(View.VISIBLE);
		holder.six.setVisibility(View.VISIBLE);
		holder.sixValue.setVisibility(View.VISIBLE);
		
		Field field = workOrder.fields.get("projectname");
		holder.title.setText(null == field ? "" : (field.fieldChName + ":"));
		holder.titleValue.setText(null == field ? "" : "" + field.fieldContent);
		
		field = null;
		field = workOrder.fields.get("eqorder");
		if (null != field) {
			holder.itme.setVisibility(View.VISIBLE);
			holder.itmeValue.setVisibility(View.VISIBLE);
			holder.itme.setText(null == field ? "" : (field.fieldChName + ":"));
			holder.itmeValue.setText(null == field ? "" : (field.fieldContent + ""));
		}
		
		field = null;
		field = workOrder.fields.get("materialtype");//物料类型
		holder.num.setText(null == field ? "" : (field.fieldChName + ":"));
		holder.numValue.setText(null == field ? "" : (field.fieldContent + ""));
		if (field != null) {
			if (field.fieldContent.contains("变压器")) {
				holder.icon.setImageResource(R.drawable.transformer);
				holder.img_end.setVisibility(View.VISIBLE);
				holder.img_end.setImageResource(R.drawable.transformer_end);
			} else if (field.fieldContent.contains("电抗器")) {
				holder.icon.setImageResource(R.drawable.reactor);
				holder.img_end.setVisibility(View.VISIBLE);
				holder.img_end.setImageResource(R.drawable.reactor_end);
			} else if (field.fieldContent.contains("断路器")) {
				holder.icon.setImageResource(R.drawable.breaker);
				holder.img_end.setVisibility(View.VISIBLE);
				holder.img_end.setImageResource(R.drawable.breaker_end);
			} else if (field.fieldContent.contains("组合电器")) {
				holder.icon.setImageResource(R.drawable.combinational);
				holder.img_end.setVisibility(View.VISIBLE);
				holder.img_end.setImageResource(R.drawable.combinationalr_end);
			}else if (field.fieldContent.contains("隔离开关")) {
				holder.icon.setImageResource(R.drawable.isolation);
				holder.img_end.setVisibility(View.VISIBLE);
				holder.img_end.setImageResource(R.drawable.isolation_end);
			} else {
				holder.icon.setVisibility(View.INVISIBLE); 
				holder.img_end.setVisibility(View.INVISIBLE);
			}
		}
		
		field = null;
		field = workOrder.fields.get("voltagegrade");
		holder.colour.setText(null == field ? "" : ("电压等级" + ":"));
		holder.colourValue.setText(null == field ? "" : (field.fieldContent + ""));

		field = null;
		field = workOrder.fields.get("amount");
		holder.count.setText(null == field ? "" : (field.fieldChName  + ":"));
		holder.countValue.setText(null == field ? "" : (field.fieldContent + ""));

		
		field = null;
		field = workOrder.fields.get("suppliername");
		String state = "";
		if (null != field) {
			state = field.fieldContent;
		}

		if (null != field) {
			holder.time.setText(field.fieldChName + ":");
			holder.timeValue.setText(""
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
		TextView time;
		TextView timeValue;
		TextView itme;
		TextView itmeValue;
		TextView colour;
		TextView colourValue;
		TextView count;
		TextView countValue;
		TextView six;
		TextView sixValue;
		TextView seven;
		TextView sevenValue;
	}
}
