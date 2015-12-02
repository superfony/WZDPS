package com.epsmart.wzdp.bean;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.epsmart.wzdp.R;
import com.epsmart.wzdp.activity.supply.bean.Field;

public class ViewBuildSer extends ViewBuilder<WorkOrder> {

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
		convertView.setTag(holder);

		fillData(holder, data);
		return convertView;
	}

	@Override
	public void updateView(View view, int position, WorkOrder data) {
		fillData((ViewHolder) view.getTag(), data);
	}

	private void fillData(ViewHolder holder, WorkOrder workOrder) {
		holder.img.setVisibility(View.INVISIBLE);

		holder.icon.setVisibility(View.VISIBLE);
		holder.icon.setImageResource(R.drawable.provision_service);
		holder.img_end.setVisibility(View.VISIBLE);
		holder.img_end.setImageResource(R.drawable.breaker_end);

		Field field = workOrder.fields.get("purchaseorder");
		holder.title.setText(null == field ? "" : (field.fieldChName + ":"));
		holder.titleValue.setText(null == field ? "" : "" + field.fieldContent);

		field = null;
		field = workOrder.fields.get("suppliername");
		holder.num.setText(null == field ? "" : (field.fieldChName + ":"));
		holder.numValue.setText(null == field ? "" : (field.fieldContent + ""));
		// if(field.fieldChName.contains("行项目")){
		// int info=Integer.valueOf(field.fieldContent);
		// String newinfo = ""+info;
		// holder.numValue.setText(newinfo);
		// }

		field = null;
		field = workOrder.fields.get("servicestate");
		holder.seven.setText(null == field ? "" : (field.fieldChName + ":"));
		holder.sevenValue.setText(null == field ? ""
				: (field.fieldContent + ""));
		//String value=field.fieldContent;
		if(field!=null){
		if ("待审核".equals(field.fieldContent)) {
			holder.img.setVisibility(View.VISIBLE);
			holder.img.setImageResource(R.drawable.check_pending);
		} else if ("已审核".equals(field.fieldContent)) {
			holder.img.setVisibility(View.VISIBLE);
			holder.img.setImageResource(R.drawable.checked);
		} else if ("已完成".equals(field.fieldContent)) {
			holder.img.setVisibility(View.VISIBLE);
			holder.img.setImageResource(R.drawable.wesleybb);
		} else if ("已评价".equals(field.fieldContent)) {
			holder.img.setVisibility(View.VISIBLE);
			holder.img.setImageResource(R.drawable.have_evaluation);
		} else {
			System.out.println("状态不匹配");
		}
		}

		field = null;
		field = workOrder.fields.get("serviceno");
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
		ImageView img;
		ImageView img_end;
		TextView title;
		TextView titleValue;
		TextView num;
		TextView numValue;
		TextView seven;
		TextView sevenValue;
		TextView time;
		TextView timeValue;
		TextView itme;
		TextView itmeValue;
		TextView colour;
		TextView colourValue;

	}

}
