package com.epsmart.wzdp.activity.more;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.epsmart.wzdp.R;
import com.epsmart.wzdp.db.table.SubmitDateTable;

@SuppressLint("SimpleDateFormat")
public class OffLineDataAdapter extends BaseAdapter {
	private List<SubmitDateTable> list = null;
	private Activity mContext;

	public OffLineDataAdapter(Activity mContext, List<SubmitDateTable> list) {
		this.mContext = mContext;
		this.list = list;
	}

	public void updateListView(List<SubmitDateTable> list) {
		this.list = list;
		notifyDataSetChanged();
	}

	public int getCount() {
		return this.list.size();
	}

	public Object getItem(int position) {
		return list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	@SuppressWarnings("deprecation")
	public View getView(final int position, View view, ViewGroup arg2) {
		ViewHolder viewHolder = null;
		final SubmitDateTable real = list.get(position);
		 if (view == null) {
		viewHolder = new ViewHolder();
		view = LayoutInflater.from(mContext).inflate(R.layout.off_line_listview_item, null);
		viewHolder.title_tv = (TextView) view.findViewById(R.id.title);
		viewHolder.time_tv = (TextView) view.findViewById(R.id.time);
		
		view.setTag(viewHolder);
		 } else {
		 viewHolder = (ViewHolder) view.getTag();
		 }
		 
		 viewHolder.title_tv.setText(real.getMethodName());
		 SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 
		 String title=real.getMethodName();
		 String time=null;
		  Date date=real.getDate();
		 if(date!=null)
			 time=sdf.format(date);
		 viewHolder.title_tv.setText("接口名称："+title+" 数据生成时间："+time);
		 viewHolder.time_tv.setText(real.getParam());
		 
		return view;
	}

	final static class ViewHolder {
		TextView title_tv;
		TextView time_tv;
		

	}
}