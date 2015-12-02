package com.epsmart.wzdp.bean;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.epsmart.wzdp.R;
import com.epsmart.wzdp.activity.supply.bean.Field;

public class ViewBuildFeed extends ViewBuilder<WorkOrder> {
	private String materialtype = null;

	public void setMaterialtype(String materialtype) {
		this.materialtype = materialtype;
	}

	@Override
	public View createView(LayoutInflater inflater, int position, WorkOrder data) {
		View convertView = inflater.inflate(R.layout.nowwork_content_item_bak,
				null);
		ViewHolder holder = new ViewHolder();
		holder.icon = (ImageView) convertView.findViewById(R.id.icon);
		holder.img = (ImageView) convertView.findViewById(R.id.img);
		holder.img_end = (ImageView) convertView.findViewById(R.id.img_end);
		holder.title = (TextView) convertView.findViewById(R.id.title);
		holder.titleValue = (TextView) convertView
				.findViewById(R.id.titleValue);
		holder.num = (TextView) convertView.findViewById(R.id.num);
		holder.numValue = (TextView) convertView.findViewById(R.id.numValue);
		holder.time = (TextView) convertView.findViewById(R.id.time);
		holder.timeValue = (TextView) convertView.findViewById(R.id.timeValue);
		holder.seven = (TextView) convertView.findViewById(R.id.seven);
		holder.sevenValue = (TextView) convertView
				.findViewById(R.id.sevenValue);
		// holder.colour = (TextView) convertView.findViewById(R.id.colour);
		// holder.colourValue = (TextView)
		// convertView.findViewById(R.id.colourValue);
		convertView.setTag(holder);

		fillData(holder, data);
		return convertView;
	}

	@Override
	public void updateView(View view, int position, WorkOrder data) {
		fillData((ViewHolder) view.getTag(), data);
	}

	private void fillData(ViewHolder holder, WorkOrder workOrder) {
		Field field = workOrder.fields.get("purchaseorder");
		holder.title.setText(null == field ? "" : (field.fieldChName + ":"));
		holder.titleValue.setText(null == field ? "" : "" + field.fieldContent);

		field = null;
		field = workOrder.fields.get("poitem");
		holder.num.setText(null == field ? "" : (field.fieldChName + ":"));
		holder.numValue.setText(null == field ? "" : (field.fieldContent + ""));

		field = null;
		field = workOrder.fields.get("releaseflag");
		holder.seven.setText(null == field ? "" : (field.fieldChName + ":"));
		holder.sevenValue.setText(null == field ? ""
				: (field.fieldContent + ""));
		if (field != null) {
			if ("待审核".equals(field.fieldContent)) {
				holder.img.setVisibility(View.VISIBLE);
				holder.img.setImageResource(R.drawable.check_pending);
				holder.img_end.setVisibility(View.VISIBLE);
				holder.img_end.setImageResource(R.drawable.breaker_end);
			} else if ("已审核".equals(field.fieldContent)) {
				holder.img.setVisibility(View.VISIBLE);
				holder.img.setImageResource(R.drawable.checked);
				holder.img_end.setVisibility(View.VISIBLE);
				holder.img_end.setImageResource(R.drawable.breaker_end);
			} else if ("已处理".equals(field.fieldContent)) {
				holder.img.setVisibility(View.VISIBLE);
				holder.img.setImageResource(R.drawable.processed);
				holder.img_end.setVisibility(View.VISIBLE);
				holder.img_end.setImageResource(R.drawable.breaker_end);
			} else {

			}
			if (materialtype.contains("变压器")) {
				holder.icon.setImageResource(R.drawable.transformer);
				holder.img_end.setVisibility(View.VISIBLE);
				holder.img_end.setImageResource(R.drawable.transformer_end);
			} else if (materialtype.contains("电抗器")) {
				holder.icon.setImageResource(R.drawable.reactor);
				holder.img_end.setVisibility(View.VISIBLE);
				holder.img_end.setImageResource(R.drawable.reactor_end);
			} else if (materialtype.contains("断路器")) {
				holder.icon.setImageResource(R.drawable.breaker);
				holder.img_end.setVisibility(View.VISIBLE);
				holder.img_end.setImageResource(R.drawable.breaker_end);
			} else if (materialtype.contains("组合电器")) {
				holder.icon.setImageResource(R.drawable.combinational);
				holder.img_end.setVisibility(View.VISIBLE);
				holder.img_end.setImageResource(R.drawable.combinationalr_end);
			} else if (materialtype.contains("隔离开关")) {
				holder.icon.setImageResource(R.drawable.isolation);
				holder.img_end.setVisibility(View.VISIBLE);
				holder.img_end.setImageResource(R.drawable.isolation_end);
			} else {
				System.out.println("不存在这个可能");
			}
		}

		field = null;
		field = workOrder.fields.get("issueno");
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
		ImageView img;
		ImageView img_end;
		ImageView icon;
		TextView title;
		TextView titleValue;
		TextView num;
		TextView numValue;
		TextView seven;
		TextView sevenValue;
		TextView time;
		TextView timeValue;

	}

}
